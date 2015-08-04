package com.example.a1.attempt4;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.Header;
import org.json.*;
import com.loopj.android.http.*;

public class ApiFacade {
    protected Context ctx;

    public static ApiFacade getInstance(Context context) {
        ApiFacade api = new ApiFacade();
        api.ctx = context;

        return api;
    }


    public void getVariable(String variable) throws JSONException {

        RestClient.get(variable, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Toast.makeText(ctx,"Success!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                Toast.makeText(ctx,"Success!", Toast.LENGTH_LONG).show();
                Log.i("ApiFacade", response.toString());

                // Do something with the response
            }
            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                // Hide Progress Dialog
                //prgDialog.hide();
                // When Http response code is '404'
                if(statusCode == 404){
                    Toast.makeText(ctx,"Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if(statusCode == 500){
                    Toast.makeText(ctx, "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else{
                    Toast.makeText(ctx, errorResponse.toString(), Toast.LENGTH_LONG).show();
                    Toast.makeText(ctx, "Unexpected Error occcured! " + statusCode, Toast.LENGTH_LONG).show();
                }
            }

            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, java.lang.Throwable throwable, org.json.JSONArray errorResponse) {
                // Hide Progress Dialog
                //prgDialog.hide();
                // When Http response code is '404'
                if(statusCode == 404){
                    Toast.makeText(ctx,"Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if(statusCode == 500){
                    Toast.makeText(ctx, "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else{
                    Toast.makeText(ctx, "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void callFunction(String functionName, String params) throws JSONException {
        RequestParams true_params = new RequestParams();
          true_params.put("args",params);
        RestClient.post(functionName, true_params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Toast.makeText(ctx, "Success!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                Toast.makeText(ctx, "Success!", Toast.LENGTH_LONG).show();
                Log.i("ApiFacade", response.toString());

                // Do something with the response


            }

            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                // Hide Progress Dialog
                //prgDialog.hide();
                // When Http response code is '404'
                if (statusCode == 404) {
                    Toast.makeText(ctx, "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    Toast.makeText(ctx, "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else {
                    Toast.makeText(ctx, errorResponse.toString(), Toast.LENGTH_LONG).show();
                    Toast.makeText(ctx, "Unexpected Error occcured! " + statusCode, Toast.LENGTH_LONG).show();
                }
            }

            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, java.lang.Throwable throwable, org.json.JSONArray errorResponse) {
                // Hide Progress Dialog
                //prgDialog.hide();
                // When Http response code is '404'
                if (statusCode == 404) {
                    Toast.makeText(ctx, "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    Toast.makeText(ctx, "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else {
                    Toast.makeText(ctx, "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}