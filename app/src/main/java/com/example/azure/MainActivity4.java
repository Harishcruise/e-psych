package com.example.azure;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity4 extends AppCompatActivity implements RetrieveGetSentiment.AsyncResponse {
   // private CustomResultReciever resultReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feeling_activity);

    //    registerService();
    }
//
//    private void registerService() {
//        Intent intent = new Intent(getApplicationContext(), CustomIntentService.class);
//
//        /*
//         * Step 2: We pass the ResultReceiver via the intent to the intent service
//         * */
//        resultReceiver = new CustomResultReciever(new Handler(), this);
//        intent.putExtra("receiver", resultReceiver);
//        startService(intent);
//    }
//
//    @Override
//    public void onReceiveResult(int resultCode, Bundle resultData) {
//        /*
//         * Step 3: Handle the results from the intent service here!
//         * */
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//
//        /*
//         * Step 4: don't forget to clear receiver in order to avoid leaks.
//         * */
//        if (resultReceiver != null) {
//            resultReceiver.setAppReceiver(null);
//        }
//    }
    public void analyzer(String score){
        if ( Double.parseDouble(score) <= .5 ){
            Intent i = new Intent (this, NextActivity1.class);
            startActivity(i);
        }
        else{
            Intent n = new Intent(this, NextActivity2.class);
            startActivity(n);
        }
    }

            @Override
            public void processFinish(String output) {
                final String[] score2 = {null};

                score2[0] = output;
                analyzer(score2[0]);
            }

    public void Check(View view) {
        final String[] score2 = {null};
//        RetrieveGetSentiment RetrieveGetSentiment = new RetrieveGetSentiment(new RetrieveGetSentiment.AsyncResponse() {
//            @Override
//            public void processFinish(String output) {
//                score2[0] = output;
//            }
//        });
       EditText feeling = (EditText) findViewById(R.id.editText);
        RetrieveGetSentiment RetrieveGetSentiment = new RetrieveGetSentiment(this);
           RetrieveGetSentiment.execute(feeling.getText().toString());
//        if ( Double.parseDouble(score2[0]) < .5 ){
//            Intent i = new Intent (this, NextActivity1.class);
//            startActivity(i);
//        }
//        else{
//            Intent n = new Intent(this, NextActivity2.class);
//            startActivity(n);
//        }
//        System.out.print(RetrieveGetSentiment.getScore());
//        double score = .04;
//        if ( score < .5 ){
//            Intent i = new Intent (this, NextActivity1.class);
//            startActivity(i);
//        }
//        else{
//            Intent n = new Intent(this, NextActivity2.class);
//            startActivity(n);
//        }
    }
}
