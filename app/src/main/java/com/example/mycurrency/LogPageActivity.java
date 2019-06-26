package com.example.mycurrency;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class LogPageActivity extends AppCompatActivity {

    private EditText login, password;
    private Button btn, btn_save;

    public static FragmentManager fragmentManager;
    public static MyAppDatabase myAppDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_page);

        fragmentManager = getSupportFragmentManager();
        myAppDatabase = Room.databaseBuilder(getApplicationContext(), MyAppDatabase.class, "userdb").allowMainThreadQueries().build();

        addListenerOnButton();
    }

    public void addListenerOnButton () {
        final LogPageActivity logPageActivity = this;

        login = (EditText)findViewById(R.id.login);
        password = (EditText)findViewById(R.id.password);
        btn = (Button)findViewById(R.id.button);
        btn_save = (Button)findViewById(R.id.button_save);

        btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<User> users = myAppDatabase.myDao().getUserPass(login.getText().toString());

                        for (User usr : users) {
                            String log = usr.getUser_login();
                            String pass = usr.getUser_password();
                            if (password.getText().toString().equals(pass)) {
                                Toast.makeText(getBaseContext(), "Success", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(logPageActivity, MainActivity.class);
                                intent.putExtra("1", log);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getBaseContext(), "Failure", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }
                }
        );

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String log = login.getText().toString();
                String pass = password.getText().toString();

                List<User> allUsers = myAppDatabase.myDao().getUserPass(login.getText().toString());

                boolean bool = true;

                for (User usr : allUsers) {
                    String logUser = usr.getUser_login();
                    if (log.equals(logUser)) {
                        bool = false;
                    }
                }

                if (bool) {
                    User user = new User();
                    user.setUser_login(log);
                    user.setUser_password(pass);

                    myAppDatabase.myDao().addUser(user);
                    Toast.makeText(getBaseContext(), "User added", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getBaseContext(), "Choose another login", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}