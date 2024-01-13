package com.example.azure;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.Volley;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class ChatbotActivity extends AppCompatActivity {
    private final int REQ_CODE_SPEECH_INPUT = 100;
    ImageButton btnSpeak;
    Button b1;
    TextView txtSpeechInput, outputText,sentianalysis;
    int pos,neg,zero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatbot_activity);
        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);
        txtSpeechInput = (TextView) findViewById(R.id.txtSpeechInput);
        outputText = (TextView) findViewById(R.id.outputTex);
        sentianalysis = (TextView) findViewById(R.id.sentimentalanalysis);
        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promptSpeechInput();
            }
        });
        b1=(Button)findViewById(R.id.b1);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pos > neg && pos> zero)
                    sentianalysis.setText("POSITIVE");
                else if(neg>pos && neg>zero)
                    sentianalysis.setText("NEGATIVE");
                else
                    sentianalysis.setText("ZERO");
            }
        });

    }

    /**
     * Showing google speech input dialog
     */
    private void promptSpeechInput() {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        // intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Say Something");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    "sorry! Your device doesn't support speech input",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String userQuery = result.get(0);
                    txtSpeechInput.setText(userQuery);
                    RequestQueue queue;
                    queue = Volley.newRequestQueue(ChatbotActivity.this);
                    String url = "https://sentiment-analysis-api.herokuapp.com/sentiment";
                    StringRequest sr = new StringRequest(Request.Method.POST, url , new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if((response.toUpperCase()).matches("POSITIVE"))
                                pos = pos + 1;
                            else if((response.toUpperCase().matches("NEGATIVE")))
                                neg=neg+1;
                            else zero= zero+1;
                            sentianalysis.setText(response.toUpperCase());
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            sentianalysis.setText(error.getMessage());
                        }
                    }) {
                        @Override
                        public byte[] getBody() throws AuthFailureError {
                            HashMap<String, String> params2 = new HashMap<String, String>();
                            params2.put("text", txtSpeechInput.getText().toString() );
                            return new JSONObject(params2).toString().getBytes();
                        }

                        @Override
                        public String getBodyContentType() {
                            return "application/json";
                        }
                    };

                    queue.add(sr);

                    RetrieveFeedTask task = new RetrieveFeedTask();
                    task.execute(userQuery);


                }
                break;
            }

        }
    }


    // Create GetText Metod
    public String GetText(String query) throws UnsupportedEncodingException {

        String text = "";
        BufferedReader reader = null;

        // Send data
        try {

            // Defined URL  where to send data
            URL url = new URL("https://api.api.ai/v1/query?v=20150910");

            // Send POST data request

            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);

            conn.setRequestProperty("Authorization", "Bearer 7c83dee45e134a7ba5adee74b6e34c60");
            conn.setRequestProperty("Content-Type", "application/json");

            //Create JSONObject here
            JSONObject jsonParam = new JSONObject();
            JSONArray queryArray = new JSONArray();
            queryArray.put(query);
            jsonParam.put("query", queryArray);
//            jsonParam.put("name", "order a medium pizza");
            jsonParam.put("lang", "en");
            jsonParam.put("sessionId", "1234567890");


            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            Log.d("karma", "after conversion is " + jsonParam.toString());
            wr.write(jsonParam.toString());
            wr.flush();
            Log.d("karma", "json is " + jsonParam);

            // Get the server response

            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;


            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }


            text = sb.toString();


            JSONObject object1 = new JSONObject(text);
            JSONObject object = object1.getJSONObject("result");
            JSONObject fulfillment = null;
            String speech = null;
//            if (object.has("fulfillment")) {
            fulfillment = object.getJSONObject("fulfillment");
//                if (fulfillment.has("speech")) {
            speech = fulfillment.optString("speech");
//                }
//            }


            Log.d("karma ", "response is " + text);
            return speech;

        } catch (Exception ex) {
            Log.d("karma", "exception at last " + ex);
        } finally {
            try {

                reader.close();
            } catch (Exception ex) {
            }
        }

        return null;
    }


    class RetrieveFeedTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... voids) {
            String s = null;
            try {

                s = GetText(voids[0]);


            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                Log.d("karma", "Exception occurred " + e);
            }

            return s;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            outputText.setText(s);

        }
    }
}
