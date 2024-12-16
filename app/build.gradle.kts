import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin)

    id("com.google.devtools.ksp")
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.storyapps"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.storyapps"
        minSdk = 30
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        //load the values from .properties file
        val keystoreFile = project.rootProject.file("local.properties")
        val properties = Properties()
        properties.load(keystoreFile.inputStream())

        //return empty key in case something goes wrong
        val dicodingStoryApiKey = properties.getProperty("DICODING_STORY_API_KEY") ?: ""
        buildConfigField(
            type = "String",
            name = "DICODING_STORY_API_KEY",
            value = dicodingStoryApiKey
        )

        val mapsApiKey = properties.getProperty("MAPS_API_KEY") ?: ""
        buildConfigField(
            type = "String",
            name = "MAPS_API_KEY",
            value = mapsApiKey
        )
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.play.services.maps)
    implementation(libs.play.services.location)
    implementation(libs.androidx.junit.ktx)
    implementation(libs.androidx.rules)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // data store
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // lifecycle
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
    implementation("androidx.activity:activity-ktx:1.7.2")

    // navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.4")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.4")

    // image
    implementation("com.github.bumptech.glide:glide:4.16.0")

    // retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit2.converter.gson)
    implementation(libs.logging.interceptor)

    // rotasi
    implementation(libs.androidx.exifinterface)

    // room
    implementation(libs.androidx.room.ktx)
    androidTestImplementation(project(":app"))
    ksp(libs.room.compiler)

    // paging3 network
    implementation(libs.androidx.paging.runtime.ktx)

    // paging 3 RemoteMediator
    implementation(libs.androidx.room.paging)

    // testing
    androidTestImplementation(libs.androidx.core.testing) //InstantTaskExecutorRule
    androidTestImplementation(libs.kotlinx.coroutines.test) //TestDispatcher

    testImplementation(libs.androidx.core.testing) // InstantTaskExecutorRule
    testImplementation(libs.kotlinx.coroutines.test) //TestDispatcher
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.inline)

    // espresso
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.espresso.idling.resource)
    implementation ("com.android.support.test.espresso:espresso-idling-resource:3.0.2")

}