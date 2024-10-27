import org.gradle.language.nativeplatform.internal.BuildType
import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.archivesName

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    kotlin("plugin.serialization") version "2.0.21"
}

android {
    namespace = "xyz.ggeorge.fisesms"
    compileSdk = 34

    defaultConfig {
        applicationId = "xyz.ggeorge.fisesms"
        minSdk = 23
        targetSdk = 33
        versionCode = Versioning.code
        versionName = Versioning.name
        archivesName = "${rootProject.name}-${versionName}"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        named(BuildType.DEBUG.name) {
            storeFile = rootProject.file("secrets/debug-keystore.jks")
            storePassword = envOrProp("DEBUG_KEYSTORE_PASSWORD")
            keyAlias = "debug"
            keyPassword = envOrProp("DEBUG_KEY_PASSWORD")
        }
        register(BuildType.RELEASE.name) {
            storeFile = rootProject.file("secrets/release-keystore.jks")
            storePassword = envOrProp("RELEASE_KEYSTORE_PASSWORD")
            keyAlias = "release"
            keyPassword = envOrProp("RELEASE_KEY_PASSWORD")
        }
    }
    buildTypes {
        named(BuildType.DEBUG.name) {
            signingConfig = signingConfigs.getByName(BuildType.DEBUG.name)
            isDebuggable = true
        }
        named(BuildType.RELEASE.name) {
            if (rootProject.file("secrets/release-keystore.jks").exists()) {
                signingConfig = signingConfigs.getByName(BuildType.RELEASE.name)
            }
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "21"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(project(":core"))
    implementation(project(":components"))

    implementation(libs.core.ktx)
    implementation(libs.activity.compose)
    implementation(libs.bundles.compose)
    implementation(libs.bundles.lifecycle)
    implementation(libs.bundles.accompanist)
    implementation(libs.kotlin.reflection)
    implementation(libs.kotlin.serialization)
    implementation(libs.material3)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    implementation(libs.accompanist.permissions)
    implementation(libs.navigation.compose)

    annotationProcessor(libs.room.compiler)

    ksp(libs.room.compiler)

    testImplementation(libs.junit)
    androidTestImplementation(libs.junit.android)
    androidTestImplementation(libs.espresso)
    androidTestImplementation(platform(libs.compose.bom.test))
    androidTestImplementation(libs.compose.test)
    debugImplementation(libs.compose.tooling)
    debugImplementation(libs.compose.test.manifest)

}