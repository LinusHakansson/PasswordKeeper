package se.mah.ag8987.passwordkeeper.Controllers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import se.mah.ag8987.passwordkeeper.Fragment.PinCodeFragment;
import se.mah.ag8987.passwordkeeper.R;

/**
 * Created by LinusHakansson on 2017-11-19.
 */

public class LoginActivityController {
    private PinCodeFragment pinCodeFragment;
    private FragmentManager fm;
    private FragmentTransaction ft;

    private SharedPreferences sharedPreferences;


    public LoginActivityController(FragmentManager fm, Activity activity) {
        this.pinCodeFragment = new PinCodeFragment();
        this.fm = fm;
        sharedPreferences = activity.getSharedPreferences("MYPREFS", Context.MODE_PRIVATE);

    }

    public void changeFragment(){
        ft = fm.beginTransaction();
        ft.replace(R.id.fragmentContainer, pinCodeFragment, "pincodefragment");
//        ft.addToBackStack(null);
        ft.disallowAddToBackStack();

        ft.commit();
    }

    public void addSecondFragment(){
        FragmentTransaction ft = fm.beginTransaction();
        PinCodeFragment pinCodeFragment2 = new PinCodeFragment();
        ft.replace(R.id.fragmentContainer2,pinCodeFragment2);
        ft.commit();
    }


    private boolean savePin(String pinCode){
        boolean saved = false;
        if(sharedPreferences.contains("pin" + pinCode)){
            promptUser("Pin Already Exist");
        } else{
            promptUser("Pin Saved");
            saved = true;
        }
        return saved;
    }

    public boolean savePin(){
        if(savePin(pinCodeFragment.getPinNumber())){
            return true;
        }
        return false;
    }

    private boolean login(String pinCode){
        if(sharedPreferences.contains("pin" + pinCode)){
            return true;
        }
        return false;
    }

    public boolean login(){
        if(pinCodeFragment.getPinLength() == 4){
            if(login(pinCodeFragment.getPinNumber())){

                return true;
            }
        }
        promptUser("Pin too short");
        return false;
    }


    private void promptUser(String message){
        Toast.makeText(this.pinCodeFragment.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void removeFragment(){

        FragmentTransaction ft = fm.beginTransaction();
        ft.remove(pinCodeFragment);
        PinCodeFragment pinCodeFragment = new PinCodeFragment();
        ft.replace(R.id.fragmentContainer, pinCodeFragment, "pincodefragment");
        ft.commit();
    }
}
