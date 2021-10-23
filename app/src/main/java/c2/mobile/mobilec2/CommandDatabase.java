package c2.mobile.mobilec2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class CommandDatabase extends SQLiteOpenHelper {

    private final SQLiteDatabase dbwrite;
    private final SQLiteDatabase dbread;

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
        dbwrite = this.getWritableDatabase();
        dbread = this.getReadableDatabase();
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

    public boolean insertIntoDatabase(String uuid, String command, String output) {
        ContentValues values = new ContentValues();
        values.put(BotEntryContract.BotEntry.uuid, uuid);
        values.put(BotEntryContract.BotEntry.command, command);
        values.put(BotEntryContract.BotEntry.output, output);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = -1;
        newRowId = dbwrite.insert(BotEntryContract.BotEntry.tablename, null, values);
        Log.e("Database entry:", "Inserted into Bots(" + uuid + ", " + command + ", " + output + ")");
        return newRowId != -1;

    }

    public ArrayList<String> getFromDatabase(String uuid, String type) {
        String[] projection = {
            BotEntryContract.BotEntry.uuid,
            BotEntryContract.BotEntry.command,
            BotEntryContract.BotEntry.output
        };

        String selection = BotEntryContract.BotEntry.uuid + " = ?";
        String[] selectionArgs = { uuid };

        Cursor cursor = dbread.query(
            BotEntryContract.BotEntry.tablename,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            null
        );

        ArrayList<String> itemIds = new ArrayList<>();
        while (cursor.moveToNext()) {
            String itemId = cursor.getString(
                    cursor.getColumnIndexOrThrow(type)
            );
            itemIds.add(itemId);
        }
        cursor.close();
        return itemIds;
    }

    public void updateDatabase(String uuid, String command, String output) {
        // TODO: change this to be json string for cmd and output
        // ArrayList commands = getFromDatabase(command);
        // ArrayList outputs = getFromDatabase(output);
        // String cmd, out = convertToJson(commands, outputs);
        // values.put(BotEntryContract.BotEntry.command, cmd);
        // values.put(BotEntryContract.BotEntry.output, out);

        ContentValues values = new ContentValues();

        // TODO: add implementation for multiple bots
        values.put(BotEntryContract.BotEntry.command, command);
        values.put(BotEntryContract.BotEntry.output, output);

        String selection = BotEntryContract.BotEntry.uuid + " LIKE ?";
        String[] selectionArgs = { uuid };

        int count = dbwrite.update(
            BotEntryContract.BotEntry.tablename,
            values,
            selection,
            selectionArgs
        );
    }
}
