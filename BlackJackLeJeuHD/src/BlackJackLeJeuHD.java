import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
import java.awt.GraphicsEnvironment;

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

    //fenêtre
    int fenLarg = 600;
    int fenHaut = 800;
    
    //JFrame fene = new JFrame("BlackJack Le Jeu HD");
    


    BlackJackLeJeuHD() {
        startGame();

        //fenêtre
    /*  fene.setVisible(true);
        fene.setSize(fenLarg, fenHaut);
        fene.setLocationRelativeTo(null);
        fene.setResizable(false);
        fene.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);*/
        
    
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



}