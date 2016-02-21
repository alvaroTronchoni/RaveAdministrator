package com.examen.raveadministrator.activitisYfragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.examen.raveadministrator.R;
import com.examen.raveadministrator.SimpleDividerItemDecoration;
import com.examen.raveadministrator.adaptadores.RVComentariosAdapter;
import com.examen.raveadministrator.modelos.Comentario;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentComentarios.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentComentarios#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentComentarios extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private OnFragmentInteractionListener mListener;
    private List<Comentario> comentarios;
    private RVComentariosAdapter adapter;
    private String claveApi;
    private String idFestival;
    private ProgressBar progressBar;
    private EditText texto_comentario;
    private static final String PREFERENCIAS = "Preferencias";

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentComentarios.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentComentarios newInstance(String param1, String param2) {
        FragmentComentarios fragment = new FragmentComentarios();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentComentarios() {
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
        View v = inflater.inflate(R.layout.fragment_fragment_comentarios, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        Bundle bundle = getArguments();

        if (bundle != null) {
            idFestival = bundle.getString("idFestival");
        }

        texto_comentario = (EditText)v.findViewById(R.id.texto_comentario);

        ImageView ic_enviar = (ImageView) v.findViewById(R.id.ic_enviar);

        ic_enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!texto_comentario.getText().toString().equals("")) {
                    FetchUserDataTask fetchUserDataTask = new FetchUserDataTask();
                    fetchUserDataTask.execute(idFestival, texto_comentario.getText().toString());
                }
            }
        });

        progressBar = (ProgressBar)v.findViewById(R.id.progress_bar);

        comentarios = new ArrayList<>();

        adapter = new RVComentariosAdapter(comentarios);

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

        RecyclerView rv = (RecyclerView) view.findViewById(R.id.rvComentarios);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        rv.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        rv.setAdapter(adapter);

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

            JSONArray comentariosArray = festivalesObject.getJSONArray("comentarios");

            for(int i = 0; i < comentariosArray.length(); i++){

                JSONObject comentarioObject = comentariosArray.getJSONObject(i);

                String nombreAutor = comentarioObject.getString("nombreAutor");

                String perfilUsuario = comentarioObject.getString("perfilUsuario");

                if(perfilUsuario.equals("")){
                    perfilUsuario = "NADA";
                }

                String fecha = comentarioObject.getString("fecha");

                String texto = comentarioObject.getString("texto");

                arrayResultados.add(nombreAutor+"*"+perfilUsuario+"*"+fecha+"*"+texto);
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

                comentarios.clear();

                for(String resultStr : result){
                    String[] resultArray = resultStr.split("\\*");
                    comentarios.add(new Comentario(resultArray[0], resultArray[1], resultArray[2], resultArray[3]));
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

    public class FetchUserDataTask extends AsyncTask<String, Void, String> {

        private final String LOG_TAG = FetchUserDataTask.class.getSimpleName();

        @Override
        protected String doInBackground(String... params) {

            StringBuilder userDataJsonStr = new StringBuilder();

            String http = "http://ubuntu.westeurope.cloudapp.azure.com/ApiRAVE/v1/comentarios";

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

                Calendar c = Calendar.getInstance();

                SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
                String formattedDate = df.format(c.getTime());

                //Create JSONObject here
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("idFestival", params[0]);
                jsonParam.put("texto", params[1]);
                jsonParam.put("fecha", formattedDate);
                OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
                out.write(jsonParam.toString());
                out.close();

                int HttpResult = urlConnection.getResponseCode();
                if(HttpResult == HttpURLConnection.HTTP_OK){
                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            urlConnection.getInputStream(),"utf-8"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        userDataJsonStr.append(line + "\n");
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
                return getUserDataFromJson(userDataJsonStr.toString());
            }catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        private String getUserDataFromJson(String userDataJsonStr) throws JSONException {

            JSONObject rootObject = new JSONObject(userDataJsonStr);

            return rootObject.getString("estado");
        }


        @Override
        protected void onPostExecute(String result) {

            if(result!=null){

                comentarios.clear();

                if(result.equals("3")){
                    FetchFestivalDataTask fetchFestivalDataTask = new FetchFestivalDataTask();
                    fetchFestivalDataTask.execute(idFestival);
                    texto_comentario.setText("");
                }else{
                    Toast.makeText(getActivity(), "No se ha podido publicar el comentario", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
