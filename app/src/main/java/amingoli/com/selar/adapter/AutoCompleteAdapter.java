package ir.trano.keeper.adapter;


import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import ir.trano.keeper.R;
import ir.trano.keeper.model.RegionBringer;


public class RegionBringerAdaptor extends ArrayAdapter<RegionBringer> {

    private Context context;
    private int resourceId;
    private List<RegionBringer> items, tempItems, suggestions;

    int selected_region_id = 0;

    public RegionBringerAdaptor(@NonNull Context context, int resourceId, ArrayList<RegionBringer> items, int region_id) {
        super(context, resourceId, items);
        this.items = items;
        this.context = context;
        this.resourceId = resourceId;
        tempItems = new ArrayList<>(items);
        suggestions = new ArrayList<>();
        this.selected_region_id = region_id;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        try {
            if (convertView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                view = inflater.inflate(resourceId, parent, false);
            }
            RegionBringer Region = getItem(position);
            TextView name = (TextView) view.findViewById(R.id.title);
            name.setText(Region.getName() + " (" + Region.getPhone() + ") ");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }
    @Nullable
    @Override
    public RegionBringer getItem(int position) {
        return items.get(position);
    }
    @Override
    public int getCount() {
        return items.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return RegionFilter;
    }

    private Filter RegionFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            RegionBringer Region = (RegionBringer) resultValue;
            return Region.getName();
        }
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults filterResults = new FilterResults();
            synchronized (filterResults) {
                if (charSequence != null) {
                    // Clear and Retrieve the autocomplete results.
                    List<RegionBringer> resultList = getFilteredResults(charSequence);
                    suggestions.addAll(resultList);
                    // Assign the data to the FilterResults
                    filterResults.values = resultList;
                    filterResults.count = resultList.size();
                }
                return filterResults;
            }

        }
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            ArrayList<RegionBringer> tempValues = (ArrayList<RegionBringer>) filterResults.values;
            Log.e("qqqq", "publishResults: " + charSequence + " - " + filterResults.count);
            if (filterResults != null && filterResults.count > 0) {
                clear();
                for (RegionBringer RegionObj : tempValues) {
                    add(RegionObj);
                    //notifyDataSetChanged();
                }
            } else {
                clear();
                addAll(tempItems);
                notifyDataSetChanged();
            }
        }
    };

    private List<RegionBringer> getFilteredResults(CharSequence charSequence) {
        List<RegionBringer> filteredResults = new ArrayList<>();
        for (RegionBringer Region: tempItems) {
            if (Region.getName().toLowerCase().contains(charSequence.toString().toLowerCase()) ) {
                filteredResults.add(Region);
            }
        }
        return  filteredResults;
    }
}

