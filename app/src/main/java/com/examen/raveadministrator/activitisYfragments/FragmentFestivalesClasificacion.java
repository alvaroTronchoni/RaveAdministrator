package com.examen.raveadministrator.activitisYfragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;


import com.examen.raveadministrator.R;
import com.examen.raveadministrator.adaptadores.RVClasificacionesAdapter;
import com.examen.raveadministrator.modelos.Clasificacion;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentFestivalesClasificacion.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentFestivalesClasificacion#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentFestivalesClasificacion extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = "FestivalesClasificacion";
    private static final String ARG_PARAM2 = "param2";

    private OnFragmentInteractionListener mListener;
    private RVClasificacionesAdapter adapter;
    private static final String PREFERENCIAS = "Preferencias";

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentFestivales.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentFestivales newInstance(String param1, String param2) {
        FragmentFestivales fragment = new FragmentFestivales();
        Bundle args = new Bundle();
        args.putString(TAG, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentFestivalesClasificacion() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mParam1 = getArguments().getString(TAG);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("FESTIVALES");

        List<Clasificacion> clasificaciones = new ArrayList<>();
        clasificaciones.add(new Clasificacion("EDM", R.drawable.fader, R.drawable.edm));
        clasificaciones.add(new Clasificacion("Indie", R.drawable.musical191, R.drawable.indie));
        clasificaciones.add(new Clasificacion("Techno", R.drawable.dj, R.drawable.techno));
        clasificaciones.add(new Clasificacion("Dubstep", R.drawable.music94, R.drawable.dubstep));
        clasificaciones.add(new Clasificacion("Rock", R.drawable.drum24, R.drawable.rock));
        clasificaciones.add(new Clasificacion("Más cercanos", R.drawable.map102, R.drawable.mas_cercanos));

        adapter = new RVClasificacionesAdapter(clasificaciones);

        SharedPreferences prefs = getActivity().getSharedPreferences(PREFERENCIAS, Context.MODE_PRIVATE);
        String claveApi = prefs.getString("claveApi", "");
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set title
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("FESTIVALES");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fragment_festivales_clasificacion, container, false);

        ProgressBar progressBar = (ProgressBar) v.findViewById(R.id.progress_bar);

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
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("FESTIVALES");
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

        RecyclerView rv = (RecyclerView) view.findViewById(R.id.rvFestivalesClasificacion);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        rv.setAdapter(adapter);
    }
}
