package com.example.a1.attempt4;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

/**
 * Created by A1 on 17/08/2015.
 */
public class Sampling extends Activity implements MessageApi.MessageListener, GoogleApiClient.ConnectionCallbacks {
    private static final String TAG = "Sampling";
    //phone to watch
    private static final String START_ACTIVITY = "/start_activity";
    private static final String WEAR_MESSAGE_PATH = "/message";
    //watch to phone
    private static final String START_ACTIVITY2 = "/start_activity2";
    private static final String WEAR_MESSAGE_PATH2 = "/message2";
    private GoogleApiClient mGoogleApiClient;
    private GoogleApiClient mGoogleApiClient2;

    String pagesw;
    private Button d0Button,a0Button,a1Button,a4Button,a5Button,a6Button,a7Button;
    private TextView d0res, a0res, a1res, a4res, a5res, a6res, a7res, progress_value;
    private SeekBar seek;
    private Handler handler1, handler2, handler3, handler4, handler5, handler6, handler7;
    private Switch mySwitch;

    private static final int SAMPLING_INTERVAL = 1000;



    int defaultcolor=0;
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo4);
        init();
        initGoogleApiClient();
    }
    private void init(){
        d0Button = (Button) findViewById(R.id.d0r);
        a0Button = (Button) findViewById(R.id.a0r);
        a1Button = (Button) findViewById(R.id.a1r);
        a4Button = (Button) findViewById(R.id.a4r);
        a5Button = (Button) findViewById(R.id.a5r);
        a6Button = (Button) findViewById(R.id.a6r);
        a7Button = (Button) findViewById(R.id.a7r);
        d0res = (TextView) findViewById(R.id.d0rvalue);
        a0res = (TextView) findViewById(R.id.a0rvalue);
        a1res = (TextView) findViewById(R.id.a1rvalue);
        a4res = (TextView) findViewById(R.id.a4rvalue);
        a5res = (TextView) findViewById(R.id.a5rvalue);
        a6res = (TextView) findViewById(R.id.a6rvalue);
        a7res = (TextView) findViewById(R.id.a7rvalue);
        handler1 = new Handler();
        handler2 = new Handler();
        handler3 = new Handler();
        handler4 = new Handler();
        handler5 = new Handler();
        handler6 = new Handler();
        handler7 = new Handler();
        mySwitch = (Switch) findViewById(R.id.sampling_switch);
        mySwitch.setChecked(false);

        defaultcolor= a0Button.getSolidColor();

        d0Button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendMessage(WEAR_MESSAGE_PATH2, "SET D0r");

                if(mySwitch.isChecked()) {
                    handler1.postDelayed(runnable1, SAMPLING_INTERVAL);
                }
            }
        });
        a0Button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendMessage(WEAR_MESSAGE_PATH2, "SET A0r");
                if(mySwitch.isChecked()) {
                    handler2.postDelayed(runnable2, SAMPLING_INTERVAL);
                }
            }
        });
        a1Button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendMessage(WEAR_MESSAGE_PATH2, "SET A1r");
                if(mySwitch.isChecked()) {
                    handler3.postDelayed(runnable3, SAMPLING_INTERVAL);
                }
            }
        });
        a4Button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendMessage(WEAR_MESSAGE_PATH2, "SET A4r");
                if(mySwitch.isChecked()) {
                    handler4.postDelayed(runnable4, SAMPLING_INTERVAL);
                }
            }
        });
        a5Button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendMessage(WEAR_MESSAGE_PATH2, "SET A5r");
                if(mySwitch.isChecked()) {
                    handler5.postDelayed(runnable5, SAMPLING_INTERVAL);
                }
            }
        });
        a6Button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendMessage(WEAR_MESSAGE_PATH2, "SET A6r");
                if(mySwitch.isChecked()) {
                    handler6.postDelayed(runnable6, SAMPLING_INTERVAL);
                }
            }
        });
        a7Button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendMessage(WEAR_MESSAGE_PATH2, "SET A7r");
                if(mySwitch.isChecked()) {
                    handler7.postDelayed(runnable7, SAMPLING_INTERVAL);
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
    }

    public void responseHandler(String in){
    String pinRead = in.substring(0,2);
        switch(pinRead){
            case "D0":
                d0res.setText(in.substring(2));
                break;
            case "A0":
                a0res.setText(in.substring(2));
                break;
            case "A1":
                a1res.setText(in.substring(2));
                break;
            case "A4":
                a4res.setText(in.substring(2));
                break;
            case "A5":
                a5res.setText(in.substring(2));
                break;
            case "A6":
                a6res.setText(in.substring(2));
                break;
            case "A7":
                a7res.setText(in.substring(2));
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
                        if (Character.isDigit(in.charAt(1))) {
                            responseHandler(in);
                        }
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
    public void onConnectionSuspended(int i) {}
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
    /**********************************/
    private Runnable runnable1 = new Runnable() {
        @Override
        public void run() {
            sendMessage(WEAR_MESSAGE_PATH2, "SET D0r");
            if(mySwitch.isChecked()) {
                handler1.postDelayed(this, SAMPLING_INTERVAL);
            }
        }
    };
    private Runnable runnable2 = new Runnable() {
        @Override
        public void run() {
            sendMessage(WEAR_MESSAGE_PATH2, "SET A0r");
            if(mySwitch.isChecked()) {
                handler2.postDelayed(this, SAMPLING_INTERVAL);
            }
        }
    };
    private Runnable runnable3 = new Runnable() {
        @Override
        public void run() {
            sendMessage(WEAR_MESSAGE_PATH2, "SET A1r");
            if(mySwitch.isChecked()) {
                handler3.postDelayed(this, SAMPLING_INTERVAL);
            }
        }
    };
    private Runnable runnable4 = new Runnable() {
        @Override
        public void run() {
            sendMessage(WEAR_MESSAGE_PATH2, "SET A4r");
            if(mySwitch.isChecked()) {
                handler4.postDelayed(this, SAMPLING_INTERVAL);
            }
        }
    };
    private Runnable runnable5 = new Runnable() {
        @Override
        public void run() {
            sendMessage(WEAR_MESSAGE_PATH2, "SET A5r");
            if(mySwitch.isChecked()) {
                handler5.postDelayed(this, SAMPLING_INTERVAL);
            }
        }
    };
    private Runnable runnable6 = new Runnable() {
        @Override
        public void run() {
            sendMessage(WEAR_MESSAGE_PATH2, "SET A6r");
            if(mySwitch.isChecked()) {
                handler6.postDelayed(this, SAMPLING_INTERVAL);
            }
        }
    };
    private Runnable runnable7 = new Runnable() {
        @Override
        public void run() {
            sendMessage(WEAR_MESSAGE_PATH2, "SET A7r");
            if(mySwitch.isChecked()) {
                handler7.postDelayed(this, SAMPLING_INTERVAL);
            }
        }
    };
}
