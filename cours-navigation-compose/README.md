# Cours débutant : la navigation avec Jetpack Compose

| Élément | Contenu |
|---|---|
| `Cours_Navigation_Jetpack_Compose.pdf` | Le cours (28 pages, A4) : explications, exemples, captures d'écran, exercices corrigés, aide-mémoire |
| `projet/MesRecettes/` | Le projet Android Studio avec tout le code du cours (à ouvrir avec **File › Open…**) |
| `captures/` | Les 12 captures d'écran du cours (PNG) |
| `src/` | Les sources du PDF |

## Le projet MesRecettes
Au lancement, un menu ouvre chacun des 5 exemples :

| Dossier | Chapitre | Sujet |
|---|---|---|
| `exemple1/` | 3 | Aller d'un écran à l'autre (+ exercice 1) |
| `exemple2/` | 4 | Passer un argument (+ exercice 2) |
| `exemple3/` | 5 | Gérer la pile avec `popUpTo` (+ exercice 3) |
| `exemple4/` | 6 | Barre d'onglets en bas |
| `exemple5/` | 7 | Renvoyer un résultat |

AGP 8.7.3 · Gradle 8.11.1 · Kotlin 2.0.21 · Navigation Compose 2.8.5 · minSdk 24.

## Régénérer le PDF
```
cd src
python3 genere_cours.py   # recrée cours.html à partir du code du projet
node build.js             # captures + PDF (Playwright + Chromium)
```
