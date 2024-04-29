# Cloud Cover USA 2
Shows current cloud cover for North America, Mexico and Canada

[<img src="app_publishing/app_store.png" width="150">](https://apps.apple.com/us/app/cloud-cover-usa-2i/id6499233780)
[<img src="app_publishing/play_store.png" width="150">](https://play.google.com/store/apps/details?id=com.realityexpander.cloudcoverusa2)

- KMP Application for iOS, Android and Desktop (Apple Chip only for now)
- Source data from the Space Science and Engineering Center at University of Wisconsin-Madison https://www.ssec.wisc.edu/

This is a Kotlin Multiplatform project targeting Android, iOS & Desktop.

To run iOS & Android, select from the Run Configuration menu. 
Note: Recommend running iOS and Android from Android Studio.

- Note: Recommend running desktop from IntelliJ
To run desktop:
`./gradlew composeApp:run`

## Android Screenshots
<img width="300" src="https://github.com/realityexpander/CloudCoverUSA2/assets/5157474/b4d648a8-d694-4468-b63b-73de6ccd165a">
<img width="600" src="https://github.com/realityexpander/CloudCoverUSA2/assets/5157474/97c29961-72fe-4d72-bd55-9e9b212bc3b1">

## iOS Screenshots
<img width="300" src="https://github.com/realityexpander/CloudCoverUSA2/assets/5157474/bc72685a-20bb-4541-908d-d8c4e091aad8">
<img width="600" src="https://github.com/realityexpander/CloudCoverUSA2/assets/5157474/c3fa61ed-16c9-4215-a87d-b06da60b482e">

## Desktop Screenshots
<img width="912" src="https://github.com/realityexpander/CloudCoverUSA2/assets/5157474/92b9d692-cfaf-4f3f-950d-7bb130afd457">
<img width="912" src="https://github.com/realityexpander/CloudCoverUSA2/assets/5157474/7dbdd5d5-b459-4d3e-97d9-ba6a4fdf1249">

## Bug Report
- Re: Deploaying MacOS Desktop App to Apple App Store: https://github.com/JetBrains/compose-multiplatform/issues/4712 

## Create Icons for MacOS
- make a folder `iconMaker` in the root of the project
- Make a 1024x1024 PNG image, call it `Icon1024.png`
- copy the PNG image to the folder,
- run the following command from inside the folder:
```bash
mkdir icon.iconset
sips -z 16 16     Icon1024.png --out icon.iconset/icon_16x16.png
sips -z 32 32     Icon1024.png --out icon.iconset/icon_16x16@2x.png
sips -z 32 32     Icon1024.png --out icon.iconset/icon_32x32.png
sips -z 64 64     Icon1024.png --out icon.iconset/icon_32x32@2x.png
sips -z 128 128   Icon1024.png --out icon.iconset/icon_128x128.png
sips -z 256 256   Icon1024.png --out icon.iconset/icon_128x128@2x.png
sips -z 256 256   Icon1024.png --out icon.iconset/icon_256x256.png
sips -z 512 512   Icon1024.png --out icon.iconset/icon_256x256@2x.png
sips -z 512 512   Icon1024.png --out icon.iconset/icon_512x512.png
cp Icon1024.png icon.iconset/icon_512x512@2x.png
iconutil -c icns icon.iconset
rm -R icon.iconset
```
- Copy the `icon.icns` file to the `composeApp` folder.
- Copy the `Icon1024.png` to the `composeApp/src/commonMain/composeResources/drawable` folder.

# Check notarization status

### Check main (not release)
```bash
# Check the app
spctl -a -vvv -t install 'composeApp/build/compose/binaries/main/app/Cloud Cover USA 2.app' -v
```
```bash
# Check the dmg
spctl -a -vvv -t install 'composeApp/build/compose/binaries/main/dmg/Cloud Cover USA 2-1.0.0.dmg' -v
```
```bash
# Check the pkg
spctl -a -vvv -t install 'composeApp/build/compose/binaries/main/pkg/Cloud Cover USA 2-1.0.0.pkg' -v
```

### Check main-release
```bash
# Check the release app
spctl -a -vvv -t install 'composeApp/build/compose/binaries/main-release/app/Cloud Cover USA 2.app' -v
```

```bash
# Check the release dmg
spctl -a -vvv -t install 'composeApp/build/compose/binaries/main-release/dmg/Cloud Cover USA 2-1.0.0.dmg' -v
```

### Show security cert
```bash
security cms -D -i composeApp/build/compose/binaries/main/app/Cloud\ Cover\ USA\ 2.app/Contents/embedded.provisionprofile
```
### Show Entitlements
```bash
codesign --display --entitlements -dv --verbose=4 'composeApp/build/compose/binaries/main/app/Cloud Cover USA 2.app'
```
### Check Apple System Status
- https://developer.apple.com/system-status/

### Check Signing/Validation Resources
- https://dev.to/ajpagente/how-to-check-if-a-macos-app-is-notarized-8p4#:~:text=You%20can%20check%20if%20a,source%20indicates%20Unnotarized%20Developer%20ID.
- https://developer.apple.com/forums/thread/130560
- https://developer.apple.com/forums/thread/706442
- https://developer.apple.com/forums/thread/701514
- https://forums.developer.apple.com/forums/thread/675354

## Java Versions for Gradle Builds

### Show java version
```bash
/usr/libexec/java_home -V  # List all versions
```

### Change java version
```bash
export JAVA_HOME=`/usr/libexec/java_home -v 21`  # Change to version 21
```

### Fetch Tester (JS - for debug console)
`fetch("https://wsrv.nl/?url=https://plus.unsplash.com/premium_photo-1661438314870-d819b854b58e&w=300").then( (s)=> s.text() ).then( (s)=>console.log(s) )`


## TestFlight
- Upload using Transporter app
- https://appstoreconnect.apple.com/apps/6498872435/testflight/macos
- Download using TestFlight app

## Publishing App
- Free Privacy Generator: https://app.freeprivacypolicy.com/download/a0f10c87-ac25-471a-94bb-85ef6a659811
- App Icon Generator (for iOS & Android formats)
  - https://www.appicon.co/#image-sets


# Dev target notes:

### Target Android
  - Works as expected.

### Target iOS
  - Run `Project > Archive` in XCode
  - `Verify` and `Check Privacy` in Organizer
  - Upload to App Store Connect
    - Wait for processing
  - Check release in TestFlight, roll out to internal testers, production

### Target desktop MacOS build (CURRENTLY NOT DISTRIBUTABLE VIA APPLE APP STORE)
  - Bump Build version in `composeApp/build.gradle.kts`
  - `./gradlew composeApp:packagePkg`
  - Upload via Transporter
  - Wait to appear in App Store Connect (may take 2-3 min to appear, 3-5 to process)
  - Check release in TestFlight

  - **IMPORTANT**: The `VLCJ` Video Player and `WebView` Java libraries crash when accessed at runtime for apps deployed to the 
    Apple App Store. So desktop target for MacOS is currently broken, and I filed a bug report at jetbrains:
    - https://github.com/JetBrains/compose-multiplatform/issues/4712
 
### Target JS (CURRENTLY DOES NOT LOAD IMAGES)
  - Runs fine except the images will not load via Coil3's `AsyncImage`. 
    - Images served from the local server work fine
    - Images served from other open-CORS sites works fine
    - Images served via proxy DO NOT work (?)
  - Workarounds require research:
    - Try another library.
    - Set option headers(?)


## Last commit built with:
```
* Android Studio Koala | 2023.3.2 Canary 2
* Build #AI-233.14475.28.2332.11606850, built on March 21, 2024
* Runtime version: 17.0.10+0-17.0.10b1087.21-11572160 aarch64
* VM: OpenJDK 64-Bit Server VM by JetBrains s.r.o.
* macOS 14.4.1
* GC: G1 Young Generation, G1 Old Generation
* Memory: 4096M
* Cores: 10
* Metal Rendering is ON
* Registry:
* ide.tooltip.initialDelay=812
* ide.browser.jcef.gpu.disable=true
* debugger.new.tool.window.layout=true
* analyze.exceptions.on.the.fly=true
* ide.experimental.ui=true
* Non-Bundled Plugins:
* com.c5inco.modifiers (1.0.15)
* wu.seal.tool.jsontokotlin (3.7.4)
* com.wakatime.intellij.plugin (14.3.13)
* com.github.airsaid.androidlocalize (3.0.0)
* DBN (3.4.3211.0)
* net.seesharpsoft.intellij.plugins.csv (3.3.0-233)
* com.jetbrains.kmm (0.8.2(233)-8)
* com.squareup.sqldelight (2.0.2)
* com.developerphil.adbidea (1.6.15)
* org.jetbrains.compose.desktop.ide (1.6.1)
* by.overpass.svg-to-compose-intellij (0.14)
* com.github.copilot (1.5.2.5345)
* mobi.hsz.idea.gitignore (4.5.3)
* com.abeade.plugin.figma.import (1.0.8)
```
* XCode Version 15.3 (15E204a)



## Original Instructions
* `/composeApp` is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - `commonMain` is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    `iosMain` would be the right folder for such calls.

* `/iosApp` contains iOS applications. Even if you’re sharing your UI with Compose Multiplatform, 
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…


