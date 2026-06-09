package com.tripleJTec.rotinaplus.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.tripleJTec.rotinaplus.R;
import com.tripleJTec.rotinaplus.data.DataBase; // Importação do seu banco de dados!

public class ProfileActivity extends AppCompatActivity {

    private ImageView imgProfile;
    private Button btnTakeFoto;

    private ActivityResultLauncher<String> requestPermissionLauncher;
    private ActivityResultLauncher<Intent> takePictureLauncher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // 1. Vincula os componentes do XML
        imgProfile = findViewById(R.id.imgProfile);
        btnTakeFoto = findViewById(R.id.btnTakeFoto);
        Button btnVoltar = findViewById(R.id.btnVoltar);

        // 2. Carrega a foto do banco assim que a tela abre
        carregarFotoSalva();

        // 3. Inicializa os launchers da câmera e permissão
        inicializarLaunchers();

        // 4. ATIVA O CLIQUE DO BOTÃO DE TIRAR FOTO (A linha que estava faltando!)
        btnTakeFoto.setOnClickListener(view -> verificarPermissaoEAbrirCamara());

        // 5. Ativa o clique do botão de voltar
        btnVoltar.setOnClickListener(view -> finish());
    }

    private void inicializarLaunchers() {
        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        abrirCamara();
                    } else {
                        Toast.makeText(this, "Permissão da câmera negada.", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        takePictureLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Bundle extras = result.getData().getExtras();
                        if (extras != null) {
                            try {
                                Bitmap imageBitmap = (Bitmap) extras.get("data");
                                imgProfile.setImageBitmap(imageBitmap);

                                String fotoEmTextoBase64 = converterImagemParaTexto(imageBitmap);

                                DataBase db = new DataBase(ProfileActivity.this);
                                boolean sucesso = db.salvarFotoPerfil(fotoEmTextoBase64);

                                if (sucesso) {
                                    Toast.makeText(ProfileActivity.this, "Foto salva com sucesso!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ProfileActivity.this, "Erro: Nenhum usuário no banco para atualizar.", Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception e) {
                                // ESTA É A ARMADILHA! Vai mostrar o erro real na tela.
                                Toast.makeText(ProfileActivity.this, "ERRO FATAL: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
        );
    }

    private void verificarPermissaoEAbrirCamara() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            abrirCamara();
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void abrirCamara() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            takePictureLauncher.launch(takePictureIntent);
        } else {
            Toast.makeText(this, "Nenhum aplicativo de câmera encontrado.", Toast.LENGTH_SHORT).show();
        }
    }

    // Método para traduzir a imagem para texto (Base64)
    private String converterImagemParaTexto(Bitmap bitmap) {
        java.io.ByteArrayOutputStream stream = new java.io.ByteArrayOutputStream();
        // Comprime a imagem para JPEG (qualidade 70%)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] imagemEmBytes = stream.toByteArray();
        // Converte os bytes em uma String
        return android.util.Base64.encodeToString(imagemEmBytes, android.util.Base64.DEFAULT);
    }

    private void carregarFotoSalva() {
        DataBase db = new DataBase(this);

        // Tira o "1". Fica apenas assim:
        String fotoBase64 = db.getFotoPerfil();

        if (fotoBase64 != null && !fotoBase64.isEmpty()) {
            byte[] bytesDaImagem = android.util.Base64.decode(fotoBase64, android.util.Base64.DEFAULT);
            Bitmap bitmap = android.graphics.BitmapFactory.decodeByteArray(bytesDaImagem, 0, bytesDaImagem.length);
            imgProfile.setImageBitmap(bitmap);
        }
    }
}