plugins {
    java
    id("org.springframework.boot")
    id("com.google.cloud.tools.jib") version "3.1.4"
    id("red.sukun1899.wanko") version "1.0.0"
}

buildscript {
    dependencies {
        classpath("com.google.cloud.tools:jib-spring-boot-extension-gradle:0.1.0")
        classpath("org.postgresql:postgresql:42.3.5")
    }
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
    implementation("org.commonmark:commonmark")
    implementation("io.jsonwebtoken:jjwt-api")

    developmentOnly("org.springframework.boot:spring-boot-devtools")

    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("org.flywaydb:flyway-core")
    runtimeOnly("io.jsonwebtoken:jjwt-impl")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
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

    register("generateSecretKey", JavaExec::class) {
        description = "Generates a secret key to populate the diversity.security.secret-key property"
        group = "application"
        classpath = sourceSets.main.get().runtimeClasspath
        mainClass.set("fr.mnhn.diversity.admin.security.JwtHelper")
    }

    register("hashPassword", JavaExec::class) {
        description = "Salts and hashes a user password passed with `-Ppassword=...`"
        group = "application"
        classpath = sourceSets.main.get().runtimeClasspath
        mainClass.set("fr.mnhn.diversity.admin.security.PasswordHasher")
        val argumentProvider = CommandLineArgumentProvider {
            listOf(((project.findProperty("password") ?: throw GradleException("No property password found. Use -Ppassword=...")) as String))
        }
        argumentProviders.add(argumentProvider)
    }

    jib {
        from {
            image = "adoptopenjdk:11-jre-openj9"
        }
        to {
            image = "outils-patrinat.mnhn.fr/biom-" + project.properties["dockerEnvironmentTag"] as String?
            tags = setOf("latest")
            auth {
                username = project.properties["dockerUsername"] as String?
                password =  project.properties["dockerPassword"] as String?
            }
        }
        container {
            jvmFlags = listOf("-Xmx4g", "-Xms128m", "-Xscmx128m", "-Xscmaxaot100m", "-Xshareclasses:cacheDir=/opt/shareclasses")
            mainClass = "fr.mnhn.diversity.DiversityApplication"
        }
        setAllowInsecureRegistries(true)
        pluginExtensions {
            pluginExtension {
                implementation = "com.google.cloud.tools.jib.gradle.extension.springboot.JibSpringBootExtension"
            }
        }
        extraDirectories {
            paths{
                 path {
                     setFrom(project(":frontend").file("build/dist"))
                     into = "/app/classes/static"
                 }
                 path {
                     setFrom(project(":admin").file("dist"))
                     into = "/app/classes/static"
                 }
            }
        }
    }

    register("insertTestData") {
        group = "application"
        dependsOn("wankoRun")
        description = "Inserts the test data located in database/testing/test-data.sql"
    }
}

wanko {
    url = "jdbc:postgresql://localhost:5432/diversity"
    user = "diversity"
    password = "diversity"
    driverClassName = "org.postgresql.Driver"
    sqlDir = project.file("database/testing/test-data.sql").absolutePath
}
