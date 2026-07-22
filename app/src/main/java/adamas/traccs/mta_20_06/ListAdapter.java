
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package adamas.traccs.mta_20_06;

/**** @author arshad*/


import android.content.Context;
import android.content.SharedPreferences;


import android.os.Environment;
import android.text.Html;


import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;

import android.widget.*;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;



import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class ListAdapter extends BaseAdapter
{
	private final Context context;
	private final Task[] values;
        String root;
        Boolean server_available;
         Task t=null;
         int selected_position=0;
         String OperatorId;
         String Security_Token;
         final String PREFS_NAME = "MTAPrefs";
         SharedPreferences settings=null;
        
	public ListAdapter(Context context, Task[] values,String root, Boolean server_available,String OperatorId,String Security_Token,SharedPreferences st) {
		this.context = context;
		this.values = values;                      
                this.root=root;
                this.server_available=server_available;
                this.OperatorId=OperatorId;
                this.Security_Token=Security_Token;
                this.settings=st;
               
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
	        	
            	gridView = inflater.inflate(R.layout.grid_item2, null);
	        }else{
	        	 gridView=(LinearLayout) convertView;
	        	
	        }
	        
            	
                    //  ll = new LinearLayout(context);
            	  textView =  (TextView) gridView.findViewById(R.id.txtCheckBoxDetail);     	
                   
            	  save =  (Button) gridView.findViewById(R.id.btnCheckbox);
                  item =  (CheckBox) gridView.findViewById(R.id.chkItem);

        item.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try{

                    settings.edit().putBoolean("Update", true).commit();
                }catch(Exception ex){}

                if( isChecked) {
                    save.setBackgroundResource(R.drawable.checkbox5);
                    save.setTag("1");
                    // checkbox is checked - doSomething()
                    int val=1;
                    // String  sql= "Update Current_task set Roster_TaskList=" + val +" where recordNo=" + save.getTag();
                    String  sql= "Update Roster_TaskList  set TaskComplete=" + val +" where recordNo=" + textView.getTag();
                    try{
                        Update_Roster_Node(textView.getTag().toString(),"TaskCOmplete","U");
                    }catch(Exception ex){}

                    try{

                        //	  Make_update(sql);
                        set_Updates(sql);
                    }catch(Exception ex){}
                    values[selected_position].setTaskCOmplete("U");
                } else {
                    // checkbox is unchecked
                    save.setBackgroundResource(R.drawable.checkbox6);
                    save.setTag("0");
                    int val=0;
                    // String  sql= "Update Current_task set Roster_TaskList=" + val +" where recordNo=" + save.getTag();
                    String  sql= "Update Roster_TaskList  set TaskComplete=" + val +" where recordNo=" + textView.getTag();
                    try{

                        Update_Roster_Node(textView.getTag().toString(),"TaskCOmplete","-");

                    }catch(Exception ex){}

                    try{
                        //Make_update(sql);
                        set_Updates(sql);
                    }catch(Exception ex){}
                    values[selected_position].setTaskCOmplete("F");
                }


            }
        });
            	//  save.setLayoutParams(new LinearLayout.LayoutParams(150,115,(float) 0.3));



            	  	//	textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    //       LinearLayout.LayoutParams.WRAP_CONTENT,
                    //       (float) 0.7));

            	  
            	  /*    save.setTextSize(12);   
                      save.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                      save.setGravity(Gravity.CENTER_VERTICAL);
                      save.setTextColor(Color.parseColor("#B0171F"));
                    //  save.setHeight(200); 
                    //  save.setWidth(200); 
                    //  CheckBox.resolveSize(25, 1);
                     // save.setVisibility(View.INVISIBLE);
                      
                       
                 */
                         t=values[position];
                         selected_position=position;
                      //  textView.setText(  t.getTaskDetail());
                         textView.setText(Html.fromHtml("<b>" + t.getTaskDetail() + "</font></b>"));
                         item.setText(t.getTaskDetail());

                       // save.setText( t.getTaskDetail());
                         textView.setTag(t.getRecordNo());
                        if  (t.getTaskCOmplete().equals("U")){
                          save.setBackgroundResource(R.drawable.checkbox5);
                          save.setTag("1");
                            item.setChecked(true);
                         // isChecked=true;
                        } else{
                        	 save.setBackgroundResource(R.drawable.checkbox6); 
                        	 save.setTag("0");
                            item.setChecked(false);
                        	// isChecked=false;
                        }
                   
                    save.setOnClickListener(new View.OnClickListener() {   
                    	  public void onClick(View v){

	                    	try{
	                    		  
	                    	      settings.edit().putBoolean("Update", true).commit();          		
	                  		}catch(Exception ex){}
	                    	
	                        if( save.getTag().toString().equals("0")) {
	                        	 save.setBackgroundResource(R.drawable.checkbox5); 
                            	 save.setTag("1");
	                            // checkbox is checked - doSomething()
	                          int val=1;
	                           // String  sql= "Update Current_task set Roster_TaskList=" + val +" where recordNo=" + save.getTag();
	                              String  sql= "Update Roster_TaskList  set TaskComplete=" + val +" where recordNo=" + textView.getTag();
	                              try{
	                                  Update_Roster_Node(textView.getTag().toString(),"TaskCOmplete","U");
	                                  }catch(Exception ex){}
	                              
	                              try{
	                             
	                            //	  Make_update(sql);
	                            	  set_Updates(sql);
	                              }catch(Exception ex){}
	                              values[selected_position].setTaskCOmplete("U");
	                        } else {
	                            // checkbox is unchecked
	                        	 save.setBackgroundResource(R.drawable.checkbox6);
                                 save.setTag("0");
	                          int val=0;
	                         // String  sql= "Update Current_task set Roster_TaskList=" + val +" where recordNo=" + save.getTag();
	                           String  sql= "Update Roster_TaskList  set TaskComplete=" + val +" where recordNo=" + textView.getTag();
	                           try{
	                           Update_Roster_Node(textView.getTag().toString(),"TaskCOmplete","-");
	                           }catch(Exception ex){}
	                          
	                           try{
	                        	   //Make_update(sql);
	                                set_Updates(sql);
	                               }catch(Exception ex){}
	                          values[selected_position].setTaskCOmplete("F");
	                        }
	                          
	                    }
                });


        textView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {


                    try{

                        settings.edit().putBoolean("Update", true).commit();
                    }catch(Exception ex){}

                    if( save.getTag().toString().equals("0")) {
                        save.setBackgroundResource(R.drawable.checkbox5);
                        save.setTag("1");
                        // checkbox is checked - doSomething()
                        int val=1;
                        // String  sql= "Update Current_task set Roster_TaskList=" + val +" where recordNo=" + save.getTag();
                        String  sql= "Update Roster_TaskList  set TaskComplete=" + val +" where recordNo=" + textView.getTag();
                        try{
                            Update_Roster_Node(textView.getTag().toString(),"TaskCOmplete","U");
                        }catch(Exception ex){}

                        try{
                            // Make_update(sql);
                            set_Updates(sql);
                        }catch(Exception ex){}
                        values[selected_position].setTaskCOmplete("U");
                    } else {
                        // checkbox is unchecked
                        save.setBackgroundResource(R.drawable.checkbox6);
                        save.setTag("0");
                        int val=0;
                        // String  sql= "Update Current_task set Roster_TaskList=" + val +" where recordNo=" + save.getTag();
                        String  sql= "Update Roster_TaskList  set TaskComplete=" + val +" where recordNo=" + textView.getTag();
                        try{
                            Update_Roster_Node(textView.getTag().toString(),"TaskCOmplete","-");
                        }catch(Exception ex){}

                        try{
                            //Make_update(sql);
                            set_Updates(sql);
                        }catch(Exception ex){}
                        values[selected_position].setTaskCOmplete("F");
                    }
                }

                return false;
            }
        });

/*
            textView.setOnTouchListener(new OnTouchListener() {

                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            if (event.getAction() == MotionEvent.ACTION_UP) {


    	                    	try{

    	                    	      settings.edit().putBoolean("Update", true).commit();
    	                  		}catch(Exception ex){}

    	                        if( save.getTag().toString().equals("0")) {
    	                        	 save.setBackgroundResource(R.drawable.checkbox5);
                                	 save.setTag("1");
    	                            // checkbox is checked - doSomething()
    	                          int val=1;
    	                           // String  sql= "Update Current_task set Roster_TaskList=" + val +" where recordNo=" + save.getTag();
    	                              String  sql= "Update Roster_TaskList  set TaskComplete=" + val +" where recordNo=" + textView.getTag();
    	                              try{
    	                                  Update_Roster_Node(textView.getTag().toString(),"TaskCOmplete","U");
    	                                  }catch(Exception ex){}

    	                              try{
    	                            	 // Make_update(sql);
    	                              set_Updates(sql);
    	                              }catch(Exception ex){}
    	                              values[selected_position].setTaskCOmplete("U");
    	                        } else {
    	                            // checkbox is unchecked
    	                        	 save.setBackgroundResource(R.drawable.checkbox6);
                                     save.setTag("0");
    	                          int val=0;
    	                         // String  sql= "Update Current_task set Roster_TaskList=" + val +" where recordNo=" + save.getTag();
    	                           String  sql= "Update Roster_TaskList  set TaskComplete=" + val +" where recordNo=" + textView.getTag();
    	                           try{
    	                           Update_Roster_Node(textView.getTag().toString(),"TaskCOmplete","-");
    	                           }catch(Exception ex){}

    	                           try{
    	                        	   //Make_update(sql);
    	                               set_Updates(sql);
    	                               }catch(Exception ex){}
    	                          values[selected_position].setTaskCOmplete("F");
    	                        }
                            }


                            return true;
                        }
                    });

*/

                    
                return gridView;
            
            
        
       }
	@Override
	public int getCount() {
		return values.length;
	}
 
	@Override
	public Object getItem(int position) {
		return null;
	}
 
	@Override
	public long getItemId(int position) {
		return 0;
	}
      
	  String getSecurityToken(){
		   
		   	String Val= this.OperatorId  + "$" + this.Security_Token + "$";
		   	return Val;
		   }
	  
  public void Make_update(String command  )
    {
        try
        {
            if (server_available==false){
                set_Updates(command);
                return;
            }
             String URL5 = root + "/TimeSheet.asmx?op=Make_update";
             String SOAP_ACTION5 =  "https://tempuri.org/Make_update";
             String METHOD_NAME5 = "Make_update";
             String NAMESPACE = "https://tempuri.org/";
      if ((!command.equals("")) && command!=null ){
            SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME5);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL5);
            androidHttpTransport.debug =true;
            
            PropertyInfo pi=new PropertyInfo(); 
            pi.setName("command"); 
          
            pi.setValue(command); 
            request.addProperty(pi);
            
            PropertyInfo pi2=new PropertyInfo(); 
            pi2.setName("Fontra");           
            pi2.setValue(getSecurityToken() + "99"); 
            request.addProperty(pi2);
            
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet=true;
            envelope.setOutputSoapObject(request);
      
                    // Make the soap call.
                    androidHttpTransport.call(SOAP_ACTION5, envelope);     

                    SoapPrimitive  result =(SoapPrimitive)envelope.getResponse();
        }
        }catch(Exception ex){
       
        }
        
    }
   public void set_Updates(String command ) throws IOException{
             File froot = null; 
      try{
            // check for SDcard   
  
      froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);  
      File filein=null;
      File fileDir=null;
       String state = Environment.getExternalStorageState();
    //  textMsg.setText("No Server found - " + state);
       if (Environment.MEDIA_MOUNTED.equals(state)) {
          
          //check sdcard permission 
           fileDir = new File(froot.getAbsolutePath()+"/.server/"); 

           filein= new File(fileDir, "Updates.txt");  
         
                       if (filein.exists())
                        {
                            try{
                               froot.setWritable(true);
                                //if (froot.canWrite()) {  
                              
                                File file = new File(fileDir, "Updates.txt");  
                                FileWriter filewriter = new FileWriter(file,true);  
                                BufferedWriter out = new BufferedWriter(filewriter);  
                                out.write("\n" + command);  
                                out.close();
                              
                            } catch (Exception e) {
                         
                             //Log.e("Exception","error occurred while creating xml file");
                            }
                        }else{
                            froot.setWritable(true);

                            //if (froot.canWrite()) {  
                        
                            if (!fileDir.exists())
                            fileDir.mkdirs();  

                            File file = new File(fileDir, "Updates.txt");  
                            FileWriter filewriter = new FileWriter(file,true);  
                            BufferedWriter out = new BufferedWriter(filewriter);  
                            out.write("\n" + command);  
                            out.close();
                        }
                 }                  
     } catch (Exception e) {
                      
             }

}
   public void Update_Roster_Node(String RecordNo, String node_name,String node_value)
   {
      try {
             File froot = null; 
             froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);  	
             String filePath = froot.getAbsolutePath()+"/.server/task.xml";
		//  String filePath = "D:\\traccs.xml";
             File xmlFile = new File(filePath);
             DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
             DocumentBuilder dBuilder;
  
             dBuilder = dbFactory.newDocumentBuilder();
             Document doc = dBuilder.parse(xmlFile);
             doc.getDocumentElement().normalize();
             
            NodeList rosters = doc.getElementsByTagName("Task");
            Element element = null;
            Boolean found=false;
            //loop for each employee
            for(int i=0; i<rosters.getLength();i++){
                element = (Element) rosters.item(i);
                String recordNo = element.getElementsByTagName("RecordNo").item(0).getFirstChild().getNodeValue();
                  if(recordNo.equals(RecordNo)){
                     found=true; 
                  }
             if(found){
                   Node Started = element.getElementsByTagName(node_name).item(0).getFirstChild();
                  Started.setNodeValue(node_value);  
                   break;
            }
    }
        
            //write the updated document to file or console
            doc.getDocumentElement().normalize();
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filePath));
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(source, result);
            //System.out.println("XML file updated successfully");
             
        } catch (SAXException sax){sax.printStackTrace();}
          catch( ParserConfigurationException pc){pc.printStackTrace();}
          catch(IOException iox){iox.printStackTrace();}
          catch(TransformerException e1) {
            e1.printStackTrace();
        }
   }


}