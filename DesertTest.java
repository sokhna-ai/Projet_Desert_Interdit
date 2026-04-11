import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests JUnit pour la classe Desert.
 * Couvre les 5 cas demandés dans le sujet.
 */
public class DesertTest {

    private Desert desert;
    private Joueur joueur;

    @BeforeEach
    public void setUp() {
        desert = new Desert();
        joueur = new Joueur(3, 0, 0);
        joueur.setNom("TestJoueur");
        desert.ajouterJoueur(joueur, 0, 0);
    }

    // ------------------------------------------------------------------
    // Test 1 : déplacer un joueur sur une case valide → vérifier nouvelles coordonnées
    // ------------------------------------------------------------------
    @Test
    public void testDeplacerJoueurCaseValide() {
        // Vérifier que la case en bas (0,1) est libre de sable
        desert.getZone(0, 1).setNbSable(0);

        boolean resultat = desert.deplacerJoueur(Desert.Direction.BAS);

        assertTrue(resultat, "Le déplacement vers le bas doit réussir.");
        assertEquals(0, joueur.getX(), "X du joueur doit être 0.");
        assertEquals(1, joueur.getY(), "Y du joueur doit être 1.");
    }

    // ------------------------------------------------------------------
    // Test 2 : déplacer sur une case bloquée → vérifier que ça échoue
    // ------------------------------------------------------------------
    @Test
    public void testDeplacerJoueurCaseBloquee() {
        // Bloquer la case (0,1) avec 2 grains de sable
        desert.getZone(0, 1).setNbSable(2);

        boolean resultat = desert.deplacerJoueur(Desert.Direction.BAS);

        assertFalse(resultat, "Le déplacement vers une case bloquée doit échouer.");
        assertEquals(0, joueur.getX(), "X du joueur ne doit pas changer.");
        assertEquals(0, joueur.getY(), "Y du joueur ne doit pas changer.");
    }

    // ------------------------------------------------------------------
    // Test 3 : mettre le sable à 44 → vérifier "DEFAITE_SABLE"
    // ------------------------------------------------------------------
    @Test
    public void testDefaiteSable() {
        // Répartir 44 grains sur la grille (9 cases × 4 + 8 cases × 1 = 44)
        int sableAjoute = 0;
        outer:
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                Zone z = desert.getZone(j, i);
                // On peut forcer en-dessous du max visuel : on force à 2 sur plusieurs cases
                int ajout = Math.min(2, 44 - sableAjoute);
                z.setNbSable(ajout);
                sableAjoute += ajout;
                if (sableAjoute >= 44) break outer;
            }
        }

        assertEquals("DEFAITE_SABLE", desert.etatPartie(),
                "Avec 44 grains de sable, l'état doit être DEFAITE_SABLE.");
    }

    // ------------------------------------------------------------------
    // Test 4 : mettre le niveau tempête à 7 → vérifier "DEFAITE_TEMPETE"
    // ------------------------------------------------------------------
    @Test
    public void testDefaiteTempete() {
        // Forcer le niveau tempête à 7 via des setters indirects :
        // on appelle 14 fois tempeteSeDechaine via finDeTour (trop aléatoire),
        // donc on utilise la réflexion pour accéder au champ privé.
        try {
            java.lang.reflect.Field f = Desert.class.getDeclaredField("niveauTempete");
            f.setAccessible(true);
            f.set(desert, 7.0);
        } catch (Exception e) {
            fail("Impossible d'accéder au champ niveauTempete : " + e.getMessage());
        }

        assertEquals("DEFAITE_TEMPETE", desert.etatPartie(),
                "Avec niveauTempete = 7, l'état doit être DEFAITE_TEMPETE.");
    }

    // ------------------------------------------------------------------
    // Test 5 : mettre l'eau d'un joueur à 0 → vérifier "DEFAITE_SOIF"
    // ------------------------------------------------------------------
    @Test
    public void testDefaiteSoif() {
        joueur.setNiveauEau(0);

        assertEquals("DEFAITE_SOIF", desert.etatPartie(),
                "Avec un joueur à 0 eau, l'état doit être DEFAITE_SOIF.");
    }
}
