##---------------Begin: proguard configuration for Pusher Java Client  ----------
-dontwarn org.slf4j.impl.StaticLoggerBinder
-dontwarn org.slf4j.MDC
-dontwarn org.slf4j.MarkerFactory

## VLCJ
## https://github.com/Kotlin/kotlinx.coroutines/issues/4025
#-keep class kotlinx.coroutines.internal.MainDispatcherFactory { *; }
#-keep class kotlinx.coroutines.swing.SwingDispatcherFactory { *; }

## WebView
## https://github.com/KevinnZou/compose-webview-multiplatform/blob/main/README.desktop.md
-keep class org.cef.** { *; }
-keep class kotlinx.coroutines.swing.SwingDispatcherFactory

#-dontwarn org.cef.**
#-dontwarn jogamp.**
#-dontwarn com.jogamp.**
#-dontwarn org.apache.**
#-dontwarn okhttp3.**
#-dontwarn com.multiplatform.**
#-dontwarn com.sun.**
-dontwarn **

##---------------End: proguard configuration for Pusher Java Client  ----------