plugins {
    alias(libs.plugins.android.application)

    // App level dependency for Firebase
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.taam_project"
    compileSdk = 34

    aaptOptions.cruncherEnabled = false
    aaptOptions.useNewCruncher = false

    defaultConfig {
        applicationId = "com.example.taam_project"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
}

dependencies {
    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.1.1"))

    // Import the BoM for the Firebase platform
    implementation(platform("com.google.firebase:firebase-bom:33.1.2"))

    // Add the dependency for the Firebase Authentication library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-auth")

    // Bumptech
    implementation("com.github.bumptech.glide:glide:4.14.2")

    // ExoPlayer
    implementation("com.google.android.exoplayer:exoplayer-core:2.18.1")
    implementation("com.google.android.exoplayer:exoplayer-ui:2.18.1")

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.database)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.auth)
    testImplementation(libs.junit)
    testImplementation("org.mockito:mockito-all:1.10.19")
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    dependencies {
        implementation(libs.firebase.auth)
    }
}