package com.store.okidosmobileapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;

public class Admin_ProductDetailsActivity extends AppCompatActivity {

    private Button addToCartButton;
    private ImageView addToCartImage;
    private ElegantNumberButton addToCartNumberButton;
    private TextView addToCartProductName;
    private TextView addtoCartProductDescription;
    private TextView addToCartProductPrice;
    private String addtoCartCategory;
    private String addtoCartProductID;
    private String addToCartProductID = "";
    private String Cartkey;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_product_details);

        addToCartProductID = getIntent().getStringExtra("productID");

        addToCartImage = (ImageView) findViewById(R.id.addtocart_product_image);
        addToCartProductName = (TextView) findViewById(R.id.addtocart_product_name);
        addtoCartProductDescription = (TextView) findViewById(R.id.addtocart_product_description);
        addToCartProductPrice = (TextView) findViewById(R.id.addtocart_product_price);

                
    }
}