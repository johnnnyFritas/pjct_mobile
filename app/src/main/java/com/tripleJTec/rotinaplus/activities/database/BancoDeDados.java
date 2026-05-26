package com.tripleJTec.rotinaplus.activities.database;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.util.Log;
public class BancoDeDados extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "RotinaPlusDB";
    // Mudamos a versão para 2. Isso avisa ao Android que a estrutura mudou
    // e aciona o método onUpgrade para recriar a tabela.
    private static final int DATABASE_VERSION = 2;

    public BancoDeDados(Context context) {
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
    public boolean inserirUsuario(String nome, String email, String senha) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nome", nome);
        values.put("email", email);
        values.put("senha", senha);

        long resultado = db.insert("usuarios", null, values);
        return resultado != -1;
    }

    public boolean checarLogon(String email, String senha) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Consulta para verificar se existe o e-mail e a senha informados
        String query = "SELECT * FROM usuarios WHERE email = ? AND senha = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email, senha});

        // Se o cursor encontrar pelo menos uma linha, significa que as credenciais estão corretas
        boolean existe = cursor.getCount() > 0;

        cursor.close(); // Sempre feche o cursor para evitar vazamento de memória
        return existe;
    }

    public void imprimirTodosOsUsuarios() {
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
