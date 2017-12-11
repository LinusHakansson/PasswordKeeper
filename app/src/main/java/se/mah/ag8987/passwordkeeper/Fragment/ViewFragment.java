package se.mah.ag8987.passwordkeeper.Fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import se.mah.ag8987.passwordkeeper.Model.Application;
import se.mah.ag8987.passwordkeeper.Controllers.MainActivityController;
import se.mah.ag8987.passwordkeeper.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewFragment extends android.support.v4.app.Fragment {
    private View view;
    private MainActivityController controller;

    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;

    private EditText input;

    private ListView lv;
    private Context context;

    private int position;
    private boolean updateClicked;

    private List<Application>  test1;

    public ViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_view, container, false);
        lv = view.findViewById(R.id.lv);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                alertDialogBuilder2(test1.get(position).getName(), test1.get(position).getPassword());
            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    ViewFragment.this.position = position;
                    alertDialogBuilder();
                return true;
            }

        });
        return view;
    }

    private void alertDialogBuilder(){
        updateClicked = false;
        alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setTitle("Are you sure?")
                .setMessage("Your saved password for this app will be deleted")
                .setCancelable(false)
                .setView(input)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(updateClicked){
                            String newPassword = input.getText().toString();
                            if(newPassword.length() != 0){
                                controller.update(position, newPassword);
                            } else {
                                Toast.makeText(context, "Password too short", Toast.LENGTH_SHORT).show();
                            }
                            input.setText("");
                            input.setVisibility(View.INVISIBLE);
                            ((ViewGroup) input.getParent()).removeView(input);

                        } else {
                            controller.delete(position);
                            ((ViewGroup) input.getParent()).removeView(input);
                            Snackbar.make(view, "Deleted", Snackbar.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        input.setText("");
                        input.setVisibility(View.INVISIBLE);
                        ((ViewGroup) input.getParent()).removeView(input);

                    }
                })
                .setNeutralButton("Update", null);

                 alertDialog = alertDialogBuilder.create();
                 alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                     @Override
                     public void onShow(final DialogInterface dialog) {
                        final Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEUTRAL);

                         button.setOnClickListener(new View.OnClickListener() {
                             @Override
                             public void onClick(View v) {
                                 ((AlertDialog) dialog).setMessage("Enter a new password for this application");
                                input.setVisibility(View.VISIBLE);
                                button.setVisibility(View.INVISIBLE);
                                updateClicked = true;
                             }
                         });
                     }
                 });
                 if(!alertDialog.isShowing()){
                     alertDialog.show();
                 }
    }

    private void alertDialogBuilder2(final String message, final String password){
        alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setTitle("Hit ''Show Password''")
                .setCancelable(false)
                .setMessage("Your password for " + message + " is \n " + getCryptedPass(password.length()))
                .setPositiveButton("OK", null)
                .setNeutralButton("Show Password", null);
        alertDialog = alertDialogBuilder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEUTRAL);
                button.setText("Show Password");
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((AlertDialog) dialog).setMessage("Your password for " + message + " is \n\n " + password);
                    }
                });

                Button buttonPos = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                buttonPos.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }

        });


        alertDialog.show();

    }

    public void showSnackBar(String message){
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }


   public void setController(MainActivityController controller){
        this.controller = controller;
   }

   public void setAdapter(Context context){
       test1 = controller.getApplications();
       String [] appNames = new String[test1.size()];
       for(int i = 0; i < test1.size(); i++){
           appNames [i] = test1.get(i).getName();
       }
       lv.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, appNames));
   }

   private String getCryptedPass(int length){
       String crypt = "";
       for(int i = 0; i < length; i++){
           crypt += "*";
       }
       return crypt;
   }

   public void setNewAdater(List<Application> apps){
       String [] appNames = new String[apps.size()];
       for(int i = 0; i < apps.size(); i++){
           appNames [i] = apps.get(i).getName();
       }
       lv.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, appNames));
   }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.context = getContext();
        input = new EditText(context);
        input.setVisibility(View.INVISIBLE);
        input.setTransformationMethod(PasswordTransformationMethod.getInstance());
        setAdapter(context);
    }
}
