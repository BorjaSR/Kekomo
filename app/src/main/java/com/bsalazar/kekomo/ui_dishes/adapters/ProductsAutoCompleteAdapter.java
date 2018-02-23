package com.bsalazar.kekomo.ui_dishes.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.bsalazar.kekomo.data.entities.Product;

import java.util.ArrayList;

/**
 * Created by bsalazar on 12/12/17.
 */

public class ProductsAutoCompleteAdapter extends ArrayAdapter<Product> {

    private Context mContext;
    private ArrayList<Product> products;

    private ArrayList<Product> itemsAll;
    private ArrayList<Product> suggestions;

    public ProductsAutoCompleteAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<Product> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.products = objects;

        itemsAll = (ArrayList<Product>) this.products.clone();
        suggestions = new ArrayList<>();
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);

        TextView text = (TextView) convertView.findViewById(android.R.id.text1);
        text.setText(products.get(position).getName());

        return convertView;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Nullable
    @Override
    public Product getItem(int position) {
        return products.get(position);
    }


    @NonNull
    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    private Filter nameFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            return ((Product)(resultValue)).getName();
        }
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if(constraint != null) {
                suggestions.clear();
                for (Product product : itemsAll) {
                    if(product.getName().toLowerCase().startsWith(constraint.toString().toLowerCase())){
                        suggestions.add(product);
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
            ArrayList<Product> filteredList = (ArrayList<Product>) results.values;
            if(results != null && results.count > 0) {
                clear();
                for (Product c : filteredList) {
                    add(c);
                }

                products = (ArrayList<Product>) filteredList.clone();
                notifyDataSetChanged();
            }
        }
    };
}
