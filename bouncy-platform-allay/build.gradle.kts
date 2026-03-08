import org.gradle.jvm.tasks.Jar

plugins {
    `java-library`
    id("org.allaymc.gradle.plugin") version "0.2.1"
}

group = "miroshka.allay"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

allay {
    api = "0.25.0"
    plugin {
        name = "BouncyBlocksMX"
        version = project.version.toString()
        description = project.description.toString()
        entrance = "miroshka.allay.BouncyBlocksAllayPlugin"
        authors += "Miroshka"
    }
}

dependencies {
    implementation(project(":bouncy-core"))
    implementation("eu.okaeri:okaeri-configs:5.0.13")
    implementation("eu.okaeri:okaeri-configs-yaml-snakeyaml:5.0.13")
}

afterEvaluate {
    tasks.named<Jar>("shadowJar") {
        dependsOn(project(":bouncy-core").tasks.named("jar"))
        archiveClassifier.set("")
    }

    tasks.named<Jar>("jar") {
        enabled = false
    }

    tasks.named("assemble") {
        dependsOn(tasks.named("shadowJar"))
    }
}
