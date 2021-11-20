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
    id("org.jlleitschuh.gradle.ktlint") version "9.4.1"
    id("jacoco")
}

val kotlinProjects = setOf(
    "service",
    "assembly"
)

subprojects {

    if (kotlinProjects.contains(this.project.name)) {
        apply {
            plugin("kotlin")
            plugin("kotlin-kapt")
            plugin("org.jlleitschuh.gradle.ktlint")
            plugin("jacoco")
        }

        ktlint {
            enableExperimentalRules.set(true)
        }

        tasks.withType<Test> {
            useJUnitPlatform()
            finalizedBy(tasks.jacocoTestReport)
        }

        jacoco {
            toolVersion = "0.8.6"
        }

        tasks.jacocoTestReport {
            reports {
                xml.isEnabled = true
                csv.isEnabled = false
            }
        }
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "11"
    }
}
