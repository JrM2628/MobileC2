package c2.mobile.mobilec2;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class C2ClientThread extends Thread {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public C2ClientThread(Socket socket) {
        super("C2ClientThread");
        this.socket = socket;
        try {
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while(true){
            String str = null;
            try {
                str = in.readLine();
                if(str != null){
                    Log.i("received response from server", str);
                    out.println("Echo: " + str);
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
        }
    }
}
