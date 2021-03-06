// this is a precompiled script plugin that can be applied to all the projects using node
// it applies the base and node plugins, configures versions of node, npm and yarn, disables the npmInstall task
// configures the yarn install task and registers a prepare task that other node tasks of the project
// should depend on.

// this precompiled script plugin avoids repeating all the stuff described above for every node project, and
// ensures the same versions are used everywhere and configured in a single place.

import com.github.gradle.node.yarn.task.YarnInstallTask
import com.github.gradle.node.yarn.task.YarnTask

plugins {
    base
    id("com.github.node-gradle.node")
}

node {
    version.set("16.15.0")
    npmVersion.set("6.14.17")
    yarnVersion.set("1.22.18")
    download.set(true)
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

    register("prepare") {
        dependsOn(yarn_install)
    }
}
