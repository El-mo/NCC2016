package aitp.simpleapp.Helper;


import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Parcel;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;

public class LocationHelper implements Serializable {
    byte[] location;
    double latitude, longitude;
    String[] address;

    public LocationHelper(){}
    public LocationHelper(Location location, Context context){
        setLocation(location, context);
    }

    public Location getLocation() {
        Parcel p = Parcel.obtain();
        p.readByteArray(this.location);
        Location location = Location.CREATOR.createFromParcel(p);
        p.recycle();
        return location;
    }

    public void setLocation(Location location, Context context) {
        Parcel p = Parcel.obtain();
        location.writeToParcel(p, 0);
        this.location = p.marshall();
        p.recycle();
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        getAddressFromLocation(latitude, longitude, context);
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String[] getAddress() {
        return address;
    }

    private void getAddressFromLocation(double latitude, double longitude, Context context){
        Geocoder geocoder = new Geocoder(context, Locale.ENGLISH);
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

            if(addresses != null) {
                Address returnedAddress = addresses.get(0);
                address = new String[returnedAddress.getMaxAddressLineIndex()];
                for(int i=0; i<returnedAddress.getMaxAddressLineIndex(); i++) {
                    address[i] = returnedAddress.getAddressLine(i);
                }
            }
            else{
                address = new String[] {"Cannot find location"};
            }
        } catch (IOException e) {
            e.printStackTrace();
            address =new String[] {"Cannot find location"};
        }

    }
}
