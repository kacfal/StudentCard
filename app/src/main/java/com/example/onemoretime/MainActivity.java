package com.example.onemoretime;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
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

    @BindView(R.id.first_name_edit) TextView first_name_edit;
    @BindView(R.id.last_name_edit) TextView last_name_edit;
    @BindView(R.id.index_edit) TextView index_edit;
    @BindView(R.id.card_number_edit) TextView card_number_edit;

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
        getMenuInflater().inflate(R.menu.menu, menu);
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
        first_name_edit = (TextView) findViewById(R.id.first_name_edit);
        last_name_edit = (TextView) findViewById(R.id.last_name_edit);
        index_edit = (TextView) findViewById(R.id.index_edit);
        card_number_edit = (TextView) findViewById(R.id.card_number_edit);

        first_name_edit.setText(name);
        last_name_edit.setText(lastName);
        index_edit.setText(index);
        card_number_edit.setText(uid);
    }
}
