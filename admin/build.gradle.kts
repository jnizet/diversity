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
        inputs.files("yarn.lock", ".eslintrc.json", "angular.json", ".prettierrc")
        outputs.file("build/eslint-result.txt")
    }

    val lint by registering {
        dependsOn("yarnLint")
    }

    check {
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
