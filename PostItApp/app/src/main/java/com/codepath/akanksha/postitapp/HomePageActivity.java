package com.codepath.akanksha.postitapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class HomePageActivity extends AppCompatActivity {

    private final int REQUEST_CODE = 20;
    ArrayList <String> values;
    ArrayAdapter <String> valuesAdapter;
    ListView lvValues ;

    DatabaseHelper databaseHelper = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        lvValues = (ListView) findViewById(R.id.lvValues);
        //values = new ArrayList<String>();

        //readItems();
        values = databaseHelper.readExistingValues();
        valuesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, values);

        lvValues.setAdapter(valuesAdapter);

        //setting up listener for deleting list values on long click
        setupListViewListener();

        //setting up listener to edit list values on short click
        setupListEditListener();
    }

    /*  Adding new values to list and updating the records file after every addition  */
    public void addNewValues(View view){
        EditText etValues = (EditText) findViewById(R.id.etValues);
        String newValue = etValues.getText().toString();
        valuesAdapter.add(newValue);
        etValues.setText("");

        //writeItems();
        databaseHelper.addValues(newValue);
    }

    /*  Setting up listener to edit list value on short click by calling edit activity */
    private void setupListEditListener(){
        lvValues.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View item, int pos, long id ){
                        Intent i = new Intent(HomePageActivity.this, EditItemActivity.class);
                        i.putExtra("valueToEdit", ((TextView) item).getText().toString());
                        i.putExtra("position", pos);
                        startActivityForResult(i, REQUEST_CODE);
                    }
                }
        );
    }

    /*  Retrieving the results from edit activity and adding the edited value to existing list  */
    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent resultData){
        String editedValue = null;
        int editedPosition;
        String oldValue = null;

        if(reqCode== REQUEST_CODE && resCode == RESULT_OK){
            editedValue = resultData.getExtras().getString("resultData");
            editedPosition = resultData.getExtras().getInt("resultPosition");
            oldValue = resultData.getExtras().getString("oldValue");

            if(null != editedValue) {
                values.remove(editedPosition);
                values.add(editedPosition, editedValue);
                valuesAdapter.notifyDataSetChanged();
                //writeItems();

                databaseHelper.updateValues(oldValue, editedValue);
            }
        }
    }

    /* Setting up listener for deleting items from the list and updating the records file after deletion */
    private void setupListViewListener(){
        lvValues.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener(){
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View item, int pos, long id){
                        values.remove(pos);
                        valuesAdapter.notifyDataSetChanged();
                        //writeItems();
                        String deletedValue = ((TextView) item).getText().toString();

                        databaseHelper.deleteValues(deletedValue);
                        valuesAdapter.notifyDataSetChanged();
                        return true;
                    }
                }
        );
    }

    /* Reading items from records file to prepopulate the list */
    private void readItems(){
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");

        try{
            values = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch (IOException e){
            values = new ArrayList<String>();
        }
    }

    /* Writing items to records file to store the list records */
    private void writeItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");

        try {
            FileUtils.writeLines(todoFile, values);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}