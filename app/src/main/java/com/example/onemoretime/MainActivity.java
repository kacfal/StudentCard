package com.example.onemoretime;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.onemoretime.LoginRegister.LoginActivity;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import butterknife.BindView;

public class MainActivity extends Activity {
    RequestQueue requestQueue;
    public String name;
    public String lastName;
    public String index;
    public String email;
    public String uid;
    private String token;

    @BindView(R.id.userTextView) TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestQueue = Volley.newRequestQueue(this);
        if(name == null)
            getUser();
        setUserDataOnTextView();
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                token = data.getStringExtra("token");
                Log.e("Response: ", "" + token);
            }
            if (resultCode == RESULT_CANCELED) {
                Log.e("Response: ", "Nothing selected");
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            Toast.makeText(this, "Logout",
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void getUser() {
        String baseUrl = "http://192.168.43.95:8000/api/v1/rest-auth/user/";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, baseUrl, null,
                new Response.Listener<JSONObject>() {
                    public void onResponse(JSONObject response) {
                        try {
                            name = response.getString("first_name");
                            lastName = response.getString("last_name");
                            index = response.getString("username");
                            email = response.getString("email");
                            uid = response.getString("uid");
                            Log.e("last_name: ", lastName);
                            Log.e("index: ", index);
                            Log.e("email: ", email);
                            Log.e("name: ", name);

                        } catch (JSONException e) {
                            Log.e("Error: ", "Invalid JSON Object.");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error: ", error.toString());
            }
        }){
        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            HashMap<String, String> headers = new HashMap<String, String>();
            headers.put("Content-Type", "application/json");
            headers.put("Authorization", " Token " + token);
            Log.e("json: ", headers.toString());
            return headers;
        }};
        requestQueue.add(jsonObjectRequest);

    }

    private void setUserDataOnTextView(){

        if(name != null) {
            textView = (TextView) findViewById(R.id.userTextView);

            String someText = "name: " + name + "\nlastName: " + lastName + "\nindex: "
                    + index + "\nemail: " + email + "\nuid: " + uid;
            textView.setText(someText);
        }
    }
}
