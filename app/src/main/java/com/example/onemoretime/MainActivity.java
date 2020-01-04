package com.example.onemoretime;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.onemoretime.LoginRegister.LoginActivity;
import butterknife.BindView;

public class MainActivity extends Activity {
    RequestQueue requestQueue;
    public String name;
    public String lastName;
    public String index;
    public String email;
    public String uid;

    @BindView(R.id.userTextView) TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestQueue = Volley.newRequestQueue(this);
        name = getIntent().getStringExtra("first_name");
        lastName = getIntent().getStringExtra("last_name");
        index = getIntent().getStringExtra("username");
        email = getIntent().getStringExtra("email");
        uid = getIntent().getStringExtra("uid");

        setUserDataOnTextView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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

    private void setUserDataOnTextView(){

        if(name != null) {
            textView = (TextView) findViewById(R.id.userTextView);

            String someText = "name: " + name + "\nlastName: " + lastName + "\nindex: "
                    + index + "\nemail: " + email + "\nuid: " + uid;
            textView.setText(someText);
        }
    }
}
