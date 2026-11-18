# Cours Room + Jetpack Compose

## Contenu

```
cours-room-compose/
├── cours/
│   ├── Cours_Room_Jetpack_Compose.pdf   ← le cours (31 pages, 11 figures)
│   └── cours_room_compose.html          ← source HTML du PDF
└── projet-exemple/                      ← projet Android « Mes tâches »
    ├── settings.gradle.kts / build.gradle.kts
    ├── gradle/libs.versions.toml        ← versions (Room, KSP, Compose…)
    └── app/
        ├── build.gradle.kts             ← plugin KSP + dépendances Room
        └── src/
            ├── main/java/com/exemple/roomcours/
            │   ├── data/local/entity/       Tache, Categorie, CategorieAvecTaches
            │   ├── data/local/dao/          TacheDao, CategorieDao
            │   ├── data/local/converter/    Converters (LocalDate, enum)
            │   ├── data/local/AppDatabase.kt  (singleton + migration 1→2)
            │   ├── data/repository/         TacheRepository
            │   ├── ui/TacheViewModel.kt     StateFlow, combine, flatMapLatest
            │   ├── ui/screens/TachesScreen.kt  écran Compose (LazyColumn, dialogue)
            │   ├── TacheApplication.kt
            │   └── MainActivity.kt
            └── androidTest/…            TacheDaoTest, MigrationTest
```

## Plan du cours

1. Introduction à Room · 2. Configuration Gradle (KSP) · 3. Entity / DAO / Database
4. Entités · 5. DAO (suspend vs Flow) · 6. RoomDatabase (singleton) · 7. TypeConverters
8. Relations (1-N, N-N, @Relation, jointures) · 9. MVVM : Repository & ViewModel
10. UI Compose · 11. Cycle réactif · 12. Migrations · 13. Tests
14. Bonnes pratiques & erreurs fréquentes · 15. Aide-mémoire · 16. Exercices (+ corrigés)

## Ouvrir le projet

1. Android Studio → *File › Open* → dossier `projet-exemple`.
2. Laisser Android Studio générer le Gradle Wrapper et synchroniser.
3. Lancer l'application sur un émulateur (minSdk 24).
4. Tests : clic droit sur `androidTest` → *Run tests*.

> `MigrationTest` nécessite les schémas exportés `1.json` et `2.json` dans `app/schemas/`.
> Ils sont générés à la compilation (`exportSchema = true`) : compilez d'abord avec
> `version = 1` (sans la colonne `cree_le`), puis avec `version = 2`.
