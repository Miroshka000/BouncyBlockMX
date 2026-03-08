plugins {
    base
}

group = "miroshka"
version = "1.5.0"
description = "Bounce blocks plugin with platform adapters for Nukkit and AllayMC"

subprojects {
    version = rootProject.version
    description = rootProject.description

    repositories {
        mavenCentral()
        maven("https://repo.opencollab.dev/main/")
    }
}
