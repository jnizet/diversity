import com.moowork.gradle.node.yarn.YarnTask

plugins {
  id("diversity.node-conventions")
}

tasks {
  val yarn_build by getting {
    dependsOn(prepare)
    inputs.dir("src")
    outputs.dir("dist")
  }

  val yarn_test by getting {
    dependsOn(prepare)
    inputs.dir("src")
    outputs.dir("coverage")
  }

  val test by registering {
    dependsOn(yarn_test)
  }

  val yarn_lint by getting {
    dependsOn(prepare)
    inputs.dir("src")
    inputs.file("tslint.json")
    outputs.file("build/tslint-result.txt")
  }

  val lint by registering {
    dependsOn("yarn_lint")
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
    args = listOf("run", "format:check")
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
    dependsOn(yarn_build)
  }

  clean {
    dependsOn("cleanYarn_build")
  }
}
