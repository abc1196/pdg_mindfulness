package mindfulness.pdg_mindfulness.dashboard;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mindfulness.pdg_mindfulness.R;
import mindfulness.pdg_mindfulness.utils.interfaces.DashboardNavigationHost;

/**
 * A simple {@link Fragment} subclass.
 */
public class HRVFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hrv, container, false);
        MaterialButton newPstButton = view.findViewById(R.id.new_pst_button);

        newPstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((DashboardNavigationHost) getActivity()).newPST(); // Navigate to the next Fragment
            }
        });
        return view;
    }
}
