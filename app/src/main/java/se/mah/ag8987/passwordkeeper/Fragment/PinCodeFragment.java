package se.mah.ag8987.passwordkeeper.Fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chaos.view.PinView;

import se.mah.ag8987.passwordkeeper.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class PinCodeFragment extends android.support.v4.app.Fragment {
    private static final String TAG = "PinCodeFragment";
    private View view;
    private PinView pinView;
    private String pinNumber = "";
    private int pin = 0;


    public PinCodeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_pin, container, false);
        initializeComponents();
        return view;
    }

    private void initializeComponents(){
        pinView = (PinView) view.findViewById(R.id.pinView);
    }

    public int getPinLength(){
        pinNumber = pinView.getText().toString();
        pin = pinNumber.length();
        return pin;
    }



    public String getPinNumber(){
        pinView.clearAnimation();
        return pinNumber;
    }

    @Override
    public void onPause() {
        pinNumber = "";
        pin = 0;
        super.onPause();
    }
}
