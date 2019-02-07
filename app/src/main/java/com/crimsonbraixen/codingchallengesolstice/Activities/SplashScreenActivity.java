package com.crimsonbraixen.codingchallengesolstice.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.crimsonbraixen.codingchallengesolstice.R;
import com.crimsonbraixen.codingchallengesolstice.Utils;

/**
 * Activity to show a splash screen with a contacts logo while the data to show is fetched.
 */
public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        //Make call to AsyncTask to fetch the contacts data
        new Utils.AsyncFetch(getApplicationContext(), Utils.CONTACTS, Utils.URL, null).execute();
    }
}