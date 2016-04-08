package aitp.simpleapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;

import aitp.simpleapp.Adapters.ImageListViewAdapter;
import aitp.simpleapp.Helper.ImageHelper;


public class ImageDisplayActivity extends AppCompatActivity {

    ArrayList<ImageHelper> images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);

        Intent i = getIntent();
        images = (ArrayList<ImageHelper>) i.getSerializableExtra(ImageHelper.IMAGEHELPER);


        ListView listView = (ListView) findViewById(R.id.imageList);
        listView.setAdapter(new ImageListViewAdapter(this, this.images));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File f = new File(images.get(position).getImagePath());
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                Uri u = Uri.fromFile(f);
                intent.setDataAndType(u, "image/*");
                startActivity(intent);
            }
        });

        //noinspection ConstantConditions
        getSupportActionBar().setTitle("Images");
    }
}
