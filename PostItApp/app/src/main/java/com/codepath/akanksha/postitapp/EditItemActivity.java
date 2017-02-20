package com.codepath.akanksha.postitapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {

    String valueToEdit = null;
    int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        Button btnSave =  (Button) findViewById(R.id.btnSave);

        //retrieving parameters passed from calling method
        valueToEdit = getIntent().getStringExtra("valueToEdit");
        pos = getIntent().getIntExtra("position", 0);

        EditText editedValue = (EditText) findViewById(R.id.etEditedValue);
        editedValue.setText(valueToEdit);
        //setting cursor value to end of the current text
        editedValue.setSelection(editedValue.getText().length());

        //calling listener on Save action
        btnSave.setOnClickListener(onSaveListener);
    }

    /* Setting listener to save the edited list value and send new value back to calling activity */
    private OnClickListener onSaveListener = new OnClickListener() {
        @Override
        public void onClick(View v){
            EditText newVal = (EditText) findViewById(R.id.etEditedValue);
            String resultNewValue = newVal.getText().toString();

            Intent resultData = new Intent();
            resultData.putExtra("resultData" , resultNewValue);
            resultData.putExtra("resultPosition", pos);
            resultData.putExtra("oldValue", valueToEdit);

            setResult(RESULT_OK, resultData);
            finish();
        }
    };

}
