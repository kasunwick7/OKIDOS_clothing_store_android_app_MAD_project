package com.store.okidosmobileapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.store.okidosmobileapp.Model.FeedBack;
import com.store.okidosmobileapp.Prevalent.Prevalent;

import java.util.Objects;

public class FeedBackActivity extends AppCompatActivity {

    EditText txtEmail, txtName, txtFeedback;
    Button butSave, butShow, butUpdate, butDelete;
    DatabaseReference dbRef;
    FeedBack feedBack;

    private String userKey;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.send_feedback);

        txtEmail = findViewById(R.id.edtEmail);
        txtName = findViewById(R.id.edtName);
        txtFeedback = findViewById(R.id.edtFeedback);

        butSave = findViewById(R.id.butSave);
        butShow = findViewById(R.id.butShow);
        butUpdate = findViewById(R.id.butUpdate);
        butDelete = findViewById(R.id.butDelete);


        feedBack = new FeedBack();
        userKey = Prevalent.CurrentOnlineUser.getPhone();

        dbRef = FirebaseDatabase.getInstance().getReference().child("FeedBack").child(userKey);


        butDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //dbRef = FirebaseDatabase.getInstance().getReference().child("Customer").child("cus1");
                dbRef.removeValue();
                Toast.makeText(getApplicationContext(), "Successfully deleted", Toast.LENGTH_SHORT).show();
            }
        });

        butUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feedBack.setName(txtName.getText().toString().trim());
                feedBack.setEmail(txtEmail.getText().toString().trim());
                feedBack.setFeedback(txtFeedback.getText().toString().trim());
                //dbRef = FirebaseDatabase.getInstance().getReference();
                //dbRef.child("Customer").child("cus1").child("email").setValue(txtEmail.getText().toString().trim());
                //dbRef.child("Customer/cus1/feedback").setValue(txtFeedback.getText().toString().trim());
                dbRef.setValue(feedBack);

                Toast.makeText(getApplicationContext(), "Successfully updated", Toast.LENGTH_SHORT).show();
                clearControls();

            }
        });

        butShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //dbRef = FirebaseDatabase.getInstance().getReference().child("Customer/cus1");
                dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChildren()) {
                            txtEmail.setText(Objects.requireNonNull(dataSnapshot.child("email").getValue()).toString());
                            txtName.setText(Objects.requireNonNull(dataSnapshot.child("name").getValue()).toString());
                            txtFeedback.setText(Objects.requireNonNull(dataSnapshot.child("feedback").getValue()).toString());
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Cannot find cus1", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        butSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //dbRef = FirebaseDatabase.getInstance().getReference().child("FeedBack");
                //dbRef.setValue(feedBack);
                try {
                    if (TextUtils.isEmpty(txtEmail.getText().toString()))
                        Toast.makeText(getApplicationContext(), "Empty email", Toast.LENGTH_SHORT).show();
                    else if (TextUtils.isEmpty(txtName.getText().toString()))
                        Toast.makeText(getApplicationContext(), "Empty name", Toast.LENGTH_SHORT).show();
                    else if (TextUtils.isEmpty(txtFeedback.getText().toString()))
                        Toast.makeText(getApplicationContext(), "Empty feedback", Toast.LENGTH_SHORT).show();
                    else {
                        feedBack.setEmail(txtEmail.getText().toString().trim());
                        feedBack.setName(txtName.getText().toString().trim());
                        feedBack.setFeedback(txtFeedback.getText().toString().trim());
                        dbRef.setValue(feedBack);
                        Toast.makeText(getApplicationContext(), "Successfully inserted", Toast.LENGTH_SHORT).show();
                        clearControls();
                    }
                }
                catch (NumberFormatException e){

                }
            }
        });
    }

    private void clearControls(){
        txtEmail.setText("");
        txtName.setText("");
        txtFeedback.setText("");
    }





    }