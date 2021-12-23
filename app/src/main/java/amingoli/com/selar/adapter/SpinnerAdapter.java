package amingoli.com.selar.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import amingoli.com.selar.R;
import amingoli.com.selar.model.Spinner;

public class SpinnerAdapter extends BaseAdapter {
    Context context;
    ArrayList<Spinner> regions;
    LayoutInflater inflater;

    public SpinnerAdapter(Context context, ArrayList<Spinner> regions) {
        this.context = context;
        this.regions = regions;
        inflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return regions.size();
    }

    @Override
    public Object getItem(int i) {
        return regions.get(i);
    }

    @Override
    public long getItemId(int i) {
        return regions.get(i).getId();
    }

    @SuppressLint({"ViewHolder", "InflateParams", "SetTextI18n"})
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.item_spinner, null);
        TextView names = (TextView) view.findViewById(R.id.textView);
        if (regions.get(i).getContent() != null && !regions.get(i).getContent().isEmpty()){
            names.setText(regions.get(i).getName() + " ("+regions.get(i).getContent()+")");
        }else {
            names.setText(regions.get(i).getName());
        }
        return view;
    }
}