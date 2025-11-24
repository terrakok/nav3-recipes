# Nav3Play

Original: https://github.com/android/nav3-recipes

Web demo: https://terrakok.github.io/nav3-recipes/

**Since Web Integration doesn't support Browser History for now, use the Esc button to navigate back**

https://github.com/user-attachments/assets/3acf291a-203b-4b06-8491-f3a0bdfdd799

### Android
To run the application on android device/emulator:  
 - open project in Android Studio and run imported android run configuration  

To build the application bundle:  
 - run `./gradlew :androidApp:assembleDebug`  
 - find `.apk` file in `androidApp/build/outputs/apk/debug/androidApp-debug.apk`  

### Desktop
Run the desktop application: `./gradlew :desktopApp:run`  

### iOS
To run the application on iPhone device/simulator:  
 - Open `iosApp/iosApp.xcproject` in Xcode and run standard configuration  
 - Or use [Kotlin Multiplatform Mobile plugin](https://plugins.jetbrains.com/plugin/14936-kotlin-multiplatform-mobile) for Android Studio  

### Web Distribution
Build web distribution: `./gradlew :webApp:composeCompatibilityBrowserDistribution`  
Deploy a dir `webApp/build/dist/composeWebCompatibility/productionExecutable` to a web server  

### JS Browser
Run the browser application: `./gradlew :webApp:jsBrowserDevelopmentRun`  

### Wasm Browser
Run the browser application: `./gradlew :webApp:wasmJsBrowserDevelopmentRun`  

