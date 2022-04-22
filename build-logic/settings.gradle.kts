import org.gradle.api.internal.FeaturePreviews

include("bundle")
include("java")

// Activates all incubating features
FeaturePreviews.Feature.values().forEach { feature ->
    if (feature.isActive) {
        enableFeaturePreview(feature.name)
    }
}