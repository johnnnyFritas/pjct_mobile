package com.tripleJTec.rotinaplus.data.local;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.util.Log;

import com.tripleJTec.rotinaplus.domain.model.User;

public class dataBase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "RotinaPlusDB";
    // Mudamos a versão para 2. Isso avisa ao Android que a estrutura mudou
    // e aciona o método onUpgrade para recriar a tabela.
    private static final int DATABASE_VERSION = 2;

    public dataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Agora a tabela inclui o campo "nome"
        String createTableUsuarios = "CREATE TABLE usuarios (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nome TEXT NOT NULL, " +
                "email TEXT UNIQUE NOT NULL, " +
                "senha TEXT NOT NULL)";

        db.execSQL(createTableUsuarios);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS usuarios");
        onCreate(db);
    }

    // Atualizamos o método para receber os três parâmetros
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

    public void getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();

        // Puxa todos os registros da tabela usuarios
        Cursor cursor = db.rawQuery("SELECT * FROM usuarios", null);

        Log.d("DADOS_DO_BANCO", "===== INÍCIO DA TABELA DE USUÁRIOS =====");

        // Verifica se existe pelo menos um registro
        if (cursor.moveToFirst()) {
            do {
                // Pega o valor de cada coluna da linha atual
                int indiceId = cursor.getColumnIndex("id");
                int indiceNome = cursor.getColumnIndex("nome");
                int indiceEmail = cursor.getColumnIndex("email");
                int indiceSenha = cursor.getColumnIndex("senha");

                String id = cursor.getString(indiceId);
                String nome = cursor.getString(indiceNome);
                String email = cursor.getString(indiceEmail);
                String senha = cursor.getString(indiceSenha);

                // Imprime a linha no Logcat
                Log.d("DADOS_DO_BANCO", "ID: " + id + " | Nome: " + nome + " | E-mail: " + email + " | Senha: " + senha);

            } while (cursor.moveToNext()); // Vai para a próxima linha até acabar
        } else {
            Log.d("DADOS_DO_BANCO", "A tabela de usuários está vazia.");
        }

        Log.d("DADOS_DO_BANCO", "===== FIM DA TABELA =====");
        cursor.close();
    }
}
