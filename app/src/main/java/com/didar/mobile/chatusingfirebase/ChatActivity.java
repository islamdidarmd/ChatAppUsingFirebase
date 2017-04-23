package com.didar.mobile.chatusingfirebase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    Button send_msg;
    EditText inp_msg;
    TextView chat_conversation;
    String user_name , room_name, chat_msg;
    DatabaseReference root = FirebaseDatabase.getInstance().getReference();
    String temp_key;
    String chat_msg_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        send_msg = (Button) findViewById(R.id.btn_send);
        inp_msg = (EditText) findViewById(R.id.msg_field);
        chat_conversation = (TextView) findViewById(R.id.txt_msg);

        user_name = getIntent().getStringExtra("USERNAME");
        room_name = getIntent().getStringExtra("ROOMNAME");

        setTitle("Room-"+room_name);
        root = FirebaseDatabase.getInstance().getReference().child(room_name);
        send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String,Object> mp = new HashMap<>();
                temp_key = root.push().getKey();

                DatabaseReference msg_root = root.child(temp_key);
                mp.put("name",user_name);
                mp.put("msg",inp_msg.getText().toString());
                msg_root.updateChildren(mp);
                inp_msg.setText("");
            }
        });
        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                append_chat_conversation(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void append_chat_conversation(DataSnapshot dataSnapshot) {
        Iterator i = dataSnapshot.getChildren().iterator();
        while(i.hasNext()){
            chat_msg = (String)((DataSnapshot)i.next()).getValue();
            chat_msg_name = (String)((DataSnapshot)i.next()).getValue();

            chat_conversation.append(chat_msg_name+" : "+chat_msg+"\n");
        }
    }
}
