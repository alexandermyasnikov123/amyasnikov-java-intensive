import net.dunice.buildSrc.ProjectConstants

plugins {
    java
    `kotlin-dsl`
}

group = "net.dunice.intensive"
version = "1.0-SNAPSHOT"

allprojects {
    plugins.withType<JavaPlugin> {
        plugins.apply(KotlinDslPlugin::class)

        java {
            version = ProjectConstants.JAVA_VERSION
        }
    }
}

dependencies {
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
}

tasks.test {
    useJUnitPlatform()
}
