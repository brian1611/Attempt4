package com.example.a1.attempt4;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.os.AsyncTask;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;



/////////////////////////

import org.kaazing.net.sse.SseEventSourceFactory;
import org.kaazing.net.sse.SseEventSource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


import org.kaazing.net.sse.SseEventReader;
import org.kaazing.net.sse.SseEventType;


/***************************************/
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;
import com.google.android.gms.wearable.MessageEvent;
import android.util.Log;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.content.IntentFilter;
import android.content.BroadcastReceiver;
import android.content.Context;

import javax.net.ssl.HttpsURLConnection;

/***************************************/
public class MainActivity extends Activity implements MessageApi.MessageListener, GoogleApiClient.ConnectionCallbacks{

    private static final String TAG = "MainActivity";
    private TextView txtDisplay1;
    private Button sendbut;
    private Button testbut;
    ImageView image;
    ImageView image2;
    //phone to watch
    private static final String START_ACTIVITY = "/start_activity";
    private static final String WEAR_MESSAGE_PATH = "/message";
    //watch to phone
    private static final String START_ACTIVITY2 = "/start_activity2";
    private static final String WEAR_MESSAGE_PATH2 = "/message2";
    private GoogleApiClient mGoogleApiClient;
    private GoogleApiClient mGoogleApiClient2;
    String dir = "idle";
    String band = "mid";
    ///////
    ApiFacade api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                // mTextView = (TextView) stub.findViewById(R.id.text);
                init();
                initGoogleApiClient();
                ////

            }
        });
        api = ApiFacade.getInstance(this);
    }
private void init(){
    txtDisplay1 = (TextView)findViewById(R.id.textView);
    sendbut = (Button) findViewById(R.id.connect);
    testbut = (Button) findViewById(R.id.button);
    image = (ImageView) findViewById(R.id.imageView);
    image2 = (ImageView) findViewById(R.id.imageView2);
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

   sendbut.setOnClickListener(new Button.OnClickListener() {
       @Override
       public void onClick(View arg0) {
          // image.setImageResource(R.drawable.idle);
           sendMessage(WEAR_MESSAGE_PATH2, "POST on");
       }
   });
    testbut.setOnClickListener(new Button.OnClickListener() {
        @Override
        public void onClick(View arg0) {

            sendMessage(WEAR_MESSAGE_PATH2, "POST off");
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
    }
    public void drawing(String in){
        switch (in){
            case "BU":
                band = "up";
                image2.setImageResource(R.drawable.up);
                break;
            case "BD":
                band = "down";
                image2.setImageResource(R.drawable.down);
                break;
            case "BM":
                band = "mid";
                image2.setImageResource(R.drawable.idle);
                break;
            case "DR":
                dir = "right";
                image.setImageResource(R.drawable.right);

                break;
            case "DL":
                dir = "left";
                image.setImageResource(R.drawable.left);
                break;
            case "DI":
                dir = "idle";
                image.setImageResource(R.drawable.idle);
                break;
        }
    }

    @Override
    public void onMessageReceived( final MessageEvent messageEvent ) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (messageEvent.getPath().equalsIgnoreCase(WEAR_MESSAGE_PATH)) {

                    if (messageEvent.getData() != null) {
                        String in = new String(messageEvent.getData());
                        txtDisplay1.setText(in);
                        ////////////
                        if(in.charAt(0)=='D' || in.charAt(0)=='B' ) {
                            drawing(in);
                        }
                        ////////////
                    }
                }
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        if( mGoogleApiClient != null && !( mGoogleApiClient.isConnected() || mGoogleApiClient.isConnecting() ) )
            mGoogleApiClient.connect();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
    @Override
    public void onConnected(Bundle bundle) {
        Wearable.MessageApi.addListener(mGoogleApiClient, this);

            sendMessage(START_ACTIVITY, "");


    }

    @Override
    protected void onStop() {
        if ( mGoogleApiClient != null ) {
            Wearable.MessageApi.removeListener( mGoogleApiClient, this );
            if ( mGoogleApiClient.isConnected() ) {
                mGoogleApiClient.disconnect();
            }
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if( mGoogleApiClient != null )
            mGoogleApiClient.unregisterConnectionCallbacks(this);
        super.onDestroy();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

/////-----------------
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



    /***************test********************/


}
