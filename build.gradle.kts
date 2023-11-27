plugins {
    kotlin("jvm") version "1.9.21"
}

repositories {
    mavenCentral()
}

tasks {
    wrapper {
        gradleVersion = "8.4"
    }
}
