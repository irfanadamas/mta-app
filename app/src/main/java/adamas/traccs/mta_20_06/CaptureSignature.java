package adamas.traccs.mta_20_06;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.util.Calendar;

import org.kobjects.base64.Base64;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
 
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Images;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.kobjects.base64.Base64;

public class CaptureSignature extends Activity { 
 
	
	  public String root="https://58.162.142.150/timesheet" ; //https://10.0.2.2:49884
	    private final String NAMESPACE = "https://tempuri.org/";
	    
	    private  String URL4 = root  + "/TimeSheet.asmx?op=Login_User";
	    private final String SOAP_ACTION4 =  "https://tempuri.org/Login_User";
	    private final String METHOD_NAME4 = "Login_User"; 
	    
	    private  String URL = root  + "/TimeSheet.asmx?op=Save_User_Signature";
	    private final String SOAP_ACTION =  "https://tempuri.org/Save_User_Signature";
	    private final String METHOD_NAME = "Save_User_Signature"; 
	    
	    
    LinearLayout mContent;
    signature mSignature;
    Button mClear, mGetSign, mCancel;
    public static String tempDir;
    Context context;
    public int count = 1;
    public String current = null;
    private Bitmap mBitmap;
    View mView;
    File mypath;
    String picturePath="";
    private String uniqueId;
    private EditText yourName;
    private EditText password;
    byte[] buffer=null;
    
    String Security_Token ="";
    String OperatorId ="";

    
 int one=1;
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        this.context=this.getApplicationContext();
        getWindow().setFormat(PixelFormat.RGBA_8888);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DITHER);  
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.signature);
        set_Server_Ip();   
     
        
       // if (one==1) return;
        try{
	        tempDir = context.getExternalFilesDir(null).getAbsolutePath();//Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/" + getResources().getString(R.string.external_dir) + "/";
	        ContextWrapper cw = new ContextWrapper(getApplicationContext());
	        File directory = cw.getDir(getResources().getString(R.string.external_dir), Context.MODE_PRIVATE);
	 
	        prepareDirectory();
	        uniqueId = getTodaysDate() + "_" + getCurrentTime() + "_" + Math.random();
	        current = uniqueId + ".png";
	        mypath= new File(directory,current);
	        
   	 	}catch(Exception ex){Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();}
        
		 try{
		        mContent = (LinearLayout) findViewById(R.id.linearLayout);
		        mSignature = new signature(this, null);
		        mSignature.setBackgroundColor(Color.WHITE);
		        mContent.addView(mSignature, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		        mClear = (Button)findViewById(R.id.clear);
		        mGetSign = (Button)findViewById(R.id.getsign);
		        mGetSign.setEnabled(false);
		        mCancel = (Button)findViewById(R.id.cancel);
		        mView = mContent;
		 
		        yourName = (EditText) findViewById(R.id.txtUser);
		 }catch(Exception ex){Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();}
		 
        mClear.setOnClickListener(new OnClickListener() 
        {        
            public void onClick(View v) 
            {
                Log.v("log_tag", "Panel Cleared");
                mSignature.clear();
                mGetSign.setEnabled(false);
                TextView lblMsg  = (TextView) findViewById(R.id.lblMsg);
                lblMsg.setText("Please Sign below ...");
            }
        });
 
        mGetSign.setOnClickListener(new OnClickListener() 
        {        
            public void onClick(View v) 
            {
            	try{
            		 TextView lblMsg  = (TextView) findViewById(R.id.lblMsg);
                     
              //.v("log_tag", "Panel Saved");
            	if (login_user()==false){
            		Toast.makeText(getApplicationContext(),"Invalid User Information" , Toast.LENGTH_LONG).show();
            		lblMsg.setText("Invalid User Information");
            		return;
            	}
                boolean error = captureSignature();
                if(!error){
                    mView.setDrawingCacheEnabled(true);
                    mSignature.save(mView);
                    Save_User_Signature();
                    Bundle b = new Bundle();
                    b.putString("status", "done");                 
                    
                    Intent intent = new Intent();
                    intent.putExtras(b);
                    setResult(RESULT_OK,intent);   
                   // finish();
                }
            	
            }catch(Exception ex){Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();}
            }
        });
 
        mCancel.setOnClickListener(new OnClickListener() 
        {        
            public void onClick(View v) 
            {
             //Log.v("log_tag", "Panel Canceled");
            	try{
                Bundle b = new Bundle();
                b.putString("status", "cancel");
                Intent intent = new Intent();
                intent.putExtras(b);
                setResult(RESULT_OK,intent);  
                finish();
            	 }catch(Exception ex){Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();}
            }
        });
 
    }
 
   
    @Override
    protected void onDestroy() {
        Log.w("GetSignature", "onDestory");
        try {
            mypath.delete();
        }catch(Exception ex){}

        super.onDestroy();
    }
 
    private boolean captureSignature() {
 
        boolean error = false;
        String errorMessage = "";
 
 
        if(yourName.getText().toString().equalsIgnoreCase("")){
            errorMessage = errorMessage + "Please enter your Name\n";
            error = true;
        }   
 
        if(error){
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 105, 50);
            toast.show();
        }
 
        return error;
    }
 
    private String getTodaysDate() { 
 
        final Calendar c = Calendar.getInstance();
        int todaysDate =     (c.get(Calendar.YEAR) * 10000) + 
        ((c.get(Calendar.MONTH) + 1) * 100) + 
        (c.get(Calendar.DAY_OF_MONTH));
        Log.w("DATE:",String.valueOf(todaysDate));
        return(String.valueOf(todaysDate));
 
    }
 
    private String getCurrentTime() {
 
        final Calendar c = Calendar.getInstance();
        int currentTime =     (c.get(Calendar.HOUR_OF_DAY) * 10000) + 
        (c.get(Calendar.MINUTE) * 100) + 
        (c.get(Calendar.SECOND));
        Log.w("TIME:",String.valueOf(currentTime));
        return(String.valueOf(currentTime));
 
    }
 
 
    private boolean prepareDirectory() 
    {
        try
        {
            return makedirs();
        } catch (Exception e) 
        {
            e.printStackTrace();
            Toast.makeText(this, "Could not initiate File System.. Is Sdcard mounted properly?", Toast.LENGTH_LONG).show();
            return false;
        }
    }
 
    private boolean makedirs() 
    {
        File tempdir = new File(tempDir);
        if (!tempdir.exists())
            tempdir.mkdirs();
 
        if (tempdir.isDirectory()) 
        {
            File[] files = tempdir.listFiles();
            for (File file : files) 
            {
                if (!file.delete()) 
                {
                    System.out.println("Failed to delete " + file);
                }
            }
        }
        return (tempdir.isDirectory());
    }
    public void set_Server_Ip(){
 	   
        Bundle bundle = getIntent().getExtras(); 
        
          root=bundle.get("root").toString();

          OperatorId=bundle.get("OperatorId").toString();
          Security_Token=bundle.get("Security_Token").toString();

           URL=root  + "/TimeSheet.asmx?op=Save_User_Signature";
           URL4=root  + "/TimeSheet.asmx?op=Login_User";
           
         
   } 
    String getSecurityToken(){
  	   
       	String Val= OperatorId  + "$" + Security_Token + "$";
       	return Val;
       }

    void Save_User_Signature(){
        SoapPrimitive  result=null;
        TextView lblMsg  = (TextView) findViewById(R.id.lblMsg);
        
        try{
        	Toast.makeText(getApplicationContext(),"Saving Signature" , Toast.LENGTH_LONG).show();
        	
             if (buffer==null)
             {
            	 Toast.makeText(getApplicationContext(), "Invalid Signature", Toast.LENGTH_LONG).show();
                 return;
             }
         
          SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
          HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
          androidHttpTransport.debug =true;
          
        try{
                           
             
             
              PropertyInfo pi=new PropertyInfo();
              pi.type = MarshalBase64.BYTE_ARRAY_CLASS;
              pi.setName("Signature"); 
              String  base64String = Base64.encode(buffer);
              pi.setValue(base64String); 
              request.addProperty(pi);
              
              PropertyInfo pi2=new PropertyInfo(); 
              pi2.setName("User");     
              pi2.setValue(getSecurityToken() + yourName.getText().toString() ); 
              request.addProperty(pi2);
              
              
        }catch (Exception e) {Toast.makeText(getApplicationContext(),e.toString() , Toast.LENGTH_LONG).show();}
        
         try{
              SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
              envelope.dotNet=true;
              envelope.encodingStyle = SoapEnvelope.ENC; 
              envelope.setOutputSoapObject(request);
              // Make the soap call.
              androidHttpTransport.call(SOAP_ACTION, envelope);     
             result =(SoapPrimitive)envelope.getResponse();
       }catch (Exception e) {Toast.makeText(getApplicationContext(),e.toString() , Toast.LENGTH_LONG).show();}
         
        
        // Toast.makeText(getApplicationContext(),"result="+ result.toString() , Toast.LENGTH_LONG).show();
         
	   if (result.toString().equalsIgnoreCase("true")){
	            Toast.makeText(getApplicationContext(), "Signature saved successfully", Toast.LENGTH_LONG).show();
	            lblMsg.setText("Signature saved successfully");
	    }
       else{
        	 Toast.makeText(getApplicationContext(), "Signature not saved", Toast.LENGTH_LONG).show();
        	 lblMsg.setText("Signature not saved");
          }
      
         
         }catch (Exception e) {Toast.makeText(getApplicationContext(),e.toString() , Toast.LENGTH_LONG).show();
         }
   }
    public boolean login_user()
    {
    	boolean status=false;
       try{
       
	   
	       EditText usr  = (EditText) findViewById(R.id.txtUser);
	       EditText pass  = (EditText) findViewById(R.id.txtPassword);
	      
	       SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME4);
                                                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL4 );
                                                androidHttpTransport.debug =true;

                                                PropertyInfo pi3=new PropertyInfo(); 
                                                pi3.setName("User"); 

                                                String message = usr.getText().toString();
                                                pi3.setValue(message); 
                                                OperatorId=message;
                                                request.addProperty(pi3);

                                                PropertyInfo pi4=new PropertyInfo(); 
                                                pi4.setName("Password"); 

                                                 message = pass.getText().toString();
                                                pi4.setValue(message); 
                                                request.addProperty(pi4);
                                                                                               
                                                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

                                                envelope.dotNet=true;
                                                envelope.setOutputSoapObject(request);

                                                // Make the soap call.
                                                androidHttpTransport.call(SOAP_ACTION4, envelope);     
                                               SoapObject obj=(SoapObject)envelope.getResponse();
                                             
                                               // textMsg.setText( obj.getName() + " " + obj.getPropertyCount());
                                               SoapPrimitive  result=null;
                                               if (obj.getPropertyCount()>0)
                                                   result =(SoapPrimitive) obj.getProperty("Login");
                                               
                                             //  Toast.makeText(getApplicationContext(), result.toString(), Toast.LENGTH_LONG).show(); 
                                                   
                                              if (result==null)
                                                 status=false;
                                              else status= result.toString().equalsIgnoreCase("true");
                                              
                                            if ( obj.getProperty("Security_Token")!=null) 
                                          	{
                                              	result =(SoapPrimitive) obj.getProperty("Security_Token");
                                              	Security_Token= result.toString().substring(3);                                              	
                                            }
                                            
                                            		  
        
       }catch(Exception ex){Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();}
       
       return status;
       }
    public class signature extends View 
    {
        private static final float STROKE_WIDTH = 5f;
        private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;
        private final Paint paint = new Paint();
        private final Path path = new Path();
 
        private float lastTouchX;
        private float lastTouchY;
        private final RectF dirtyRect = new RectF();
 
        public signature(Context context, AttributeSet attrs) 
        {
            super(context, attrs);
            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeWidth(STROKE_WIDTH);
        }
 
        public void save(View v) 
        {
            Log.v("log_tag", "Width: " + v.getWidth());
            Log.v("log_tag", "Height: " + v.getHeight());
            if(mBitmap == null)
            {
                mBitmap =  Bitmap.createBitmap (mContent.getWidth(), mContent.getHeight(), Bitmap.Config.RGB_565);
            }
            Canvas canvas = new Canvas(mBitmap);
           // buffer= bitmapToByteArray(mBitmap);
          
            try
            {
                FileOutputStream mFileOutStream = new FileOutputStream(mypath);
                picturePath= mypath.getPath();
                v.draw(canvas); 
                mBitmap.compress(Bitmap.CompressFormat.PNG, 90, mFileOutStream); 
                mFileOutStream.flush();
                mFileOutStream.close();
                String url = Images.Media.insertImage(getContentResolver(), mBitmap, "title", null);
                Log.v("log_tag","url: " + url);
                buffer= bitmapToByteArray(BitmapFactory.decodeFile(picturePath));
                //In case you want to delete the file
                //boolean deleted = mypath.delete();
                //Log.v("log_tag","deleted: " + mypath.toString() + deleted);
                //If you want to convert the image to string use base64 converter
               
 
            }
            catch(Exception e) 
            { 
                Log.v("log_tag", e.toString()); 
            } 
        }
 
        public  byte[] bitmapToByteArray(Bitmap bm) {
            // Create the buffer with the correct size
            int iBytes = bm.getWidth() * bm.getHeight() * 4;
            ByteBuffer buffer = ByteBuffer.allocate(iBytes);

            // Log.e("DBG", buffer.remaining()+""); -- Returns a correct number based on dimensions
            // Copy to buffer and then into byte array
            bm.copyPixelsToBuffer(buffer);
            // Log.e("DBG", buffer.remaining()+""); -- Returns 0
         
                ByteArrayOutputStream stream=new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.PNG, 90, stream);
                byte[] image=stream.toByteArray();
                
            return image;
            
            
        }
        
        public void clear() 
        {
            path.reset();
            invalidate();
        }
 
        @Override
        protected void onDraw(Canvas canvas) 
        {
            canvas.drawPath(path, paint);
        }
 
        @Override
        public boolean onTouchEvent(MotionEvent event) 
        {
            float eventX = event.getX();
            float eventY = event.getY();
            mGetSign.setEnabled(true);
 
            switch (event.getAction()) 
            {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(eventX, eventY);
                lastTouchX = eventX;
                lastTouchY = eventY;
                return true;
 
            case MotionEvent.ACTION_MOVE:
 
            case MotionEvent.ACTION_UP:
 
                resetDirtyRect(eventX, eventY);
                int historySize = event.getHistorySize();
                for (int i = 0; i < historySize; i++) 
                {
                    float historicalX = event.getHistoricalX(i);
                    float historicalY = event.getHistoricalY(i);
                    expandDirtyRect(historicalX, historicalY);
                    path.lineTo(historicalX, historicalY);
                }
                path.lineTo(eventX, eventY);
                break;
 
            default:
                debug("Ignored touch event: " + event.toString());
                return false;
            }
 
            invalidate((int) (dirtyRect.left - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.top - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.right + HALF_STROKE_WIDTH),
                    (int) (dirtyRect.bottom + HALF_STROKE_WIDTH));
 
            lastTouchX = eventX;
            lastTouchY = eventY;
 
            return true;
        }
 
        private void debug(String string){
        }
 
        private void expandDirtyRect(float historicalX, float historicalY) 
        {
            if (historicalX < dirtyRect.left) 
            {
                dirtyRect.left = historicalX;
            } 
            else if (historicalX > dirtyRect.right) 
            {
                dirtyRect.right = historicalX;
            }
 
            if (historicalY < dirtyRect.top) 
            {
                dirtyRect.top = historicalY;
            } 
            else if (historicalY > dirtyRect.bottom) 
            {
                dirtyRect.bottom = historicalY;
            }
        }
 
        private void resetDirtyRect(float eventX, float eventY) 
        {
            dirtyRect.left = Math.min(lastTouchX, eventX);
            dirtyRect.right = Math.max(lastTouchX, eventX);
            dirtyRect.top = Math.min(lastTouchY, eventY);
            dirtyRect.bottom = Math.max(lastTouchY, eventY);
        }
    }
}