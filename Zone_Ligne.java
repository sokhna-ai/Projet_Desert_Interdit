public class Zone_Ligne extends Zone{
    protected int numeroLigne;
    protected String pieceReference;
    public Zone_Ligne(int x, int y, int nl, String pr){
        super(x,y);
        this.pieceReference = pr;
        this.numeroLigne = nl;
    }
    public int getNumeroLigne(){
        return this.numeroLigne;
    }
    public void setNumeroLigne(int n){
        this.numeroLigne = n;
    }
    public String getpieceReference(){
        return this.pieceReference;
    }
    public void setpieceReference(String p){
        this.pieceReference = p;
    }
    @Override
    public String getSymbole(){
        return "L";
    }
    public void actionSpeciale(Desert d) {
        System.out.println(this.pieceReference + " est sur la ligne " + this.numeroLigne);
    }
}
