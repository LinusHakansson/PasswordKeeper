package se.mah.ag8987.passwordkeeper.Controllers;

import android.support.design.widget.Snackbar;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.List;

import se.mah.ag8987.passwordkeeper.AesCbcWithIntegrity;
import se.mah.ag8987.passwordkeeper.Model.Application;
import se.mah.ag8987.passwordkeeper.DatabaseHandler;
import se.mah.ag8987.passwordkeeper.Fragment.AddFragment;
import se.mah.ag8987.passwordkeeper.Fragment.HomeFragment;
import se.mah.ag8987.passwordkeeper.Fragment.ViewFragment;
import se.mah.ag8987.passwordkeeper.Activity.MainActivity;
import se.mah.ag8987.passwordkeeper.R;
import se.mah.ag8987.passwordkeeper.UpdateUIRunnable;

import static se.mah.ag8987.passwordkeeper.AesCbcWithIntegrity.keys;


/**
 * Created by LinusHakansson on 2017-11-19.
 */

public class MainActivityController {
    private static final String TAG = "MainActivityController";
    private DatabaseHandler db;
    private List<Application> appList;

    public static final int  ADD_FRAGMENT = 0;
    public static final int  View_FRAGMENT = 1;
    public static final int HOME_FRAGMENT = 2;

    private ViewFragment viewFragment;
    private AddFragment addFragment;
    private HomeFragment homeFragment;
    private android.support.v4.app.FragmentManager fm;
    private MainActivity mainActivity;




    public MainActivityController(android.support.v4.app.FragmentManager fragmentManager, MainActivity mainActivity) {
//        this.addFragment = new AddFragment();
        this.viewFragment = new ViewFragment();
        this.homeFragment = new HomeFragment();

        this.mainActivity = mainActivity;
        db = new DatabaseHandler(this.mainActivity);
        getApplications();
        fm = fragmentManager;

    }

    public void changeFragment(int fragment){
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        if(fragment == ADD_FRAGMENT){
            if(addFragment == null){
                addFragment = new AddFragment();
            } else {
                addFragment  = (AddFragment)fm.findFragmentByTag("addFragment");
            }
            ft.addToBackStack(null);
            ft.replace(R.id.fragmentContainer, addFragment, "addFragment");
            addFragment.setController(this);
        } else if(fragment == View_FRAGMENT){
            ft.addToBackStack(null);
            ft.replace(R.id.fragmentContainer, viewFragment, "viewFragment");
            viewFragment.setController(this);
        } else if(fragment == HOME_FRAGMENT){
            ft.addToBackStack(null);
            ft.replace(R.id.fragmentContainer, homeFragment, "homeFragment");
            homeFragment.setController(this);
        }
        ft.commit();
    }

    public int getListSize(){
        return appList.size();
    }


    public List<Application> getApplications(){
        appList = new ArrayList<>();
        appList = db.getApplications();
        new Thread(new Runnable() {
            @Override
            public void run() {
                AesCbcWithIntegrity.SecretKeys key;

                for (Application a : appList){
                    String correctPassword = decryptPassword(a);
                    a.setPassword(correctPassword);
                }

            }
        }).start();

        return appList;
    }

    public void addApplication(final Application a){

        new Thread(new Runnable() {
            @Override
            public void run() {
                a.setPassword(encryptPassword(a));

                if(db.addPassword(a)){
                    Log.d(TAG, "addApplication: ADDED ");
                } else {
                    Log.d(TAG, "addApplication: Funkade ej");
                }
            }
        }).start();
    }

    public void delete(final int pos){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(db.deletePassword(appList.get(pos))){
                    appList.remove(pos);
                    mainActivity.runOnUiThread(new UpdateAdapter(appList));
                }
            }
        }).start();
    }

    public void update(final int pos, final String newPassword){
        final Application a;
        a = appList.get(pos);
        new Thread(new Runnable() {
            String snackbarMessage = "";
            @Override
            public void run() {
                a.setPassword(newPassword);
                a.setPassword(encryptPassword(a));
                if(db.updatePassword(a)){
                    a.setPassword(decryptPassword(a));
                    snackbarMessage = "Password updated for " + a.getName();
                } else {
                    snackbarMessage = "Update failed";
                }
                mainActivity.runOnUiThread(new UpdateUIRunnable(appList, snackbarMessage, viewFragment));
            }
        }).start();
    }

    private String decryptPassword(Application a){
        AesCbcWithIntegrity.SecretKeys key;
        String correctPassword = "";
        AesCbcWithIntegrity.CipherTextIvMac cipherTextIvMac = new AesCbcWithIntegrity.CipherTextIvMac(a.getPassword());
        try {
            key = keys(a.getKey());
            correctPassword = AesCbcWithIntegrity.decryptString(cipherTextIvMac, key);

        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return correctPassword;
    }

    private String encryptPassword(Application a){
        AesCbcWithIntegrity.SecretKeys keys;
        String encryptedPassword = "";
        try {
            keys = AesCbcWithIntegrity.generateKey();
            a.setKey(keys.toString());
            AesCbcWithIntegrity.CipherTextIvMac cipherTextIvMac = AesCbcWithIntegrity.encrypt(a.getPassword(), keys);
            encryptedPassword = cipherTextIvMac.toString();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encryptedPassword;
    }

    private class UpdateAdapter implements Runnable{
        List<Application> applicationList;

        public UpdateAdapter(List<Application> applicationList) {
            this.applicationList = applicationList;
        }

        @Override
        public void run() {
            viewFragment.setNewAdater(applicationList);
        }
    }
}
