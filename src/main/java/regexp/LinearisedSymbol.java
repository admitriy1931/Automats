package regexp;

import java.util.Objects;

public class LinearisedSymbol {
    private final Character symbol;
    private final Integer number;

    public LinearisedSymbol(Character value, Integer number){
        this.symbol = value;
        this.number = number;
    }

    public Character getSymbol(){
        return symbol;
    }

    public Integer getNumber() {
        return number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LinearisedSymbol that = (LinearisedSymbol) o;
        return Objects.equals(symbol, that.symbol) && Objects.equals(number, that.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, number);
    }
}
