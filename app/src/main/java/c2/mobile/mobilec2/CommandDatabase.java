package c2.mobile.mobilec2;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.widget.Toast;

public class CommandDatabase extends SQLiteOpenHelper {

    private Context context;

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + BotEntryContract.BotEntry.tablename + " (" +
                    BotEntryContract.BotEntry.uuid + " TEXT PRIMARY KEY," +
                    BotEntryContract.BotEntry.command + " TEXT," +
                    BotEntryContract.BotEntry.output + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + BotEntryContract.BotEntry.tablename;
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "commands.db";

    public CommandDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);

    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
