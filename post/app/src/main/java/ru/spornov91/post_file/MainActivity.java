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
import java.io.BufferedWriter;
import android.content.Intent;
import android.net.Uri;
import java.io.FileInputStream;
import java.io.DataOutputStream;
import android.Manifest;
import android.content.pm.PackageManager;

public class MainActivity extends Activity 
{
	public String TAG = "spornov91";

	String upload_url = "http://z91374e0.beget.tech/upload.php";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


		Intent intent = new Intent()
			.setType("*/*")
			.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, "Выберите файл"), 1);

	};
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK)
		{

            int REQUEST_CODE_PERMISSIONS = 1;
		    int permissionStatus = getApplicationContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);

		    if (permissionStatus == PackageManager.PERMISSION_GRANTED)
			{

				String path0 = Environment.getExternalStorageDirectory().toString();

				try
				{
					Uri selectedfile = data.getData();
					String path1 = selectedfile.getPath().split(":")[1];
					Log.d(TAG, path1);
					final String filepath = path0 + "/" + path1;
					final String filename = selectedfile.getLastPathSegment();

					final TextView post_request = findViewById(R.id.postRequest);
					final TextView text_status = findViewById(R.id.status);

					Thread t = new Thread(new Runnable() { 
							public void run()
							{

								final String post_response = postFile(upload_url, filename, filepath);

								new Handler(getMainLooper()).post(
									new Runnable() {
										@Override
										public void run()
										{
											post_request.setText(post_response);
											text_status.setText("Файл отправлен!");
										}
									});
							}});
					t.start();

				}
				finally
				{}

			}
			else
			{
				requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSIONS);
			}}
    }

	/**
     * HttpURLConnection POST FILE
     * @param urlString
     * @param paramName
	 * @param filePath
     * @return
     */
	private static String postFile(String urlString, String paramName, String filePath)
	{
		try
		{

	        String BOUNDARY_PREFIX = "--";
	        String LINE_END = "\r\n";
			String boundary = "MyBoundary" + System.currentTimeMillis();

			URL url = new URL(urlString);
            HttpURLConnection urlConnection = null;
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("POST");
			urlConnection.setRequestProperty("Accept-Charset", "UTF-8");
			urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:221.0) Gecko/20100101 Firefox/31.0");
			urlConnection.setRequestProperty("Connection", "Keep-Alive");
			urlConnection.setRequestProperty("Cache-Control", "no-cache");
			urlConnection.setReadTimeout(5000);
			urlConnection.setConnectTimeout(10000);
			urlConnection.setDoOutput(true);

			urlConnection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

			DataOutputStream out = new DataOutputStream(urlConnection.getOutputStream());
            FileInputStream fis = new FileInputStream(filePath);

            String boundaryStr = BOUNDARY_PREFIX + boundary + LINE_END;
            out.write(boundaryStr.getBytes());

            String fileName = new File(filePath).getName();
            String contentDispositionStr = String.format("Content-Disposition: form-data; name=\"%s\"; filename=\"%s\"", paramName, fileName) + LINE_END;
            out.write(contentDispositionStr.getBytes());
            String contentType = "Content-Type: application/octet-stream" + LINE_END + LINE_END;
            out.write(contentType.getBytes());
            int len;
            byte[] buf = new byte[100];
            while ((len = fis.read(buf)) != -1)
			{
                out.write(buf, 0, len);
            }

            out.write(LINE_END.getBytes());
        }
		catch (Exception e)
		{
            Log.e("writeFile", "" + e);
        }
		return "2";
    }

	/**
     * HttpURLConnection POST
     * @param urlString
     * @param params
     * @return
     */
    public String doPost(String urlString, String params)
	{
        try
		{
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = null;
            try
			{
                urlConnection = (HttpURLConnection) url.openConnection();
				//urlConnection.setRequestProperty("Accept-Charset", "UTF-8");
				urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:221.0) Gecko/20100101 Firefox/31.0");
                urlConnection.setRequestMethod("POST");
                urlConnection.setReadTimeout(5000);
                urlConnection.setConnectTimeout(10000);
                urlConnection.setDoOutput(true);

                OutputStream os = urlConnection.getOutputStream();
                os.write(params.getBytes());
                os.flush();
                os.close();

                int responseCode = urlConnection.getResponseCode();
                Log.d(TAG, "" + responseCode);
                if (responseCode == 200)
				{
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));
					StringBuilder sb = new StringBuilder();
					String line = null;
					while ((line = br.readLine()) != null)
					{
						sb.append(line + "\n");
					}
					Log.e(TAG, sb.toString());
					br.close();
					return sb.toString();
                }

            }
			catch (IOException e)
			{
                e.printStackTrace();
            }
			finally
			{
                urlConnection.disconnect();
            }
        }
		catch (MalformedURLException e)
		{
            e.printStackTrace();
        }
        return null;
    }
}
