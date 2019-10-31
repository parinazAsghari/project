package com.example.parinaz.chainstoresapp.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.example.parinaz.chainstoresapp.R;

public class SplashActivity extends AppCompatActivity {
    private Handler handler;
    //TextView splash_tv;
    //ImageView splash_image;
    Dialog dialog;
    private static final int PERMISSION_REQ_CODE = 1234;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

     //   splash_tv = (TextView) findViewById(R.id.splash_tv);
       // splash_image = (ImageView) findViewById(R.id.splash_image);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

      //  Animation animation = AnimationUtils.loadAnimation(this, R.anim.move_from_top);
       // splash_tv.setAnimation(animation);

        if (!hasPermissions()){
            ActivityCompat.requestPermissions(SplashActivity.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQ_CODE);

        }
        else{
            InternetAvailable();
        }



     //   InternetAvailable();

     /*   handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        } , 3000);*/
    }

    public boolean hasPermissions() {

        return (ActivityCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQ_CODE :{
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // camera related task you need to do.
                    InternetAvailable();
                } else {
                    finish();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }


        }
    }


    public void InternetAvailable(){
        //if(hasInternetConnection()){

                handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, 5000);
            }

       // }
       /* else{
            dialog = new Dialog(SplashActivity.this);
            dialog.setContentView(R.layout.alert_dialog_no_network);
            dialog.setCancelable(false);
            dialog.getWindow().getAttributes().windowAnimations = R.style.AlertDialogAnimation;
            Button tryAgain , exit ;
            tryAgain = dialog.findViewById(R.id.try_again);
            exit = dialog.findViewById(R.id.exit);
            tryAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    InternetAvailable();
                }
            });
            exit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    finish();
                }
            });

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        }*/


    //internet connection
    public Boolean hasInternetConnection() {
        ConnectivityManager connection = (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connection.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifi != null && wifi.isConnected())
            return true;
        NetworkInfo mobileNetwork = connection.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetwork != null && mobileNetwork.isConnected())
            return true;
        NetworkInfo activeNetwork = connection.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected())
            return true;
        return false;
    }
}