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
import android.widget.GridView;
import android.widget.ProgressBar;


import com.examen.raveadministrator.R;
import com.examen.raveadministrator.adaptadores.GVInstagramAdapter;
import com.examen.raveadministrator.modelos.InstagramPhoto;

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
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentFestivalFotos.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentFestivalFotos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentArtistaFotos extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private OnFragmentInteractionListener mListener;
    private ArrayList<InstagramPhoto> instagramPhotos;
    private ProgressBar progressBar;
    private GVInstagramAdapter adapter;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentFestivalFotos.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentArtistaFotos newInstance(String param1, String param2) {
        FragmentArtistaFotos fragment = new FragmentArtistaFotos();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentArtistaFotos() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }

        instagramPhotos = new ArrayList<>();

        adapter = new GVInstagramAdapter(getActivity(), instagramPhotos);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fragment_festival_fotos, container, false);

        progressBar = (ProgressBar)v.findViewById(R.id.progress_bar);

        String[] tagArray = container.getTag().toString().split("\\*");
        String result = tagArray[1];

        FetchFestivalIntagramPhotosTask fetchFestivalIntagramPhotosTask =  new FetchFestivalIntagramPhotosTask();
        fetchFestivalIntagramPhotosTask.execute(result);

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

        GridView gridViewInstagram = (GridView) view.findViewById(R.id.gridViewInstagram);
        gridViewInstagram.setAdapter(adapter);
    }

    public class FetchFestivalIntagramPhotosTask extends AsyncTask<String, Void, ArrayList<String>> {

        private final String LOG_TAG = FetchFestivalIntagramPhotosTask.class.getSimpleName();

        @Override
        protected ArrayList<String> doInBackground(String... params) {

            String festival = params[0];

            // If there's no artist name, there's nothing to look up.  Verify size of params.
            if (params.length == 0) {
                return null;
            }

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String instagramPhotosJsonStr = null;

            try {
                // Construct the URL for the Instagram query
                final String FESTIVALS_BASE_URL = "https://www.instagram.com/"+festival+"/media/";

                Uri builtUri = Uri.parse(FESTIVALS_BASE_URL);

                URL url = new URL(builtUri.toString());

                Log.v(LOG_TAG, "Built URI " + builtUri.toString());

                // Create the request to Instagram, and open the connection
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
                instagramPhotosJsonStr = buffer.toString();
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
                return getFestivalInstagramPhotosFromJson(instagramPhotosJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            // This will only happen if there was an error getting or parsing the forecast.

            return null;
        }

        private ArrayList<String> getFestivalInstagramPhotosFromJson(String instagramPhotosJsonStr) throws JSONException {

            JSONObject rootObject = new JSONObject(instagramPhotosJsonStr);

            JSONArray itemsArray = rootObject.getJSONArray("items");

            ArrayList<String> arrayResultados = new ArrayList<>();

            for(int i = 0; i < itemsArray.length(); i++){
                JSONObject imageObject = itemsArray.getJSONObject(i);

                JSONObject imagesObject = imageObject.getJSONObject("images");

                JSONObject standard_resolution_object = imagesObject.getJSONObject("standard_resolution");

                String imageUrl = standard_resolution_object.getString("url");

                String url = imageObject.getString("link");

                arrayResultados.add(imageUrl+"*"+url);
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

                instagramPhotos.clear();

                for(String festivalInstagramPhotosStr: result){
                    String[] festivalInstagramPhotosArray = festivalInstagramPhotosStr.split("\\*");
                    String imageUrl = festivalInstagramPhotosArray[0].replace("\\", "").replace("https", "http");
                    String url = festivalInstagramPhotosArray[1].replace("\\", "");
                    instagramPhotos.add(new InstagramPhoto(imageUrl, url));
                    adapter.setGridData(instagramPhotos);
                }
            }
        }
    }
}
