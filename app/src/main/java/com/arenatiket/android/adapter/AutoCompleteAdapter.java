package com.arenatiket.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.arenatiket.android.R;
import com.arenatiket.android.model.AirPort;

import java.util.ArrayList;

public class AutoCompleteAdapter extends ArrayAdapter<AirPort> {
    private final String MY_DEBUG_TAG = "AutoCompleteAdapter";
    private ArrayList<AirPort> items;
    private ArrayList<AirPort> itemsAll;
    private ArrayList<AirPort> suggestions;
    private int viewResourceId;

    public AutoCompleteAdapter(Context context, int viewResourceId, ArrayList<AirPort> items) {
        super(context, viewResourceId, items);
        this.items = items;
        this.itemsAll = (ArrayList<AirPort>) items.clone();
        this.suggestions = new ArrayList<AirPort>();
        this.viewResourceId = viewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(viewResourceId, null);
        }
        AirPort port = items.get(position);
        if (port != null) {
            TextView codeNameLabel = (TextView) v.findViewById(R.id.codeTextView);
            TextView portNameLabel = (TextView) v.findViewById(R.id.nameTextView);
            if (portNameLabel != null) {
//              Log.i(MY_DEBUG_TAG, "getView AirPort Name:"+port.getName());
                codeNameLabel.setText(port.getCode());
                portNameLabel.setText(port.getCity());
            }
        }
        return v;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    Filter nameFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            String str = ((AirPort) (resultValue)).getName();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (AirPort port : itemsAll) {
                    if (port.getCity().toLowerCase().contains(constraint.toString().toLowerCase())
                                                                                                                                                                                                                                                                                                                                                                                    || port.getCode().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(port);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<AirPort> filteredList = (ArrayList<AirPort>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (AirPort c : filteredList) {
                    add(c);
                }
                notifyDataSetChanged();
            }
        }
    };

}