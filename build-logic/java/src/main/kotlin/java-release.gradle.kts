import proguard.gradle.ProGuardTask

tasks {
    create<ProGuardTask>("releaseJar") {
        group = "build"

        val configurationFile = layout.projectDirectory.file("rules.pro")
        val inputFile = tasks.getByName("jar").outputs.files
        val outputFileName = provider { inputFile.singleFile.name.replace(".jar", "") }
        val outputFile = provider { layout.buildDirectory.file("libs/$outputFileName.jar") }
        val outputMappingFile = provider { layout.buildDirectory.file("libs/$outputFileName.mapping") }

        inputs.files(inputFile)
        outputs.files(outputFile)

        injars(inputFile)
        outjars(outputFile)

        optimizationpasses(10)
        overloadaggressively()
        repackageclasses()
        allowaccessmodification()

        verbose()

        doFirst {
            libraryjars(configurations.getByName("runtimeClasspath").files)
            printmapping(outputMappingFile.get())

            if(configurationFile.asFile.exists()) {
                configuration(configurationFile)
            }
        }
    }
}