import net.dunice.buildSrc.ProjectConstants

plugins {
    java
    `kotlin-dsl`
}

version = "1.0-SNAPSHOT"

allprojects {
    group = "net.dunice.intensive"

    plugins.withType<JavaPlugin> {
        apply<KotlinDslPlugin>()

        java {
            version = ProjectConstants.JAVA_VERSION
        }

        dependencies {
            compileOnly(libs.lombok)
            annotationProcessor(libs.lombok)

            testCompileOnly(libs.lombok)
            testAnnotationProcessor(libs.lombok)

            testImplementation(platform(libs.junit.bom))
            testImplementation(libs.junit.jupiter)
        }

        tasks.test {
            useJUnitPlatform()
        }
    }
}

subprojects {
    plugins.withType<JavaPlugin> {
        tasks.compileJava {
            doFirst {
                copyLombokConfig(project)
            }
        }
    }
}

tasks.register(ProjectConstants.COPY_LOMBOK_CONFIG) {
    group = "lombok"
    description = "Copy the lombok config to all the subprojects"

    doLast {
        subprojects.forEach(::copyLombokConfig)
    }
}

fun copyLombokConfig(project: Project) {
    copy {
        from(ProjectConstants.findLombokConfig(rootDir.path)).into(project.projectDir)
    }
}
