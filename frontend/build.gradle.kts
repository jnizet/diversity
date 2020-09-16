import com.moowork.gradle.node.yarn.YarnTask

plugins {
    id("diversity.node-conventions")
}

tasks {
    val prepare by getting

    // This is not a yarn_format task because the task to run is `yarn format:check`
    // and tasks with colons are not supported
    val checkFormat by registering(YarnTask::class) {
        args = listOf("run", "format:check")
        dependsOn(prepare)
        inputs.dir("src")
        inputs.file("package.json")
        outputs.file("build/prettier-result.txt")
    }

    // This is not a yarn_build task because the task to run is `yarn build:prod`
    // and tasks with colons are not supported
    val yarnBuildProd by registering(YarnTask::class) {
        args = listOf("run", "build:prod")
        dependsOn(prepare)
        inputs.file("webpack.common.js")
        inputs.file("webpack.prod.js")
        inputs.file("tsconfig.json")
        inputs.file("package.json")
        inputs.file("yarn.lock")
        inputs.dir("src")
        outputs.dir("$buildDir/dist")
    }

    assemble {
        dependsOn(yarnBuildProd)
    }

    check {
        dependsOn(checkFormat)
    }
}
