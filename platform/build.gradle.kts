plugins {
    `java-platform`
}

repositories {
    mavenCentral()
}

javaPlatform {
    allowDependencies()
}

val jwtVersion = "0.11.2"

dependencies {
    api(platform("org.springframework.boot:spring-boot-dependencies:2.4.0"))

    constraints {
        api("com.ninja-squad:DbSetup:2.1.0")
        api("com.ninja-squad:DbSetup-kotlin:2.1.0")
        api("io.jsonwebtoken:jjwt-api:$jwtVersion")
        api("io.jsonwebtoken:jjwt-impl:$jwtVersion")
        api("io.jsonwebtoken:jjwt-jackson:$jwtVersion")
    }
}
