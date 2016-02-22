package com.examen.raveadministrator.activitisYfragments;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.examen.raveadministrator.R;
import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ObservableScrollView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentArtistaDescripcion.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentArtistaDescripcion#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentArtistaDescripcion extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private OnFragmentInteractionListener mListener;
    private TextView textoDescripcion;
    private ProgressBar progressBar;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentArtistaDescripcion.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentArtistaDescripcion newInstance(String param1, String param2) {
        FragmentArtistaDescripcion fragment = new FragmentArtistaDescripcion();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentArtistaDescripcion() {
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
        View v = inflater.inflate(R.layout.fragment_fragment_artista_descripcion, container, false);

        View view = container.getRootView();

        FloatingActionButton FABMas = (FloatingActionButton)view.findViewById(R.id.FABMas);

        ObservableScrollView sv_descripcion = (ObservableScrollView)v.findViewById(R.id.sv_descripcion);

        FABMas.attachToScrollView(sv_descripcion);

        progressBar = (ProgressBar)v.findViewById(R.id.progress_bar);

        textoDescripcion = (TextView)v.findViewById(R.id.texto_descripcion);

        String[] tagArray = container.getTag().toString().split("\\*");
        String result = tagArray[0];

        FetchArtistDataTask fetchArtistDataTask =  new FetchArtistDataTask();
        fetchArtistDataTask.execute(result);

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

    public class FetchArtistDataTask extends AsyncTask<String, Void, String> {

        private final String LOG_TAG = FetchArtistDataTask.class.getSimpleName();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {

            String artista;

            // If there's no artist name, there's nothing to look up.  Verify size of params.
            if (params.length == 0) {
                return null;
            }

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnectionBiography = null;
            BufferedReader readerBiography = null;

            // Will contain the raw JSON response as a string.
            String biographyArtistJsonStr = null;
            artista = params[0].replace(" ", "%20").replace("&", "%26");

            try {
                // Construct the URL for the Echo Nest query
                final String ARTISTS_BASE_URL = "http://ws.audioscrobbler.com/2.0/?method=artist.getInfo&artist="+artista+"&api_key=e0ed07c7f7a0f8ddb7fd91675e78fa63&format=json";

                Uri builtUri = Uri.parse(ARTISTS_BASE_URL);

                URL url = new URL(builtUri.toString());

                Log.v(LOG_TAG, "Built URI " + builtUri.toString());

                // Create the request to Echo Nest, and open the connection
                urlConnectionBiography = (HttpURLConnection) url.openConnection();
                urlConnectionBiography.setRequestMethod("GET");
                urlConnectionBiography.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnectionBiography.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                readerBiography = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = readerBiography.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line+  "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                biographyArtistJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the artist data, there's no point in attempting
                // to parse it.
                return null;
            } finally{
                if (urlConnectionBiography != null) {
                    urlConnectionBiography.disconnect();
                }
                if (readerBiography != null) {
                    try {
                        readerBiography.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getArtistBiographyFromJson(biographyArtistJsonStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        private String getArtistBiographyFromJson(String biographyArtistJsonStr) throws JSONException {

            JSONObject rootObject = new JSONObject(biographyArtistJsonStr);

            JSONObject artistObject = rootObject.getJSONObject("artist");

            JSONObject bioObject = artistObject.getJSONObject("bio");

            String summary = bioObject.getString("summary");

            int firstIndex = summary.indexOf(" <");

            summary = summary.substring(0, firstIndex);

            return summary;
        }

        @Override
        protected void onPostExecute(String result) {

            progressBar.setVisibility(View.GONE);

            if(result!=null){
                textoDescripcion.setText(result);
            }
        }
    }
}
