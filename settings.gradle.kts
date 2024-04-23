rootProject.name = "CloudCoverUSA2"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://jogamp.org/deployment/maven") // for webview
        maven("https://maven.pkg.jetbrains.space/kotlin/p/wasm/experimental")  // ktor experimental
    }
}

include(":composeApp")
//include(":shared")
