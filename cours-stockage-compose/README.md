# Cours : enregistrer des données avec Jetpack Compose (débutant)

Contenu :
- `Cours_Stockage_Jetpack_Compose.pdf` : le cours (15 pages) avec les écrans intégrés
- `ecrans/` : les captures des 5 écrans de l'application
- `StockageCompose/` : le projet Android Studio complet

Les 4 solutions présentées :
1. SharedPreferences (`ui/PreferencesScreen.kt`)
2. DataStore Preferences (`data/ReglagesDataStore.kt`, `ui/DataStoreScreen.kt`)
3. Fichier interne (`ui/FichierScreen.kt`)
4. Room (`data/Note.kt`, `data/NoteDao.kt`, `data/AppDatabase.kt`, `ui/RoomScreen.kt`)

## Lancer le projet
Android Studio › File › Open › choisir le dossier `StockageCompose`,
attendre la synchronisation Gradle, puis cliquer sur ▶ (minSdk 24, compileSdk 35).
