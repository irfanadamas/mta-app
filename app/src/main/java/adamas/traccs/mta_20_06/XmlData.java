/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package adamas.traccs.mta_20_06;

import android.content.Context;
import android.os.Environment;
import android.text.format.Time;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author arshad
 */
public class XmlData {
    static Context context;
    XmlData(Context cntx){
        context=cntx;
    }
     public void Update_Roster_Node(String RecordNo, String node_name,String node_value)
   {
      try {
             File froot = null;

             froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);  	
             String filePath = froot.getAbsolutePath()+"/.server/traccs.xml";
		//  String filePath = "D:\\traccs.xml";
             File xmlFile = new File(filePath);
             DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
             DocumentBuilder dBuilder;
  
             dBuilder = dbFactory.newDocumentBuilder();
             Document doc = dBuilder.parse(xmlFile);
             doc.getDocumentElement().normalize();
             
            NodeList rosters = doc.getElementsByTagName("Roster_Info");
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

    public void updateAllRosters()
    {
        try {
            File froot = null;

            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            String filePath = froot.getAbsolutePath()+"/.server/traccs.xml";
            //  String filePath = "D:\\traccs.xml";
            File xmlFile = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder;

            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            NodeList rosters = doc.getElementsByTagName("Roster_Info");
            Element element = null;
            Boolean found=false;
            //loop for each employee
            for(int i=0; i<rosters.getLength();i++){
                element = (Element) rosters.item(i);


                Node Started = element.getElementsByTagName("NewAcceptDate").item(0).getFirstChild();
                Started.setNodeValue("10/08/2025");

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


    public void set_Job_Completed_Status(String RecordNo)
    {
        try {
            File froot = null;
            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            String filePath = froot.getAbsolutePath()+"/.server/traccs.xml";
            //  String filePath = "D:\\traccs.xml";
            File xmlFile = new File(filePath);
            DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
            fac.setNamespaceAware(false);
            fac.setValidating(false);
            fac.setFeature("http://xml.org/sax/features/namespaces", false);
            fac.setFeature("http://xml.org/sax/features/validation", false);
          //  fac.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
            //fac.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            DocumentBuilder dBuilder = fac.newDocumentBuilder();
           // DocumentBuilder dBuilder;

            //dBuilder = dbFactory.newDocumentBuilder();

            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            NodeList rosters = doc.getElementsByTagName("Roster_Info");
            Element element = null;
            Boolean found=false;
            String curr_time = "";
            try {

                curr_time = getCurrentTime();
            } catch (Exception ex) {
            }
            //loop for each employee
            for(int i=0; i<rosters.getLength();i++){
                element = (Element) rosters.item(i);
                String recordNo = element.getElementsByTagName("RecordNo").item(0).getFirstChild().getNodeValue();
                if(recordNo.equals(RecordNo)){
                    found=true;
                }
                if(found){
                    Node Started = element.getElementsByTagName("Started").item(0).getFirstChild();
                    Started.setNodeValue("1");
                    Node Completed = element.getElementsByTagName("Completed").item(0).getFirstChild();
                    Completed.setNodeValue(RecordNo);
                    Node actual_end = element.getElementsByTagName("Actual_End").item(0).getFirstChild();
                    actual_end.setNodeValue(curr_time);
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
    public String get_Roster_Node(String RecordNo, String node_name)
   {
       String node_value="";
      try {
             File froot = null; 
             froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);  	
             String filePath = froot.getAbsolutePath()+"/.server/traccs.xml";
		//  String filePath = "D:\\traccs.xml";
             File xmlFile = new File(filePath);
             DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
             DocumentBuilder dBuilder;
  
             dBuilder = dbFactory.newDocumentBuilder();
             Document doc = dBuilder.parse(xmlFile);
             doc.getDocumentElement().normalize();
             
            NodeList rosters = doc.getElementsByTagName("Roster_Info");
            Element element = null;
            Boolean found=false;
            //loop for each employee
            for(int i=0; i<rosters.getLength();i++){
                element = (Element) rosters.item(i);
                String recordNo = element.getElementsByTagName("RecordNo").item(0).getFirstChild().getNodeValue();
                  if(recordNo.equalsIgnoreCase(RecordNo)){
                     found=true; 
                  }
             if(found){
                   Node node = element.getElementsByTagName(node_name).item(0).getFirstChild();
                 node_value= node.getNodeValue(); 
                  break;
            }
    }
      
       } catch (SAXException sax){sax.printStackTrace();}
          catch( ParserConfigurationException pc){pc.printStackTrace();}
          catch(IOException iox){iox.printStackTrace();}
         
      return node_value;
   }  
   public long get_Job_Started(String RecordNo,String Roster_Date)
   {
       String node_value="";
       String node_value_roster_date="";
       long Return_value=0;
      // String roster_date="";
              
       
      try {
             File froot = null; 
             froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);  	
             String filePath = froot.getAbsolutePath()+"/.server/traccs.xml";
		//  String filePath = "D:\\traccs.xml";
             File xmlFile = new File(filePath);
             DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
             DocumentBuilder dBuilder;
  
             dBuilder = dbFactory.newDocumentBuilder();
             Document doc = dBuilder.parse(xmlFile);
             doc.getDocumentElement().normalize();
             
            NodeList rosters = doc.getElementsByTagName("Roster_Info");
            Element element = null;
          
            DateFormat dateFormat2 = new SimpleDateFormat("yyyy/MM/dd");
            Date date21 = new Date(Roster_Date);
            Roster_Date=dateFormat2.format(date21);    
            
            for(int i=0; i<rosters.getLength();i++){
                element = (Element) rosters.item(i);
                node_value = element.getElementsByTagName("RecordNo").item(0).getFirstChild().getNodeValue();
                node_value_roster_date = element.getElementsByTagName("Roster_Date").item(0).getFirstChild().getNodeValue();
                
             //   Toast.makeText(this, node_value +" " + node_value_roster_date , Toast.LENGTH_LONG).show();
                
             
                Date date2 = new Date(node_value_roster_date);
                node_value_roster_date=dateFormat2.format(date2);     
               
                String started ="0" ;
                String completed ="0";
              
                try{  
                	started= element.getElementsByTagName("Started").item(0).getFirstChild().getNodeValue();
                    completed = element.getElementsByTagName("Completed").item(0).getFirstChild().getNodeValue();
                }catch(Exception ex){}
                
              //if ( node_value_roster_date.equals(Roster_Date))
                if ( Long.parseLong(node_value) == Long.parseLong(RecordNo))
                {
	               	                
	                if( Integer.parseInt(started)>0 && Integer.parseInt(completed)==0 ){
	                      Return_value=1;
	                      break;
	                }else if( Integer.parseInt(completed)>0){
	                     Return_value=Integer.parseInt(node_value);
	                     break; 
	                }  
	                
	             }else  if(Date.parse(node_value_roster_date)== Date.parse(Roster_Date )  && 
	            		 Integer.parseInt(started)>0  && 
	            		 Integer.parseInt(completed)==0){
	                    Return_value=Integer.parseInt(node_value);                      
	                    break;
	             }else{
	                    Return_value=0;
	              }
	          
           }
       
       } catch (SAXException sax){sax.printStackTrace();Return_value=-1;}
          catch( ParserConfigurationException pc){pc.printStackTrace();Return_value=-2;}
          catch(IOException iox){iox.printStackTrace();Return_value=-3;}
      	catch(Exception ex){Return_value=-4;}
         
      return Return_value;
   }

    public long get_Job_Started_only(String RecordNo)
    {
        String node_value="";
        String node_value_roster_date="";
        long Return_value=0;
        // String roster_date="";


        try {
            File froot = null;
            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            String filePath = froot.getAbsolutePath()+"/.server/traccs.xml";
            //  String filePath = "D:\\traccs.xml";
            File xmlFile = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder;

            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            NodeList rosters = doc.getElementsByTagName("Roster_Info");
            Element element = null;


            for(int i=0; i<rosters.getLength();i++){
                element = (Element) rosters.item(i);
                node_value = element.getElementsByTagName("RecordNo").item(0).getFirstChild().getNodeValue();

                String started ="0" ;
                String completed ="0";

                try{
                    started= element.getElementsByTagName("Started").item(0).getFirstChild().getNodeValue();
                    completed = element.getElementsByTagName("Completed").item(0).getFirstChild().getNodeValue();
                }catch(Exception ex){}

                //if ( node_value_roster_date.equals(Roster_Date))
                if ( Long.parseLong(node_value) == Long.parseLong(RecordNo))
                {

                    if( Integer.parseInt(started)>0 && Integer.parseInt(completed)==0 ){
                        Return_value=1;
                        break;
                    }else if(Integer.parseInt(started)==0  && Integer.parseInt(completed)>0){
                        Return_value=Integer.parseInt(node_value);
                        break;

                    }else if(Integer.parseInt(completed)>0){
                        Return_value=Integer.parseInt(node_value);
                        break;
                    }

                }else{
                    Return_value=0;
                }

            }

        } catch (SAXException sax){sax.printStackTrace();Return_value=-1;}
        catch( ParserConfigurationException pc){pc.printStackTrace();Return_value=-2;}
        catch(IOException iox){iox.printStackTrace();Return_value=-3;}
        catch(Exception ex){Return_value=-4;}

        return Return_value;
    }
    public void add_Roster_Element(String recordNo) {
        try {
             File froot = null; 
             froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);  	
             String filePath = froot.getAbsolutePath()+"/.server/traccs.xml";
		//  String filePath = "D:\\traccs.xml";
             File xmlFile = new File(filePath);
             DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
             DocumentBuilder dBuilder;
  
             dBuilder = dbFactory.newDocumentBuilder();
             Document doc = dBuilder.parse(xmlFile);
             doc.getDocumentElement().normalize();
         
                NodeList rosters = doc.getElementsByTagName("Roster_Info");
                Element element = null;

                //loop for each employee
                for(int i=0; i<rosters.getLength();i++){
                    element = (Element) rosters.item(i);
                  String RecordNo = element.getElementsByTagName("RecordNo").item(0).getFirstChild().getNodeValue();
                      if(RecordNo.equalsIgnoreCase(recordNo)){
                            Element newElement = doc.createElement("Completed");
                            newElement.appendChild(doc.createTextNode("1"));
                            element.appendChild(newElement);
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
    public  void remove_Roster_Element(String recordNo) {
        try {
             File froot = null; 
             froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);  	
             String filePath = froot.getAbsolutePath()+"/.server/traccs.xml";
		//  String filePath = "D:\\traccs.xml";
             File xmlFile = new File(filePath);
             DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
             DocumentBuilder dBuilder;
  
             dBuilder = dbFactory.newDocumentBuilder();
             Document doc = dBuilder.parse(xmlFile);
             doc.getDocumentElement().normalize();
         
                NodeList rosters = doc.getElementsByTagName("Roster_Info");
                Element element = null;

                //loop for each employee
                for(int i=0; i<rosters.getLength();i++){
                    element = (Element) rosters.item(i);
                  String RecordNo = element.getElementsByTagName("RecordNo").item(0).getFirstChild().getNodeValue();
                      if(RecordNo.equalsIgnoreCase(recordNo)){
                            Node completed = element.getElementsByTagName("Completed").item(0);
                            element.removeChild(completed);
                          
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
         } catch (SAXException sax){sax.printStackTrace();}
          catch( ParserConfigurationException pc){pc.printStackTrace();}
          catch(IOException iox){iox.printStackTrace();}
          catch(TransformerException e1) {
            e1.printStackTrace();
        }
    }
    public static void updateStartedValue(Document doc,String recordNo) {
        NodeList rosters = doc.getElementsByTagName("Roster_Info");
        Element element = null;
        Boolean found=false;
        //loop for each employee
        for(int i=0; i<rosters.getLength();i++){
            element = (Element) rosters.item(i);
            String RecordNo = element.getElementsByTagName("RecordNo").item(0).getFirstChild().getNodeValue();
              if(RecordNo.equalsIgnoreCase(recordNo)){
                 found=true; 
              }
         if(found){
               Node Started = element.getElementsByTagName("Started").item(0).getFirstChild();
              Started.setNodeValue("1");  
              break;
        }
    }
  }
   
    public static void updateCompletedValue(Document doc,String recordNo) {
          NodeList rosters = doc.getElementsByTagName("Roster_Info");
          Element element = null;
          Boolean found=false;
          //loop for each employee
          for(int i=0; i<rosters.getLength();i++){
              element = (Element) rosters.item(i);
              String RecordNo = element.getElementsByTagName("RecordNo").item(0).getFirstChild().getNodeValue();
                if(RecordNo.equalsIgnoreCase(recordNo)){
                   found=true; 
                }
           if(found){
                 Node Started = element.getElementsByTagName("Started").item(0).getFirstChild();
                Started.setNodeValue("0");
                Node Completed = element.getElementsByTagName("Completed").item(0).getFirstChild();
                Completed.setNodeValue(recordNo);
                break;
          }
      }
    }
      private static void addElement(Document doc) {
        NodeList rosters = doc.getElementsByTagName("Roster_Info");
        Element element = null;
         
        //loop for each employee
        for(int i=0; i<rosters.getLength();i++){
            element = (Element) rosters.item(i);
            Element salaryElement = doc.createElement("salary");
            salaryElement.appendChild(doc.createTextNode("10000"));
            element.appendChild(salaryElement);
        }
    }
 
    private static void deleteElement(Document doc) {
        NodeList rosters = doc.getElementsByTagName("Roster_Info");
        Element element = null;
        //loop for each employee
        for(int i=0; i<rosters.getLength();i++){
            element = (Element) rosters.item(i);
            Node genderNode = element.getElementsByTagName("gender").item(0);
            element.removeChild(genderNode);
        }
         
    }
 
    private static void updateElementValue(Document doc,String recordNo) {
        NodeList rosters = doc.getElementsByTagName("Roster_Info");
        Element element = null;
        Boolean found=false;
        //loop for each employee
        for(int i=0; i<rosters.getLength();i++){
            element = (Element) rosters.item(i);
            String RecordNo = element.getElementsByTagName("RecordNo").item(0).getFirstChild().getNodeValue();
              if(RecordNo.equalsIgnoreCase(recordNo)){
                 found=true; 
              }
         if(found){
               Node Started = element.getElementsByTagName("Started").item(0).getFirstChild();
              Started.setNodeValue("0");             
        }
    }
  }
    private static void updateAttributeValue(Document doc) {
        NodeList rosters = doc.getElementsByTagName("Roster_Info");
        Element element = null;
        //loop for each employee
        for(int i=0; i<rosters.getLength();i++){
            element = (Element) rosters.item(i);
            String RecordNo = element.getElementsByTagName("RecordNo").item(0).getFirstChild().getNodeValue();
            if(RecordNo.equalsIgnoreCase("34324")){
                //prefix id attribute with M
                element.setAttribute("id", "M"+element.getAttribute("id"));
            }else{
                //prefix id attribute with F
                element.setAttribute("id", "F"+element.getAttribute("id"));
            }
        }
    }

    public Roster_Info Get_MultipleGroup_Last_Shift(String RecordNo)
    {
        String node_value="Single";
        String Client_Code="";
        String Client_Code_found="";
        String roster_date="";
        String roster_date_found="";
        String start_Time="";
        String duration="";
        Roster_Info last_shift=new Roster_Info();

        try {
            File froot = null;
            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            String filePath = froot.getAbsolutePath()+"/.server/traccs.xml";
            //  String filePath = "D:\\traccs.xml";
            File xmlFile = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder;

            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            NodeList rosters = doc.getElementsByTagName("Roster_Info");
            Element element = null;
            Boolean found=false;

            //loop for each employee
            for(int i=0; i<rosters.getLength();i++){
                element = (Element) rosters.item(i);
                String recordNo = element.getElementsByTagName("RecordNo").item(0).getFirstChild().getNodeValue();

                if(recordNo.equalsIgnoreCase(RecordNo)){

                    Client_Code_found=element.getElementsByTagName("Actual_Client_Code").item(0).getFirstChild().getNodeValue();
                    roster_date_found=element.getElementsByTagName("Roster_Date").item(0).getFirstChild().getNodeValue();
                    // time= new Time(element.getElementsByTagName("Start_Time").item(0).getFirstChild().getNodeValue());

                    found=true;
                }

                if(found & !recordNo.equalsIgnoreCase(RecordNo)){


                    element = (Element) rosters.item(i);
                    Client_Code=element.getElementsByTagName("Actual_Client_Code").item(0).getFirstChild().getNodeValue();
                    roster_date=element.getElementsByTagName("Roster_Date").item(0).getFirstChild().getNodeValue();
                    start_Time=element.getElementsByTagName("Start_Time").item(0).getFirstChild().getNodeValue();
                    duration=element.getElementsByTagName("Duration").item(0).getFirstChild().getNodeValue();
                    SimpleDateFormat tFormat = new SimpleDateFormat("HH:mm");

                    Date date = new Date();
                    // String strDate = dateFormat.format(date);
                    String Curr_Time = tFormat.format(date);

                    Date time = tFormat.parse(Curr_Time);
                    Date time2 = tFormat.parse(start_Time);

                    //time.after(time2)

                    if( Client_Code_found.equalsIgnoreCase(Client_Code) && roster_date_found.equals(roster_date)){

                        node_value= "Multiple";
                        last_shift.setRecordNo(recordNo);
                        last_shift.setStart_Time(start_Time);
                        last_shift.setRoster_Date(roster_date);
                        last_shift.setActual_Client_Code(Client_Code);
                        last_shift.setDuration(duration);


                    }
                }
            }

        } catch (SAXException sax){sax.printStackTrace();}
        catch( ParserConfigurationException pc){pc.printStackTrace();}
        catch(IOException iox){iox.printStackTrace();}
        catch(Exception iox){iox.printStackTrace();}
        finally{

        }
        return last_shift;
    }

    public String check_MultipleShift(String RecordNo, String current_Time)
    {
        String node_value="Single";
        String Client_Code="";
        String Client_Code_found="";
        String roster_date="";
        String roster_date_found="";
        String start_Time="";
        
       try {
              File froot = null; 
              froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);  	
              String filePath = froot.getAbsolutePath()+"/.server/traccs.xml";
 		//  String filePath = "D:\\traccs.xml";
              File xmlFile = new File(filePath);
              DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
              DocumentBuilder dBuilder;
   
              dBuilder = dbFactory.newDocumentBuilder();
              Document doc = dBuilder.parse(xmlFile);
              doc.getDocumentElement().normalize();
              
             NodeList rosters = doc.getElementsByTagName("Roster_Info");
             Element element = null;
             Boolean found=false;

             //loop for each employee
             for(int i=0; i<rosters.getLength();i++){
                 element = (Element) rosters.item(i);
                 String recordNo = element.getElementsByTagName("RecordNo").item(0).getFirstChild().getNodeValue();

                   if(recordNo.equalsIgnoreCase(RecordNo)){    

                       Client_Code_found=element.getElementsByTagName("Actual_Client_Code").item(0).getFirstChild().getNodeValue();
                       roster_date_found=element.getElementsByTagName("Roster_Date").item(0).getFirstChild().getNodeValue();
                       // time= new Time(element.getElementsByTagName("Start_Time").item(0).getFirstChild().getNodeValue());

                       found=true;
                   }

                 if(found & !recordNo.equalsIgnoreCase(RecordNo)){

	                   element = (Element) rosters.item(i);
	                   Client_Code=element.getElementsByTagName("Actual_Client_Code").item(0).getFirstChild().getNodeValue();
	                   roster_date=element.getElementsByTagName("Roster_Date").item(0).getFirstChild().getNodeValue();
	                   start_Time=element.getElementsByTagName("Start_Time").item(0).getFirstChild().getNodeValue();

                     SimpleDateFormat tFormat = new SimpleDateFormat("HH:mm");

                     Date date = new Date();
                     // String strDate = dateFormat.format(date);
                     String Curr_Time ="";
                     if (current_Time.equals("") || current_Time==null)
                       Curr_Time = tFormat.format(date);
                     else
                         Curr_Time =current_Time;

                     Date time = tFormat.parse(Curr_Time);
                     Date time2 = tFormat.parse(start_Time);




                     if(time.after(time2) && Client_Code_found.equalsIgnoreCase(Client_Code) && roster_date_found.equals(roster_date)){
                		   
                			   	node_value= "Multiple"; 
                			   	break;
                		   }
                	   }
                   }
     
        } catch (SAXException sax){sax.printStackTrace();}
           catch( ParserConfigurationException pc){pc.printStackTrace();}
           catch(IOException iox){iox.printStackTrace();}
           catch(Exception iox){iox.printStackTrace();}
          finally{
        	  
          }
       return node_value;
    }
    public static boolean check_TravelClaim(String End_Time, String Roster_Date, String Recipient)
    {

        String start_time_found="";
        String roster_date_found="";
         String start_Time_found="";
        String Client_Code_found="";
        String Roster_Type="";
        Boolean found=false;
        try {
            File froot = null;
            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            String filePath = froot.getAbsolutePath()+"/.server/traccs.xml";
            //  String filePath = "D:\\traccs.xml";
            File xmlFile = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder;

            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            NodeList rosters = doc.getElementsByTagName("Roster_Info");
            Element element = null;


            //loop for each employee
            for(int i=0; i<rosters.getLength();i++){
                element = (Element) rosters.item(i);
                start_time_found=element.getElementsByTagName("Start_Time").item(0).getFirstChild().getNodeValue();
                roster_date_found=element.getElementsByTagName("Roster_Date").item(0).getFirstChild().getNodeValue();
                Client_Code_found=element.getElementsByTagName("Actual_Client_Code").item(0).getFirstChild().getNodeValue();
                Roster_Type=element.getElementsByTagName("Roster_Type").item(0).getFirstChild().getNodeValue();

                if(start_time_found.equalsIgnoreCase(End_Time) && roster_date_found.equalsIgnoreCase(Roster_Date)
                && (Client_Code_found.equalsIgnoreCase("!INTERNAL") || Client_Code_found.equalsIgnoreCase(Recipient)) && Roster_Type.equalsIgnoreCase("9")
                ){

                    // time= new Time(element.getElementsByTagName("Start_Time").item(0).getFirstChild().getNodeValue());

                    found=true;
                    break;
                }



            }

        } catch (SAXException sax){sax.printStackTrace();}
        catch( ParserConfigurationException pc){pc.printStackTrace();}
        catch(IOException iox){iox.printStackTrace();}
        catch(Exception iox){iox.printStackTrace();}
        finally{

        }
        return found;
    }
    private String getCurrentTime() {

        final Calendar c = Calendar.getInstance();
        int currentTime = (c.get(Calendar.HOUR_OF_DAY) * 10000) +
                (c.get(Calendar.MINUTE) * 100) +
                (c.get(Calendar.SECOND));
        Log.w("TIME:", String.valueOf(currentTime));
       // return (String.valueOf(currentTime));
        return (c.get(Calendar.HOUR_OF_DAY)<10 ? "0" + c.get(Calendar.HOUR_OF_DAY) : c.get(Calendar.HOUR_OF_DAY)) + ":" +
                (c.get(Calendar.MINUTE)<10?"0"+c.get(Calendar.MINUTE) :c.get(Calendar.MINUTE));


    }
    private String get_proper_time(int t_val){
        if (t_val<=0)
            return t_val + "00";
        else if (t_val<10)
            return "0"+ t_val ;
        else
            return "" + t_val;
    }
    public String get_End_Time(String Start_Time,String Duration){
        int val=Integer.parseInt(Duration);
        String[] strStart = Start_Time.split(":") ;
        String str="";
        if ((val*5)%60+ Integer.parseInt(strStart[1])==60)
            str=((val*5)/60 + Integer.parseInt( strStart[0]))+1 + ":00" ;
        else if (((val*5)%60+ Integer.parseInt(strStart[1]))>60)
            str=get_proper_time(((val*5)/60 + Integer.parseInt( strStart[0])) +1) + ":" + get_proper_time(Math.abs((60-((val*5)%60+ Integer.parseInt(strStart[1])))));
        else{
            int hr=Integer.parseInt( strStart[0]);
            str=get_proper_time(((val*5)/60 + hr)) + ":" + ((((val*5)%60==0) && (Integer.parseInt(strStart[1])==0))? "00" : get_proper_time(((val*5)%60+ Integer.parseInt(strStart[1]))));
            //str=((val*5)/60 + Integer.parseInt( strStart[0])) + ":" +  ((val*5)%60+ Integer.parseInt(strStart[1]));
        }
        str=str.replace("000", "00");
        return str ;
    }


    public String Process_MultipleShift(String RecordNo, String Client_Code_found, String roster_date_found, String Shift_time, String EndTime, String current_time)
    {
        String node_value="Single";
        String Client_Code="";

        String roster_date="";
        String duration="";
        String start_Time="";
        String Break_shift="";
        String Prev_End_Time=EndTime;
        String End_Time="";

        try {
            File froot = null;
            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            String filePath = froot.getAbsolutePath()+"/.server/traccs.xml";
            //  String filePath = "D:\\traccs.xml";
            File xmlFile = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder;

            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            NodeList rosters = doc.getElementsByTagName("Roster_Info");
            Element element = null;
            Boolean found=false;

            //loop for each employee
            for(int i=0; i<rosters.getLength();i++){
                element = (Element) rosters.item(i);
                String recordNo = element.getElementsByTagName("RecordNo").item(0).getFirstChild().getNodeValue();
                Client_Code=element.getElementsByTagName("Actual_Client_Code").item(0).getFirstChild().getNodeValue();
                roster_date=element.getElementsByTagName("Roster_Date").item(0).getFirstChild().getNodeValue();
                start_Time=element.getElementsByTagName("Start_Time").item(0).getFirstChild().getNodeValue();
                duration=element.getElementsByTagName("Duration").item(0).getFirstChild().getNodeValue();
                Break_shift=element.getElementsByTagName("MinorGroup").item(0).getFirstChild().getNodeValue();

                if (Prev_End_Time.equalsIgnoreCase("")){
                    Prev_End_Time=Shift_time;
                }

               if (Client_Code_found.equalsIgnoreCase(Client_Code) && roster_date_found.equals(roster_date) && !recordNo.equalsIgnoreCase(RecordNo)){

                      SimpleDateFormat tFormat = new SimpleDateFormat("HH:mm");

                        Date date = new Date();
                      // String strDate = dateFormat.format(date);
                        String Curr_Time = tFormat.format(date);
                       if (current_time.equals("")|| current_time==null)
                           Curr_Time = tFormat.format(date);
                       else
                           Curr_Time=current_time;

                        Date time ;
                        time=tFormat.parse(Curr_Time);

                        Date time2 = tFormat.parse(start_Time);


                        Date Shift_time2 = tFormat.parse(Shift_time);
                         End_Time=get_End_Time(start_Time,duration);
                         Date time_end = tFormat.parse(End_Time);



                     String result =time2.after(time) + ", " + time.after(time2);



                     //|| Prev_End_Time.equalsIgnoreCase("")

                    if(time.after(time2) && time2.after(Shift_time2) && (Prev_End_Time.equalsIgnoreCase(start_Time) )){

                        Prev_End_Time=End_Time;

                        if (Break_shift.equalsIgnoreCase("BREAK")) continue;
                        Node Started = element.getElementsByTagName("Started").item(0).getFirstChild();
                        Started.setNodeValue("0");
                       // if  (time.after(time_end) || time.equals(time_end) ) {
                            Node Completed = element.getElementsByTagName("Completed").item(0).getFirstChild();
                            Completed.setNodeValue(recordNo);
                        //}
                    }
                }
            }

            doc.getDocumentElement().normalize();
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filePath));
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(source, result);


        } catch (SAXException sax){sax.printStackTrace();}
        catch( ParserConfigurationException pc){pc.printStackTrace();}
        catch(IOException iox){iox.printStackTrace();}
        catch(Exception iox){iox.printStackTrace();}
        finally{

        }
        return node_value;
    }
}
