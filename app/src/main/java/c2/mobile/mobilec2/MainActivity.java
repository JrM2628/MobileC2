package c2.mobile.mobilec2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLConnection;

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

    public void DBCreate(){
        SQLiteDatabase SQLITEDATABASE = openOrCreateDatabase("DemoDataBase", Context.MODE_PRIVATE, null);
        SQLITEDATABASE.execSQL("CREATE TABLE IF NOT EXISTS demoTable(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, name VARCHAR, phone_number VARCHAR, subject VARCHAR);");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Thread(conn).start();

        EditText command = (EditText)findViewById(R.id.command);
        Button getCommand = (Button)findViewById(R.id.GetCommand);
        getCommand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBCreate();
                SubmitData2SQLiteDB();
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