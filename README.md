# Cloud View USA 2.0
Shows current cloud cover for North America, Mexico and Canada

- KMP Application for iOS, Android and Desktop
- Source data from the Space Science https://www.ssec.wisc.edu/

This is a Kotlin Multiplatform project targeting Android, iOS & Desktop.

## Android Screenshots
<img width="300" src="https://github.com/realityexpander/CloudCoverUSA2/assets/5157474/2e61a455-5f26-42d8-b7dc-75da0ace24b8">
<img width="600" src="https://github.com/realityexpander/CloudCoverUSA2/assets/5157474/19e11983-16ad-4115-a2c2-7ecd903e4a57">

## iOS Screenshots
<img width="300" src="https://github.com/realityexpander/CloudCoverUSA2/assets/5157474/bde7f6ca-e3db-41f1-91c1-bf940e4a4be3">
<img width="600" src="https://github.com/realityexpander/CloudCoverUSA2/assets/5157474/c4531bd7-bca1-4e9c-a403-241630c09cc9">

## Desktop Screenshots
<img width="912" alt="Screenshot 2024-04-16 at 11 46 50 PM" src="https://github.com/realityexpander/CloudCoverUSA2/assets/5157474/92b9d692-cfaf-4f3f-950d-7bb130afd457">
<img width="912" alt="Screenshot 2024-04-16 at 11 47 01 PM" src="https://github.com/realityexpander/CloudCoverUSA2/assets/5157474/7dbdd5d5-b459-4d3e-97d9-ba6a4fdf1249">


* `/composeApp` is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - `commonMain` is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    `iosMain` would be the right folder for such calls.

* `/iosApp` contains iOS applications. Even if you’re sharing your UI with Compose Multiplatform, 
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.


Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…
