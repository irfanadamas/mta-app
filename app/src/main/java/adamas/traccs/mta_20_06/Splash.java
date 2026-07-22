package adamas.traccs.mta_20_06;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import java.util.Timer;

import androidx.appcompat.app.AppCompatActivity;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

    }

    @Override
    protected void onResume(){
        super.onResume();
        delay(2);
        TextView textView1= findViewById(R.id.textView1);
        TextView textView2= findViewById(R.id.textView2);
        TextView textView3= findViewById(R.id.textView3);
        TextView textView4= findViewById(R.id.textView4);
        TextView textView5= findViewById(R.id.textView5);

       final Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
        animation.setDuration(1000); // duration - 1000 for 1 second and 500 for half a second
        animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
        animation.setRepeatMode(Animation.RELATIVE_TO_SELF); // Reverse animation at the end so the button will fade back in
        textView1.startAnimation(animation);
        delay(2);
        textView1.setVisibility(View.GONE);
        textView2.startAnimation(animation);

        delay(2);
        textView2.setVisibility(View.GONE);
        textView3.startAnimation(animation);

        delay(2);
        textView3.setVisibility(View.GONE);
        textView4.startAnimation(animation);
        delay(2);
        textView4.setVisibility(View.GONE);
        textView5.startAnimation(animation);

        Intent login= new Intent(Splash.this, Login.class);
        startActivity(login);
    }

    void delay(int sec){
        try {

            Thread.currentThread().sleep(1000*sec);
        }catch(Exception ex){}

    }
}
