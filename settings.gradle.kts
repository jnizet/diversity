pluginManagement {
    plugins {
        id( "org.springframework.boot") version "2.3.3.RELEASE"
        id("com.github.node-gradle.node") version "2.2.4"
    }
}

rootProject.name = "diversity"

include("platform")
include("backend")
include("e2e")
