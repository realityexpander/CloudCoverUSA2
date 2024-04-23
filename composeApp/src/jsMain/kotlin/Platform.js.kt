class JSPlatform: Platform {
	override val name: String = "Web with Kotlin/JS"
}

actual fun getPlatform(): Platform = JSPlatform()