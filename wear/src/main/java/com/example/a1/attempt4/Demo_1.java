package com.example.a1.attempt4;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

/**
 * Created by A1 on 13/08/2015.
 */
public class Demo_1 extends Activity implements MessageApi.MessageListener, GoogleApiClient.ConnectionCallbacks {
    private static final String TAG = "demo1";
    //phone to watch
    private static final String START_ACTIVITY = "/start_activity";
    private static final String WEAR_MESSAGE_PATH = "/message";
    //watch to phone
    private static final String START_ACTIVITY2 = "/start_activity2";
    private static final String WEAR_MESSAGE_PATH2 = "/message2";
    private GoogleApiClient mGoogleApiClient;
    private GoogleApiClient mGoogleApiClient2;
    private String dir;
    private ImageView image;
    String pagesw;

    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo1);
        init();
        initGoogleApiClient();
    }
    private void init(){
image = (ImageView)findViewById(R.id.imageView3);
        image.setImageResource(R.drawable.normal);
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

            case "DR":
                dir = "right";
                image.setImageResource(R.drawable.goright);

                break;
            case "DL":
                dir = "left";
                image.setImageResource(R.drawable.goleft);

                break;
            case "DI":
                dir = "idle";
                image.setImageResource(R.drawable.normal);

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
                        drawing(in);

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
