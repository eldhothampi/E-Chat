package com.example.myapplication.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.myapplication.Main2Activity;
import com.example.myapplication.Main3Activity;
import com.example.myapplication.R;
import com.example.myapplication.UserData;
import com.example.myapplication.adapters.UserAdapter;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserListFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private DatabaseReference myDatabase;
    FirebaseAuth fAuth;
    ListView myText;
    FirebaseFirestore fStore;
    ArrayList<UserData> listOfChat=new ArrayList<UserData>();
    UserAdapter arrayAdapt;
    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.user_list_fragment, container, false);
//        final TextView textView = root.findViewById(R.id.text_home);
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        initilizie(root);
        return root;
    }

    private void initilizie(View root) {



        myText=root.findViewById(R.id.chatTextView);
        myText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), Main3Activity.class);
                intent.putExtra("user", listOfChat.get(position));
                startActivity(intent);
            }
        });
        arrayAdapt=new UserAdapter(getContext(),listOfChat);
        myText.setAdapter(arrayAdapt);
        fStore=FirebaseFirestore.getInstance();
        fAuth=FirebaseAuth.getInstance();
        fStore.collection("user").get().continueWithTask(new Continuation<QuerySnapshot, Task<List<QuerySnapshot>>>() {
            @Override
            public Task<List<QuerySnapshot>> then(@NonNull Task<QuerySnapshot> task) {
                List<Task<QuerySnapshot>> tasks = new ArrayList<Task<QuerySnapshot>>();
                for (DocumentSnapshot ds : task.getResult()) {
                    Log.e("ResultSet", ds.getId());

                    final DocumentReference documentReference = fStore.collection("user").document(ds.getId().toString());
                    documentReference.addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
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
    }
}
