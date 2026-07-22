package adamas.traccs.mta_20_06;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class Group_Alert_Adapter_Recyler extends RecyclerView.Adapter<Group_Alert_Adapter_Recyler.MyViewHolder>  {
    Context context;
    View itemView;
    private final ArrayList<GroupAlerts> values;
    public Group_Alert_Adapter_Recyler(Context context,  ArrayList<GroupAlerts> values) {
        this.context = context;
        this.values = values;

    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtdetail;
        Context context;

        public MyViewHolder(View view) {
            super(view);

            this.txtdetail = (TextView) view.findViewById(R.id.txtdetail);
            context = view.getContext();

        }
    }

        @Override
        public Group_Alert_Adapter_Recyler.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_alert_row, parent, false);
            return new MyViewHolder(itemView);

        }


    @Override
    public void onBindViewHolder(@NonNull Group_Alert_Adapter_Recyler.MyViewHolder holder, int position) {
        GroupAlerts grp = values.get(position);

        holder.txtdetail.setText(Html.fromHtml("<b>" + grp.getGroup() + "</b>" +  "<br />" +
                "" + grp.getNotes() + "" + "<br />" ));

    }
    @Override
    public int getItemCount() {
        return values.size();
    }



}
