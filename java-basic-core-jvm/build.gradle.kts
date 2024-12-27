plugins {
    java
    application
}

application {
    mainClass.set("net.dunice.intensive.basics.UserGreeter")
}

dependencies {
    implementation(libs.spring.boot)
    implementation(libs.spring.jpa)
    implementation(libs.spring.validation)
    runtimeOnly(libs.spring.h2)
}
