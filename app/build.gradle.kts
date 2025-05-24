extensions.getByType(BasePluginExtension::class.java).archivesName.set("archiveName")

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    kotlin("plugin.serialization") version "2.0.21"
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "xyz.ggeorge.fisesms"
    compileSdk = 35

    defaultConfig {
        applicationId = "xyz.ggeorge.fisesms"
        minSdk = 23
        targetSdk = 35
        versionCode = Versioning.code
        versionName = Versioning.name

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        getByName("debug") {
            storeFile = rootProject.file("secrets/debug-keystore.jks")
            storePassword = envOrProp("DEBUG_KEYSTORE_PASSWORD")
            keyAlias = "debug"
            keyPassword = envOrProp("DEBUG_KEY_PASSWORD")
        }
        register("release") {
            storeFile = rootProject.file("secrets/release-keystore.jks")
            storePassword = envOrProp("RELEASE_KEYSTORE_PASSWORD")
            keyAlias = "release"
            keyPassword = envOrProp("RELEASE_KEY_PASSWORD")
        }
    }
    buildTypes {
        getByName("debug") {
            signingConfig = signingConfigs.getByName("debug")
            isDebuggable = true
        }
        getByName("release") {
            if (rootProject.file("secrets/release-keystore.jks").exists()) {
                signingConfig = signingConfigs.getByName("release")
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
        buildConfig = true
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
    implementation(libs.androidx.datastore)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter)
    implementation(libs.retrofit.okhttp)
    implementation(libs.timber)

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

tasks.withType<Jar> {
    val archiveName = "${rootProject.name}-${Versioning.name}"
    archiveBaseName.set(archiveName)
}