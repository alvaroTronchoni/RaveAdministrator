package com.examen.raveadministrator.activitisYfragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.examen.raveadministrator.ImagePicker;
import com.examen.raveadministrator.R;
import com.examen.raveadministrator.RoundedImageView;
import com.examen.raveadministrator.adaptadores.PagerAdapterUsuario;
import com.examen.raveadministrator.modelos.Usuario;
import com.melnykov.fab.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
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
 * {@link FragmentNuevoEvento.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentNuevoEvento#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentNuevoEvento extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = "Mi Perfil";
    private static final String ARG_PARAM2 = "param2";

    private OnFragmentInteractionListener mListener;
    String claveApi;
    Usuario usuario;
    String id;
    private static final String PREFERENCIAS = "Preferencias";

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentUsuario.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentNuevoEvento newInstance(String param1, String param2) {
        FragmentNuevoEvento fragment = new FragmentNuevoEvento();
        Bundle args = new Bundle();
        args.putString(TAG, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentNuevoEvento() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mParam1 = getArguments().getString(TAG);
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

        View v = inflater.inflate(R.layout.fragment_fragment_nuevo_eventoo, container, false);

        Bundle bundle = getArguments();
        String transitionName = "";

        if (bundle != null) {
            transitionName = bundle.getString("NombreTransicionProfile");
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            v.findViewById(R.id.perfil_usuario).setTransitionName(transitionName);
        }

        if(bundle != null){
            usuario = (Usuario)bundle.getSerializable("Usuario");
        }

        ImageView back_image = (ImageView) v.findViewById(R.id.back_image);

        Picasso.with(getActivity()).load(R.drawable.wallpaper_login).fit().centerCrop().into(back_image);

        SharedPreferences prefs = getActivity().getSharedPreferences(PREFERENCIAS, Context.MODE_PRIVATE);
        id = prefs.getString("id", "");



        TabLayout tabLayout = (TabLayout) v.findViewById(R.id.tab_layout_nuevo_evento);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_rounded59));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_users25));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_favorite21_2));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_connection21_2));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) v.findViewById(R.id.pager);
        final PagerAdapterUsuario adapter = new PagerAdapterUsuario
                (getChildFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        if(usuario == null){
            viewPager.setTag(id);
        }else{
            viewPager.setTag(usuario.getId());
        }




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

    public class FetchFriendDataTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            StringBuilder favouriteDataJsonStr = new StringBuilder();

            String http = "http://ubuntu.westeurope.cloudapp.azure.com/ApiRAVE/v1/usuarios";

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
                jsonParam.put("idUsuario1", params[0]);
                jsonParam.put("idUsuario2", params[1]);
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

                Toast.makeText(getActivity(), "Amigo guardado correctamente", Toast.LENGTH_LONG).show();
            }
        }
    }

    public class DeleteFriendDataTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            StringBuilder favouriteDataJsonStr = new StringBuilder();

            String http = "http://ubuntu.westeurope.cloudapp.azure.com/ApiRAVE/v1/usuarios";

            HttpURLConnection urlConnection=null;
            try {
                URL url = new URL(http);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("DELETE");
                urlConnection.setUseCaches(false);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Authorization", claveApi);
                urlConnection.setRequestProperty("idUsuario1", params[0]);
                urlConnection.setRequestProperty("idUsuario2", params[1]);
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

                Toast.makeText(getActivity(), "Amigo eliminado correctamente", Toast.LENGTH_LONG).show();
            }
        }
    }

    // convert from bitmap to byte array
    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
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

    public class FetchPerfilUsuarioDataTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            StringBuilder favouriteDataJsonStr = new StringBuilder();

            String http = "http://ubuntu.westeurope.cloudapp.azure.com/ApiRAVE/v1/usuarios";

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
                jsonParam.put("idUsuario", params[0]);
                jsonParam.put("perfilUsuario", params[1]);
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

                Toast.makeText(getActivity(), "Imagen guardada correctamente", Toast.LENGTH_LONG).show();
            }
        }
    }

    public class FetchDatosUsuarioDataTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            StringBuilder favouriteDataJsonStr = new StringBuilder();

            String http = "http://ubuntu.westeurope.cloudapp.azure.com/ApiRAVE/v1/usuarios";

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
                jsonParam.put("idUsuario", params[0]);
                jsonParam.put("nombreUsuario", params[1]);
                jsonParam.put("correoUsuario", params[2]);
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

                Toast.makeText(getActivity(), "Datos guardados correctamente", Toast.LENGTH_LONG).show();
            }
        }
    }
}
