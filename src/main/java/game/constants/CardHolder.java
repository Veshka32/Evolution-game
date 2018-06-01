package game.constants;

import game.entities.Card;

import javax.decorator.Decorator;
import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@ApplicationScoped
public class CardHolder {

    public static Card getCard(int i) {
        return CardEnum.CARDS.getCard(i);
    }

    public List<Card> getCards() {
        return CardEnum.CARDS.getCards();
    }

    public static boolean isDouble(String property) {
        return property.equals("Cooperation") || property.equals("Communication") || property.equals("Symbiosis");
    }

    enum CardEnum {

        CARDS(new Card[]{new Card(1, "Symbiosis"),
                new Card(2, "Piracy"),
                new Card(3, "Grazing"),
                new Card(4, "Tail loss", "DeleteProperty"),
                new Card(5, "Hibernation"),
                new Card(6, "Poisonous"),
                new Card(7, "Communication", "Predator"),
                new Card(8, "Scavenger"),
                new Card(9, "Running"),
                new Card(10, "Mimicry"),
                new Card(11, "Camouflage"),
                new Card(12, "Burrowing"),
                new Card(13, "Sharp Vision"),
                new Card(14, "Parasite", "Predator"),
                new Card(15, "Parasite", "Fat"),
                new Card(16, "Cooperation", "Predator"),
                new Card(17, "Cooperation", "Fat"),
                new Card(18, "Big", "Predator"),
                new Card(19, "Big", "Fat"),
                new Card(20, "Swimming"),
                new Card(21, "Swimming")
        });

        private Card[] cards;

        CardEnum(Card[] cards) {
            this.cards = cards;
        }

        public Card getCard(int i) {
            return cards[i];
        }

        public List<Card> getCards() {
            ArrayList<Card> all = new ArrayList<>(Constants.TOTAL_NUMBER_OF_CARDS.getValue());
            all.addAll(new ArrayList<>(Arrays.asList(cards)));
            all.addAll(new ArrayList<>(Arrays.asList(cards)));
            all.addAll(new ArrayList<>(Arrays.asList(cards)));
            all.addAll(new ArrayList<>(Arrays.asList(cards)));
            Collections.shuffle(all);
            return all;
        }
    }
}
