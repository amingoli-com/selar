package amingoli.com.selar.adapter;


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

import amingoli.com.selar.R;
import amingoli.com.selar.model.Spinner;


public class AutoCompleteAdapter extends ArrayAdapter<Spinner> {

    private Context context;
    private int resourceId;
    private List<Spinner> items, tempItems, suggestions;
    private int selected_region_id = 0;
    private boolean showAllItemWhenNull = false;

    public AutoCompleteAdapter(@NonNull Context context, int resourceId, ArrayList<Spinner> items, int region_id, boolean showAllItemWhenNull) {
        super(context, resourceId, items);
        this.items = items;
        this.context = context;
        this.resourceId = resourceId;
        tempItems = new ArrayList<>(items);
        suggestions = new ArrayList<>();
        this.selected_region_id = region_id;
        this.showAllItemWhenNull = showAllItemWhenNull;
    }

    public AutoCompleteAdapter(@NonNull Context context, ArrayList<Spinner> items, boolean showAllItemWhenNull) {
        super(context, R.layout.item_list, items);
        this.items = items;
        this.context = context;
        this.resourceId = R.layout.item_list;
        tempItems = new ArrayList<>(items);
        suggestions = new ArrayList<>();
        this.showAllItemWhenNull = showAllItemWhenNull;
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
            Spinner Spinner = getItem(position);
            TextView name = (TextView) view.findViewById(R.id.title);
            if (Spinner.getContent() == null || Spinner.getContent().isEmpty()){
                name.setText(Spinner.getName());
            }else {
                name.setText(Spinner.getName() + " (" + Spinner.getContent() + ") ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }
    @Nullable
    @Override
    public Spinner getItem(int position) {
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
            Spinner Region = (Spinner) resultValue;
            return Region.getName();
        }
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults filterResults = new FilterResults();
            synchronized (filterResults) {
                if (charSequence != null) {
                    // Clear and Retrieve the autocomplete results.
                    List<Spinner> resultList = getFilteredResults(charSequence);
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
            ArrayList<Spinner> tempValues = (ArrayList<Spinner>) filterResults.values;
            Log.e("qqqq", "publishResults: " + charSequence + " - " + filterResults.count);
            if (filterResults != null && filterResults.count > 0) {
                clear();
                for (Spinner RegionObj : tempValues) {
                    add(RegionObj);
                    //notifyDataSetChanged();
                }
            } else if (showAllItemWhenNull){
                clear();
                addAll(tempItems);
                notifyDataSetChanged();
            }
        }
    };

    private List<Spinner> getFilteredResults(CharSequence charSequence) {
        List<Spinner> filteredResults = new ArrayList<>();
        for (Spinner Region: tempItems) {
            if (Region.getName().toLowerCase().contains(charSequence.toString().toLowerCase()) ) {
                filteredResults.add(Region);
            }
        }
        return  filteredResults;
    }
}

