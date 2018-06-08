package game.controller;

import game.entities.Animal;
import game.entities.Card;

import java.util.List;

public class Changes {
    private int food;
    private List<Integer> deletedAnimals;
    private List<Animal> newAnimals;
    private List<Animal> changedAnimals;
    private int deletedCardId;
    private List<Card> addedCards;
    private String phase;
    private String log;
}
