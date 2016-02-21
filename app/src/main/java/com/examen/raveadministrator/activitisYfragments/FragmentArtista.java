package com.examen.raveadministrator.activitisYfragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cocosw.bottomsheet.BottomSheet;
import com.examen.raveadministrator.R;
import com.examen.raveadministrator.adaptadores.PagerAdapterArtista;
import com.examen.raveadministrator.modelos.Artista;
import com.melnykov.fab.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentFestival.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentFestival#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentArtista extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private OnFragmentInteractionListener mListener;
    private Artista artista;
    private ImageView ic_facebook;
    private TextView seguidoresArtista;
    private TextView oyentesArtista;
    private String nombreArtista;
    private String webUrl;
    private String facebookUrl;
    private String idArtista;
    private String claveApi;
    private BottomSheet bottomSheet;
    private static final String PREFERENCIAS = "Preferencias";

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentFestival.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentFestival newInstance(String param1, String param2) {
        FragmentFestival fragment = new FragmentFestival();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentArtista() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");

        ColorDrawable colorDrawable = new ColorDrawable(android.R.color.transparent);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(colorDrawable);

        SharedPreferences prefs = getActivity().getSharedPreferences(PREFERENCIAS, Context.MODE_PRIVATE);
        claveApi = prefs.getString("claveApi", "");
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set title
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");


        ColorDrawable colorDrawable = new ColorDrawable(android.R.color.transparent);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(colorDrawable);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_fragment_artista, container, false);

        ic_facebook = (ImageView)v.findViewById(R.id.ic_facebook);

        seguidoresArtista = (TextView)v.findViewById(R.id.seguidores_artista);

        oyentesArtista = (TextView)v.findViewById(R.id.oyentes_artista);

        Bundle bundle = getArguments();
        String transitionName = "";

        if (bundle != null) {
            transitionName = bundle.getString("NombreTransicionProfile");
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            v.findViewById(R.id.perfil_artista).setTransitionName(transitionName);
        }

        if (bundle != null) {
            artista = (Artista)bundle.getSerializable("Artista");
            idArtista = artista.getIdArtista();
            ImageView back_image = (ImageView) v.findViewById(R.id.back_image);
            Picasso.with(getActivity()).load(artista.getUrlBack()).fit().centerCrop().into(back_image);
            ImageView perfil_artista = (ImageView) v.findViewById(R.id.perfil_artista);
            Picasso.with(getActivity()).load(artista.getUrlPerfil()).fit().into(perfil_artista);
            TextView nombre_artista = (TextView) v.findViewById(R.id.nombre_artista);
            nombre_artista.setText(artista.getNombreArtista());
            TextView genero_artista = (TextView) v.findViewById(R.id.genero_artista);
            genero_artista.setText(artista.getGeneroArtista());
        }

        TabLayout tabLayout = (TabLayout) v.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_rounded59));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_headset11));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_add184));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_photo212));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) v.findViewById(R.id.pager);
        final PagerAdapterArtista adapter = new PagerAdapterArtista
                (getChildFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        if (bundle != null) {
            nombreArtista = artista.getNombreArtista();
            String userInstagram = artista.getUserInstagram();
            viewPager.setTag(nombreArtista+"*"+ userInstagram);
        }

        bottomSheet = new BottomSheet.Builder(getActivity()).grid().sheet(R.menu.sheet_artista).build();

        final Menu menu = bottomSheet.getMenu();

        SharedPreferences prefs = getActivity().getSharedPreferences(PREFERENCIAS, Context.MODE_PRIVATE);
        boolean nombreArtistaPreference  = prefs.getBoolean(nombreArtista, false);
        if(!nombreArtistaPreference){
            menu.getItem(2).setIcon(R.drawable.ic_favorite22);
        }else{
            menu.getItem(2).setIcon(R.drawable.ic_favorite21);
        }

        FloatingActionButton FABMas = (FloatingActionButton) v.findViewById(R.id.FABMas);

        FABMas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                bottomSheet.show();
            }
        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        FetchArtistSheetDataTask fetchArtistSheetDataTask = new FetchArtistSheetDataTask();
        fetchArtistSheetDataTask.execute(artista.getNombreArtista());

        FetchArtistDataTask fetchArtistDataTask = new FetchArtistDataTask();
        fetchArtistDataTask.execute(artista.getNombreArtista());

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
            HttpURLConnection urlConnectionSpotify = null;
            BufferedReader readerSpotify = null;

            // Will contain the raw JSON response as a string.
            String artistDataJsonStrSpotify = null;
            String artista = params[0].replace(" ", "%20").replace("&", "%26");

            try {
                // Construct the URL for the Last.fm query
                final String FESTIVAL_BASE_URL = "https://api.spotify.com/v1/search?q="+artista+"&type=artist&limit=1";

                Uri builtUri = Uri.parse(FESTIVAL_BASE_URL);

                URL url = new URL(builtUri.toString());

                Log.v(LOG_TAG, "Built URI " + builtUri.toString());

                // Create the request to Last.fm, and open the connection
                urlConnectionSpotify = (HttpURLConnection) url.openConnection();
                urlConnectionSpotify.setRequestMethod("GET");
                urlConnectionSpotify.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnectionSpotify.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                readerSpotify = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = readerSpotify.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line+  "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                artistDataJsonStrSpotify = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the artist data, there's no point in attempting
                // to parse it.
                return null;
            } finally{
                if (urlConnectionSpotify != null) {
                    urlConnectionSpotify.disconnect();
                }
                if (readerSpotify != null) {
                    try {
                        readerSpotify.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            // If there's no artist name, there's nothing to look up.  Verify size of params.
            if (params.length == 0) {
                return null;
            }

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnectionLastfm = null;
            BufferedReader readerLastfm = null;

            // Will contain the raw JSON response as a string.
            String artistDataJsonStrLastfm = null;

            try {
                // Construct the URL for the Last.fm query
                final String FESTIVAL_BASE_URL = "http://ws.audioscrobbler.com/2.0/?method=artist.getInfo&artist="+artista+"&api_key=e0ed07c7f7a0f8ddb7fd91675e78fa63&format=json";

                Uri builtUri = Uri.parse(FESTIVAL_BASE_URL);

                URL url = new URL(builtUri.toString());

                Log.v(LOG_TAG, "Built URI " + builtUri.toString());

                // Create the request to Last.fm, and open the connection
                urlConnectionLastfm = (HttpURLConnection) url.openConnection();
                urlConnectionLastfm.setRequestMethod("GET");
                urlConnectionLastfm.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnectionLastfm.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                readerLastfm = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = readerLastfm.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line+  "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                artistDataJsonStrLastfm = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the artist data, there's no point in attempting
                // to parse it.
                return null;
            } finally{
                if (urlConnectionLastfm != null) {
                    urlConnectionLastfm.disconnect();
                }
                if (readerLastfm != null) {
                    try {
                        readerLastfm.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            String followers;
            String listeners;
            ArrayList<String> arrayResultados = new ArrayList<>();

            try {
                followers = getArtistFollowersFromJson(artistDataJsonStrSpotify);
                listeners = getArtistListenersFromJson(artistDataJsonStrLastfm);
                arrayResultados.add(followers);
                arrayResultados.add(listeners);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return arrayResultados;
        }

        private String getArtistFollowersFromJson(String artistDataJsonStrSpotify) throws JSONException {

            JSONObject rootObject = new JSONObject(artistDataJsonStrSpotify);

            JSONObject artistsObject = rootObject.getJSONObject("artists");

            JSONArray itemsArray = artistsObject.getJSONArray("items");

            JSONObject itemObject = itemsArray.getJSONObject(0);

            JSONObject followersObject = itemObject.getJSONObject("followers");

            int total = followersObject.getInt("total");

            return String.valueOf(total);
        }

        private String getArtistListenersFromJson(String artistDataJsonStrLastfm) throws JSONException {

            JSONObject rootObject = new JSONObject(artistDataJsonStrLastfm);

            JSONObject artistObject = rootObject.getJSONObject("artist");

            JSONObject statsObject = artistObject.getJSONObject("stats");

            return statsObject.getString("listeners");
        }


        @Override
        protected void onPostExecute(ArrayList<String> result) {
            String followers = result.get(0);
            String listeners = result.get(1);
            seguidoresArtista.setText(followers);
            oyentesArtista.setText(listeners);
        }
    }

    public class FetchArtistSheetDataTask extends AsyncTask<String, Void, ArrayList<String>> {

        private final String LOG_TAG = FetchArtistDataTask.class.getSimpleName();

        @Override
        protected ArrayList<String> doInBackground(String... params) {

            String artista;

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnectionWebUrl = null;
            BufferedReader readerWebUrl = null;

            // Will contain the raw JSON response as a string.
            String webUrlArtistJsonStr = null;
            artista = params[0].replace(" ", "%20").replace("&", "%26");

            try {
                // Construct the URL for the Echo Nest query
                final String ARTISTS_BASE_URL = "http://developer.echonest.com/api/v4/artist/urls?api_key=MRUF8KUHOM2WTLKPW&name="+artista+"&format=json";

                Uri builtUri = Uri.parse(ARTISTS_BASE_URL);

                URL url = new URL(builtUri.toString());

                Log.v(LOG_TAG, "Built URI " + builtUri.toString());

                // Create the request to Echo Nest, and open the connection
                urlConnectionWebUrl = (HttpURLConnection) url.openConnection();
                urlConnectionWebUrl.setRequestMethod("GET");
                urlConnectionWebUrl.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnectionWebUrl.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                readerWebUrl = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = readerWebUrl.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line+  "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                webUrlArtistJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the artist data, there's no point in attempting
                // to parse it.
                return null;
            } finally{
                if (urlConnectionWebUrl != null) {
                    urlConnectionWebUrl.disconnect();
                }
                if (readerWebUrl != null) {
                    try {
                        readerWebUrl.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            // If there's no artist name, there's nothing to look up.  Verify size of params.
            if (params.length == 0) {
                return null;
            }

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnectionFacebook = null;
            BufferedReader readerFacebook = null;

            // Will contain the raw JSON response as a string.
            String facebookArtistJsonStr = null;
            artista = params[0].replace(" ", "%20").replace("&", "%26");

            try {
                // Construct the URL for the Bandsintown query
                final String ARTISTS_BASE_URL = "http://api.bandsintown.com/artists/"+artista+".json?api_version=2.0&app_id=RAVE";

                Uri builtUri = Uri.parse(ARTISTS_BASE_URL);

                URL url = new URL(builtUri.toString());

                Log.v(LOG_TAG, "Built URI " + builtUri.toString());

                // Create the request to Bandsintown, and open the connection
                urlConnectionFacebook = (HttpURLConnection) url.openConnection();
                urlConnectionFacebook.setRequestMethod("GET");
                urlConnectionFacebook.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnectionFacebook.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                readerFacebook = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = readerFacebook.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line+  "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                facebookArtistJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the artist data, there's no point in attempting
                // to parse it.
                return null;
            } finally{
                if (urlConnectionFacebook != null) {
                    urlConnectionFacebook.disconnect();
                }
                if (readerFacebook != null) {
                    try {
                        readerFacebook.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            ArrayList<String> resultados = new ArrayList<>();
            String webUrl;
            String facebookUrl;

            try {
                webUrl = getArtistWebUrlFromJson(webUrlArtistJsonStr);
                facebookUrl = getArtistFacebookFromJson(facebookArtistJsonStr);
                resultados.add(webUrl);
                resultados.add(facebookUrl);
                return resultados;
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        private String getArtistWebUrlFromJson(String webUrlArtistJsonStr) throws JSONException {

            JSONObject rootObject = new JSONObject(webUrlArtistJsonStr);

            JSONObject responseObject = rootObject.getJSONObject("response");

            JSONObject urlsObject = responseObject.getJSONObject("urls");

            return urlsObject.getString("official_url");
        }

        private String getArtistFacebookFromJson(String facebookArtistJsonStr) throws JSONException {

            JSONObject rootObject = new JSONObject(facebookArtistJsonStr);

            return rootObject.getString("facebook_page_url");
        }


        @Override
        protected void onPostExecute(ArrayList<String> result) {

            if(result!=null){
                webUrl = result.get(0);
                facebookUrl = result.get(1);
                ic_facebook.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl));
                        getActivity().startActivity(browserIntent);
                    }
                });
                final Menu menu = bottomSheet.getMenu();
                menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Intent browserIntentWeb = new Intent(Intent.ACTION_VIEW, Uri.parse(webUrl));
                        getActivity().startActivity(browserIntentWeb);
                        return true;
                    }
                });
                menu.getItem(1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Transition slideTransform = null;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            slideTransform = TransitionInflater.from(getActivity()).
                                    inflateTransition(android.R.transition.slide_bottom);
                        }
                        Bundle bundle = new Bundle();
                        bundle.putString("nombreArtista", nombreArtista);
                        FragmentEventos fragmentEventos = new FragmentEventos();
                        fragmentEventos.setArguments(bundle);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            fragmentEventos.setEnterTransition(slideTransform);
                        }
                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, fragmentEventos)
                                .addToBackStack(null);
                        // Apply the transaction
                        ft.commit();
                        return true;
                    }
                });
                menu.getItem(2).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        SharedPreferences prefs = getActivity().getSharedPreferences(PREFERENCIAS, Context.MODE_PRIVATE);
                        boolean nombreArtistaPreference = prefs.getBoolean(nombreArtista, false);
                        if (!nombreArtistaPreference) {
                            menu.getItem(2).setIcon(R.drawable.ic_favorite21);
                            bottomSheet.invalidate();
                            SharedPreferences.Editor editor = getActivity().getSharedPreferences(PREFERENCIAS, Context.MODE_PRIVATE).edit();
                            editor.putBoolean(nombreArtista, true);
                            editor.apply();
                            FetchFavouriteDataTask fetchFavouriteDataTask = new FetchFavouriteDataTask();
                            fetchFavouriteDataTask.execute(idArtista);
                        } else {
                            menu.getItem(2).setIcon(R.drawable.ic_favorite22);
                            bottomSheet.invalidate();
                            SharedPreferences.Editor editor = getActivity().getSharedPreferences(PREFERENCIAS, Context.MODE_PRIVATE).edit();
                            editor.putBoolean(nombreArtista, false);
                            editor.apply();
                            DeleteFavouriteDataTask deleteFavouriteDataTask = new DeleteFavouriteDataTask();
                            deleteFavouriteDataTask.execute(idArtista);
                        }
                        return true;
                    }
                });
            }
        }
    }

    public class FetchFavouriteDataTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            StringBuilder favouriteDataJsonStr = new StringBuilder();

            String http = "http://ubuntu.westeurope.cloudapp.azure.com/ApiRAVE/v1/favoritos";

            HttpURLConnection urlConnection=null;
            try {
                URL url = new URL(http);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setUseCaches(false);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Authorization", claveApi);
                urlConnection.connect();

                //Create JSONObject here
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("idArtista", params[0]);
                OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
                out.write(jsonParam.toString());
                out.close();

                int HttpResult = urlConnection.getResponseCode();
                if(HttpResult == HttpURLConnection.HTTP_OK){
                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            urlConnection.getInputStream(),"utf-8"));
                    String line;
                    while ((line = br.readLine()) != null) {
                        favouriteDataJsonStr.append(line + "\n");
                    }
                    br.close();

                }else{
                    System.out.println(urlConnection.getResponseMessage());
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }finally{
                if(urlConnection!=null)
                    urlConnection.disconnect();
            }

            try{
                return getFavouriteDataFromJson(favouriteDataJsonStr.toString());
            }catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        private String getFavouriteDataFromJson(String favouriteDataJsonStr) throws JSONException {

            JSONObject rootObject = new JSONObject(favouriteDataJsonStr);

            return rootObject.getString("estado");
        }


        @Override
        protected void onPostExecute(String result) {

            if(result!=null){

                Toast.makeText(getActivity(), "Favorito guardado correctamente", Toast.LENGTH_LONG).show();
            }
        }
    }

    public class DeleteFavouriteDataTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            StringBuilder favouriteDataJsonStr = new StringBuilder();

            String http = "http://ubuntu.westeurope.cloudapp.azure.com/ApiRAVE/v1/favoritos";

            HttpURLConnection urlConnection=null;
            try {
                URL url = new URL(http);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("DELETE");
                urlConnection.setUseCaches(false);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Authorization", claveApi);
                urlConnection.setRequestProperty("idArtista", params[0]);
                urlConnection.connect();

                int HttpResult = urlConnection.getResponseCode();
                if(HttpResult == HttpURLConnection.HTTP_OK){
                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            urlConnection.getInputStream(),"utf-8"));
                    String line;
                    while ((line = br.readLine()) != null) {
                        favouriteDataJsonStr.append(line + "\n");
                    }
                    br.close();

                }else{
                    System.out.println(urlConnection.getResponseMessage());
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }finally{
                if(urlConnection!=null)
                    urlConnection.disconnect();
            }

            try{
                return getFavouriteDataFromJson(favouriteDataJsonStr.toString());
            }catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        private String getFavouriteDataFromJson(String favouriteDataJsonStr) throws JSONException {

            JSONObject rootObject = new JSONObject(favouriteDataJsonStr);

            return rootObject.getString("estado");
        }


        @Override
        protected void onPostExecute(String result) {

            if(result!=null){

                Toast.makeText(getActivity(), "Favorito eliminado correctamente", Toast.LENGTH_LONG).show();
            }
        }
    }
}
