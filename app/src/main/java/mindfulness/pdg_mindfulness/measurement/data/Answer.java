package mindfulness.pdg_mindfulness.measurement.data;

public class Answer {
    private String answer;
    private int value;

    public Answer(String answer, int value) {
        this.answer = answer;
        this.value = value;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }


}
