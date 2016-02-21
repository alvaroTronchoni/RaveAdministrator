package com.examen.raveadministrator.activitisYfragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.examen.raveadministrator.R;
import com.examen.raveadministrator.SimpleDividerItemDecoration;
import com.examen.raveadministrator.adaptadores.RVArtistasAdapter;
import com.examen.raveadministrator.modelos.Artista;
import com.melnykov.fab.FloatingActionButton;

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
 * {@link FragmentFestivalArtistas.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentFestivalArtistas#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentFestivalArtistas extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private OnFragmentInteractionListener mListener;
    private List<Artista> artistas;
    private RVArtistasAdapter adapter;
    private ProgressBar progressBar;
    private String claveApi;
    private FloatingActionButton FABMas;
    private static final String PREFERENCIAS = "Preferencias";

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentFestivalArtistas.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentFestivalArtistas newInstance(String param1, String param2) {
        FragmentFestivalArtistas fragment = new FragmentFestivalArtistas();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentFestivalArtistas() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }

        artistas = new ArrayList<>();

        adapter = new RVArtistasAdapter(artistas);

        SharedPreferences prefs = getActivity().getSharedPreferences(PREFERENCIAS, Context.MODE_PRIVATE);
        claveApi = prefs.getString("claveApi", "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fragment_festival_artistas, container, false);

        View view = container.getRootView();

        FABMas = (FloatingActionButton)view.findViewById(R.id.FABMas);

        progressBar = (ProgressBar)v.findViewById(R.id.progress_bar);

        ImageView back_image = (ImageView) v.findViewById(R.id.back_image);

        String idFestival = container.getTag().toString();

        FetchFestivalDataTask fetchFestivalDataTask = new FetchFestivalDataTask();
        fetchFestivalDataTask.execute(idFestival);

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

        RecyclerView rv = (RecyclerView) view.findViewById(R.id.rvArtistas);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        rv.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        rv.setAdapter(adapter);

        FABMas.attachToRecyclerView(rv);
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
                final String FESTIVAL_BASE_URL = "http://ubuntu.westeurope.cloudapp.azure.com/ApiRAVE/v1/festivales?id="+params[0];

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
                    buffer.append(line+  "\n");
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
                return getFestivalDataFromJson(festivalDataJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            // This will only happen if there was an error getting or parsing the forecast.

            return null;
        }

        private ArrayList<String> getFestivalDataFromJson(String festivalDataJsonStr) throws JSONException {

            JSONObject rootObject = new JSONObject(festivalDataJsonStr);

            JSONObject festivalesObject = rootObject.getJSONObject("festivales");

            JSONArray artistasArray = festivalesObject.getJSONArray("artistas");

            ArrayList<String> arrayResultados = new ArrayList<>();

            for(int i = 0; i < artistasArray.length(); i++){
                JSONObject artistObject = artistasArray.getJSONObject(i);

                String idArtista = artistObject.getString("idArtista");

                String nombreArtista = artistObject.getString("nombreArtista");

                String generoArtista = artistObject.getString("generoArtista");

                String perfilArtista = artistObject.getString("perfilArtista");

                String backUrl = artistObject.getString("backUrl");

                String instagramUser = artistObject.getString("instagramUser");

                String nombreArtistaConsulta = nombreArtista.replace(" ", "%20").replace("&", "%26");

                String popularity = getArtistPopularityFromJSON(nombreArtistaConsulta);

                arrayResultados.add(idArtista+"*"+nombreArtista+"*"+generoArtista+"*"+perfilArtista+"*"+backUrl+"*"+instagramUser+"*"+popularity);
            }

            for (String s : arrayResultados) {
                Log.v(LOG_TAG, "Similar artist entry: " + s);
            }
            return arrayResultados;
        }

        private String getArtistPopularityFromJSON(String nombreArtista) {

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String artistDataJsonStr = null;
            String artista = nombreArtista.replace(" ", "%20").replace("&", "%26");

            try {
                // Construct the URL for the Last.fm query
                final String FESTIVAL_BASE_URL = "https://api.spotify.com/v1/search?q="+artista+"&type=artist&limit=1";

                Uri builtUri = Uri.parse(FESTIVAL_BASE_URL);

                URL url = new URL(builtUri.toString());

                Log.v(LOG_TAG, "Built URI " + builtUri.toString());

                // Create the request to Last.fm, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
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
                artistDataJsonStr = buffer.toString();
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
                return parsePopularityJSON(artistDataJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            // This will only happen if there was an error getting or parsing the forecast.

            return null;
        }

        private String parsePopularityJSON(String artistDataJsonStr) throws JSONException {

            JSONObject rootObject = new JSONObject(artistDataJsonStr);

            JSONObject artistsObject = rootObject.getJSONObject("artists");

            JSONArray itemsArray = artistsObject.getJSONArray("items");

            JSONObject itemsObject = itemsArray.getJSONObject(0);

            return itemsObject.getString("popularity");
        }


        @Override
        protected void onPostExecute(ArrayList<String> result) {

            progressBar.setVisibility(View.GONE);

            if(result!=null){

                artistas.clear();

                for(String festivalDataStr: result){
                    final String[] festivalDataArray = festivalDataStr.split("\\*");
                    String backUrl = festivalDataArray[4].replace("https", "http");
                    artistas.add(new Artista(festivalDataArray[0], festivalDataArray[1], festivalDataArray[2], festivalDataArray[3], backUrl, festivalDataArray[5], festivalDataArray[6]));
                }
            }
        }
    }
}
