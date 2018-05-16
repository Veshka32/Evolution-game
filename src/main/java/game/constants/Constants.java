package game.constants;

public enum Constants {
    START_NUMBER_OF_CARDS (6),
    START_CARD_INDEX (1),
    NUMBER_OF_PLAYER(2),
    TOTAL_NUMBER_OF_CARDS(84),
    FLAG_FOR_COMMUNICATION(2),
    FLAG_FOR_COOPERATION(1);


    private final int id;

    Constants(int id) {
        this.id=id;
    }

    public int getValue() {
        return id;
    }
}
