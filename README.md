# Cloud Cover USA 2
Shows current cloud cover for North America, Mexico and Canada

- KMP Application for iOS, Android and Desktop
- Source data from the Space Science https://www.ssec.wisc.edu/

This is a Kotlin Multiplatform project targeting Android, iOS & Desktop.

To run iOS & Android, select from the Run Configuration menu.

To run desktop:
`./gradlew composeApp:run`

## Android Screenshots
<img width="300" src="https://github.com/realityexpander/CloudCoverUSA2/assets/5157474/2e61a455-5f26-42d8-b7dc-75da0ace24b8">
<img width="600" src="https://github.com/realityexpander/CloudCoverUSA2/assets/5157474/19e11983-16ad-4115-a2c2-7ecd903e4a57">

## iOS Screenshots
<img width="300" src="https://github.com/realityexpander/CloudCoverUSA2/assets/5157474/bde7f6ca-e3db-41f1-91c1-bf940e4a4be3">
<img width="600" src="https://github.com/realityexpander/CloudCoverUSA2/assets/5157474/c4531bd7-bca1-4e9c-a403-241630c09cc9">

## Desktop Screenshots
<img width="912" src="https://github.com/realityexpander/CloudCoverUSA2/assets/5157474/92b9d692-cfaf-4f3f-950d-7bb130afd457">
<img width="912" src="https://github.com/realityexpander/CloudCoverUSA2/assets/5157474/7dbdd5d5-b459-4d3e-97d9-ba6a4fdf1249">


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

## TestFlight
- Upload using Transporter app
- https://appstoreconnect.apple.com/apps/6498872435/testflight/macos
- Download using TestFlight app

## Last commit built with:
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


