package aitp.simpleapp.Models;

import android.content.Context;
import android.location.Location;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import aitp.simpleapp.Helper.ImageHelper;
import aitp.simpleapp.Helper.LocationHelper;

public class SerialObject implements Serializable {

    String title = "";
    String description = "";
    String type = "";
    Date date = new Date();
    LocationHelper location;
    float distanceTo = -1;

    ArrayList<ImageHelper> images = new ArrayList<>();

    public SerialObject(){}

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Date getDate() {
        return date;
    }
    public String getDateString(){
        return DateFormat.getDateTimeInstance().format(date.getTime());
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public LocationHelper getLocation() {
        return location;
    }
    public void setLocation(Location location, Context context) {
        this.location = new LocationHelper(location, context);
    }
    public ArrayList<ImageHelper> getImages() {
        return images;
    }
    public void addImage(ImageHelper image) {if(!images.contains(image)) images.add(image);}
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setLocation(LocationHelper location) {
        this.location = location;
    }
    public float getDistanceTo() {
        return distanceTo;
    }
    public void setDistanceTo(float distanceTo) {
        this.distanceTo = distanceTo;
    }
    public void setImages(ArrayList<ImageHelper> images) {
        this.images = images;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SerialObject))
            return false;
        if (obj == this)
            return true;

        SerialObject otherObject = (SerialObject) obj;
        return (title.equals(otherObject.getTitle()) && (description.equals(otherObject.getDescription())));
    }

}
