package com.example.httpapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	Editable url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn1=(Button) findViewById(R.id.button1);
        final EditText et= (EditText) findViewById(R.id.editText1);
        
        btn1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				url=et.getText();
				(new HTTPtask()).execute();
			
			}
		});
    }
    private class HTTPtask extends AsyncTask<String, Void, String> {
    	
        @Override
        protected String doInBackground(String... params) {
        	String app;
        	 Log.i("Praeda","1");
        	app=connectUDP((url.toString().equals("")) ? "151.24.156.247:8082" : url.toString());
            return app;
        }

        @Override
        protected void onPostExecute(String result) {
        	if (result!=null)
        	 Toast.makeText(getApplicationContext(), "EUREKAAAAA!", Toast.LENGTH_LONG).show();
        	else 
        	 Toast.makeText(getApplicationContext(), "EPIC FAIL! LOL", Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

public String connect(String url)
{

    HttpClient httpclient = new DefaultHttpClient();

    // Prepare a request object
    HttpGet httpget = new HttpGet(url); 
    Log.i("Praeda","0s");
    // Execute the request
    HttpResponse response;
    try {
    	  Log.i("Praeda","a");
        response = httpclient.execute(httpget);
        // Examine the response status
        //Log.i("Praeda","a");

        Log.i("Praeda",response.getStatusLine().toString());

        // Get hold of the response entity
        HttpEntity entity = response.getEntity();
        // If the response does not enclose an entity, there is no need
        // to worry about connection release

        if (entity != null) {

            // A Simple JSON Response Read
            InputStream instream = entity.getContent();
            String result= convertStreamToString(instream);
            Log.i("Praeda",result);

            // now you have the string representation of the HTML request
            instream.close();
        }
        return "a";

    } catch (Exception e) {e.printStackTrace(); return null;}
}
public String connectUDP(String url){
	 BufferedReader inFromUser =    new BufferedReader(new InputStreamReader(System.in));
	 String [] app=url.split(":");
	      DatagramSocket clientSocket = null;
		try {
			clientSocket = new DatagramSocket();
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	      //InetAddress IPAddress = InetAddress.getByName("localhost");
	      byte[] sendData = new byte[1024];
	      byte[] receiveData = new byte[1024];
	      String appS=new String("non viene un cazzo");
	      sendData=appS.getBytes();
	      
	      DatagramPacket sendPacket = null;
		try {
			sendPacket = new DatagramPacket(sendData, appS.length(), InetAddress.getByName(app[0]), Integer.parseInt(app[1]));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	      try {
			clientSocket.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	      DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
	      
	      try {
	    	  clientSocket.setSoTimeout(10000);
			clientSocket.receive(receivePacket);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	      String modifiedSentence = new String(receivePacket.getData());
	      System.out.println("FROM SERVER:" + modifiedSentence);
	      clientSocket.close();
	      return "a";
}
public String connectTCP(String url){
	String[] app = url.split(":");

	try {
		Socket socket=new Socket(InetAddress.getByName(app[0]), Integer.parseInt(app[1]));
		return "a";
	} catch (NumberFormatException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (UnknownHostException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return null;
	
}

private static String convertStreamToString(InputStream is) {
	    /*
	     * To convert the InputStream to String we use the BufferedReader.readLine()
	     * method. We iterate until the BufferedReader return null which means
	     * there's no more data to read. Each line will appended to a StringBuilder
	     * and returned as String.
	     */
	    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    StringBuilder sb = new StringBuilder();
	
	    String line = null;
	    try {
	        while ((line = reader.readLine()) != null) {
	            sb.append(line + "\n");
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            is.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    return sb.toString();
    }

}

