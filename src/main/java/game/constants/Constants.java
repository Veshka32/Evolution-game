package game.constants;

public enum Constants {
    START_NUMBER_OF_CARDS (6),
    START_CARD_INDEX (1),
    NUMBER_OF_PLAYER(2),
    NUMBER_OF_EXTRA_CARD(1),
    MIN_FOOD(2),
    MAX_FOOD(8);

    private final int id;

    Constants(int id) {
        this.id=id;
    }

    public int getValue() {
        return id;
    }
}
