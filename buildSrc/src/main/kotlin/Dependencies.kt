object Dependencies {

    object AndroidX{
        private const val coreKtxVersion = "1.7.0"
        const val coreKtx = "androidx.core:core-ktx:$coreKtxVersion"

        private const val appCompatVersion = "1.4.0"
        const val appCompat = "androidx.appcompat:appcompat:$appCompatVersion"

        private const val constraintLayoutVersion = "2.1.3"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:$constraintLayoutVersion"
    }

    object Testing{
        private const val junitVersion = "4.13.2"
        const val junit4 = "junit:junit:$junitVersion"

        private const val junitAndroidExtVersion = "1.1.3"
        const val junitAndroidExt = "androidx.test.ext:junit:$junitAndroidExtVersion"

        private const val espressoVersion = "3.4.0"
        const val espresso = "androidx.test.espresso:espresso-core:$espressoVersion"
    }

    object Room{
        private const val roomVersion = "2.4.2"

        const val roomRuntime = "androidx.room:room-runtime:$roomVersion"
        const val roomCompiler = "androidx.room:room-compiler:$roomVersion"
        const val roomKtx = "androidx.room:room-ktx:$roomVersion"
    }

    object DaggerHilt{
        private const val hiltVersion = "2.41"
        const val hiltAndroid = "com.google.dagger:hilt-android:$hiltVersion"
        const val hiltCompiler = "com.google.dagger:hilt-android-compiler:$hiltVersion"

        const val hiltPlugin = "com.google.dagger:hilt-android-gradle-plugin:$hiltVersion"
    }

    object Coroutines{
        private const val coroutinesCoreVersion = "1.5.0"
        const val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesCoreVersion"

        private const val coroutinesAndroidVersion = "1.5.1"
        const val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesAndroidVersion"
    }

    object Material{
        private const val materialVersion = "1.5.0"
        const val material = "com.google.android.material:material:$materialVersion"
    }

    object ProjectConfig{
        const val appId = "com.example.workoutdiary"
        const val compileSdk = 32
        const val minSdk = 21
        const val targetSdk = 32
        const val versionCode = 1
        const val versionName = "1.0"
    }


}


