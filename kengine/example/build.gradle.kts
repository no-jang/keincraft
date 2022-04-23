plugins {
    `java-library`
}

repositories {
    mavenCentral()
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
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17

    withJavadocJar()
    withSourcesJar()

    registerFeature("api") {
        usingSourceSet(sourceSets["api"])

        withJavadocJar()
        withSourcesJar()
    }
}

dependencies {
    implementation("org.tinylog:tinylog-impl:2.5.0-M1.1")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
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

    withType<Test> {
        useJUnitPlatform()
        workingDir(layout.buildDirectory.dir("test"))

        doFirst {
            workingDir.mkdirs()
        }
    }
}