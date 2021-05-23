import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

allprojects {
    repositories {
        jcenter()
    }
}

plugins {
    kotlin("jvm") version "1.4.10"
    kotlin("kapt") version "1.4.10"
    id("at.phatbl.shellexec") version "1.5.1"
}

subprojects {
    extra["dockerImage"] = "${this.rootProject.name}-${this.project.name}"
}
