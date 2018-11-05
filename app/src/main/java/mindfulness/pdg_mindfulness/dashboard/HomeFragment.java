package mindfulness.pdg_mindfulness.dashboard;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mindfulness.pdg_mindfulness.R;
import mindfulness.pdg_mindfulness.utils.interfaces.DashboardNavigationHost;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment  {
    ;private MaterialButton goTreatment;
    public HomeFragment() {}
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

}
