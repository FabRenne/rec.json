# CrudApp — CRUD Jetpack Compose + SQLite

Application Android (Kotlin, Jetpack Compose, Material 3) qui gère des enregistrements
`(nom, date)` dans une base **SQLite** (`SQLiteOpenHelper`, sans Room).

## Écrans

| Écran | Contenu |
|---|---|
| **Accueil** | Nombre d'enregistrements en base + boutons **Ajouter**, **Modifier**, **Supprimer** (ces deux derniers sont désactivés si la base est vide) |
| **Ajouter** | Champ *Nom* + champ *Date* (saisie `jj/mm/aaaa` ou sélection via l'icône calendrier) avec validation |
| **Modifier** | Liste pour choisir l'enregistrement → même formulaire que *Ajouter*, pré-rempli |
| **Supprimer** | Liste des enregistrements avec icône corbeille → boîte de dialogue de confirmation |

## Structure

```
app/src/main/java/com/example/crudapp/
├── MainActivity.kt            # NavHost + routes
├── data/
│   ├── Enregistrement.kt      # modèle
│   └── DatabaseHelper.kt      # SQLite : création table + CRUD
└── ui/
    ├── CrudViewModel.kt       # état (compteur, liste) + appels BDD sur Dispatchers.IO
    ├── AccueilScreen.kt
    ├── FormulaireScreen.kt    # partagé Ajouter / Modifier
    └── ListeScreen.kt         # partagé choix-modification / suppression
```

## Lancer

Ouvrir le dossier `CrudApp` dans Android Studio puis *Run*, ou :

```
./gradlew installDebug
```

minSdk 24 · compileSdk/targetSdk 35 · AGP 8.7.3 · Kotlin 2.0.21 · Gradle 8.14.3
