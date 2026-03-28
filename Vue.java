import javax.swing.JFrame;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

public class Vue extends JFrame{

    private Desert desert;
    private GridBagLayout layout; //J'utlise GridBagLayout pour les cases

    public Vue() {
        desert = new Desert();
        initialiserComposants();
    }

    
    private boolean initialiserComposants() {
        setLayout();
        return true;
    }

    private boolean setLayout() {
        layout = new GridBagLayout();
        this.setLayout(layout);
        return true;
    }
}
