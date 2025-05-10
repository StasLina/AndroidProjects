import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
    id("androidx.navigation.safeargs")
}

android {
    namespace = "com.example.maproutebuilder"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.maproutebuilder"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Чтение local.properties
        val localProperties = Properties().apply {
            load(rootProject.file("local.properties").inputStream())
        }

        val yandexMapkitKey = localProperties.getProperty("yandex.mapkit.key") ?: ""

        buildConfigField("String", "YANDEX_MAPKIT_API_KEY", "\"$yandexMapkitKey\"")
        manifestPlaceholders["YandexMapkitApiKey"] = yandexMapkitKey
    }

    buildFeatures {
        buildConfig = true
        viewBinding = true
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
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation("com.squareup.retrofit2:converter-gson:2.5.0")
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.jakewharton.timber:timber:5.0.1")

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    implementation("com.google.dagger:hilt-android:2.48")
    ksp("com.google.dagger:hilt-android-compiler:2.48")
}

dependencies {
    testImplementation("junit:junit:4.13.2")
    testImplementation("androidx.test:core:1.5.0")
    testImplementation("org.mockito:mockito-core:4.6.1")
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.0.0")
    testImplementation("io.mockk:mockk:1.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // тесты view model
    testImplementation("androidx.arch.core:core-testing:2.1.0")
    testImplementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    testImplementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")

    // тесты корутин
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
}

dependencies {
    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")

    // If this project uses any Kotlin source, use Kotlin Symbol Processing (KSP)
    // See Add the KSP plugin to your project
    ksp("androidx.room:room-compiler:$room_version")

    // If this project only uses Java source, use the Java annotationProcessor
    // No additional plugins are necessary
    annotationProcessor("androidx.room:room-compiler:$room_version")

    // optional - Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:$room_version")

    // optional - RxJava2 support for Room
    implementation("androidx.room:room-rxjava2:$room_version")

    // optional - RxJava3 support for Room
    implementation("androidx.room:room-rxjava3:$room_version")

    // optional - Guava support for Room, including Optional and ListenableFuture
    implementation("androidx.room:room-guava:$room_version")

    // optional - Test helpers
    testImplementation("androidx.room:room-testing:$room_version")

    // optional - Paging 3 Integration
    implementation("androidx.room:room-paging:$room_version")

    // Navigation Component
    implementation("androidx.navigation:navigation-fragment-ktx:2.6.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.6.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
//    implementation("androidx.navigation:navigation-safe-args-gradle-plugin:2.6.0")
    implementation("androidx.navigation:navigation-dynamic-features-fragment:2.6.0")


//    val nav_version = "2.6.0"
//
//    // Jetpack Compose integration
//    implementation("androidx.navigation:navigation-compose:$nav_version")
//
//    // Views/Fragments integrationv
//    implementation("androidx.navigation:navigation-fragment:$nav_version")
//    implementation("androidx.navigation:navigation-ui:$nav_version")
//
//    // Feature module support for Fragments
//    implementation("androidx.navigation:navigation-dynamic-features-fragment:$nav_version")
//
//    // Testing Navigation
//    androidTestImplementation("androidx.navigation:navigation-testing:$nav_version")
//
//    // JSON serialization library, works with the Kotlin serialization plugin
//    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")

    // Yandex MapKit
    implementation("com.yandex.android:maps.mobile:4.5.1-full")

    // Транспортный модуль (если требуется отдельно)
//    implementation("com.yandex.android:transport.mobile:4.5.1-full")
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.55")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.6.0")
    }
}

