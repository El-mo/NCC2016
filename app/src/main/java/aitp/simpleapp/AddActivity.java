package aitp.simpleapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import aitp.simpleapp.DataLayer.InternalStorage;
import aitp.simpleapp.Helper.ImageHelper;
import aitp.simpleapp.Models.SerialObject;

public class AddActivity extends AppCompatActivity implements LocationListener {

    SerialObject itemToAdd;
    ImageHelper imageFromCam;

    LocationManager locationManager;
    String provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        itemToAdd = new SerialObject();
    }

    //FORM DATA
    public void saveAddFormToItem() {
        //TODO get values from form that are editable
        itemToAdd.setTitle(((TextView) findViewById(R.id.add_et_title)).getText().toString());
        itemToAdd.setDescription(((TextView) findViewById(R.id.add_et_desc)).getText().toString());
    }

    public void saveItemToStorage() {
        ArrayList<SerialObject> itemList = (ArrayList<SerialObject>) InternalStorage.readList(this, InternalStorage.LISTKEY);
        itemList.add(itemToAdd);
        InternalStorage.writeObject(this, InternalStorage.LISTKEY, itemList);
        //InternalStorage.appendItem(this, itemToAdd);
    }

    //BUTTON OnClicks
    public void addItemFromAddForm(View view) {
        saveAddFormToItem();
        InternalStorage.appendItem(this, itemToAdd);
        finish();
    }

    public void addLocationFromAddForm(View view) {
        if (setUpLocationListener()) {
            //Android required line...
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {            }
            Location location = locationManager.getLastKnownLocation(provider);
            // Check if last known location set
            if (location != null) {
                itemToAdd.setLocation(location, this);
                Toast.makeText(this, "Location Added!", Toast.LENGTH_LONG).show();
            } else {
                // if last location not already recorded, force location lookup
                provider = locationManager.getBestProvider(new Criteria(), false);
                locationManager.requestSingleUpdate(provider, this, null);
                location = locationManager.getLastKnownLocation(provider);
                if (location != null) {
                    itemToAdd.setLocation(location, this);
                    Toast.makeText(this, "Location Added!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Location Not Found", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void takePhotoFromAddForm(View view){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            this.imageFromCam = new ImageHelper();
            try {
                photoFile = imageFromCam.createImageFile();
            } catch (IOException ex) {
                Log.d(MainActivity.DEBUGSTR, "Create file error");
            }
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, ImageHelper.IMAGE_REQUEST_CODE);
            }
        }
    }

    //PHOTO

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ImageHelper.IMAGE_REQUEST_CODE) {
            if(resultCode == RESULT_OK)
            {
                if(this.imageFromCam != null) {
                    itemToAdd.addImage(imageFromCam);
                    Toast.makeText(this, "Image Saved", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(this, "Error Saving Image", Toast.LENGTH_LONG).show();
                }
            }
            else {
                Toast.makeText(this, "Image Not Added", Toast.LENGTH_LONG).show();
                imageFromCam = null;
            }
        }
    }

    // LOCATION
    public boolean setUpLocationListener(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // permission granted
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            provider = locationManager.getBestProvider(new Criteria(), false);
            locationManager.requestLocationUpdates(provider, 400, 1, this);
            return true;
        }
        else {
            Toast.makeText(this, "Location Not Enabled", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    @Override
    public void onLocationChanged(Location location) {

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
