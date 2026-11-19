# Cours illustré — l'application Moutures (Kotlin + Jetpack Compose)

## Contenu

| Fichier | Description |
|---|---|
| `Cours-Moutures-illustre.pdf` | Le cours complet (14 chapitres, 29 illustrations) |
| `Cours-Moutures-illustre.html` | Le même cours, lisible dans un navigateur |
| `projet/` | Le projet Android Studio complet |

## Le projet

```
projet/
├── settings.gradle.kts
├── build.gradle.kts
├── gradle.properties
├── gradle/wrapper/gradle-wrapper.properties
└── app/
    ├── build.gradle.kts
    └── src/main/
        ├── AndroidManifest.xml
        ├── assets/rec.json
        └── kotlin/com/example/moutures2/
            ├── MainActivity.kt
            ├── Mouture.kt
            ├── MoutureRepository.kt
            ├── MoutureViewModel.kt
            ├── MouturesScreen.kt
            └── Theme.kt
```

## Ouvrir le projet

1. Android Studio › **File › Open** › choisir le dossier `projet/`.
2. Laisser Android Studio faire la synchronisation Gradle (**Sync**).
   S'il le propose, accepter de générer le *Gradle wrapper* (`gradlew`).
3. Lancer avec ▶ **Run** sur un émulateur ou un téléphone (Android 7.0 / API 24 minimum).

Versions utilisées : AGP 8.7.3, Kotlin 2.1.0, Gradle 8.9, Compose BOM 2024.12.01, compileSdk 35.
