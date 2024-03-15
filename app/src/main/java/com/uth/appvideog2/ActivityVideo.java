package com.uth.appvideog2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;

import android.widget.Toast;
import android.widget.VideoView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ActivityVideo extends AppCompatActivity {
    static final int peticion_camara = 100;
    static final int peticion_video = 102;
    static final int peticion_seleccionar_video = 104;
    VideoView videoView;
    Button btnGrabar, btnAlmacenar, btnAlmacenamiento;
    private Uri videoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        videoView = (VideoView) findViewById(R.id.videoView);
        btnGrabar = (Button) findViewById(R.id.btnGrabar);
        btnAlmacenar = (Button) findViewById(R.id.btnAlmacenar);
        btnAlmacenamiento = findViewById(R.id.btnAlmacenamiento);

        btnGrabar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Permisos();
            }
        });

        btnAlmacenar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarVideo();
            }
        });

        btnAlmacenamiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirAlmacenamiento();
            }
        });

        btnAlmacenar.setEnabled(false);

    }
    private void guardarVideo() {

        videoView.stopPlayback();
        videoView.setVideoURI(null);
        btnAlmacenar.setEnabled(false);

        try {
            AssetFileDescriptor videoAsset = getContentResolver().openAssetFileDescriptor(videoUri, "r");
            FileInputStream in = videoAsset.createInputStream();
            FileOutputStream archivo = openFileOutput(crearNombreArchivoMP4(), Context.MODE_PRIVATE);
            byte[] buf = new byte[1024];
            int len;

            while ((len = in.read(buf)) > 0) {
                archivo.write(buf, 0, len);
            }


            Toast.makeText(this, "Video guardado correctamente", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Problemas al guardar el video", Toast.LENGTH_SHORT).show();
        }
    }


    private void Permisos() {

        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, peticion_camara);

        } else {
            tomarVideo();
        }
    }


    private void tomarVideo() {

        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, peticion_video);
        }
    }

    private void abrirAlmacenamiento() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("video/*");
        startActivityForResult(intent, peticion_seleccionar_video);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull
    int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == peticion_camara) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                tomarVideo();
            } else {
                Toast.makeText(getApplicationContext(), "Permiso Denegado", Toast.LENGTH_LONG).show();
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == peticion_video && resultCode == RESULT_OK) {

            this.videoUri = data.getData();
            videoView.setVideoURI(this.videoUri);
            videoView.start();
            btnAlmacenar.setEnabled(true);

        } else if (requestCode == peticion_seleccionar_video && resultCode == RESULT_OK) {
            Uri selectedVideoUri = data.getData();
            videoView.setVideoURI(selectedVideoUri);
            videoView.start();
        }
    }
    private String crearNombreArchivoMP4() {

        String fecha = new SimpleDateFormat("yyyyMMss_HHmmss").format(new Date());
        String nombre = fecha + ".mp4";
        return nombre;
    }
}