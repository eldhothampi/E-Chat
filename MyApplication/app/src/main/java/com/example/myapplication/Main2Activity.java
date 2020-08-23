package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.AppBarConfiguration;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myapplication.adapters.UserAdapter;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Main2Activity extends AppCompatActivity {
    private DatabaseReference myDatabase;
    FirebaseAuth fAuth;
    ListView myText;
    FirebaseFirestore fStore;
    ArrayList<UserData> listOfChat=new ArrayList<UserData>();
    UserAdapter arrayAdapt;

    private  DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().getRoot();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View view= navigationView.getHeaderView(0);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();

        myText=findViewById(R.id.chatTextView);
        myText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Main2Activity.this,Main3Activity.class);
                intent.putExtra("user", listOfChat.get(position));
                startActivity(intent);
            }
        });
        arrayAdapt=new UserAdapter(Main2Activity.this,listOfChat);
        myText.setAdapter(arrayAdapt);
        fStore=FirebaseFirestore.getInstance();

      fStore.collection("user").get().continueWithTask(new Continuation<QuerySnapshot, Task<List<QuerySnapshot>>>() {
          @Override
          public Task<List<QuerySnapshot>> then(@NonNull Task<QuerySnapshot> task) {
              List<Task<QuerySnapshot>> tasks = new ArrayList<Task<QuerySnapshot>>();
              for (DocumentSnapshot ds : task.getResult()) {
                  Log.e("ResultSet", ds.getId());

                      final DocumentReference documentReference = fStore.collection("user").document(ds.getId().toString());
                      documentReference.addSnapshotListener(Main2Activity.this, new EventListener<DocumentSnapshot>() {
                          @Override
                          public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                              Log.e("User Id's", documentReference.getId());
                              Log.e("User Name's", documentSnapshot.getString("name"));
                              if (!fAuth.getCurrentUser().getUid().equalsIgnoreCase(documentReference.getId())) {
                                  UserData obj = new UserData(documentReference.getId(), documentSnapshot.getString("name"));
                                  listOfChat.add(obj);
                              }
                              arrayAdapt.notifyDataSetChanged();

                          }
                      });


                  }
                  Log.e("Final Names", listOfChat.toString());
                  return Tasks.whenAllSuccess(tasks);
              }


      });

     /* documentSnapshot.getResult().getData();
        Log.e("Hello",documentSnapshot.getResult().getData().toString());*/
       // arrayAdapt=new ArrayAdapter(Main2Activity.this,android.R.layout.simple_list_item_1,listOfChat);
      //  myText.setAdapter(arrayAdapt);
        fAuth=FirebaseAuth.getInstance();




     //   myDatabase = FirebaseDatabase.getInstance().getReference("Message").child("Anoop");

     //    myText=findViewById(R.id.chatTextView);


       /* myDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Set<String> set=new HashSet<String>();
                Iterator i=dataSnapshot.getChildren().iterator();
while (i.hasNext()){
    set.add(((DataSnapshot)i.next()).getKey());
}
                arrayAdapt.clear();
arrayAdapt.addAll(set);


           //     ArrayList<String> chats=new ArrayList<>();
          //    Object obj=dataSnapshot.getValue();
            //    String[]=dataSnapshot.getValue().toString().split("")
;          //      Collections.reverse(chats);
            //    ArrayAdapter arrayAdapter=new ArrayAdapter(Main2Activity.this,android.R.layout.simple_list_item_1,chats);
            //    myText.setAdapter(arrayAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/


    }



    public void onChat(View view){
        startActivity(new Intent(getApplicationContext(),Main2Activity.class));
    }
}