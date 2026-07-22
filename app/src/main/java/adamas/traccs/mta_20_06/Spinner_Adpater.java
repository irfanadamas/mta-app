
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package adamas.traccs.mta_20_06;

/**** @author arshad*/

        import android.content.Context;
        import android.content.SharedPreferences;

        import android.view.LayoutInflater;
        import android.view.MotionEvent;
        import android.view.View;
        import android.view.ViewGroup;
        import android.view.View.OnTouchListener;

        import android.view.WindowManager;
        import android.widget.*;
        import java.util.List;

public class Spinner_Adpater extends BaseAdapter
{
    private final Context context;
    private final List<String> lst_values;
    String root;
    Boolean server_available;
    Task t=null;
    int selected_position=0;
    String OperatorId;
    String Security_Token;
    final String PREFS_NAME = "MTAPrefs";
    SharedPreferences settings=null;

    public Spinner_Adpater(Context context, List<String>  values) {
        this.context = context;
        this.lst_values = values;
        this.root=root;


    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout ll;
        TextView tv;
        final Button save;
        final TextView textView;
        final CheckBox item;
        final boolean isChecked=false;

        View gridView=null;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        if (convertView == null) {

            gridView = inflater.inflate(R.layout.spinner_item, null);
        }else{
            gridView=(LinearLayout) convertView;

        }


        //  ll = new LinearLayout(context);
        textView =  (TextView) gridView.findViewById(R.id.txtspinner_item);
        String val= lst_values.get(position);
        textView.setText(val);

        return gridView;



    }
    @Override
    public int getCount() {
        return lst_values.size();
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