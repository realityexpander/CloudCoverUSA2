import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig
import java.util.*

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
    }

    jvmToolchain(17)
    jvm("desktop")

    js {
        moduleName = "app"
        binaries.executable()
        browser {
            useCommonJs()
            commonWebpackConfig {
                outputFileName = "$moduleName.js"
            }
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        val desktopMain by getting

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.compose.material.icons.extended)

            implementation(libs.ktor.core)
            implementation(libs.ktor.contentNegotiation)
            implementation(libs.ktor.serialization)
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor)

            implementation(libs.slf4j.nop) // prevent silly warning that's been there for years

            // for vlcj - necessary?
            implementation(libs.kotlinx.coroutines.core)
        }
        androidMain.dependencies {
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.ktor.client.android)

            // Video
            implementation(libs.androidx.media3.exoplayer)
            implementation(libs.androidx.media3.exoplayer.dash)
            implementation(libs.androidx.media3.ui)

            implementation(libs.github.compose.webview.multiplatform)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(compose.desktop.common)
            implementation(libs.ktor.client.java)
            implementation(libs.coil.network.ktor)

            // for vlcj
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.vlcj)

            implementation(libs.github.compose.webview.multiplatform)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
            implementation(libs.github.compose.webview.multiplatform)
        }
        jsMain.dependencies {
            // js target has problems using fetch from js, always gives CORS issues.
            implementation(libs.ktor.client.js)
            implementation(compose.html.core) // Required for Compose Web/Canvas on JS
        }
    }
}

compose.experimental {
    web.application {}
}

android {
    namespace = "com.realityexpander.cloudcoverusa2"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "com.realityexpander.cloudcoverusa2"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 4
        versionName = "1.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    dependencies {
        debugImplementation(libs.compose.ui.tooling)

        // Splash Screen
        implementation(libs.androidx.core.splashscreen)
        debugImplementation(libs.androidx.core.splashscreen)
    }
    kotlin {
        jvmToolchain(17)
    }
}
dependencies {
    implementation(libs.androidx.media3.session)
}

// Setup for distribution: https://github.com/JetBrains/compose-multiplatform/blob/master/tutorials/Signing_and_notarization_on_macOS/README.md#configuring-entitlements
compose.desktop {
    application {
        mainClass = "MainKt"

        // Set in IntelliJ IDEA -> Settings...-> Build, Execution, Deployment -> Build Tools -> Gradle -> Gradle JVM
        // Built with JDK Corretto 18.0.2
        // Built with JDK Corretto 20.0.1
        // Built with Temurin 20.0.2
//        javaHome = System.getProperty("java.home")

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Pkg) // TargetFormat.Msi, TargetFormat.Deb,
            modules("java.instrument", "jdk.unsupported")

            packageName = "Cloud Cover USA 2"
            description = "Cloud Cover USA 2"
            packageVersion = "1.0.0"
            copyright = "© 2014-2024 Chris Athanas. All rights reserved."
            vendor = "Chris Athanas"

            includeAllModules = true

            macOS {
                packageBuildVersion = "28"
                appStore = true // make true for Mac App Store, false for external distribution
                dockName = "Cloud Cover USA"
                bundleID = "com.realityexpander.cloudcoverusa2"
                appCategory = "public.app-category.weather"
                minimumSystemVersion = "12.0" // available in compose-plugin 1.6.10-dev1584

                iconFile.set(project.file("icon.icns"))
                infoPlist {
                    extraKeysRawXml = """
                        <key>ITSAppUsesNonExemptEncryption</key>
                        <false/>
                        <key>NSAppDataUsageDescription</key>
                        <string>Cloud Cover USA 2 requires access to the internet to download weather data.</string>
                        <key>com.apple.security.files.user-selected.read-write</key>
                        <true/>
                        <key>com.apple.security.network.client</key>
                        <true/>
                    """.trimIndent()
                }

                // signing requires appStore = true
                signing {
                    val props: Properties = Properties()
                    props.load(project.file("./../local.properties").inputStream())
                    identity.set(props.getProperty("SIGNING_IDENTITY"))
                    sign.set(true)
                }

                // notarizing requires appStore = false
                notarization {
                    val props: Properties = Properties()
                    props.load(project.file("./../local.properties").inputStream())
                    appleID.set(props.getProperty("NOTARIZATION_APPLE_ID"))
                    password.set(props.getProperty("NOTARIZATION_PASSWORD"))
                    teamID.set(props.getProperty("NOTARIZATION_TEAM_ID"))
                }

                provisioningProfile.set(project.file("embedded.provisionprofile"))
                runtimeProvisioningProfile.set(project.file("runtime.provisionprofile"))
                entitlementsFile.set(project.file("entitlements.plist"))
                runtimeEntitlementsFile.set(project.file("runtime-entitlements.plist"))
            }
        }

        buildTypes.release.proguard {
            version.set("7.4.2")
            isEnabled.set(false)
            configurationFiles.from(file("proguard/proguard-rules.pro"))
            optimize.set(false)
            obfuscate.set(false)
        }

        // For Webview
        jvmArgs("--add-opens", "java.desktop/sun.awt=ALL-UNNAMED")
        jvmArgs("--add-opens", "java.desktop/java.awt.peer=ALL-UNNAMED")
        if (System.getProperty("os.name").contains("Mac")) {
            jvmArgs("--add-opens", "java.desktop/sun.awt=ALL-UNNAMED")
            jvmArgs("--add-opens", "java.desktop/sun.lwawt=ALL-UNNAMED")
            jvmArgs("--add-opens", "java.desktop/sun.lwawt.macosx=ALL-UNNAMED")
        }
    }
}

// Publish to GitHub Pages
// Task to copy from ./composeApp/build/dist/wasmJs/productionExecutable to ./docs
tasks.register("copyJsToDocs") {
    group = "build"
    doLast {
//        val wasmJsDir = project.file("./build/dist/Js/productionExecutable")
        val wasmJsDir = project.file("./build/dist/Js/developmentExecutable")
        val docsDir = file(rootDir.path + "/docs")
        wasmJsDir.copyRecursively(docsDir, overwrite = true)
    }
}
//tasks.getByName("jsBrowserDistribution").finalizedBy("copyJsToDocs")
tasks.getByName("jsBrowserDevelopmentWebpack").finalizedBy("copyJsToDocs")


// Task to clean ./docs (docs is the dir that will be published to GitHub Pages)
tasks.register("cleanDocs") {
    group = "build"
    doLast {
        val docsDir = file(rootDir.path + "/docs")
        docsDir.deleteRecursively()
    }
}
tasks.getByName("clean").dependsOn("cleanDocs")

// From: https://youtrack.jetbrains.com/issue/KT-67603
//project.afterEvaluate {
//    tasks.withType<XCFrameworkTask>().forEach { task ->
//        task.doLast {
//            val xcframework = task.outputs.files.files.first().toPath()
//            val iosFrameworks = Files.find(xcframework, 2, { path, _ ->
//                val isFramework = path.fileName.endsWith(task.baseName.get() + ".framework")
//                val destination = path.getName(path.count() - 2).fileName.toString()
//                val isIOS = destination.startsWith("ios-")
//                isFramework && isIOS
//            })
//
//            for (framework in iosFrameworks) {
//                project.copy {
//                    from(project.file("assets/PrivacyInfo.xcprivacy"))
//                    into(framework)
//                }
//            }
//        }
//    }
//}
