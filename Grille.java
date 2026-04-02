import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
/**
     * Classe qui représente la grille
     */
    class Grille extends JPanel {
        Grille() {
            super();
            this.setLayout(new GridLayout(5, 5));
            this.setBackground(new Color(0x7BB77E));
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    JLabel label = new JLabel("case" + i + " " + j,SwingConstants.CENTER);
                    label.setOpaque(false);
                    this.add(label);
                }
            }
        }
        private Case getCase(int i, int j) 
        { 
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        class Case extends JPanel {
            protected JLayeredPane layer;
            Case() {
                super();
                this.setLayout(new BorderLayout());
                this.layer = new JLayeredPane();
                this.add(layer,BorderLayout.CENTER);
            }
        }
        class caseNormale extends Case {
            String piece;
            Zone_Normale z;
            caseNormale(Zone_Normale z) {
                super();
                this.setBackground(new Color(0xdbb748));
                this.piece = z.getpieceCachee();
            }
            private void showPiece() {
                JLabel label = new JLabel(this.piece,SwingConstants.CENTER);
                label.setOpaque(false);
                label.setVisible(z.getExploree());
                this.layer.add(label);
            }
        }
        
    }