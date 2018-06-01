package game.constants;

import game.entities.Card;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@ApplicationScoped
public class CardHolder {
    private int startId=Constants.START_CARD_INDEX.getValue();

    private Card[] cards=new Card[]{new Card(startId++, "Symbiosis"),
            new Card(startId++, "Piracy"),
            new Card(startId++, "Grazing"),
            new Card(startId++, "Tail loss", "DeleteProperty"),
            new Card(startId++, "Hibernation"),
            new Card(startId++, "Poisonous"),
            new Card(startId++, "Communication", "Predator"),
            new Card(startId++, "Scavenger"),
            new Card(startId++, "Running"),
            new Card(startId++, "Mimicry"),
            new Card(startId++, "Camouflage"),
            new Card(startId++, "Burrowing"),
            new Card(startId++, "Sharp Vision"),
            new Card(startId++, "Parasite", "Predator"),
            new Card(startId++, "Parasite", "Fat"),
            new Card(startId++, "Cooperation", "Predator"),
            new Card(startId++, "Cooperation", "Fat"),
            new Card(startId++, "Big", "Predator"),
            new Card(startId++, "Big", "Fat"),
            new Card(startId++, "Swimming"),
            new Card(startId++, "Swimming")
    };

    public List<Card> getCards() {
        ArrayList<Card> all = new ArrayList<>(cards.length);
        all.addAll(new ArrayList<>(Arrays.asList(cards)));
        all.addAll(new ArrayList<>(Arrays.asList(cards)));
        all.addAll(new ArrayList<>(Arrays.asList(cards)));
        all.addAll(new ArrayList<>(Arrays.asList(cards)));
        Collections.shuffle(all);
        return all;
    }

    public static boolean isDouble(String property) {
        return property.equals("Cooperation") || property.equals("Communication") || property.equals("Symbiosis");
    }

    public int cardDeckSize(){
        return cards.length;
    }
}
