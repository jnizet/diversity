import com.moowork.gradle.node.yarn.YarnTask

plugins {
    base
    id("com.github.node-gradle.node")
}

node {
    version = "12.18.3"
    npmVersion = "6.14.6"
    yarnVersion = "1.22.5"
    download = true
}

tasks {
    npmInstall {
        enabled = false
    }

    val yarn_install by getting {
        inputs.file("package.json")
        inputs.file("yarn.lock")
        outputs.dir("node_modules")
    }

    val prepare by registering {
        dependsOn(yarn_install)
    }

    // This is not a yarn_format task because the task to run is `yarn format:check`
    // and tasks with colons are not supported
    val checkFormat by registering(YarnTask::class) {
        args = listOf("run", "format:check")
        execRunner.ignoreExitValue = true
        dependsOn(prepare)
        inputs.dir("cypress")
        inputs.file("package.json")
        outputs.file("prettier-result.txt")
        doLast {
            file("prettier-result.txt").useLines { sequence ->
                if (sequence.any { it.contains("cypress") }) {
                    throw GradleException ("Formatting warning found. Check prettier-result.txt")
                }
            }
        }
    }

    // This is not a yarn_e2e task because the task to run is `yarn e2e:standalone`
    // and tasks with colons are not supported
    val e2e by registering(YarnTask::class) {
        // On CI, we need to start the app with a different postgres host
        // so we have a dedicated task in the package.json
        if (project.findProperty("CI") != null) {
            args = listOf("run", "e2e:standalone:ci")
        } else {
            args = listOf("run", "e2e:standalone")
        }
        dependsOn(prepare)
        dependsOn(":backend:bootJar")
        inputs.dir("cypress")
        inputs.file("package.json")
        inputs.file("cypress.json")
        inputs.file("${project(":backend").buildDir}/libs/diversity.jar")
        outputs.file("cypress-results.json")
    }

    check {
        dependsOn(checkFormat)
        dependsOn(e2e)
    }

    clean {
        dependsOn("cleanCheckFormat")
        dependsOn("cleanE2e")
    }
}
