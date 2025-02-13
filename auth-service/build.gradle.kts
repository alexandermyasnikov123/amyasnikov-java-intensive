import net.dunice.buildSrc.ProjectConstants

plugins {
    java
    application
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
}

application {
    mainClass = "net.dunice.intensive.auth_service.AuthServiceApplication"
}

dependencies {
    implementation(libs.bundles.spring.web) {
        libs.spring.openapi.get().let { openApi ->
            exclude(group = openApi.group, module = openApi.module.name)
        }
    }
    implementation(libs.spring.security)
    implementation(libs.jwt.jose4j)

    implementation(libs.spring.eureka.client)
    implementation(libs.liquibase)

    runtimeOnly(libs.spring.postgres)
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
