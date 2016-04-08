package aitp.simpleapp.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import aitp.simpleapp.Models.SerialObject;
import aitp.simpleapp.R;

public class ListViewAdapter extends ArrayAdapter<SerialObject>{

    public ListViewAdapter(Context context, ArrayList<SerialObject> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        SerialObject item = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_row, parent, false);
        }
        // Lookup view for data population
        TextView title = (TextView) convertView.findViewById(R.id.tv_title);
        TextView dateTime = (TextView) convertView.findViewById(R.id.tv_date_time);
        TextView detail = (TextView) convertView.findViewById(R.id.tv_detail);

        String detailStr = item.getDescription();
        if(detailStr.length() > 20){
            detailStr = detailStr.substring(0, 20) + "...";
        }

        title.setText(item.getTitle());
        dateTime.setText(item.getDateString());
        detail.setText(item.getDescription());


        return convertView;
    }
}
