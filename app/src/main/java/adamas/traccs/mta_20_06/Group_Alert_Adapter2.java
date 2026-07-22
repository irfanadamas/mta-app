
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package adamas.traccs.mta_20_06;

/**** @author arshad*/

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Environment;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.*;

import java.util.ArrayList;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

public class Group_Alert_Adapter2 extends BaseAdapter
{
    private final Context context;
    private final  ArrayList<GroupAlerts> values;
    private GroupAlerts grp=null;

    public Group_Alert_Adapter2(Context context,  ArrayList<GroupAlerts> values) {
        this.context = context;
        this.values = values;

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View gridView;
        TextView tv;

        final TextView textView;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        if (convertView == null) {

            gridView = inflater.inflate(R.layout.grid_alert_row2, null);
        }else{
            gridView=convertView;
        }

        textView=gridView.findViewById(R.id.txtdetail);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            textView.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);

        grp=values.get(position);
        String str="";

        textView.setText(Html.fromHtml("<b>" + grp.getGroup() + "</b>" +  "<br>" +
                "" + grp.getNotes() + "" + "<br />" ));

        // textView.setText(Html.fromHtml("" + grp.getGroup() + "\n" + grp.getNotes() ));

        textView.setTag( grp.getRecordNo());



        return gridView;



    }
    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }




}