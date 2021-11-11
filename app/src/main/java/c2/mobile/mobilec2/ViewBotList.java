package c2.mobile.mobilec2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.ArrayList;

public class ViewBotList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_view_bot_list);
        ScrollView scrl = new ScrollView(this);
        final LinearLayout ll=new LinearLayout(this);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(100, 500, 100, 200);
        scrl.addView(ll);


        CommandDatabase commandDatabase = new CommandDatabase(getApplicationContext());
        ArrayList<String> uuids = commandDatabase.getUUIDsFromDatabase();
        for(int i = 0; i < uuids.size(); i++){
            String uuid = uuids.get(i);
            Button add_btn=new Button(this);
            add_btn.setText(uuid);
            ll.addView(add_btn, layoutParams);
            final Context context = this;
            add_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra("uuid", uuid);
                    startActivity(intent);
                }
            });
            this.setContentView(scrl);

        }
    }
}