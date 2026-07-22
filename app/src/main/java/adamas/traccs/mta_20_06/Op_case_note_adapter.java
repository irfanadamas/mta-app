
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package adamas.traccs.mta_20_06;

/**** @author arshad*/


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;


import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;


import android.widget.*;


import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;


public class Op_case_note_adapter extends RecyclerView.Adapter<Op_case_note_adapter.MyViewHolder>
{
	private final Context context;
	private final List<OP_Case_Note> values;
        String root;
        Boolean server_available;
        OP_Case_Note t=null;
         int selected_position=0;
         String OperatorId;
         String Security_Token;
         final String PREFS_NAME = "MTAPrefs";
         SharedPreferences settings=null;
	View itemView;
	public Op_case_note_adapter( List<OP_Case_Note> values,Context context) {
		this.context = context;
		this.values = values;
               
               
	}
	public class MyViewHolder extends RecyclerView.ViewHolder  {
		 TextView txtDateTime ,txtCaseType;
		 TextView txtDetail ;
		 TextView txtOperator,txtCreatedBy;
		View card;
		Context context;

		public MyViewHolder(View view) {
			super(view);

			txtDateTime =  (TextView) view.findViewById(R.id.txtDateTime);
			txtOperator	  =  (TextView) view.findViewById(R.id.txtOperator);
			txtDetail =  (TextView) view.findViewById(R.id.txtDetail);
			txtCaseType =  (TextView) view.findViewById(R.id.txtCaseType);
			txtCreatedBy = (TextView) view.findViewById(R.id.txtCreatedBy);
			context = view.getContext();
			card= view.findViewById(R.id.cardView);

		}



	}

	@Override
	public Op_case_note_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_row_opnote, parent, false);
		return new Op_case_note_adapter.MyViewHolder(itemView);


	}



	@Override
	public void onBindViewHolder(Op_case_note_adapter.MyViewHolder holder, int position){
            LinearLayout ll;
            
            
            final TextView txtDateTime ,txtCaseType;
            final TextView txtDetail ;
            final TextView txtOperator,txtCreatedBy;
            final ImageView imgAdd;
            

	        
	  try {

		  t=values.get(position);
		  selected_position=position;

	         txtDateTime =   holder.txtDateTime;
	         txtOperator	  =  holder.txtOperator;
             txtDetail = holder.txtDetail;
		  	  txtCaseType =  holder.txtCaseType;
		    // txtCreatedBy = holder.txtCreatedBy;

                         txtDateTime.setText(t.getNote_Date());
                         txtOperator.setText(t.getCreator());
                         txtDetail.setText(t.getDetail() );
                         switch(t.getNoteType())
						 {
						 	case "OPNOTE":
								txtCaseType.setText("OP Note");
								break;
							case "CASENOTE":
								 txtCaseType.setText("Case Note");
								 break;
						    case "CLINNOTE":
								 txtCaseType.setText("Clinical Note");
								 break;
						  	case "SVCNOTE":
								 txtCaseType.setText("Service Note");
								 break;
						  	default:
								 txtCaseType.setText("");
							 break;
						 }


		  
	  	}catch(Exception ex){}         
                    

            
        
       }
	@Override
	public int getItemCount() {
		return values.size();
	}
 

	public void Set_Client_Note(View view) {

		Intent in= new Intent(view.getContext(),Add_Note_Activity.class);

		Bundle b= new Bundle();

		b.putString("RecordNo",t.getRecordNo());
		b.putString("Recipient",t.getRecipient());
		b.putString("PersonId", t.getPersonId());
		b.putString("AccountNo", t.getRecipient());
		b.putString("Roster_Date", t.getRoster_Date());
		b.putString("Main_Op_Note", t.getNoteType());
		b.putString("Job_Time", t.getJob_Time());
		b.putString("Enforce_Note", "true");

		in.putExtras(b);
		view.getContext().startActivity(in);


	}
}