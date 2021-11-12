package c2.mobile.mobilec2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class DisplayOutput extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 🌭
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_output);
        TextView uuidDisplay = (TextView) findViewById(R.id.uuidDisplay);
        TextView outputTextView = (TextView) findViewById(R.id.outputTextView);

        uuidDisplay.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);

        Intent intent = getIntent();
        String uuid = intent.getStringExtra("uuid");
        CommandDatabase commandDatabase = new CommandDatabase(getApplicationContext());
        ArrayList<String> outputList = commandDatabase.getFromDatabase(uuid, BotEntryContract.BotEntry.output);

        if(outputList.size() > 0){
            String output = outputList.get(0);
            uuidDisplay.setText(uuid);
            outputTextView.setText(output);
        }
        else {
            String blah = "testing";
            uuidDisplay.setText(uuid);
            outputTextView.setText(blah);
        }

    }
}