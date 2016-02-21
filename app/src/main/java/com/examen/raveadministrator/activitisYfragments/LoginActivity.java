package com.examen.raveadministrator.activitisYfragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.examen.raveadministrator.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private static final String PREFERENCIAS = "Preferencias";

    @InjectView(R.id.input_email)
    EditText _emailText;
    @InjectView(R.id.input_password)
    EditText _passwordText;
    @InjectView(R.id.btn_login)
    Button _loginButton;
    @InjectView(R.id.link_signup)
    TextView _signupLink;
    private ProgressDialog progressDialog;
    private CallbackManager callbackManager;
    private Profile profile;
    private String email;
    private Bitmap bitmap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);

        ImageView back_image = (ImageView) findViewById(R.id.back_image);

        Picasso.with(getApplicationContext()).load(R.drawable.wallpaper_login).fit().centerCrop().into(back_image);

        /*LoginButton loginButton = (LoginButton) findViewById(R.id.btn_login_facebook);
        loginButton.setReadPermissions("email");
        loginButton.setReadPermissions("public_profile");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest.newMeRequest(
                        loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject me, GraphResponse response) {
                                if (response.getError() != null) {
                                } else {
                                    try {
                                        email = me.getString("email");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    profile = Profile.getCurrentProfile();
                                    RegisterFacebookUserTask registerFacebookUserTask = new RegisterFacebookUserTask();
                                    registerFacebookUserTask.execute(profile.getName(), email, profile.getId());
                                }
                            }
                        }).executeAsync();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getBaseContext(), "Los datos introducidos no son correctos", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException e) {
                Toast.makeText(getBaseContext(), "Los datos introducidos no son correctos", Toast.LENGTH_LONG).show();
            }
        });*/

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });

        SharedPreferences prefs = getSharedPreferences(PREFERENCIAS, MODE_PRIVATE);
        boolean autenticado = prefs.getBoolean("autenticado", false);
        if (autenticado) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.Base_Theme_AppCompat_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Iniciando sesión...");
        progressDialog.show();

        final String email = _emailText.getText().toString();
        final String password = _passwordText.getText().toString();

        // TODO: Implement your own authentication logic here.

        FetchUserDataTask fetchUserDataTask = new FetchUserDataTask();
        fetchUserDataTask.execute(email, password);
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    private void onLoginSuccess(String id, String nombre, String correo, String claveApi) {
        _loginButton.setEnabled(true);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        SharedPreferences.Editor editor = getSharedPreferences(PREFERENCIAS, MODE_PRIVATE).edit();
        editor.putString("id", id);
        editor.putString("nombre", nombre);
        editor.putString("correo", correo);
        editor.putString("claveApi", claveApi);
        editor.putBoolean("autenticado", true);
        editor.apply();
        startActivity(intent);
        finish();
    }

    private void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Los datos introducidos no son correctos", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    private boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("introduce una dirección de email válida");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 20) {
            _passwordText.setError("entre 4 y 20 caracteres alfanuméricos");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public class FetchUserDataTask extends AsyncTask<String, Void, ArrayList<String>> {

        private final String LOG_TAG = FetchUserDataTask.class.getSimpleName();

        @Override
        protected ArrayList<String> doInBackground(String... params) {

            StringBuilder userDataJsonStr = new StringBuilder();

            String http = "http://ubuntu.westeurope.cloudapp.azure.com/ApiRAVE/v1/usuarios/login";

            HttpURLConnection urlConnection=null;
            try {
                URL url = new URL(http);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setUseCaches(false);
                urlConnection.setRequestProperty("Content-Type","application/json");
                urlConnection.connect();

                //Create JSONObject here
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("correo", params[0]);
                jsonParam.put("contrasena", params[1]);
                OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
                out.write(jsonParam.toString());
                out.close();

                int HttpResult = urlConnection.getResponseCode();
                if(HttpResult == HttpURLConnection.HTTP_OK){
                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            urlConnection.getInputStream(),"utf-8"));
                    String line;
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

        private ArrayList<String> getUserDataFromJson(String userDataJsonStr) throws JSONException {

            ArrayList<String> arrayResultados = new ArrayList<>();

            JSONObject rootObject = new JSONObject(userDataJsonStr);

            String estado = rootObject.getString("estado");

            JSONObject usuarioObject = rootObject.getJSONObject("usuario");

            String id = usuarioObject.getString("id");

            String nombre = usuarioObject.getString("nombre");

            String correo = usuarioObject.getString("correo");

            String claveApi = usuarioObject.getString("claveApi");

            arrayResultados.add(estado);
            arrayResultados.add(id);
            arrayResultados.add(nombre);
            arrayResultados.add(correo);
            arrayResultados.add(claveApi);

            return arrayResultados;
        }


        @Override
        protected void onPostExecute(ArrayList<String> result) {

            if(result!=null){

                String estado = result.get(0);
                String id = result.get(1);
                String nombre = result.get(2);
                String correo = result.get(3);
                String claveApi = result.get(4);

                if(estado.equals("1")){
                    onLoginSuccess(id, nombre, correo, claveApi);
                    URL imageURL = null;
                    try {
                        if(profile != null){
                            imageURL = new URL("https://graph.facebook.com/" + profile.getId() + "/picture?type=large");
                            bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if(bitmap != null){
                        // get the base 64 string
                        String imgString = Base64.encodeToString(getBytesFromBitmap(bitmap),
                                Base64.NO_WRAP);
                        FetchPerfilUsuarioDataTask fetchPerfilUsuarioDataTask = new FetchPerfilUsuarioDataTask();
                        fetchPerfilUsuarioDataTask.execute(claveApi, id, imgString);
                    }
                }else{
                    onLoginFailed();
                }
                progressDialog.dismiss();
            }
        }
    }

    public class RegisterFacebookUserTask extends AsyncTask<String, Void, String> {

        private final String LOG_TAG = RegisterFacebookUserTask.class.getSimpleName();

        @Override
        protected String doInBackground(String... params) {

            StringBuilder userDataJsonStr = new StringBuilder();

            String http = "http://ubuntu.westeurope.cloudapp.azure.com/ApiRAVE/v1/usuarios/registro";

            HttpURLConnection urlConnection=null;
            try {
                URL url = new URL(http);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setUseCaches(false);
                urlConnection.setRequestProperty("Content-Type","application/json");
                urlConnection.connect();

                //Create JSONObject here
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("nombre", params[0]);
                jsonParam.put("correo", params[1]);
                jsonParam.put("contrasena", params[2]);
                OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
                out.write(jsonParam.toString());
                out.close();

                int HttpResult = urlConnection.getResponseCode();
                if(HttpResult == HttpURLConnection.HTTP_OK){
                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            urlConnection.getInputStream(),"utf-8"));
                    String line;
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

                if(result.equals("1")){
                    FetchUserDataTask fetchUserDataTask = new FetchUserDataTask();
                    fetchUserDataTask.execute(email, profile.getId());
                }
            }
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
                urlConnection.setRequestProperty("Authorization", params[0]);
                urlConnection.connect();

                //Create JSONObject here
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("idUsuario", params[1]);
                jsonParam.put("perfilUsuario", params[2]);
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

    // convert from bitmap to byte array
    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
    }
}