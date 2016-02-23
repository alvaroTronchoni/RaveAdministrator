package com.examen.raveadministrator.activitisYfragments;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;


import com.examen.raveadministrator.R;
import com.examen.raveadministrator.adaptadores.RVNoticiasAdapter;
import com.examen.raveadministrator.modelos.Noticia;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentNoticias.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentNoticias#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentNoticias extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = "Inicio";
    private static final String ARG_PARAM2 = "param2";

    private OnFragmentInteractionListener mListener;
    private List<Noticia> noticias;
    private LinearLayoutManager llm;
    private RVNoticiasAdapter adapter;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBarRefresh;
    private boolean loading = true;
    private int pastVisiblesItems;
    private int visibleItemCount;
    private int totalItemCount;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentNoticias.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentNoticias newInstance(String param1, String param2) {
        FragmentNoticias fragment = new FragmentNoticias();
        Bundle args = new Bundle();
        args.putString(TAG, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentNoticias() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mParam1 = getArguments().getString(TAG);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("NOTICIAS");

        noticias = new ArrayList<>();

        adapter = new RVNoticiasAdapter(noticias);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set title
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("NOTICIAS");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fragment_noticias, container, false);

        progressBar = (ProgressBar)v.findViewById(R.id.progress_bar);
        progressBarRefresh = (ProgressBar)v.findViewById(R.id.progress_bar_refresh);

        swipeRefreshLayout = (SwipeRefreshLayout)v.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.myPrimaryDarkColor, R.color.myPrimaryColor, R.color.myPrimaryDarkColor);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                FetchPage1NewsDataTask fetchPage1NewsDataTask = new FetchPage1NewsDataTask();
                fetchPage1NewsDataTask.execute();
            }
        });

        FetchPage1NewsDataTask fetchPage1NewsDataTask = new FetchPage1NewsDataTask();
        fetchPage1NewsDataTask.execute();

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

        RecyclerView rv = (RecyclerView) view.findViewById(R.id.rvInicio);
        rv.setHasFixedSize(true);
        llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        rv.setAdapter(adapter);

        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) { //check for scroll down
                    visibleItemCount = llm.getChildCount();
                    totalItemCount = llm.getItemCount();
                    pastVisiblesItems = llm.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
                            FetchPage2NewsDataTask fetchPage2NewsDataTask = new FetchPage2NewsDataTask();
                            fetchPage2NewsDataTask.execute();
                            progressBarRefresh.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });
    }

    private class FetchPage1NewsDataTask extends AsyncTask<Void, Void, ArrayList<String>> {

        private final String LOG_TAG = FetchPage1NewsDataTask.class.getSimpleName();

        @Override
        protected ArrayList<String> doInBackground(Void... params) {

            ArrayList<String> resultados = new ArrayList<>();

            Document docEDMSpainPage1;
            try {

                //Creamos el documento a partir de la URL
                docEDMSpainPage1 = Jsoup.connect("http://edmspain.es/noticias/").get();

                //Guardamos los elementos que queremos recorrer para saber cuántos hay
                Elements articulos = docEDMSpainPage1.getElementsByClass("block-title");
                int numArticulos = articulos.size();
                for(int i = 0; i < numArticulos-2; i++) {
                    //Para cada uno de estos elementos mostramos su nombre y su precio
                    String enlace = "";
                    Element articulo = docEDMSpainPage1.getElementsByClass("block-title").get(i);
                    String nombre = docEDMSpainPage1.getElementsByClass("block-title").get(i).text();
                    String contenido = docEDMSpainPage1.getElementsByTag("p").get(i).text();
                    Elements links = articulo.getElementsByAttribute("href");
                    for(int j = 0; j < links.size(); j++){
                        enlace = links.attr("href");
                    }
                    Element imageElement = docEDMSpainPage1.select("img").get(i+5);

                    String absoluteUrl = imageElement.absUrl("src");  //absolute URL on src

                    String srcValue = imageElement.attr("src");

                    resultados.add(enlace+"*"+srcValue+"*"+nombre+"*"+contenido);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            Document docViciousMagazinePage1;
            try {

                //Creamos el documento a partir de la URL
                docViciousMagazinePage1 = Jsoup.connect("http://www.viciousmagazine.com/noticias/").get();

                //Guardamos los elementos que queremos recorrer para saber cuántos hay
                Elements articulos = docViciousMagazinePage1.getElementsByClass("caja_blanca_grande_listado_secciones");
                int numArticulos = articulos.size();
                for(int i = 0; i < numArticulos; i++){
                    //Para cada uno de estos elementos mostramos su nombre y su precio
                    String enlace = "";
                    Element articulo = docViciousMagazinePage1.getElementsByClass("caja_blanca_grande_listado_secciones").get(i);
                    String nombre = docViciousMagazinePage1.getElementsByClass("titulo_home_post").get(i).text();
                    String contenido = docViciousMagazinePage1.getElementsByClass("texto_home_post").get(i).text();
                    Elements imagen_618 = docViciousMagazinePage1.getElementsByClass("imagen_618");

                    Element imageElement = docViciousMagazinePage1.select("img").get(i + 3);

                    String absoluteUrl = imageElement.absUrl("src");  //absolute URL on src

                    String srcValue = imageElement.attr("src");

                    Element link = articulo.select("a").get(0);

                    enlace = link.attr("href");

                    resultados.add(enlace + "*" + srcValue + "*" + nombre + "*" + contenido);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return resultados;
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {

            swipeRefreshLayout.setRefreshing(false);
            progressBar.setVisibility(View.GONE);

            if(result!=null){

                noticias.clear();

                for(String newsArtistDataStr: result){
                    String[] newsArtistaArray = newsArtistDataStr.split("\\*");
                    noticias.add(new Noticia(newsArtistaArray[0], newsArtistaArray[1], newsArtistaArray[2], newsArtistaArray[3]));
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

    private class FetchPage2NewsDataTask extends AsyncTask<Void, Void, ArrayList<String>> {

        private final String LOG_TAG = FetchPage1NewsDataTask.class.getSimpleName();

        @Override
        protected ArrayList<String> doInBackground(Void... params) {

            ArrayList<String> resultados = new ArrayList<>();

            Document docEDMSpainPage2;
            try {

                //Creamos el documento a partir de la URL
                docEDMSpainPage2 = Jsoup.connect("http://edmspain.es/noticias/page/2/").get();

                //Guardamos los elementos que queremos recorrer para saber cuántos hay
                Elements articulos = docEDMSpainPage2.getElementsByClass("block-title");
                int numArticulos = articulos.size();
                for(int i = 0; i < numArticulos - 2; i++){
                    //Para cada uno de estos elementos mostramos su nombre y su precio
                    String enlace = "";
                    Element articulo = docEDMSpainPage2.getElementsByClass("block-title").get(i);
                    String nombre = docEDMSpainPage2.getElementsByClass("block-title").get(i).text();
                    String contenido = docEDMSpainPage2.getElementsByTag("p").get(i).text();
                    Elements links = articulo.getElementsByAttribute("href");
                    for(int j = 0; j < links.size(); j++){
                        enlace = links.attr("href");
                    }
                    Element imageElement = docEDMSpainPage2.select("img").get(i + 5);

                    String absoluteUrl = imageElement.absUrl("src");  //absolute URL on src

                    String srcValue = imageElement.attr("src");

                    resultados.add(enlace + "*" + srcValue+"*"+nombre+"*"+contenido);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            Document docViciousMagazinePage2;
            try {

                //Creamos el documento a partir de la URL
                docViciousMagazinePage2 = Jsoup.connect("http://www.viciousmagazine.com/noticias/page/2/").get();

                //Guardamos los elementos que queremos recorrer para saber cuántos hay
                Elements articulos = docViciousMagazinePage2.getElementsByClass("caja_blanca_grande_listado_secciones");
                int numArticulos = articulos.size();
                for(int i = 0; i < numArticulos; i++){
                    //Para cada uno de estos elementos mostramos su nombre y su precio
                    String enlace = "";
                    Element articulo = docViciousMagazinePage2.getElementsByClass("caja_blanca_grande_listado_secciones").get(i);
                    String nombre = docViciousMagazinePage2.getElementsByClass("titulo_home_post").get(i).text();
                    String contenido = docViciousMagazinePage2.getElementsByClass("texto_home_post").get(i).text();
                    Elements imagen_618 = docViciousMagazinePage2.getElementsByClass("imagen_618");

                    Element imageElement = docViciousMagazinePage2.select("img").get(i+3);

                    String absoluteUrl = imageElement.absUrl("src");  //absolute URL on src

                    String srcValue = imageElement.attr("src");

                    Element link = articulo.select("a").get(0);

                    enlace = link.attr("href");

                    resultados.add(enlace + "*" + srcValue + "*" + nombre + "*" + contenido);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return resultados;
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {

            swipeRefreshLayout.setRefreshing(false);
            progressBar.setVisibility(View.GONE);
            progressBarRefresh.setVisibility(View.GONE);

            if(result!=null){
                for(String newsArtistDataStr: result){
                    String[] newsArtistaArray = newsArtistDataStr.split("\\*");
                    noticias.add(new Noticia(newsArtistaArray[0], newsArtistaArray[1], newsArtistaArray[2], newsArtistaArray[3]));
                    //adapter.notifyDataSetChanged();
                }
            }
        }
    }
}
