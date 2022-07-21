package regexp;

public class GrammarTreeException extends Exception{
    private final String text;
    private final Integer wrongSymbolPosition;

    public GrammarTreeException(String text, Integer wrongSymbolPosition){
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
