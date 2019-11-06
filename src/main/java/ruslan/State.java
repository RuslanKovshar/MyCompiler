package ruslan;

import java.util.Objects;

public class State {
    private int number;
    private String type;

    public State(int number, String type) {
        this.number = number;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return number == state.number &&
                Objects.equals(type, state.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, type);
    }

    @Override
    public String toString() {
        return "State{" +
                "number=" + number +
                ", type='" + type + '\'' +
                '}';
    }
}
