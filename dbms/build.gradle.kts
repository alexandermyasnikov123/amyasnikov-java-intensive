plugins {
    java
    application
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
}

application {
    mainClass = "net.dunice.intensive.dbms.DbmsApplication"
}

group = "net.dunice.intensive"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(libs.spring.data)
    implementation(libs.spring.boot)
    implementation(libs.spring.validation)

    runtimeOnly(libs.spring.h2)
}
