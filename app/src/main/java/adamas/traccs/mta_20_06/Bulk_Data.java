package adamas.traccs.mta_20_06;

import android.content.Context;
import android.os.Environment;
import android.util.Xml;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.w3c.dom.*;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import timesheet.EncryptData;

/**
 * Created by arshad on 12/04/2018.
 */

public class Bulk_Data {
    public String root = "https://58.162.142.150/timesheet"; //https://10.0.2.2:49884
    private final String NAMESPACE = "https://tempuri.org/";
    private String StaffCode = "";
    private String OperatorId = "";
    private String Security_Token = "";
    private boolean server_available = false;
    private File froot = null;
    private String rosterDate = "";
    Context context;
    public Bulk_Data(String root, String StaffCode, String OperatorId, String Security_Token, boolean server_available, String rosterDate, Context context) {
        this.StaffCode = StaffCode;
        this.root = root;
        this.OperatorId = OperatorId;
        this.Security_Token = Security_Token;
        this.server_available = server_available;
        this.rosterDate = rosterDate;
        this.context=context;
    }

    String getSecurityToken() {

        String Val = OperatorId + "$" + Security_Token + "$";
        return Val;
    }

    public void load_Transport_Detail() {

        String URL6 = root + "/TimeSheet.asmx?op=getTransport_Detail";
        String SOAP_ACTION6 = "https://tempuri.org/getTransport_Detail";
        String METHOD_NAME6 = "getTransport_Detail";


        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME6);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL6);
            androidHttpTransport.debug = true;

            PropertyInfo pi1 = new PropertyInfo();
            pi1.setName("Carer_Code");
            pi1.setValue(getSecurityToken() + StaffCode);
            request.addProperty(pi1);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            // Make the soap call.
            androidHttpTransport.call(SOAP_ACTION6, envelope);

            String xml = androidHttpTransport.responseDump;
            // txtServer.setText(androidHttpTransport.requestDump);

            File fileDir = null;
            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            fileDir = new File(froot.getAbsolutePath() + "/.server/");

            XmlSerializer serializer = Xml.newSerializer();
            File newxmlfile = new File(fileDir, "Transport.xml");
            String state = Environment.getExternalStorageState();

            if (Environment.MEDIA_MOUNTED.equals(state)) {
                try {
                    newxmlfile.createNewFile();
                } catch (IOException e) {

                }
                FileOutputStream fileos = null;
                try {
                    fileos = new FileOutputStream(newxmlfile);
                    fileos.write(xml.getBytes());
                    serializer.setOutput(fileos, "UTF-8");
                } catch (Exception ex) {
                } finally {
                    fileos.close();
                }
            }

        } catch (Exception e) {
            //Toast.makeText(getApplicationContext(),e.toString() , Toast.LENGTH_LONG).show();
        }
    }
    public void load_Service_Wise_Shift_Goals() {

        String URL6 = root + "/TimeSheet.asmx?op=get_Service_wise_Shift_goals";
        String SOAP_ACTION6 = "https://tempuri.org/get_Service_wise_Shift_goals";
        String METHOD_NAME6 = "get_Service_wise_Shift_goals";


        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME6);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL6);
            androidHttpTransport.debug = true;

            PropertyInfo pi2 = new PropertyInfo();
            pi2.setName("CarerCode");
            pi2.setValue(getSecurityToken() + StaffCode);
            request.addProperty(pi2);

            PropertyInfo pi3 = new PropertyInfo();
            pi3.setName("MobileFutureLimit");
            pi3.setValue("10");
            request.addProperty(pi3);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            // Make the soap call.
            androidHttpTransport.call(SOAP_ACTION6, envelope);

            String xml = androidHttpTransport.responseDump;
            // txtServer.setText(androidHttpTransport.requestDump);

            File fileDir = null;
            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            fileDir = new File(froot.getAbsolutePath() + "/.server/");

            XmlSerializer serializer = Xml.newSerializer();
            File newxmlfile = new File(fileDir, "Service_ShiftGoals.xml");
            String state = Environment.getExternalStorageState();

            if (Environment.MEDIA_MOUNTED.equals(state)) {
                try {
                    newxmlfile.createNewFile();
                } catch (IOException e) {

                }
                FileOutputStream fileos = null;
                try {
                    fileos = new FileOutputStream(newxmlfile);
                    fileos.write(xml.getBytes());
                    serializer.setOutput(fileos, "UTF-8");
                } catch (Exception ex) {
                } finally {
                    fileos.close();
                }
            }

        } catch (Exception e) {
            //Toast.makeText(getApplicationContext(),e.toString() , Toast.LENGTH_LONG).show();
        }
    }
    public void load_Shift_Goals() {

        String URL6 = root + "/TimeSheet.asmx?op=getShiftGoals";
        String SOAP_ACTION6 = "https://tempuri.org/getShiftGoals";
        String METHOD_NAME6 = "getShiftGoals";


        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME6);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL6);
            androidHttpTransport.debug = true;

            PropertyInfo pi2 = new PropertyInfo();
            pi2.setName("Fontra");
            pi2.setValue(getSecurityToken() + "99");
            request.addProperty(pi2);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            // Make the soap call.
            androidHttpTransport.call(SOAP_ACTION6, envelope);

            String xml = androidHttpTransport.responseDump;
            // txtServer.setText(androidHttpTransport.requestDump);

            File fileDir = null;
            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            fileDir = new File(froot.getAbsolutePath() + "/.server/");

            XmlSerializer serializer = Xml.newSerializer();
            File newxmlfile = new File(fileDir, "ShiftGoals.xml");
            String state = Environment.getExternalStorageState();

            if (Environment.MEDIA_MOUNTED.equals(state)) {
                try {
                    newxmlfile.createNewFile();
                } catch (IOException e) {

                }
                FileOutputStream fileos = null;
                try {
                    fileos = new FileOutputStream(newxmlfile);
                    fileos.write(xml.getBytes());
                    serializer.setOutput(fileos, "UTF-8");
                } catch (Exception ex) {
                } finally {
                    fileos.close();
                }
            }

        } catch (Exception e) {
            //Toast.makeText(getApplicationContext(),e.toString() , Toast.LENGTH_LONG).show();
        }
    }

    public void get_Recipient_ShiftGoals(String PersonId) {

        String URL6 = root + "/TimeSheet.asmx?op=get_Recipient_Goals";
        String SOAP_ACTION6 = "https://tempuri.org/get_Recipient_Goals";
        String METHOD_NAME6 = "get_Recipient_Goals";


        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME6);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL6);
            androidHttpTransport.debug = true;

            PropertyInfo pi2 = new PropertyInfo();
            pi2.setName("PersonID");
            pi2.setValue(getSecurityToken() + PersonId);
            request.addProperty(pi2);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            // Make the soap call.
            androidHttpTransport.call(SOAP_ACTION6, envelope);

            String xml = androidHttpTransport.responseDump;
            // txtServer.setText(androidHttpTransport.requestDump);

            File fileDir = null;
            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            fileDir = new File(froot.getAbsolutePath() + "/.server/");

            XmlSerializer serializer = Xml.newSerializer();
            File newxmlfile = new File(fileDir, "Recipient_Goals.xml");
            String state = Environment.getExternalStorageState();

            if (Environment.MEDIA_MOUNTED.equals(state)) {
                try {
                    newxmlfile.createNewFile();
                } catch (IOException e) {

                }
                FileOutputStream fileos = null;
                try {
                    fileos = new FileOutputStream(newxmlfile);
                    fileos.write(xml.getBytes());
                    serializer.setOutput(fileos, "UTF-8");
                } catch (Exception ex) {
                } finally {
                    fileos.close();
                }
            }

        } catch (Exception e) {
            //Toast.makeText(getApplicationContext(),e.toString() , Toast.LENGTH_LONG).show();
        }

        get_Recipient_Goals_Strategies(PersonId);
    }
    public void get_Recipient_Goals_Strategies(String PersonId) {

        String URL6 = root + "/TimeSheet.asmx?op=get_Recipient_Goals_Strategies";
        String SOAP_ACTION6 = "https://tempuri.org/get_Recipient_Goals_Strategies";
        String METHOD_NAME6 = "get_Recipient_Goals_Strategies";


        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME6);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL6);
            androidHttpTransport.debug = true;

            PropertyInfo pi2 = new PropertyInfo();
            pi2.setName("PersonID");
            pi2.setValue(getSecurityToken() + PersonId);
            request.addProperty(pi2);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            // Make the soap call.
            androidHttpTransport.call(SOAP_ACTION6, envelope);

            String xml = androidHttpTransport.responseDump;
            // txtServer.setText(androidHttpTransport.requestDump);

            File fileDir = null;
            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            fileDir = new File(froot.getAbsolutePath() + "/.server/");

            XmlSerializer serializer = Xml.newSerializer();
            File newxmlfile = new File(fileDir, "Recipient_Strategies.xml");
            String state = Environment.getExternalStorageState();

            if (Environment.MEDIA_MOUNTED.equals(state)) {
                try {
                    newxmlfile.createNewFile();
                } catch (IOException e) {

                }
                FileOutputStream fileos = null;
                try {
                    fileos = new FileOutputStream(newxmlfile);
                    fileos.write(xml.getBytes());
                    serializer.setOutput(fileos, "UTF-8");
                } catch (Exception ex) {
                } finally {
                    fileos.close();
                }
            }

        } catch (Exception e) {
            //Toast.makeText(getApplicationContext(),e.toString() , Toast.LENGTH_LONG).show();
        }
    }
    public void get_Strategies() {

        String URL6 = root + "/TimeSheet.asmx?op=get_Strategies";
        String SOAP_ACTION6 = "https://tempuri.org/get_Strategies";
        String METHOD_NAME6 = "get_Strategies";


        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME6);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL6);
            androidHttpTransport.debug = true;

            PropertyInfo pi2 = new PropertyInfo();
            pi2.setName("Fontra");
            pi2.setValue(getSecurityToken() + "99");
            request.addProperty(pi2);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            // Make the soap call.
            androidHttpTransport.call(SOAP_ACTION6, envelope);

            String xml = androidHttpTransport.responseDump;
            // txtServer.setText(androidHttpTransport.requestDump);

            File fileDir = null;
            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            fileDir = new File(froot.getAbsolutePath() + "/.server/");

            XmlSerializer serializer = Xml.newSerializer();
            File newxmlfile = new File(fileDir, "Strategies.xml");
            String state = Environment.getExternalStorageState();

            if (Environment.MEDIA_MOUNTED.equals(state)) {
                try {
                    newxmlfile.createNewFile();
                } catch (IOException e) {

                }
                FileOutputStream fileos = null;
                try {
                    fileos = new FileOutputStream(newxmlfile);
                    fileos.write(xml.getBytes());
                    serializer.setOutput(fileos, "UTF-8");
                } catch (Exception ex) {
                } finally {
                    fileos.close();
                }
            }

        } catch (Exception e) {
            //Toast.makeText(getApplicationContext(),e.toString() , Toast.LENGTH_LONG).show();
        }
    }
    public void load_incident_Locations() {

        String URL6 = root + "/TimeSheet.asmx?op=GetDomain_with_Criteria";
        String SOAP_ACTION6 = "https://tempuri.org/GetDomain_with_Criteria";
        String METHOD_NAME6 = "GetDomain_with_Criteria";


        String Criteria = "domain ='IMLocation'";
        boolean b_NoBlank = false;
        String s_Default = "";

        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME6);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL6);
            androidHttpTransport.debug = true;

            PropertyInfo pi1 = new PropertyInfo();
            pi1.setName("Criteria");
            pi1.setValue(getSecurityToken() + Criteria);
            request.addProperty(pi1);

            PropertyInfo pi2 = new PropertyInfo();
            pi2.setName("b_NoBlank");
            pi2.setValue(b_NoBlank);
            request.addProperty(pi2);

            PropertyInfo pi3 = new PropertyInfo();
            pi3.setName("s_Default");
            pi3.setValue(s_Default);
            request.addProperty(pi3);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            // Make the soap call.
            androidHttpTransport.call(SOAP_ACTION6, envelope);

            String xml = androidHttpTransport.responseDump;
            // txtServer.setText(androidHttpTransport.requestDump);

            File fileDir = null;
            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            fileDir = new File(froot.getAbsolutePath() + "/.server/");

            XmlSerializer serializer = Xml.newSerializer();
            File newxmlfile = new File(fileDir, "Incident_Locations.xml");
            String state = Environment.getExternalStorageState();

            if (Environment.MEDIA_MOUNTED.equals(state)) {
                try {
                    newxmlfile.createNewFile();
                } catch (IOException e) {

                }
                FileOutputStream fileos = null;
                try {
                    fileos = new FileOutputStream(newxmlfile);
                    fileos.write(xml.getBytes());
                    serializer.setOutput(fileos, "UTF-8");
                } catch (Exception ex) {
                } finally {
                    fileos.close();
                }
            }

        } catch (Exception e) {
            //Toast.makeText(getApplicationContext(),e.toString() , Toast.LENGTH_LONG).show();
        }
    }

    public void load_incident_Types() {

        String URL6 = root + "/TimeSheet.asmx?op=GetDomain_with_Criteria";
        String SOAP_ACTION6 = "https://tempuri.org/GetDomain_with_Criteria";
        String METHOD_NAME6 = "GetDomain_with_Criteria";


        String Criteria = "domain ='INCIDENT TYPE'";
        boolean b_NoBlank = false;
        String s_Default = "";

        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME6);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL6);
            androidHttpTransport.debug = true;

            PropertyInfo pi1 = new PropertyInfo();
            pi1.setName("Criteria");
            pi1.setValue(getSecurityToken() + Criteria);
            request.addProperty(pi1);

            PropertyInfo pi2 = new PropertyInfo();
            pi2.setName("b_NoBlank");
            pi2.setValue(b_NoBlank);
            request.addProperty(pi2);

            PropertyInfo pi3 = new PropertyInfo();
            pi3.setName("s_Default");
            pi3.setValue(s_Default);
            request.addProperty(pi3);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            // Make the soap call.
            androidHttpTransport.call(SOAP_ACTION6, envelope);

            String xml = androidHttpTransport.responseDump;
            // txtServer.setText(androidHttpTransport.requestDump);

            File fileDir = null;
            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            fileDir = new File(froot.getAbsolutePath() + "/.server/");

            XmlSerializer serializer = Xml.newSerializer();
            File newxmlfile = new File(fileDir, "Incident_Types.xml");
            String state = Environment.getExternalStorageState();

            if (Environment.MEDIA_MOUNTED.equals(state)) {
                try {
                    newxmlfile.createNewFile();
                } catch (IOException e) {

                }
                FileOutputStream fileos = null;
                try {
                    fileos = new FileOutputStream(newxmlfile);
                    fileos.write(xml.getBytes());
                    serializer.setOutput(fileos, "UTF-8");
                } catch (Exception ex) {
                } finally {
                    fileos.close();
                }
            }

        } catch (Exception e) {
            //Toast.makeText(getApplicationContext(),e.toString() , Toast.LENGTH_LONG).show();
        }
    }

    public void getRoster_Recipient() {

        try {
            String URL5 = root + "/TimeSheet.asmx?op=getRoster_RecipientAll";
            String SOAP_ACTION5 = "https://tempuri.org/getRoster_RecipientAll";
            String METHOD_NAME5 = "getRoster_RecipientAll";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME5);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL5);
            androidHttpTransport.debug = true;

            PropertyInfo pi = new PropertyInfo();
            pi.setName("StaffCode");

            pi.setValue(getSecurityToken() + StaffCode);
            request.addProperty(pi);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            // Make the soap call.
            androidHttpTransport.call(SOAP_ACTION5, envelope);

            String xml = androidHttpTransport.responseDump;
            // txtServer.setText(androidHttpTransport.requestDump);

            File fileDir = null;
            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            fileDir = new File(froot.getAbsolutePath() + "/.server/");

            XmlSerializer serializer = Xml.newSerializer();
            File newxmlfile = new File(fileDir, "Roster_Recipient.xml");
            String state = Environment.getExternalStorageState();

            if (Environment.MEDIA_MOUNTED.equals(state)) {
                try {
                    newxmlfile.createNewFile();
                } catch (IOException e) {

                }
                FileOutputStream fileos = null;
                try {
                    fileos = new FileOutputStream(newxmlfile);
                    fileos.write(xml.getBytes());
                    serializer.setOutput(fileos, "UTF-8");
                } catch (Exception ex) {
                } finally {
                    fileos.close();
                }
            }

        } catch (Exception ex) {
        }


    }

    public void load_Receipient_Detail(String MobileFutureLimit) {
        String URL2 = root + "/TimeSheet.asmx?op=getStaff_Recipient_Detail";
        String SOAP_ACTION2 = "https://tempuri.org/getStaff_Recipient_Detail";
        String METHOD_NAME2 = "getStaff_Recipient_Detail";

        String buff = "";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME2);
        try {
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL2);
            androidHttpTransport.debug = true;

            PropertyInfo pi3 = new PropertyInfo();
            pi3.setName("AccountNo");
            pi3.setValue(getSecurityToken() + StaffCode);
            request.addProperty(pi3);

            PropertyInfo pi4 = new PropertyInfo();
            pi4.setName("MobileFutureLimit");
            pi4.setValue(MobileFutureLimit);
            request.addProperty(pi4);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);
            androidHttpTransport.call(SOAP_ACTION2, envelope);
            // SoapObject obj=(SoapObject) envelope.getResponse();
            String xml = androidHttpTransport.responseDump;
            // txtServer.setText(androidHttpTransport.requestDump);

            byte[] byte_Data = xml.getBytes();// xml.getBytes(Charset.forName("UTF-8"));
            char[] key = {'M', 'T', 'A', 'S', 'A', 'M', 'A', 'D', 'A', '2', '0', '0', '2'};
            EncryptData.encodeFile(EncryptData.generateKey(key, byte_Data), byte_Data);

            File fileDir = null;
            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);

            fileDir = new File(froot.getAbsolutePath() + "/.server/");
            XmlSerializer serializer = Xml.newSerializer();
            File newxmlfile = new File(fileDir, "Recipient.xml");
            String state = Environment.getExternalStorageState();

            if (Environment.MEDIA_MOUNTED.equals(state)) {
                try {
                    newxmlfile.createNewFile();

                } catch (IOException e) {
                    // textMsg.setText("bb "+ e.toString());
                }
                FileOutputStream fileos = null;


                try {
                    fileos = new FileOutputStream(newxmlfile);
                    fileos.write(xml.getBytes());
                    serializer.setOutput(fileos, "UTF-8");
                } catch (Exception ex) {
                } finally {
                    fileos.close();
                }
            }
        } catch (Exception ex) {
        }

        //    textMsg.setText("Done successfully");

    }

    public void load_Task_Detail() {
        String URL3 = root + "/TimeSheet.asmx?op=getAllTaskList";
        String SOAP_ACTION3 = "https://tempuri.org/getAllTaskList";
        String METHOD_NAME3 = "getAllTaskList";

        String buff = "";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME3);
        try {
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL3);
            androidHttpTransport.debug = true;
            PropertyInfo pi = new PropertyInfo();
            pi.setName("carer_code");
            pi.setValue(getSecurityToken() + StaffCode);
            request.addProperty(pi);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);
            androidHttpTransport.call(SOAP_ACTION3, envelope);
            // SoapObject obj=(SoapObject) envelope.getResponse();
            String xml = androidHttpTransport.responseDump;
            // txtServer.setText(androidHttpTransport.requestDump);

            File fileDir = null;
            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);

            fileDir = new File(froot.getAbsolutePath() + "/.server/");
            XmlSerializer serializer = Xml.newSerializer();
            File newxmlfile = new File(fileDir, "task.xml");
            String state = Environment.getExternalStorageState();

            if (Environment.MEDIA_MOUNTED.equals(state)) {
                try {
                    newxmlfile.createNewFile();

                } catch (IOException e) {

                }
                FileOutputStream fileos = null;

                try {
                    fileos = new FileOutputStream(newxmlfile);
                    fileos.write(xml.getBytes());
                    serializer.setOutput(fileos, "UTF-8");
                } catch (Exception ex) {
                } finally {
                    fileos.close();
                }
            }
        } catch (Exception ex) {
        }
        //    textMsg.setText("Done successfully");

    }

    public void load_Group_Alerts_Detail() {
        String URL6 = root + "/TimeSheet.asmx?op=getAllAlertGroups";
        String SOAP_ACTION6 = "https://tempuri.org/getAllAlertGroups";
        String METHOD_NAME6 = "getAllAlertGroups";

        String buff = "";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME6);
        try {
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL6);
            androidHttpTransport.debug = true;
            PropertyInfo pi2 = new PropertyInfo();
            pi2.setName("Fontra");
            pi2.setValue(getSecurityToken() + "99");
            request.addProperty(pi2);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);
            androidHttpTransport.call(SOAP_ACTION6, envelope);
            // SoapObject obj=(SoapObject) envelope.getResponse();
            String xml = androidHttpTransport.responseDump;
            // txtServer.setText(androidHttpTransport.requestDump);

            File fileDir = null;
            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);

            fileDir = new File(froot.getAbsolutePath() + "/.server/");
            XmlSerializer serializer = Xml.newSerializer();
            File newxmlfile = new File(fileDir, "group_alerts.xml");
            String state = Environment.getExternalStorageState();

            if (Environment.MEDIA_MOUNTED.equals(state)) {
                try {
                    newxmlfile.createNewFile();

                } catch (IOException e) {
                    //textMsg.setText("bb "+ e.toString());
                }
                FileOutputStream fileos = null;

                try {
                    fileos = new FileOutputStream(newxmlfile);
                    fileos.write(xml.getBytes());
                    serializer.setOutput(fileos, "UTF-8");
                } catch (Exception ex) {
                } finally {
                    fileos.close();
                }
            }
        } catch (Exception ex) {
        }
        //    textMsg.setText("Done successfully");

    }

    public void load_LeaveTypes() {

        String URL6 = root + "/TimeSheet.asmx?op=GetDomain_with_Criteria";
        String SOAP_ACTION6 = "https://tempuri.org/GetDomain_with_Criteria";
        String METHOD_NAME6 = "GetDomain_with_Criteria";

        String Criteria = " DOMAIN = 'LEAVEAPP'";


        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME6);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL6);
            androidHttpTransport.debug = true;

            PropertyInfo pi1 = new PropertyInfo();
            pi1.setName("Criteria");
            pi1.setValue(getSecurityToken() + Criteria);
            request.addProperty(pi1);

            PropertyInfo pi2 = new PropertyInfo();
            pi2.setName("b_NoBlank");
            pi2.setValue("true");
            request.addProperty(pi2);

            PropertyInfo pi3 = new PropertyInfo();
            pi3.setName("s_Default");
            pi3.setValue("-");
            request.addProperty(pi3);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            // Make the soap call.
            androidHttpTransport.call(SOAP_ACTION6, envelope);

            String xml = androidHttpTransport.responseDump;
            // txtServer.setText(androidHttpTransport.requestDump);

            File fileDir = null;
            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            fileDir = new File(froot.getAbsolutePath() + "/.server/");

            XmlSerializer serializer = Xml.newSerializer();
            File newxmlfile = new File(fileDir, "LeaveTypes.xml");
            String state = Environment.getExternalStorageState();

            if (Environment.MEDIA_MOUNTED.equals(state)) {
                try {
                    newxmlfile.createNewFile();
                } catch (IOException e) {

                }
                FileOutputStream fileos = null;
                try {
                    fileos = new FileOutputStream(newxmlfile);
                    fileos.write(xml.getBytes());
                    serializer.setOutput(fileos, "UTF-8");
                } catch (Exception ex) {
                } finally {
                    fileos.close();
                }
            }

        } catch (Exception e) {
            //Toast.makeText(getApplicationContext(),e.toString() , Toast.LENGTH_LONG).show();
        }
    }

    public void load_paid_hours() {

        String URL6 = root + "/TimeSheet.asmx?op=get_Paid_Hours";
        String SOAP_ACTION6 = "https://tempuri.org/get_Paid_Hours";
        String METHOD_NAME6 = "get_Paid_Hours";


        String format = "yyyy/MM/dd";

        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        Calendar c = Calendar.getInstance();
        Date dt = c.getTime();

        String curr_Date = sdf.format(dt);

        // Toast.makeText(getApplicationContext(),  "StaffCode=" + StaffCode, Toast.LENGTH_LONG).show();
        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME6);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL6);
            androidHttpTransport.debug = true;

            PropertyInfo pi1 = new PropertyInfo();
            pi1.setName("AccountNo");
            pi1.setValue(getSecurityToken() + StaffCode);
            request.addProperty(pi1);

            PropertyInfo pi2 = new PropertyInfo();
            pi2.setName("Roster_Date");
            pi2.setValue(curr_Date);
            request.addProperty(pi2);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            // Make the soap call.
            androidHttpTransport.call(SOAP_ACTION6, envelope);

            String xml = androidHttpTransport.responseDump;
            // txtServer.setText(androidHttpTransport.requestDump);

            File fileDir = null;
            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            fileDir = new File(froot.getAbsolutePath() + "/.server/");

            XmlSerializer serializer = Xml.newSerializer();
            File newxmlfile = new File(fileDir, "Paid_Hours.xml");
            String state = Environment.getExternalStorageState();

            if (Environment.MEDIA_MOUNTED.equals(state)) {
                try {
                    newxmlfile.createNewFile();
                } catch (IOException e) {

                }
                FileOutputStream fileos = null;
                try {
                    fileos = new FileOutputStream(newxmlfile);
                    fileos.write(xml.getBytes());
                    serializer.setOutput(fileos, "UTF-8");
                } catch (Exception ex) {
                } finally {
                    fileos.close();
                }
            }

        } catch (Exception e) {
            //  Toast.makeText(getApplicationContext(),e.toString() , Toast.LENGTH_LONG).show();
        }
    }
    public void load_Roster_data(){

        String URL = root  + "/TimeSheet.asmx?op=getMonth_Rosters";
        String SOAP_ACTION =  "https://tempuri.org/getMonth_Rosters";
        String METHOD_NAME = "getMonth_Rosters";

        String buff="";
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

        Calendar myCalendar = Calendar.getInstance();
        Date date = myCalendar.getTime();
        String strDate=dateFormat.format(date);
        String[] strDate2 =strDate.split("/");

        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);

        try{
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.debug =true;

            PropertyInfo pi=new PropertyInfo();
            pi.setName("client_code");
            pi.setValue( getSecurityToken() + StaffCode);
            request.addProperty(pi);

            PropertyInfo pi2=new PropertyInfo();
            pi2.setName("MonthNo");
            pi2.setValue(strDate2[1]);
            request.addProperty(pi2);

            PropertyInfo pi3=new PropertyInfo();
            pi3.setName("YearNo");
            pi3.setValue(strDate2[0]);
            request.addProperty(pi3);


            PropertyInfo pi4=new PropertyInfo();
            pi4.setName("client");
            pi4.setValue(false);
            request.addProperty(pi4);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet=true;

            envelope.setOutputSoapObject(request);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            // SoapObject obj=(SoapObject) envelope.getResponse();
            String xml= androidHttpTransport.responseDump;
            // txtServer.setText(androidHttpTransport.requestDump);

            File fileDir=null;
            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);

            fileDir = new File(froot.getAbsolutePath()+"/.server/");
            XmlSerializer serializer = Xml.newSerializer();
            File newxmlfile = new File(fileDir,"traccs.xml");
            String state = Environment.getExternalStorageState();

            if (Environment.MEDIA_MOUNTED.equals(state)) {
                try{
                    newxmlfile.createNewFile();

                }catch(IOException e){
                    //textMsg.setText("bb "+ e.toString());
                }
                FileOutputStream fileos = null;

                try{
                    fileos = new FileOutputStream(newxmlfile );
                    fileos.write(xml.getBytes());

                    serializer.setOutput(fileos, "UTF-8");
                }catch (Exception ex){}// textMsg.setText("cc: " + ex.toString());}

            }
        }catch (Exception ex){ }//textMsg.setText("cc: " + ex.toString());}
        //    textMsg.setText("Done successfully");


    }

    public void get_OP_Case_Notes(String RecipientCode){
/*

        try {
            String URL5 = root + "/TimeSheet.asmx?op=getOP_Case_NoteAll";
            String SOAP_ACTION5 = "https://tempuri.org/getOP_Case_NoteAll";
            String METHOD_NAME5 = "getOP_Case_NoteAll";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME5);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL5);
            androidHttpTransport.debug = true;

            PropertyInfo pi = new PropertyInfo();
            pi.setName("StaffCode");

            pi.setValue(getSecurityToken() + StaffCode);
            request.addProperty(pi);

            PropertyInfo pi2 = new PropertyInfo();
            pi2.setName("RecipientsCodes");

            pi2.setValue("A");
            request.addProperty(pi2);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            // Make the soap call.
            androidHttpTransport.call(SOAP_ACTION5, envelope);

            String xml = androidHttpTransport.responseDump;
            // txtServer.setText(androidHttpTransport.requestDump);

            File fileDir = null;
            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            fileDir = new File(froot.getAbsolutePath() + "/.server/");

            XmlSerializer serializer = Xml.newSerializer();
            File newxmlfile = new File(fileDir, "op_case_note.xml");
            String state = Environment.getExternalStorageState();

            if (Environment.MEDIA_MOUNTED.equals(state)) {
                try {
                    newxmlfile.createNewFile();
                } catch (IOException e) {

                }
                FileOutputStream fileos = null;
                try {
                    fileos = new FileOutputStream(newxmlfile);
                    fileos.write(xml.getBytes());
                    serializer.setOutput(fileos, "UTF-8");
                } catch (Exception ex) {
                } finally {
                    fileos.close();
                }
            }

        } catch (Exception ex) {

            ex.printStackTrace();

        }
*/

//Testing

        try {
            String URL5 = root + "/TimeSheet.asmx?op=getOP_Case_NoteSingle";
            String SOAP_ACTION5 = "https://tempuri.org/getOP_Case_NoteSingle";
            String METHOD_NAME5 = "getOP_Case_NoteSingle";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME5);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL5);
            androidHttpTransport.debug = true;

            PropertyInfo pi = new PropertyInfo();
            pi.setName("StaffCode");

            pi.setValue(getSecurityToken() + StaffCode);
            request.addProperty(pi);

            PropertyInfo pi2 = new PropertyInfo();
            pi2.setName("RecipientsCodes");

            pi2.setValue(RecipientCode);
            request.addProperty(pi2);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            // Make the soap call.
            androidHttpTransport.call(SOAP_ACTION5, envelope);

            String xml = androidHttpTransport.responseDump;
            // txtServer.setText(androidHttpTransport.requestDump);

            File fileDir = null;
            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            fileDir = new File(froot.getAbsolutePath() + "/.server/");

            XmlSerializer serializer = Xml.newSerializer();
            File newxmlfile = new File(fileDir, "op_case_note.xml");
            String state = Environment.getExternalStorageState();

            if (Environment.MEDIA_MOUNTED.equals(state)) {
                try {
                    newxmlfile.createNewFile();
                } catch (IOException e) {

                }
                FileOutputStream fileos = null;
                try {
                    fileos = new FileOutputStream(newxmlfile);
                    fileos.write(xml.getBytes());
                    serializer.setOutput(fileos, "UTF-8");
                } catch (Exception ex) {
                } finally {
                    fileos.close();
                }
            }

        } catch (Exception ex) {

            ex.printStackTrace();

        }

//Testing



    }

    public void get_OP_Case_Note(String RecipientCode) {


        try {
            String URL5 = root + "/TimeSheet.asmx?op=getOP_Case_Notes";
            String SOAP_ACTION5 = "https://tempuri.org/getOP_Case_Notes";
            String METHOD_NAME5 = "getOP_Case_Notes";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME5);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL5);
            androidHttpTransport.debug = true;

            PropertyInfo pi = new PropertyInfo();
            pi.setName("RecipientCode");

            pi.setValue(getSecurityToken() + RecipientCode);
            request.addProperty(pi);

            PropertyInfo pi2 = new PropertyInfo();
            pi2.setName("Note_Type");

            pi2.setValue("CASENOTE");
            request.addProperty(pi2);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            // Make the soap call.
            androidHttpTransport.call(SOAP_ACTION5, envelope);

            String xml = androidHttpTransport.responseDump;
            // txtServer.setText(androidHttpTransport.requestDump);

            File fileDir = null;
            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            fileDir = new File(froot.getAbsolutePath() + "/.server/");

            XmlSerializer serializer = Xml.newSerializer();
            File newxmlfile = new File(fileDir, "op_case_note.xml");
            String state = Environment.getExternalStorageState();

            if (Environment.MEDIA_MOUNTED.equals(state)) {
                try {
                    newxmlfile.createNewFile();
                } catch (IOException e) {

                }
                FileOutputStream fileos = null;
                try {
                    fileos = new FileOutputStream(newxmlfile);
                    fileos.write(xml.getBytes());
                    serializer.setOutput(fileos, "UTF-8");
                } catch (Exception ex) {
                } finally {
                    fileos.close();
                }
            }

        } catch (Exception ex) {
        }


    }

    public String getRecipientCodes() {

        String client_codes = "";

        try {
            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            String state = Environment.getExternalStorageState();
            File fileDir = null;
            fileDir = new File(froot.getAbsolutePath() + "/.server/");
            File fXmlFile = new File(fileDir, "Recipient.xml");
            //  rosters=new ArrayList<Roster_Info>();
            if (fXmlFile.exists()) {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                org.w3c.dom.Document doc = dBuilder.parse(fXmlFile);
                doc.getDocumentElement().normalize();
                NodeList nList = doc.getElementsByTagName("Recipient");
                if (nList == null) return "";

                String client_code = "";


                for (int temp = 0; temp < nList.getLength(); temp++) {

                    try {
                        Node nNode = nList.item(temp);

                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) nNode;
                            client_code = eElement.getElementsByTagName("AccountNo").item(0).getTextContent();

                            if ((client_codes.indexOf(client_code)) > -1) continue;

                            if (client_codes.equalsIgnoreCase(""))
                                client_codes = client_code;
                            else
                                client_codes = client_codes + "," + client_code;

                        }

                    } catch (Exception ex) {
                    }

                }
            }
        } catch (Exception ex) {
        }

        return client_codes;
    }

    public void getProgramRecipients(String MobileFutureLimit) {

        try {
            String URL5 = root + "/TimeSheet.asmx?op=getProgram_Recipients";
            String SOAP_ACTION5 = "https://tempuri.org/getProgram_Recipients";
            String METHOD_NAME5 = "getProgram_Recipients";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME5);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL5);
            androidHttpTransport.debug = true;

            PropertyInfo pi = new PropertyInfo();
            pi.setName("StaffCode");

            pi.setValue(getSecurityToken() + StaffCode);
            request.addProperty(pi);

            PropertyInfo pi2 = new PropertyInfo();
            pi2.setName("MobileFutureLimit");

            pi2.setValue(MobileFutureLimit);
            request.addProperty(pi2);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            // Make the soap call.
            androidHttpTransport.call(SOAP_ACTION5, envelope);

            String xml = androidHttpTransport.responseDump;
            // txtServer.setText(androidHttpTransport.requestDump);

            File fileDir = null;
            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            fileDir = new File(froot.getAbsolutePath() + "/.server/");

            XmlSerializer serializer = Xml.newSerializer();
            File newxmlfile = new File(fileDir, "ProgramRecipients.xml");
            String state = Environment.getExternalStorageState();

            if (Environment.MEDIA_MOUNTED.equals(state)) {
                try {
                    newxmlfile.createNewFile();
                } catch (IOException e) {

                }
                FileOutputStream fileos = null;
                try {
                    fileos = new FileOutputStream(newxmlfile);
                    fileos.write(xml.getBytes());
                    serializer.setOutput(fileos, "UTF-8");
                } catch (Exception ex) {
                } finally {
                    fileos.close();
                }
            }

        } catch (Exception ex) {
        }


    }

    public void get_Active_Device_Reminder() {


        try {

            String URL4 = root + "/TimeSheet.asmx?op=getDevice_Active_Reminders";
            String SOAP_ACTION4 = "https://tempuri.org/getDevice_Active_Reminders";
            String METHOD_NAME4 = "getDevice_Active_Reminders";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME4);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL4);
            androidHttpTransport.debug = true;

            PropertyInfo pi = new PropertyInfo();
            pi.setName("UserID");
            pi.setValue(getSecurityToken() + StaffCode);
            request.addProperty(pi);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);

            // Make the soap call.
            androidHttpTransport.call(SOAP_ACTION4, envelope);


            String xml = androidHttpTransport.responseDump;
            // txtServer.setText(androidHttpTransport.requestDump);

            File fileDir = null;
            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            fileDir = new File(froot.getAbsolutePath() + "/.server/");

            XmlSerializer serializer = Xml.newSerializer();
            File newxmlfile = new File(fileDir, "Reminder.xml");
            String state = Environment.getExternalStorageState();

            if (Environment.MEDIA_MOUNTED.equals(state)) {
                try {
                    newxmlfile.createNewFile();
                } catch (IOException e) {

                }
                FileOutputStream fileos = null;
                try {
                    fileos = new FileOutputStream(newxmlfile);
                    fileos.write(xml.getBytes());
                    serializer.setOutput(fileos, "UTF-8");
                } catch (Exception ex) {
                } finally {
                    fileos.close();
                }
            }


        } catch (Exception ex) {
        }

    }
}
