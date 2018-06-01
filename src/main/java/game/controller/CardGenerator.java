package game.controller;

import game.constants.Constants;
import game.entities.Card;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CardGenerator implements Serializable {
    List<Card> cardList;

    CardGenerator(){
        int cardID = Constants.START_CARD_INDEX.getValue();
        int duplicate=Constants.NUMBER_OF_DUPLICATE_CARDS.getValue();
        cardList = new ArrayList<>(Constants.TOTAL_NUMBER_OF_CARDS.getValue());
        for (int i = 0; i < duplicate; i++) {
            cardList.add(new Card(cardID++, "Symbiosis"));
            cardList.add(new Card(cardID++, "Piracy"));
            cardList.add(new Card(cardID++, "Grazing"));
            cardList.add(new Card(cardID++, "Tail loss","DeleteProperty"));
            cardList.add(new Card(cardID++, "Hibernation"));
            cardList.add(new Card(cardID++, "Poisonous"));
            cardList.add(new Card(cardID++, "Communication","Predator"));
            cardList.add(new Card(cardID++, "Scavenger"));
            cardList.add(new Card(cardID++, "Running"));
            cardList.add(new Card(cardID++, "Mimicry"));
            cardList.add(new Card(cardID++, "Camouflage"));
            cardList.add(new Card(cardID++, "Burrowing"));
            cardList.add(new Card(cardID++, "Sharp Vision"));
            cardList.add(new Card(cardID++, "Parasite", "Predator"));
            cardList.add(new Card(cardID++, "Parasite", "Fat"));
            cardList.add(new Card(cardID++, "Cooperation", "Predator"));
            cardList.add(new Card(cardID++, "Cooperation", "Fat"));
            cardList.add(new Card(cardID++, "Big", "Predator"));
            cardList.add(new Card(cardID++, "Big", "Fat"));
            cardList.add(new Card(cardID++, "Swimming"));
            cardList.add(new Card(cardID++, "Swimming"));
        }
    }

    public List<Card> getCards(){
        Collections.shuffle(cardList);
        return cardList;
    }

}
