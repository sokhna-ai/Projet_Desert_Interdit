public class Piece {
    private int x, y;
    private String piece;
    private boolean xIndice, yIndice;
    private boolean recuperee = false;

    public Piece(int x, int y, String piece) {
        this.x = x;
        this.y = y;
        this.piece = piece;
        xIndice = false;
        yIndice = false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getPiece() {
        return piece;
    }

    public void setXIndice(boolean xIndice) {
        this.xIndice = xIndice;
    }

    public void setYIndice(boolean yIndice) {
        this.yIndice = yIndice;
    }

    public boolean estLocalisee() {
        return xIndice && yIndice;
    }

    public boolean estRecuperee() {
        return recuperee;
    }

    public void setRecuperee(boolean r) {
        recuperee = r;
    }

    public boolean hasXIndice() {
        return xIndice;
    }

    public boolean hasYIndice() {
        return yIndice;
    }
}