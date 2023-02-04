package ru.spornov91.post_file;

import android.app.*;
import android.os.*;
import android.util.Log;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.IOException;
import android.widget.TextView;
import java.io.File;
import java.io.PrintWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.BufferedInputStream;
import java.net.MalformedURLException;

public class MainActivity extends Activity 
{
	public String TAG = "spornov91";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		final TextView post_request = findViewById(R.id.postRequest);
		final TextView text_status = findViewById(R.id.status);
		Thread t = new Thread(new Runnable() { 
				public void run() {
					
					String url = "http://z91374e0.beget.tech/upload.php";
					String query = "type=file&version=1.1";
					
					final String post_response = doPost(url, query);
					
                    new Handler(getMainLooper()).post(
						new Runnable() {
							@Override
							public void run () {
								post_request.setText(post_response);
								text_status.setText("Файл отправлен!");
							}
					});
		}});
		t.start();
    }
	
	String sendfile (String filename, String filepath) throws IOException {
		String boundary = Long.toHexString(System.currentTimeMillis());
		String charset = "UTF-8";
		URL url = new URL("...");
		File file = new File(filepath);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("POST");
		con.setUseCaches(false);
		con.setDoOutput(true);
		con.setDoInput(true);
		con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
		OutputStream os = con.getOutputStream();
		PrintWriter writer = new PrintWriter(new OutputStreamWriter(os, charset), true);
		return "";
	};
	
	void getFile(String url){
		HttpURLConnection c = null;
		try {
			URL u = new URL(url);
			c = (HttpURLConnection) u.openConnection();
			c.setRequestMethod("GET");
			c.setRequestProperty("Content-length", "0");
			c.setUseCaches(false);
			c.setAllowUserInteraction(false);
			c.connect();
			int status = c.getResponseCode();
			Log.d("status",""+status);
		}catch (IOException ex) {

		} finally {}
	}
	
	/**
     * HttpURLConnection POST
     * @param urlString
     * @param params
     * @return
     */
    public String doPost(String urlString, String params) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setRequestMethod("POST");
                urlConnection.setReadTimeout(5000);
                urlConnection.setConnectTimeout(10000);
                urlConnection.setDoOutput(true);
				urlConnection.setRequestProperty("Accept-Charset", "UTF-8");
				
                OutputStream os = urlConnection.getOutputStream();
                os.write(params.getBytes());
                os.flush();
                os.close();

                int responseCode = urlConnection.getResponseCode();
                Log.d(TAG, ""+responseCode);
                if (responseCode == 200) {
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    return readStream(in);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param in
     * @return
     * @throws IOException
     */
    private String readStream(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }
        Log.e(TAG,sb.toString());
        br.close();
        return sb.toString();
    }
}
