package com.examen.raveadministrator.activitisYfragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.examen.raveadministrator.R;
import com.examen.raveadministrator.modelos.Noticia;
import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ObservableScrollView;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentNoticia.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentNoticia#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentNoticia extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private OnFragmentInteractionListener mListener;
    private TextView titulo_noticia;
    private TextView texto_descripcion;
    private ProgressBar progressBar;
    private String url;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentNoticia.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentNoticia newInstance(String param1, String param2) {
        FragmentNoticia fragment = new FragmentNoticia();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentNoticia() {
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fragment_noticia, container, false);

        ObservableScrollView sv_descripcion = (ObservableScrollView) v.findViewById(R.id.sv_descripcion);

        FloatingActionButton FABCompartir = (FloatingActionButton) v.findViewById(R.id.FABCompartir);

        FABCompartir.attachToScrollView(sv_descripcion);

        progressBar = (ProgressBar)v.findViewById(R.id.progress_bar);

        ImageView back_image = (ImageView) v.findViewById(R.id.back_image);

        titulo_noticia = (TextView)v.findViewById(R.id.titulo_noticia);

        texto_descripcion = (TextView)v.findViewById(R.id.texto_descripcion);

        Bundle bundle = getArguments();

        if(bundle != null){
            Noticia noticia = (Noticia) bundle.getSerializable("Noticia");
            if(noticia.getImage_url().contains("http:") || noticia.getImage_url().contains("https:")){
                Picasso.with(getActivity()).load(noticia.getImage_url()).fit().centerCrop().into(back_image);
            }else{
                String imageUrl = "http:"+ noticia.getImage_url();
                Picasso.with(getActivity()).load(imageUrl).fit().centerCrop().into(back_image);
            }
            url = noticia.getUrl();
            if(url.contains("edmspain")){
                FetchEDMSpainArticleDataTask fetchEDMSpainArticleDataTask = new FetchEDMSpainArticleDataTask();
                fetchEDMSpainArticleDataTask.execute(url);
            }
            if(url.contains("viciousmagazine")){
                FetchViciousMagazineArticleDataTask fetchViciousMagazineArticleDataTask = new FetchViciousMagazineArticleDataTask();
                fetchViciousMagazineArticleDataTask.execute(url);
            }
        }

        FABCompartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, url);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
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

    private class FetchEDMSpainArticleDataTask extends AsyncTask<String, Void, ArrayList<String>> {

        private final String LOG_TAG = FetchEDMSpainArticleDataTask.class.getSimpleName();

        @Override
        protected ArrayList<String> doInBackground(String... params) {

            ArrayList<String> arrayResultados = new ArrayList<>();

            Document doc;
            try {

                doc = Jsoup.connect(params[0]).get();

                StringBuilder stringBuilder = new StringBuilder();

                String contenido = "";

                String titulo = doc.getElementsByTag("h1").get(0).text();
                Element e = doc.select("img").get(6);
                Elements textos = doc.select("p");
                for(int i = 0; i < textos.size(); i++){
                    Element texto = textos.get(i);
                    stringBuilder.append(texto.text()).append("\n");
                }
                int firstIndex = 0;
                if(stringBuilder.toString().contains("______")){
                    firstIndex = stringBuilder.toString().indexOf("______");
                }else{
                    firstIndex = stringBuilder.toString().indexOf("Rellena el formulario pulsando la imagen.");
                }
                contenido = stringBuilder.toString().substring(0, firstIndex);
                arrayResultados.add(titulo);
                arrayResultados.add(contenido);

            } catch (IOException e) {
                e.printStackTrace();
            }

            return arrayResultados;
        }

        @Override
        protected void onPostExecute(final ArrayList<String> result) {

            if(result!=null){

                progressBar.setVisibility(View.GONE);

                titulo_noticia.setText(result.get(0));

                texto_descripcion.setText(result.get(1));
            }
        }
    }

    private class FetchViciousMagazineArticleDataTask extends AsyncTask<String, Void, ArrayList<String>> {

        private final String LOG_TAG = FetchViciousMagazineArticleDataTask.class.getSimpleName();

        @Override
        protected ArrayList<String> doInBackground(String... params) {

            ArrayList<String> arrayResultados = new ArrayList<>();

            Document doc;
            try {

                //Creamos el documento a partir de la URL
                doc = Jsoup.connect(params[0]).get();

                StringBuilder stringBuilder = new StringBuilder();

                String titulo = doc.getElementsByClass("titulo_detalle").get(0).text();
                String entradilla = doc.getElementsByClass("entradilla_detalle").get(0).text();
                stringBuilder.append(entradilla).append("\n");
                Elements textos = doc.select("p");
                for(int i = 1; i < textos.size(); i++){
                    Element texto = textos.get(i);
                    stringBuilder.append(texto.text()).append("\n");
                    Log.e(LOG_TAG, texto.text());
                }
                arrayResultados.add(titulo);
                arrayResultados.add(stringBuilder.toString());

            } catch (IOException e) {
                e.printStackTrace();
            }

            return arrayResultados;
        }

        @Override
        protected void onPostExecute(final ArrayList<String> result) {

            if(result!=null && !result.isEmpty()){

                Log.e(LOG_TAG, result.get(0));

                Log.e(LOG_TAG, result.get(1));

                progressBar.setVisibility(View.GONE);

                titulo_noticia.setText(result.get(0));

                texto_descripcion.setText(result.get(1));
            }
        }
    }
}
