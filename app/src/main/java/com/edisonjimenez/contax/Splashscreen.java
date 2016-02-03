package com.edisonjimenez.contax;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Splashscreen extends Activity {
    private final static String TAG = Splashscreen.class.getSimpleName();
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
    }
    
    @Override
    protected void onResume() {
        super.onResume();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Finish the splash activity so it can't be returned to.
                finish();

                // Create an Intent that will start the main activity.
                Intent mainIntent = new Intent(Splashscreen.this, MainActivity.class);
                startActivity(mainIntent);
            }
        }, 2000);
    }
}
