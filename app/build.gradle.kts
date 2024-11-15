import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose.compiler)
    //kotlin("plugin.serialization") version "2.0.20"
}

android {
    namespace = "com.example.liftoff"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.liftoff"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "0.13"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        val properties = Properties().apply {
            load(project.rootProject.file("local.properties").inputStream())
        }

        val supabaseUrl = properties.getProperty("SUPABASE_URL", "")
        val supabaseKey = properties.getProperty("SUPABASE_KEY", "")

        buildConfigField(
            type = "String",
            name = "SUPABASE_URL",
            value = "\"$supabaseUrl\""
        )
        buildConfigField(
            type = "String",
            name = "SUPABASE_KEY",
            value = "\"$supabaseKey\""
        )
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.kotlinx.serialization.json)
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.material3)
    implementation(libs.postgrest.kt)
    implementation(libs.auth.kt)
    implementation(libs.storage.kt)
    implementation(libs.ktor.client.android)
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation("com.google.code.gson:gson:2.8.8")

    //implementation(platform("io.github.jan-tennert.supabase:bom:3.0.1"))
    //implementation("io.github.jan-tennert.supabase:postgrest-kt")
   // implementation("io.github.jan-tennert.supabase:auth-kt")

   // implementation("io.ktor:ktor-client-android:3.0.0")


}
