package model;

public enum Constants {
    START_NUMBER_OF_CARDS (6),
    START_CARD_INDEX (1),
    NUMBER_OF_PLAYER(2);


    private final int id;

    Constants(int id) {
        this.id=id;
    }

    public int getValue() {
        return id;
    }
}
