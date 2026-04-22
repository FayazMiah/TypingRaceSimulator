public class Passage {
    private String text;

    public Passage(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public int getLength() {
        return text.length();
    }
}//