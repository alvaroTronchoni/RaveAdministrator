package com.examen.raveadministrator.activitisYfragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.examen.raveadministrator.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * A fragment that launches other parts of the demo application.
 */
public class FragmentFestivalUbicacion extends Fragment {

    private MapView mMapView;
    private GoogleMap googleMap;
    private String idFestival;
    private String claveApi;
    private RelativeLayout rlPlaces;
    private ProgressDialog progressDialog;
    private static final String PREFERENCIAS = "Preferencias";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflat and return the layout
        View v = inflater.inflate(R.layout.fragment_fragment_festival_ubicacion, container,
                false);

        SharedPreferences prefs = getActivity().getSharedPreferences(PREFERENCIAS, Context.MODE_PRIVATE);
        claveApi = prefs.getString("claveApi", "");

        idFestival = container.getTag().toString();

        rlPlaces = (RelativeLayout)v.findViewById(R.id.rlPlaces);

        mMapView = (MapView) v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        googleMap = mMapView.getMap();

        FetchFestivalDataTask fetchFestivalDataTask = new FetchFestivalDataTask();
        fetchFestivalDataTask.execute(idFestival);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    public class FetchFestivalDataTask extends AsyncTask<String, Void, ArrayList<String>> {

        private final String LOG_TAG = FetchFestivalDataTask.class.getSimpleName();

        @Override
        protected ArrayList<String> doInBackground(String... params) {

            // If there's no artist name, there's nothing to look up.  Verify size of params.
            if (params.length == 0) {
                return null;
            }

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String festivalDataJsonStr = null;

            try {
                // Construct the URL for the Last.fm query
                final String FESTIVAL_BASE_URL = "http://ubuntu.westeurope.cloudapp.azure.com/ApiRAVE/v1/festivales?id=" + params[0];

                Uri builtUri = Uri.parse(FESTIVAL_BASE_URL);

                URL url = new URL(builtUri.toString());

                Log.v(LOG_TAG, "Built URI " + builtUri.toString());

                // Create the request to Last.fm, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Authorization", claveApi);
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                festivalDataJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the artist data, there's no point in attempting
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getFestivalDataFromJson(festivalDataJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            // This will only happen if there was an error getting or parsing the forecast.

            return null;
        }

        private ArrayList<String> getFestivalDataFromJson(String festivalDataJsonStr) throws JSONException {

            ArrayList<String> arrayResultados = new ArrayList<>();

            JSONObject rootObject = new JSONObject(festivalDataJsonStr);

            JSONObject festivalesObject = rootObject.getJSONObject("festivales");

            JSONArray infoArray = festivalesObject.getJSONArray("info");

            JSONObject infoObject = infoArray.getJSONObject(0);

            String nombreFestival = infoObject.getString("nombreFestival");

            String latitudFestival = infoObject.getString("latitudFestival");

            String longitudFestival = infoObject.getString("longitudFestival");

            arrayResultados.add(nombreFestival + "*" + latitudFestival + "*" + longitudFestival);

            for (String s : arrayResultados) {
                Log.v(LOG_TAG, "Similar artist entry: " + s);
            }
            return arrayResultados;
        }


        @Override
        protected void onPostExecute(ArrayList<String> result) {

            if (result != null) {
                for (String festivalDataStr : result) {
                    final String[] festivalDataArray = festivalDataStr.split("\\*");
                    String nombreFestival = festivalDataArray[0];
                    double latitude = Double.parseDouble(festivalDataArray[1]);
                    double longitude = Double.parseDouble(festivalDataArray[2]);
                    // create marker
                    MarkerOptions marker = new MarkerOptions().position(
                            new LatLng(latitude, longitude)).title(nombreFestival);

                    // Changing marker icon
                    marker.icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_RED));

                    // adding marker
                    googleMap.addMarker(marker);
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(new LatLng(latitude,
                                    longitude)).zoom(12).build();
                    googleMap.animateCamera(CameraUpdateFactory
                            .newCameraPosition(cameraPosition));

                    googleMap.setMyLocationEnabled(true);

                    // Perform any camera updates here

                    rlPlaces.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            progressDialog = new ProgressDialog(getActivity(),
                                    R.style.Base_Theme_AppCompat_Dialog);
                            progressDialog.setIndeterminate(true);
                            progressDialog.setMessage("Consultando sitios...");
                            progressDialog.show();

                            FetchFestivalLatLongDataTask fetchFestivalLatLongDataTask = new FetchFestivalLatLongDataTask();
                            fetchFestivalLatLongDataTask.execute(idFestival);
                        }
                    });
                }
            }
        }
    }

    public class FetchFestivalLatLongDataTask extends AsyncTask<String, Void, ArrayList<String>> {

        private final String LOG_TAG = FetchFestivalLatLongDataTask.class.getSimpleName();

        @Override
        protected ArrayList<String> doInBackground(String... params) {

            // If there's no artist name, there's nothing to look up.  Verify size of params.
            if (params.length == 0) {
                return null;
            }

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String festivalDataJsonStr = null;

            try {
                // Construct the URL for the Last.fm query
                final String FESTIVAL_BASE_URL = "http://ubuntu.westeurope.cloudapp.azure.com/ApiRAVE/v1/festivales?id=" + params[0];

                Uri builtUri = Uri.parse(FESTIVAL_BASE_URL);

                URL url = new URL(builtUri.toString());

                Log.v(LOG_TAG, "Built URI " + builtUri.toString());

                // Create the request to Last.fm, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Authorization", claveApi);
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                festivalDataJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the artist data, there's no point in attempting
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getFestivalDataFromJson(festivalDataJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            // This will only happen if there was an error getting or parsing the forecast.

            return null;
        }

        private ArrayList<String> getFestivalDataFromJson(String festivalDataJsonStr) throws JSONException {

            ArrayList<String> arrayResultados = new ArrayList<>();

            JSONObject rootObject = new JSONObject(festivalDataJsonStr);

            JSONObject festivalesObject = rootObject.getJSONObject("festivales");

            JSONArray infoArray = festivalesObject.getJSONArray("info");

            JSONObject infoObject = infoArray.getJSONObject(0);

            String latitudFestival = infoObject.getString("latitudFestival");

            String longitudFestival = infoObject.getString("longitudFestival");

            arrayResultados.add(latitudFestival);

            arrayResultados.add(longitudFestival);

            for (String s : arrayResultados) {
                Log.v(LOG_TAG, "Similar artist entry: " + s);
            }
            return arrayResultados;
        }


        @Override
        protected void onPostExecute(ArrayList<String> result) {

            if (result != null) {

                String latitudFestival = result.get(0);

                String longitudFestival = result.get(1);

                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                if (!latitudFestival.equals("") && !longitudFestival.equals("")) {
                    builder.setLatLngBounds(new LatLngBounds(new LatLng(Double.parseDouble(latitudFestival)-0.001, Double.parseDouble(longitudFestival)-0.001), new LatLng(Double.parseDouble(latitudFestival)+0.001, Double.parseDouble(longitudFestival)+0.001)));
                }
                try {
                    getParentFragment().startActivityForResult(builder.build(getActivity()), 1);
                    progressDialog.dismiss();
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}