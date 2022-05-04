pluginManagement {
    plugins {
        id( "org.springframework.boot") version "2.6.7"
    }
}

rootProject.name = "diversity"

include("platform")
include("backend")
include("frontend")
include("admin")
include("e2e")
