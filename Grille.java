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
import java.lang.reflect.Array;
import java.util.ArrayList;
/**
     * Classe qui représente la grille
     */
    class Grille extends JPanel {
        Desert desert;
        boolean testMode = true;
        ArrayList<ArrayList<Case>> cases = new ArrayList();
        Grille(Desert desert) {
            super();
            this.setLayout(new GridLayout(5, 5));
            this.setBackground(new Color(0x7BB77E));
            this.desert = desert;
            
        }
        private Case getCase(int i, int j) 
        { 
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        private boolean setGrille() {
            for (int j = 0; j < 5; j++) {
                ArrayList<Case> ligne = new ArrayList();
                for (int i = 0; i < 5; i++) {
                    this.add(setCase(desert.getZone(i, j)));
                }
                this.cases.add(ligne);
            }
            return true;
        }

        private Case setCase(Zone zone){
            if (zone instanceof Zone_Normale) {
                return new CaseNormale((Zone_Normale)zone);
            }
            if (zone instanceof Zone_Ligne) {
                return new CaseLigne((Zone_Ligne)zone);
            }
            if (zone instanceof Zone_Colonne) {
                return new CaseColonne((Zone_Colonne)zone);
            }
            if (zone instanceof Piste_Vol) {
                return new CasePiste((Piste_Vol)zone);
            }
            if (zone instanceof Tunnel) {
                return new CaseTunnel((Tunnel)zone);
            }
            if (zone instanceof Oeil_Tempête) {
                return new CaseOeilTempete((Oeil_Tempête)zone);
            }
            
            return null;
        }

        abstract class Case extends JPanel {
            protected JLayeredPane layer;
            Case() {
                super();
                this.setLayout(new BorderLayout());
                this.layer = new JLayeredPane();
                this.add(layer,BorderLayout.CENTER);
            }
        }
        class CaseNormale extends Case {
            String piece;
            Zone_Normale z;
            CaseNormale(Zone_Normale z) {
                super();
                this.setBackground(new Color(0xdbb748));
                this.piece = z.getpieceCachee();
                this.z = z;
                showPiece();
            }
            private void showPiece() {
                JLabel label = new JLabel("",SwingConstants.CENTER);
                if (testMode) {
                    label.setText("x: "+z.getX()+" Y: "+z.getY()+"  Zone Normal  "+ this.piece);
                }
                else{
                    label.setText("x: "+z.getX()+" Y: "+z.getY());
                }
                label.setOpaque(false);
                label.setVisible(z.getExploree());
                this.layer.add(label);
            }
        }
        class CaseLigne extends Case {
            Zone_Ligne z;
            String piece;
            int ligne;
            CaseLigne(Zone_Ligne z) {
                super();
                this.z=z;
                this.setBackground(new Color(0x7BB77E));
                this.piece=z.getpieceReference();
            }
            private void showPiece() {
                JLabel label = new JLabel("",SwingConstants.CENTER);
                if (testMode) {
                    label.setText("x: "+z.getX()+" Y: "+z.getY()+"  Zone Ligne");
                }
                else{
                    label.setText("x: "+z.getX()+" Y: "+z.getY());
                }
                label.setOpaque(false);
                label.setVisible(z.getExploree());
                this.layer.add(label);
            }
        }

        class CaseColonne extends Case {
            CaseColonne(Zone_Colonne z) {
                super();
                this.setBackground(new Color(0x7BB77E));
            }
        }

        class CasePiste extends Case {
            CasePiste(Piste_Vol z) {
                super();
                this.setBackground(new Color(0x7BB77E));
            }
        }

        class CaseTunnel extends Case {
            CaseTunnel(Tunnel z) {
                super();
                this.setBackground(new Color(0x7BB77E));
            }
        }

        class CaseOeilTempete extends Case {
            CaseOeilTempete(Oeil_Tempête z) {
                super();
                this.setBackground(new Color(0x7BB77E));
            }
        }

        class CaseOasis extends Case {
            CaseOasis(Oasis z) {
                super();
                this.setBackground(new Color(0x7BB77E));
            }
        }
        
    }