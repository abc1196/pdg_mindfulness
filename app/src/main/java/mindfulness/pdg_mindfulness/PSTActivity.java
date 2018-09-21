package mindfulness.pdg_mindfulness;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class PSTActivity extends AppCompatActivity {
    private final static String PST_TEST="PST_Test";
    private final static String PST_ANSWER="PST_Answers";
    private ArrayList<Question> questions;
    private HashMap<String,Answer> answers;
    private int score;
    private int index;
    private MaterialButton answerButton1;
    private MaterialButton answerButton2;
    private MaterialButton answerButton3;
    private MaterialButton answerButton4;
    private MaterialButton answerButton5;
    private TextView textQuestion;
    private final String BUTTON1_ID="ZNBYbelS3pDqfCRL7WAD";
    private final String BUTTON2_ID="fJeQbU1phyuU2IKCUlKG";
    private final String BUTTON3_ID="wqtImht5qMeX22yiqcgY";
    private final String BUTTON4_ID="mGwy7blT08CdwP2qBs7n";
    private final String BUTTON5_ID="XNsZkMDMtScTvZFg7Ib6";




    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pst);
        index=0;
        score=0;
        questions=new ArrayList<>();
        answers=new HashMap<>();

        textQuestion=(TextView) findViewById(R.id.question_text);
        answerButton1 = (MaterialButton)findViewById(R.id.answer_button1);
        answerButton2 = (MaterialButton)findViewById(R.id.answer_button2);
        answerButton3 = (MaterialButton)findViewById(R.id.answer_button3);
        answerButton4 = (MaterialButton)findViewById(R.id.answer_button4);
        answerButton5 = (MaterialButton)findViewById(R.id.answer_button5);




        FirebaseFirestore db1 = FirebaseFirestore.getInstance();

        db1.collection(PST_TEST)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document2 : task.getResult()) {

                                String pQuestion = document2.get("question").toString();
                                int pNumber = Integer.parseInt(document2.get("number").toString());
                                Question newQuestion = new Question(pQuestion, pNumber);
                                questions.add( newQuestion);
                                Log.d("CONITAGQUESTION", document2.get("question").toString());
                                Log.d("CONITAGQobject", pQuestion+pNumber);
                                Log.d("questionssize", questions.size()+"");

                            }
                            textQuestion.setText(questions.get(index).getQuestion());
                            index++;
                            Log.d("CONITAGQUESTION", "QUESTIONS OK");
                            FirebaseFirestore db2 = FirebaseFirestore.getInstance();
                            db2.collection(PST_ANSWER)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                Log.d("CONITAGQUESTION", "INIT ANSWERS");
                                                Log.d("CONIOBJECT", task.getResult().size()+"");
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    String pAnswer = document.get("answer").toString();
                                                    int pValue = Integer.parseInt(document.get("value").toString());
                                                    Log.d("CONIOBJECT", pAnswer+pValue);
                                                    Answer newAnswer = new Answer(pAnswer, pValue);
                                                    answers.put(document.getId(), newAnswer);
                                                    Log.d("CONITAGOK", document.get("answer").toString());


                                                }

                                                answerButton1.setText(answers.get(BUTTON1_ID).getAnswer());
                                                answerButton2.setText(answers.get(BUTTON2_ID).getAnswer());
                                                answerButton3.setText(answers.get(BUTTON3_ID).getAnswer());
                                                answerButton4.setText(answers.get(BUTTON4_ID).getAnswer());
                                                answerButton5.setText(answers.get(BUTTON5_ID).getAnswer());


                                                setOnClickListeners();
                                                Log.d("CONITAGQUESTION", "ANSWERS OK");
                                            } else {
                                                Log.w("ERRORGETTINGFIREBASE", "Error getting answers.", task.getException());
                                            }
                                        }
                                    });



                        } else {
                            Log.w("ERRORGETTINGFIREBASE", "Error getting questions.", task.getException());
                        }
                    }
                });
}

    private void setOnClickListeners() {
        answerButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                score=score+4;
                upDateQuestion();
            }
        });
        answerButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                score=score+3;
                upDateQuestion();
            }
        });
        answerButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                score=score+2;
                upDateQuestion();
            }
        });

        answerButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                score=score+1;
                upDateQuestion();
            }
        });
        answerButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                score=score+0;
                upDateQuestion();
            }
        });
        answerButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                score=score+0;
                upDateQuestion();
            }
        });
    }

    private void upDateQuestion() {
        if(index<questions.size()) {
            textQuestion.setText(questions.get(index).getQuestion());
            index++;
        }else{
            textQuestion.setText("Tu puntaje es: "+score+"");
            answerButton1.setVisibility(View.GONE);
            answerButton2.setVisibility(View.GONE);
            answerButton3.setVisibility(View.GONE);
            answerButton4.setVisibility(View.GONE);
            answerButton5.setVisibility(View.GONE);

        }

    }
    private void upDateAnswers(){
        answerButton1.setText(answers.get(4).getAnswer());
        answerButton2.setText(answers.get(3).getAnswer());
        answerButton3.setText(answers.get(2).getAnswer());
        answerButton4.setText(answers.get(1).getAnswer());
        answerButton5.setText(answers.get(0).getAnswer());
    }


    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
