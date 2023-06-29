plugins {
    java
    signing
    `java-library`
    `maven-publish`
    kotlin("jvm")
}

apply(from = "../repositories.gradle")

val archivesBaseName = "protocol"
group = "dev.arbjerg.lavalink"

java {
    targetCompatibility = JavaVersion.VERSION_11
    sourceCompatibility = JavaVersion.VERSION_11

    withJavadocJar()
    withSourcesJar()
}

dependencies {
    compileOnly(libs.lavaplayer)
    implementation(libs.kotlin.stdlib.jdk8)
    implementation(libs.jackson.module.kotlin)
}

val isGpgKeyDefined = findProperty("signing.gnupg.keyName") != null

publishing {
    publications {
        create<MavenPublication>("Protocol") {
            from(project.components["java"])

            pom {
                name.set("Lavalink Protocol")
                description.set("Protocol for Lavalink Client development")
                url.set("https://github.com/lavalink-devs/lavalink")

                licenses {
                    license {
                        name.set("The MIT License")
                        url.set("https://github.com/lavalink-devs/Lavalink/blob/master/LICENSE")
                    }
                }

                developers {
                    developer {
                        id.set("freyacodes")
                        name.set("Freya Arbjerg")
                        url.set("https://www.arbjerg.dev")
                    }
                }

                scm {
                    connection.set("scm:git:ssh://github.com/lavalink-devs/lavalink.git")
                    developerConnection.set("scm:git:ssh://github.com/lavalink-devs/lavalink.git")
                    url.set("https://github.com/lavalink-devs/lavalink")
                }
            }
        }
    }
}

if (isGpgKeyDefined) {
    signing {
        sign(publishing.publications["Protocol"])
        useGpgCmd()
    }
}
