pluginManagement {
    repositories {
        maven {
            url = uri("https://maven-central.storage-download.googleapis.com/maven2/")
        }
        gradlePluginPortal()
    }
}

rootProject.name = "example-graphql-java"
include("app")
