plugins {
    id("com.gradle.plugin-publish") version "1.2.1"
    `embedded-kotlin`
}

group = "com.uselessmnemonic"
version = "1.0-SNAPSHOT"

gradlePlugin {
    website = "https://github.com/UselessMnemonic/nso-gradle"
    vcsUrl = "https://github.com/UselessMnemonic/nso-gradle.git"
    plugins {
        create("nso-gradle") {
            id = "com.uselessmnemonic.nso-gradle"
            implementationClass = "com.uselessmnemonic.gradle.nso.NsoPlugin"
            displayName = "NSO Plugin"
            description = "Plugin supporting Cisco NSO packages"
            tags = listOf("nso", "cisco")
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            pom {
                name = "NSO Gradle Plugin"
                description = "Plugin supporting Cisco NSO packages"
                url = "https://github.com/UselessMnemonic/nso-gradle"
                developers {
                    developer {
                        id = "UselessMnemonic"
                        name = "Christopher Madrigal"
                        email = "chrisjmadrigal@gmail.com"
                    }
                }
                scm {
                    connection = "scm:git:git://github.com/UselessMnemonic/nso-gradle.git"
                    url = "https://github.com/UselessMnemonic/nso-gradle"
                }
            }
        }
    }
}
