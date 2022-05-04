import com.github.gradle.node.yarn.task.YarnTask

plugins {
    id("diversity.node-conventions")
}

tasks {
    val yarnBuild by registering(YarnTask::class) {
        args.set(listOf("build"))
        dependsOn(prepare)
        inputs.dir("src")
        outputs.dir("dist")
    }

    val yarnTest by registering(YarnTask::class) {
        args.set(listOf("test"))
        dependsOn(prepare)
        inputs.dir("src")
        outputs.dir("coverage")
    }

    val test by registering {
        dependsOn(yarnTest)
    }

    val yarnLint by registering(YarnTask::class) {
        args.set(listOf("lint"))
        dependsOn(prepare)
        inputs.dir("src")
        inputs.file("tslint.json")
        outputs.file("build/tslint-result.txt")
    }

    val lint by registering {
        dependsOn("yarnLint")
        doLast {
            file("build/tslint-result.txt").useLines { sequence ->
                if (sequence.any { it.contains("WARNING") }) {
                    throw GradleException("Lint warning found. Check tslint-result.txt")
                }
            }
        }
    }

    // This is not a yarn_format task because the task to run is `yarn format:check`
    // and tasks with colons are not supported
    val checkFormat by registering(YarnTask::class) {
        args.set(listOf("run", "format:check"))
        dependsOn(prepare)
        inputs.dir("src")
        inputs.file("package.json")
        outputs.file("build/prettier-result.txt")
    }

    check {
        dependsOn(checkFormat)
        dependsOn(lint)
        dependsOn(test)
    }

    assemble {
        dependsOn(yarnBuild)
    }

    clean {
        dependsOn("cleanYarnBuild")
    }
}
