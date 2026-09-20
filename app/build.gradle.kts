import java.util.Properties
import java.io.FileInputStream



plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
     alias(libs.plugins.kotlin.serialization)
}

val localProps = Properties().apply {
    val f = rootProject.file("local.properties")
    if (f.exists()) load(FileInputStream(f))
}

android {
    namespace = "com.lordsandlegends.crew"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.lordsandlegends.crew"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        buildConfigField("String", "SUPABASE_URL", "\"${localProps["SUPABASE_URL"]}\"")
        buildConfigField("String", "SUPABASE_ANON_KEY", "\"${localProps["SUPABASE_ANON_KEY"]}\"")
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
             isCoreLibraryDesugaringEnabled = true
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
         buildConfig = true
    }

    lint {
        // The lifecycle library's NullSafeMutableLiveData check crashes lint on this
        // AGP / Kotlin analysis API combination, so it is switched off. Remove this
        // once the project moves to a newer AGP.
        disable += "NullSafeMutableLiveData"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {



coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.1.5")
//superbase

    implementation(platform(libs.supabase.bom))
    implementation(libs.supabase.postgrest)
    implementation(libs.supabase.auth)
    implementation(libs.supabase.storage)
    implementation(libs.supabase.realtime)
    implementation(libs.ktor.client.android)
    implementation(libs.kotlinx.datetime)
    implementation(libs.androidx.lifecycle.viewmodel)

    implementation("com.tom-roush:pdfbox-android:2.0.27.0")


    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended)
    
    // YouTube Player Library (Reference: https://github.com/PierfrancescoSoffritti/android-youtube-player)
    implementation(libs.youtube.player)
    implementation(libs.coil.compose)
    implementation(libs.coil.network)


    debugImplementation(libs.androidx.ui.tooling)
}
