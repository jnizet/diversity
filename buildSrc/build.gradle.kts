plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal() // so that external plugins can be resolved in dependencies section
}

dependencies {
    implementation("com.github.node-gradle:gradle-node-plugin:2.2.4")
}
