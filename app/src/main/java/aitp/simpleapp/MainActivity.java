package aitp.simpleapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import aitp.simpleapp.Adapters.ListViewAdapter;
import aitp.simpleapp.Callbacks.AlertCallBack;
import aitp.simpleapp.DataLayer.InternalStorage;
import aitp.simpleapp.Helper.AlertHelper;
import aitp.simpleapp.Models.SerialObject;
import aitp.simpleapp.Models.SortComparator;

public class MainActivity extends AppCompatActivity implements AlertCallBack, LocationListener {

    public static final String DEBUGSTR = "DBLog";
    public static final String LISTINDEX = "INDEXOFLIST";

    ArrayList<SerialObject> list;
    ArrayList<SerialObject> display;
    int deleteListIndex = -1;

    LocationManager locationManager;
    String provider;
    Location location;
    ArrayList<String> filterOptions = new ArrayList<>();
    ArrayList<String> sortOptions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //set SortOptions
        sortOptions.add("Date");
        sortOptions.add("Title");
        sortOptions.add("Type");
        //set filterOptions
        filterOptions.add("None");
        filterOptions.add("Favorites");
        filterOptions.add("Type");

        retrieveUserData();
        setUpList();
    }

    private void retrieveUserData() {
        //list = (ArrayList<SerialObject>) InternalStorage.readList(this, InternalStorage.LISTKEY);
        list = InternalStorage.readItems(this);
        display = list;
        if(list.size() == 0)
            new AlertHelper(this, this).makeAlertBoolean("There are no items yet. Would you like to add one?");
    }

    //LIST MANAGEMENT

    private void setUpList() {
        ListView listView = (ListView) findViewById(R.id.main_list);
        listView.setAdapter(new ListViewAdapter(this, display));

        //Add onPress listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startDetailActivity(position);
            }
        });
        //Add onLongPress listener
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                new AlertHelper(MainActivity.this, MainActivity.this).makeAlertBoolean("Delete " + display.get(position).getTitle() + "?");
                setForDeletion(position);
                return true;
            }
        });
    }

    public void saveList(){
        InternalStorage.writeItems(this, list);
    }

    public void setForDeletion(int displayListIndex){
        SerialObject item = display.get(displayListIndex);
        deleteListIndex = list.indexOf(item);
    }

    public void executeDelete(){
        if(deleteListIndex != -1) list.remove(deleteListIndex);
        saveList();
        setUpList();
    }

    @Override
    public void alertHelperTextResponse(String title, String response) {}
    @Override
    public void alertHelperBooleanResponse(String title, Boolean response) {
        if(title.contains("Delete") && response)
            executeDelete();
        else if(title.contains("Delete") && !response){
            deleteListIndex = -1;
        }
        else if(title.contains("There are no items yet") && response) startAddActivity();
    }

    //START ACTIVITIES

    public void startDetailActivity(int index){
        Intent i = new Intent(MainActivity.this, DetailActivity.class);
        i.putExtra(LISTINDEX, index); //nice, only passing an int.
        startActivity(i);
    }

    public void startAddActivity(){
        Intent i = new Intent(MainActivity.this, AddActivity.class);
        startActivity(i);
    }

    //MENU FUNCTIONS

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        //Search
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        runSearch(newText);
                        return false;
                    }
                });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filter:
                showFilterDialog();
                return true;
            case R.id.sort:
                showSortDialog();
                return true;
            case R.id.add_button:
                startAddActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Filter By:");

        //TODO Add filter by options
        /*
            for(SerialObject item: list){
                filterOptions.add(item.getType());
            }
         */
        String[] types =  new String[filterOptions.size()];
        types = filterOptions.toArray(types);

        builder.setItems(types, new AlertDialog.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                filterBy(filterOptions.get(which));
            }
        });
        builder.show();
    }

    private void filterBy(String filterStr) {
        //TODO: Set Filter Logic
        this.display = new ArrayList<>();
        switch (filterStr) {
            case "None":
                this.display = this.list;
                //noinspection ConstantConditions
                getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
                break;
            /*case "Favorites":
                for (SerialObject item : this.list) {
                    if (item.isFavorite())
                        display.add(item);
                }
                //noinspection ConstantConditions
                getSupportActionBar().setTitle("My Favorites");
                break;*/
            default:
                /*
                for (SerialObject item : this.list) {
                    if (item.getType().equals(filterStr))
                        display.add(item);
                }
                //noinspection ConstantConditions
                getSupportActionBar().setTitle(filterStr);
                */
                break;
        }
        setUpList();
    }

    private void runSearch(String query) {
        if(query.isEmpty()){
            this.display = this.list;
        }
        else{
            this.display = new ArrayList<>();
            for(SerialObject item : this.list){
                //TODO Update with search properties
                if(item.getTitle().toLowerCase().contains(query.toLowerCase()) || item.getDescription().toLowerCase().contains(query.toLowerCase())){
                    display.add(item);
                }
            }
        }
        setUpList();
    }

    private void showSortDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sort By:");
        //TODO sortOptions
        String[] types =  new String[sortOptions.size()];
        types = sortOptions.toArray(types);
        builder.setItems(types, new AlertDialog.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                SortComparator comp = new SortComparator();
                switch (sortOptions.get(which)) {
                    case "Date":
                        comp.setCompareToDate();
                        Collections.sort(display, comp);
                        break;
                    case "Title":
                        comp.setCompareToTitle();
                        Collections.sort(display, comp);
                        break;
                    case "Type":
                        comp.setCompareToType();
                        Collections.sort(display, comp);
                        break;
                    case "location":
                        if(getCurrentLocation())
                            calculateDistances();
                            comp.setCompareToLocation();
                            Collections.sort(display, comp);
                        break;
                }
                setUpList();
            }
        });
        builder.show();
    }

    private void calculateDistances() {
        if(list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setDistanceTo(location.distanceTo(list.get(i).getLocation().getLocation()));
            }
        }

    }

    private boolean getCurrentLocation() {
        if (setUpLocationListener()) {
            //Android required line...
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {            }
            location = locationManager.getLastKnownLocation(provider);
            // Check if last known location set
            if (location != null) {
                Toast.makeText(this, "Location Added!", Toast.LENGTH_LONG).show();
                return true;
            } else {
                // if last location not already recorded, force location lookup
                provider = locationManager.getBestProvider(new Criteria(), false);
                locationManager.requestSingleUpdate(provider, this, null);
                location = locationManager.getLastKnownLocation(provider);
                if (location != null) {
                    Toast.makeText(this, "Location Added!", Toast.LENGTH_LONG).show();
                    return true;
                } else {
                    Toast.makeText(this, "Location Not Found", Toast.LENGTH_LONG).show();
                    return false;
                }
            }
        }
        else{
            return false;
        }
    }

    //LIFECYCLE

    @Override
    protected void onStop() {
        super.onStop();
        saveList();
    }

    @Override
    public void onResume(){
        super.onResume();
        retrieveUserData();
        setUpList();
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
