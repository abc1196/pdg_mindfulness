package mindfulness.pdg_mindfulness.measurement;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import mindfulness.pdg_mindfulness.measurement.data.Answer;
import mindfulness.pdg_mindfulness.utils.others.BaseFragment;
import mindfulness.pdg_mindfulness.measurement.data.Question;
import mindfulness.pdg_mindfulness.R;
import mindfulness.pdg_mindfulness.utils.interfaces.ScoringTest;


public class PSTFragment extends BaseFragment {

    private OnFragmentInteractionListener mListener;
    private ArrayList<Question> questions;
    private ArrayList<Answer> answers;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param
     * @return A new instance of fragment example.

    // TODO: Rename and change types and number of parameters
    public static PSTFragment newInstance(ArrayList pquestions, ArrayList panswers) {
        PSTFragment fragment = new PSTFragment();
        questions=pquestions;
        answers=panswers;
        return fragment;
    }
*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_pst, container, false);

        TextView questionTest = view.findViewById(R.id.question_text);

        final MaterialButton answerButton1 = view.findViewById(R.id.answer_button1);
        final   MaterialButton answerButton2 = view.findViewById(R.id.answer_button2);
        final  MaterialButton answerButton3 = view.findViewById(R.id.answer_button3);
        final  MaterialButton answerButton4 = view.findViewById(R.id.answer_button4);


        answerButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ScoringTest) getActivity()).addScore(answerButton1.getText().toString());
                ((ScoringTest) getActivity()).addScore(answerButton1.getText().toString());
            }
        });

        answerButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ScoringTest) getActivity()).addScore(answerButton2.getText().toString());
                ((ScoringTest) getActivity()).addScore(answerButton2.getText().toString());
            }
        });
        answerButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ScoringTest) getActivity()).addScore(answerButton3.getText().toString());
                ((ScoringTest) getActivity()).addScore(answerButton3.getText().toString());
            }
        });
        answerButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ScoringTest) getActivity()).addScore(answerButton4.getText().toString());
                ((ScoringTest) getActivity()).addScore(answerButton4.getText().toString());
            }
        });



        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
