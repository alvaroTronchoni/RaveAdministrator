package com.examen.raveadministrator.activitisYfragments;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.examen.raveadministrator.NavigationDrawerCallbacks;
import com.examen.raveadministrator.NavigationDrawerFragment;
import com.examen.raveadministrator.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, NavigationDrawerCallbacks/*, FragmentBuscar.OnFragmentInteractionListener, FragmentNoticias.OnFragmentInteractionListener*/, FragmentFestivales.OnFragmentInteractionListener,FragmentMisFestivales.OnFragmentInteractionListener, FragmentUsuario.OnFragmentInteractionListener /*,FragmentConfiguracion.OnFragmentInteractionListener*/, FragmentFestival.OnFragmentInteractionListener, FragmentFestivalDescripcion.OnFragmentInteractionListener, FragmentFestivalArtistas.OnFragmentInteractionListener, FragmentFestivalFotos.OnFragmentInteractionListener, FragmentArtista.OnFragmentInteractionListener, FragmentArtistaDescripcion.OnFragmentInteractionListener, FragmentArtistaMusica.OnFragmentInteractionListener, FragmentArtistaArtistasSimilares.OnFragmentInteractionListener, FragmentArtistaFotos.OnFragmentInteractionListener, FragmentComentarios.OnFragmentInteractionListener, FragmentEventos.OnFragmentInteractionListener /*,FragmentNoticia.OnFragmentInteractionListener*/, FragmentFestivalesClasificacion.OnFragmentInteractionListener, FragmentUsuarioDescripcion.OnFragmentInteractionListener, FragmentUsuarioAmigos.OnFragmentInteractionListener, FragmentUsuarioFavoritos.OnFragmentInteractionListener, FragmentUsuarioCalendario.OnFragmentInteractionListener{

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private GoogleApiClient googleApiClient;
    private GoogleCloudMessaging gcm;
    private Fragment fragment;
    private String claveApi;
    private String regid;
    private String id;
    private static final String PREFERENCIAS = "Preferencias";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FetchGCMDataTask fetchGCMDataTask = new FetchGCMDataTask();
        fetchGCMDataTask.execute();

        RelativeLayout container = (RelativeLayout) findViewById(R.id.container);

        SharedPreferences prefs = getSharedPreferences(PREFERENCIAS, Context.MODE_PRIVATE);
        id = prefs.getString("id", "");
        String nombre = prefs.getString("nombre", "");
        String correo = prefs.getString("correo", "");
        claveApi = prefs.getString("claveApi", "");

        // initialising the object of the FragmentManager. Here I'm passing getSupportFragmentManager(). You can pass getFragmentManager() if you are coding for Android 3.0 or above.
        FragmentManager fragmentManager = getSupportFragmentManager();

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.fragment_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setup((DrawerLayout) findViewById(R.id.drawer), mToolbar);
        // populate the navigation drawer
        mNavigationDrawerFragment.setUserData(nombre, correo, BitmapFactory.decodeResource(getResources(), R.drawable.avatar));

        googleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addApi(LocationServices.API)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .build();

        handleIntent(getIntent());

        /*MeterEvento m = new MeterEvento();
        m.execute("");*/
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        swapFragment(position);
    }

    /** Swaps fragments in the main content view */
    private void swapFragment(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager(); // For AppCompat use getSupportFragmentManager
        switch (position) {
            default:
            case 0:
                fragment = new FragmentMisFestivales();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment)
                        .commit();
                break;
            case 1:
                fragment = new FragmentFestivalesClasificacion();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment)
                        .commit();
                break;
            case 2:
                /*fragment = new FragmentNoticias();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment)
                        .commit();*/
                break;
            case 3:
                fragment = new FragmentUsuario();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment)
                        .commit();
                break;
            case 4:
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Estás seguro?").setPositiveButton("Si", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
                break;
        }

    }

    @Override
    public void setTitle(CharSequence title) {
        CharSequence mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    public void onBackPressed() {
        if (mNavigationDrawerFragment.isDrawerOpen())
            mNavigationDrawerFragment.closeDrawer();
        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getFragmentManager().popBackStack();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
       /* int id = item.getItemId();

        if(id == R.id.search){
            FragmentManager fragmentManager = getSupportFragmentManager(); // For AppCompat use getSupportFragmentManager
            FragmentBuscar fragment = new FragmentBuscar();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }*/

        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onConnected(Bundle bundle) {
        Toast.makeText(getApplicationContext(), "HOLAAAAAAAAAAAAAAAAAAAAAAAAA", Toast.LENGTH_LONG).show();
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    private class FetchGCMDataTask extends AsyncTask<Void, Void, String> {

        private final String LOG_TAG = FetchGCMDataTask.class.getSimpleName();

        @Override
        protected String doInBackground(Void... params) {
            String msg = "";
            try {
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                }
                String PROJECT_NUMBER = "541116496893";
                regid = gcm.register(PROJECT_NUMBER);
                msg = "Device registered, registration ID = " + regid;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return msg;
        }

        @Override
        protected void onPostExecute(String msg) {

            FetchRegIdUsuarioDataTask fetchRegIdUsuarioDataTask = new FetchRegIdUsuarioDataTask();
            fetchRegIdUsuarioDataTask.execute(id, regid);

            //Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
        }
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    SharedPreferences.Editor editor = getSharedPreferences(PREFERENCIAS, Context.MODE_PRIVATE).edit();
                    editor.putBoolean("autenticado", false);
                    editor.apply();
                    Intent i = new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(i);
                    finish();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        }
    };

    public class FetchRegIdUsuarioDataTask extends AsyncTask<String, Void, String> {

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
                jsonParam.put("regId", params[1]);
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

                //Toast.makeText(getApplicationContext(), "Imagen guardada correctamente", Toast.LENGTH_LONG).show();
            }
        }
    }

    public class MeterEvento extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

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
                jsonParam.put("nombreFestival", "Mad Cool Festival");
                jsonParam.put("idAdministrador", "8");
                jsonParam.put("generoFestival", "Indie, Rock");
                jsonParam.put("ubicacionFestival", "Madrid");
                jsonParam.put("latitudFestival", "40.4378698");
                jsonParam.put("longitudFestival", "-3.8196207");
                jsonParam.put("descripcionFestival", "El Mad Cool Festival se celebrará en un recinto ubicado en la Caja Mágica de Madrid del 16 al 18 de junio de 2016.");
                jsonParam.put("facebookUrl", "https://www.facebook.com/madcoolfestival/");
                jsonParam.put("webUrl", "http://madcoolfestival.es/");
                jsonParam.put("logoUrl", "http://static.guiaocio.com/var/guiadelocio.com/storage/images/conciertos/madrid/madrid/mad-cool-festival-2016/27885014-10-esl-ES/mad-cool-festival-2016.jpg");
                jsonParam.put("backUrl", "http://5www.ecestaticos.com/imagestatic/clipping/587/acd/85c/587acd85c091f684137c7cd35a4dbe9d/bienvenidos-al-mad-cool-el-nuevo-macrofestival-musical-de-madrid.jpg?mtime=1453892094");
                jsonParam.put("instagramUser", "madcoolfestival");
                jsonParam.put("entradasUrl", "http://madcoolfestival.es/entradas/");
                jsonParam.put("fechaInicio", "2016-06-16");
                jsonParam.put("fechaFin", "2016-06-18");
                jsonParam.put("asistentesFestival", "40.000");
                jsonParam.put("edicionesFestival", "1");

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

            if(result!=null){

                //Toast.makeText(getApplicationContext(), "Imagen guardada correctamente", Toast.LENGTH_LONG).show();
            }
        }
    }
}