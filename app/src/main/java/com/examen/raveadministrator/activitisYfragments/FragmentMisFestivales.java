package com.examen.raveadministrator.activitisYfragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.examen.raveadministrator.R;
import com.examen.raveadministrator.adaptadores.RVFestivalesAdapter;
import com.examen.raveadministrator.modelos.Clasificacion;
import com.examen.raveadministrator.modelos.Festival;

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
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentMisFestivales.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentMisFestivales#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentMisFestivales extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = "Festivales";
    private static final String ARG_PARAM2 = "param2";

    private OnFragmentInteractionListener mListener;
    private List<Festival> festivales;
    private RVFestivalesAdapter adapter;
    private ProgressBar progressBar;
    private String claveApi;
    private Clasificacion clasificacion;
    private String admin;
    private String idAdmin;
    double latitude;
    double longitude;
    private static final String PREFERENCIAS = "Preferencias";

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentFestivales.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentMisFestivales newInstance(String param1, String param2) {
        FragmentMisFestivales fragment = new FragmentMisFestivales();
        Bundle args = new Bundle();
        args.putString(TAG, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentMisFestivales() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mParam1 = getArguments().getString(TAG);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("FESTIVALES");

        festivales = new ArrayList<>();
        /*festivales.add(new Festival("1", "Medusa Sunbeach Festival", "Cullera", R.drawable.logo_medusa, R.drawable.back_medusa));
        festivales.add(new Festival("2", "Marenostrum Music Festival", "Valencia", R.drawable.logo_marenostrum, R.drawable.back_marenostrum));
        festivales.add(new Festival("3", "Barcelona Beach Festival", "Barcelona", R.drawable.logo_medusa, R.drawable.back_medusa));
        festivales.add(new Festival("4", "Dreambeach Villaricos", "Almería", R.drawable.logo_marenostrum, R.drawable.back_marenostrum));
        festivales.add(new Festival("5", "FIB", "Benicassim", R.drawable.logo_medusa, R.drawable.back_medusa));
        festivales.add(new Festival("6", "Arenal Sound", "Burriana", R.drawable.logo_marenostrum, R.drawable.back_marenostrum));
        festivales.add(new Festival("7", "BBK Bilbao Live", "Bilbao", R.drawable.logo_medusa, R.drawable.back_medusa));
        festivales.add(new Festival("8", "Electrofallas", "Valencia", R.drawable.logo_marenostrum, R.drawable.back_marenostrum));*/

        adapter = new RVFestivalesAdapter(festivales);

        SharedPreferences prefs = getActivity().getSharedPreferences(PREFERENCIAS, Context.MODE_PRIVATE);
        claveApi = prefs.getString("claveApi", "");
        admin = prefs.getString("admin","");
        idAdmin = prefs.getString("id","");
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set title
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("FESTIVALES");
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fragment_mis_festivales, container, false);

        progressBar = (ProgressBar)v.findViewById(R.id.progress_bar);

        Bundle bundle = getArguments();

       /* if (bundle != null) {
            clasificacion = (Clasificacion) bundle.getSerializable("Clasificacion");
            genero = clasificacion.getNombreClasificacion();
        }*/

        FetchFestivalsDataTask fetchFestivalsDataTask = new FetchFestivalsDataTask();
        fetchFestivalsDataTask.execute(idAdmin);

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("FESTIVALES");
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView rv = (RecyclerView) view.findViewById(R.id.rvFestivales);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        rv.setAdapter(adapter);
    }

    public class FetchFestivalsDataTask extends AsyncTask<String, Void, ArrayList<String>> {

        private final String LOG_TAG = FetchFestivalsDataTask.class.getSimpleName();

        @Override
        protected ArrayList<String> doInBackground(String... params) {

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String festivalsJsonStr = null;

            try {
                // Construct the URL for the Instagram query
                final String FESTIVALS_BASE_URL = "http://ubuntu.westeurope.cloudapp.azure.com/ApiRAVE/v1/festivales?idAdministrador=" +params[0];

                Uri builtUri = Uri.parse(FESTIVALS_BASE_URL);

                URL url = new URL(builtUri.toString());

                Log.v(LOG_TAG, "Built URI " + builtUri.toString());

                // Create the request to Instagram, and open the connection
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
                    buffer.append(line+  "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                festivalsJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the artist data, there's no point in attempting
                // to parse it.
                return null;
            } finally{
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
                return getFestivalsDataFromJson(festivalsJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            // This will only happen if there was an error getting or parsing the forecast.

            return null;
        }

        private ArrayList<String> getFestivalsDataFromJson(String festivalsJsonStr) throws JSONException {

            JSONObject rootObject = new JSONObject(festivalsJsonStr);

            JSONArray festivalesArray = rootObject.getJSONArray("festivales");

            ArrayList<String> arrayResultados = new ArrayList<>();

            for(int i = 0; i < festivalesArray.length(); i++){
                JSONObject festivalObject = festivalesArray.getJSONObject(i);

                String id = festivalObject.getString("idFestival");

                String nombreFestival = festivalObject.getString("nombreFestival");

                String ubicacionFestival = festivalObject.getString("ubicacionFestival");

                String logoUrl = festivalObject.getString("logoUrl");

                String backUrl = festivalObject.getString("back_Url");

                arrayResultados.add(id+"*"+nombreFestival+"*"+ubicacionFestival+"*"+logoUrl+"*"+backUrl);
            }

            for (String s : arrayResultados) {
                Log.v(LOG_TAG, "Similar artist entry: " + s);
            }
            return arrayResultados;
        }


        @Override
        protected void onPostExecute(ArrayList<String> result) {

            progressBar.setVisibility(View.GONE);

            if(result!=null){

                festivales.clear();

                for(String festivalStr: result){
                    String[] festivalArray = festivalStr.split("\\*");
                    String backUrl = festivalArray[4].replace("https", "http");
                    festivales.add(new Festival(festivalArray[0], festivalArray[1], festivalArray[2], festivalArray[3], backUrl));
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            FetchNearbyFestivalsDataTask fetchNearbyFestivalsDataTask = new FetchNearbyFestivalsDataTask();
            fetchNearbyFestivalsDataTask.execute(String.valueOf(latitude), String.valueOf(longitude));
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    public class FetchNearbyFestivalsDataTask extends AsyncTask<String, Void, ArrayList<String>> {

        private final String LOG_TAG = FetchNearbyFestivalsDataTask.class.getSimpleName();

        @Override
        protected ArrayList<String> doInBackground(String... params) {

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String festivalsJsonStr = null;

            try {
                // Construct the URL for the Instagram query
                final String FESTIVALS_BASE_URL = "http://ubuntu.westeurope.cloudapp.azure.com/ApiRAVE/v1/festivales?latUsuario="+params[0]+"&lngUsuario="+params[1];

                Uri builtUri = Uri.parse(FESTIVALS_BASE_URL);

                URL url = new URL(builtUri.toString());

                Log.v(LOG_TAG, "Built URI " + builtUri.toString());

                // Create the request to Instagram, and open the connection
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
                    buffer.append(line+  "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                festivalsJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the artist data, there's no point in attempting
                // to parse it.
                return null;
            } finally{
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
                return getFestivalsDataFromJson(festivalsJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            // This will only happen if there was an error getting or parsing the forecast.

            return null;
        }

        private ArrayList<String> getFestivalsDataFromJson(String festivalsJsonStr) throws JSONException {

            JSONObject rootObject = new JSONObject(festivalsJsonStr);

            JSONArray festivalesArray = rootObject.getJSONArray("festivales");

            ArrayList<String> arrayResultados = new ArrayList<>();

            for(int i = 0; i < festivalesArray.length(); i++){
                JSONObject festivalObject = festivalesArray.getJSONObject(i);

                String id = festivalObject.getString("id");

                String nombreFestival = festivalObject.getString("nombre");

                String ubicacionFestival = festivalObject.getString("ubicacion");

                String logoUrl = festivalObject.getString("logo_url");

                String backUrl = festivalObject.getString("back_url");

                arrayResultados.add(id+"*"+nombreFestival+"*"+ubicacionFestival+"*"+logoUrl+"*"+backUrl);
            }

            for (String s : arrayResultados) {
                Log.v(LOG_TAG, "Similar artist entry: " + s);
            }
            return arrayResultados;
        }


        @Override
        protected void onPostExecute(ArrayList<String> result) {

            progressBar.setVisibility(View.GONE);

            if(result!=null){

                festivales.clear();

                for(String festivalStr: result){
                    String[] festivalArray = festivalStr.split("\\*");
                    String backUrl = festivalArray[4].replace("https", "http");
                    festivales.add(new Festival(festivalArray[0], festivalArray[1], festivalArray[2], festivalArray[3], backUrl));
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }
}
