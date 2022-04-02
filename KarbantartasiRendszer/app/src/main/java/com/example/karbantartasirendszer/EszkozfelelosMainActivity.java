package com.example.karbantartasirendszer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.karbantartasirendszer.Eszkoz;
import androidx.annotation.NonNull;
import android.util.Log;
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
import com.google.android.gms.tasks.OnSuccessListener;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.firebase.firestore.FirebaseFirestore;

public class EszkozfelelosMainActivity extends AppCompatActivity {

    private EditText EditPlace, EditName, EditAzon, EditKateg, EditCom, EditKarIdo, EditNormIdo, EditInstr;
    private Button kuldes;
    private Button vissza;
    private static List<String> list;
    private static List<String> names;

    private static ArrayList<Type> mArrayList = new ArrayList<>();

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eszkozfelelos_main);

        EditPlace = findViewById(R.id.EditPlace);
        EditName = findViewById(R.id.EditName);
        EditAzon = findViewById(R.id.EditAzon);
        EditKateg = findViewById(R.id.EditKateg);
        EditCom = findViewById(R.id.EditCom);
        EditKarIdo = findViewById(R.id.EditKarIdo);
        EditNormIdo = findViewById(R.id.EditNormIdo);
        EditInstr = findViewById(R.id.EditInstr);
        vissza = findViewById(R.id.back);
        vissza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(EszkozfelelosMainActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
        kuldes = findViewById(R.id.submit);
        kuldes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                test();
            }
        });
    }
    private void test() {

                Map<String, Object> note = new HashMap<>();
                note.put("Azonosito",EditAzon.getText().toString());
                note.put("Elhelyezkedes",EditPlace.getText().toString());
                note.put("Kategoria",EditKateg.getText().toString());
                note.put("Leiras",EditCom.getText().toString());
                note.put("Karbantartasi ido",EditKarIdo.getText().toString());
                note.put("Norm ido",EditNormIdo.getText().toString());
                note.put("Instrukcio",EditInstr.getText().toString());


                FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
                CollectionReference Eszkozok = rootRef.collection("Eszkozok");

                Eszkozok.document(EditName.getText().toString()).set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(EszkozfelelosMainActivity.this,"Új eszköz elmentve", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EszkozfelelosMainActivity.this,"Valami hiba tortent",Toast.LENGTH_LONG).show();

                    }
                });;











    }




}