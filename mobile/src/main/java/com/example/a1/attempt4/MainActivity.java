package com.example.a1.attempt4;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;



/////////////////////////

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kaazing.net.sse.SseEventSourceFactory;
import org.kaazing.net.sse.SseEventSource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.kaazing.net.sse.SseEventReader;
import org.kaazing.net.sse.SseEventType;
import org.kaazing.net.sse.impl.SseEventReaderImpl;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataItem;
//import org.kaazing.net.sse.impl.legacy.EventSource;

/******************************************************************/
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.MessageApi.SendMessageResult;
import android.util.Log;
/******************************************************************/
//////////

public class MainActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, MessageApi.MessageListener{
    private static final String TAG = "MainActivity";
    private Button sseSwitch;

    boolean connection = false;

    String deviceID = "Nope!";
    String token = "Not gonna tell you";
    String base = "https://api.particle.io/v1/devices/";
    String SSEcall = "/events/?access_token=";
    TextView progress, txtDisplay1, txtDisplay2;
    ImageView image;
    //
    SseEventSource eventSource = null;
    //phone to watch
    private static final String START_ACTIVITY = "/start_activity";
    private static final String WEAR_MESSAGE_PATH = "/message";
    //watch to phone
    private static final String START_ACTIVITY2 = "/start_activity2";
    private static final String WEAR_MESSAGE_PATH2 = "/message2";

    private GoogleApiClient mGoogleApiClient;
    /////
    ///


    /////
    @Override
    public void onConnected(Bundle bundle) {
        Wearable.MessageApi.addListener(mGoogleApiClient, this);
        sendMessage(START_ACTIVITY, "");

    }


    @Override
    public void onConnectionSuspended(int i) {
      //  Log.i("mobile", "Connection Suspended");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.disconnect();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
/******************************************************************/
         init();
         initGoogleApiClient();
/******************************************************************/

    }
    private void init() {


        sseSwitch = (Button) findViewById(R.id.connect);
        txtDisplay1 = (TextView) findViewById(R.id.TextView1);
        txtDisplay2 = (TextView) findViewById(R.id.TextView2);
        image = (ImageView) findViewById(R.id.imageView);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        sseSwitch.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (connection == false) {
                    txtDisplay2.setText("button pressed");
                    Con con = new Con();
                    con.execute(base + deviceID + SSEcall + token);
                    sseSwitch.setText("Connected");
                    connection = true;

                } else {
                    DisCon discon = new DisCon();
                    discon.execute();
                    sseSwitch.setText("Disconnected");
                    connection = false;
                }
            }
        });
    }
    private void initGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder( this )
                .addApi( Wearable.API )
                .addConnectionCallbacks( this )
                .build();

        if( mGoogleApiClient != null && !( mGoogleApiClient.isConnected() || mGoogleApiClient.isConnecting() ) )
            mGoogleApiClient.connect();
    //    mGoogleApiClient.connect();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    /******************************************************************/
    public void onMessageReceived( final MessageEvent messageEvent ) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (messageEvent.getPath().equalsIgnoreCase(WEAR_MESSAGE_PATH2)) {

                    if (messageEvent.getData() != null) {
                        String in = new String(messageEvent.getData());
                        txtDisplay1.setText(in);
//                        MakeConnection m = new MakeConnection();
//                        /***********/
//                        m.execute(in);
                          PostCon(in);
                    }
                }
            }
        });
    }
    /******************************************************************/
    class Con extends AsyncTask<String, String, Void> {

        @Override
        protected void onProgressUpdate(String... values) {
           // txtDisplay1.setText(values[0]);
try {
    JSONObject json = new JSONObject(values[0]);

    Object ob = json.get("data");

    if(ob!=null){
    String parsed = ob.toString();
        txtDisplay2.setText(parsed);
        sendMessage(WEAR_MESSAGE_PATH, parsed);
    }

}catch (JSONException je){
 txtDisplay2.setText("JSON parse error");
}
       //    sendMessage(WEAR_MESSAGE_PATH ,values[0]);
        }
        @Override
        protected Void doInBackground(String... urls) {
        try {
            SseEventSourceFactory factory = SseEventSourceFactory.createEventSourceFactory();
            URI net = new URI(urls[0]);
            eventSource = factory.createEventSource(net);
            eventSource.connect();
        }catch(URISyntaxException e){
            publishProgress("Syntex exception");
        }catch (IOException e) {
            e.printStackTrace();
            publishProgress("IO exception");
        }
                   try {
                        SseEventReader reader = eventSource.getEventReader(); // Receive event stream

                        SseEventType type = null;

                       while ((type = reader.next()) != SseEventType.EOS) { // Wait until type is DATA


                            switch (type) {
                                case DATA:

                                    publishProgress( (String)reader.getData());
                                    reader = null;
                                    reader = eventSource.getEventReader();
                                    break;
                                case EMPTY:
                                   // publishProgress( (String)reader.getName());
                                    break;
                            }
                     }
                    } catch (Exception ex) {
                       try{
                         eventSource.connect();}
                       catch (Exception e){

                       }
                         publishProgress((String) ("Exception: " + ex.getMessage()));
                         //txtDisplay2.setText("Exception: " + ex.getMessage());
                    }
            return null;
        }

        @Override
        protected void onPostExecute(Void feed) {

        }
    }

    class DisCon extends AsyncTask<String,Void,Void> {
        @Override
        protected Void doInBackground(String... urls) {
            try {
                eventSource.close();
            } catch (IOException e) {
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void feed) {
        txtDisplay1.setText("Disconnected");
        }

    }

    private void sendMessage( final String path, final String text ) {
        new Thread( new Runnable() {
            @Override
            public void run() {
                NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes( mGoogleApiClient ).await();
                for(Node node : nodes.getNodes()) {
                    MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(
                            mGoogleApiClient, node.getId(), path, text.getBytes()).await();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                   //     txtDisplay2.setText("new feed "+text +" has been sent"+"\n");
                    }
                });
            }
        }).start();
    }

    private void PostCon(final String in ) {
        new Thread( new Runnable() {
            @Override
            public void run() {
                String data = null;
                String[] s = in.split(" ");


                try {
                    data = URLEncoder.encode("access_token", "UTF-8")
                            + "=" + URLEncoder.encode("a076afbb8dac01d59f7aa8d2561d7fdc2b394a6b", "UTF-8");
                    data += "&" + URLEncoder.encode("params", "UTF-8") + "="
                            + URLEncoder.encode(s[1], "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                String text = "";
                BufferedReader reader=null;

                try
                {
                    // Defined URL  where to send data
                    URL url = new URL("https://api.particle.io/v1/devices/53ff71066667574857452367/"+s[0]);

                    // Send POST data request

                    URLConnection conn = url.openConnection();
                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                    wr.write( data );
                    wr.flush();

                    // Get the server response

                    reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while((line = reader.readLine()) != null)
                    {

                        // Append server response in string
                        sb.append(line + "\n");
                    }


                    text = sb.toString();
//try {
//    JSONObject json = new JSONObject(text);
//
//    Object ob = json.get("return_value");
//
//    if (ob != null) {
//        String parsed2 = ob.toString();
//       // txtDisplay2.setText(parsed2);
//        sendMessage(WEAR_MESSAGE_PATH, parsed2);
//    }
//
//}
//catch (JSONException e){
//    Log.d(TAG, "Json error" + text);
//}
//
               //    sendMessage(WEAR_MESSAGE_PATH,text);
                    Log.d(TAG, "result text " + text);
                }

                catch(Exception ex)
                {
                    Log.d(TAG, "exception 1: " + ex.getMessage(), ex);
                }
                finally
                {
                    try
                    {

                        reader.close();
                    }

                    catch(Exception ex) {
                        Log.d(TAG, "reader close exception: " + ex.getMessage(), ex);
                    }
                }
                Log.d(TAG, "result text 2" + text);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run(){ }
                });
            }
        }).start();
    }



}

