package se.mah.ag8987.passwordkeeper;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import se.mah.ag8987.passwordkeeper.Model.Application;

/**
 * Created by m_mol on 2017-10-18.
 */

public class PasswordAdapter extends RecyclerView.Adapter<PasswordAdapter.ConcertViewHolder> {
    private List<Application> applications;
    private ApplicationListener applicationListener;


    public PasswordAdapter(List<Application> applications, ApplicationListener applicationListener) {
        this.applications = applications;
        this.applicationListener = applicationListener;
    }

    @Override
    public ConcertViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_application, parent,false);
        v.setClickable(true);
        return new ConcertViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ConcertViewHolder holder, int position) {
        Application application = applications.get(position);
        holder.tv_date.setText(application.getName());
    }

    @Override
    public int getItemCount() {
        return applications.size();
    }

    class ConcertViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView tv_artist;
        private TextView tv_date;


        public ConcertViewHolder(View itemView) {
            super(itemView);
            tv_artist = (TextView)itemView.findViewById(R.id.tv_artist);
            tv_date = (TextView)itemView.findViewById(R.id.tv_date);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            applicationListener.onClick(applications.get(getAdapterPosition()));

        }
    }

    public interface ApplicationListener{
        void onClick(Application a);

    }
}
