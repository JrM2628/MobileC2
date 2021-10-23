package c2.mobile.mobilec2;

import android.provider.BaseColumns;

public class BotEntryContract {
    private BotEntryContract(){}
    public static class BotEntry implements BaseColumns {
        public static final String tablename = "bots";
        public static final String uuid = "uuid";
        public static final String command = "command";
        public static final String output = "output";
    }
}
