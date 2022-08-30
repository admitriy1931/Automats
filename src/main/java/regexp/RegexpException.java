package regexp;

public class RegexpException extends Exception{
    private final String text;
    private final Integer wrongSymbolPosition;

    public RegexpException(String text, Integer wrongSymbolPosition){
        this.text = text;
        this.wrongSymbolPosition = wrongSymbolPosition;
    }

    public String getText() {
        return text;
    }

    public Integer getWrongSymbolPosition() {
        return wrongSymbolPosition;
    }
}
