plugins {
    java
    id("org.springframework.boot")
}

group = "fr.mnhn"
version = "0.0.1-SNAPSHOT"

java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform(project(":platform")))
    developmentOnly(platform(project(":platform")))

    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("com.ninja-squad:DbSetup")

    developmentOnly("org.springframework.boot:spring-boot-devtools")

    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("org.flywaydb:flyway-core")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("com.squareup.okhttp3:mockwebserver")
}

tasks.withType<Test> {
    useJUnitPlatform()
    // On CI, Gitlab will spin a Postgres service on host "postgres"
    if (project.findProperty("CI") != null) {
        systemProperty("diversity.database.host-and-port", "postgres:5432")
    }
}

tasks.bootJar {
    archiveFileName.set("diversity.jar")
}
