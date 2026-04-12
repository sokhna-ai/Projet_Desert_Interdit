import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests JUnit pour la classe Desert.
 * Couvre : déplacement, cases bloquées, états de fin de partie.
 */
public class DesertTest {

    private Desert desert;
    private Joueur alice;
    private Joueur bob;

    /** Initialisation avant chaque test : on crée un désert et deux joueurs. */
    @Before
    public void setUp() {
        desert = new Desert();
        alice = new Joueur(5, 0, 0);
        alice.setNom("Alice");
        bob = new Joueur(5, 4, 4);
        bob.setNom("Bob");
        desert.ajouterJoueur(alice, 0, 0);
        desert.ajouterJoueur(bob, 4, 4);
    }

    // ------------------------------------------------------------------
    // Test 1 : déplacer un joueur sur une case valide
    // ------------------------------------------------------------------
    @Test
    public void testDeplacementValide() {
        // Alice est en (0,0), on la déplace à droite → elle doit être en (1,0)
        // On s'assure d'abord que la case (1,0) n'est pas bloquée
        desert.getZone(1, 0).setNbSable(0); // garantir qu'elle n'est pas bloquée
        boolean ok = desert.deplacerJoueur(Desert.Direction.DROITE);
        assertTrue("Le déplacement valide doit réussir", ok);
        assertEquals("Alice doit être en x=1", 1, alice.getX());
        assertEquals("Alice doit rester en y=0", 0, alice.getY());
    }

    // ------------------------------------------------------------------
    // Test 2 : déplacer vers une case bloquée → échec
    // ------------------------------------------------------------------
    @Test
    public void testDeplacementCaseBloquee() {
        // On bloque la case (1,0) avec 2 grains de sable
        desert.getZone(1, 0).setNbSable(2);
        boolean ok = desert.deplacerJoueur(Desert.Direction.DROITE);
        assertFalse("Le déplacement vers une case bloquée doit échouer", ok);
        assertEquals("Alice ne doit pas avoir bougé (x)", 0, alice.getX());
        assertEquals("Alice ne doit pas avoir bougé (y)", 0, alice.getY());
    }

    // ------------------------------------------------------------------
    // Test 3 : sable > 43 → DEFAITE_SABLE
    // ------------------------------------------------------------------
    @Test
    public void testDefaiteSable() {
        // On force le sable sur plusieurs cases pour dépasser 43
        int pose = 0;
        outer:
        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                Zone z = desert.getZone(x, y);
                if (!(z instanceof Oeil_Tempete)) {
                    z.setNbSable(2);
                    pose += 2;
                    if (pose > 43) break outer;
                }
            }
        }
        assertEquals("Avec plus de 43 sables, la partie est perdue par ensablement",
                "DEFAITE_SABLE", desert.etatPartie());
    }

    // ------------------------------------------------------------------
    // Test 4 : niveau tempête ≥ 7 → DEFAITE_TEMPETE
    // ------------------------------------------------------------------
    @Test
    public void testDefaiteTempete() {
        // On accède au niveau tempête via réflexion ou on force via finDeTour
        // Méthode directe : forcer le champ (package-private trick via setter non fourni)
        // → On utilise finDeTour() en boucle pour monter la tempête
        // Mais c'est non-déterministe. On préfère vérifier l'état via accès direct au champ.
        // Comme le champ est privé, on utilise java.lang.reflect uniquement pour le test.
        try {
            java.lang.reflect.Field f = Desert.class.getDeclaredField("niveauTempete");
            f.setAccessible(true);
            f.set(desert, 7.0);
        } catch (Exception e) {
            fail("Impossible d'accéder à niveauTempete : " + e.getMessage());
        }
        assertEquals("Niveau tempête ≥ 7 → DEFAITE_TEMPETE",
                "DEFAITE_TEMPETE", desert.etatPartie());
    }

    // ------------------------------------------------------------------
    // Test 5 : eau d'un joueur à 0 → DEFAITE_SOIF
    // ------------------------------------------------------------------
    @Test
    public void testDefaiteSoif() {
        alice.setNiveauEau(0);
        assertEquals("Joueur sans eau → DEFAITE_SOIF",
                "DEFAITE_SOIF", desert.etatPartie());
    }

    // ------------------------------------------------------------------
    // Test bonus : etatPartie renvoie EN_COURS au démarrage
    // ------------------------------------------------------------------
    @Test
    public void testEtatInitialEnCours() {
        assertEquals("Au démarrage la partie est EN_COURS",
                "EN_COURS", desert.etatPartie());
    }

    // ------------------------------------------------------------------
    // Test bonus : déblayage de sable
    // ------------------------------------------------------------------
    @Test
    public void testDeblayage() {
        desert.getZone(0, 0).setNbSable(1);
        boolean ok = desert.deblayer(0, 0);
        assertTrue("Déblayage d'une case avec sable doit réussir", ok);
        assertEquals("Après déblayage, la case doit avoir 0 sable",
                0, desert.getZone(0, 0).getnbSable());
    }

    // ------------------------------------------------------------------
    // Test bonus : fouiller une zone normale explorée
    // ------------------------------------------------------------------
    @Test
    public void testFouillerCase() {
        // Alice est en (0,0), on fouille
        assertFalse("La case ne doit pas être explorée avant fouille",
                desert.getZone(0, 0).getExploree());
        desert.fouillerCase();
        assertTrue("La case doit être explorée après fouille",
                desert.getZone(0, 0).getExploree());
    }
}
