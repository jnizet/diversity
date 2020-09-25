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
    annotationProcessor(platform(project(":platform")))
    developmentOnly(platform(project(":platform")))

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("com.ninja-squad:DbSetup")
    implementation("io.jsonwebtoken:jjwt-api")

    developmentOnly("org.springframework.boot:spring-boot-devtools")

    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("org.flywaydb:flyway-core")
    runtimeOnly("io.jsonwebtoken:jjwt-impl")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("com.squareup.okhttp3:mockwebserver")
}

tasks {
    withType<Test> {
        useJUnitPlatform()
        // On CI, Gitlab will spin a Postgres service on host "postgres"
        if (project.findProperty("CI") != null) {
            systemProperty("diversity.database.host-and-port", "postgres:5432")
        }
    }

    bootJar {
        dependsOn(":frontend:assemble")
        dependsOn(":admin:assemble")
        archiveFileName.set("diversity.jar")
        bootInf {
            into("classes/static") {
                from(project(":frontend").file("build/dist"))
                from(project(":admin").file("dist"))
            }
        }
    }

    bootRun {
        workingDir = rootDir
        args = listOf("--spring.profiles.active=dev")
    }
}
