package com.example.karbantartasirendszer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.HashMap;
import java.util.Map;

public class addUser extends AppCompatActivity {

    private Button hozzaad, addV;
    private EditText password, name;
    private Spinner role, vegz, users;
    private ArrayAdapter<CharSequence> roleAdapter;
    private ArrayAdapter<String> vegzettsegAdapter;
    private ArrayAdapter<String> usersAdapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        name = findViewById(R.id.name);
        addV = findViewById(R.id.addV);
        hozzaad = findViewById(R.id.hozzaad);
        role = findViewById(R.id.role);
        password = findViewById(R.id.password);
        vegz = findViewById(R.id.vegz);
        users = findViewById(R.id.users);
        roleAdapter = ArrayAdapter.createFromResource(this, R.array.roles, android.R.layout.simple_spinner_dropdown_item);
        roleAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        role.setAdapter(roleAdapter);
        Loader.loadVegzettsegek();
        vegzettsegAdapter = new ArrayAdapter<String>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, Loader.vegzettsegek) {
            // Disable click item < month current
            @Override
            public boolean isEnabled(int position) {
                // TODO Auto-generated method stub
                if(position == 0)
                    return false;
                return true;
            }
        };;
        vegzettsegAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        vegz.setAdapter(vegzettsegAdapter);

        Loader.loadFelhasznalok();

        usersAdapter = new ArrayAdapter<String>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, Loader.felhasznalok) {
            // Disable click item < month current
            @Override
            public boolean isEnabled(int position) {
                // TODO Auto-generated method stub
                if(position == 0)
                    return false;
                return true;
            }
        };;
        usersAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        users.setAdapter(usersAdapter);
    }

    public void hozzaad(View v){

        Felhasznalo uj = new Felhasznalo(name.getText().toString(), password.getText().toString(), role.getSelectedItem().toString());

        Map<String, Object> note = new HashMap<>();
        note.put("role", uj.getSzerep());
        note.put("pw", uj.getJelszo());


        db.collection("Users").document(uj.getNev()).set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(addUser.this,"Note saved",Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(addUser.this,"Error",Toast.LENGTH_LONG).show();

            }
        });
    }

    public void addVegzettseg(View v){
        String user = users.getSelectedItem().toString();
        String vegzettseg = vegz.getSelectedItem().toString();

        Map<String, Object> note = new HashMap<>();
        note.put("vegzettseg", vegzettseg);

        db.collection("Users").document(user).update("Vegzettsegek", FieldValue.arrayUnion(vegzettseg)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(addUser.this,"Note saved",Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(addUser.this,"Error",Toast.LENGTH_LONG).show();

            }
        });

    }
}