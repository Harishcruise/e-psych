package com.example.azure;


import android.content.Intent;
import android.os.AsyncTask;

class RetrieveGetSentiment extends AsyncTask<String, Void, String> {

    private Exception exception;
    private String returnscore;
    public String getScore(){
        return returnscore;
    }
    public interface AsyncResponse{
        void processFinish(String output);
    }

    public AsyncResponse delegate = null;

    public RetrieveGetSentiment (AsyncResponse delegate){
        this.delegate = delegate;
    }
    protected String doInBackground(String... feeling) {
        String score = "Failed";
        try {
           score = GetSentiment.makeCall(feeling[0]);
//            URL url = new URL(urls[0]);
//            SAXParserFactory factory = SAXParserFactory.newInstance();
//            SAXParser parser = factory.newSAXParser();
//            XMLReader xmlreader = parser.getXMLReader();
//            RssHandler theRSSHandler = new RssHandler();
//            xmlreader.setContentHandler(theRSSHandler);
//            InputSource is = new InputSource(url.openStream());
//            xmlreader.parse(is);


        } catch (Exception e) {
            this.exception = e;
        } finally {
        }
        return score;
    }
    @Override
    protected void onPostExecute(String something) {
      // returnscore = something;
        delegate.processFinish(something);
    }
}