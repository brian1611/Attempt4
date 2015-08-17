package com.example.a1.attempt4;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

/**
 * Created by A1 on 15/08/2015.
 */
public class PinSet extends Activity implements MessageApi.MessageListener, GoogleApiClient.ConnectionCallbacks {
    private static final String TAG = "PinSet";
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
    String lastPin;
    int defaultcolor=0;
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo3);
        init();
        initGoogleApiClient();
    }
    private void init(){
        d0Button = (Button) findViewById(R.id.d0);
        a0Button = (Button) findViewById(R.id.a0);
        a1Button = (Button) findViewById(R.id.a1);
        a4Button = (Button) findViewById(R.id.a4);
        a5Button = (Button) findViewById(R.id.a5);
        a6Button = (Button) findViewById(R.id.a6);
        a7Button = (Button) findViewById(R.id.a7);
        d0res = (TextView) findViewById(R.id.d0value);
        a0res = (TextView) findViewById(R.id.a0value);
        a1res = (TextView) findViewById(R.id.a1value);
        a4res = (TextView) findViewById(R.id.a4value);
        a5res = (TextView) findViewById(R.id.a5value);
        a6res = (TextView) findViewById(R.id.a6value);
        a7res = (TextView) findViewById(R.id.a7value);
        progress_value = (TextView)findViewById(R.id.s_value);
        seek = (SeekBar) findViewById(R.id.seekbar_s);
        defaultcolor= a0Button.getSolidColor();

        d0Button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                sendMessage(WEAR_MESSAGE_PATH2, "SET D0w" + seek.getProgress());
                d0res.setText(String.valueOf(seek.getProgress()));

                if(seek.getProgress()>0)
                {d0Button.setBackgroundColor(getResources().getColor(R.color.green));}
                else{
                    d0Button.setBackgroundColor(getResources().getColor(R.color.grey));
                }
            }
        });
        a0Button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendMessage(WEAR_MESSAGE_PATH2,"SET A0w"+seek.getProgress());
                a0res.setText(String.valueOf(seek.getProgress()));

                if(seek.getProgress()>0)
                {a0Button.setBackgroundColor(getResources().getColor(R.color.green));}
                else{
                    a0Button.setBackgroundColor(getResources().getColor(R.color.grey));
                }
            }
        });
        a1Button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendMessage(WEAR_MESSAGE_PATH2,"SET A1w"+seek.getProgress());
                a1res.setText(String.valueOf(seek.getProgress()));

                if(seek.getProgress()>0)
                {a1Button.setBackgroundColor(getResources().getColor(R.color.green));}
                else{
                    a1Button.setBackgroundColor(getResources().getColor(R.color.grey));
                }
            }
        });
        a4Button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendMessage(WEAR_MESSAGE_PATH2,"SET A4w"+seek.getProgress());
                a4res.setText(String.valueOf(seek.getProgress()));

                if(seek.getProgress()>0)
                {a4Button.setBackgroundColor(getResources().getColor(R.color.green));}
                else{
                    a4Button.setBackgroundColor(getResources().getColor(R.color.grey));
                }
            }
        });
        a5Button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendMessage(WEAR_MESSAGE_PATH2,"SET A5w"+seek.getProgress());
                a5res.setText(String.valueOf(seek.getProgress()));

                if(seek.getProgress()>0)
                {a5Button.setBackgroundColor(getResources().getColor(R.color.green));}
                else{
                    a5Button.setBackgroundColor(getResources().getColor(R.color.grey));
                }
            }
        });
        a6Button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendMessage(WEAR_MESSAGE_PATH2,"SET A6w"+seek.getProgress());
                a6res.setText(String.valueOf(seek.getProgress()));

                if(seek.getProgress()>0)
                {a6Button.setBackgroundColor(getResources().getColor(R.color.green));}
                else{
                    a6Button.setBackgroundColor(getResources().getColor(R.color.grey));
                }
            }
        });
        a7Button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendMessage(WEAR_MESSAGE_PATH2,"SET A7w"+seek.getProgress());
                a7res.setText(String.valueOf(seek.getProgress()));

                if(seek.getProgress()>0)
                {a7Button.setBackgroundColor(getResources().getColor(R.color.green));}
                else{
                    a7Button.setBackgroundColor(getResources().getColor(R.color.grey));
                }
            }
        });
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                progress_value.setText(progressValue+"/255");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
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

    @Override
    public void onMessageReceived( final MessageEvent messageEvent ) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (messageEvent.getPath().equalsIgnoreCase(WEAR_MESSAGE_PATH)) {

                    if (messageEvent.getData() != null) {
                        String in = new String(messageEvent.getData());
                        pagesw = in;

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

}
