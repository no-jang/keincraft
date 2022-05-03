import org.gradle.api.internal.FeaturePreviews

rootProject.name = "keincraft"

pluginManagement {
    includeBuild("build-logic")
}

include("kengine")
include("kengine:example")

// Activates all incubating features
FeaturePreviews.Feature.values().forEach { feature ->
    if (feature.isActive) {
        enableFeaturePreview(feature.name)
    }
}