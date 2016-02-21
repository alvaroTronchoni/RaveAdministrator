package com.examen.raveadministrator.activitisYfragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.examen.raveadministrator.R;
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

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    @InjectView(R.id.input_name)
    EditText _nameText;
    @InjectView(R.id.input_email)
    EditText _emailText;
    @InjectView(R.id.input_password)
    EditText _passwordText;
    @InjectView(R.id.btn_signup)
    Button _signupButton;
    @InjectView(R.id.link_login)
    TextView _loginLink;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.inject(this);

        ImageView back_image = (ImageView) findViewById(R.id.back_image);

        Picasso.with(getApplicationContext()).load(R.drawable.wallpaper_login).fit().centerCrop().into(back_image);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }

    private void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.Base_Theme_AppCompat_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creando cuenta...");
        progressDialog.show();

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        FetchUserDataTask fetchUserDataTask = new FetchUserDataTask();
        fetchUserDataTask.execute(name, email, password);
    }


    private void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    private void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Los datos introducidos no son correctos", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    private boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("por lo menos 3 caracteres");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("introduce una dirección de email válida");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 20) {
            _passwordText.setError("entre 4 y 20 caracteres alfanumericos");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    public class FetchUserDataTask extends AsyncTask<String, Void, String> {

        private final String LOG_TAG = FetchUserDataTask.class.getSimpleName();

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

                if(result.equals("4")){
                    onSignupSuccess();
                }else{
                    onSignupFailed();
                }
                progressDialog.dismiss();
            }
        }
    }
}