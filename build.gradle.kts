plugins {
    java
}

sourceSets {
    main {
        java {
            srcDir("client")
            srcDir("common")
            srcDir("server")
        }

        resources {
            srcDir("shaders")
            srcDir("textures")
        }
    }
}

repositories {
    mavenCentral()
}

val lwjglNatives = arrayOf("natives-linux", "natives-windows")

dependencies {
    // LWJGL
    implementation(platform("org.lwjgl:lwjgl-bom:3.2.3"))

    implementation("org.lwjgl", "lwjgl") // Core
    implementation("org.lwjgl", "lwjgl-glfw") // Window, Input
    implementation("org.lwjgl", "lwjgl-opengl") // Graphics
    implementation("org.lwjgl", "lwjgl-stb") // Textures

    // Native libraries
    lwjglNatives.forEach { native ->
        runtimeOnly("org.lwjgl", "lwjgl", classifier = native)
        runtimeOnly("org.lwjgl", "lwjgl-glfw", classifier = native)
        runtimeOnly("org.lwjgl", "lwjgl-opengl", classifier = native)
        runtimeOnly("org.lwjgl", "lwjgl-stb", classifier = native)
    }

    // Math
    implementation("org.joml:joml:1.10.2")
}

tasks {
    create<Sync>("syncLibraryDirectory") {
        from(configurations.runtimeClasspath)
        into("libs")
    }
}