plugins {
    id("java-basic")
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
}

tasks.withType<Test> {
    workingDir(layout.buildDirectory.dir("test"))

    useJUnitPlatform()

    doFirst {
        workingDir.mkdirs()
    }
}