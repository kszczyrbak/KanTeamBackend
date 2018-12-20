package szczkrzy.kanteam.model.enums;

public enum TaskColor {
    BLACK(0),
    RED(1),
    YELLOW(2),
    GREEN(3),
    BLUE(4);

    private final int value;

    TaskColor(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
