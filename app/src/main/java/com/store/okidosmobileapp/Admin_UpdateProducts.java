package com.store.okidosmobileapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.store.okidosmobileapp.Model.Product;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Admin_UpdateProducts extends AppCompatActivity {

    private Button updateButton;
    private ImageView updateImage;

    private EditText updateProductName;
    private EditText updateProductDescription;
    private EditText updateProductPrice;
    private String updateCategory;
    private String updateProductID;
    private String addProductID = "";
    private String updateItemID = "";
    DatabaseReference ProductRef;
    Product products;
    AwesomeValidation awesomeValidation;
    String url;

    private String Cartkey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_update_products);
        ProductRef = FirebaseDatabase.getInstance().getReference().child("Product");
        addProductID = getIntent().getStringExtra("productID");
        updateItemID = getIntent().getStringExtra("productID");

        updateButton = (Button) findViewById(R.id.admin_update_button);
        updateImage = (ImageView) findViewById(R.id.update_product_image1);
        updateProductName = (EditText) findViewById(R.id.update_product_name1);
        updateProductDescription = (EditText) findViewById(R.id.update_product_description1);
        updateProductPrice = (EditText) findViewById(R.id.update_product_price1);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        awesomeValidation.addValidation(this,R.id.update_product_name1, RegexTemplate.NOT_EMPTY,R.string.invalid_name);
        awesomeValidation.addValidation(this,R.id.update_product_description1, RegexTemplate.NOT_EMPTY,R.string.invalid_description);
        awesomeValidation.addValidation(this,R.id.txt_product_gender, RegexTemplate.NOT_EMPTY,R.string.invalid_gender);
        awesomeValidation.addValidation(this,R.id.update_product_price1,RegexTemplate.NOT_EMPTY,R.string.invalid_price);


        getProductDetails(addProductID);

        products = new Product();


        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(awesomeValidation.validate()){
                    Toast.makeText(getApplicationContext(), "Form Validate Successfully..", Toast.LENGTH_SHORT).show();
                    updateList();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Validation Fail", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    private void updateList()
    {
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd,YYYY");
        String saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        String saveCurrentTime = currentTime.format(calendar.getTime());


        products.setProductID(updateItemID);

        products.setProductName(updateProductName.getText().toString().trim());
        products.setProductDescription(updateProductDescription.getText().toString().trim());
//        products.setProductPrice(updateProductPrice.getText().toString().trim());
        products.setProductPrice(updateProductPrice.getText().toString());
        products.setProductCategory(updateCategory);
        products.setDate(saveCurrentDate);
        products.setTime(saveCurrentTime);
        products.setProductImage(url);


        ProductRef.child(updateItemID).setValue(products);

        Toast.makeText(Admin_UpdateProducts.this, "Product Details Updated", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Admin_UpdateProducts.this, Admin_HomeFragment.class);
        startActivity(intent);

    }

    private void getProductDetails(final String addProductID)
    {

        ProductRef = FirebaseDatabase.getInstance().getReference().child("Product");

        ProductRef.child(addProductID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {

                if(snapshot.exists())
                {
                    Product product = snapshot.getValue(Product.class);

                    updateProductName.setText(product.getProductName());
                    updateItemID = product.getProductID();
                    updateProductDescription.setText(product.getProductDescription());
                    updateProductPrice.setText(product.getProductPrice());
                    updateCategory = product.getProductCategory();

                    Picasso.get().load(product.getProductImage()).into(updateImage);
                    url = snapshot.child("productImage").getValue(String.class);



                }

            }




            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



}