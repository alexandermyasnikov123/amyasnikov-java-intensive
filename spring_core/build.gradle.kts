plugins {
    java
    application
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
}

application {
    mainClass = "net.dunice.intensive.spring_core.SpringCoreApplication"
}

group = "net.dunice.intensive.spring_core"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(libs.spring.boot)
    implementation(libs.spring.aop)
    testImplementation(libs.spring.test)
}
