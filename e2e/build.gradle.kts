import com.github.gradle.node.yarn.task.YarnTask

plugins {
    id("diversity.node-conventions")
}

tasks {
    val prepare by getting

    // This is not a yarn_format task because the task to run is `yarn format:check`
    // and tasks with colons are not supported
    val checkFormat by registering(YarnTask::class) {
        args.set(listOf("run", "format:check"))
        dependsOn(prepare)
        inputs.dir("cypress")
        inputs.file("package.json")
        outputs.file("$buildDir/prettier-result.txt")
    }

    // This is not a yarn_e2e task because the task to run is `yarn e2e:standalone`
    // and tasks with colons are not supported
    val e2e by registering(YarnTask::class) {
        // On CI, we need to start the app with a different postgres host
        // so we have a dedicated task in the package.json
        if (project.findProperty("CI") != null) {
            args.set(listOf("run", "e2e:standalone:ci"))
        } else {
            args.set(listOf("run", "e2e:standalone"))
        }
        dependsOn(prepare)
        dependsOn(":backend:bootJar")
        inputs.dir("cypress")
        inputs.file("package.json")
        inputs.file("cypress.json")
        inputs.file("${project(":backend").buildDir}/libs/diversity.jar")
        outputs.file("$buildDir/cypress-results.json")
    }

    check {
        dependsOn(checkFormat)
        dependsOn(e2e)
    }
}
