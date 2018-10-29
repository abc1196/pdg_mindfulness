package mindfulness.pdg_mindfulness.dashboard;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import mindfulness.pdg_mindfulness.R;
import mindfulness.pdg_mindfulness.measurement.PSTActivity;

public class WelcomeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private ViewPager slideViewPager;
    private LinearLayout dotsLayout;

    private TextView[] mDots;

    private  SliderAdapter sliderAdapter;

    private Button buttonPrevious;
    private Button buttonNext;

    private int currentPage;

    @Override
    public void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        db= FirebaseFirestore.getInstance();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        buttonPrevious=(Button)findViewById(R.id.button_previous);
        buttonNext=(Button)findViewById(R.id.button_next);
        slideViewPager=(ViewPager) findViewById(R.id.slide_view_pager);
        dotsLayout=(LinearLayout)findViewById(R.id.dots_layout);
        sliderAdapter= new SliderAdapter(this);
        slideViewPager.setAdapter(sliderAdapter);
        addDotsIndicator(0);
        slideViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                addDotsIndicator(i);
                currentPage=i;

                if(i==0){
                    buttonNext.setEnabled(true);
                    buttonPrevious.setEnabled(false);
                    buttonPrevious.setVisibility(View.INVISIBLE);

                    buttonNext.setText("SIGUIENTE");
                    buttonPrevious.setText("");
                }else if(i==mDots.length-1){
                    buttonNext.setEnabled(true);
                    buttonPrevious.setEnabled(true);
                    buttonPrevious.setVisibility(View.VISIBLE);

                    buttonNext.setText("INICIAR");
                    buttonPrevious.setText("ANTERIOR");

                }else{
                    buttonNext.setEnabled(true);
                    buttonPrevious.setEnabled(true);
                    buttonPrevious.setVisibility(View.VISIBLE);

                    buttonNext.setText("SIGUIENTE");
                    buttonPrevious.setText("ANTERIOR");
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        buttonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideViewPager.setCurrentItem(currentPage-1);
            }
        });


        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentPage==mDots.length-1){
                    FirebaseUser user=mAuth.getCurrentUser();
                    if(user!=null) {
                        db.collection("users").document(user.getUid()).update("isFirstLogin",false)
                                .addOnSuccessListener(new OnSuccessListener< Void >() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Intent intent = new Intent(getApplicationContext(), PSTActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });

                    }
                }else {
                    slideViewPager.setCurrentItem(currentPage +1);
                }
            }
        });
    }

    public void addDotsIndicator(int position){
        mDots= new TextView[3];
        dotsLayout.removeAllViews();
        for(int i=0; i<mDots.length;i++){
            mDots[i]= new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.colorTransparentWhite));
            dotsLayout.addView(mDots[i]);
        }
        if(mDots.length>0){
            mDots[position].setTextColor(getResources().getColor(R.color.colorWhite));
        }
    }
}
