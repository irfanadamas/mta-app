package adamas.traccs.mta_20_06;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class LoadingDialog {
    private final Activity activity;
    private AlertDialog dialog;

    private TextView txtMsg;
    private String Message="Please wait while loading ......";
    private boolean CancelAble=true;
    LoadingDialog(Activity _activity){
        this.activity=_activity;

    }
    LoadingDialog(Activity _activity,Boolean cancel_able){
        this.activity=_activity;
        this.CancelAble=cancel_able;
    }

    void show(){

        try {

            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            LayoutInflater inflater = activity.getLayoutInflater();
            View view= inflater.inflate(R.layout.loading_dialog,null);
            TextView txtMessage= view.findViewById(R.id.txtMessage);
            ImageView btnExit= view.findViewById(R.id.btnExit);
            txtMessage.setText(this.Message);
            builder.setView(view);
            builder.setCancelable(this.CancelAble);
            this.dialog = builder.create();

            btnExit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

           // dialog.setMessage("Processing....");
            if (dialog!=null)
               dialog.show();

        }catch(Exception ex){}

    }
    void refresh(){

        try {


            this.dialog.setMessage(this.Message);

        }catch(Exception ex){}

    }
    public void setCancelAble(boolean cancelable){
        this.CancelAble=cancelable;
    }
    public void setMessage(String message){
    this.Message=message;

}
    public  void dismiss(){
        if (dialog!=null)
        dialog.dismiss();
    }

    public void cancel(){
        if (dialog!=null) dialog.dismiss();
    }
}
