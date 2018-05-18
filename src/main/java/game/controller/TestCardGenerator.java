package game.controller;

import game.entities.Card;

import java.util.List;

public class TestCardGenerator extends CardGenerator {

    @Override
    public List<Card> getCards() {
        return cardList; //no shuffle
    }
}
