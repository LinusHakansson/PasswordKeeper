package se.mah.ag8987.passwordkeeper.Fragment;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import se.mah.ag8987.passwordkeeper.Model.Application;
import se.mah.ag8987.passwordkeeper.Controllers.MainActivityController;
import se.mah.ag8987.passwordkeeper.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddFragment extends android.support.v4.app.Fragment{
    private static final String TAG = "AddFragment";
    private View view;
    private TextView tvApplicationName, tvPassword;
    private EditText etApplication, etPassword;
    private Button btnSave;

    private boolean tvAppAnimated = false;
    private boolean tvPasswAnimated = false;

    private float [] appStart = new float[2];
    private float [] passStart = new float[2];


    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    private MainActivityController controller;


    public AddFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add, container, false);
        initializeComponents();
        return view;
    }



    private void initializeComponents() {
        tvApplicationName = view.findViewById(R.id.tvApplicationName);
        tvPassword = view.findViewById(R.id.tvPassword);
        btnSave = view.findViewById(R.id.btnSavePass);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.show();
            }
        });
        etApplication = view.findViewById(R.id.etAppName);
        etApplication.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus && !tvAppAnimated){
                    animateTextView(tvApplicationName, 80f);
                    tvAppAnimated = true;
                }
            }
        });
        etPassword = view.findViewById(R.id.etPassword);
        etPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus && !tvPasswAnimated){
                    animateTextView(tvPassword, 80f);
                    tvPasswAnimated = true;
                }
            }
        });

            alertDialogBuilder = new AlertDialog.Builder(getContext());
            alertDialogBuilder.setTitle("Are you sure?")
                    .setMessage("Click yes to save")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                           controller.addApplication(new Application(etApplication.getText().toString(), etPassword.getText().toString()));
                            clearViews();
                            Snackbar.make(view, "Password saved", Snackbar.LENGTH_LONG).show();
                        }
                    })
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

            alertDialog = alertDialogBuilder.create();
    }

    private void animateTextView(View view, float y2){
        float y = view.getY();
        float x = view.getX();
        float newY = y - y2;
        float newX = etApplication.getX();

        ObjectAnimator ymover = ObjectAnimator.ofFloat(view, "y", y, newY );
        ObjectAnimator xmover = ObjectAnimator.ofFloat(view, "x", x, newX);
        AnimatorSet set = new AnimatorSet();
        ymover.setDuration(1000);
        xmover.setDuration(1000);
        set.playTogether(ymover, xmover);
        set.start();
    }

    private void clearViews(){
        etApplication.setText("");
        etPassword.setText("");
    }

    public void setController(MainActivityController controller){
        this.controller = controller;
    }


    @Override
    public void onPause() {
        appStart [0] = tvApplicationName.getX();
        passStart [0] = tvPassword.getX();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        if(tvAppAnimated && tvApplicationName.getX() != appStart[0]){
            tvAppAnimated = false;
        }

        if (tvPasswAnimated && tvPassword.getX() != passStart[0]){
            tvPasswAnimated = false;
        }

        Log.d(TAG, "onResume: " + tvAppAnimated);
        Log.d(TAG, "onResume: " + tvPasswAnimated);
    }
}
