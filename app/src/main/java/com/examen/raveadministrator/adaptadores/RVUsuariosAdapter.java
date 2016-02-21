package com.examen.raveadministrator.adaptadores;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.examen.raveadministrator.R;
import com.examen.raveadministrator.RoundedImageView;
import com.examen.raveadministrator.activitisYfragments.FragmentUsuario;
import com.examen.raveadministrator.activitisYfragments.FragmentUsuarioAmigos;
import com.examen.raveadministrator.modelos.Usuario;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class RVUsuariosAdapter extends RecyclerView.Adapter<RVUsuariosAdapter.UsuarioViewHolder>{

    private final List<Usuario> usuarios;
    private Context context;
    private String claveApi;
    private static final String PREFERENCIAS = "Preferencias";

    public RVUsuariosAdapter(List<Usuario> usuarios){
        this.usuarios = usuarios;
    }

    public static class UsuarioViewHolder extends RecyclerView.ViewHolder{
        final TextView nombreUsuario;
        final TextView correoUsuario;
        final RoundedImageView perfilUsuario;
        final ImageView ic_usuario;
        final RelativeLayout rl;

        UsuarioViewHolder(View itemView) {
            super(itemView);
            nombreUsuario = (TextView)itemView.findViewById(R.id.nombre_usuario);
            correoUsuario = (TextView)itemView.findViewById(R.id.correo_usuario);
            perfilUsuario = (RoundedImageView)itemView.findViewById(R.id.perfil_usuario);
            ic_usuario = (ImageView)itemView.findViewById(R.id.ic_usuario);
            rl = (RelativeLayout)itemView.findViewById(R.id.rlUsuario);
        }
    }

    @Override
    public int getItemCount() {
        return usuarios.size();
    }

    @Override
    public UsuarioViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.elemento_usuario, viewGroup, false);
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCIAS, Context.MODE_PRIVATE);
        claveApi = prefs.getString("claveApi", "");
        return new UsuarioViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final UsuarioViewHolder usuarioViewHolder, final int i) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            usuarioViewHolder.perfilUsuario.setTransitionName("profile_transition" + i);
        }
        usuarioViewHolder.nombreUsuario.setText(usuarios.get(i).getNombre());
        usuarioViewHolder.correoUsuario.setText(usuarios.get(i).getCorreo());
        RelativeLayout rl = usuarioViewHolder.rl;
        if(usuarios.get(i).getPerfilUsuario().equals("NADA")){
            Picasso.with(context).load(R.drawable.avatar).into(usuarioViewHolder.perfilUsuario);
        }else{
            Bitmap bitmap = StringToBitMap(usuarios.get(i).getPerfilUsuario());
            usuarioViewHolder.perfilUsuario.setImageBitmap(bitmap);
        }
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCIAS, Context.MODE_PRIVATE);
        boolean nombreArtistaPreference  = prefs.getBoolean(usuarios.get(i).getCorreo(), false);
        if(!nombreArtistaPreference){
            usuarioViewHolder.ic_usuario.setImageResource(R.drawable.ic_add186);
        }else{
            usuarioViewHolder.ic_usuario.setImageResource(R.drawable.ic_close47);
        }
        usuarioViewHolder.rl.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                String logoTransitionName;
                logoTransitionName = usuarioViewHolder.perfilUsuario.getTransitionName();

                Bundle bundle = new Bundle();
                bundle.putString("NombreTransicionProfile", logoTransitionName);
                bundle.putSerializable("Usuario", usuarios.get(i));

                // Get access to or create instances to each fragment
                FragmentUsuarioAmigos fragmentUsuarioAmigos = new FragmentUsuarioAmigos();
                FragmentUsuario fragmentUsuario = new FragmentUsuario();
                fragmentUsuario.setArguments(bundle);
                // Check that the device is running lollipop
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    // Inflate transitions to apply
                    Transition changeTransform = TransitionInflater.from(context).
                            inflateTransition(R.transition.change_image_transform);
                    Transition slideTransform = TransitionInflater.from(context).
                            inflateTransition(android.R.transition.slide_bottom).setDuration(changeTransform.getDuration());

                    // Setup exit transition on first fragment
                    fragmentUsuarioAmigos.setSharedElementReturnTransition(changeTransform);
                    fragmentUsuarioAmigos.setExitTransition(slideTransform);

                    // Setup enter transition on second fragment
                    fragmentUsuario.setSharedElementEnterTransition(changeTransform);
                    fragmentUsuario.setEnterTransition(slideTransform);

                    // Add second fragment by replacing first
                    FragmentTransaction ft = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, fragmentUsuario)
                            .addToBackStack(null)
                            .addSharedElement(usuarioViewHolder.perfilUsuario, logoTransitionName);
                    // Apply the transaction
                    ft.commit();
                }
            }
        });
        usuarioViewHolder.ic_usuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = context.getSharedPreferences(PREFERENCIAS, Context.MODE_PRIVATE);
                boolean nombreArtistaPreference = prefs.getBoolean(usuarioViewHolder.correoUsuario.getText().toString(), false);
                String id = prefs.getString("id", "");
                if (!nombreArtistaPreference) {
                    usuarioViewHolder.ic_usuario.setImageResource(R.drawable.ic_close47);
                    SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCIAS, Context.MODE_PRIVATE).edit();
                    editor.putBoolean(usuarioViewHolder.correoUsuario.getText().toString(), true);
                    editor.apply();
                    FetchFriendDataTask fetchFriendDataTask = new FetchFriendDataTask();
                    fetchFriendDataTask.execute(id, usuarios.get(i).getId());
                } else {
                    usuarioViewHolder.ic_usuario.setImageResource(R.drawable.ic_add186);
                    SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCIAS, Context.MODE_PRIVATE).edit();
                    editor.putBoolean(usuarioViewHolder.correoUsuario.getText().toString(), false);
                    editor.apply();
                    DeleteFriendDataTask deleteFriendDataTask = new DeleteFriendDataTask();
                    deleteFriendDataTask.execute(id, usuarios.get(i).getId());
                }
            }
        });
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

                Toast.makeText(context, "Amigo guardado correctamente", Toast.LENGTH_LONG).show();
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

                Toast.makeText(context, "Amigo eliminado correctamente", Toast.LENGTH_LONG).show();
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