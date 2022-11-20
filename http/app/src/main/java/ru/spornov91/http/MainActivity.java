package ru.spornov91.http;

import android.app.*;
import android.os.*;
import android.util.*;
import java.io.*;
import java.net.*;
import android.webkit.*;
import java.util.concurrent.*;

public class MainActivity extends Activity 
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		//WebView wv = new WebView(getApplicationContext());
		final WebView wv = findViewById(R.id.webView);
		wv.setWebViewClient(new WebViewClient());
		WebSettings webSettings = wv.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setDomStorageEnabled(true);
		webSettings.setLoadWithOverviewMode(true);
		webSettings.setUseWideViewPort(true);
		webSettings.setBuiltInZoomControls(true);
		webSettings.setDisplayZoomControls(false);
		webSettings.setSupportZoom(true);
		webSettings.setDefaultTextEncodingName("utf-8");
		
		
		//Log.d("s91; ", "l; "+html_txt.length());
		Thread t = new Thread(new Runnable() { 
				public void run() {
					//mock html source
					final String html_txt1 = "<html> <body>1</body></html>";
					final String html_txt2 = http("https://jsonplaceholder.typicode.com");
					//final String html_txt2 = http("https://square.github.io/okhttp/3.x/okhttp/index.html?okhttp3/ResponseBody.html");
					Log.d("s91; ", html_txt2);
                    new Handler(getMainLooper()).post(
						new Runnable() {
							@Override
							public void run () {
								wv.loadData(html_txt2, "text/html", "UTF-8");
							}
						});
				};
		});
		t.start();
		
		
    };
	
	public String http(String site) {

        URL url;
        HttpURLConnection urlConnection = null;
		String server_response = "";
        try {
            url = new URL(site);
            urlConnection = (HttpURLConnection) url.openConnection();

            int responseCode = urlConnection.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK){
                server_response = readStream(urlConnection.getInputStream());
				//Log.v("http: ", server_response);
            }
			return server_response;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    };

	// Converting InputStream to String

	private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }


};
