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
    private static final int DATABASE_VERSION = 3;

    public DataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableUsuarios = "CREATE TABLE usuarios (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nome TEXT NOT NULL, " +
                "email TEXT UNIQUE NOT NULL, " +
                "senha TEXT NOT NULL)";

        db.execSQL(createTableUsuarios);

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

    public boolean updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        String whereClause = "email = ?";
        String[] whereArgs = new String[]{
                user.getEmail()
        };

        ContentValues values = new ContentValues();
        values.put("nome", user.getNome());

        long result = db.update("usuarios", values, whereClause, whereArgs);
        return result != -1;
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

    public Routines getRoutine(String name, String hour, String[] daysOfWeek) {
        SQLiteDatabase db = this.getReadableDatabase();

        String daysFormatted = String.join(", ", daysOfWeek);

        String query = "SELECT * FROM rotinas WHERE nome = ? AND dias_da_semana = ? AND hora = ?";

        Cursor cursor = db.rawQuery(query, new String[]{name, daysFormatted, hour});

        if (cursor.getCount() == 1) {
            if (cursor.moveToFirst()) {
                int idId = cursor.getColumnIndex("id");
                int nomeId = cursor.getColumnIndex("nome");
                int descricaoId = cursor.getColumnIndex("descricao");
                int horaId = cursor.getColumnIndex("hora");
                int repeteId = cursor.getColumnIndex("repete");
                int concluidaId = cursor.getColumnIndex("concluida");
                int idUsuarioId = cursor.getColumnIndex("id_usuario");

                String id = cursor.getString(idId);
                String nome = cursor.getString(nomeId);
                String descriao = cursor.getString(descricaoId);
                String hora = cursor.getString(horaId);
                Boolean repete = cursor.getInt(repeteId) == 1;
                Boolean concluida = cursor.getInt(concluidaId) == 1;
                String idUsuario = cursor.getString(idUsuarioId);

                return new Routines(id, nome, descriao, daysOfWeek, hora, repete, concluida, idUsuario);
            }
        }

        cursor.close();
        return null;
    }
}
