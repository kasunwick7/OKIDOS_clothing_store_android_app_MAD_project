package com.store.okidosmobileapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.store.okidosmobileapp.Model.Product;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Admin_InsertProduct extends AppCompatActivity {

    private String pname, pdescription, pgender, pcategory, pprice, saveCurrentDate, saveCurrentTime;
    private Button addProductsBtn;
    private EditText product_name;
    private EditText product_description;
    private EditText product_gender;
    private EditText product_category;
    private EditText product_price;
    private ImageView productImg;
    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private String productRandomKey;
    private StorageReference ProductImageRef;
    private String downloadImageUrl;
    private DatabaseReference ProductsRef;
    private ProgressDialog loadingBar;
    Product product;
    private String key;
    AwesomeValidation awesomeValidation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_insert_product);

        ProductImageRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Product");

        addProductsBtn = (Button) findViewById(R.id.btn_submit_products);
        product_name = (EditText) findViewById(R.id.txt_product_name);
        product_description = (EditText) findViewById(R.id.txt_product_description);
        product_gender = (EditText) findViewById(R.id.txt_product_gender);
        product_category = (EditText) findViewById(R.id.txt_product_category);
        product_price = (EditText) findViewById(R.id.txt_product_price);
        productImg = (ImageView) findViewById(R.id.product_image);
        loadingBar = new ProgressDialog(this);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        awesomeValidation.addValidation(this,R.id.txt_product_name, RegexTemplate.NOT_EMPTY,R.string.invalid_name);
        awesomeValidation.addValidation(this,R.id.txt_product_description, RegexTemplate.NOT_EMPTY,R.string.invalid_description);
        awesomeValidation.addValidation(this,R.id.txt_product_gender, RegexTemplate.NOT_EMPTY,R.string.invalid_gender);
        awesomeValidation.addValidation(this,R.id.txt_product_price,RegexTemplate.NOT_EMPTY,R.string.invalid_price);
        awesomeValidation.addValidation(this,R.id.txt_product_category, RegexTemplate.NOT_EMPTY,R.string.invalid_category);


//        String pattern = "([0-9]{5})(\\.)([0-2]{2})"; // 4 digits followe by . followed by 2 digits
//        Pattern r = Pattern.compile(pattern);
//        Matcher m = r.matcher((CharSequence) product_price);
//        if(m.matches())
//        {
//            System.out.println("Validated");
//        }
//        else
//        {
//            System.out.println("Please insert correct pattern");
//        }

        product = new Product();

        productImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openGallery();

            }
        });

        addProductsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(awesomeValidation.validate()){
                    Toast.makeText(getApplicationContext(), "Form Validate Successfully..", Toast.LENGTH_SHORT).show();
                    ValidateProductData();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Validation Fail", Toast.LENGTH_SHORT).show();
                }




            }
        });

    }

    private void openGallery()
    {

        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GalleryPick && resultCode == RESULT_OK && data != null)
        {

            ImageUri = data.getData();
            productImg.setImageURI(ImageUri);

        }
    }

    public boolean ValidateProductData()
    {

        pname = product_name.getText().toString();
        pdescription = product_description.getText().toString();
        pgender = product_gender.getText().toString();
        pcategory = product_category.getText().toString();
        pprice = product_price.getText().toString();

        if(ImageUri == null)
        {
            Toast.makeText(this,"Product image is mandatory...", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (TextUtils.isEmpty(pname))
        {
            Toast.makeText(this,"Enter Product Name", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (TextUtils.isEmpty(pdescription))
        {
            Toast.makeText(this,"Enter Product Description", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (TextUtils.isEmpty(pgender))
        {
            Toast.makeText(this,"Enter Product Gender", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (TextUtils.isEmpty(pcategory))
        {
            Toast.makeText(this,"Enter Product Category", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (TextUtils.isEmpty(pprice))
        {
            Toast.makeText(this,"Enter Product Price", Toast.LENGTH_SHORT).show();
            return false;
        }
        else
        {
            StoreProductInformation();
            return true;
        }

    }

    private void StoreProductInformation() {

        loadingBar.setTitle("Adding New Product");
        loadingBar.setMessage("Please wait while we are adding the new product");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd,YYYY");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = saveCurrentDate + saveCurrentTime;

        final StorageReference filePath = ProductImageRef.child(ImageUri.getLastPathSegment() + productRandomKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {

                String message = e.toString();
                Toast.makeText(Admin_InsertProduct.this, "Error: "+message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {

                Toast.makeText(Admin_InsertProduct.this, "Image Upload Successfully", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                    {
                        if (!task.isSuccessful())
                        {
                            throw task.getException();

                        }

                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task)
                    {

                        if(task.isSuccessful())
                        {
                            downloadImageUrl = task.getResult().toString();
                            Toast.makeText(Admin_InsertProduct.this, "got the Product Image Url Successfully", Toast.LENGTH_SHORT).show();

                            SaveProductInfoToDatabase();
                        }

                    }
                });

            }

        });

    }

    private void SaveProductInfoToDatabase()
    {

        key = ProductsRef.push().getKey();
        product.setProductID(key);
        product.setDate(saveCurrentDate);
        product.setTime(saveCurrentTime);
        product.setProductName(product_name.getText().toString().trim());
        product.setProductDescription(product_description.getText().toString().trim());
        product.setProductGender(product_gender.getText().toString().trim());
        product.setProductCategory(product_category.getText().toString().trim());
        product.setProductPrice(product_price.getText().toString().trim());
        product.setProductImage(downloadImageUrl);

        ProductsRef.child(key).setValue(product);


        Toast.makeText(Admin_InsertProduct.this, "Product is added successfully", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Admin_InsertProduct.this, Admin_HomeFragment.class);
        startActivity(intent);

        //HashMap<String, Object> productMap = new HashMap<>();
        //productMap.put("productID", productRandomKey);
        //productMap.put("Date", saveCurrentDate);
        //productMap.put("Time", saveCurrentTime);
        //productMap.put("productName", product_name);
        //productMap.put("productDescription", product_description);
        //productMap.put("productGender", product_gender);
        //productMap.put("productCategory", product_category);
        //productMap.put("productPrice", product_price);
       // productMap.put("productImage", downloadImageUrl);

        //ProductsRef.child(productRandomKey).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
        //    @Override
       //     public void onComplete(@NonNull Task<Void> task)
        //    {

         //       if(task.isSuccessful())
          //      {
           //         Intent intent = new Intent(InsertProduct.this, HomeFragment.class);
           //         startActivity(intent);

            //        loadingBar.dismiss();
             //       Toast.makeText(InsertProduct.this, "Product is added successfully", Toast.LENGTH_SHORT).show();
             //   }
             //   else
              //  {
               //     loadingBar.dismiss();
              //      String message = task.getException().toString();
              //      Toast.makeText(InsertProduct.this, "Error: "+message, Toast.LENGTH_SHORT).show();
              //  }

          //  }
       // });
    }

}