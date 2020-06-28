package com.example.zapimini;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.zapimini.localStorage.UserLocalStorage;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    UserLocalStorage userLocalStorage;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userLocalStorage = new UserLocalStorage(this);

        Thread timer = new Thread(){
            public void run(){

                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    if(userLocalStorage.isUserLogged()){
                        intent = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        };
        timer.start();
    }
}
