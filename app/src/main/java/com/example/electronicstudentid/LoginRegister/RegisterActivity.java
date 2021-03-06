package com.example.electronicstudentid.LoginRegister;

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
import com.example.electronicstudentid.Nfc.NfcActivity;
import com.example.electronicstudentid.R;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends AppCompatActivity {
    RequestQueue requestQueue;
    String name;
    String lastName;
    String index;
    String email;
    String password;
    String uid;

    @BindView(R.id.input_name)
    EditText nameText;
    @BindView(R.id.input_last_name)
    EditText lastNameText;
    @BindView(R.id.input_name)
    EditText indexText;
    @BindView(R.id.input_email)
    EditText emailText;
    @BindView(R.id.input_password)
    EditText passwordText;
    @BindView(R.id.btn_signup)
    Button signupButton;
    @BindView(R.id.link_login)
    TextView loginLink;
    @BindView(R.id.btn_student_card)
    Button studentCard;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        requestQueue = Volley.newRequestQueue(this);
        ButterKnife.bind(this);
        studentCard = findViewById(R.id.btn_student_card);
        studentCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, NfcActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        signupButton = findViewById(R.id.btn_signup);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });
        loginLink = findViewById(R.id.link_login);
        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                uid = data.getStringExtra("tag");
                Log.e("Response: ", "" + uid);
            }
            if (resultCode == RESULT_CANCELED) {
                Log.e("Response: ", "Nothing selected");
            }
        }
    }

    public void signup() {
        if (!validate()) {
            onSignupFailed();
            return;
        }
        signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String baseUrl = "http://192.168.0.105:8000/api/v1/rest-auth/registration/";
        JSONObject json = new JSONObject();

        try {
            json.put("username", index);
            json.put("email", email);
            json.put("first_name", name);
            json.put("last_name", lastName);
            json.put("uid", uid);
            json.put("password1", password);
            json.put("password2", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, baseUrl, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        onSignupSuccess();
                        Log.e("Response Register: ", "" + response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onSignupFailed();
                Log.e("Error: ", error.toString());
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

    public void onSignupSuccess() {
        signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        Toast.makeText(this, "Sing up successful !", Toast.LENGTH_LONG).show();
        finish();
    }

    public void onSignupFailed() {
        if(uid == null){
            Toast.makeText(this, "You have to register your Student Card.",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Sing up failed !", Toast.LENGTH_LONG).show();
        }
        signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;
        nameText = findViewById(R.id.input_name);
        lastNameText = findViewById(R.id.input_last_name);
        indexText = findViewById(R.id.input_index);
        emailText = findViewById(R.id.input_email);
        passwordText = findViewById(R.id.input_password);

        name = nameText.getText().toString();
        lastName = lastNameText.getText().toString();
        index = indexText.getText().toString();
        email = emailText.getText().toString();
        password = passwordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            nameText.setError("at least 3 characters");
            valid = false;
        } else {
            nameText.setError(null);
        }
        if (lastName.isEmpty() || lastName.length() < 3) {
            lastNameText.setError("at least 3 characters");
            valid = false;
        } else {
            lastNameText.setError(null);
        }
        if (index.isEmpty() || index.length() == 7) {
            indexText.setError("Exactly 6 characters");
            valid = false;
        } else {
            indexText.setError(null);
        }
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("enter a valid email address");
            valid = false;
        } else {
            emailText.setError(null);
        }
        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            passwordText.setError(null);
        }
        if (uid == null) {
            valid = false;
        }
        return valid;
    }
}
