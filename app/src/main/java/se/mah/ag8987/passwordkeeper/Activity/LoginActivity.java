package se.mah.ag8987.passwordkeeper.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.facebook.stetho.DumperPluginsProvider;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.dumpapp.DumperPlugin;

import java.util.ArrayList;

import se.mah.ag8987.passwordkeeper.Controllers.LoginActivityController;
import se.mah.ag8987.passwordkeeper.Fragment.PinCodeFragment;
import se.mah.ag8987.passwordkeeper.R;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private Intent intent;
    private LoginActivityController loginActivityController;
    private FragmentManager fm;
    private Button btnLogin, btnCreate;
    private Context context = this;
    private SharedPreferences sharedPreferences;
    private boolean logged = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPreferences = getSharedPreferences("MYPREFS", MODE_PRIVATE);
        intent = new Intent(LoginActivity.this, MainActivity.class);
        initializeComponents();
        fm = getSupportFragmentManager();
        loginActivityController = new LoginActivityController(fm, LoginActivity.this);
        loginActivityController.changeFragment();

        Stetho.initialize(
                Stetho.newInitializerBuilder(context)
                        .enableDumpapp(new SampleDumperPluginsProvider(context)).enableWebKitInspector(Stetho.defaultInspectorModulesProvider(context))
                        .build()
        );
    }

    private void initializeComponents(){
        btnCreate = findViewById(R.id.btnCreate);
        btnLogin = findViewById(R.id.btnLogin);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                loginActivityController.addSecondFragment(); // Compare both pin when create


                    if(loginActivityController.savePin()){
                        startActivity(intent);
                        loginActivityController.removeFragment();
                        finish();
                    }

            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(loginActivityController.login()){
                    startActivity(intent);
                    loginActivityController.removeFragment();
                    finish();
                }
            }
        });
    }



    @Override
    public void onBackPressed() {
        finish();
    }

    private class SampleDumperPluginsProvider implements DumperPluginsProvider {
        private final Context mContext;

        public SampleDumperPluginsProvider(Context context) {
            this.mContext = context;
        }

        @Override
        public Iterable<DumperPlugin> get() {
            ArrayList<DumperPlugin> plugins = new ArrayList<>();
            for(DumperPlugin defaultPlugin : Stetho.defaultDumperPluginsProvider(mContext).get()){
                plugins.add(defaultPlugin);
            }
            return plugins;
        }

    }


}
