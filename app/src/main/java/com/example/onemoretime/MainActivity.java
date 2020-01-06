package com.example.onemoretime;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
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
    @BindView(R.id.logout) TextView logout;

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
        logout = (TextView) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivityForResult(intent, 0);
            }
        });
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
