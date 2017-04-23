package com.didar.mobile.chatusingfirebase;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    Button add_room;
    EditText room_name;
    ListView listView;
    String name;
    ArrayAdapter<String> adapter;
    ArrayList<String> arrayList = new ArrayList<>();
    DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        add_room = (Button) findViewById(R.id.btn_add_room);
        room_name = (EditText) findViewById(R.id.room_name);
        listView = (ListView) findViewById(R.id.list_rooms);

        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayList);

        listView.setAdapter(adapter);
        req_username();

        add_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String,Object> mp = new HashMap<>();
                mp.put(room_name.getText().toString(),"");
                root.updateChildren(mp);
                room_name.setText("");
            }
        });
        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Set<String> st = new HashSet<String>();
                for (Object o : dataSnapshot.getChildren()) {
                    st.add(((DataSnapshot) (o)).getKey());
                }
                arrayList.clear();
                arrayList.addAll(st);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this,ChatActivity.class);
                intent.putExtra("USERNAME",name);
                intent.putExtra("ROOMNAME",((TextView)view).getText().toString());
                startActivity(intent);
            }
        });
    }
    void req_username(){
     AlertDialog.Builder builder =  new  AlertDialog.Builder(this);
        builder.setTitle("Enter Username");
        final EditText nameField = new EditText(this);
        builder.setView(nameField);
        builder.setCancelable(false);
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                name = nameField.getText().toString();
            }
        });
        builder.setNegativeButton("Cancel",null);
        builder.show();

    }

}
