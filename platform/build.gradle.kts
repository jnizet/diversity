plugins {
    `java-platform`
}

repositories {
    mavenCentral()
}

javaPlatform {
    allowDependencies()
}

dependencies {
    api(platform("org.springframework.boot:spring-boot-dependencies:2.3.3.RELEASE"))

    constraints {
        api("com.ninja-squad:DbSetup:2.1.0")
        api("com.ninja-squad:DbSetup-kotlin:2.1.0")
    }
}
