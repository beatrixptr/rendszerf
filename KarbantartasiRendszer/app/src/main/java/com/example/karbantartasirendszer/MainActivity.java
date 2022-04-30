package com.example.karbantartasirendszer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private EditText nevET, jelszoET;
    private Button bejelentkezesBtn, colreftestBtn;
    private TextView tv,testTV;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nevET = findViewById(R.id.felhNevET);
        jelszoET = findViewById(R.id.felhJelszoET);
        //bejelentkezesBtn = findViewById(R.id.bejelentkezesBtn);
        colreftestBtn = findViewById(R.id.colreftestBtn);
        tv = findViewById(R.id.textView);
        testTV = findViewById(R.id.testTV);
        /*
        bejelentkezesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });*/
        colreftestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                test();
            }
        });

    }

    private void test(){
        CollectionReference collectionReference = db.collection("Users");
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    int count = 0;
                    String data = "";
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        count++;
                        data += document.getId()+"\n";
                        String nev = nevET.getText().toString();
                        String jelszo = jelszoET.getText().toString();
                        if(nev.equals(document.getId()) && jelszo.equals(document.getString("pw"))){
                            String szerep = document.getString("role");
                            switch (szerep){
                                case "admin":
                                    Intent i = new Intent(MainActivity.this, AdminMainActivity.class);
                                    startActivity(i);
                                    break;
                                case "eszkozfelelos":
                                    Intent b = new Intent(MainActivity.this, EszkozfelelosMainActivity.class);
                                    startActivity(b);
                                    break;
                                case "operator":
                                    Intent op = new Intent(MainActivity.this, OperatorMainActivity.class);
                                    startActivity(op);
                                    break;
                                case "karbantarto":
                                    testTV.setText("Karbantartó");
                                    Intent c = new Intent(MainActivity.this, KarbantartoMainActivity.class);
                                    startActivity(c);
                                    break;
                                default:
                                    break;
                            }
                        }
                        else{
                            Toast.makeText(MainActivity.this, "Helytelen bejelentkezési adatok", Toast.LENGTH_SHORT).show();
                        }
                    }
                    tv.setText(data + String.valueOf(count));
                } else {
                    Log.d("TAG", "Error getting documents: ", task.getException());
                }
            }
        });
    }





    /*
    private void login(){

        DocumentReference felhRef = db.collection("Users").document(nevET.getText().toString());
        felhRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    String jelszo = documentSnapshot.getString("pw");
                    String szerep = documentSnapshot.getString("role");
                    if(jelszo.equals(jelszoET.getText().toString())){
                        if(szerep.equals("admin")){
                            Intent i = new Intent(MainActivity.this, AdminMainActivity.class);
                            startActivity(i);
                        }

                        tv.setText(jelszo + ", " + szerep);
                    }
                    else{
                        tv.setText("Hibás jelszó");
                    }

                }
                else{
                    Toast.makeText(MainActivity.this, "Document does not exist", Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                Log.d("login", e.toString());
            }
        });
    }*/

        /*
        CollectionReference users = db.collection("Users");

        users.whereEqualTo("nev","sad").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                String data = "";
                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    Felhasznalo felhasznalo = documentSnapshot.toObject(Felhasznalo.class);
                    data += felhasznalo.getNev() + " " + felhasznalo.getJelszo();
                }
                tv.setText(data);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "HIBA", Toast.LENGTH_LONG).show();
            }
        }); */


        /*
        db.collection("Users")
                .whereEqualTo("name", "admin")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });*/


        /*String nev =  nevET.getText().toString();
        String jelszo = jelszoET.getText().toString();


        Map<String, Object> note = new HashMap<>();
        note.put("nev", nev);
        note.put("jelszo",jelszo);


        db.collection("Users").document("id123").set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(MainActivity.this,"Note saved",Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this,"Error",Toast.LENGTH_LONG).show();
                Log.d(TAG,e.toString());
            }
        });*/




}