plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.diet_app"
    // Genellikle kullanılan kararlı sürüme düşürüldü
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.diet_app"
        minSdk = 26
        // Genellikle kullanılan kararlı sürüme düşürüldü
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    // ViewBinding özelliğini etkinleştir
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
        // JavaVersion'ı kullanırken doğru referansı kullanın
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    // Mevcut libs katalog girdileri
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Yaşamdöngüsü (Lifecycle) bağımlılıkları için versiyon katalog girdileri kullanılmalı.
    // Varsayım: libs.gradle'da tanımlanmamış. Bu nedenle sabit dize yerine
    // katalogda tanımlanmış yeni bir "libs.androidx.lifecycle.viewmodel" vb. yapısı önerilir.
    // Ancak mevcut yapıyı korumak adına, dize gösterimi versiyon tanımını dışarıdan alacak şekilde düzeltildi.

    // Sabit dize kullanımı hatalıydı; katalog kullanılırken tek tip olmalı.
    // VEYA, tüm bu bağımlılıklar için libs.versions.toml'da tanımlama yapmalısınız.

    // Varsayılan sabit dize kullanımı (libs katalogunda tanımlı değilse bu şekilde devam edin):

    // Navigation Component
    val nav_version = "2.7.5"
    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")

    // Lifecycle (ViewModel ve LiveData)
    val lifecycle_version = "2.6.2"
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")

    // Fragment KTX
    implementation("androidx.fragment:fragment-ktx:1.6.2")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
}