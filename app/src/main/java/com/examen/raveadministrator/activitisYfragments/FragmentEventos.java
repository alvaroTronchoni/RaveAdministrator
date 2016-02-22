package com.examen.raveadministrator.activitisYfragments;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
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
import com.examen.raveadministrator.SimpleDividerItemDecoration;
import com.examen.raveadministrator.adaptadores.RVEventosAdapter;
import com.examen.raveadministrator.modelos.Evento;

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
 * {@link FragmentEventos.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentEventos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentEventos extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private OnFragmentInteractionListener mListener;
    private List<Evento> eventos;
    private RVEventosAdapter adapter;
    private String nombreArtista;
    private ProgressBar progressBar;
    private boolean d;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentEventos.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentEventos newInstance(String param1, String param2) {
        FragmentEventos fragment = new FragmentEventos();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentEventos() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fragment_eventos, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        progressBar = (ProgressBar)v.findViewById(R.id.progress_bar);

        Bundle bundle = getArguments();

        if (bundle != null) {
            nombreArtista = bundle.getString("nombreArtista");
        }

        eventos = new ArrayList<>();

        adapter = new RVEventosAdapter(eventos);

        FetchArtistDataTask fetchArtistDataTask = new FetchArtistDataTask();
        fetchArtistDataTask.execute(nombreArtista);

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
     * <p>
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

        RecyclerView rv = (RecyclerView) view.findViewById(R.id.rvEventos);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        rv.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        rv.setAdapter(adapter);

    }

    public class FetchArtistDataTask extends AsyncTask<String, Void, ArrayList<String>> {

        private final String LOG_TAG = FetchArtistDataTask.class.getSimpleName();

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
            String artistDataJsonStr = null;
            String artista = params[0].replace(" ", "%20").replace("&", "%26");

            try {
                // Construct the URL for the Last.fm query
                final String ARTIST_BASE_URL = "http://api.bandsintown.com/artists/"+artista+"/events.json?api_version=2.0&app_id=RAVE";

                Uri builtUri = Uri.parse(ARTIST_BASE_URL);

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
                return getArtistDataFromJson(artistDataJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            // This will only happen if there was an error getting or parsing the forecast.

            return null;
        }

        private ArrayList<String> getArtistDataFromJson(String artistDataJsonStr) throws JSONException {

            ArrayList<String> arrayResultados =  new ArrayList<>();

            JSONArray rootArray = new JSONArray(artistDataJsonStr);

            for(int i = 0; i < rootArray.length(); i++){

                JSONObject eventoObject = rootArray.getJSONObject(i);

                String nombreEvento = eventoObject.getString("title");

                String formatted_datetime = eventoObject.getString("formatted_datetime");

                String formatted_location = eventoObject.getString("formatted_location");

                String ticket_status = eventoObject.getString("ticket_status");

                String ticket_url = eventoObject.getString("ticket_url");

                JSONObject venueObject = eventoObject.getJSONObject("venue");

                double latitude = venueObject.getDouble("latitude");

                double longitude = venueObject.getDouble("longitude");

                arrayResultados.add(nombreEvento+"*"+formatted_datetime+"*"+formatted_location+"*"+ticket_status+"*"+ticket_url+"*"+ String.valueOf(latitude)+"*"+ String.valueOf(longitude));
            }

            for (String s : arrayResultados) {
                Log.v(LOG_TAG, "Similar artist entry: " + s);
            }
            return arrayResultados;
        }


        @Override
        protected void onPostExecute(final ArrayList<String> result) {

            progressBar.setVisibility(View.GONE);

            if(result!=null){

                eventos.clear();

                for(String resultStr : result){
                    String[] resultArray = resultStr.split("\\*");
                    eventos.add(new Evento(resultArray[0], resultArray[1], resultArray[2], resultArray[3], resultArray[4], resultArray[5], resultArray[6]));
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }
}
