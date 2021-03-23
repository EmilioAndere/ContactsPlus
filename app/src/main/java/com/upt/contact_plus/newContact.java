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

public class newContact extends AppCompatActivity {

    ImageView imvLogo;
    EditText edtName, edtPhone, edtMail;
    Button btnAgregar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contact);

        imvLogo = (ImageView) findViewById(R.id.imvLogo2);
        edtName = (EditText) findViewById(R.id.edtNameShow);
        edtPhone = (EditText) findViewById(R.id.edtPhoneShow);
        edtMail = (EditText) findViewById(R.id.edtMailShow);
        btnAgregar = (Button) findViewById(R.id.btnSave);

        imvLogo.setImageDrawable(getResources().getDrawable(R.drawable.ic_upt));

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newContact("https://contactsplus.000webhostapp.com/add_contacts.php?name="+edtName.getText().toString()+"&phone="+edtPhone.getText().toString()+"&mail="+edtMail.getText().toString()+"&image=default.png");
            }
        });
    }

    private void newContact(String URL){
        RequestQueue queue = Volley.newRequestQueue(newContact.this);

        StringRequest rmReq = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("1")){
                    Intent newContact = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(newContact);
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
        queue.add(rmReq);
    }
}