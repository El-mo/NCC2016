package aitp.simpleapp.Helper;


import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageHelper implements Serializable{
    public static final int IMAGE_REQUEST_CODE = 100;
    public static final String IMAGEHELPER = "IMAGEHELPER";

    String name, imagePath;

    public ImageHelper(){ }

    public File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        this.name = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                this.name,      /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        this.imagePath = image.getAbsolutePath();
        return image;
    }


    public String getName() {
        return name;
    }

    public String getImagePath() {
        return imagePath;
    }



    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ImageHelper))
        return false;
        if (obj == this)
            return true;

        ImageHelper imageHelper = (ImageHelper) obj;
        return (name.equals(imageHelper.getName()) && (imagePath.equals(imageHelper.getImagePath())));
    }
}
