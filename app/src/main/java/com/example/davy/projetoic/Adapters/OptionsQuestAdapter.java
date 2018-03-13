package com.example.davy.projetoic.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.example.davy.projetoic.R;

import java.util.ArrayList;

public class OptionsQuestAdapter extends BaseAdapter {
    private ArrayList<String> options;
    private Context context;

    //alterar para receber como parametro as opções e cada questão
    public OptionsQuestAdapter(Context context, String optionA, String optionB
                , String optionC, String optionD, String optionE) {
        super();
        this.context = context;
        options = new ArrayList<>();
        options.add("A) "+optionA);
        options.add("B) "+optionB);
        options.add("C) "+optionC);
        options.add("D) "+optionD);
        options.add("E) "+optionE);
    }

    @Override
    public int getCount() {
        return options.size();
    }

    @Override
    public String getItem(int i) {
        return options.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        if(view != null){
            TextView t = (TextView) view.findViewById(R.id.rowTextOption);
            t.setText(options.get(0));
            return view;
        }else {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.row_option, null);
            TextView t = (TextView) v.findViewById(R.id.rowTextOption);
            t.setText(options.get(i));
            return v;
        }
        //return null;
    }
}
