import org.gradle.api.file.DuplicatesStrategy
import org.gradle.jvm.tasks.Jar

plugins {
    `java-library`
}

group = "miroshka.nukkit"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

dependencies {
    implementation(project(":bouncy-core"))
    compileOnly("cn.nukkit:nukkit:1.0-SNAPSHOT")
    testImplementation(platform("org.junit:junit-bom:5.10.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.processResources {
    filesMatching("plugin.yml") {
        expand("version" to project.version.toString())
    }
}

tasks.named<Jar>("jar") {
    dependsOn(project(":bouncy-core").tasks.named("jar"))
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from({
        configurations.runtimeClasspath.get()
                .filter { it.exists() }
                .map { if (it.isDirectory) it else zipTree(it) }
    })
}

tasks.test {
    useJUnitPlatform()
}
