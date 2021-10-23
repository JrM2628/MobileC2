package c2.mobile.mobilec2;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class CommandDatabase {
    private SQLiteDatabase database;

    public CommandDatabase(){
        this.database = openOrCreateDatabase("DemoDataBase", Context.MODE_PRIVATE, null);
        this.database.execSQL("CREATE TABLE IF NOT EXISTS demoTable(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, name VARCHAR, phone_number VARCHAR, subject VARCHAR);");
    }
}
