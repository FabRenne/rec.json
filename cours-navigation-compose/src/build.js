// Génère les captures d'écran puis le PDF du cours.
// Usage : node build.js
const { chromium } = require('playwright');
const path = require('path');

const ids = [
  'ex1-accueil', 'ex1-profil', 'ex2-liste', 'ex2-detail', 'ex3-connexion', 'ex3-accueil',
  'ex4-recettes', 'ex4-favoris', 'ex4-profil', 'ex5-detail', 'ex5-noter', 'studio-arbo',
];

(async () => {
  const browser = await chromium.launch();
  const ctx = await browser.newContext({ deviceScaleFactor: 2, viewport: { width: 1400, height: 1000 } });
  const page = await ctx.newPage();
  await page.goto('file://' + path.join(__dirname, 'maquettes.html'));
  await page.evaluate(() => document.fonts.ready);
  for (const id of ids) {
    await page.locator('#' + id).screenshot({ path: path.join(__dirname, '..', 'captures', id + '.png'), omitBackground: true });
  }

  const doc = await ctx.newPage();
  await doc.goto('file://' + path.join(__dirname, 'cours.html'));
  await doc.evaluate(() => document.fonts.ready);
  await doc.pdf({
    path: path.join(__dirname, '..', 'Cours_Navigation_Jetpack_Compose.pdf'),
    format: 'A4',
    printBackground: true,
    margin: { top: '18mm', bottom: '18mm', left: '16mm', right: '16mm' },
    displayHeaderFooter: true,
    headerTemplate: '<div></div>',
    footerTemplate: '<div style="width:100%;font-family:Inter,sans-serif;font-size:8px;color:#888;padding:0 16mm;display:flex;justify-content:space-between"><span>La navigation avec Jetpack Compose — cours débutant</span><span><span class="pageNumber"></span> / <span class="totalPages"></span></span></div>',
  });
  await browser.close();
  console.log('PDF généré.');
})();
