public class Message {
    private String variable;
    private int value;

    public Message(String variable, int value) {
        this.variable = variable;
        this.value = value;
    }

    public String getVariable() {
        return variable;
    }

    public int getValue() {
        return value;
    }
}
