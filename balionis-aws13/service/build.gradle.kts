import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("application")
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation(Libs.KOTLIN_LOGGING)
    implementation(Libs.LOGBACK_CLASSIC)

    implementation(Libs.CONFIG4K)

    testImplementation(Libs.KOTEST_RONNER_JUNIT5)
    testImplementation(Libs.KOTEST_ASSERTIONS_CORE)
    testImplementation(kotlin("test-junit"))
}

tasks.withType<AbstractArchiveTask> {
    archiveBaseName.set(rootProject.name + "-service")
}

application {
    mainClassName = "com.balionis.aws13.service.AppLambdaHandlerKt"

    // FIXME: this does not work!
    applicationDistribution.exclude("**/logback*.xml")
}

tasks.withType<ShadowJar> {
    mergeServiceFiles {
        // FIXME: this does not work!
        exclude("**/logback*.xml")
    }
}
