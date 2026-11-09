import sys
from reportlab.lib.pagesizes import A4
from reportlab.lib.units import cm
from reportlab.lib import colors
from reportlab.lib.styles import ParagraphStyle
from reportlab.lib.enums import TA_CENTER, TA_LEFT
from reportlab.pdfbase import pdfmetrics
from reportlab.pdfbase.ttfonts import TTFont
from reportlab.platypus import (BaseDocTemplate, PageTemplate, Frame, Paragraph, Spacer, PageBreak,
    Preformatted, Table, TableStyle, Image, KeepTogether, NextPageTemplate, CondPageBreak)
from reportlab.platypus.tableofcontents import TableOfContents
from PIL import Image as PILImage

BASE = sys.argv[1]; CAP = BASE + "/captures/"; OUT = sys.argv[2]
F = "/usr/share/fonts/truetype/dejavu/"
pdfmetrics.registerFont(TTFont("Sans", F+"DejaVuSans.ttf"))
pdfmetrics.registerFont(TTFont("Sans-B", F+"DejaVuSans-Bold.ttf"))
pdfmetrics.registerFont(TTFont("Serif-B", F+"DejaVuSerif-Bold.ttf"))
pdfmetrics.registerFont(TTFont("Mono", F+"DejaVuSansMono.ttf"))
pdfmetrics.registerFont(TTFont("Mono-B", F+"DejaVuSansMono-Bold.ttf"))
from reportlab.pdfbase.pdfmetrics import registerFontFamily
registerFontFamily("Sans", normal="Sans", bold="Sans-B", italic="Sans", boldItalic="Sans-B")

VERT = colors.HexColor("#2E6B30"); VERT_CLAIR = colors.HexColor("#E6F2E1"); ENCRE = colors.HexColor("#1C211B")
GRIS = colors.HexColor("#5B6358"); CODE_BG = colors.HexColor("#F3F5F1"); CODE_BORD = colors.HexColor("#D5DBD0")

st = {
 "h1": ParagraphStyle("h1", fontName="Serif-B", fontSize=22, leading=28, textColor=VERT, spaceBefore=4, spaceAfter=12),
 "h2": ParagraphStyle("h2", fontName="Sans-B", fontSize=13.5, leading=18, textColor=ENCRE, spaceBefore=12, spaceAfter=6),
 "h2c": ParagraphStyle("h2c", fontName="Sans-B", fontSize=13.5, leading=18, textColor=ENCRE, spaceBefore=12, spaceAfter=6),
 "h1s": ParagraphStyle("h1s", fontName="Serif-B", fontSize=22, leading=28, textColor=VERT, spaceAfter=12),
 "p": ParagraphStyle("p", fontName="Sans", fontSize=10, leading=15, textColor=ENCRE, spaceAfter=7),
 "li": ParagraphStyle("li", fontName="Sans", fontSize=10, leading=15, textColor=ENCRE, leftIndent=14, bulletIndent=3, spaceAfter=3),
 "code": ParagraphStyle("code", fontName="Mono", fontSize=8.2, leading=11, textColor=ENCRE),
 "cap": ParagraphStyle("cap", fontName="Sans", fontSize=8.5, leading=11, textColor=GRIS, alignment=TA_CENTER),
 "box": ParagraphStyle("box", fontName="Sans", fontSize=9.5, leading=14, textColor=ENCRE),
 "td": ParagraphStyle("td", fontName="Sans", fontSize=9, leading=12, textColor=ENCRE),
 "tdc": ParagraphStyle("tdc", fontName="Mono", fontSize=8.5, leading=12, textColor=ENCRE),
}
W = A4[0] - 4*cm

def P(t, s="p"): return Paragraph(t, st[s])
def c(t): return f'<font name="Mono" color="#2E6B30">{t}</font>'
def puces(*items): return [Paragraph(i, st["li"], bulletText="•") for i in items]

def code(txt, titre=None):
    cell = []
    if titre: cell += [Paragraph(f'<font name="Mono-B" size="7.5" color="#5B6358">{titre}</font>', st["code"]), Spacer(1,4)]
    cell.append(Preformatted(txt.strip("\n"), st["code"]))
    rows = [[cell]]
    t = Table(rows, colWidths=[W])
    t.setStyle(TableStyle([("BACKGROUND",(0,0),(-1,-1),CODE_BG),("BOX",(0,0),(-1,-1),0.6,CODE_BORD),
        ("LEFTPADDING",(0,0),(-1,-1),9),("RIGHTPADDING",(0,0),(-1,-1),9),("TOPPADDING",(0,0),(-1,-1),5),("BOTTOMPADDING",(0,-1),(-1,-1),7),
        ("LINEABOVE",(0,0),(-1,0),3,VERT)]))
    return [t, Spacer(1,8)]

def encadre(type_, txt):
    coul = {"Astuce": ("#E6F2E1","#2E6B30"), "Attention": ("#FDECE8","#BA1A1A"), "À retenir": ("#E3F1F3","#38656A"), "Exercice": ("#FFF4E0","#8A5100")}[type_]
    t = Table([[Paragraph(f'<b><font color="{coul[1]}">{type_}</font></b><br/>{txt}', st["box"])]], colWidths=[W])
    t.setStyle(TableStyle([("BACKGROUND",(0,0),(-1,-1),colors.HexColor(coul[0])),("LINEBEFORE",(0,0),(0,-1),4,colors.HexColor(coul[1])),
        ("LEFTPADDING",(0,0),(-1,-1),12),("RIGHTPADDING",(0,0),(-1,-1),10),("TOPPADDING",(0,0),(-1,-1),8),("BOTTOMPADDING",(0,0),(-1,-1),9)]))
    return [Spacer(1,4), t, Spacer(1,10)]

def img(nom, larg):
    w, h = PILImage.open(CAP+nom).size
    i = Image(CAP+nom, width=larg, height=larg*h/w)
    return i

def captures(items, larg=None, hmax=None):
    n = len(items); larg = larg or min(7.2*cm, (W - (n-1)*0.6*cm)/n)
    cells = []; caps = []
    for nom, legende in items:
        i = img(nom, larg)
        if hmax and i.drawHeight > hmax:
            r = hmax / i.drawHeight; i.drawHeight *= r; i.drawWidth *= r
        cadre = Table([[i]], colWidths=[i.drawWidth]); cadre.setStyle(TableStyle([("BOX",(0,0),(-1,-1),0.8,colors.HexColor("#9AA396")),("LEFTPADDING",(0,0),(-1,-1),0),("RIGHTPADDING",(0,0),(-1,-1),0),("TOPPADDING",(0,0),(-1,-1),0),("BOTTOMPADDING",(0,0),(-1,-1),0)]))
        cells.append([cadre, Spacer(1,5), P(legende, "cap")])
    t = Table([cells], colWidths=[W/n]*n)
    t.setStyle(TableStyle([("ALIGN",(0,0),(-1,-1),"CENTER"),("VALIGN",(0,0),(-1,-1),"TOP")]))
    return [Spacer(1,4), t, Spacer(1,12)]

def tableau(entetes, lignes, largeurs, mono_col=0):
    data = [[Paragraph(f"<b>{e}</b>", st["td"]) for e in entetes]]
    for l in lignes:
        data.append([Paragraph(v, st["tdc"] if j == mono_col else st["td"]) for j, v in enumerate(l)])
    t = Table(data, colWidths=largeurs, repeatRows=1)
    t.setStyle(TableStyle([("BACKGROUND",(0,0),(-1,0),VERT_CLAIR),("GRID",(0,0),(-1,-1),0.5,CODE_BORD),("VALIGN",(0,0),(-1,-1),"TOP"),
        ("ROWBACKGROUNDS",(0,1),(-1,-1),[colors.white, colors.HexColor("#FAFBF8")]),("TOPPADDING",(0,0),(-1,-1),4),("BOTTOMPADDING",(0,0),(-1,-1),5)]))
    return [t, Spacer(1,10)]

class Doc(BaseDocTemplate):
    def __init__(self, fn):
        super().__init__(fn, pagesize=A4, leftMargin=2*cm, rightMargin=2*cm, topMargin=2*cm, bottomMargin=2*cm,
            title="Les thèmes en Jetpack Compose — cours pour débutants", author="Cours Jetpack Compose", subject="Thèmes Material 3 en Jetpack Compose")
        fr = Frame(self.leftMargin, self.bottomMargin, self.width, self.height, id="f")
        self.addPageTemplates([PageTemplate("couv", [fr], onPage=self.couv), PageTemplate("std", [fr], onPage=self.pied)])
        self.chap = 0
    def couv(self, cv, doc):
        cv.saveState(); cv.setFillColor(VERT); cv.rect(0, A4[1]-11*cm, A4[0], 11*cm, fill=1, stroke=0)
        cv.setFillColor(colors.HexColor("#B1F2A9")); cv.circle(A4[0]-3*cm, A4[1]-9.5*cm, 2.2*cm, fill=1, stroke=0)
        cv.setFillColor(colors.HexColor("#38656A")); cv.circle(A4[0]-1.2*cm, A4[1]-7.8*cm, 1.1*cm, fill=1, stroke=0)
        cv.setFillColor(colors.white); cv.setFont("Sans", 12); cv.drawString(2*cm, A4[1]-3*cm, "COURS POUR DÉBUTANTS · ANDROID")
        cv.setFont("Serif-B", 34); cv.drawString(2*cm, A4[1]-5*cm, "Les thèmes en")
        cv.drawString(2*cm, A4[1]-6.4*cm, "Jetpack Compose")
        cv.setFont("Sans", 13); cv.drawString(2*cm, A4[1]-8*cm, "Couleurs, typographie, formes, mode sombre")
        cv.drawString(2*cm, A4[1]-8.7*cm, "avec Material 3 — exemples et captures d'écran")
        cv.restoreState()
    def pied(self, cv, doc):
        cv.saveState(); cv.setStrokeColor(CODE_BORD); cv.line(2*cm, 1.5*cm, A4[0]-2*cm, 1.5*cm)
        cv.setFont("Sans", 8); cv.setFillColor(GRIS); cv.drawString(2*cm, 1.05*cm, "Les thèmes en Jetpack Compose")
        cv.drawRightString(A4[0]-2*cm, 1.05*cm, f"{doc.page}"); cv.restoreState()
    def afterFlowable(self, f):
        if isinstance(f, Paragraph) and f.style.name in ("h1", "h2"):
            lvl = 0 if f.style.name == "h1" else 1
            txt = f.getPlainText(); key = f"k{id(f)}"
            self.canv.bookmarkPage(key); self.canv.addOutlineEntry(txt, key, lvl, closed=lvl == 0)
            self.notify("TOCEntry", (lvl, txt, self.page))

S = []
def H1(t): S.append(PageBreak()); S.append(P(t, "h1"))
PEND = []
def H2(t): S.append(CondPageBreak(4*cm)); PEND.append(P(t, "h2"))
def add(*x):
    for e in x:
        L = e if isinstance(e, list) else [e]
        if PEND:
            S.append(KeepTogether(PEND + L[:1])); PEND.clear(); L = L[1:]
        S.extend(L)

# ---------------- Couverture ----------------
S.append(Spacer(1, 10.5*cm))
add(P("Ce que vous allez apprendre", "h2c"))
add(puces("Comprendre ce qu'est un thème et à quoi sert " + c("MaterialTheme") + ".",
          "Définir vos propres couleurs avec " + c("ColorScheme") + " (rôles " + c("primary") + ", " + c("onPrimary") + "…).",
          "Gérer automatiquement le mode clair et le mode sombre.",
          "Personnaliser la typographie et les formes.",
          "Utiliser le thème dans vos composables, sans jamais coder une couleur « en dur ».",
          "Aller plus loin : couleurs dynamiques, couleurs personnalisées, thèmes imbriqués."))
add(P("<b>Prérequis</b> : savoir créer un projet Android Studio « Empty Activity » et écrire une fonction "
      + c("@Composable") + " simple (" + c("Text") + ", " + c("Column") + ", " + c("Button") + ")."))
add(P("<b>Fil rouge</b> : nous construisons pas à pas le thème d'une petite application « Mon Jardin » qui liste des plantes à arroser. "
      "Le code complet est fourni dans le dossier <font name='Mono'>code-android/</font> de l'archive."))
S.append(NextPageTemplate("std")); S.append(PageBreak())
toc = TableOfContents()
toc.levelStyles = [ParagraphStyle("t0", fontName="Sans-B", fontSize=10.5, leading=16, leftIndent=0, spaceBefore=4, textColor=ENCRE),
                   ParagraphStyle("t1", fontName="Sans", fontSize=9.5, leading=13, leftIndent=16, textColor=GRIS)]
S.append(P("Sommaire", "h1s")); S.append(toc)

# ---------------- 1 ----------------
H1("1. Qu'est-ce qu'un thème ?")
add(P("Dans une application, un <b>thème</b> est un ensemble de choix visuels partagés par tous les écrans : les couleurs, les polices "
      "de caractères et les formes (arrondis des boutons, des cartes…). Au lieu de régler la couleur de chaque bouton un par un, "
      "on décrit le style <b>une seule fois</b> et tous les composants s'en servent automatiquement."))
add(P("En Jetpack Compose, le thème est fourni par le composable " + c("MaterialTheme") + " (bibliothèque <b>Material 3</b>). "
      "Il contient trois « piliers » :"))
add(tableau(["Pilier", "Classe", "Ce qu'il définit"], [
    ["Couleurs", "ColorScheme", "Les couleurs de l'application, organisées par <i>rôles</i> (primary, surface, error…)"],
    ["Typographie", "Typography", "Les styles de texte : titres, corps de texte, libellés…"],
    ["Formes", "Shapes", "Les arrondis ou coins coupés des cartes, boutons, boîtes de dialogue…"],
], [3*cm, 3.2*cm, W-6.2*cm], mono_col=1))
add(P("Tout composable placé <i>à l'intérieur</i> de " + c("MaterialTheme { … }") + " peut lire ces valeurs grâce à trois propriétés :"))
add(code("""
MaterialTheme.colorScheme   // les couleurs
MaterialTheme.typography    // les styles de texte
MaterialTheme.shapes        // les formes
"""))
add(P("Voici la même interface affichée avec le thème par défaut de Material 3 (violet) puis avec notre thème « Jardin » "
      "(vert). <b>Le code de l'écran est strictement identique</b> : seul le thème qui l'entoure change."))
add(captures([("01_sans_theme.png", "Thème Material 3 par défaut"), ("02_theme_clair.png", "Thème « Jardin » personnalisé")], hmax=11.5*cm))
add(encadre("À retenir", "Un thème est un « réservoir de style » placé en haut de l'arborescence de composables. "
      "Les composants Material (Button, Card, TopAppBar…) vont y chercher leurs couleurs, polices et formes."))

# ---------------- 2 ----------------
H1("2. Les fichiers du thème dans un projet")
add(P("Quand vous créez un projet « Empty Activity » dans Android Studio, un dossier " + c("ui/theme") + " est généré automatiquement :"))
add(code("""
app/src/main/java/com/exemple/monjardin/
├── MainActivity.kt
└── ui/
    └── theme/
        ├── Color.kt    ← les couleurs
        ├── Type.kt     ← la typographie
        ├── Shape.kt    ← les formes (à créer si absent)
        └── Theme.kt    ← la fonction qui assemble le tout
"""))
add(P("Le fichier " + c("Theme.kt") + " contient une fonction composable (par exemple " + c("MonJardinTheme") + ", nommée d'après votre projet) "
      "qui appelle " + c("MaterialTheme") + ". Dans " + c("MainActivity") + ", tout l'écran est enveloppé dans cette fonction :"))
add(code("""
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JardinTheme {          // Tout ce qui est à l'intérieur hérite du thème
                EcranJardin()
            }
        }
    }
}
""", "MainActivity.kt"))
add(encadre("Astuce", "Enveloppez aussi vos fonctions " + c("@Preview") + " dans votre thème, sinon l'aperçu d'Android Studio "
      "utilisera les couleurs par défaut et ne ressemblera pas à votre application."))
add(code("""
@Preview(name = "Clair", showBackground = true)
@Composable
fun ApercuClair() { JardinTheme(darkTheme = false) { EcranJardin() } }

@Preview(name = "Sombre", showBackground = true)
@Composable
fun ApercuSombre() { JardinTheme(darkTheme = true) { EcranJardin() } }
""", "Aperçus en mode clair et sombre"))

# ---------------- 3 ----------------
H1("3. Les couleurs : ColorScheme")
H2("3.1 La notion de rôle")
add(P("Material 3 ne vous demande pas « la couleur du bouton » mais des couleurs par <b>rôle</b>. Chaque rôle a un usage précis, "
      "et presque chaque couleur a son partenaire <b>on…</b> : la couleur du texte ou de l'icône qui s'affiche <i>par-dessus</i>."))
add(tableau(["Rôle", "Utilisation typique"], [
    ["primary / onPrimary", "Éléments principaux : bouton plein, bouton flottant (FAB), éléments sélectionnés."],
    ["primaryContainer / onPrimaryContainer", "Zones mises en avant mais plus douces : barre du haut, FAB, puces."],
    ["secondary / secondaryContainer", "Éléments secondaires : bouton « tonal », indicateur de la barre de navigation."],
    ["tertiary / tertiaryContainer", "Couleur d'accent pour contraster (badges, zone spéciale)."],
    ["error / errorContainer", "Messages et champs en erreur."],
    ["background, surface / onSurface", "Fond de l'écran, des cartes, des menus ; couleur du texte par défaut."],
    ["surfaceVariant / onSurfaceVariant", "Fonds alternatifs, texte secondaire (moins important)."],
    ["outline", "Bordures : champ de texte, bouton « outlined »."],
], [6.2*cm, W-6.2*cm]))
add(encadre("À retenir", "Règle d'or : un texte posé sur " + c("X") + " utilise la couleur " + c("onX") + ". "
      "Exemple : du texte sur " + c("primary") + " → " + c("onPrimary") + ". C'est ce qui garantit un bon contraste."))
H2("3.2 Déclarer ses couleurs (Color.kt)")
add(P("On crée deux palettes, une claire avec " + c("lightColorScheme()") + " et une sombre avec " + c("darkColorScheme()") + ". "
      "Les couleurs s'écrivent au format " + c("0xAARRGGBB") + " (AA = opacité, FF = opaque). Les rôles non précisés prennent une valeur par défaut."))
add(code("""
val Vert40 = Color(0xFF2E6B30)
val Vert80 = Color(0xFF96D68F)
val VertClair = Color(0xFFB1F2A9)

val LightColors = lightColorScheme(
    primary = Vert40, onPrimary = Color.White,
    primaryContainer = VertClair, onPrimaryContainer = Color(0xFF002106),
    secondary = Color(0xFF52634F), onSecondary = Color.White,
    secondaryContainer = Color(0xFFD5E8CF), onSecondaryContainer = Color(0xFF101F10),
    tertiary = Color(0xFF38656A), onTertiary = Color.White,
    tertiaryContainer = Color(0xFFBCEBF0), onTertiaryContainer = Color(0xFF002023),
    error = Color(0xFFBA1A1A), onError = Color.White,
    background = Color(0xFFF7FBF1), onBackground = Color(0xFF181D17),
    surface = Color(0xFFF7FBF1), onSurface = Color(0xFF181D17),
    surfaceVariant = Color(0xFFDEE5D8), onSurfaceVariant = Color(0xFF424940),
    outline = Color(0xFF72796F),
)

val DarkColors = darkColorScheme(
    primary = Vert80, onPrimary = Color(0xFF00390E),
    primaryContainer = Color(0xFF13521A), onPrimaryContainer = VertClair,
    secondary = Color(0xFFB9CCB4), onSecondary = Color(0xFF253423),
    // ... même principe pour les autres rôles (voir code-android/)
    background = Color(0xFF10140F), onBackground = Color(0xFFE0E4DA),
    surface = Color(0xFF10140F), onSurface = Color(0xFFE0E4DA),
)
""", "ui/theme/Color.kt"))
add(P("Voici les deux palettes obtenues. Remarquez qu'en mode sombre, les couleurs principales deviennent plus <b>claires</b> "
      "(vert pastel) et les fonds deviennent foncés : c'est normal, cela conserve la lisibilité."))
add(captures([("04_palette_clair.png", "Palette claire (LightColors)"), ("05_palette_sombre.png", "Palette sombre (DarkColors)")], hmax=11*cm))
add(encadre("Astuce", "Inutile de choisir 25 couleurs à la main ! L'outil en ligne <b>Material Theme Builder</b> (material-foundation.github.io/material-theme-builder) "
      "génère toute la palette claire et sombre à partir d'<b>une seule</b> couleur de départ, et exporte directement les fichiers " + c("Color.kt") + " et " + c("Theme.kt") + "."))

# ---------------- 4 ----------------
H1("4. Assembler le thème (Theme.kt)")
add(P("La fonction de thème choisit la bonne palette puis appelle " + c("MaterialTheme") + ". Le paramètre " + c("content") +
      " représente tous les composables qui seront « à l'intérieur » du thème."))
add(code("""
@Composable
fun JardinTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // true = couleurs du fond d'écran (Android 12+)
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColors
        else -> LightColors
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = JardinTypography,   // voir chapitre 6
        shapes = JardinShapes,           // voir chapitre 7
        content = content,
    )
}
""", "ui/theme/Theme.kt"))
add(P("Ligne par ligne :"))
add(puces(c("isSystemInDarkTheme()") + " renvoie " + c("true") + " si le téléphone est réglé en mode sombre. C'est la valeur par défaut du paramètre.",
          c("dynamicColor") + " active les <b>couleurs dynamiques</b> (voir chapitre 5).",
          "Le bloc " + c("when") + " choisit la palette : dynamique, sombre ou claire.",
          c("MaterialTheme(...)") + " rend ces valeurs disponibles à tous les composables de " + c("content") + "."))

# ---------------- 5 ----------------
H1("5. Mode clair, mode sombre et couleurs dynamiques")
H2("5.1 Le mode sombre automatique")
add(P("Grâce à " + c("isSystemInDarkTheme()") + ", l'application suit automatiquement le réglage du téléphone. "
      "Il n'y a <b>rien d'autre à faire</b> dans vos écrans, à condition d'utiliser les couleurs du thème."))
add(captures([("02_theme_clair.png", "JardinTheme(darkTheme = false)"), ("03_theme_sombre.png", "JardinTheme(darkTheme = true)")], hmax=11.5*cm))
H2("5.2 Les couleurs dynamiques (Material You)")
add(P("Depuis Android 12 (API 31), le système peut générer une palette à partir du <b>fond d'écran</b> de l'utilisateur. "
      "Les fonctions " + c("dynamicLightColorScheme(context)") + " et " + c("dynamicDarkColorScheme(context)") + " renvoient alors un "
      + c("ColorScheme") + " complet. Sur les versions plus anciennes, elles n'existent pas : c'est pourquoi on teste "
      + c("Build.VERSION.SDK_INT >= Build.VERSION_CODES.S") + " et qu'on garde une palette de secours."))
add(encadre("Attention", "Avec les couleurs dynamiques, <b>votre</b> palette n'est plus utilisée sur Android 12+ : l'application prend "
      "les couleurs de l'utilisateur. Si l'identité de votre marque est importante (logo vert, par exemple), laissez " + c("dynamicColor = false") + "."))
H2("5.3 Forcer un mode (réglage dans l'application)")
add(P("Pour proposer un choix « Clair / Sombre / Système » dans les réglages, il suffit de passer une valeur à " + c("darkTheme") + " :"))
add(code("""
enum class ModeTheme { CLAIR, SOMBRE, SYSTEME }

@Composable
fun App() {
    var mode by rememberSaveable { mutableStateOf(ModeTheme.SYSTEME) }
    val sombre = when (mode) {
        ModeTheme.CLAIR -> false
        ModeTheme.SOMBRE -> true
        ModeTheme.SYSTEME -> isSystemInDarkTheme()
    }
    JardinTheme(darkTheme = sombre) {
        EcranJardin()
    }
}
"""))
add(encadre("Astuce", "Pour que le choix survive à la fermeture de l'application, enregistrez-le avec <b>DataStore</b> plutôt qu'avec " + c("rememberSaveable") + "."))

# ---------------- 6 ----------------
H1("6. La typographie : Typography")
add(P("Material 3 définit une <b>échelle typographique</b> de 15 styles répartis en 5 familles (Display, Headline, Title, Body, Label), "
      "chacune en 3 tailles (Large, Medium, Small)."))
add(tableau(["Famille", "Usage"], [
    ["display…", "Très grands textes, rares (chiffre clé, écran d'accueil)."],
    ["headline…", "Titres de page."],
    ["title…", "Titres de section, de carte, de barre d'application."],
    ["body…", "Texte courant (paragraphes, descriptions)."],
    ["label…", "Petits textes de composants : boutons, onglets, puces."],
], [3.5*cm, W-3.5*cm]))
add(P("On crée un objet " + c("Typography") + " en ne redéfinissant que les styles qui nous intéressent ; les autres gardent leur valeur par défaut :"))
add(code("""
val Titres = FontFamily.Serif
val Corps = FontFamily.SansSerif

val JardinTypography = Typography(
    displayLarge = TextStyle(fontFamily = Titres, fontWeight = FontWeight.Bold,
                             fontSize = 48.sp, lineHeight = 56.sp),
    headlineMedium = TextStyle(fontFamily = Titres, fontWeight = FontWeight.Bold,
                               fontSize = 28.sp, lineHeight = 36.sp),
    titleLarge = TextStyle(fontFamily = Titres, fontWeight = FontWeight.SemiBold,
                           fontSize = 22.sp, lineHeight = 28.sp),
    titleMedium = TextStyle(fontFamily = Corps, fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp, lineHeight = 24.sp),
    bodyLarge = TextStyle(fontFamily = Corps, fontSize = 16.sp, lineHeight = 24.sp),
    labelLarge = TextStyle(fontFamily = Corps, fontWeight = FontWeight.Bold,
                           fontSize = 14.sp, letterSpacing = 0.5.sp),
)
""", "ui/theme/Type.kt"))
add(captures([("06_typo_defaut.png", "Typography() par défaut"), ("07_typo_perso.png", "JardinTypography (titres en Serif, gras)")], hmax=11*cm))
H2("6.1 Utiliser une police personnalisée")
add(P("1. Copiez votre fichier de police (ex. <font name='Mono'>lora_bold.ttf</font>, nom en minuscules, sans tiret) dans " + c("res/font/") + ".<br/>"
      "2. Déclarez la famille et utilisez-la dans vos styles :"))
add(code("""
val Lora = FontFamily(
    Font(R.font.lora_regular, FontWeight.Normal),
    Font(R.font.lora_bold, FontWeight.Bold),
)
// puis : headlineMedium = TextStyle(fontFamily = Lora, ...)
"""))
H2("6.2 Utiliser un style dans un écran")
add(code("""
Text("Bonjour !", style = MaterialTheme.typography.headlineMedium)
Text("3 plantes ont besoin de vous.", style = MaterialTheme.typography.bodyMedium)
"""))
add(encadre("Attention", "Évitez " + c("Text(\"…\", fontSize = 28.sp)") + " éparpillé dans vos écrans. Si vous changez d'avis sur la taille des titres, "
      "il faudrait modifier chaque écran. Avec le thème, une seule ligne suffit."))

# ---------------- 7 ----------------
H1("7. Les formes : Shapes")
add(P("L'objet " + c("Shapes") + " propose 5 tailles : " + c("extraSmall") + ", " + c("small") + ", " + c("medium") + ", " + c("large") + ", "
      + c("extraLarge") + ". Chaque composant Material utilise l'une d'elles : par exemple les cartes utilisent " + c("medium") +
      ", les boîtes de dialogue " + c("extraLarge") + ", les champs de texte " + c("extraSmall") + "."))
add(code("""
val JardinShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(topStart = 24.dp, bottomEnd = 24.dp), // forme « feuille »
    large = RoundedCornerShape(28.dp),
    extraLarge = CutCornerShape(16.dp),                              // coins coupés
)
""", "ui/theme/Shape.kt"))
add(P("À gauche les formes par défaut, à droite les nôtres. La carte de plante a pris automatiquement la forme « feuille » "
      "(" + c("medium") + ") sans qu'on modifie son code :"))
add(captures([("08_formes_defaut.png", "Shapes() par défaut"), ("09_formes_perso.png", "JardinShapes")], hmax=8.5*cm))
add(P("Vous pouvez aussi réutiliser une forme du thème sur vos propres éléments :"))
add(code("""
Box(
    Modifier
        .size(120.dp)
        .clip(MaterialTheme.shapes.large)
        .background(MaterialTheme.colorScheme.primaryContainer)
)
"""))
add(encadre("À retenir", "Les boutons (" + c("Button") + ") ne suivent pas " + c("shapes") + " : ils sont toujours en forme de « pilule ». "
      "Pour les changer, passez " + c("shape = …") + " directement au bouton."))

# ---------------- 8 ----------------
H1("8. Utiliser le thème dans ses composables")
H2("8.1 Les composants Material s'adaptent tout seuls")
add(P("Boutons, champs de texte, interrupteurs, cases à cocher, puces, curseurs… tous lisent le thème. Le même code donne ces deux rendus :"))
add(captures([("10_composants_clair.png", "Composants — mode clair"), ("11_composants_sombre.png", "Composants — mode sombre")], hmax=11.5*cm))
H2("8.2 Lire une valeur du thème")
add(code("""
@Composable
fun CartePlante(p: Plante) {
    Card(shape = MaterialTheme.shapes.medium, modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Filled.LocalFlorist, contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,      // couleur du thème
            )
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(p.nom, style = MaterialTheme.typography.titleMedium)
                Text(p.info, style = MaterialTheme.typography.bodyMedium)
            }
            FilledTonalButton(onClick = {}) { Text("Arroser") }
        }
    }
}
""", "Exemple : la carte d'une plante"))
H2("8.3 Surface et la couleur de contenu automatique")
add(P("Le composable " + c("Surface") + " fait plus que peindre un fond : il choisit aussi la <b>couleur de contenu</b> "
      "(" + c("LocalContentColor") + ") correspondante. Si le fond est " + c("primary") + ", les " + c("Text") + " et " + c("Icon") +
      " à l'intérieur deviennent automatiquement " + c("onPrimary") + "."))
add(code("""
Surface(color = MaterialTheme.colorScheme.primary, shape = MaterialTheme.shapes.small) {
    Row(Modifier.padding(16.dp)) {
        Icon(Icons.Filled.LocalFlorist, null)   // pas de couleur précisée...
        Text("Surface(color = primary)")         // ...et pourtant bien lisible !
    }
}
"""))
add(captures([("12_surface_contentcolor.png", "Le texte et l'icône prennent la couleur « on… » adaptée")], larg=7.5*cm))
add(encadre("Astuce", "Si vous voulez la couleur de contenu de n'importe quelle couleur du thème, utilisez "
      + c("contentColorFor(MaterialTheme.colorScheme.tertiary)") + "."))
H2("8.4 L'erreur classique : la couleur codée en dur")
add(P("Écrire " + c("color = Color.Black") + " fonctionne… jusqu'au jour où l'utilisateur passe en mode sombre :"))
add(code("""
// ✘ À éviter
Text("Mon texte", color = Color.Black)

// ✔ Correct
Text("Mon texte", color = MaterialTheme.colorScheme.onSurface)
// ou, encore plus simple, ne rien préciser : Text utilise LocalContentColor
Text("Mon texte")
"""))
add(captures([("13_erreur_sombre.png", "✘ Color.Black en mode sombre : illisible"), ("14_bonne_pratique_sombre.png", "✔ onSurface : lisible")]))

# ---------------- 9 ----------------
H1("9. Aller plus loin")
H2("9.1 Ajouter ses propres couleurs (CompositionLocal)")
add(P(c("ColorScheme") + " n'a pas de rôle « succès » ou « avertissement ». Pour en ajouter, on crée une classe et on la diffuse "
      "avec un <b>CompositionLocal</b>, exactement comme le fait " + c("MaterialTheme") + " en interne."))
add(code("""
@Immutable
data class CouleursEtendues(
    val succes: Color, val surSucces: Color,
    val alerte: Color, val surAlerte: Color,
)

val CouleursClaires = CouleursEtendues(Color(0xFF1B6D2F), Color.White,
                                       Color(0xFF8A5100), Color.White)
val CouleursSombres = CouleursEtendues(Color(0xFF8BD89A), Color(0xFF00391A),
                                       Color(0xFFFFB870), Color(0xFF4A2800))

// 1) Déclarer le « canal » avec une valeur par défaut
val LocalCouleursEtendues = staticCompositionLocalOf { CouleursClaires }

// 2) Un accès pratique, comme MaterialTheme.colorScheme
object JardinThemeExt {
    val couleurs: CouleursEtendues
        @Composable @ReadOnlyComposable
        get() = LocalCouleursEtendues.current
}
""", "ui/theme/CouleursEtendues.kt"))
add(P("Puis, dans " + c("JardinTheme") + ", on fournit la bonne valeur selon le mode :"))
add(code("""
val etendues = if (darkTheme) CouleursSombres else CouleursClaires
CompositionLocalProvider(LocalCouleursEtendues provides etendues) {
    MaterialTheme(colorScheme = colorScheme, typography = JardinTypography,
                  shapes = JardinShapes, content = content)
}

// Utilisation dans un écran :
val ext = JardinThemeExt.couleurs
Surface(color = ext.succes, contentColor = ext.surSucces, shape = CircleShape) {
    Text("En forme", Modifier.padding(horizontal = 12.dp, vertical = 6.dp))
}
"""))
add(captures([("15_couleurs_etendues_clair.png", "Badges « succès » / « alerte » — clair"), ("16_couleurs_etendues_sombre.png", "Les mêmes badges — sombre")]))
H2("9.2 Thème imbriqué : changer le style d'une seule zone")
add(P("On peut placer un " + c("MaterialTheme") + " à l'intérieur d'un autre. Le thème intérieur ne s'applique qu'à sa zone. "
      "Avec " + c("copy()") + ", on part du thème courant et on ne modifie que quelques rôles :"))
add(code("""
MaterialTheme(
    colorScheme = MaterialTheme.colorScheme.copy(
        primary = MaterialTheme.colorScheme.tertiary,
        onPrimary = MaterialTheme.colorScheme.onTertiary,
        surfaceVariant = MaterialTheme.colorScheme.tertiaryContainer,
    )
) {
    // Ici, Button et Card utilisent la couleur tertiaire
    Button(onClick = {}) { Text("Bouton premium") }
    CartePlante(Plante("Orchidée", "Arrosage : 1 fois / 10 jours"))
}
"""))
add(captures([("17_theme_imbrique.png", "La zone « Premium » utilise un thème imbriqué")], larg=7.5*cm))

# ---------------- 10 ----------------
H1("10. Bonnes pratiques et mémo")
H2("10.1 Bonnes pratiques")
add(puces("Toujours envelopper l'application <b>et</b> les " + c("@Preview") + " dans votre thème.",
          "Ne jamais coder une couleur en dur dans un écran : passer par " + c("MaterialTheme.colorScheme") + ".",
          "Respecter les paires " + c("X / onX") + " pour garantir le contraste.",
          "Utiliser les styles " + c("MaterialTheme.typography.…") + " plutôt que des " + c("fontSize") + " isolés.",
          "Toujours fournir une palette claire <b>et</b> une palette sombre.",
          "Tester vos écrans dans les deux modes (deux " + c("@Preview") + ").",
          "Générer vos palettes avec Material Theme Builder pour des contrastes corrects."))
H2("10.2 Mémo")
add(tableau(["Je veux…", "J'écris…"], [
    ["Lire une couleur", "MaterialTheme.colorScheme.primary"],
    ["Lire un style de texte", "MaterialTheme.typography.titleLarge"],
    ["Lire une forme", "MaterialTheme.shapes.medium"],
    ["Savoir si le téléphone est en sombre", "isSystemInDarkTheme()"],
    ["Couleurs du fond d'écran (Android 12+)", "dynamicLightColorScheme(context)"],
    ["La couleur de texte adaptée à un fond", "contentColorFor(couleur)"],
    ["Modifier quelques couleurs localement", "MaterialTheme(colorScheme = …copy(…)) { }"],
    ["Ajouter mes propres valeurs au thème", "staticCompositionLocalOf + CompositionLocalProvider"],
], [6.3*cm, W-6.3*cm], mono_col=1))
H2("10.3 Exercices")
add(encadre("Exercice", "<b>1.</b> Changez la couleur de départ de « Mon Jardin » pour un thème bleu « Océan ». "
      "Générez la palette avec Material Theme Builder et remplacez " + c("Color.kt") + "."))
add(encadre("Exercice", "<b>2.</b> Ajoutez un interrupteur « Mode sombre » dans un écran de réglages qui force " + c("darkTheme") + " (chapitre 5.3)."))
add(encadre("Exercice", "<b>3.</b> Changez la forme " + c("medium") + " pour " + c("CutCornerShape(12.dp)") + " et observez les cartes."))
add(encadre("Exercice", "<b>4.</b> Ajoutez une couleur étendue « info » (bleu) à " + c("CouleursEtendues") + " et affichez un badge « Nouveau »."))
add(encadre("Exercice", "<b>5.</b> Cherchez dans votre projet toutes les occurrences de " + c("Color(0x") + " en dehors du dossier " + c("ui/theme") + " et remplacez-les par des rôles du thème."))
H2("10.4 Contenu de l'archive")
add(tableau(["Dossier", "Contenu"], [
    ["cours-themes-jetpack-compose.pdf", "Ce cours."],
    ["captures/", "Toutes les captures d'écran du cours, en haute résolution (PNG)."],
    ["code-android/", "Les fichiers Kotlin de l'exemple « Mon Jardin » à copier dans un projet Android Studio : Color.kt, Type.kt, Shape.kt, CouleursEtendues.kt, Theme.kt, EcranJardin.kt, MainActivity.kt."],
    ["generateur-captures/", "Le petit projet Compose (Kotlin/JVM) qui a produit les captures : le même code de thème, rendu hors écran."],
], [5.6*cm, W-5.6*cm], mono_col=0))
add(P("<font size='8.5' color='#5B6358'>Note sur les captures : elles ont été générées par le vrai moteur de rendu Compose (Compose Multiplatform, "
      "bibliothèque Material 3) à partir du code présenté dans ce cours. Sur un téléphone Android, la police système (Roboto) "
      "et certains détails d'espacement peuvent légèrement différer.</font>"))

doc = Doc(OUT)
doc.multiBuild(S)
print("PDF OK")
