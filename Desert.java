import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Desert {
    private enum Direction { HAUT, DROITE, BAS, GAUCHE };
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
        xOeil=2;
        yOeil=2;
        while (!(initialiserPieces() && initialiserZones()&&initialiserZonesColonneLigne()&&initialiserOasis()&&initialiserTunnel()&&initialiserPisteEtCrash())){ 
        }
    }

    public Zone getZone(int x, int y) {
        return this.zones[y][x];
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
                if(this.zones[y][x] instanceof Zone_Normale 
                    &&  ((Zone_Normale)this.zones[y][x]).getpieceCachee().equals("")){
                    this.zones[y][x] = new Piste_Vol(x,y);
                     ok=true;
                    }
            }
        ok=false;
            while(!ok){
                x=rand.nextInt(5);
                y=rand.nextInt(5);
                if(this.zones[y][x] instanceof Zone_Normale 
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
                if(this.zones[y][x] instanceof Zone_Normale 
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
                if (this.zones[i][j] instanceof Zone_Normale && !((Zone_Normale)this.zones[i][j]).getpieceCachee().equals("")) {
                    ok=false;
                    while (!ok) {
                        if (nbEssais>PAREFEU) {
                            return false;
                        }
                        nbEssais++;
                        x=rand.nextInt(5);
                        if (this.zones[i][x] instanceof Zone_Normale && ((Zone_Normale)this.zones[i][j]).getpieceCachee().equals("")) {
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
                        if (this.zones[y][i] instanceof Zone_Normale && ((Zone_Normale)this.zones[y][i]).equals("")) {
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
        boolean ok;
        for (int i=0;i<2;i++){
            ok=false;
            while(!ok){
                x=rand.nextInt(5);
                y=rand.nextInt(5);
                if(this.zones[y][x] instanceof Zone_Normale 
                    &&  ((Zone_Normale)this.zones[y][x]).getpieceCachee().equals("")){
                    this.zones[y][x] = new Oasis(x,y,false);
                     ok=true;
                    }
            }
        }

        
        ok = false;
        while(!ok){
                x=rand.nextInt(5);
                y=rand.nextInt(5);
                if(this.zones[y][x] instanceof Zone_Normale 
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
                if(this.zones[y][x] instanceof Zone_Normale 
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

    //!!!à finir
    private void finDeTour() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'finDeTour'");
    }    

    public int totalSable(){
        int total=0;
        for(int i=0;i<5;i++){
            for(int j=0;j<5;j++){
                total+=this.zones[i][j].getnbSable();
            }
        }
        return total;
    }  

    private void soufflerVent(){
        int x=rand.nextInt(4);
        int distance = rand.nextInt(2)+1;
        Direction dir = Direction.values()[x];
        Zone zone;
        switch (dir) {
            case HAUT:
                for(int i=0;i<distance;i++){
                    if (this.yOeil!=0) {
                        this.yOeil--;
                        zone=getZone(this.xOeil,this.yOeil);
                        if (zone.getnbSable()<2) {
                            zone.setNbSable(zone.getnbSable()+1);
                            this.totalSable++;
                        }
    
                    }
                }
                
                break;
            case BAS:
                for(int i=0;i<distance;i++){
                    if (this.yOeil!=4) {
                        this.yOeil++;
                        zone=getZone(this.xOeil,this.yOeil);
                        if (zone.getnbSable()<2) {
                            zone.setNbSable(zone.getnbSable()+1);
                            this.totalSable++;
                        }
                    }
                }
                break;
            case DROITE:
                for(int i=0;i<distance;i++){
                    if (this.xOeil!=4) {
                        this.xOeil++;
                        zone=getZone(this.xOeil,this.yOeil);
                        if (zone.getnbSable()<2) {
                            zone.setNbSable(zone.getnbSable()+1);
                            this.totalSable++;
                        }
                    }
                }
                break;
            case GAUCHE:
                for(int i=0;i<distance;i++){
                    if (this.xOeil!=0) {
                        this.xOeil--;
                        zone=getZone(this.xOeil,this.yOeil);
                        if (zone.getnbSable()<2) {
                            zone.setNbSable(zone.getnbSable()+1);
                            this.totalSable++;
                        }
                    }
                }
                break;
        
            default:
                break;
        }
    }

    private void vagueDeChaleur() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'vagueDeChaleur'");
    }

    private void tempeteSeDechaine() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'tempeteSeDechaine'");
    }
    
    private boolean estVicoire() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'estVicoire'");
    }
    private boolean estDefaite() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'est Defaite'");
    }
    private boolean estEnsable() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'estEnsable'");
    }
    public void afficherGrille() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("afficherGrille'");
    }



}


