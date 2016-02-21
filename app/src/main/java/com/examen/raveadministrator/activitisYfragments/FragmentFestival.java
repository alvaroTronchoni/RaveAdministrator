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
import android.provider.CalendarContract;
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
import com.examen.raveadministrator.RoundedImageView;
import com.examen.raveadministrator.adaptadores.PagerAdapterFestival;
import com.examen.raveadministrator.modelos.Festival;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentFestival.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentFestival#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentFestival extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private OnFragmentInteractionListener mListener;
    private Festival festival;
    private TextView asistentes_festival;
    private TextView ediciones_festival;
    private ImageView ic_facebook;
    ImageView back;
    private String claveApi;
    private String idFestival;
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

    public FragmentFestival() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }

        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");

        ColorDrawable colorDrawable = new ColorDrawable(android.R.color.transparent);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(colorDrawable);

        SharedPreferences prefs = getActivity().getSharedPreferences(PREFERENCIAS, Context.MODE_PRIVATE);
        claveApi = prefs.getString("claveApi", "");
    }

    @Override
    public void onResume() {
        super.onResume();

        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");

        ColorDrawable colorDrawable = new ColorDrawable(android.R.color.transparent);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(colorDrawable);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_fragment_festival, container, false);

        ic_facebook = (ImageView)v.findViewById(R.id.ic_facebook);

        asistentes_festival = (TextView)v.findViewById(R.id.asistentes_festival);

        ediciones_festival = (TextView)v.findViewById(R.id.ediciones_festival);

        ImageView back_image = (ImageView) v.findViewById(R.id.back_image);

        RoundedImageView logo_festival = (RoundedImageView) v.findViewById(R.id.logo_festival);

        TextView nombre_festival = (TextView) v.findViewById(R.id.nombre_festival);

        TextView ubicacion_festival = (TextView) v.findViewById(R.id.ubicacion_festival);

        Bundle bundle = getArguments();
        String logoTransitionName = "";

        if (bundle != null) {
            logoTransitionName = bundle.getString("NombreTransicionLogo");
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            v.findViewById(R.id.logo_festival).setTransitionName(logoTransitionName);
        }

        if(bundle != null){
            festival = (Festival)bundle.getSerializable("Festival");
            Picasso.with(getActivity()).load(festival.getBackUrl()).fit().centerCrop().into(back_image);
            Picasso.with(getActivity()).load(festival.getLogoUrl()).fit().into(logo_festival);
            nombre_festival.setText(festival.getNombreFestival());
            ubicacion_festival.setText(festival.getUbicacionFestival());
        }

        TabLayout tabLayout = (TabLayout) v.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_rounded59));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_users25));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_map103));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_photo212));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) v.findViewById(R.id.pager);
        final PagerAdapterFestival adapter = new PagerAdapterFestival
                (getChildFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        if (bundle != null) {
            idFestival = festival.getIdFestival();
            viewPager.setTag(idFestival);
        }

        bottomSheet = new BottomSheet.Builder(getActivity()).grid().sheet(R.menu.sheet_festival).build();

        final Menu menu = bottomSheet.getMenu();

        SharedPreferences prefs = getActivity().getSharedPreferences(PREFERENCIAS, Context.MODE_PRIVATE);
        boolean idFestivalPreference  = prefs.getBoolean(idFestival, false);
        if(!idFestivalPreference){
            menu.getItem(4).setIcon(R.drawable.ic_favorite22);
        }else{
            menu.getItem(4).setIcon(R.drawable.ic_favorite21);
        }

        FetchFestivalSheetDataTask festivalSheetDataTask = new FetchFestivalSheetDataTask();
        festivalSheetDataTask.execute(idFestival);

        FetchFestivalDataTask fetchFestivalDataTask = new FetchFestivalDataTask();
        fetchFestivalDataTask.execute(idFestival);

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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == getActivity().RESULT_OK) {
                Place selectedPlace = PlacePicker.getPlace(data, getActivity());
                String placeId = selectedPlace.getId();
                FetchPlaceDataTask fetchPlaceDataTask = new FetchPlaceDataTask();
                fetchPlaceDataTask.execute(placeId);
            }
        }
    }

    private void guardarEvento(String pTitle, String pDescription, String pLocation, String fechaInicio, String fechaFin) {
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra(CalendarContract.Events.TITLE, pTitle);
        SimpleDateFormat sdfBegin = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate = null;
        try {
            beginDate = sdfBegin.parse(fechaInicio);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat sdfEnd = new SimpleDateFormat("yyyy-MM-dd");
        Date endDate = null;
        try {
            endDate = sdfEnd.parse(fechaFin);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println();
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginDate.getTime());
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endDate.getTime());
        intent.putExtra(CalendarContract.Events.ALL_DAY, true);// periodicity
        intent.putExtra(CalendarContract.Events.DESCRIPTION, pDescription);
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, pLocation);
        getActivity().startActivity(intent);
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

            ArrayList<String> arrayResultados =  new ArrayList<>();

            JSONObject rootObject = new JSONObject(festivalDataJsonStr);

            JSONObject festivalesObject = rootObject.getJSONObject("festivales");

            JSONArray infoArray = festivalesObject.getJSONArray("info");

            JSONObject infoObject = infoArray.getJSONObject(0);

            String facebookUrlFestival = infoObject.getString("facebookUrlFestival");

            String asistentesFestival = infoObject.getString("asistentesFestival");

            String ediciones_festival = infoObject.getString("edicionesFestival");

            arrayResultados.add(facebookUrlFestival);

            arrayResultados.add(asistentesFestival);

            arrayResultados.add(ediciones_festival);

            for (String s : arrayResultados) {
                Log.v(LOG_TAG, "Similar artist entry: " + s);
            }
            return arrayResultados;
        }


        @Override
        protected void onPostExecute(final ArrayList<String> result) {

            if(result!=null){

                ic_facebook.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(result.get(0)));
                        getActivity().startActivity(browserIntent);
                    }
                });
                asistentes_festival.setText(result.get(1));
                ediciones_festival.setText(result.get(2));
            }
        }
    }

    public class FetchFestivalSheetDataTask extends AsyncTask<String, Void, ArrayList<String>> {

        private final String LOG_TAG = FetchFestivalSheetDataTask.class.getSimpleName();

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

            ArrayList<String> arrayResultados =  new ArrayList<>();

            JSONObject rootObject = new JSONObject(festivalDataJsonStr);

            JSONObject festivalesObject = rootObject.getJSONObject("festivales");

            JSONArray infoArray = festivalesObject.getJSONArray("info");

            JSONObject infoObject = infoArray.getJSONObject(0);

            String nombreFestival = infoObject.getString("nombreFestival");

            String ubicacionFestival = infoObject.getString("ubicacionFestival");

            String webUrlFestival = infoObject.getString("webUrlFestival");

            String entradasUrl = infoObject.getString("entradasUrl");

            String fechaInicio = infoObject.getString("fechaInicio");

            String fechaFin = infoObject.getString("fechaFin");

            arrayResultados.add(nombreFestival+"*"+ubicacionFestival+"*"+webUrlFestival+"*"+entradasUrl+"*"+fechaInicio+"*"+fechaFin);

            for (String s : arrayResultados) {
                Log.v(LOG_TAG, "Similar artist entry: " + s);
            }
            return arrayResultados;
        }


        @Override
        protected void onPostExecute(ArrayList<String> result) {

            if(result!=null){

                for(String festivalDataStr: result) {
                    final String[] festivalDataArray = festivalDataStr.split("\\*");
                    final Menu menu = bottomSheet.getMenu();
                    menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            Intent browserIntentWeb = new Intent(Intent.ACTION_VIEW, Uri.parse(festivalDataArray[2]));
                            getActivity().startActivity(browserIntentWeb);
                            return true;
                        }
                    });
                    menu.getItem(1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            guardarEvento(festivalDataArray[0], "Hoy empieza el " + festivalDataArray[0], festivalDataArray[1], festivalDataArray[4], festivalDataArray[5]);
                            return true;
                        }
                    });
                    menu.getItem(2).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            Intent browserIntentEntradas = new Intent(Intent.ACTION_VIEW, Uri.parse(festivalDataArray[3]));
                            getActivity().startActivity(browserIntentEntradas);
                            return true;
                        }
                    });
                    menu.getItem(3).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            Transition slideTransform = null;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                slideTransform = TransitionInflater.from(getActivity()).
                                        inflateTransition(android.R.transition.slide_bottom);
                            }
                            Bundle bundle = new Bundle();
                            bundle.putString("idFestival", idFestival);
                            FragmentComentarios fragmentComentarios = new FragmentComentarios();
                            fragmentComentarios.setArguments(bundle);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                fragmentComentarios.setEnterTransition(slideTransform);
                            }
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.container, fragmentComentarios)
                                    .addToBackStack(null);
                            // Apply the transaction
                            ft.commit();
                            return true;
                        }
                    });
                    menu.getItem(4).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            SharedPreferences prefs = getActivity().getSharedPreferences(PREFERENCIAS, Context.MODE_PRIVATE);
                            boolean nombreFestivalPreference = prefs.getBoolean(festival.getNombreFestival(), false);
                            if (!nombreFestivalPreference) {
                                menu.getItem(4).setIcon(R.drawable.ic_favorite21);
                                bottomSheet.invalidate();
                                SharedPreferences.Editor editor = getActivity().getSharedPreferences(PREFERENCIAS, Context.MODE_PRIVATE).edit();
                                editor.putBoolean(festival.getNombreFestival(), true);
                                editor.apply();
                                FetchFavouriteDataTask fetchFavouriteDataTask = new FetchFavouriteDataTask();
                                fetchFavouriteDataTask.execute(idFestival);
                            } else {
                                menu.getItem(4).setIcon(R.drawable.ic_favorite22);
                                bottomSheet.invalidate();
                                SharedPreferences.Editor editor = getActivity().getSharedPreferences(PREFERENCIAS, Context.MODE_PRIVATE).edit();
                                editor.putBoolean(festival.getNombreFestival(), false);
                                editor.apply();
                                DeleteFavouriteDataTask deleteFavouriteDataTask = new DeleteFavouriteDataTask();
                                deleteFavouriteDataTask.execute(idFestival);
                            }
                            return true;
                        }
                    });
                }
            }
        }
    }

    public class FetchPlaceDataTask extends AsyncTask<String, Void, String> {

        private final String LOG_TAG = FetchPlaceDataTask.class.getSimpleName();

        @Override
        protected String doInBackground(String... params) {

            // If there's no artist name, there's nothing to look up.  Verify size of params.
            if (params.length == 0) {
                return null;
            }

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String placeDataJsonStr = null;

            try {
                // Construct the URL for the Last.fm query
                final String PLACE_BASE_URL = "https://maps.googleapis.com/maps/api/place/details/json?placeid="+params[0]+"&key=AIzaSyAoW-TkmlXMvQXSPZ2k83kPNXyaRveBZdw";

                Uri builtUri = Uri.parse(PLACE_BASE_URL);

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
                placeDataJsonStr = buffer.toString();
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
                return getPlaceDataFromJson(placeDataJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            // This will only happen if there was an error getting or parsing the forecast.

            return null;
        }

        private String getPlaceDataFromJson(String placeDataJsonStr) throws JSONException {

            JSONObject rootObject = new JSONObject(placeDataJsonStr);

            JSONObject resultObject = rootObject.getJSONObject("result");

            return resultObject.getString("url");
        }


        @Override
        protected void onPostExecute(String result) {

            if(result!=null){
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(result));
                getActivity().startActivity(browserIntent);
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
                jsonParam.put("idFestival", params[0]);
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
                urlConnection.setRequestProperty("idFestival", params[0]);
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
