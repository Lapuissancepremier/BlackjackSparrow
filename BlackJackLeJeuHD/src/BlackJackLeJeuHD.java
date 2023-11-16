import java.awt.*;
import java.awt.event.*;
import java.nio.channels.SelectableChannel;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;


public class BlackJackLeJeuHD {
    private class DosCartes{
        String nom;
        String prix;

        DosCartes(String nom, String prix){
            this.nom = nom;
            this.prix = prix;
        }

        public String dosCartesChoisi(){
            return nom + "-" + prix;

        }

        public String getDosCartesChem(){
            return "./asset/doscartes/" + dosCartesChoisi() + ".png";
        }

    }



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
            return "./asset/cartes/" + toString() + ".png";
        }



    }




        

    ArrayList<Carte> deck;
    Random random = new Random(); // mélangé le deck

    //dealer
    Carte hiddenCarte;
    DosCartes doscartes;
    ArrayList<Carte> dealerhand;
    int dealersomme;
    int dealerNbAs;

    //Joueur
    ArrayList<Carte> mainjoueur;
    int joueurSomme;
    int joueurNbAs;






        public void gamewindow(){
            int fenlarg = 1800;
            int fenHaut = 1020;
            
            //fenêtre
            JFrame fene = new JFrame("BlackJack Le Jeu HD");
            fene.setVisible(true);
            fene.setSize(fenlarg, fenHaut);
            fene.setLocationRelativeTo(null);
            fene.setResizable(false);
            fene.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            //layout
            
            //Image & icon
            Icon optIcon = new ImageIcon(getClass().getResource("./asset/option_petit.png"), "Option");
            Icon menuIcon = new ImageIcon(getClass().getResource("./asset/quitter_petit.png"), "Menu principal");
            Icon deco1 = new ImageIcon(getClass().getResource("./asset/deco1.png"));
            Icon jetonIcon = new ImageIcon(getClass().getResource("./asset/jeton_petit.png"));
            Icon argentIcon = new ImageIcon(getClass().getResource("./asset/argent_petit.png"));
            

            //bouton & panel
            JPanel bouttonPanel = new JPanel();

            JButton hitBoutton = new JButton("Pioche");
            JButton stayboutton = new javax.swing.JButton("Check");
            JButton replayboutton = new JButton("Rejouer");

            int valeurMise =  0;
            int argentJoueur = 500;

            JPanel panelgauche1 = new JPanel();
            BoxLayout box = new BoxLayout(panelgauche1, 1);
            
            JButton deco1Button = new JButton(deco1);
            JButton miser = new JButton("Miser");
            JButton retourmenu = new JButton(menuIcon);
            JButton optioButton = new JButton(optIcon);
            

            //Jlabel
            JLabel miseJoueur = new JLabel("" + valeurMise, jetonIcon, 0);
            JLabel ArgentJoueur = new JLabel("" + argentJoueur, argentIcon, 0);



            deco1Button.setEnabled(false);
            miser.setFocusable(false);
            hitBoutton.setFocusable(false);
            bouttonPanel.add(hitBoutton);
            stayboutton.setFocusable(false);
            bouttonPanel.add(stayboutton);
            replayboutton.setFocusable(false);
            replayboutton.setEnabled(false);
            bouttonPanel.add(replayboutton);

            


            
            
            
            
            
            

            
        
            
    
    
            //gamepanel
            int largCarte = 210;
            int hautCarte = 300;





    
            JPanel gamPanel = new JPanel(){
                @Override
                
                public void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    
                

                    try {

                        
    
                        //carte face cachée
                        Image carteFaceCacheImg = new ImageIcon(getClass().getResource("/asset/doscartes/base-0.png")).getImage();
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
                            replayboutton.setEnabled(true);
                            dealersomme = reductionAsDealer();
                            joueurSomme = reductionAsJoueur();
                            System.out.println("Stay");
                            System.out.println(dealersomme);
                            System.out.println(joueurSomme);

                            String message = "";
                            if (joueurSomme > 21 ) {
                                message = " PERDU LOOSER";
                                CompteurVictoire_defaite(1);
                                g.setColor(Color.RED);
                                miseJoueur.setText("0");

                            }
                            else if (dealersomme > 21) {
                                message = "GAGNE GAGNANT";
                                CompteurVictoire_defaite(0);
                                g.setColor(Color.ORANGE);
                                ArgentJoueur.setText("" + (Integer.parseInt(ArgentJoueur.getText()) + 1.5* Integer.parseInt(miseJoueur.getText())));
                                miseJoueur.setText("0");

                            }
                            else if (joueurSomme == dealersomme){
                                message = " EGALITE DE LOOSER";
                                CompteurVictoire_defaite(1);
                                g.setColor(Color.WHITE);
                                ArgentJoueur.setText("" + (Integer.parseInt(ArgentJoueur.getText()) + Integer.parseInt(miseJoueur.getText())));
                                miseJoueur.setText("0");
                                
                            }
                            else if (joueurSomme > dealersomme){
                                message = "GAGNE GAGNANT";
                                CompteurVictoire_defaite(0);
                                g.setColor(Color.ORANGE);
                                ArgentJoueur.setText("" + (Integer.parseInt(ArgentJoueur.getText()) + 1.5* (int)Integer.parseInt(miseJoueur.getText())));
                                miseJoueur.setText("0");
                            }
                            else if ( joueurSomme < dealersomme) {
                                message = "PERDU LOOSER";
                                CompteurVictoire_defaite(1);
                                g.setColor(Color.RED);
                                miseJoueur.setText("0");
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
            
            panelgauche1.setLayout(box);
            panelgauche1.add(retourmenu);
            panelgauche1.add(optioButton);
            panelgauche1.add(deco1Button);
            panelgauche1.add(miser);
            panelgauche1.add(miseJoueur);
            panelgauche1.add(ArgentJoueur);
            fene.add(panelgauche1, BorderLayout.WEST);
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
            
            
                
            

            replayboutton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e){
                    startGame();
                    stayboutton.setEnabled(true);
                    hitBoutton.setEnabled(true);
                    replayboutton.setEnabled(false);
                    gamPanel.repaint();
                    
                }
            });

            retourmenu.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e){
                    MenuValid();
                    fene.dispose();

                    
                }
            });

            optioButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e){
                    optionMenu();
                }
            });

            miser.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e){
                    if(Integer.parseInt(ArgentJoueur.getText()) == 0){
                        miser.setEnabled(false);
                    }
                    if(hitBoutton.isEnabled() != true){
                        miser.setEnabled(false);
                    }
                    if(hitBoutton.isEnabled()== true){
                        miser.setEnabled(true);
                    }
                    if (Integer.parseInt(ArgentJoueur.getText()) != 0){
                        miser.setEnabled(true);
                        miseJoueur.setText("" + (Integer.parseInt(miseJoueur.getText()) + 10));
                        ArgentJoueur.setText("" + (Integer.parseInt(ArgentJoueur.getText()) - 10));
                    }                
                     
                }
            });

            



            gamPanel.repaint();


            
        
    }
            
            
            
            
        
    BlackJackLeJeuHD() {
        MenuValid();
        
        
        
        
    System.out.println("execution...");
    }

    int victoire = 0;
    int defaite = 0;
    int nbPartieJoue = 0;
    

    public int GestionMise(int sousTotal, int mise, int mise_gain) {
        int sousfinal = 0;
        if (mise_gain != 1){
            sousfinal = sousTotal - mise ;
        } 
        if(mise_gain == 1){
            sousfinal = sousTotal + mise;
        }
        return sousfinal ;


    }



    public void CompteurVictoire_defaite(int e){
        if(e == 0){
            victoire += 1;
            nbPartieJoue+=1;

            System.out.println("nombre de victoire :");
            System.out.println(victoire);  
        }

        if(e == 1){
            defaite += 1;
            nbPartieJoue += 1;
            System.out.println("nombre de défaite :");
            System.out.println(defaite); 
        }
    }


    public void optionMenu(){
        JFrame optionFrame = new JFrame("Option");

        optionFrame.setSize(1000, 500);
        optionFrame.setVisible(true);
        optionFrame.setLocationRelativeTo(null);
        optionFrame.setResizable(false);
        optionFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        

        //panel
        JPanel buttonPanel = new JPanel();
        /*
        JPanel graphiquePanel = new JPanel(){
                @Override
                
                public void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    
                

                    try {



                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
    
            };
            graphiquePanel.setBackground(Color.gray);
            */

        


        //layout
        Icon suivanIcon = new ImageIcon(getClass().getResource("./asset/suivant.png"));
        Icon retourIcon = new ImageIcon(getClass().getResource("./asset/retour.png"));
        Icon quitterIcon = new ImageIcon(getClass().getResource("./asset/quitter_petit.png"));
        Icon dos_petitIcon = new ImageIcon(getClass().getResource("./asset/dos_petit.png"));
    

        //Jlabel
        JLabel dos_petit = new JLabel(dos_petitIcon);


        //Bouton
        JButton graphiqueBasseBoutton = new JButton("Basse");
        JButton graphiqueHauteBoutton = new JButton("Haute");
        JButton suivanButton = new JButton("Suivant", suivanIcon);
        JButton retourButton = new JButton("Retour", retourIcon);
        JButton quitterButton = new JButton("Quitter", quitterIcon);



        buttonPanel.add(quitterButton);
        buttonPanel.add(retourButton);
        buttonPanel.add(dos_petit);
        buttonPanel.add(suivanButton);

        // graphiquePanel.repaint();
        //Affichage
        optionFrame.setLayout(null);
        optionFrame.add(buttonPanel, BorderLayout.SOUTH);
        //optionFrame.add(graphiquePanel);
        





        

        


        

        
        
    }



    public void MenuValid(){
        JFrame fene = new JFrame("BlackJackLeJeuHD");
        int fenlarg = 1800;
        int fenHaut = 1020;
            
        //fenêtre
        fene.setVisible(true);
        fene.setSize(fenlarg, fenHaut);
        fene.setLocationRelativeTo(null);
        fene.setResizable(false);
        fene.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        


        

        //Icon bouton
        ImageIcon optionIcon = new ImageIcon(getClass().getResource("./asset/option_menu.png"),"Option");
        ImageIcon quitterIcon = new ImageIcon(getClass().getResource("/asset/quitter_menu.png"), "Quitter");
        ImageIcon jouerIcon = new ImageIcon(getClass().getResource("/asset/jouer_menu.png"),"Jouer");
        Icon LogoIcon = new ImageIcon(getClass().getResource("./asset/logo.png"));
        

        //titre
        JLabel titre = new JLabel("BlackJackLeJeuHD");  
        JLabel logo = new JLabel(LogoIcon);

        logo.setBounds(0,   0,  1800, 1020);

        //Option
        int xo = (int)(fenlarg - fenlarg*0.75);
        int y = (int)(fenHaut -(100 + fenHaut*0.40));

        JButton OptionBoutton = new JButton(optionIcon);
       // OptionBoutton.setLayout(grid);

        OptionBoutton.setFocusable(true);
        OptionBoutton.setBounds(xo, y , 310, 470);

        
    
        // Jouer
        int xp = (int)(xo + 200);

        JButton Playboutton = new JButton(jouerIcon);
    


        //Playboutton.setLayout(grid);
        Playboutton.setBounds(xp, y, 310, 470);
        Playboutton.setFocusable(false);



        //quitter
        int xq = (int)(xp + 200);
        
        JButton Quitboutton = new JButton(quitterIcon);
        Quitboutton.setFocusable(false);
        Quitboutton.setBounds( xq, y, 310, 470);
        
       
       
    
        
       

        

        

        //affichage
        fene.setBackground(Color.MAGENTA);
        fene.setLayout(null);
        fene.add(logo);
        //fene.add(titre);
        fene.add(OptionBoutton);
        fene.add(Playboutton);
        fene.add(Quitboutton);
        

       Playboutton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e){
            Playboutton.setEnabled(false);
            Playboutton.setBackground(Color.green);
            fene.dispose();
            startGame();
            gamewindow();
            
            
            
            
        }});

       Quitboutton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e){
            fene.dispose();

        }});

       
       
       
        
       

        
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

