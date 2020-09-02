pluginManagement {
    plugins {
        id( "org.springframework.boot") version "2.3.3.RELEASE"
    }
}

rootProject.name = "diversity"

include("platform")
include("backend")
