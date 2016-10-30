package com.example.nande.sunshine;


import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ForecastFragment extends Fragment {
ListView listView;

    public ForecastFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_main, container, false);
        listView=(ListView) view.findViewById(R.id.listview_forecast);
        List<String> forecast_list =new ArrayList<String>();
        forecast_list.add("temp is 75");
        forecast_list.add("temp is 67");
        forecast_list.add("temp is 84");
        forecast_list.add("temp is 95");
        forecast_list.add("temp is 53");
        ArrayAdapter<String> adapter=
                new ArrayAdapter<String>(getContext(),R.layout.list_item_forecast,R.id.list_item_forecast_textview,forecast_list);
        // Inflate the layout for this fragment
        listView.setAdapter(adapter);





        return view;


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_forecast,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();

        if(id==R.id.action_refresh){
          click_triggered();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void click_triggered(){
        FetchWeatherTask backgroundtask=new FetchWeatherTask();
        backgroundtask.execute("64468");
    }

    public class FetchWeatherTask extends AsyncTask<String,Void,Void>{

        public final String Log_tag= FetchWeatherTask.class.getSimpleName();

        @Override
        protected Void doInBackground(String... params) {

            if(params.length==0){
                return null;
            }

            // NETWORK CODE
            BufferedReader reader=null;
            HttpURLConnection urlConnection=null;

            String forecastjsonStr=null;
            String format="json";
            String units="metric";
            int numDays=7;


            try{
               /* String base_url="http://api.openweathermap.org/data/2.5/weather?zip=64468,us&units=metric";
                String api_key="&APPID="+BuildConfig.OPEN_WEATHER_MAP_API_KEY;
                URL url=new URL(base_url.concat(api_key));*/

               // URL url =new URL("http://api.openweathermap.org/data/2.5/forecast/daily?zip=64468,us&units=metric&count=7&APPID=df017260489c94f1a58ccfdb6a940177");
                final String FORECAST_BASE_URL="http://api.openweathermap.org/data/2.5/forecast/daily?";
                final String QUERY_PARAM="q";
                final String FORMAT_PARAM="mode";
                final String UNITS_PARAM="units";
                final String DAYS_PARAM="cnt";
                final String APPID_PARAM="APPID";

                Uri builturi= Uri.parse(FORECAST_BASE_URL).buildUpon()
                        .appendQueryParameter(QUERY_PARAM,params[0])
                        .appendQueryParameter(FORMAT_PARAM,format)
                        .appendQueryParameter(UNITS_PARAM,units)
                        .appendQueryParameter(DAYS_PARAM,Integer.toString(numDays))
                        .appendQueryParameter(APPID_PARAM,BuildConfig.OPEN_WEATHER_MAP_API_KEY).build();
                URL url=new URL(builturi.toString());
                Log.d("url",builturi.toString());
                urlConnection=(HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                //read inputstream into a string
                InputStream input=urlConnection.getInputStream();
                StringBuffer buffer=new StringBuffer();
                if(input==null){
                    return null;
                }
                reader=new BufferedReader(new InputStreamReader(input));

                String line;
                while((line=reader.readLine())!=null){
                    //Log.d("deb","inside while");
                    buffer.append(line+"\n");
                }
                if(buffer.length()==0){
                    return null;
                }
                forecastjsonStr=buffer.toString();

                Log.v(Log_tag,"Data in Json from internet is: "+forecastjsonStr);


            }catch (Exception e){
                Log.e(Log_tag,"Error in network code",e);
            }finally {
                if(urlConnection!=null){
                    urlConnection.disconnect();
                }
                if(reader!=null){
                    try{
                        reader.close();}catch (IOException e){
                        Log.e(Log_tag,"Error closing reader",e);
                    }
                }
            }




            return null;
        }


    }

}
