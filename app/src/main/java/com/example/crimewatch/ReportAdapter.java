package com.example.crimewatch;


import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.crimewatch.Report; // Adjust import path if needed

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

public class ReportAdapter extends ArrayAdapter<Report> {

    private Context context;
    private List<Report> reports;

    public ReportAdapter(Context context, List<Report> reports) {
        super(context, 0, reports);
        this.context = context;
        this.reports = reports;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.report_item, parent, false);
        }

        Report report = reports.get(position);

        TextView timestampTextView = convertView.findViewById(R.id.timestampTextView);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // Example format
        String formattedTimestamp = sdf.format(report.getTimestamp().toDate());
        timestampTextView.setText(formattedTimestamp);

        // Format and display location if needed (e.g., using a library or custom logic)
        TextView locationTextView = convertView.findViewById(R.id.locationTextView);
        // ... inside your ReportAdapter's getView method
        Geocoder geocoder = new Geocoder(context);
        try {
            List<Address> addresses = geocoder.getFromLocation(report.getLocation().getLatitude(), report.getLocation().getLongitude(), 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                // Use address.getLocality() for city, address.getThoroughfare() for street, etc.
                String locationName = address.getLocality() + ", " + address.getCountryName();
                locationTextView.setText(locationName);
            } else {
                // Handle cases where no address is found (e.g., use approximate coordinates or display a generic message)
            }
        } catch (IOException e) {
            // Handle errors
        }

        TextView descTextView = convertView.findViewById(R.id.descTextView);
        descTextView.setText(report.getDesc());

        TextView userIdTextView = convertView.findViewById(R.id.userIdTextView);
        userIdTextView.setText(report.getUserId());

        return convertView;
    }
}
