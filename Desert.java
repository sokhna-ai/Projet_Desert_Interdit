import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Desert {
    final static int NB_PIECES=4;
    final static String[] PIECES={"Moteur","Hélice","Gouvernail","Capteur"};
    final static int PAREFEU=500;
    private Zone[][] zones;
    private ArrayList<Joueur>joueurs;
    private int niveauTempete=0;
    private int totalSable=0;
    private int nbPiecesRamasses=0;
    private int joueurCourant=0;
    private int actionsRestatntes=4;
    private int xOeil;
    private int yOeil;
    
    Random rand = new Random();
    public Desert() {
        this.joueurs = new ArrayList<>();
        this.zones  =  new Zone[5][5];
        rand = new Random();
        while (!(initialiserPieces() && initialiserZones()&&initialiserZonesColonneLigne()&&initialiserOasis()&&initialiserTunnel()&&initialiserPisteEtCrash())){ 
        }
    }

    private boolean ajouterJoueur(Joueur j, int x, int y){
        joueurs.add(j);
        return true;
    }

    //!!!à finir
    private boolean initialiserZones() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                this.zones[i][j] = new Zone_Normale(j,i,""); //!!! à modifier
            }
        }
        this.zones[2][2] = new Oeil_Tempête(2,2);


        return true;
    }

    private boolean initialiserPisteEtCrash() {
        int x;
        int y;
        boolean ok = false;
        ok=false;
            while(!ok){
                x=rand.nextInt(5);
                y=rand.nextInt(5);
                if(this.zones[x][y] instanceof Zone_Normale 
                    &&  ((Zone_Normale)this.zones[y][x]).getpieceCachee().equals("")){
                    this.zones[y][x] = new Piste_Vol(x,y);
                     ok=true;
                    }
            }
        ok=false;
            while(!ok){
                x=rand.nextInt(5);
                y=rand.nextInt(5);
                if(this.zones[x][y] instanceof Zone_Normale 
                    &&  ((Zone_Normale)this.zones[y][x]).getpieceCachee().equals("")){
                    this.zones[y][x] = new Crash_Helicoptere(x,y);
                     ok=true;
                    }
            }
        return true;
    }


    private boolean initialiserPieces() {
        boolean ok = false;
        int x;
        int y;
        for (int i=0;i<NB_PIECES;i++){
            while(!ok){
                x=rand.nextInt(5);
                y=rand.nextInt(5);
                if(this.zones[x][y] instanceof Zone_Normale 
                    &&  ((Zone_Normale)this.zones[y][x]).getpieceCachee().equals("")
                    &&  getNbPieceEnLigne(x)<1&&getNbPieceEnColonne(y)<1){
                        ((Zone_Normale)this.zones[y][x]).setpieceCachee(PIECES[i]); 
                         ok=true;
                }
            }
            ok=false;
        }
        return true;
    }
    
    private boolean initialiserZonesColonneLigne() {
        int x;
        int y;
        int nbEssais=0;
        boolean ok = false;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (this.zones[i][j] instanceof Zone_Normale && ((Zone_Normale)this.zones[i][j]).getpieceCachee()!="") {
                    ok=false;
                    while (!ok) {
                        if (nbEssais>PAREFEU) {
                            return false;
                        }
                        nbEssais++;
                        x=rand.nextInt(5);
                        if (this.zones[i][x] instanceof Zone_Normale && ((Zone_Normale)this.zones[i][j]).getpieceCachee()=="") {
                            this.zones[i][x] = new Zone_Colonne(x,i,i,((Zone_Normale)this.zones[i][j]).getpieceCachee());
                            ok = true;
                        }
                    }
                    ok=false;
                    while(!ok){
                        if (nbEssais>PAREFEU) {
                            return false;
                        }
                        nbEssais++;
                        y=rand.nextInt(5);
                        if (this.zones[y][i] instanceof Zone_Normale && ((Zone_Normale)this.zones[y][i]).getpieceCachee()=="") {
                            this.zones[y][i] = new Zone_Ligne(y,i,i,((Zone_Normale)this.zones[i][j]).getpieceCachee());
                            ok = true;
                        }
                    }
                    
                }
            }
        }
        return true;
    }

    private boolean initialiserOasis() {
        int x;
        int y;
        boolean ok = false;
        for (int i=0;i<2;i++){
            ok=false;
            while(!ok){
                x=rand.nextInt(5);
                y=rand.nextInt(5);
                if(this.zones[x][y] instanceof Zone_Normale 
                    &&  ((Zone_Normale)this.zones[y][x]).getpieceCachee().equals("")){
                    this.zones[y][x] = new Oasis(x,y,false);
                     ok=true;
                    }
            }
        }
        while(!ok){
                x=rand.nextInt(5);
                y=rand.nextInt(5);
                if(this.zones[x][y] instanceof Zone_Normale 
                    &&  ((Zone_Normale)this.zones[y][x]).getpieceCachee().equals("")){
                    this.zones[y][x] = new Oasis(x,y,true);
                     ok=true;
                    }
            }
        return true;
    }
    private boolean initialiserTunnel() {
        int x;
        int y;
        boolean ok = false;
        for (int i=0;i<3;i++){
            ok=false;
            while(!ok){
                x=rand.nextInt(5);
                y=rand.nextInt(5);
                if(this.zones[x][y] instanceof Zone_Normale 
                    &&  ((Zone_Normale)this.zones[y][x]).getpieceCachee().equals("")){
                    this.zones[y][x] = new Tunnel(x,y);
                     ok=true;
                    }
            }
        }
        return true;
    }
    private int getNbPieceEnLigne(int l){
        int nb=0;
        for(int i=0;i<5;i++){
            if(this.zones[l][i] instanceof Zone_Normale 
                &&  !((Zone_Normale)this.zones[l][i]).getpieceCachee().equals("")){
                    nb++;
            }
        }
        return nb;
    }
    private int getNbPieceEnColonne(int c){
        int nb = 0;
        for(int i=0;i<5;i++){
            if(this.zones[i][c] instanceof Zone_Normale 
                &&  !((Zone_Normale)this.zones[i][c]).getpieceCachee().equals("")){
                    nb++;
            }
        }
        return nb;
    }

    
}
