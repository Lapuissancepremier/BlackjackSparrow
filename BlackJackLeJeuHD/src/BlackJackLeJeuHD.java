import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class BlackJackLeJeuHD {
    private class Carte{
        String valeur;
        String type;

        Carte(String valeur, String type){
            this.valeur = valeur;
            this.type = type;
        }

        public String toString() {              // pour convertir le nom de la carte par sa valeur - type comme enregistré dans le dossier carte.
            return valeur + "-" + type;

        }

        public int getValue(){
            if ("AJQK".contains(valeur)){ // A J Q K
                if (valeur == "A") {
                    return 11;
                }
                return 10;
            }
            return Integer.parseInt(valeur); //2 -> 10
        }
        public boolean estunAs(){
            return valeur == "A";
        }

        public String getImageChem(){
            return "./cartes/" + toString() + ".png";
        }





        }

    ArrayList<Carte> deck;
    Random random = new Random(); // mélangé le deck

    //dealer
    Carte hiddenCarte;
    ArrayList<Carte> dealerhand;
    int dealersomme;
    int dealerNbAs;

    //Joueur
    ArrayList<Carte> mainjoueur;
    int joueurSomme;
    int joueurNbAs;






        public void gamewindow(){
            
            //fenêtre
            JFrame fene = new JFrame("BlackJack Le Jeu HD");
            int fenlarg = 1800;
            int fenHaut = 1020;
            fene.setVisible(true);
            fene.setSize(fenlarg, fenHaut);
            fene.setLocationRelativeTo(null);
            fene.setResizable(false);
            fene.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    
            //bouton
            JPanel bouttonPanel = new JPanel();
            JButton hitBoutton = new JButton("Hit");
            JButton stayboutton = new javax.swing.JButton("Stay");
            hitBoutton.setFocusable(false);
            bouttonPanel.add(hitBoutton);
            stayboutton.setFocusable(false);
            bouttonPanel.add(stayboutton);

    
    
    
    
            //gamepanel
            int largCarte = 210;
            int hautCarte = 300;
    
            JPanel gamPanel = new JPanel(){
                @Override
                public void paintComponent(Graphics g) {
                    super.paintComponent(g);
    
                    try {
    
                        //carte face cachée
                        Image carteFaceCacheImg = new ImageIcon(getClass().getResource("./cartes/DOS.png")).getImage();
                        if (!stayboutton.isEnabled()){
                            carteFaceCacheImg = new ImageIcon(getClass().getResource(hiddenCarte.getImageChem())).getImage();
                        }
                        g.drawImage(carteFaceCacheImg, 350, 20, largCarte, hautCarte, null);
    
                        //carte du dealer
                        for (int i = 0; i < dealerhand.size();i++){
                            Carte carte = dealerhand.get(i);
                            Image carteImg = new ImageIcon(getClass().getResource(carte.getImageChem())).getImage();
                            g.drawImage(carteImg, largCarte + 360 + (largCarte + 5)*i, 20, largCarte, hautCarte, null);

                        };
                        
                        //carte joueur
                        for (int i = 0; i < mainjoueur.size();i++){
                            Carte carte = mainjoueur.get(i);
                            Image carteImg = new ImageIcon(getClass().getResource(carte.getImageChem())).getImage();
                            g.drawImage(carteImg, 350 + (largCarte + 5)*i, 625, largCarte, hautCarte, null);
                        }

                        if (!stayboutton.isEnabled()){
                            dealersomme = reductionAsDealer();
                            joueurSomme = reductionAsJoueur();
                            System.out.println("Stay");
                            System.out.println(dealersomme);
                            System.out.println(joueurSomme);

                            String message = "";
                            if (joueurSomme > 21 ) {
                                message = " PERDU LOOSER";
                                g.setColor(Color.RED);
                            }
                            else if (dealersomme > 21) {
                                message = "GAGNE GAGNANT";
                                g.setColor(Color.ORANGE);
                            }
                            else if (joueurSomme == dealersomme){
                                message = " EGALITE DE LOOSER";
                                g.setColor(Color.WHITE);
                            }
                            else if (joueurSomme > dealersomme){
                                message = "GAGNE GAGNANT";
                                g.setColor(Color.ORANGE);
                            }
                            else if ( joueurSomme < dealersomme) {
                                message = "PERDU LOOSER";
                                g.setColor(Color.RED);
                            }
                            

                            g.setFont(new Font("Arial", Font.PLAIN, 50));
                            g.drawString(message, 500, 500);
                            
                            



                        }
    
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
    
            };
            gamPanel.setBackground(new Color(53, 101,77));
    
    
    
    
    
            //affichage
            fene.add(gamPanel);
            fene.add(bouttonPanel, BorderLayout.SOUTH);

            hitBoutton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e){
                    Carte carte = deck.remove(deck.size()-1);
                    joueurSomme += carte.getValue();
                    joueurNbAs += carte.estunAs()? 1 : 0;
                    mainjoueur.add(carte);
                    if (reductionAsJoueur() > 21) {
                        hitBoutton.setEnabled(false);;
                    }
                    gamPanel.repaint();
            }});

            stayboutton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    hitBoutton.setEnabled(false);
                    stayboutton.setEnabled(false);

                    while (dealersomme < 17) {
                        Carte carte = deck.remove(deck.size()-1);
                        dealersomme += carte.getValue();
                        dealerNbAs += carte.estunAs()? 1 : 0;
                        dealerhand.add(carte);
                    }
                    gamPanel.repaint();
                }
            });



            gamPanel.repaint();
        }
        
    

    

    


    BlackJackLeJeuHD() {
        startGame();
        gamewindow();
        
        
    System.out.println("execution...");
    }


    public void startGame(){
        //deck
        buildDeck();
        melangedeck();

        //dealer
        dealerhand = new ArrayList<Carte>();
        dealersomme = 0;
        dealerNbAs = 0;

        hiddenCarte = deck.remove(deck.size()-1); // retirer la dernière carte au dernier index
        dealersomme += hiddenCarte.getValue();
        dealerNbAs += hiddenCarte.estunAs() ? 1 : 0;
        

        Carte carte = deck.remove(deck.size()-1);
        dealersomme +=carte.getValue();
        dealerNbAs += carte.estunAs() ? 1 : 0;
        dealerhand.add(carte);


        System.out.println("Dealer");
        System.out.println(hiddenCarte);
        System.out.println(dealerhand);
        System.out.println(dealersomme);
        System.out.println(dealerNbAs);

        //Joueur
        mainjoueur = new ArrayList<Carte>();
        joueurSomme = 0;
        joueurNbAs = 0;

        for (int i = 0; i < 2; i++ ){
            carte = deck.remove(deck.size()-1);
            joueurSomme += carte.getValue();
            joueurNbAs += carte.estunAs() ?1 :0 ;
            mainjoueur.add(carte);
        }



        System.out.println("Joueur :");
        System.out.println(mainjoueur);
        System.out.println(joueurSomme);
        System.out.println(joueurNbAs);


    }

    
    public void buildDeck(){
        deck = new ArrayList<Carte>();
        String[] valeurs = { "A", "2", "3","4","5","6","7","8","9","10","J","Q","K"};
        String[] types = {"T","CA","CO","P" };

        for (int i = 0; i < types.length; i++) {
            for(int j = 0; j< valeurs.length; j++) {
                Carte carte = new Carte(valeurs[j], types[i]);
                deck.add(carte);
            }
        }

        System.out.println("Préparation du deck :");
        System.out.println(deck);

    }

    public void melangedeck(){
        for (int i = 0; i< deck.size(); i++){
            int j = random.nextInt(deck.size());
            Carte currCarte = deck.get(i);
            Carte randomCarte = deck.get(j);
            deck.set(j, randomCarte);
            deck.set(j, currCarte);

        }

        System.out.println("Après le mélange :");
        System.out.println(deck);
    }

    public int reductionAsJoueur(){
        while (joueurSomme > 21 && joueurNbAs > 0) {
            joueurSomme -= 10;
            joueurNbAs -= 1;
        }
        return joueurSomme;
    }
    public int reductionAsDealer() {
        while (dealersomme > 21 && dealerNbAs > 0) {
            dealersomme -= 10;
            dealerNbAs -= 1;
        }
        return dealersomme ;


    }

    }

