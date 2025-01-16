@file:Suppress("UnstableApiUsage")

rootProject.name = "amyasnikov-java-intensive"

dependencyResolutionManagement {
    repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
    repositories {
        mavenCentral()
    }
}
include("java-basic-core-jvm")
include("java-concurrency")
