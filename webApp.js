const loadApp = () => {
     const simpleWasmModule = new Uint8Array([
        0,  97, 115, 109,   1,   0,   0,  0,   1,   8,   2,  95,
        1, 120,   0,  96,   0,   0,   3,  3,   2,   1,   1,  10,
       14,   2,   6,   0,   6,  64,  25, 11,  11,   5,   0, 208,
      112,  26,  11,   0,  45,   4, 110, 97, 109, 101,   1,  15,
        2,   0,   5, 102, 117, 110,  99, 48,   1,   5, 102, 117,
      110,  99,  49,   4,   8,   1,   0,  5, 116, 121, 112, 101,
       48,  10,  11,   1,   0,   1,   0,  6, 102, 105, 101, 108,
      100,  48
        ]);

    const hasSupportOfAllRequiredWasmFeatures = () =>
        typeof WebAssembly !== "undefined" &&
        typeof WebAssembly?.validate === "function" &&
        WebAssembly.validate(simpleWasmModule);

    const createScript = (src) => {
        const script = document.createElement("script");
        script.src = src;
        script.type = "application/javascript";
        return script;
    }

    document.body.appendChild(createScript(hasSupportOfAllRequiredWasmFeatures() ? "originWasmWebApp.js" : "originJsWebApp.js"));
}

if (document.readyState === "loading") {
    document.addEventListener("DOMContentLoaded", loadApp);
} else {
    loadApp();
}