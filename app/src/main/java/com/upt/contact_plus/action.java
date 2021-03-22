package com.upt.contact_plus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class action extends AppCompatActivity {

    TextView txvName;
    ImageButton btnLlamar, btnMail;
    String phone;
    private final int PHONE_CALL_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action);

        txvName = (TextView) findViewById(R.id.txvNameContact);
        String name = getIntent().getStringExtra("name");
        txvName.setText(name);

        btnLlamar = (ImageButton) findViewById(R.id.imvLlamar);
        btnLlamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone = getIntent().getStringExtra("phone");
                if(phone != null){
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, PHONE_CALL_CODE);
                    }else{
                        versionesAnteriores(phone);
                    }
                }
            }
        });

        btnMail = (ImageButton) findViewById(R.id.imbMailContact);
        btnMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = getIntent().getStringExtra("mail");
                Intent email = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto",mail, null));
                email.putExtra(Intent.EXTRA_SUBJECT, "App Contact+ - ");
                startActivity(Intent.createChooser(email,  "Email Nuevo"));
            }
        });
    }

    private void versionesAnteriores(String num){
        Intent llamada = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+num));
        if(verificarPermisos(Manifest.permission.CALL_PHONE)){
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                startActivity(llamada);
            }
        }else{
            Toast.makeText(this, "Configura los Permisos", Toast.LENGTH_SHORT);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PHONE_CALL_CODE:
                String permission = permissions[0];
                int result = grantResults[0];
                if(permission.equals(Manifest.permission.CALL_PHONE)){
                    if(result == PackageManager.PERMISSION_GRANTED){
                        String phoneNum = phone;
                        Intent llamada = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+phoneNum));
                        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
                            startActivity(llamada);
                        }else{
                            Toast.makeText(this, "No aceptes el permiso", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    private boolean verificarPermisos(String permisos){
        int resultado = this.checkCallingOrSelfPermission(permisos);
        return resultado == PackageManager.PERMISSION_GRANTED;
    }
}