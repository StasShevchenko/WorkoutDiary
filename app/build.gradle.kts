plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.devtools.ksp")
}

android {

    compileSdk = 34
    defaultConfig {
        applicationId = Dependencies.ProjectConfig.appId
        minSdk = Dependencies.ProjectConfig.minSdk
        targetSdk = 34
        versionCode = Dependencies.ProjectConfig.versionCode
        versionName = Dependencies.ProjectConfig.versionName
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner";
        resourceConfigurations.addAll(listOf("en", "ru"))
        multiDexEnabled = true
    }
    buildFeatures {
        viewBinding = true
    }

    buildTypes {
        debug {
            isMinifyEnabled = false

        }
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
    namespace = "com.example.workoutdiary"

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

    ksp(Dependencies.Room.roomCompiler)
    implementation(Dependencies.Room.roomKtx)
    implementation(Dependencies.Room.roomRuntime)

    coreLibraryDesugaring(Dependencies.CoreLibraryDesugaring.desugarJdkLibs)

    ksp(Dependencies.DaggerHilt.hiltCompiler)
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

    implementation(Dependencies.Picasso.picasso)
}