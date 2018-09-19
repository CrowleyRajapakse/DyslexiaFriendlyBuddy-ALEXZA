package com.fsociety2.dyslexiafriendlybuddy;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

class CustomSpinnerAdapter  extends ArrayAdapter {
    private Context context;
    private List<String> itemList;
    public CustomSpinnerAdapter(Context context, int textViewResourceId,List<String> itemList) {

        super(context, textViewResourceId,itemList);
        this.context=context;
        this.itemList=itemList;
    }

    public TextView getView(int position, View convertView, ViewGroup parent) {

        TextView v = (TextView) super
                .getView(position, convertView, parent);
        Typeface myTypeFace = Typeface.createFromAsset(context.getAssets(),
                "fonts/"+ itemList.get(position)+".ttf");
        v.setTypeface(myTypeFace);
        v.setText(itemList.get(position));
        return v;
    }

    public TextView getDropDownView(int position, View convertView,
                                    ViewGroup parent) {

        TextView v = (TextView) super
                .getView(position, convertView, parent);
        Typeface myTypeFace = Typeface.createFromAsset(context.getAssets(),
                "fonts/"+ itemList.get(position)+".ttf");
        v.setTypeface(myTypeFace);
        v.setText(itemList.get(position));
        return v;
    }

}


