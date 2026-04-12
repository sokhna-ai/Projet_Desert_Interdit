public class Zone_Colonne extends Zone{
    protected int numeroColonne;
    protected Piece pieceReference;
    public Zone_Colonne(int x, int y,  Piece piece){
        super(x,y);
        this.pieceReference = piece;
        numeroColonne=piece.getX();
    }
    public int getNumeroColonne(){
        return this.numeroColonne;
    }
    public void setNumeroColonne(int n){
        this.numeroColonne = n;
    }
    public String getpieceReference(){
        return this.pieceReference.getPiece();
    }
    
    @Override
    public String getSymbole(){
        return "C";
    }
    public void actionSpeciale(Desert d) {
        System.out.println(this.pieceReference.getPiece() + " est sur la Colonne " + this.numeroColonne);
        pieceReference.setXIndice(true);
    }
}