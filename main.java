public class Main {
    public static void main (String[] args) {
        Desert desert = new Desert();
        Vue vue = new Vue(desert);
        vue.setSize(1280, 720);
        vue.setVisible(true);
    }
}
