package com.codepath.akanksha.postitapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import java.util.ArrayList;

/**
 * Created by Akanksha on 2/21/17.
 */

public class PostsAdapter extends ArrayAdapter<String> {

    public PostsAdapter(Context context, ArrayList<String> posts) {
        super(context, android.R.layout.simple_list_item_1 , posts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //Posts post = getItem(position);

        String post = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        EditText toAddValue = (EditText) convertView.findViewById(R.id.etValues);
        toAddValue.setText(post);

        return convertView;
    }
}