plugins {
    java
    application
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
}

application {
    mainClass.set("net.dunice.intensive.basics.UserGreeter")
}

dependencies {
    implementation(libs.bundles.spring.web)
    runtimeOnly(libs.spring.h2)
}
