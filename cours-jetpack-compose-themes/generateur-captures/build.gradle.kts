plugins {
    kotlin("jvm") version "1.9.22"
    id("org.jetbrains.compose") version "1.5.12"
    application
}
dependencies {
    implementation(compose.desktop.currentOs)
    implementation(compose.material3)
    implementation(compose.materialIconsExtended)
}
kotlin { jvmToolchain(21) }
application { mainClass.set("MainKt") }
configurations.all { exclude(group = "androidx.annotation") }
