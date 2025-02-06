plugins {
    java
    application
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
}

application {
    mainClass = "net.dunice.intensive.brokers_microservices.BrokerMicroServicesApplication"
}

dependencies {
    implementation(libs.bundles.spring.web) {
        exclude(group = libs.spring.data.get().group, module = libs.spring.data.get().module.name)
    }

    runtimeOnly(libs.spring.h2)
}
