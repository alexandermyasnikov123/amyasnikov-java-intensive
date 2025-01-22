package net.dunice.buildSrc

import org.gradle.jvm.toolchain.JavaLanguageVersion

object ProjectConstants {
    val JAVA_23_VERSION = JavaLanguageVersion.of(23)

    const val ARCHIVE_NAME = "app.jar"

    const val COPY_LOMBOK_CONFIG = "copyLombokConfig"

    const val MAIN_CLASS_ATTRIBUTE = "Main-Class"

    const val LOGBACK_GROUP = "ch.qos.logback"

    const val GRADLE_COPY_CONFIG_NAME = "copyDockerEnvToProperties"

    fun findLombokConfig(projectDir: String) = "$projectDir/lombok.config"
}