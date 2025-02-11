import net.dunice.buildSrc.ProjectConstants

plugins {
    java
    application
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
}

application {
    mainClass = "net.dunice.intensive.spring_boot.SpringIntensiveApplication"
}

group = "net.dunice.intensive"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(libs.bundles.spring.web)
    implementation(libs.spring.eureka.client)
    runtimeOnly(libs.spring.postgres)

    implementation(libs.mapstruct)
    annotationProcessor(libs.mapstruct.apt)

    implementation(libs.liquibase)
    implementation(libs.thumbnailator)

    testImplementation(libs.spring.test)
}

tasks.build {
    dependsOn(tasks.findByName(ProjectConstants.GRADLE_COPY_CONFIG_NAME))
}

tasks.register(ProjectConstants.GRADLE_COPY_CONFIG_NAME) {
    description = "Copies the docker environment variables to the hidden.properties file"

    doLast {
        copy {
            val basePath = "./src/main/resources/"
            from("$basePath/hidden.env").into(basePath)
            rename("hidden.env", "hidden.properties")
        }
    }
}
