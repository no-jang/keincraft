plugins {
    id("bundle-library")
}

sourceSets {
    create("api") {
        compileClasspath += sourceSets["main"].compileClasspath
    }

    getByName("main") {
        compileClasspath += sourceSets["api"].output
        runtimeClasspath += sourceSets["api"].output
    }

    getByName("test") {
        compileClasspath += sourceSets["api"].output
        runtimeClasspath += sourceSets["api"].output
    }
}

java {
    registerFeature("api") {
        usingSourceSet(sourceSets["api"])

        withJavadocJar()
        withSourcesJar()
    }
}

dependencies {
    implementation("org.tinylog:tinylog-impl:2.5.0-M1.1")
}

tasks{
    val features = listOf("api")

    features.forEach { feature ->
        create("${feature}Assemble") {
            dependsOn("${feature}Jar")
            dependsOn("${feature}JavadocJar")
            dependsOn("${feature}SourcesJar")
        }
    }

    names.forEach { name ->
        features.forEach { feature ->
            if (name.startsWith(feature)) {
                getByName(name).group = "build-$feature"
            }
        }
    }
}