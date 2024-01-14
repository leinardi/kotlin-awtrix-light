/*
 * Copyright 2024 Roberto Leinardi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

plugins {
    alias(libs.plugins.kotlin.jvm)
    application
    alias(libs.plugins.kotlinx.serialization)
    id("detekt-conventions")
}

kotlin {
    jvmToolchain(libs.versions.java.get().toInt())
}

application {
    mainClass.set("com.leinardi.kal.MainKt")
}

dependencies {
    implementation(libs.bundles.kmqtt.jvm)
    implementation(libs.bundles.kotlin.logging.jvm)
    implementation(libs.coroutines.core)
    implementation(libs.kodein)
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlin.result)
    implementation(libs.kotlin.result.coroutines)
    implementation(libs.kotlinx.serialization.json)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.kotlin.test.junit5)
}

tasks {
    withType<Test> {
        useJUnitPlatform()
    }

    named<JavaExec>("run") {
        description = "Runs the MQTT broker"
    }

    compileJava {
        options.compilerArgs.add("-Xlint:deprecation")
        options.compilerArgs.add("-Xlint:unchecked")
    }
}
