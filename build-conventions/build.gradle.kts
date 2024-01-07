plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.plugin.detekt)
    implementation(libs.plugin.kotlin)
    implementation(libs.plugin.spotless)
    implementation(libs.plugin.versions)
    implementation(libs.plugin.versions.update)
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}
