package adamas.traccs.mta_20_06;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AddNewwAttendeesCellAdapter extends BaseAdapter {

    ArrayList<ProgrameRecipientDC> arrProgrameRecipients;
    private final LayoutInflater inflater;

    public AddNewwAttendeesCellAdapter(Context context, ArrayList<ProgrameRecipientDC> array) {
        arrProgrameRecipients = array;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return arrProgrameRecipients.size();
    }

    @Override
    public Object getItem(int i) {
        return arrProgrameRecipients.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;

        ProgrameRecipientDC obj = arrProgrameRecipients.get(i);
        final String current = obj.getClientName() + "\n" + obj.getAddress();

        if (view == null) {
            view = inflater.inflate(R.layout.customcell_recipientlist, viewGroup, false);

            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) view.findViewById(R.id.item_name);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.name.setText(current);

        return view;
    }

    private class ViewHolder {
        TextView name;
    }
}
