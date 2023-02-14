plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    compileSdk = Dependencies.ProjectConfig.compileSdk

    defaultConfig {
        applicationId = Dependencies.ProjectConfig.appId
        minSdk = Dependencies.ProjectConfig.minSdk
        targetSdk = Dependencies.ProjectConfig.targetSdk
        versionCode = Dependencies.ProjectConfig.versionCode
        versionName = Dependencies.ProjectConfig.versionName
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner";
        resourceConfigurations.addAll(listOf("en", "ru"))
    }
    kapt {
        correctErrorTypes = true
    }
    buildFeatures {
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

        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

}


dependencies {

    constraints {
        implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1") {
            because("transitive dependencies conflict between two libraries")
        }
    }

    implementation(Dependencies.AndroidX.coreKtx)
    implementation(Dependencies.AndroidX.appCompat)
    implementation(Dependencies.AndroidX.constraintLayout)
    implementation(Dependencies.AndroidX.recyclerView)

    implementation(Dependencies.Material.material)

    implementation(Dependencies.Navigation.navigationUi)
    implementation(Dependencies.Navigation.navigationFragment)

    implementation(Dependencies.LifeCycle.lifecycle)

    kapt(Dependencies.Room.roomCompiler)
    implementation(Dependencies.Room.roomKtx)
    implementation(Dependencies.Room.roomRuntime)

    coreLibraryDesugaring(Dependencies.CoreLibraryDesugaring.desugarJdkLibs)

    kapt(Dependencies.DaggerHilt.hiltCompiler)
    implementation(Dependencies.DaggerHilt.hiltAndroid)

    implementation(Dependencies.Coroutines.coroutinesCore)
    implementation(Dependencies.Coroutines.coroutinesAndroid)

    implementation(Dependencies.LiveData.liveDataLifecycle)

    implementation(Dependencies.Calendar.calendar)

    implementation(Dependencies.Charts.chartsViews)

    implementation(Dependencies.Datastore.datastore)

    testImplementation(Dependencies.Testing.junit4)
    androidTestImplementation(Dependencies.Testing.junitAndroidExt)
    androidTestImplementation(Dependencies.Testing.espresso)
}