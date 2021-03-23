package com.upt.contact_plus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

public class searchContact extends AppCompatActivity {

    RequestQueue queue;
    EditText edtName, edtPhone, edtMail;
    Button btnSaveChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_contact);
        edtName = findViewById(R.id.edtNameShow);
        edtPhone = findViewById(R.id.edtPhoneShow);
        edtMail = findViewById(R.id.edtMailShow);
        btnSaveChange = findViewById(R.id.btnSave);
        ImageView logo = findViewById(R.id.imvLogo2);
        logo.setImageDrawable(getResources().getDrawable(R.drawable.ic_upt));
        String contact = getIntent().getStringExtra("contact");
        String srcURL = "https://contactsplus.000webhostapp.com/srch_contacts.php?contact="+contact;
        queue = Volley.newRequestQueue(searchContact.this);

        StringRequest srchreq = new StringRequest(Request.Method.POST, srcURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray action = new JSONArray(response);
                    for(int i=0; i < action.length(); i++){
                        JSONArray contactoact = action.getJSONArray(i);
                        edtName.setText(contactoact.getString(1));
                        edtPhone.setText(contactoact.getString(2));
                        edtMail.setText(contactoact.getString(3));
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT);
            }
        });
        queue.add(srchreq);

        btnSaveChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String changeURL = "https://contactsplus.000webhostapp.com/upd_contacts.php?contact="+contact+"&name="+edtName.getText().toString()+"&phone="+edtPhone.getText().toString()+"&mail="+edtMail.getText().toString();
                queue = Volley.newRequestQueue(searchContact.this);

                StringRequest updReq = new StringRequest(Request.Method.POST, changeURL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("1")){
                            Intent updContact = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(updContact);
                        }else{
                            Toast.makeText(getApplicationContext(), "Error al Guardar el Contacto", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
                queue.add(updReq);
            }
        });
    }
}