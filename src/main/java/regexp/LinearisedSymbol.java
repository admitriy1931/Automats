package regexp;

import java.util.Objects;

public class LinearisedSymbol {
    private Character symbol;
    private final Integer number;
    private String stringValue;

    public LinearisedSymbol(Character symbol, Integer number){
        this.symbol = symbol;
        this.number = number;
    }

    public LinearisedSymbol(String value, Integer number){
        this.stringValue = value;
        this.number = number;
    }

    public Character getSymbol(){
        return symbol;
    }

    public String getStringValue() {
        return stringValue;
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
