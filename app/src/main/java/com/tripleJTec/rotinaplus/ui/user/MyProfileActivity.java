package com.tripleJTec.rotinaplus.ui.user;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.tripleJTec.rotinaplus.R;
import com.tripleJTec.rotinaplus.data.DataBase;
import com.tripleJTec.rotinaplus.model.User;
import com.tripleJTec.rotinaplus.ui.auth.MainActivity;
import com.tripleJTec.rotinaplus.ui.home.HomeActivity;
import com.tripleJTec.rotinaplus.ui.routines.CreateRoutineActivity;

import java.io.ByteArrayOutputStream;

public class MyProfileActivity extends AppCompatActivity {

    DataBase dbHelper;
    TextView txtNameMyProfile;
    EditText edtTxtNameMyProfile;
    Button btnNameEditMyProfile;
    ImageView imgCreateRoutineBottomMenuIcon, imgBottomMenuIcon;
    User user;
    ImageView imgMyProfile;
    Button btnPhotoEditMyProfile;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private ActivityResultLauncher<Intent> takePictureLauncher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        dbHelper = new DataBase(this);
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.app_shared_preferences_name), MODE_PRIVATE);

        // Iniciando views normais
        txtNameMyProfile = findViewById(R.id.txtNameMyProfile);
        edtTxtNameMyProfile = findViewById(R.id.edtTxtNameMyProfile);
        imgCreateRoutineBottomMenuIcon = findViewById(R.id.imgCreateRoutineBottomMenuIcon);
        imgBottomMenuIcon = findViewById(R.id.imgBottomMenuIcon);
        btnNameEditMyProfile = findViewById(R.id.btnNameEditMyProfile);

        // Iniciando views da câmera
        imgMyProfile = findViewById(R.id.imgMyProfile);
        btnPhotoEditMyProfile = findViewById(R.id.btnPhotoEditMyProfile);

        // Checagens de usuário
        if (dbHelper.checkUserAuthenticationWithSharedPreferences(sharedPreferences)) {
            String email = getEmailWithSharedPreferences(sharedPreferences);
            user = dbHelper.getUser(email);
            if (user != null) {
                setTxtTitleMyProfile();
                setImgCreateRoutineBottomMenuIconListener();
                setImgBottomMenuIconListener();
                setBtnNameEditMyProfileListener();
                inicializarLaunchersCamera();
                carregarFotoSalva();
                setBtnPhotoEditMyProfileListener();
            } else {
                goBackToLoginWithLogout(sharedPreferences);
            }
        }else {
            goBackToLoginWithLogout(sharedPreferences);
        }
    }

    private void setImgCreateRoutineBottomMenuIconListener() {
        imgCreateRoutineBottomMenuIcon.setOnClickListener(v -> {
            Intent intent = new Intent(MyProfileActivity.this, CreateRoutineActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void setImgBottomMenuIconListener() {
        imgBottomMenuIcon.setOnClickListener(v -> {
            Intent intent = new Intent(MyProfileActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private String getEmailWithSharedPreferences(SharedPreferences sharedPreferences) {
        String email = sharedPreferences.getString("email", "E-mail não salvo");
        return email.equals("E-mail não salvo") ? "" : email;
    }

    private void setTxtTitleMyProfile() {
        txtNameMyProfile.setText(user.getNome());
    }

    private void setBtnNameEditMyProfileListener() {
        btnNameEditMyProfile.setOnClickListener(v -> {
            String newName = edtTxtNameMyProfile.getText().toString();

            if (newName.isEmpty()) {
                Toast.makeText(this, "Preisa inserir um nome para alterá-lo", Toast.LENGTH_LONG).show();
            } else {
                boolean sucesso = dbHelper.updateUser(new User(user.getId(), newName, user.getEmail(), user.getSenha()));

                if (sucesso) {
                    setLog(1, this.getLocalClassName(), "Usuário atualizado");
                    Intent intent = new Intent(MyProfileActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void setLog(Integer idLogType, String className, String message) {
        switch (idLogType) {
            case 1:
                Log.d(className, message);
                break;
            case 2:
                Log.e(className, message);
                break;
            default:
                Log.e(className, "Só são permitidos 1 ou 2 como id para o log");
        }
    }

    private void setBtnPhotoEditMyProfileListener() {
        btnPhotoEditMyProfile.setOnClickListener(v -> verificarPermissaoEAbrirCamera());
    }

    private void inicializarLaunchersCamera() {
        // 1. O que acontece se o usuário aceitar a permissão da câmera?
        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                abrirCamera();
            } else {
                Toast.makeText(this, "Permissão da câmera é necessária para tirar fotos.", Toast.LENGTH_SHORT).show();
            }
        });

        // 2. O que acontece quando a foto for tirada?
        takePictureLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                Bundle extras = result.getData().getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");

                // Coloca a foto na tela
                imgMyProfile.setImageBitmap(imageBitmap);

                // Converte a foto para texto e salva no banco usando o e-mail do usuário!
                String fotoBase64 = converterBitmapParaBase64(imageBitmap);
                boolean salvou = dbHelper.salvarFotoPerfil(user.getEmail(), fotoBase64);

                if (salvou) {
                    Toast.makeText(this, "Foto de perfil salva com sucesso!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Erro ao salvar a foto no banco.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void verificarPermissaoEAbrirCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            abrirCamera(); // Se já tem permissão, abre direto
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA); // Se não tem, pede pro usuário
        }
    }

    private void abrirCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            takePictureLauncher.launch(takePictureIntent);
        }
    }

    private void carregarFotoSalva() {
        // Busca a foto em texto no banco usando o e-mail do usuário
        String fotoBase64 = dbHelper.getFotoPerfil(user.getEmail());

        if (fotoBase64 != null && !fotoBase64.isEmpty()) {
            // Se tiver foto, converte de volta para imagem e coloca no círculo
            Bitmap bitmap = converterBase64ParaBitmap(fotoBase64);
            imgMyProfile.setImageBitmap(bitmap);
        }
    }

    private String converterBitmapParaBase64(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] byteArray = outputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private Bitmap converterBase64ParaBitmap(String base64String) {
        byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    private void goBackToLoginWithLogout(SharedPreferences sharedPreferences) {
        Intent intent = new Intent(MyProfileActivity.this, MainActivity.class);
        sharedPreferences.edit().clear().apply();
        startActivity(intent);
        finish();
    }
}
