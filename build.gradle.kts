@file:Suppress("UnstableApiUsage")

import net.dunice.buildSrc.ProjectConstants

plugins {
    java
    application
    `kotlin-dsl`
}

version = "1.0-SNAPSHOT"

application {
    mainClass = "net.dunice.intensive.Program"
}

allprojects {
    group = "net.dunice.intensive"

    plugins.withType<JavaPlugin> {
        apply<KotlinDslPlugin>()

        java {
            toolchain {
                languageVersion.set(ProjectConstants.JAVA_23_VERSION)
            }
            sourceCompatibility = JavaVersion.VERSION_23
            targetCompatibility = JavaVersion.VERSION_23
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

    plugins.withType<ApplicationPlugin> {
        tasks.jar {
            enabled = !plugins.hasPlugin(libs.plugins.spring.boot.get().pluginId)

            archiveFileName.set(ProjectConstants.ARCHIVE_NAME)
            manifest.attributes(ProjectConstants.MAIN_CLASS_ATTRIBUTE to application.mainClass.get())
        }
    }

    configurations {
        all {
            exclude(group = ProjectConstants.LOGBACK_GROUP)
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
