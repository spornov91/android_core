package ru.spornov91.json;

import android.app.*;
import android.os.*;
import android.widget.*;
import java.net.*;
import java.io.*;
import android.util.*;
import android.view.*;
import org.json.*;

public class MainActivity extends Activity 
{
	String TAG = "spornov91";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        
		String[] names = { "Иван", "Марья", "Петр", "Антон", "Даша", "Борис",
			"Костя", "Игорь", "Анна", "Денис", "Андрей" };
		
		ListView lvMain = findViewById(R.id.lvUserGithub);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		android.R.layout.simple_list_item_1, names);
		lvMain.setAdapter(adapter);
		
		final TextView tvJsonData = findViewById(R.id.mainTextView);
		
		Button getUserGithub = findViewById(R.id.btnGetUserGithub);
		getUserGithub.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					new AsyncRequest().execute();
					
				}
			});
			
		
			
    }
	
	class AsyncRequest extends AsyncTask<String, Integer, String>{

		@Override
		protected String doInBackground(String... arg) {

			//int timeout = 15000;
			//String url = "https://127.0.0.1:2012/realjson";
			String url = "https://api.github.com/users/spornov91";
			HttpURLConnection c = null;
			try {
				URL u = new URL(url);
				c = (HttpURLConnection) u.openConnection();
				c.setRequestMethod("GET");
				c.setRequestProperty("Content-length", "0");
				c.setUseCaches(false);
				c.setAllowUserInteraction(false);
				//c.setConnectTimeout(timeout);
				//c.setReadTimeout(timeout);
				c.connect();
				int status = c.getResponseCode();
				Log.d("status",""+status);
				switch (status) {
					case 200:
					case 201:
						BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
						StringBuilder sb = new StringBuilder();
						String line;
						while ((line = br.readLine()) != null) {
							sb.append(line+"\n");
						}
						br.close();
						try{
						    JSONObject obj = new JSONObject(sb.toString());
							//JSONArray arr = obj.getJSONArray("Prod");
							String _name = obj.getString("name");
							Log.d("obj.name",_name);
						}catch (JSONException e) {
							e.printStackTrace();
						}
						//Log.d("br.readLine",sb.toString());
						return sb.toString();
				}
			} catch (MalformedURLException ex) {

			} catch (IOException ex) {

			} finally {
				if (c != null) {
					try {
						c.disconnect();
					} catch (Exception ex) {

					}
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(String s) {
			super.onPostExecute(s);
			//tvJsonData.setText(s);
		}
	}
}
