package com.example.a1.attempt4;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.wearable.view.WatchViewStub;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewDebug;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
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
import android.widget.Toast;

import javax.net.ssl.HttpsURLConnection;

/***************************************/
public class MainActivity extends Activity implements MessageApi.MessageListener, GoogleApiClient.ConnectionCallbacks{

    private static final String TAG = "MainActivity";
    private TextView txtDisplay1;
    private TextView txtDisplay2;
    private Button sendbut;
    private Button testbut;
    private Switch mySwitch;
    private String pagesw;
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

    private Spinner spinner, spinner1, spinner2, spinner3;
    private SeekBar seek1;
    String dir = "idle";
    String band = "mid";
    String temp = null;
    int seekvalue = 0;
    String merge;
    private Handler handler;
    ///////


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

    }
private void init(){
    txtDisplay1 = (TextView)findViewById(R.id.textView);
    txtDisplay2 = (TextView)findViewById(R.id.textView2);
    sendbut = (Button) findViewById(R.id.connect);
    testbut = (Button) findViewById(R.id.button);
    image = (ImageView) findViewById(R.id.imageView);

    image2 = (ImageView) findViewById(R.id.imageView2);
    spinner = (Spinner) findViewById(R.id.spinner);
    spinner1 = (Spinner) findViewById(R.id.spinner1);
    spinner2 = (Spinner) findViewById(R.id.spinner2);
    spinner3 = (Spinner) findViewById(R.id.spinner3);
    seek1 = (SeekBar) findViewById(R.id.seekBar);
    mySwitch = (Switch) findViewById(R.id.switch1);
    mySwitch.setChecked(false);
    handler = new Handler();

    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

   sendbut.setOnClickListener(new Button.OnClickListener() {
       @Override
       public void onClick(View arg0) {
           // image.setImageResource(R.drawable.idle);
           //CharSequence c = txtDisplay2.getText();
           String p1 = spinner2.getSelectedItem().toString();
           String p2 = spinner.getSelectedItem().toString();
           String p3 = spinner1.getSelectedItem().toString();
           String rw = spinner3.getSelectedItem().toString();
           String T=null;
           int tint = seek1.getProgress();
           switch (rw){
               case"Read":
                   T ="r";
                   break;
               case"Write":
                   T = "w";
                   break;
           }
           merge = p1 + " " + p2;
           ///////
           if (p1.equals("PROX")) {
               merge += p3;
           }
           if (T == "r" || T == "w") {
               merge += T;
           }
           if (T == "w") {
               merge += tint;
           }

           txtDisplay2.setText(merge);
           if (p2.charAt(0) == 'A' || p2.charAt(0) == 'D') {

               sendMessage(WEAR_MESSAGE_PATH2, merge);
               if(mySwitch.isChecked()) {
                   handler.postDelayed(runnable, 1000);
               }

           }
       }
   });
    testbut.setOnClickListener(new Button.OnClickListener() {
        @Override
        public void onClick(View arg0) {
            temp = null;
            txtDisplay2.setText("");
        }
    });

    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

        public void onItemSelected(AdapterView<?> arg0, View arg1,
                                   int arg2, long arg3) {
            // TODO Auto-generated method stub
//            Toast.makeText(getBaseContext(), spinner.getSelectedItem().toString(),
//                    Toast.LENGTH_SHORT).show();
            //  txtDisplay2.append(spinner.getSelectedItem().toString());
            //   temp = temp + spinner.getSelectedItem().toString();
        }

        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub

        }
    });
    spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

        public void onItemSelected(AdapterView<?> arg0, View arg1,
                                   int arg2, long arg3) {
            // TODO Auto-generated method stub
        //    txtDisplay2.append(spinner1.getSelectedItem().toString());
           // temp = temp +spinner1.getSelectedItem().toString();
        }

        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub

        }
    });
    spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

        public void onItemSelected(AdapterView<?> arg0, View arg1,
                                   int arg2, long arg3) {
            // TODO Auto-generated method stub
      //      txtDisplay2.setText(spinner2.getSelectedItem().toString() + " ");
      //      temp = spinner2.getSelectedItem().toString() + " ";
        }

        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub

        }
    });
    spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

        public void onItemSelected(AdapterView<?> arg0, View arg1,
                                   int arg2, long arg3) {
            // TODO Auto-generated method stub
            String m;
          //  txtDisplay2.append(spinner3.getSelectedItem().toString());

            if(spinner3.getSelectedItem().toString().equals("Read")){
                m ="r";
            }else if (spinner3.getSelectedItem().toString().equals("Write")){m="w";}
            else{m = "";}
            temp = m;
        }

        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub

        }
    });
    seek1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
        int progress = 0;

        @Override
        public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
            progress = progressValue;
            txtDisplay1.setText(String.valueOf(progress));

        }
        @Override

        public void onStartTrackingTouch(SeekBar seekBar) {
        }
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        //    txtDisplay2.append(String.valueOf(progress));
            seekvalue= progress;
        }


    });

}
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
      /* do what you need to do */
            sendMessage(WEAR_MESSAGE_PATH2, merge);

      /* and here comes the "trick" */
            if(mySwitch.isChecked()) {
                handler.postDelayed(this, 1000);
            }
        }
    };

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
                        pagesw = in;

                        ////////////
                        if(in.charAt(0)=='D' || in.charAt(0)=='B' ) {
                            drawing(in);
                        }
                            txtDisplay1.setText(in);

                        ////////////
                    }
                }
                switch (pagesw){
                   // setContentView(R.layout.demo1);
                    //image = (ImageView)findViewById(R.id.imageView3);

                    case "demo1":
                        Log.d(TAG,"before intent");
                        Intent   intent = new Intent( MainActivity.this, Demo_1.class);
                        startActivity(intent);
                        break;
                    case "demo2":
                        Log.d(TAG, "before intent");
                        Intent intent2 = new Intent( MainActivity.this, Demo_2.class);
                        startActivity(intent2);
                        break;
                    case "pinset":
                        Log.d(TAG, "before intent");
                        Intent intent3 = new Intent( MainActivity.this, PinSet.class);
                        startActivity(intent3);
                        break;
                    case "samples":
                        Log.d(TAG, "before intent");
                        Intent intent4 = new Intent( MainActivity.this, Sampling.class);
                        startActivity(intent4);
                        break;
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

          //  sendMessage(START_ACTIVITY, "");


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
