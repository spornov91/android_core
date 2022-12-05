package ru.spornov91.json;

import android.app.*;
import android.os.*;
import android.widget.*;
import java.net.*;
import java.io.*;
import android.util.*;
import android.view.*;
import org.json.*;
import java.util.*;

public class MainActivity extends Activity 
{
	String TAG = "spornov91";
	String[] names;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        
		final TextView tvJsonData = findViewById(R.id.mainTextView);
		
		Button getUserGithub = findViewById(R.id.btnGetUserGithub);
		getUserGithub.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					
					Thread t = new Thread(new Runnable() { 
							public void run() {
							
								String url = "https://api.github.com/users/spornov91/repos";
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
											names = jsontoarr(sb.toString());
									}
								} catch (MalformedURLException ex) {

								} catch (IOException ex) {

								} finally {
									if (c != null) {
										try {
											c.disconnect();
											
											new Handler(getMainLooper()).post(
												new Runnable() {
													@Override
													public void run () {
														ListView lvMain = findViewById(R.id.lvUserGithub);
														ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
																												android.R.layout.simple_list_item_1, names);
														lvMain.setAdapter(adapter);
													}
												});
										} catch (Exception ex) {

										}
									}
								}
							};
						});
					t.start();
				}
			});
			
    }
	
	private static String[] jsontoarr(String str)
	{
		ArrayList<String> list = new ArrayList<String>();
		try{
		//JSONObject obj = new JSONObject(str);
		JSONArray arr = new JSONArray(str);
		for (int n = 0; n < arr.length(); n++) {
			JSONObject obj = arr.getJSONObject(n);
			//JSONArray arr = obj.getJSONArray("Prod");
			String _name = obj.getString("name");
			Log.d("obj.name",_name);
			list.add(_name);
		} 
		
		}catch (JSONException e) {
			e.printStackTrace();
		}
		
		return list.toArray(new String[0]);
    };
}
