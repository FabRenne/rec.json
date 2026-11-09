import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.unit.Density
import org.jetbrains.skia.EncodedImageFormat
import java.io.File

fun capture(nom: String, largeurDp: Int = 360, hauteurDp: Int = 640, contenu: @Composable () -> Unit) {
    val d = 2f
    val scene = ImageComposeScene((largeurDp * d).toInt(), (hauteurDp * d).toInt(), Density(d), content = contenu)
    scene.render(0); scene.render(500_000_000L)
    val img = scene.render(1_000_000_000L)
    File("captures").mkdirs()
    File("captures/$nom.png").writeBytes(img.encodeToData(EncodedImageFormat.PNG)!!.bytes)
    scene.close()
    println("OK $nom")
}

fun main() {
    capture("01_sans_theme") { MaterialTheme { EcranJardin() } }
    capture("02_theme_clair") { JardinTheme(darkTheme = false) { EcranJardin() } }
    capture("03_theme_sombre") { JardinTheme(darkTheme = true) { EcranJardin() } }
    capture("04_palette_clair", hauteurDp = 680) { JardinTheme(false) { EcranPalette() } }
    capture("05_palette_sombre", hauteurDp = 680) { JardinTheme(true) { EcranPalette() } }
    capture("06_typo_defaut") { MaterialTheme { EcranTypo() } }
    capture("07_typo_perso") { JardinTheme { EcranTypo() } }
    capture("08_formes_defaut") { MaterialTheme(colorScheme = LightColors) { EcranFormes() } }
    capture("09_formes_perso") { JardinTheme { EcranFormes() } }
    capture("10_composants_clair") { JardinTheme(false) { EcranComposants() } }
    capture("11_composants_sombre") { JardinTheme(true) { EcranComposants() } }
    capture("12_surface_contentcolor", hauteurDp = 500) { JardinTheme { EcranSurface() } }
    capture("13_erreur_sombre", hauteurDp = 220) { JardinTheme(true) { EcranErreur(false) } }
    capture("14_bonne_pratique_sombre", hauteurDp = 220) { JardinTheme(true) { EcranErreur(true) } }
    capture("15_couleurs_etendues_clair", hauteurDp = 360) { JardinTheme(false) { EcranEtendues() } }
    capture("16_couleurs_etendues_sombre", hauteurDp = 360) { JardinTheme(true) { EcranEtendues() } }
    capture("17_theme_imbrique", hauteurDp = 520) { JardinTheme { EcranImbrique() } }
}
