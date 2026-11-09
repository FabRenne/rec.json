# Génère cours.html à partir du texte ci-dessous et du code du projet MesRecettes.
# Usage : python3 genere_cours.py   (puis node build.js pour le PDF)
import html, os, re, textwrap

ICI = os.path.dirname(os.path.abspath(__file__))
KT = os.path.join(ICI, '..', 'projet', 'MesRecettes', 'app', 'src', 'main', 'java', 'com', 'exemple', 'mesrecettes')


def fichier(chemin, debut=None, fin=None, sans_imports=True):
    """Lit un fichier du projet (éventuellement un extrait entre deux marqueurs)."""
    txt = open(os.path.join(KT, chemin), encoding='utf-8').read()
    if sans_imports:
        lignes = [l for l in txt.splitlines()
                  if not l.startswith(('package ', 'import ', '@file:'))]
        txt = '\n'.join(lignes).strip('\n')
    if debut:
        i = txt.index(debut)
        txt = txt[txt.rfind('\n', 0, i) + 1:]
    if fin:
        txt = txt[:txt.index(fin) + len(fin)]
    txt = textwrap.dedent(txt)
    return re.sub(r'\n{3,}', '\n\n', txt).strip('\n')


def code(src, titre=None):
    t = f' data-file="{html.escape(titre)}"' if titre else ''
    return f'<pre class="kt"{t}>{html.escape(src)}</pre>'


def img(nom, legende, h=None):
    style = f' style="height:{h}px"' if h else ''
    return f'<figure style="margin:0"><img src="../captures/{nom}.png" alt=""{style}><figcaption>{legende}</figcaption></figure>'


def duo(a, fleche, b, h=300):
    return f'<div class="duo">{img(*a, h=h)}<div class="arrow">→<small>{fleche}</small></div>{img(*b, h=h)}</div>'


def pile(*etats):
    """Petit schéma de pile : chaque état = (titre, [écrans de bas en haut])."""
    out = '<div class="diagram">'
    for i, (titre, ecrans, *opt) in enumerate(etats):
        if i:
            out += '<div class="arrow-r">→</div>'
        cases = ''.join(f'<div class="stack-item{" top" if j == len(ecrans) - 1 else ""}">{e}</div>'
                        for j, e in reversed(list(enumerate(ecrans))))
        if not ecrans:
            cases = '<div class="stack-empty">(vide)</div>'
        out += f'<div class="stack"><b>{titre}</b>{cases}{f"<small>{opt[0]}</small>" if opt else ""}</div>'
    return out + '</div>'


def box(kind, titre, texte):
    return f'<div class="box {kind}"><b class="t">{titre}</b>{texte}</div>'


C = []  # morceaux du document
add = C.append

# =============================================================== COUVERTURE
add('''<section class="cover">
  <div class="kicker">COURS DÉBUTANT · ANDROID</div>
  <h1>La <span>navigation</span><br>avec Jetpack Compose</h1>
  <div class="sub">Passer d'un écran à l'autre, transmettre des informations, gérer le bouton Retour et créer une barre d'onglets — pas à pas, avec des exemples simples et des captures d'écran.</div>
  <div><span class="pill">Kotlin</span><span class="pill">Jetpack Compose</span><span class="pill">Navigation Compose</span><span class="pill">Projet Android Studio inclus</span></div>
  <div class="imgs">
    <img src="../captures/ex2-liste.png" alt=""><img src="../captures/ex5-detail.png" alt=""><img src="../captures/ex4-favoris.png" alt="">
  </div>
</section>''')

# =============================================================== SOMMAIRE
toc = [
    ("Les idées de base", "vocabulaire"), ("Préparer le projet", "Gradle"),
    ("Exemple 1 — Aller d'un écran à l'autre", "navigate"), ("Exemple 2 — Passer une information", "arguments"),
    ("Exemple 3 — Maîtriser la pile d'écrans", "popUpTo"), ("Exemple 4 — Une barre d'onglets en bas", "NavigationBar"),
    ("Exemple 5 — Renvoyer un résultat", "SavedStateHandle"), ("Les bonnes pratiques", "et pour aller plus loin"),
    ("Les erreurs fréquentes du débutant", "et leurs solutions"), ("Exercices corrigés", "pratique"),
    ("Aide-mémoire", "à garder sous la main"), ("Le projet Android Studio", "mode d'emploi"),
]
add('<section class="chapter toc"><h1>Sommaire</h1><ol>' +
    ''.join(f'<li><span class="n">{i}</span>{t}<span class="d">{d}</span></li>' for i, (t, d) in enumerate(toc, 1)) +
    '</ol>' +
    box('info', 'À qui s\'adresse ce cours ?', 'À toute personne qui connaît les bases de Kotlin et qui sait afficher un texte et un bouton avec Compose (<code>@Composable</code>, <code>Text</code>, <code>Button</code>, <code>Column</code>). Aucune connaissance de la navigation n\'est nécessaire.') +
    box('tip', 'Un projet prêt à lancer', 'Tout le code de ce cours se trouve dans le projet Android Studio <b>MesRecettes</b> fourni avec ce PDF (dossier <code>projet/</code>). Chaque exemple s\'ouvre depuis un menu. Le chapitre 12 explique comment l\'ouvrir.') +
    box('warn', 'À propos des captures d\'écran', 'Les captures sont des rendus fidèles des écrans produits par le code de chaque exemple (thème Material 3 par défaut). Selon votre téléphone et votre version d\'Android, l\'apparence peut varier légèrement.') +
    '</section>')

# =============================================================== CH 1
add('''<section class="chapter"><div class="chapnum">Chapitre 1</div><h1>Les idées de base</h1>
<p>Presque toutes les applications ont plusieurs écrans : une liste, un détail, un profil, des paramètres… <b>Naviguer</b>, c'est passer de l'un à l'autre — et savoir revenir en arrière.</p>
<p>Avec Jetpack Compose, un écran n'est rien d'autre qu'une <b>fonction <code>@Composable</code></b>. La bibliothèque <b>Navigation Compose</b> s'occupe de choisir quelle fonction afficher. Elle repose sur quatre notions :</p>
<table>
<tr><th style="width:20%">Notion</th><th style="width:30%">Image</th><th>Rôle</th></tr>
<tr><td><b>Destination</b><br>(ou route)</td><td>Une adresse sur une carte</td><td>Le nom d'un écran. En Kotlin, c'est un <code>object</code> ou une <code>data class</code> marqué <code>@Serializable</code>.</td></tr>
<tr><td><b>NavHost</b></td><td>La carte de la ville</td><td>Le Composable qui liste <b>toutes</b> les destinations et affiche celle qui est active.</td></tr>
<tr><td><b>NavController</b></td><td>Le chauffeur</td><td>L'objet qui <b>change</b> d'écran : <code>navigate(...)</code> pour avancer, <code>popBackStack()</code> pour reculer.</td></tr>
<tr><td><b>Pile de retour</b><br>(back stack)</td><td>Une pile d'assiettes</td><td>La liste des écrans visités. Le nouvel écran est posé <b>dessus</b> ; « Retour » retire celui du dessus.</td></tr>
</table>
<div class="diagram">
  <div class="node fill"><b>NavController</b>navigate(Profil)</div><div class="arrow-r">→</div>
  <div class="nest" style="flex:1"><span class="lbl">NAVHOST</span>
    <div style="display:flex;gap:8pt;justify-content:center">
      <div class="node"><b>Accueil</b>EcranAccueil()</div><div class="node" style="border-width:3px;background:#EEF0FF"><b>Profil ← affiché</b>EcranProfil()</div><div class="node"><b>Parametres</b>EcranParametres()</div>
    </div></div>
</div>
<p style="text-align:center;font-size:8.6pt;color:var(--mute);margin-top:-6pt">Schéma : le NavController demande au NavHost d'afficher une destination.</p>
<h2>La pile de retour, en images</h2>''' +
    pile(("Au lancement", ["Accueil"]), ("navigate(Profil)", ["Accueil", "Profil"]), ("navigate(Parametres)", ["Accueil", "Profil", "Parametres"]), ("Retour", ["Accueil", "Profil"])) +
    '<p>L\'écran affiché est toujours celui <b>du haut</b> de la pile (en couleur). Quand la pile ne contient plus qu\'un écran, « Retour » ferme l\'application.</p>' +
    box('info', 'Navigation « typée »', 'Depuis la version 2.8, Navigation Compose utilise des <b>classes Kotlin</b> comme destinations, au lieu de chaînes de caractères comme <code>"detail/{id}"</code>. Le compilateur vérifie alors vos erreurs à votre place. C\'est la méthode recommandée, celle de ce cours. Sur Internet, vous verrez encore l\'ancienne syntaxe avec des chaînes : le principe est le même.') +
    '</section>')

# =============================================================== CH 2
add('<section class="chapter"><div class="chapnum">Chapitre 2</div><h1>Préparer le projet</h1>'
    '<p>Partez d\'un projet Android Studio créé avec le modèle <b>Empty Activity</b> (Compose déjà activé). Il faut ajouter deux choses : la bibliothèque <b>Navigation Compose</b> et le plugin de <b>sérialisation</b> Kotlin, qui transforme vos destinations en « adresses ».</p>'
    '<figure class="wide"><img src="../captures/studio-arbo.png" alt=""><figcaption>Capture 1 — Organisation du projet MesRecettes fourni avec le cours.</figcaption></figure>'
    '<h2>Étape 1 — Les versions</h2>' +
    code('''[versions]
kotlin = "2.0.21"
navigation = "2.8.5"
serialization = "1.7.3"

[libraries]
androidx-navigation-compose = { group = "androidx.navigation", name = "navigation-compose", version.ref = "navigation" }
kotlinx-serialization-json = { group = "org.jetbrains.kotlinx", name = "kotlinx-serialization-json", version.ref = "serialization" }

[plugins]
kotlin-serialization = { id = "org.jetbrains.kotlin.plugin.serialization", version.ref = "kotlin" }''', 'gradle/libs.versions.toml (extrait)') +
    '<h2>Étape 2 — Le module app</h2>' +
    code('''plugins {
    // ... plugins déjà présents
    alias(libs.plugins.kotlin.serialization)  // indispensable pour les routes @Serializable
}

dependencies {
    // ... dépendances déjà présentes
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)
}''', 'app/build.gradle.kts (extrait)') +
    '<p>Dans le fichier <code>build.gradle.kts</code> <b>racine</b>, déclarez aussi le plugin : <code>alias(libs.plugins.kotlin.serialization) apply false</code>. Cliquez enfin sur <b>Sync Now</b>.</p>' +
    box('warn', 'Même version que Kotlin', 'Le plugin de sérialisation doit avoir <b>exactement</b> la même version que votre plugin Kotlin (ici <code>2.0.21</code>). C\'est pour cela qu\'il réutilise <code>version.ref = "kotlin"</code>.') +
    box('tip', 'Mettre à jour', 'Des versions plus récentes existent sûrement quand vous lisez ce cours. Android Studio souligne les dépendances obsolètes : <b>Alt + Entrée</b> propose la mise à jour.') +
    '</section>')

# =============================================================== CH 3
add('<section class="chapter"><div class="chapnum">Chapitre 3 · Exemple 1</div><h1>Aller d\'un écran à l\'autre</h1>'
    '<p><b>Objectif :</b> un écran <b>Accueil</b> avec un bouton « Voir mon profil » qui ouvre un écran <b>Profil</b>. Le bouton Retour revient à l\'accueil.</p>' +
    duo(('ex1-accueil', 'Capture 2 — Destination Accueil'), 'navigate(<br>Profil)', ('ex1-profil', 'Capture 3 — Destination Profil')) +
    '<h2>Étape 1 — Déclarer les destinations</h2><p>Une destination sans information à transmettre est un simple <code>object</code> :</p>' +
    code(fichier('exemple1/Routes.kt'), 'exemple1/Routes.kt') +
    '<h2>Étape 2 — Écrire les écrans</h2><p>Les écrans sont des Composables ordinaires. Point essentiel : ils <b>ne connaissent pas</b> le NavController. Ils reçoivent des fonctions (<code>onVoirProfil</code>, <code>onRetour</code>) qu\'ils appellent quand on clique.</p>' +
    code(fichier('exemple1/Ecrans.kt', '@Composable\nfun EcranAccueil', '// Exercice 1').replace('// Exercice 1', '').rstrip(), 'exemple1/Ecrans.kt (extrait)') +
    '<p>Le petit composant <code>BarreDuHaut(titre, onRetour)</code> est une <code>TopAppBar</code> avec une flèche ← quand <code>onRetour</code> est fourni. Il se trouve dans <code>ui/Composants.kt</code>.</p>'
    '<h2>Étape 3 — Le NavHost</h2><p>C\'est ici que tout se relie : on crée le NavController, on liste les destinations, et on dit quoi faire quand un écran appelle ses fonctions.</p>' +
    code(fichier('exemple1/Exemple1Activity.kt'), 'exemple1/Exemple1Activity.kt') +
    '<h2>Ce qu\'il faut retenir</h2><ol>'
    '<li><code>rememberNavController()</code> crée le « chauffeur ».</li>'
    '<li><code>NavHost(navController, startDestination = Accueil) { ... }</code> liste les écrans ; <code>startDestination</code> est le premier affiché.</li>'
    '<li><code>composable&lt;Accueil&gt; { ... }</code> associe une destination à un Composable.</li>'
    '<li><code>navController.navigate(Profil)</code> avance ; <code>navController.popBackStack()</code> recule.</li></ol>' +
    '<table><tr><th>Méthode</th><th>Effet</th></tr>'
    '<tr><td><code>navigate(destination)</code></td><td>Pose un nouvel écran sur la pile.</td></tr>'
    '<tr><td><code>popBackStack()</code></td><td>Retire l\'écran du dessus (comme le bouton Retour du téléphone).</td></tr>'
    '<tr><td><code>navigateUp()</code></td><td>Remonte d\'un niveau ; presque identique à <code>popBackStack()</code> dans une appli simple. Idéal pour la flèche ← de la barre du haut.</td></tr></table>' +
    box('tip', 'Le bouton Retour du téléphone', 'Vous n\'avez <b>rien</b> à coder : le NavHost gère automatiquement le bouton (ou le geste) Retour d\'Android.') +
    '</section>')

# =============================================================== CH 4
add('<section class="chapter"><div class="chapnum">Chapitre 4 · Exemple 2</div><h1>Passer une information à un écran</h1>'
    '<p><b>Objectif :</b> une liste de recettes. Quand on touche une recette, l\'écran de détail doit savoir <b>laquelle</b> afficher. On va lui transmettre son identifiant (<code>id</code>).</p>' +
    duo(('ex2-liste', 'Capture 4 — ListeRecettes'), 'navigate(<br>Detail(id = 1))', ('ex2-detail', 'Capture 5 — Detail')) +
    '<h2>Étape 1 — Une destination avec argument = une data class</h2>' +
    code(fichier('exemple2/Routes.kt'), 'exemple2/Routes.kt') +
    '<h2>Étape 2 — Envoyer et recevoir l\'argument</h2>' +
    code(fichier('exemple2/Exemple2Activity.kt', '@Composable'), 'exemple2/Exemple2Activity.kt (extrait)') +
    '<div class="diagram"><div class="node"><b>1. Clic</b>EcranListe appelle<br>onRecetteClique(recette)</div><div class="arrow-r">→</div><div class="node fill"><b>2. Envoi</b>navigate(<br>Detail(id = 1))</div><div class="arrow-r">→</div><div class="node fill"><b>3. Réception</b>backStackEntry<br>.toRoute&lt;Detail&gt;()</div><div class="arrow-r">→</div><div class="node"><b>4. Utilisation</b>recetteParId(<br>detail.id)</div></div>'
    '<h2>Étape 3 — L\'écran de détail</h2><p>L\'écran reçoit directement la <code>Recette</code> à afficher. Il ne sait pas d\'où elle vient : c\'est le NavHost qui l\'a retrouvée.</p>' +
    code(fichier('exemple2/Ecrans.kt', '@Composable\nfun EcranDetail', '// Exercice 2').replace('// Exercice 2', '').rstrip(), 'exemple2/Ecrans.kt (extrait)') +
    box('err', 'Ne transmettez pas un objet entier', 'On envoie <code>Detail(id = 1)</code>, <b>pas</b> la recette complète. Les arguments sont enregistrés par Android (pour survivre à une rotation, par exemple) : ils doivent rester <b>petits et simples</b>. On transmet un identifiant, puis on retrouve l\'objet (ici avec <code>recetteParId</code>, plus tard dans une base de données).') +
    '<h2>Quels types peut-on utiliser ?</h2><table><tr><th>Type</th><th>Exemple</th></tr>'
    '<tr><td><code>Int</code>, <code>Long</code>, <code>Float</code>, <code>Double</code>, <code>Boolean</code></td><td><code>data class Detail(val id: Int)</code></td></tr>'
    '<tr><td><code>String</code></td><td><code>data class Auteur(val nom: String)</code></td></tr>'
    '<tr><td>Valeur facultative (avec une valeur par défaut)</td><td><code>data class Recherche(val texte: String = "")</code><br>→ <code>navigate(Recherche())</code> ou <code>navigate(Recherche("tarte"))</code></td></tr>'
    '<tr><td>Valeur qui peut être absente</td><td><code>data class Filtre(val categorie: String? = null)</code></td></tr>'
    '<tr><td>Listes simples</td><td><code>data class Selection(val ids: List&lt;Int&gt;)</code></td></tr></table>' +
    '</section>')

# =============================================================== CH 5
add('<section class="chapter"><div class="chapnum">Chapitre 5 · Exemple 3</div><h1>Maîtriser la pile d\'écrans</h1>'
    '<p><b>Situation :</b> un écran de <b>Connexion</b>. Une fois connecté, on arrive sur l\'accueil. Problème : avec un simple <code>navigate</code>, un appui sur Retour… ramène à l\'écran de connexion ! Ce n\'est pas ce qu\'on veut.</p>' +
    duo(('ex3-connexion', 'Capture 6 — Connexion'), 'navigate +<br>popUpTo', ('ex3-accueil', 'Capture 7 — Accueil connecté')) +
    '<h2>Le problème, puis la solution</h2>' +
    pile(("navigate simple", ["Connexion", "Accueil"], "❌ Retour → Connexion"), ("avec popUpTo + inclusive", ["Accueil"], "✅ Retour → quitte l'appli")) +
    '<p>L\'option <b><code>popUpTo</code></b> retire des écrans de la pile <b>avant</b> d\'ajouter le nouveau. On l\'écrit dans un bloc <code>{ }</code> juste après <code>navigate</code> :</p>' +
    code(fichier('exemple3/Exemple3Activity.kt', 'composable<Connexion>', 'composable<AccueilConnecte>').replace('composable<AccueilConnecte>', '').rstrip(), 'exemple3/Exemple3Activity.kt (extrait)') +
    '<table><tr><th>Option</th><th>Ce qu\'elle fait</th></tr>'
    '<tr><td><code>popUpTo&lt;X&gt;()</code></td><td>Retire tous les écrans posés <b>au-dessus</b> de X.</td></tr>'
    '<tr><td><code>popUpTo&lt;X&gt; { inclusive = true }</code></td><td>Pareil, et retire <b>X lui-même</b>.</td></tr>'
    '<tr><td><code>popUpTo(navController.graph.id) { inclusive = true }</code></td><td>Vide <b>toute</b> la pile.</td></tr>'
    '<tr><td><code>launchSingleTop = true</code></td><td>Si l\'écran demandé est déjà en haut de la pile, on ne le rajoute pas une 2<sup>e</sup> fois (utile contre les doubles clics).</td></tr></table>' +
    '<h2>L\'écran de connexion</h2><p>Il garde ce que l\'on tape avec <code>rememberSaveable</code> (le texte survit à la rotation), puis appelle <code>onConnecte(prenom)</code>.</p>' +
    code(fichier('exemple3/Ecrans.kt', '@Composable\nfun EcranConnexion', '"Se connecter") }') + '\n            // … petit texte d\'aide\n        }\n    }\n}', 'exemple3/Ecrans.kt (extrait)') +
    box('info', 'Et la déconnexion ?', 'C\'est l\'exercice 3 (chapitre 10) : il faut revenir à la connexion en vidant <b>toute</b> la pile, sinon Retour ramènerait à l\'accueil connecté.') +
    '</section>')

# =============================================================== CH 6
add('<section class="chapter"><div class="chapnum">Chapitre 6 · Exemple 4</div><h1>Une barre d\'onglets en bas</h1>'
    '<p><b>Objectif :</b> trois onglets — Recettes, Favoris, Profil — toujours visibles en bas de l\'écran, comme dans la plupart des applications.</p>'
    '<div class="duo trio">' + img('ex4-recettes', 'Capture 8 — Recettes', 270) + img('ex4-favoris', 'Capture 9 — Favoris', 270) + img('ex4-profil', 'Capture 10 — Profil', 270) + '</div>'
    '<h2>Étape 1 — Les destinations et la liste des onglets</h2>' +
    code(fichier('exemple4/Routes.kt'), 'exemple4/Routes.kt') +
    '<h2>Étape 2 — Le Scaffold commun et le NavHost</h2><p>Cette fois, la barre du haut et la barre du bas sont dessinées <b>une seule fois</b>, autour du NavHost. Seul le contenu du milieu change.</p>' +
    code(fichier('exemple4/Exemple4Activity.kt', '@Composable'), 'exemple4/Exemple4Activity.kt (extrait)') +
    '<h2>Les trois lignes magiques</h2><table><tr><th>Ligne</th><th>Pourquoi ?</th></tr>'
    '<tr><td><code>popUpTo(...findStartDestination().id) { saveState = true }</code></td><td>Évite d\'empiler les onglets à l\'infini (Recettes → Favoris → Profil → Favoris…). Retour depuis un onglet ramène au premier, puis quitte l\'appli.</td></tr>'
    '<tr><td><code>launchSingleTop = true</code></td><td>Toucher l\'onglet déjà sélectionné ne crée pas de doublon.</td></tr>'
    '<tr><td><code>restoreState = true</code></td><td>En revenant sur un onglet, on retrouve son état : position dans la liste, interrupteur du profil…</td></tr></table>' +
    box('info', 'Savoir quel onglet est actif', '<code>navController.currentBackStackEntryAsState()</code> donne l\'écran affiché et se met à jour à chaque navigation. On compare sa destination avec la route de chaque onglet grâce à <code>hasRoute(onglet.route::class)</code>.') +
    box('warn', 'Pas de Scaffold dans les écrans d\'onglets', 'Les écrans <code>EcranRecettes</code>, <code>EcranFavoris</code> et <code>EcranProfil</code> n\'ont pas leur propre <code>Scaffold</code> : sinon on aurait deux barres du haut. N\'oubliez pas <code>Modifier.padding(padding)</code> sur le NavHost, sinon le contenu passe sous les barres.') +
    '</section>')

# =============================================================== CH 7
add('<section class="chapter"><div class="chapnum">Chapitre 7 · Exemple 5</div><h1>Renvoyer un résultat</h1>'
    '<p><b>Objectif :</b> depuis le détail d\'une recette, on ouvre un écran <b>Noter</b>. L\'utilisateur choisit une note de 1 à 5, valide… et la note doit apparaître sur l\'écran de détail.</p>' +
    duo(('ex5-noter', 'Capture 11 — Noter'), 'note renvoyée<br>+ popBackStack', ('ex5-detail', 'Capture 12 — Retour au détail')) +
    '<h2>Le principe : une « boîte aux lettres »</h2><p>Chaque écran de la pile possède une petite boîte de rangement, le <b><code>savedStateHandle</code></b>. L\'écran Noter dépose la note dans la boîte de l\'écran <b>précédent</b>, puis se ferme. L\'écran de détail, qui surveille sa boîte, voit la note arriver.</p>'
    '<div class="diagram"><div class="node"><b>Noter</b>previousBackStackEntry<br>.savedStateHandle["note"] = 4</div><div class="arrow-r">→</div><div class="node fill orange"><b>Boîte de Detail</b>note = 4</div><div class="arrow-r">→</div><div class="node"><b>Detail</b>getStateFlow("note", 0)<br>.collectAsState() → 4 ★</div></div>' +
    code(fichier('exemple5/Exemple5Activity.kt', 'composable<Detail>').rsplit('\n', 2)[0], 'exemple5/Exemple5Activity.kt (extrait)') +
    '<h2>L\'écran Noter</h2>' +
    code(fichier('exemple5/Ecrans.kt', '@Composable\nfun EcranNoter'), 'exemple5/Ecrans.kt (extrait)') +
    '<p>Les étoiles cliquables viennent du composant <code>Etoiles(note, onChoix)</code> de <code>ui/Composants.kt</code>.</p>' +
    box('tip', 'Pourquoi pas une simple variable globale ?', 'Parce que la boîte <code>savedStateHandle</code> est <b>sauvegardée par Android</b> : la note survit à une rotation de l\'écran ou à la mise en arrière-plan de l\'appli. Une variable globale, elle, serait perdue.') +
    box('info', 'Quand utiliser un ViewModel à la place ?', 'Pour un petit résultat ponctuel (une note, un choix), le <code>savedStateHandle</code> est parfait. Pour des données partagées par plusieurs écrans (un panier, un utilisateur connecté), préférez un <b>ViewModel</b> commun (voir chapitre 8).') +
    '</section>')

# =============================================================== CH 8
add('<section class="chapter"><div class="chapnum">Chapitre 8</div><h1>Les bonnes pratiques</h1>'
    '<h2>1. Ne donnez jamais le NavController à vos écrans</h2>' +
    '<div class="split"><div>' + box('err', 'À éviter', code('''@Composable
fun EcranListe(navController: NavController) {
    Button(onClick = {
        navController.navigate(Detail(1))
    }) { Text("Ouvrir") }
}''')) + '</div><div>' + box('tip', 'À préférer', code('''@Composable
fun EcranListe(onOuvrir: (Int) -> Unit) {
    Button(onClick = { onOuvrir(1) }) {
        Text("Ouvrir")
    }
}''')) + '</div></div>' +
    '<p>Avec des lambdas, l\'écran est <b>réutilisable</b> (même écran dans deux parcours différents), <b>testable</b>, et visible dans l\'<b>aperçu</b> <code>@Preview</code> d\'Android Studio sans faux NavController. Toute la navigation reste au même endroit : le NavHost.</p>'
    '<h2>2. Un seul NavHost par « niveau »</h2><p>En général, un seul <code>NavHost</code> dans l\'Activity suffit. Créez le NavController <b>une fois</b>, tout en haut, avec <code>rememberNavController()</code> — jamais dans un écran.</p>'
    '<h2>3. Regrouper des écrans : les graphes imbriqués</h2><p>Quand l\'application grandit, on peut regrouper des écrans liés (connexion, inscription, mot de passe oublié) dans un sous-graphe :</p>' +
    code('''@Serializable object Authentification   // le nom du groupe

NavHost(navController, startDestination = Authentification) {
    navigation<Authentification>(startDestination = Connexion) {
        composable<Connexion> { /* ... */ }
        composable<Inscription> { /* ... */ }
    }
    composable<Accueil> { /* ... */ }
}

// Après la connexion, on retire tout le groupe d'un coup :
navController.navigate(Accueil) { popUpTo<Authentification> { inclusive = true } }''') +
    '<h2>4. Pour aller plus loin : lire l\'argument dans un ViewModel</h2><p>Dans une vraie application, l\'écran de détail a souvent un ViewModel. Il peut lire l\'argument lui-même grâce à son <code>SavedStateHandle</code> (dépendance <code>lifecycle-viewmodel-compose</code>) :</p>' +
    code('''class DetailViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    private val detail: Detail = savedStateHandle.toRoute()
    val recette = recetteParId(detail.id)
}

composable<Detail> {
    val vm: DetailViewModel = viewModel()
    EcranDetail(recette = vm.recette, /* ... */)
}''') +
    box('info', 'Liens profonds (deep links)', 'Navigation Compose permet aussi d\'ouvrir directement un écran depuis un lien web ou une notification (<code>deepLinks = listOf(navDeepLink&lt;Detail&gt;(basePath = "https://..."))</code>). C\'est une étape suivante, une fois les bases maîtrisées.') +
    '</section>')

# =============================================================== CH 9
erreurs = [
    ("1. « Serializer for class 'Detail' is not found »", "Il manque le plugin de sérialisation ou l'annotation. <br><b>Solution :</b> ajoutez <code>@Serializable</code> au-dessus de chaque destination, et le plugin <code>kotlin.serialization</code> dans Gradle (chapitre 2)."),
    ("2. « Destination ... cannot be found in navigation graph »", "Vous naviguez vers une destination qui n'a pas de <code>composable&lt;...&gt;</code> dans le NavHost. <br><b>Solution :</b> ajoutez-la dans le bloc <code>NavHost { }</code>."),
    ("3. Le bouton Retour ramène à l'écran de connexion", "<b>Solution :</b> <code>popUpTo&lt;Connexion&gt; { inclusive = true }</code> lors du <code>navigate</code> (chapitre 5)."),
    ("4. Les onglets s'empilent et Retour parcourt tous les onglets visités", "<b>Solution :</b> utilisez les trois options <code>popUpTo(...) { saveState = true }</code>, <code>launchSingleTop</code> et <code>restoreState</code> (chapitre 6)."),
    ("5. Un écran s'ouvre deux fois après un double clic", "<b>Solution :</b> <code>launchSingleTop = true</code>, ou désactivez le bouton pendant la navigation."),
    ("6. Le contenu passe sous la barre du bas", "<b>Solution :</b> transmettez le <code>padding</code> du Scaffold au NavHost : <code>NavHost(..., modifier = Modifier.padding(padding))</code>."),
    ("7. Le NavController est recréé et l'appli revient au début", "Vous avez écrit <code>NavHostController(context)</code> ou créé le contrôleur dans un écran. <br><b>Solution :</b> <code>val navController = rememberNavController()</code>, une seule fois, tout en haut."),
    ("8. « Unresolved reference: toRoute / composable »", "Il manque un import. <br><b>Solution :</b> <code>import androidx.navigation.toRoute</code> et <code>import androidx.navigation.compose.composable</code>. Dans Android Studio, <b>Alt + Entrée</b> sur le mot en rouge ajoute l'import."),
]
add('<section class="chapter"><div class="chapnum">Chapitre 9</div><h1>Les erreurs fréquentes du débutant</h1>' +
    ''.join(box('err', t, d) for t, d in erreurs) + '</section>')

# =============================================================== CH 10
add('<section class="chapter"><div class="chapnum">Chapitre 10</div><h1>Exercices corrigés</h1>'
    '<p>Les solutions sont déjà intégrées au projet <b>MesRecettes</b> (commentaires <code>// exercice N</code>) : essayez d\'abord sans regarder !</p>'
    '<div class="ex"><div class="lvl">Exercice 1 · Facile — dans exemple1</div>'
    '<p>Ajoutez un écran <b>Paramètres</b> (avec un interrupteur « Notifications »), accessible par un bouton depuis l\'écran Profil.</p><b>Correction</b>' +
    code('''// Routes.kt
@Serializable
object Parametres

// Dans le NavHost
composable<Profil> {
    EcranProfil(
        onRetour = { navController.popBackStack() },
        onParametres = { navController.navigate(Parametres) }
    )
}
composable<Parametres> {
    EcranParametres(onRetour = { navController.popBackStack() })
}''') + '<p>Pile après les deux clics : [Accueil, Profil, Parametres]. Deux appuis sur Retour ramènent à l\'accueil.</p></div>'
    '<div class="ex"><div class="lvl">Exercice 2 · Moyen — dans exemple2</div>'
    '<p>Sur le détail d\'une recette, ajoutez un bouton « Voir l\'auteur » qui ouvre un écran <b>Auteur</b> affichant le nom de l\'auteur et son nombre de recettes.</p><b>Correction</b>' +
    code('''@Serializable
data class Auteur(val nom: String)

// Dans composable<Detail> :
onVoirAuteur = { navController.navigate(Auteur(nom = recette.auteur)) }

composable<Auteur> { backStackEntry ->
    val auteur: Auteur = backStackEntry.toRoute()
    EcranAuteur(nom = auteur.nom, onRetour = { navController.popBackStack() })
}''') + '</div>'
    '<div class="ex"><div class="lvl">Exercice 3 · Défi — dans exemple3</div>'
    '<p>Ajoutez un bouton « Se déconnecter » sur l\'accueil connecté. Il ramène à l\'écran de connexion, et Retour doit alors <b>quitter</b> l\'application.</p><b>Correction</b>' +
    code(fichier('exemple3/Exemple3Activity.kt', 'onDeconnexion = {', 'inclusive = true }\n                    }\n                }')) +
    pile(("Avant", ["AccueilConnecte"]), ("Après la déconnexion", ["Connexion"], "Retour → quitte l'appli")) +
    '</div></section>')

# =============================================================== CH 11
add('<section class="chapter"><div class="chapnum">Chapitre 11</div><h1>Aide-mémoire</h1>'
    '<h3>Déclarer des destinations</h3>' + code('''@Serializable object Accueil                      // sans argument
@Serializable data class Detail(val id: Int)      // avec argument''') +
    '<h3>Le NavHost</h3>' + code('''val navController = rememberNavController()
NavHost(navController, startDestination = Accueil) {
    composable<Accueil> { EcranAccueil(onOuvrir = { navController.navigate(Detail(it)) }) }
    composable<Detail>  { entry -> EcranDetail(id = entry.toRoute<Detail>().id) }
}''') +
    '<h3>Naviguer</h3>' + code('''navController.navigate(Detail(42))      // avancer
navController.popBackStack()            // reculer
navController.navigateUp()              // flèche ← de la barre du haut''') +
    '<h3>Gérer la pile</h3>' + code('''navController.navigate(Accueil) {
    popUpTo<Connexion> { inclusive = true }   // retirer Connexion
    launchSingleTop = true                    // pas de doublon
}''') +
    '<h3>Onglets</h3>' + code('''navController.navigate(onglet.route) {
    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
    launchSingleTop = true
    restoreState = true
}''') +
    '<h3>Renvoyer un résultat</h3>' + code('''navController.previousBackStackEntry?.savedStateHandle?.set("note", 4)   // écran qui répond
val note by entry.savedStateHandle.getStateFlow("note", 0).collectAsState() // écran qui attend''') +
    '<h2>Check-list</h2><ul class="check">'
    '<li>Plugin <code>kotlin.serialization</code> + dépendance <code>navigation-compose</code> ajoutés.</li>'
    '<li>Chaque destination est un <code>object</code> ou une <code>data class</code> avec <code>@Serializable</code>.</li>'
    '<li>Chaque destination a son <code>composable&lt;...&gt;</code> dans le NavHost.</li>'
    '<li>Le NavController est créé une seule fois avec <code>rememberNavController()</code>.</li>'
    '<li>Les écrans reçoivent des lambdas, pas le NavController.</li>'
    '<li>On transmet des identifiants, pas des objets entiers.</li>'
    '<li>Les écrans à ne pas revoir (connexion, accueil de bienvenue) sont retirés avec <code>popUpTo</code>.</li></ul>'
    '</section>')

# =============================================================== CH 12
add('<section class="chapter"><div class="chapnum">Chapitre 12</div><h1>Le projet Android Studio</h1>'
    '<h2>Ouvrir et lancer le projet</h2><ol>'
    '<li>Décompressez l\'archive du cours.</li>'
    '<li>Dans Android Studio : <b>File › Open…</b> puis choisissez le dossier <code>projet/MesRecettes</code>.</li>'
    '<li>Attendez la fin de la synchronisation Gradle (barre de progression en bas).</li>'
    '<li>Choisissez un émulateur ou branchez un téléphone, puis cliquez sur ▶ <b>Run \'app\'</b>.</li></ol>'
    '<p>Au lancement, un <b>menu</b> propose les cinq exemples du cours. Chaque exemple est indépendant, dans son propre dossier.</p>'
    '<table><tr><th>Dossier</th><th>Chapitre</th><th>Ce que vous y trouvez</th></tr>'
    '<tr><td><code>data/</code></td><td>—</td><td>Les recettes (<code>Recette</code>, <code>recettes</code>, <code>recetteParId</code>).</td></tr>'
    '<tr><td><code>ui/</code></td><td>—</td><td>Composants communs : <code>BarreDuHaut</code>, <code>CarteRecette</code>, <code>ListeDeRecettes</code>, <code>Etoiles</code>.</td></tr>'
    '<tr><td><code>exemple1/</code></td><td>3</td><td>Accueil → Profil (+ exercice 1 : Paramètres).</td></tr>'
    '<tr><td><code>exemple2/</code></td><td>4</td><td>Liste → Détail avec argument (+ exercice 2 : Auteur).</td></tr>'
    '<tr><td><code>exemple3/</code></td><td>5</td><td>Connexion avec <code>popUpTo</code> (+ exercice 3 : déconnexion).</td></tr>'
    '<tr><td><code>exemple4/</code></td><td>6</td><td>Barre d\'onglets en bas.</td></tr>'
    '<tr><td><code>exemple5/</code></td><td>7</td><td>Noter une recette : renvoyer un résultat.</td></tr></table>' +
    '<h2>Configuration</h2><ul><li>Android Gradle Plugin 8.7.3, Gradle 8.11.1, Kotlin 2.0.21</li><li>Navigation Compose 2.8.5, Compose BOM 2024.12.01</li><li>minSdk 24 (Android 7.0), targetSdk 35</li></ul>' +
    box('tip', 'Expérimentez !', 'Le meilleur moyen d\'apprendre : modifiez le code. Retirez <code>inclusive = true</code> dans l\'exemple 3, ou <code>restoreState = true</code> dans l\'exemple 4, relancez, et observez la différence avec le bouton Retour.') +
    box('info', 'Pour aller plus loin', 'Documentation officielle Android (developer.android.com) : « Navigation avec Compose », « Sûreté du typage dans Navigation » et « Barre de navigation ».') +
    '<p style="text-align:center;margin-top:24pt;font-size:13pt;color:var(--p);font-weight:700">Bravo, vous savez maintenant naviguer dans une application Compose ! 🎉</p>'
    '</section>')

# ----------------------------------------------------------------- Assemblage
css = open(os.path.join(ICI, 'style.css'), encoding='utf-8').read()
css += '''
  .stack{display:flex;flex-direction:column;align-items:center;gap:3pt;min-width:100pt}
  .stack b{font-size:8.6pt;color:var(--mute);margin-bottom:2pt;font-family:"JetBrains Mono"}
  .stack-item{width:100%;text-align:center;border:1.5px solid var(--p);border-radius:6px;padding:3pt 6pt;font-size:9pt;background:#fff}
  .stack-item.top{background:var(--p);color:#fff;font-weight:600}
  .stack-empty{font-size:9pt;color:var(--mute)}
  .stack small{font-size:8pt;color:var(--mute);margin-top:2pt}
  .trio{gap:10pt}
  .box pre{margin:6pt 0 2pt}
'''
js = r'''
<script>
const KW = new Set("fun val var class object data if else for in return when while is as true false null private const override super this by import package".split(" "));
document.querySelectorAll("pre.kt").forEach(pre => {
  const src = pre.textContent;
  const re = /(\/\/[^\n]*)|("(?:\\.|[^"\\])*")|(@\w+)|(\b\d+(?:\.\d+)?\b)|(\b[A-Za-z_]\w*\b)/g;
  let out = "", last = 0, m;
  const esc = s => s.replace(/&/g,"&amp;").replace(/</g,"&lt;").replace(/>/g,"&gt;");
  while ((m = re.exec(src))) {
    out += esc(src.slice(last, m.index));
    const t = esc(m[0]);
    if (m[1]) out += `<span class="c">${t}</span>`;
    else if (m[2]) out += `<span class="s">${t}</span>`;
    else if (m[3]) out += `<span class="a">${t}</span>`;
    else if (m[4]) out += `<span class="n">${t}</span>`;
    else if (KW.has(m[0])) out += `<span class="k">${t}</span>`;
    else if (/^[A-Z]/.test(m[0])) out += `<span class="t">${t}</span>`;
    else out += t;
    last = re.lastIndex;
  }
  pre.innerHTML = out + esc(src.slice(last));
});
</script>'''
page = f'''<!doctype html>
<html lang="fr"><head><meta charset="utf-8"><title>La navigation avec Jetpack Compose</title>
<style>{css}</style></head><body>
{"".join(C)}
{js}
</body></html>'''
open(os.path.join(ICI, 'cours.html'), 'w', encoding='utf-8').write(page)
print('cours.html généré')
