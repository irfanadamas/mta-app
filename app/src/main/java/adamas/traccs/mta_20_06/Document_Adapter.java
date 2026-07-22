package adamas.traccs.mta_20_06;

import android.content.Context;
import android.provider.DocumentsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by arshad on 18/08/2017.
 */

public class Document_Adapter extends BaseAdapter {

    private final Context context;
    private final ArrayList<Document> values;

    public Document_Adapter(Context context, ArrayList<Document> Values)  {
        this.context = context;
        this.values = Values;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView tvDocumentName;
        TextView tvCreated;
        TextView tvCtegory;
        TextView tvStatus;

    Document doc;
    View gridView = null;

    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    try {
            if (convertView == null) {

                gridView = inflater.inflate(R.layout.grid_row_document, null);
            } else {
                gridView =  convertView;

            }

            tvDocumentName = (TextView) gridView.findViewById(R.id.txtDocumentName);
            tvCreated = (TextView) gridView.findViewById(R.id.txtCreated);
            tvCtegory = (TextView) gridView.findViewById(R.id.txtCategory);
            tvStatus = (TextView) gridView.findViewById(R.id.txtStatus);

            doc = this.values.get(position);

            tvDocumentName.setText(doc.getTitle());
            tvCreated.setText(doc.getCreated());
            tvStatus.setText("St  " + doc.getStatus());
            tvCtegory.setText(doc.getDocumentGroup());
        }catch (Exception ex){
            Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show();
        }
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
