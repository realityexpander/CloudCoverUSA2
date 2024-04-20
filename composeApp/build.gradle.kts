import org.jetbrains.compose.desktop.application.dsl.TargetFormat
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
                jvmTarget = "11"
//                jvmTarget = "17"
            }
        }
    }
    
    jvm("desktop")

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

            implementation(libs.ktor.core)
            implementation(libs.ktor.contentNegotiation)
            implementation(libs.ktor.serialization)
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor)

            implementation(libs.slf4j.nop)

            // for vlcj - necessary?
            implementation(libs.kotlinx.coroutines.core)

            // for webview
            api("io.github.kevinnzou:compose-webview-multiplatform:1.9.2")

        }
        androidMain.dependencies {
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.ktor.client.android)

            // Video
            implementation(libs.androidx.media3.exoplayer)
            implementation(libs.androidx.media3.exoplayer.dash)
            implementation(libs.androidx.media3.ui)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(compose.desktop.common)
            implementation(libs.ktor.client.java)

            // for vlcj
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.vlcj)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
    }
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
        versionCode = 1
        versionName = "1.0"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
//        sourceCompatibility = JavaVersion.VERSION_17
//        targetCompatibility = JavaVersion.VERSION_17
    }
    dependencies {
        debugImplementation(libs.compose.ui.tooling)
    }
//    kotlin {
//        jvmToolchain(17)
//    }
}
dependencies {
    implementation(libs.androidx.media3.session)
}

// Setup for distribution: https://github.com/JetBrains/compose-multiplatform/blob/master/tutorials/Signing_and_notarization_on_macOS/README.md#configuring-entitlements
compose.desktop {
    application {
        mainClass = "MainKt"
        // Built with JDK's Corretto 18.0.2 & Corretto 20.0.1
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
                packageBuildVersion = "21"
                dockName = "Cloud Cover USA"
                bundleID = "com.realityexpander.cloudcoverusa2"
                appStore = true
                appCategory = "public.app-category.weather"
                minimumSystemVersion = "12.0"

                iconFile.set(project.file("icon.icns"))
                infoPlist {
                    extraKeysRawXml = """
                        <key>ITSAppUsesNonExemptEncryption</key>
                        <false/>
                        <key>NSAppDataUsageDescription</key>
                        <string>Cloud Cover USA 2 requires access to the internet to download weather data.</string>
                        <key>com.apple.security.files.user-selected.read-write</key>
                        <true/>
                        <key>com.apple.security.files.downloads.read-write</key>
                        <true/>
                        <key>com.apple.security.network.client</key>
                        <true/>
                    """.trimIndent()
                }

                signing {
                    val props: Properties = Properties()
                    props.load(project.file("./../local.properties").inputStream())
                    identity.set(props.getProperty("SIGNING_IDENTITY"))
                    sign.set(true)
                }

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
