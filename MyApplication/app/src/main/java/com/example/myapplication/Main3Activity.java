package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.tasks.OnSuccessListener;
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
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Main3Activity extends AppCompatActivity {
    ArrayList<String> chatList=new ArrayList<String>();
    String loginUser=null;
    ArrayAdapter arrayAdt;
    String userName=null;
    String header=null;
    FirebaseAuth fAuth;
    ListView myText;
    EditText chatTxt;
    Button sendButton;
    DatabaseReference myDatabase;
    DatabaseReference dbr;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        fStore=FirebaseFirestore.getInstance();
        fAuth=FirebaseAuth.getInstance();
        Intent intent=getIntent();
        UserData receiver=intent.getParcelableExtra("user");
        String rID=receiver.getId();
        String sID=fAuth.getUid();
        int cmpStr=sID.compareTo(receiver.getId());
        if(cmpStr<0){
            header=sID.concat(receiver.getId());
        }else if(cmpStr>0){
            header=receiver.getId().concat(sID);
        }
        dbr=FirebaseDatabase.getInstance().getReference("Message").child(header);
        myText=(ListView)findViewById(R.id.chatTextView);

        arrayAdt=new ArrayAdapter(Main3Activity.this,android.R.layout.simple_list_item_1,chatList);
        myText.setAdapter(arrayAdt);

        sendButton=findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });


        chatTxt=findViewById(R.id.sendText);
        String sender=fAuth.getUid();


        userName=receiver.getName();
        int cmp=sender.compareTo(receiver.getId());
        if(cmp<0){
            header=sender.concat(receiver.getId());
        }else if(cmp>0){
            header=receiver.getId().concat(sender);
        }
        Log.e("Receiver",header);
        myDatabase = FirebaseDatabase.getInstance().getReference("Message").child(header); // Child Name
        Log.e("Database",myDatabase.getKey());
        myText=findViewById(R.id.chatTextView);
        //myDatabase.child(header).setValue("test");

        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                List<String> set=new ArrayList<String>();
                Map<String,String> hashMap=new HashMap<String,String>();
                TreeMap<String, String> sorted = new TreeMap<>();
                Iterator i=dataSnapshot.getChildren().iterator();
                while (i.hasNext()){
                  set.add(((DataSnapshot)i.next()).getValue().toString());
                   // hashMap.put(((DataSnapshot)i.next()).getKey().toString(),((DataSnapshot)i.next()).getValue().toString());
                }
                arrayAdt.clear();
                /*sorted.putAll(hashMap);
                for (Map.Entry<String, String> entry : sorted.entrySet()){
                    set.add(entry.getValue());
                }*/
                arrayAdt.addAll(set);
                arrayAdt.notifyDataSetChanged();



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }







        });


        DocumentReference documentReference=fStore.collection("user").document(fAuth.getCurrentUser().getUid());
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                loginUser=documentSnapshot.getString("name");
            }
        });




    }




    public void sendMessage()  {
        String header = null;
        EditText sendMsg=findViewById(R.id.sendText);
        String sender=fAuth.getCurrentUser().getUid();
        String receiver="";
        int cmp=sender.compareTo(receiver);
        if(cmp<0){
            header=sender.concat(receiver);
        }else if(cmp>0){
            header=receiver.concat(sender);
        }
        Calendar cal=Calendar.getInstance();
        JSONObject jsonObject=new JSONObject();
        try{

            jsonObject.put("id",sender);
            jsonObject.put("message",sendMsg.getText().toString());
        }catch (JSONException e){}

        myDatabase.child(String.valueOf(cal.getTimeInMillis())).setValue(loginUser+" : "+ sendMsg.getText().toString());
        // myDatabase.child(header).setValue(sendMsg.getText().toString());
        arrayAdt.add(loginUser+" : "+ sendMsg.getText().toString());
        Log.e("Send Button Click",arrayAdt.toString());
        sendMsg.setText("");
    }
}
