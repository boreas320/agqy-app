package org.plateau.agqy.app;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends ActionBarActivity {

    public static final String EXTRA_MESSAGE = "login_status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Intent intent;

    public void login(View view) {
        intent = new Intent(this, LoginActivity.class);
//     EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = "登陆成功";

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            // fetch data

            new DownloadWebpageTask().execute("http://10.0.1.4:8080/sdfdsf.json");

        } else {
            message = "卧槽网断了";
        }


        intent.putExtra(EXTRA_MESSAGE, message);
        this.startActivity(
                intent
        );


    }


    public String getLogin(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();


        InputStream inputStream = urlConnection.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));


        String s = "";
        StringBuilder sb = new StringBuilder();
        while ((s = br.readLine()) != null) {
            sb.append(s);
        }

        br.close();
        inputStream.close();
        return sb.toString();

    }

    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return getLogin(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {


            intent.putExtra(EXTRA_MESSAGE, result);
            startActivity( intent);

        }
    }

}
