plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.devtools.ksp") // nécessaire pour Room
}

android {
    namespace = "com.exemple.stockagecompose"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.exemple.stockagecompose"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
}

// Évite l'erreur "Duplicate class org.intellij.lang.annotations..." :
// l'ancien artefact com.intellij:annotations contient les mêmes classes
// que org.jetbrains:annotations. On n'en garde qu'un seul.
configurations.all {
    exclude(group = "com.intellij", module = "annotations")
}

dependencies {
    // Jetpack Compose
    val composeBom = platform("androidx.compose:compose-bom:2024.09.00")
    implementation(composeBom)
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.activity:activity-compose:1.9.2")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.5")
    implementation("androidx.navigation:navigation-compose:2.8.0")
    // Requis par androidx.profileinstaller (dépendance transitive parfois oubliée
    // par certains outils de build : crash NoClassDefFoundError AbstractResolvableFuture)
    implementation("androidx.concurrent:concurrent-futures:1.2.0")

    // 2) DataStore Preferences
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    // 4) Room (base de données SQLite)
    implementation("androidx.room:room-runtime:2.7.2")
    implementation("androidx.room:room-ktx:2.7.2")
    ksp("androidx.room:room-compiler:2.7.2")
}
