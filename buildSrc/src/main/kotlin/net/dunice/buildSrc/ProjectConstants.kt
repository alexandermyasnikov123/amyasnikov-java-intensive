package net.dunice.buildSrc

import org.gradle.api.JavaVersion

object ProjectConstants {

    val JAVA_VERSION = JavaVersion.VERSION_23

    const val COPY_LOMBOK_CONFIG = "copyLombokConfig"

    fun findLombokConfig(projectDir: String) = "$projectDir/lombok.config"
}