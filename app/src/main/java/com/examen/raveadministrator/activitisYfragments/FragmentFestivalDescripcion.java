package com.examen.raveadministrator.activitisYfragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.examen.raveadministrator.R;
import com.examen.raveadministrator.RoundedImageView;
import com.examen.raveadministrator.modelos.Usuario;
import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ObservableScrollView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentFestivalDescripcion.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentFestivalDescripcion#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentFestivalDescripcion extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private OnFragmentInteractionListener mListener;
    private ProgressBar progressBar;
    private TextView textoDescripcion;
    private RoundedImageView perfil_usuario1;
    private RoundedImageView perfil_usuario2;
    private RoundedImageView perfil_usuario3;
    private LinearLayout rlUsuarios;
    private String claveApi;
    private static final String PREFERENCIAS = "Preferencias";

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentFestivalDescripcion.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentFestivalDescripcion newInstance(String param1, String param2) {
        FragmentFestivalDescripcion fragment = new FragmentFestivalDescripcion();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentFestivalDescripcion() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }

        SharedPreferences prefs = getActivity().getSharedPreferences(PREFERENCIAS, Context.MODE_PRIVATE);
        claveApi = prefs.getString("claveApi", "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fragment_festival_descripcion, container, false);

        View view = container.getRootView();

        FloatingActionButton FABMas = (FloatingActionButton)view.findViewById(R.id.FABMas);

        ObservableScrollView sv_descripcion = (ObservableScrollView)v.findViewById(R.id.sv_descripcion);

        FABMas.attachToScrollView(sv_descripcion);

        String idFestival = container.getTag().toString();

        rlUsuarios = (LinearLayout)v.findViewById(R.id.rlUsuarios);

        progressBar = (ProgressBar)v.findViewById(R.id.progress_bar);

        textoDescripcion = (TextView)v.findViewById(R.id.texto_descripcion);

        perfil_usuario1 = (RoundedImageView)v.findViewById(R.id.perfil_usuario1);

        perfil_usuario2 = (RoundedImageView)v.findViewById(R.id.perfil_usuario2);

        perfil_usuario3 = (RoundedImageView)v.findViewById(R.id.perfil_usuario3);

        FetchAsistentesFestivalDataTask fetchAsistentesFestivalDataTask = new FetchAsistentesFestivalDataTask();
        fetchAsistentesFestivalDataTask.execute(idFestival);

        if(textoDescripcion.getText().equals("")){
            FetchFestivalDataTask fetchFestivalDataTask =  new FetchFestivalDataTask();
            fetchFestivalDataTask.execute(idFestival);
        }

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

    public class FetchFestivalDataTask extends AsyncTask<String, Void, String> {

        private final String LOG_TAG = FetchFestivalDataTask.class.getSimpleName();

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

        private String getFestivalDataFromJson(String festivalDataJsonStr) throws JSONException {

            JSONObject rootObject = new JSONObject(festivalDataJsonStr);

            JSONObject festivalesObject = rootObject.getJSONObject("festivales");

            JSONArray infoArray = festivalesObject.getJSONArray("info");

            JSONObject infoObject = infoArray.getJSONObject(0);

            return infoObject.getString("descripcionFestival");
        }


        @Override
        protected void onPostExecute(String result) {

            progressBar.setVisibility(View.GONE);

            if(result!=null){

                textoDescripcion.setText(result);
            }
        }
    }

    public class FetchAsistentesFestivalDataTask extends AsyncTask<String, Void, ArrayList<String>> {

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

            ArrayList<String> arrayResultados = new ArrayList<>();

            JSONObject rootObject = new JSONObject(festivalDataJsonStr);

            JSONObject festivalesObject = rootObject.getJSONObject("festivales");

            JSONArray asistentesArray = festivalesObject.getJSONArray("asistentes");

            for(int i = 0; i < 3; i++){

                JSONObject usuarioObject = asistentesArray.getJSONObject(i);

                String idUsuario = usuarioObject.getString("idUsuario");

                String perfilUsuario = usuarioObject.getString("perfilUsuario");

                if(perfilUsuario.equals("")) {
                    perfilUsuario = "NADA";
                }

                arrayResultados.add(idUsuario+"*"+perfilUsuario);
            }

            return arrayResultados;
        }


        @Override
        protected void onPostExecute(ArrayList<String> result) {

            progressBar.setVisibility(View.GONE);

            if(result!=null){

                if(!result.get(0).equals("NADA")){

                    rlUsuarios.setVisibility(View.VISIBLE);
                    perfil_usuario1.setVisibility(View.VISIBLE);

                    String[] arrayUsuario1 = result.get(0).split("\\*");

                    final Usuario usuario1 = new Usuario(arrayUsuario1[0], arrayUsuario1[1]);

                    if(usuario1.getPerfilUsuario().equals("NADA")){
                        Picasso.with(getActivity()).load(R.drawable.avatar).into(perfil_usuario1);
                    }else{
                        Bitmap bitmap = StringToBitMap(usuario1.getPerfilUsuario());
                        perfil_usuario1.setImageBitmap(bitmap);
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        perfil_usuario1.setTransitionName("usuario_transition1");
                    }

                    perfil_usuario1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String logoTransitionName = null;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                logoTransitionName = perfil_usuario1.getTransitionName();
                            }

                            Bundle bundle = new Bundle();
                            bundle.putString("NombreTransicionProfile", logoTransitionName);
                            bundle.putSerializable("Usuario", usuario1);

                            // Get access to or create instances to each fragment
                            FragmentFestivalDescripcion fragmentFestivalDescripcion = new FragmentFestivalDescripcion();
                            FragmentUsuario fragmentUsuario = new FragmentUsuario();
                            fragmentUsuario.setArguments(bundle);
                            // Check that the device is running lollipop
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                // Inflate transitions to apply
                                Transition changeTransform = TransitionInflater.from(getActivity()).
                                        inflateTransition(R.transition.change_image_transform);
                                Transition slideTransform = TransitionInflater.from(getActivity()).
                                        inflateTransition(android.R.transition.slide_bottom).setDuration(changeTransform.getDuration());

                                // Setup exit transition on first fragment
                                fragmentFestivalDescripcion.setSharedElementReturnTransition(changeTransform);
                                fragmentFestivalDescripcion.setExitTransition(slideTransform);

                                // Setup enter transition on second fragment
                                fragmentUsuario.setSharedElementEnterTransition(changeTransform);
                                fragmentUsuario.setEnterTransition(slideTransform);
                            }

                            // Add second fragment by replacing first
                            FragmentTransaction ft = ((FragmentActivity) getActivity()).getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.container, fragmentUsuario)
                                    .addToBackStack(null);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                ft.addSharedElement(perfil_usuario1, logoTransitionName);
                            }
                            // Apply the transaction
                            ft.commit();
                        }
                    });
                }

                if(!result.get(1).equals("NADA")){

                    perfil_usuario2.setVisibility(View.VISIBLE);

                    String[] arrayUsuario2 = result.get(1).split("\\*");

                    final Usuario usuario2 = new Usuario(arrayUsuario2[0], arrayUsuario2[1]);

                    if(usuario2.getPerfilUsuario().equals("NADA")){
                        Picasso.with(getActivity()).load(R.drawable.avatar).into(perfil_usuario2);
                    }else{
                        Bitmap bitmap = StringToBitMap(usuario2.getPerfilUsuario());
                        perfil_usuario2.setImageBitmap(bitmap);
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        perfil_usuario2.setTransitionName("usuario_transition2");
                    }

                    perfil_usuario2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String logoTransitionName = null;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                logoTransitionName = perfil_usuario2.getTransitionName();
                            }

                            Bundle bundle = new Bundle();
                            bundle.putString("NombreTransicionProfile", logoTransitionName);
                            bundle.putSerializable("Usuario", usuario2);

                            // Get access to or create instances to each fragment
                            FragmentFestivalDescripcion fragmentFestivalDescripcion = new FragmentFestivalDescripcion();
                            FragmentUsuario fragmentUsuario = new FragmentUsuario();
                            fragmentUsuario.setArguments(bundle);
                            // Check that the device is running lollipop
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                // Inflate transitions to apply
                                Transition changeTransform = TransitionInflater.from(getActivity()).
                                        inflateTransition(R.transition.change_image_transform);
                                Transition slideTransform = TransitionInflater.from(getActivity()).
                                        inflateTransition(android.R.transition.slide_bottom).setDuration(changeTransform.getDuration());

                                // Setup exit transition on first fragment
                                fragmentFestivalDescripcion.setSharedElementReturnTransition(changeTransform);
                                fragmentFestivalDescripcion.setExitTransition(slideTransform);

                                // Setup enter transition on second fragment
                                fragmentUsuario.setSharedElementEnterTransition(changeTransform);
                                fragmentUsuario.setEnterTransition(slideTransform);
                            }

                            // Add second fragment by replacing first
                            FragmentTransaction ft = ((FragmentActivity) getActivity()).getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.container, fragmentUsuario)
                                    .addToBackStack(null);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                ft.addSharedElement(perfil_usuario2, logoTransitionName);
                            }
                            // Apply the transaction
                            ft.commit();
                        }
                    });
                }

                if(result.get(2).equals("NADA")){
                    String[] arrayUsuario3 = result.get(2).split("\\*");

                    final Usuario usuario3 = new Usuario(arrayUsuario3[0], arrayUsuario3[1]);

                    if(usuario3.getPerfilUsuario().equals("NADA")){
                        Picasso.with(getActivity()).load(R.drawable.avatar).into(perfil_usuario3);
                    }else{
                        Bitmap bitmap = StringToBitMap(usuario3.getPerfilUsuario());
                        perfil_usuario3.setImageBitmap(bitmap);
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        perfil_usuario3.setTransitionName("usuario_transition3");
                    }

                    perfil_usuario3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            perfil_usuario3.setVisibility(View.VISIBLE);

                            String logoTransitionName = null;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                logoTransitionName = perfil_usuario3.getTransitionName();
                            }

                            Bundle bundle = new Bundle();
                            bundle.putString("NombreTransicionProfile", logoTransitionName);
                            bundle.putSerializable("Usuario", usuario3);

                            // Get access to or create instances to each fragment
                            FragmentFestivalDescripcion fragmentFestivalDescripcion = new FragmentFestivalDescripcion();
                            FragmentUsuario fragmentUsuario = new FragmentUsuario();
                            fragmentUsuario.setArguments(bundle);
                            // Check that the device is running lollipop
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                // Inflate transitions to apply
                                Transition changeTransform = TransitionInflater.from(getActivity()).
                                        inflateTransition(R.transition.change_image_transform);
                                Transition slideTransform = TransitionInflater.from(getActivity()).
                                        inflateTransition(android.R.transition.slide_bottom).setDuration(changeTransform.getDuration());

                                // Setup exit transition on first fragment
                                fragmentFestivalDescripcion.setSharedElementReturnTransition(changeTransform);
                                fragmentFestivalDescripcion.setExitTransition(slideTransform);

                                // Setup enter transition on second fragment
                                fragmentUsuario.setSharedElementEnterTransition(changeTransform);
                                fragmentUsuario.setEnterTransition(slideTransform);
                            }

                            // Add second fragment by replacing first
                            FragmentTransaction ft = ((FragmentActivity) getActivity()).getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.container, fragmentUsuario)
                                    .addToBackStack(null);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                ft.addSharedElement(perfil_usuario3, logoTransitionName);
                            }
                            // Apply the transaction
                            ft.commit();
                        }
                    });
                }
            }
        }
    }

    /**
     * @param encodedString
     * @return bitmap (from given string)
     */
    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }
}
