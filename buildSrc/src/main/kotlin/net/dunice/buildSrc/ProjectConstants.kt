package net.dunice.buildSrc

import org.gradle.jvm.toolchain.JavaLanguageVersion

object ProjectConstants {
    val JAVA_VERSION = JavaLanguageVersion.of(23)

    const val ARCHIVE_NAME = "app.jar"

    const val COPY_LOMBOK_CONFIG = "copyLombokConfig"

    fun findLombokConfig(projectDir: String) = "$projectDir/lombok.config"
}