# Mes Fruits — projet Android Studio du cours « Les Fragments avec Jetpack Compose »

Ce projet contient **tous les exemples du cours**, prêts à lancer.

## Ouvrir le projet
1. Décompressez le zip.
2. Dans Android Studio : **File › Open…** puis choisissez le dossier `MesFruits`.
3. Attendez la fin de la synchronisation Gradle (barre en bas de la fenêtre).
4. Choisissez un émulateur ou un téléphone, puis cliquez sur ▶ **Run 'app'**.

Au lancement, un **menu** permet d'ouvrir chaque exemple :

| Menu | Chapitre du cours | Fichiers à regarder |
|---|---|---|
| Exemple 1 | 3 — Compose dans un Fragment | `exemple1/PremierFragment.kt` |
| Exemples 2, 3, 4 | 4 à 6 — Navigation, arguments, ViewModel | `fragments/*.kt` |
| Exemple 5 | 7 — Ancien Fragment dans Compose | `exemple5/*.kt`, `res/layout/fragment_meteo.xml` |
| Exemple 6 | 8 — 100 % Compose, sans Fragment | `exemple6/*.kt` |

Les écrans Compose communs sont dans `ui/Ecrans.kt`, les données dans `data/Fruit.kt`.
Les exercices 2 et 3 du chapitre 11 sont aussi intégrés (bouton « Voir un autre fruit au hasard »).

## Configuration
- Android Gradle Plugin 8.7.3, Gradle 8.11.1, Kotlin 2.0.21
- compileSdk / targetSdk 35, minSdk 24 (Android 7.0)
- Versions centralisées dans `gradle/libs.versions.toml`

Si Android Studio propose de mettre à jour l'Android Gradle Plugin ou des dépendances, vous pouvez accepter.
