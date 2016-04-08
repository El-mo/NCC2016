package aitp.simpleapp.DataLayer;


import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import aitp.simpleapp.Models.SerialObject;

public class InternalStorage {
    public static String LISTKEY = "LISTKEY";

    public InternalStorage() {}

    public static void appendItem(Context context, SerialObject item){
        ArrayList<SerialObject> arrayList = readItems(context);
        if(!arrayList.contains(item)){
            arrayList.add(item);
            writeItems(context, arrayList);
        }
    }

    public static void writeItems(Context context, ArrayList<SerialObject> arrayList) {
        InternalStorage.writeObject(context, LISTKEY, arrayList);
    }

    public static ArrayList<SerialObject> readItems(Context context){
        return readList(context, LISTKEY);
    }
    public static SerialObject getItemAtIndex(Context context, int index){
        return readItems(context).get(index);
    }

    public static void setItem(Context context, int index, SerialObject item){
        ArrayList<SerialObject> list = readItems(context);
        list.set(index, item);
        writeItems(context, list);
    }

    public static void writeObject(Context context, String key, Object object) {
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(key, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(object);
            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Object readObject(Context context, String key) {
        try {
            FileInputStream fis = context.openFileInput(key);
            ObjectInputStream ois = new ObjectInputStream(fis);
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<SerialObject> readList(Context context, String key){
        ArrayList<SerialObject> arrayList;

        arrayList = (ArrayList<SerialObject>) InternalStorage.readObject(context, key);
        if(arrayList==null)
            arrayList = new ArrayList<>();

        return arrayList;
    }



}
