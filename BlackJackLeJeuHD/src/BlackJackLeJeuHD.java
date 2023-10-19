import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class BlackJackLeJeuHD {
    private class carte{
        String valeur;
        String type;

        carte(String valeur, String type){
            this.valeur = valeur;
            this.type = type;
        }
    }

    ArrayList<carte> deck;

    BlackJackLeJeuHD() {
        startGame();
    }

    public void startGame(){
        //deck
        buildDeck();


    }
    
    public void buildDeck(){
        deck = new ArrayList<carte>();
        String[] valeurs = {}

    }



}