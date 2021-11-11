package c2.mobile.mobilec2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class DisplayOutput extends AppCompatActivity {
    private String uuid;
    private CommandDatabase commandDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 🌭
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_output);
        TextView uuidDisplay = (TextView) findViewById(R.id.uuidDisplay);
        TextView outputText = (TextView) findViewById(R.id.outputText);

        Intent intent = getIntent();
        this.uuid = intent.getStringExtra("uuid");
        this.commandDatabase = new CommandDatabase(getApplicationContext());
        ArrayList<String> outlist = commandDatabase.getFromDatabase(uuid, BotEntryContract.BotEntry.output);
        Log.e("outlist: ", outlist.get(0));
        Log.e("uuid2: ", uuid);

        if(outlist != null && outlist.size() > 0){
            String output = outlist.get(0);
            uuidDisplay.setText(this.uuid);
            outputText.setText(output);
        }
    }
}