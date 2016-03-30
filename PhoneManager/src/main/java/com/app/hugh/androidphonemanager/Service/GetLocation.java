package com.app.hugh.androidphonemanager.Service;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.app.hugh.androidphonemanager.Application.DataShareApplication;

/**
 * Created by hs on 2016/3/29.
 */
public class GetLocation extends Service{
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        LocationManager mLM = (LocationManager) getSystemService(LOCATION_SERVICE);
        mLM.requestLocationUpdates("gps",0,0,new CustomizeLocationListener());
    }

    class CustomizeLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            DataShareApplication.savedata("longitude",longitude+"");
            DataShareApplication.savedata("latitude",latitude+"");

            Log.e("onLocationChanged",latitude+"");
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }


}
