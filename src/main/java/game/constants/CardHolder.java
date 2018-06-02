package game.constants;

import game.entities.Card;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CardHolder {
    private int startId = Constants.START_CARD_INDEX.getValue();
    private List<Card> cards = new ArrayList<>(Constants.TOTAL_CARD_NUMBER.getValue());

    public CardHolder() {
        for (int i = 0; i < 4; i++) {
            cards.add(new Card(startId++, "Symbiosis"));
            cards.add(new Card(startId++, "Piracy"));
            cards.add(new Card(startId++, "Grazing"));
            cards.add(new Card(startId++, "Tail loss", "DeleteProperty"));
            cards.add(new Card(startId++, "Hibernation"));
            cards.add(new Card(startId++, "Poisonous"));
            cards.add(new Card(startId++, "Communication", "Predator"));
            cards.add(new Card(startId++, "Scavenger"));
            cards.add(new Card(startId++, "Running"));
            cards.add(new Card(startId++, "Mimicry"));
            cards.add(new Card(startId++, "Camouflage"));
            cards.add(new Card(startId++, "Burrowing"));
            cards.add(new Card(startId++, "Sharp Vision"));
            cards.add(new Card(startId++, "Parasite", "Predator"));
            cards.add(new Card(startId++, "Parasite", "Fat"));
            cards.add(new Card(startId++, "Cooperation", "Predator"));
            cards.add(new Card(startId++, "Cooperation", "Fat"));
            cards.add(new Card(startId++, "Big", "Predator"));
            cards.add(new Card(startId++, "Big", "Fat"));
            cards.add(new Card(startId++, "Swimming"));
            cards.add(new Card(startId++, "Swimming"));
        }
    }

    public List<Card> getCards() {
        List<Card> all = new ArrayList<>(cards);
        Collections.shuffle(all);
        return all;
    }

    public static boolean isDouble(String property) {
        return property.equals("Cooperation") || property.equals("Communication") || property.equals("Symbiosis");
    }

    public int cardDeckSize() {
        return cards.size();
    }
}
