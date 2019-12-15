package com.example.onemoretime.LoginRegister;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.onemoretime.Nfc.NfcActivity;
import com.example.onemoretime.R;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    RequestQueue requestQueue;
    @BindView(R.id.input_index) EditText indexText;
    @BindView(R.id.input_password) EditText passwordText;
    @BindView(R.id.btn_login) Button loginButton;
    @BindView(R.id.link_signup) TextView signupLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        requestQueue = Volley.newRequestQueue(this);

        loginButton = (Button) findViewById(R.id.btn_login);
        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });
        signupLink = (TextView) findViewById(R.id.link_signup);
        signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String index = indexText.getText().toString();
        String password = passwordText.getText().toString();

        String baseUrl = "http://192.168.43.95:8000/api/v1/rest-auth/login/";
        JSONObject json = new JSONObject();

        try {
            json.put("username", index);
            json.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, baseUrl, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Response: ", response.toString());
                        onLoginSuccess();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onLoginFailed();
                Log.e("Error: ", error.toString());
                Log.e("json: ", json.toString());

            }
        });
        requestQueue.add(jsonObjectRequest);

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        progressDialog.dismiss();
                    }
                }, 60);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_SIGNUP) {
//            if (resultCode == RESULT_OK) {
//
//                // TODO: Implement successful signup logic here
//                // By default we just finish the Activity and log them in automatically
//                this.finish();
//            }
//        }
//    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        Intent anotherIntent = new Intent(this, NfcActivity.class);
        startActivity(anotherIntent);
        loginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;
        indexText = (EditText) findViewById(R.id.input_index);
        passwordText = (EditText) findViewById(R.id.input_password);

        String index = indexText.getText().toString();
        String password = passwordText.getText().toString();

        if (index.isEmpty() || index.length() == 7) {
            indexText.setError("enter a valid index");
            valid = false;
        } else {
            indexText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        return valid;
    }
}