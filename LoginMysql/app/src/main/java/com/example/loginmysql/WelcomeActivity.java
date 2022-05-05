package com.example.loginmysql;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {

    private TextView tvname,tvhobby;
    private Button btnlogout;
    private PreferenceHelper preferenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome2);

        preferenceHelper = new PreferenceHelper(this);

        tvhobby = (TextView) findViewById(R.id.tvhobby);
        tvname = (TextView) findViewById(R.id.tvname);
        btnlogout = (Button) findViewById(R.id.btn);

        tvname.setText("Welcome "+preferenceHelper.getName());
        tvhobby.setText("Your hobby is "+preferenceHelper.getHobby());

        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferenceHelper.putIsLogin(false);
                Intent intent = new Intent(WelcomeActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                WelcomeActivity.this.finish();
            }
        });

    }


}