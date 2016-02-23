package com.examen.raveadministrator.activitisYfragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.examen.raveadministrator.R;
import com.examen.raveadministrator.SimpleDividerItemDecoration;
import com.examen.raveadministrator.adaptadores.RVClasificacionesAdapter;
import com.examen.raveadministrator.adaptadores.RVEventosAdapter;
import com.examen.raveadministrator.modelos.Clasificacion;
import com.examen.raveadministrator.modelos.Evento;
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
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentFestivalesClasificacion.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentFestivalesClasificacion#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentFormularioFestival1 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = "FestivalesFormularios";
    private static final String ARG_PARAM2 = "param2";

    private OnFragmentInteractionListener mListener;
    private RVClasificacionesAdapter adapter;
    private static final String PREFERENCIAS = "Preferencias";
    private String claveApi;
    private String idAdmin;

    private EditText edit_nombre;
    private EditText edit_genero;
    private EditText edit_ubicacion;
    private EditText edit_latitud;
    private EditText edit_longitud;
    private EditText edit_descripcion;
    private EditText edit_face_url;
    private EditText edit_web_url;
    private EditText edit_logo_url;
    private EditText edit_back_url;
    private EditText edit_entradas_url;
    private EditText edit_fecha_inicio;
    private EditText edit_fecha_fin;
    private EditText edit_asistentes;
    private EditText edit_ediciones;
    private EditText edit_instagram_user;

    private Button btn_cancel;
    private Button btn_acept;

    private String nombre, genero, ubicacion, latitud, longitud, descripcion, face_url, web_url, logo_url,back_url,
    entradas_url, fecha_inicio, fecha_fin, asistentes,ediciones, instagram_user;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentFestivales.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentFormularioFestival1 newInstance(String param1, String param2) {
        FragmentFormularioFestival1 fragment = new FragmentFormularioFestival1();
        Bundle args = new Bundle();
        args.putString(TAG, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    //
    public FragmentFormularioFestival1() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mParam1 = getArguments().getString(TAG);
            String mParam2 = getArguments().getString(ARG_PARAM2);

            ButterKnife.inject((AppCompatActivity)getActivity());
        }

        SharedPreferences prefs = getActivity().getSharedPreferences(PREFERENCIAS, Context.MODE_PRIVATE);
        claveApi = prefs.getString("claveApi", "");
        idAdmin = prefs.getString("id","");


       //((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("FESTIVALES");

       /* List<Clasificacion> clasificaciones = new ArrayList<>();
        clasificaciones.add(new Clasificacion("EDM", R.drawable.fader, R.drawable.edm));
        clasificaciones.add(new Clasificacion("Indie", R.drawable.musical191, R.drawable.indie));
        clasificaciones.add(new Clasificacion("Techno", R.drawable.dj, R.drawable.techno));
        clasificaciones.add(new Clasificacion("Dubstep", R.drawable.music94, R.drawable.dubstep));
        clasificaciones.add(new Clasificacion("Rock", R.drawable.drum24, R.drawable.rock));
        clasificaciones.add(new Clasificacion("Más cercanos", R.drawable.map102, R.drawable.mas_cercanos));

        adapter = new RVClasificacionesAdapter(clasificaciones);

        SharedPreferences prefs = getActivity().getSharedPreferences(PREFERENCIAS, Context.MODE_PRIVATE);
        String claveApi = prefs.getString("claveApi", "");*/
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set title
        //((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("FESTIVALES");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fragment_formulario_festival1, container, false);

        ImageView back_image = (ImageView) v.findViewById(R.id.back_image);
        Picasso.with(getActivity().getApplicationContext()).load(R.drawable.wallpaper_login).fit().centerCrop().into(back_image);
        //ProgressBar progressBar = (ProgressBar) v.findViewById(R.id.progress_bar);

        recogerVariables(v);

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
        super.onAttach(activity);/*
        try {
            mListener = (OnFragmentInteractionListener) activity;
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("FESTIVALES");
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
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

      /*  RecyclerView rv = (RecyclerView) view.findViewById(R.id.rvFestivalesClasificacion);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        rv.setAdapter(adapter);*/
    }

    private void recogerVariables(View v){
        edit_nombre = (EditText) v.findViewById(R.id.input_nombreFestival);
        edit_genero = (EditText) v.findViewById(R.id.input_generoFestival);
        edit_ubicacion = (EditText) v.findViewById(R.id.input_ubicacionFestival);
        edit_latitud = (EditText) v.findViewById(R.id.input_latitudFestival);
        edit_longitud = (EditText) v.findViewById(R.id.input_longitudFestival);
        edit_descripcion = (EditText) v.findViewById(R.id.input_descripcionFestival);
        edit_face_url = (EditText) v.findViewById(R.id.input_facebookUrl);
        edit_web_url = (EditText) v.findViewById(R.id.input_webUrl);
        edit_logo_url = (EditText) v.findViewById(R.id.input_logoUrl);
        edit_back_url = (EditText) v.findViewById(R.id.input_backUrl);
        edit_instagram_user = (EditText) v.findViewById(R.id.input_instagramUser);
        edit_entradas_url = (EditText) v.findViewById(R.id.input_entradasUrl);
        edit_fecha_inicio = (EditText) v.findViewById(R.id.input_fechaInicio);
        edit_fecha_fin = (EditText) v.findViewById(R.id.input_fechaFin);
        edit_asistentes = (EditText) v.findViewById(R.id.input_asistentesFestival);
        edit_ediciones = (EditText) v.findViewById(R.id.input_edicionesFestival);
        btn_acept = (Button) v.findViewById(R.id.btnAceptar);
        btn_cancel = (Button) v.findViewById(R.id.btnAtras);

        btn_acept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(puedo()){
                    respuestas();
                    MeterEvento meterEvento = new MeterEvento();
                    meterEvento.execute();
                }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                Fragment fragment = new FragmentMisFestivales();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment)
                        .commit();
            }
        });

    }

    public String[] respuestas(){
        String[] respuesta = new String[16];
        nombre = edit_nombre.getText().toString();
        genero = edit_genero.getText().toString();
        ubicacion = edit_ubicacion.getText().toString();
        latitud = edit_latitud.getText().toString();
        longitud = edit_longitud.getText().toString();
        descripcion = edit_descripcion.getText().toString();
        face_url = edit_face_url.getText().toString();
        web_url = edit_web_url.getText().toString();
        logo_url = edit_logo_url.getText().toString();
        back_url = edit_back_url.getText().toString();
        instagram_user = edit_instagram_user.getText().toString();
        entradas_url = edit_entradas_url.getText().toString();
        fecha_inicio = edit_fecha_inicio.getText().toString();
        fecha_fin = edit_fecha_fin.getText().toString();
        asistentes = edit_asistentes.getText().toString();
        ediciones = edit_ediciones.getText().toString();

        return respuesta;
    }

    public boolean puedo(){
        boolean valid = true;


        if (edit_nombre.getText().toString().isEmpty()) {
            edit_nombre.setError("introduce los datos requeridos");
            valid = false;
        } else {
            edit_nombre.setError(null);
        }
        if (edit_genero.getText().toString().isEmpty()) {
            edit_genero.setError("introduce los datos requeridos");
            valid = false;
        } else {
            edit_genero.setError(null);
        }
        if (edit_ubicacion.getText().toString().isEmpty()) {
            edit_ubicacion.setError("introduce los datos requeridos");
            valid = false;
        } else {
            edit_ubicacion.setError(null);
        }
        if (edit_latitud.getText().toString().isEmpty()) {
            edit_latitud.setError("introduce los datos requeridos");
            valid = false;
        } else {
            edit_latitud.setError(null);
        }
        if (edit_longitud.getText().toString().isEmpty()) {
            edit_longitud.setError("introduce los datos requeridos");
            valid = false;
        } else {
            edit_longitud.setError(null);
        }
        if (edit_descripcion.getText().toString().isEmpty()) {
            edit_descripcion.setError("introduce los datos requeridos");
            valid = false;
        } else {
            edit_descripcion.setError(null);
        }
        if (edit_face_url.getText().toString().isEmpty()) {
            edit_face_url.setError("introduce los datos requeridos");
            valid = false;
        } else {
            edit_face_url.setError(null);
        }
        if (edit_web_url.getText().toString().isEmpty()) {
            edit_web_url.setError("introduce los datos requeridos");
            valid = false;
        } else {
            edit_web_url.setError(null);
        }
        if (edit_logo_url.getText().toString().isEmpty()) {
            edit_logo_url.setError("introduce los datos requeridos");
            valid = false;
        } else {
            edit_logo_url.setError(null);
        }
        if (edit_back_url.getText().toString().isEmpty()) {
            edit_back_url.setError("introduce los datos requeridos");
            valid = false;
        } else {
            edit_back_url.setError(null);
        }
        if (edit_instagram_user.getText().toString().isEmpty()) {
            edit_instagram_user.setError("introduce los datos requeridos");
            valid = false;
        } else {
            edit_instagram_user.setError(null);
        }
        if (edit_entradas_url.getText().toString().isEmpty()) {
            edit_entradas_url.setError("introduce los datos requeridos");
            valid = false;
        } else {
            edit_entradas_url.setError(null);
        }
        if (edit_fecha_inicio.getText().toString().isEmpty()) {
            edit_fecha_inicio.setError("introduce los datos requeridos");
            valid = false;
        } else {
            edit_fecha_inicio.setError(null);
        }
        if (edit_fecha_fin.getText().toString().isEmpty()) {
            edit_fecha_fin.setError("introduce los datos requeridos");
            valid = false;
        } else {
            edit_fecha_fin.setError(null);
        }
        if (edit_asistentes.getText().toString().isEmpty()) {
            edit_asistentes.setError("introduce los datos requeridos");
            valid = false;
        } else {
            edit_asistentes.setError(null);
        }
        if (edit_ediciones.getText().toString().isEmpty()) {
            edit_ediciones.setError("introduce los datos requeridos");
            valid = false;
        } else {
            edit_ediciones.setError(null);
        }

        return valid;
    }

    public class MeterEvento extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {

            StringBuilder favouriteDataJsonStr = new StringBuilder();

            String http = "http://ubuntu.westeurope.cloudapp.azure.com/ApiRAVE/v1/festivales";

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
                jsonParam.put("nombreFestival", nombre);
                jsonParam.put("idAdministrador", idAdmin);
                jsonParam.put("generoFestival", genero);
                jsonParam.put("ubicacionFestival", ubicacion);
                jsonParam.put("latitudFestival", latitud);
                jsonParam.put("longitudFestival", longitud);
                jsonParam.put("descripcionFestival", descripcion);
                jsonParam.put("facebookUrl", face_url);
                jsonParam.put("webUrl", web_url);
                jsonParam.put("logoUrl", logo_url);
                jsonParam.put("backUrl", back_url);
                jsonParam.put("instagramUser", instagram_user);
                jsonParam.put("entradasUrl", entradas_url);
                jsonParam.put("fechaInicio", fecha_inicio);
                jsonParam.put("fechaFin", fecha_fin);
                jsonParam.put("asistentesFestival", asistentes);
                jsonParam.put("edicionesFestival", ediciones);

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

            return null;//getFavouriteDataFromJson(favouriteDataJsonStr.toString());

        }


        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getActivity().getApplicationContext(), "Evento Registrado", Toast.LENGTH_LONG).show();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            Fragment fragment = new FragmentMisFestivales();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();

        }
    }
}
