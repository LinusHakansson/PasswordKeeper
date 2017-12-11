package se.mah.ag8987.passwordkeeper;

import android.support.design.widget.Snackbar;

import java.util.List;

import se.mah.ag8987.passwordkeeper.Fragment.ViewFragment;
import se.mah.ag8987.passwordkeeper.Model.Application;

/**
 * Created by LinusHakansson on 2017-11-24.
 */

public class UpdateUIRunnable implements Runnable{
    private List <Application> applist;
    private String snackbarMessage;
    private ViewFragment viewFragment;

    public UpdateUIRunnable(List<Application> applist, String snackbarMessage, ViewFragment viewFragment) {
        this.applist = applist;
        this.snackbarMessage = snackbarMessage;
        this.viewFragment = viewFragment;
    }

    @Override
    public void run() {
        viewFragment.setNewAdater(applist);
        Snackbar.make(viewFragment.getView(), snackbarMessage, Snackbar.LENGTH_SHORT).show();
    }
}
