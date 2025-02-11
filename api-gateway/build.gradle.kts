plugins {
    java
    application
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
}

application {
    mainClass = "net.dunice.intensive.api_gateway.GatewayApplication"
}

group = "net.dunice.intensive.api_gateway"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(libs.spring.api.gateway)
    implementation(libs.spring.eureka.client)
    implementation(libs.spring.feign)
}
