package com.store.okidosmobileapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Admin_MainActivity extends AppCompatActivity{
    Button loginbutton;
    Button insertProductsbutton;
    Button viewButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_main);


        insertProductsbutton = findViewById(R.id.addnewProductsbtn);
        viewButton = findViewById(R.id.viewButn);

    }

    @Override
    protected void onResume() {
        super.onResume();

        insertProductsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                Intent intent = new Intent(Admin_MainActivity.this, Admin_InsertProduct.class);
                startActivity(intent);

            }
        });

        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Admin_MainActivity.this, Admin_HomeFragment.class);
                startActivity(intent);
            }
        });
    }
}