plugins {
    kotlin("jvm") version "1.7.10"
    `java-gradle-plugin`
    id("com.gradle.plugin-publish") version "1.0.0"
}

group = "com.uselessmnemonic"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

gradlePlugin {
    plugins {
        create("nsoGradle") {
            id = "com.uselessmnemonic.nso-gradle"
            implementationClass = "com.uselessmnemonic.nso.gradle.NsoGradle"
        }
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
