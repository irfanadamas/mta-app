package adamas.traccs.mta_20_06;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;

import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import adamas.traccs.mta_20_06.holder.IconTreeItemHolder;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import atv.model.TreeNode;
import atv.view.AndroidTreeView;

public class Goal_Plan_Tree extends AppCompatActivity {
    private final String NAMESPACE = "https://tempuri.org/";
    String root="";
    String Recipient="";
    String OperatorId;
    String Security_Token;
    String PersonId="";
    File froot = null;
     String TAG="Goals";
     Context context;
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(false);
            try {
                String Title = actionBar.getTitle().toString();
                actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
                actionBar.setCustomView(getLayoutInflater().inflate(R.layout.action_bar_home, null),
                        new ActionBar.LayoutParams(
                                ActionBar.LayoutParams.MATCH_PARENT,
                                ActionBar.LayoutParams.MATCH_PARENT,
                                Gravity.CENTER
                        )
                );
                TextView textviewTitle = (TextView) actionBar.getCustomView().findViewById(R.id.actionbar_textview);
                textviewTitle.setGravity(Gravity.CENTER);
                textviewTitle.setText(Title);
                ImageView imageMenu=(ImageView) actionBar.getCustomView().findViewById(R.id.imageMenu);
                imageMenu.setVisibility(View.INVISIBLE);

                ImageView imageBack=(ImageView) actionBar.getCustomView().findViewById(R.id.imageBack);
                // imageMenu.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                imageBack.setVisibility(View.GONE);
                actionBar.setDisplayHomeAsUpEnabled(true);

                imageBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });

            } catch (Exception ex) {
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
       // if(1==1) return false;

        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        super.onOptionsItemSelected(item);

        switch(item.getItemId())
        {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    @Override
    public void onResume() {
        super.onResume();
    }
    @SuppressLint("GestureBackNavigation")
    @Override
    public void onBackPressed() {

        super.onBackPressed();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_file, menu);
        //if(1==1) return false;
        return false;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (menu != null) {
            menu.clear();
        }
        return false;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal__plan__tree);
        setupActionBar();
            //    listItems.put("Touch to View Goal Plans for " + Recipient, FolderStructureFragment.class);
        context=this.getApplicationContext();

            set_server_Ip();

            try {
                new MyAsyncClass3().execute();
            } catch (Exception ex) {
            }



    }



    void Fill_Tree(){

           try{
               //Root

               TreeNode root = TreeNode.root();
               //Parent
               MyHolder.IconTreeItem nodeItem = new MyHolder.IconTreeItem(R.drawable.ic_arrow_drop_down, "Goal Plans for " + Recipient);
               TreeNode parent = new TreeNode(nodeItem).setViewHolder(new MyHolder(getApplicationContext(), true, MyHolder.DEFAULT, MyHolder.DEFAULT));


               froot= (context.getExternalFilesDir(null));
              // froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
               String state = Environment.getExternalStorageState();
               File fileDir = null;
               fileDir = new File(froot.getAbsolutePath() + "/.server/");
               File fXmlFile = new File(fileDir, "plans.xml");
               String current_element = "";
               String current_element2 = "";
               String current_element3 = "";
               if (fXmlFile.exists()) {
                   DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                   DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                   Document doc = dBuilder.parse(fXmlFile);
                   doc.getDocumentElement().normalize();
                   NodeList nList = doc.getElementsByTagName("PlanGaols");
                   if (nList == null) return ;


                   for (int tmp = 0; tmp < nList.getLength(); tmp++) {
                       Node nNode = nList.item(tmp);
                       if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                           Element eElement = (Element) nNode;

                           try {
                               current_element = eElement.getElementsByTagName("Careplan_Name").item(0).getTextContent();

                           } catch (Exception ex) {
                           }

                           //Child
                           MyHolder.IconTreeItem childItem = new MyHolder.IconTreeItem(R.drawable.ic_menu_send, current_element);
                           TreeNode child = new TreeNode(childItem).setViewHolder(new MyHolder(getApplicationContext(), false, R.layout.child, 25));
                           NodeList nList2 = eElement.getElementsByTagName("Goal");
                           if (nList2 != null) {
                               for (int j = 0; j < nList2.getLength(); j++) {
                                   Node nNode2 = nList2.item(j);
                                   Element eElement2 = (Element) nNode2;
                                   current_element2 = eElement2.getElementsByTagName("GoalName").item(0).getTextContent();
                                   if (current_element2==null) continue;
                                   if (current_element2.equals("")) continue;

                                   //Sub Child
                                   MyHolder.IconTreeItem subChildItem = new MyHolder.IconTreeItem(R.drawable.ic_folder_open, current_element2);
                                   TreeNode subChild = new TreeNode(subChildItem).setViewHolder(new MyHolder(getApplicationContext(), false, R.layout.child, 75));

                                   //Adding Strategies
                                   NodeList nList3 = eElement2.getElementsByTagName("Strategy");
                                   if (nList3!=null){
                                       for (int k=0; k<nList3.getLength(); k++){
                                           Node nNode3 = nList3.item(k);
                                           Element eElement3 = (Element) nNode3;
                                           current_element3 = eElement3.getElementsByTagName("StrategyName").item(0).getTextContent();
                                           if (current_element3==null) continue;
                                           if (current_element3.equals("")) continue;
                                            //Sub Child
                                           MyHolder.IconTreeItem subChildItem2 = new MyHolder.IconTreeItem(R.drawable.gback, current_element3);
                                           TreeNode subChild2 = new TreeNode(subChildItem2).setViewHolder(new MyHolder(getApplicationContext(), false, R.layout.child, 125));
                                           //Adding Items
                                           NodeList nList4 = eElement3.getElementsByTagName("Item");
                                           if (nList4!=null) {
                                               for (int l = 0; l < nList4.getLength(); l++) {
                                                   Node nNode4 = nList4.item(l);
                                                   Element eElement4 = (Element) nNode4;
                                                   String current_element4 = eElement4.getElementsByTagName("ItemName").item(0).getTextContent();
                                                   if (current_element4 == null) continue;
                                                   if (current_element4.equals("")) continue;

                                                   //Sub Child
                                                   MyHolder.IconTreeItem subChildItem3 = new MyHolder.IconTreeItem(R.drawable.ic_next, current_element4);
                                                   TreeNode subChild3 = new TreeNode(subChildItem3).setViewHolder(new MyHolder(getApplicationContext(), false, R.layout.child, 150));
                                                   subChild2.addChild(subChild3);
                                               }
                                               subChild.addChildren(subChild2);
                                           }

                                       }
                                       child.addChildren(subChild);
                                   }
                                   //Add sub child.

                               }
                               //Add child.
                               if (child!=null)
                                   parent.addChildren(child);
                           }

                           root.addChild(parent);
                       } // Main for loop
                       //Add AndroidTreeView into view.
                     if (tmp==0) {
                         AndroidTreeView tView = new AndroidTreeView(getApplicationContext(), root);
                         ((LinearLayout) findViewById(R.id.ll_parent)).addView(tView.getView());
                     }
                       //tView.expandAll();
                   }
               } // If File Exists

        }catch(Exception ex){
               Toast.makeText(this, ex.toString(), Toast.LENGTH_SHORT).show();
           }
        }
    void set_server_Ip()
    {

        try{
            Bundle bundle = getIntent().getExtras();

            root=bundle.get("root").toString();

            Recipient=bundle.get("Recipient").toString();

            if (bundle.get("Security_Token")==null)
                Security_Token="";
            else
                Security_Token=bundle.get("Security_Token").toString();


            if (bundle.get("OperatorId")==null)
                OperatorId="";
            else
                OperatorId=bundle.get("OperatorId").toString();

            if (bundle.get("PersonId")==null)
                PersonId="";
            else
                PersonId=bundle.get("PersonId").toString();


            try{
                // this.EnableViewNoteCases= bundle.getString("EnableViewNoteCases");

            }catch(Exception ex){}
        }catch(Exception ex){}
    }
    class MyAsyncClass3 extends AsyncTask<Void, Void, Void> {

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Goal_Plan_Tree.this);
            pDialog.setMessage("Please wait while loading data from server  ....");
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... mApi) {
            try {
                load_plans();
            }

            catch (Exception ex) {
                // Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.cancel();
            Fill_Tree();

        }
    }
    String getSecurityToken(){

        String Val= OperatorId  + "$" + Security_Token + "$";
        return Val;
    }
    public void load_plans(){

        String URL = root + "/TimeSheet.asmx?op=getGoalPlans";
        String SOAP_ACTION =  "https://tempuri.org/getGoalPlans";
        String METHOD_NAME = "getGoalPlans";

        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.debug = true;


            PropertyInfo pi = new PropertyInfo();

            pi.setName("PersonId");

            pi.setValue(getSecurityToken() + PersonId);
            request.addProperty(pi);



            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);

            // Make the soap call.
            androidHttpTransport.call(SOAP_ACTION, envelope);

            String xml= androidHttpTransport.responseDump;
            File fileDir=null;
           // froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);

            fileDir = new File(froot.getAbsolutePath()+"/.server/");

            XmlSerializer serializer = Xml.newSerializer();
            File newxmlfile = new File(fileDir,"plans.xml");
            String state = Environment.getExternalStorageState();

            if (Environment.MEDIA_MOUNTED.equals(state)) {
                try{
                    newxmlfile.createNewFile();
                }catch(IOException e){

                }
                FileOutputStream fileos = null;
                try{
                    fileos = new FileOutputStream(newxmlfile );
                    fileos.write(xml.getBytes());
                    serializer.setOutput(fileos, "UTF-8");
                }catch (Exception ex){
                    Log.d(TAG, "load_plans: " + ex);
                }
                finally{   fileos.close();}
            }
            /*
            SoapObject result = (SoapObject) envelope.getResponse();
            SoapObject result2 =null;
            plans= new ArrayList<String>();
            String plan="";
            for (int j=0; j<result.getPropertyCount(); j++) {
              //  rst.setRecordNo(result2.getProperty("RecordNo").toString()); // roster No
                result2=(SoapObject)result.getProperty(j);
                plan=result2.getProperty("Careplan_Name").toString() + " Expires on " + result2.getProperty("End_Date").toString();
                plans.add(plan);

            }*/


        }catch(Exception ex){}
    }

}
