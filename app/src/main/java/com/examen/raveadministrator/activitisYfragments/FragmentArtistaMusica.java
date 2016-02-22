package com.examen.raveadministrator.activitisYfragments;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
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
import com.examen.raveadministrator.adaptadores.RVTopTracksAdapter;
import com.examen.raveadministrator.modelos.TopTrack;
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
 * {@link FragmentArtistaMusica.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentArtistaMusica#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentArtistaMusica extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private OnFragmentInteractionListener mListener;
    private List<TopTrack> topTracks;
    private RVTopTracksAdapter adapter;
    private ProgressBar progressBar;
    private FloatingActionButton FABMas;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentArtistaMusica.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentArtistaMusica newInstance(String param1, String param2) {
        FragmentArtistaMusica fragment = new FragmentArtistaMusica();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentArtistaMusica() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }

        topTracks = new ArrayList<>();

        adapter = new RVTopTracksAdapter(topTracks);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fragment_artista_musica, container, false);

        View view = container.getRootView();

        FABMas = (FloatingActionButton)view.findViewById(R.id.FABMas);

        progressBar = (ProgressBar)v.findViewById(R.id.progress_bar);

        ImageView back_image = (ImageView) v.findViewById(R.id.back_image);
        //Picasso.with(getActivity()).load(R.drawable.wallpaper_login).fit().centerCrop().into(back_image);

        //define this only once if blurring multiple times
        RenderScript rs = RenderScript.create(getActivity());

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        Bitmap blurTemplate = BitmapFactory.decodeResource(getResources(), R.drawable.wallpaper_login, options);

        //this will blur the bitmapOriginal with a radius of 8 and save it in bitmapOriginal
        final Allocation input = Allocation.createFromBitmap(rs, blurTemplate); //use this constructor for best performance, because it uses USAGE_SHARED mode which reuses memory
        final Allocation output = Allocation.createTyped(rs, input.getType());
        final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        script.setRadius(8f);
        script.setInput(input);
        script.forEach(output);
        output.copyTo(blurTemplate);

        Drawable back = new BitmapDrawable(blurTemplate);
        //back_image.setImageDrawable(back);

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

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView rv = (RecyclerView) view.findViewById(R.id.rvTopTracks);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        rv.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        rv.setAdapter(adapter);

        FABMas.attachToRecyclerView(rv);
    }

    public class FetchArtistDataTask extends AsyncTask<String, Void, ArrayList<String>> {

        private final String LOG_TAG = FetchArtistDataTask.class.getSimpleName();

        @Override
        protected ArrayList<String> doInBackground(String... params) {

            String artista = "";
            String id = "";
            ArrayList<String> resultados = null;

            // If there's no artist name, there's nothing to look up.  Verify size of params.
            if (params.length == 0) {
                return null;
            }

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnectionId = null;
            BufferedReader readerId = null;

            // Will contain the raw JSON response as a string.
            String idArtistJsonStr = null;
            artista = params[0].replace(" ", "%20").replace("&", "%26");

            try {
                // Construct the URL for the Spotify query
                final String ARTISTS_BASE_URL = "https://api.spotify.com/v1/search?q="+artista+"&type=artist&limit=1";

                Uri builtUri = Uri.parse(ARTISTS_BASE_URL);

                URL url = new URL(builtUri.toString());

                Log.v(LOG_TAG, "Built URI " + builtUri.toString());

                // Create the request to Last.fm, and open the connection
                urlConnectionId = (HttpURLConnection) url.openConnection();
                urlConnectionId.setRequestMethod("GET");
                urlConnectionId.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnectionId.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                readerId = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = readerId.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line+  "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                idArtistJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the artist data, there's no point in attempting
                // to parse it.
                return null;
            } finally{
                if (urlConnectionId != null) {
                    urlConnectionId.disconnect();
                }
                if (readerId != null) {
                    try {
                        readerId.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                 id = getArtistIdFromJson(idArtistJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            // If there's no artist name, there's nothing to look up.  Verify size of params.
            if (params.length == 0) {
                return null;
            }

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String topTracksArtistJsonStr = null;

            try {
                // Construct the URL for the Spotify query
                final String ARTISTS_BASE_URL = "https://api.spotify.com/v1/artists/"+id+"/top-tracks?country=ES";

                Uri builtUri = Uri.parse(ARTISTS_BASE_URL);

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
                topTracksArtistJsonStr = buffer.toString();
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
                resultados = getArtistTopTracksFromJson(topTracksArtistJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return resultados;
        }

        private String getArtistIdFromJson(String idArtistJsonStr) throws JSONException {

            JSONObject rootObject = new JSONObject(idArtistJsonStr);

            JSONObject artistsObject = rootObject.getJSONObject("artists");

            JSONArray itemsArray = artistsObject.getJSONArray("items");

            JSONObject itemsObject = itemsArray.getJSONObject(0);

            String id = itemsObject.getString("id");

            Log.v(LOG_TAG, "Id artist entry: " + id);

            return id;
        }

        private ArrayList<String> getArtistTopTracksFromJson(String topTracksArtistJsonStr) throws JSONException {

            ArrayList<String> resultados = new ArrayList<>();
            String imageUrl = "";
            int duration_ms;
            String name = "";
            String uri = "";
            int popularity;

            JSONObject rootObject = new JSONObject(topTracksArtistJsonStr);

            JSONArray tracksArray = rootObject.getJSONArray("tracks");

            for(int i = 0; i < tracksArray.length(); i++){

                JSONObject trackObject = tracksArray.getJSONObject(i);

                JSONObject albumObject = trackObject.getJSONObject("album");

                JSONArray imagesArray = albumObject.getJSONArray("images");

                JSONObject imageObject = imagesArray.getJSONObject(0);

                imageUrl = imageObject.getString("url");

                duration_ms = trackObject.getInt("duration_ms");

                name = trackObject.getString("name");

                uri = trackObject.getString("uri");

                popularity = trackObject.getInt("popularity");

                String artists = getArtistsDataFromJson(trackObject);

                resultados.add(imageUrl +"*"+duration_ms+"*"+name+"*"+uri+"*"+artists+"*"+popularity);
            }

            return resultados;
        }

        private String getArtistsDataFromJson(JSONObject trackObject) throws JSONException {

            String artists = "";

            JSONArray artistsArray = trackObject.getJSONArray("artists");

            for(int j = 0; j < artistsArray.length(); j++){

                JSONObject artistObject = artistsArray.getJSONObject(j);

                String nombreArtista = artistObject.getString("name");

                artists = artists+nombreArtista+", ";
            }

            return artists;
        }


        @Override
        protected void onPostExecute(ArrayList<String> result) {

            progressBar.setVisibility(View.GONE);

            if(result!=null){

                topTracks.clear();

                for(String trackDataStr: result){
                    String[] trackDataArray = trackDataStr.split("\\*");
                    String duration_ms = String.valueOf(trackDataArray[1]);
                    String popularity = String.valueOf(trackDataArray[5]);
                    topTracks.add(new TopTrack(trackDataArray[0], duration_ms, trackDataArray[2], trackDataArray[3], trackDataArray[4], popularity));
                }
            }
        }
    }

}
