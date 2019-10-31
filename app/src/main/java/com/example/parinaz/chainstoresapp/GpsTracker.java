package com.example.parinaz.chainstoresapp;

import android.Manifest;
import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;


public class GpsTracker extends Service implements LocationListener {


    private Context context;
    private LocationManager locationManager;

    private Location location;
    private double latitude = 0.0;
    private double longitude = 0.0;

    private boolean isGpsEnabled = false;
    private boolean isNetworkEnabled = false;
    private boolean canGetLocation = false;
    Dialog dialog;


    public static final int TIME_BW_UPDATES = 1000 * 10; // 10 seconds
    public static final int MIN_DISTANCE_FOR_UPDATE = 10;     // 10 meter

    public GpsTracker(Context context) {
        this.context = context;
        getLocation();
    }

    public Location getLocation() throws SecurityException {

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGpsEnabled && !isNetworkEnabled) {
            canGetLocation = false;
            // no provider enabled
        } else {
            canGetLocation = true;
            // first , get location using network provider
            if (!hasPermissions()) {
                location = null;

                return location;



            }
            if (isNetworkEnabled) {
                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        TIME_BW_UPDATES,
                        MIN_DISTANCE_FOR_UPDATE,
                        this);
                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                    }
                }
            }

            if (isGpsEnabled && location == null) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        TIME_BW_UPDATES, MIN_DISTANCE_FOR_UPDATE, this);
                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                    }
                }
            }
        }

        return location;
    }

    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }
        return latitude;
    }

    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }
        return longitude;
    }

    public boolean canGetLocation() {

        return this.canGetLocation;
    }

    public boolean hasPermissions() {

        return (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED);
    }

      /*public void showGpsAlertDialog() {
      AlertDialog.Builder builder = new AlertDialog.Builder(context,R.layout.alert_location);

        builder.setTitle("GPS")
                .setMessage("GPS is not enabled.Do you want to go to Settings menu?")
                .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        context.startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.cancel();
                    }
                });
        builder.show();
    }
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.alert_location);
        dialog.setCancelable(false);
        //  dialog.getWindow().getAttributes().windowAnimations = R.style.AlertDialogAnimation;
        Button settings, cancel;
        settings = dialog.findViewById(R.id.settings);
        cancel = dialog.findViewById(R.id.cancel);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }


    public void stopUsingGps() throws SecurityException {
        if (locationManager != null && hasPermissions()) {
            locationManager.removeUpdates(this);
        }
    }*/

    @Override
    public void onLocationChanged(Location location) {


    }
        @Override
        public void onStatusChanged (String s,int i, Bundle bundle){

        }

        @Override
        public void onProviderEnabled (String provider){

        }

        @Override
        public void onProviderDisabled (String provider){


        }

        @Nullable
        @Override
        public IBinder onBind (Intent intent){
            return null;
        }
    }
