package mindfulness.pdg_mindfulness.measurement.data;

public class Question {
    private String question;
    private int index;

    public Question(String question, int index) {
        this.question = question;
        this.index=index;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
