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
    runtimeOnly(libs.spring.postgres)
}
