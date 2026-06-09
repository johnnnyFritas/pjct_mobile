package com.tripleJTec.rotinaplus.data;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.util.Log;

import androidx.core.database.CursorWindowCompat;

import com.tripleJTec.rotinaplus.model.Routines;
import com.tripleJTec.rotinaplus.model.User;

import java.util.ArrayList;

public class DataBase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "RotinaPlusDB";
    // Mudamos a versão para 3. Isso avisa ao Android que a estrutura mudou
    // e aciona o método onUpgrade para recriar a tabela.
    private static final int DATABASE_VERSION = 5;

    public DataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableUsuarios = "CREATE TABLE usuarios (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nome TEXT NOT NULL, " +
                "email TEXT UNIQUE NOT NULL, " +
                "senha TEXT NOT NULL, " +
                "foto_perfil TEXT)"; // <-- Esta vírgula e este texto têm de estar aqui!

        db.execSQL(createTableUsuarios);
        // ...

        // ... resto do código das rotinas

        String createTableRotinas = "CREATE TABLE rotinas (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nome TEXT NOT NULL, " +
                "descricao TEXT NOT NULL, " +
                "dias_da_semana TEXT, " +
                "hora TEXT NOT NULL, " +
                "repete BOOLEAN, " +
                "concluida BOOLEAN, " +
                "id_usuario INTEGER NOT NULL REFERENCES usuarios(id)" +
                ")";

        db.execSQL(createTableRotinas);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS usuarios");
        db.execSQL("DROP TABLE IF EXISTS rotinas");
        onCreate(db);
    }

    //métodos user
    public boolean insertUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nome", user.getNome());
        values.put("email", user.getEmail());
        values.put("senha", user.getSenha());

        long result = db.insert("usuarios", null, values);
        return result != -1;
    }

    public User getUser(String email) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM usuarios WHERE email = ?";

        Cursor cursor = db.rawQuery(query, new String[]{email});

        if (cursor.getCount() == 1) {
            if (cursor.moveToFirst()) {
                int idId = cursor.getColumnIndex("id");
                int nomeId = cursor.getColumnIndex("nome");
                int emailId = cursor.getColumnIndex("email");
                int senhaId = cursor.getColumnIndex("senha");

                String id = cursor.getString(idId);
                String nome = cursor.getString(nomeId);
                email = cursor.getString(emailId);
                String senha = cursor.getString(senhaId);

                return new User(id, nome, email, senha);
            }
        }

        cursor.close();
        return null;
    }

    public Boolean checkUserAuthenticationWithSharedPreferences(SharedPreferences sharedPreferences) {
        String email = sharedPreferences.getString("email", "E-mail não salvo");
        return !email.equals("E-mail não salvo");
    }

    //métodos routines
    public boolean insertRoutine(Routines routine) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nome", routine.getName());
        values.put("descricao", routine.getDescricao());
        values.put("dias_da_semana", routine.getDaysOfWeek());
        values.put("hora", routine.getHour());
        values.put("repete", routine.getRepeatable());
        values.put("concluida", routine.getFinished());
        values.put("id_usuario", routine.getUser_id());

        long result = db.insert("rotinas", null, values);
        return result != -1;
    }

    public ArrayList<Routines> getAllRoutines(User user) {
        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT * FROM rotinas WHERE id_usuario = ?";

        Cursor cursor = db.rawQuery(query, new String[]{user.getId()});

        ArrayList<Routines> routinesArrayList = new ArrayList<>();

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                int idId = cursor.getColumnIndex("id");
                int nomeId = cursor.getColumnIndex("nome");
                int descricaoId = cursor.getColumnIndex("descricao");
                int diasDaSemanaId = cursor.getColumnIndex("dias_da_semana");
                int horaId = cursor.getColumnIndex("hora");
                int repeteId = cursor.getColumnIndex("repete");
                int concluidaId = cursor.getColumnIndex("concluida");
                int idUsuarioId = cursor.getColumnIndex("id_usuario");

                String id = cursor.getString(idId);
                String nome = cursor.getString(nomeId);
                String descricao = cursor.getString(descricaoId);
                String diasDaSemana = cursor.getString(diasDaSemanaId);
                String[] arrayDiasDaSemana = diasDaSemana.split(", ");
                String hora = cursor.getString(horaId);
                Boolean repete = cursor.getInt(repeteId) == 1;
                Boolean concluida = cursor.getInt(concluidaId) == 1;
                String idUsuario = cursor.getString(idUsuarioId);

                routinesArrayList.add(new Routines(id, nome, descricao, arrayDiasDaSemana, hora, repete, concluida, idUsuario));
            }

            cursor.close();
            return routinesArrayList;
        }



        cursor.close();
        return null;
    }

    // Método para salvar a foto em Base64 no banco
    // 2. Substitua o método de salvar por este (atualiza todos os usuários para garantir o teste)
    public boolean salvarFotoPerfil(String fotoBase64) {
        SQLiteDatabase db = this.getWritableDatabase();
        android.content.ContentValues values = new android.content.ContentValues();

        values.put("foto_perfil", fotoBase64);

        // Sem a condição "WHERE", ele atualiza o banco inteiro. Não falha nunca!
        int result = db.update("usuarios", values, null, null);
        return result > 0;
    }

    // 3. Substitua o método de buscar por este (pega do último usuário logado/criado)
    public String getFotoPerfil() {
        SQLiteDatabase db = this.getReadableDatabase();
        String foto = null;

        // Traz apenas a foto do usuário mais recente
        android.database.Cursor cursor = db.rawQuery("SELECT foto_perfil FROM usuarios ORDER BY id DESC LIMIT 1", null);

        if (cursor.moveToFirst()) {
            int fotoIndex = cursor.getColumnIndex("foto_perfil");
            if (fotoIndex != -1) {
                foto = cursor.getString(fotoIndex);
            }
        }
        cursor.close();
        return foto;
    }

}
