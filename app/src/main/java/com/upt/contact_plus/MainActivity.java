package com.upt.contact_plus;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

public class MainActivity extends AppCompatActivity {

    LinearLayout resp;
    Button btnNuevo;
    RequestQueue queue;
    Intent refresh;
    Dialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resp = (LinearLayout) findViewById(R.id.lyoHome);
        btnNuevo = (Button) findViewById(R.id.btnAgregar);

        btnNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newContact = new Intent(getApplicationContext(), newContact.class);
                startActivity(newContact);
            }
        });

        listContacts("https://contactsplus.000webhostapp.com/list_contacts.php");
        refresh = new Intent(MainActivity.this, MainActivity.class);
    }

    private void rmContacts(String URL){
        queue = Volley.newRequestQueue(MainActivity.this);

        StringRequest rmReq = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("1")){
                    Toast.makeText(getApplicationContext(), "Contacto Eliminado", Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(getApplicationContext(), "Error al eliminar el contacto", Toast.LENGTH_SHORT).show();
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

    private void listContacts(String URL){
        queue = Volley.newRequestQueue(MainActivity.this);

        StringRequest lstreq = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray data = new JSONArray(response);
                    for(int i=0; i < data.length(); i++){
                        JSONArray contactos = data.getJSONArray(i);
                        //for(int j=0; j < contactos.length(); j++){
                            //Toast.makeText(getApplicationContext(), contactos.getString(j), Toast.LENGTH_SHORT).show();
                            View contact = View.inflate(getApplicationContext(), R.layout.contact_layout, null);

                            LinearLayout lnyContact = (LinearLayout) contact.findViewById(R.id.lynContact);
                            TextView name = (TextView) contact.findViewById(R.id.txvName);
                            TextView phone = (TextView) contact.findViewById(R.id.txvPhone);
                            TextView mail = (TextView) contact.findViewById(R.id.txvMail);
                            TextView id =(TextView) contact.findViewById(R.id.txvContactId);
                            ImageView profile = (ImageView) contact.findViewById(R.id.imvProfile);
                            ImageView contactPhone = (ImageView) contact.findViewById(R.id.imvContact);
                            ImageView contactMail = (ImageView) contact.findViewById(R.id.imvMail);
                            ImageButton delete = (ImageButton) contact.findViewById(R.id.imbDelete);

                            id.setText(contactos.getString(0));
                            name.setText(contactos.getString(1));
                            phone.setText(contactos.getString(2));
                            mail.setText(contactos.getString(3));
                            profile.setImageDrawable(getResources().getDrawable(R.drawable.ic_profile));
                            contactPhone.setImageDrawable(getResources().getDrawable(R.drawable.ic_contact));
                            contactMail.setImageDrawable(getResources().getDrawable(R.drawable.ic_mail));
                            delete.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_delete_24));

                            delete.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    rmContacts("https://contactsplus.000webhostapp.com/rm_contacts.php?id="+id.getText().toString());
                                    //finish();
                                    startActivity(refresh);
                                    overridePendingTransition(0,0);
                                }
                            });

                            lnyContact.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String srcURL = "https://contactsplus.000webhostapp.com/srch_contacts.php?contact="+id.getText().toString();
                                    queue = Volley.newRequestQueue(MainActivity.this);

                                    StringRequest srchreq = new StringRequest(Request.Method.POST, srcURL, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                JSONArray action = new JSONArray(response);
                                                for(int i=0; i < action.length(); i++){
                                                    JSONArray contactoact = action.getJSONArray(i);
                                                    Intent actionsContact = new Intent(getApplicationContext(), action.class);
                                                    actionsContact.putExtra("name", contactoact.getString(1));
                                                    actionsContact.putExtra("phone", contactoact.getString(2));
                                                    actionsContact.putExtra("mail", contactoact.getString(3));
                                                    startActivity(actionsContact);
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
                                }
                            });

                            resp.addView(contact);
                        //}
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
        queue.add(lstreq);
    }
}