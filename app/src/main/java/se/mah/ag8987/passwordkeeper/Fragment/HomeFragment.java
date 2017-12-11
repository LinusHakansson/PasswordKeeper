package se.mah.ag8987.passwordkeeper.Fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.TextView;

import se.mah.ag8987.passwordkeeper.Controllers.MainActivityController;
import se.mah.ag8987.passwordkeeper.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment  {
    private View view;
    private TextView tvNumber;
    private MainActivityController controller;


    private int size;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        tvNumber = view.findViewById(R.id.tvNumberOfSaved);
        size = controller.getListSize();
        setTvNumber(String.valueOf(size));
        return view;
    }

    public void setTvNumber(String number){
        tvNumber.setText(number);
    }

    public void setController(MainActivityController controller) {
        this.controller = controller;
    }
}
