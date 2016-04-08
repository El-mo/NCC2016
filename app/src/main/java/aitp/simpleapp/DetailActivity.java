package aitp.simpleapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import aitp.simpleapp.DataLayer.InternalStorage;
import aitp.simpleapp.Helper.ImageHelper;
import aitp.simpleapp.Helper.LocationHelper;
import aitp.simpleapp.Models.SerialObject;

public class DetailActivity extends AppCompatActivity {

    int index;
    SerialObject item;
    EditText etTitle, etDesc;
    TextView tvAddress;
    Button buEditSave;
    boolean formEnabled = false;
    ImageHelper imageFromCam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        index = getIntent().getIntExtra(MainActivity.LISTINDEX, -1);
        if(index != -1){
            //getItemFromStorage();
            item = InternalStorage.getItemAtIndex(this, index);
            fillForm();
        }
        else
            finish();
    }

    // FORM OPERATIONS
    public void fillForm(){
        //TODO fill form logic
        etTitle = (EditText) findViewById(R.id.edit_et_title);
        etDesc = (EditText) findViewById(R.id.edit_et_desc);
        tvAddress = (TextView) findViewById(R.id.edit_tv_address);
        buEditSave = (Button) findViewById(R.id.edit_bu_saveedit);

        etTitle.setText(item.getTitle());
        etDesc.setText(item.getDescription());

        LocationHelper locationHelper = item.getLocation();
        if(locationHelper == null)
            tvAddress.setText("Address: Location not added");
        else{
            String address = "Address: \n";
            for(String addressLine : locationHelper.getAddress()){
                address += addressLine + "\n";
            }
            tvAddress.setText(address);
        }
    }

    public void getFromForm(){
        //TODO Get data from editable views
        item.setTitle(etTitle.getText().toString());
        item.setDescription(etDesc.getText().toString());
    }

    public void toggleForm(){
        if(formEnabled) {
            getFromForm();
            InternalStorage.setItem(this, index, item);
            //saveItemToStorage();
            buEditSave.setText("Save");
        }else {
            buEditSave.setText("Edit");
        }
        formEnabled = !formEnabled;
        etDesc.setEnabled(formEnabled);
        etTitle.setEnabled(formEnabled);
    }

    // BUTTON OnClicks
    public void editSaveButton(View view){
        toggleForm();
        /*
        if(formEnabled)
            saveItemToStorage();
        formEnabled = !formEnabled;
        toggleForm(formEnabled);
        */
    }

    public void seePictures(View view){
        Intent i = new Intent(DetailActivity.this, ImageDisplayActivity.class);
        i.putExtra(ImageHelper.IMAGEHELPER, item.getImages());
        startActivity(i);
    }

    public void takePictureEditForm(View view){
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

    // PHOTOS

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ImageHelper.IMAGE_REQUEST_CODE) {
            if(resultCode == RESULT_OK)
            {
                if(this.imageFromCam != null) {
                    item.addImage(imageFromCam);
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

    //MENU (SHARE)

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //TODO: EDIT Share Info
        switch (item.getItemId()) {
            case R.id.share_detail:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Check out " + item.getTitle() + " at AITP NCC!\n" +
                                "Follow AITP on twitter! https://twitter.com/AITP2013");
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "Share Event"));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
