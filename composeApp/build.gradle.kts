import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.plugin.extraProperties
import java.util.Properties

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
        }
        androidMain.dependencies {
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.ktor.client.android)

            // Video
//            implementation("androidx.media3:media3-exoplayer:1.1.0")
//            implementation("androidx.media3:media3-exoplayer-dash:1.1.0")
//            implementation("androidx.media3:media3-ui:1.1.0")
            implementation(libs.androidx.media3.exoplayer)
            implementation(libs.androidx.media3.exoplayer.dash)
            implementation(libs.androidx.media3.ui)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
//            implementation(compose.desktop.macos_arm64)
            implementation(compose.desktop.common)
            implementation(libs.ktor.client.java)
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
    }
    dependencies {
        debugImplementation(libs.compose.ui.tooling)
    }
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
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb, TargetFormat.Pkg)
            modules("java.instrument", "jdk.unsupported")

//            packageName = "com.realityexpander.cloudcoverusa2"
            packageName = "Cloud Cover USA 2"
            description = "Cloud Cover USA 2"
            packageVersion = "1.0.0"
            copyright = "© 2014-2024 Chris Athanas. All rights reserved."
            vendor = "Chris Athanas"

            includeAllModules = true

            macOS {
                packageBuildVersion = "10"
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
                    """.trimIndent()
                }

                signing {
                    val props: Properties = Properties()
                    props.load(project.file("./../local.properties").inputStream())
                    sign.set(true)
                    identity.set(props.getProperty("SIGNING_IDENTITY"))
                }

                notarization {
                    val props: Properties = Properties()
                    props.load(project.file("./../local.properties").inputStream())
                    appleID.set(props.getProperty("NOTARIZATION_APPLE_ID"))
                    password.set(props.getProperty("NOTARIZATION_PASSWORD"))
                    teamID.set(props.getProperty("NOTARIZATION_TEAM_ID"))
                }

                // Add libraries to the runtime
                includeAllModules = true

                provisioningProfile.set(project.file("embedded.provisionprofile"))
                runtimeProvisioningProfile.set(project.file("runtime.provisionprofile"))
                entitlementsFile.set(project.file("entitlements.plist"))
                runtimeEntitlementsFile.set(project.file("runtime-entitlements.plist"))
            }
        }
    }
}


















