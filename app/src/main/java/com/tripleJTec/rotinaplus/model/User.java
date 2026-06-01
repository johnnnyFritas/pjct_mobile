package com.tripleJTec.rotinaplus.model;

import android.content.Intent;
import android.content.SharedPreferences;

import com.tripleJTec.rotinaplus.R;
import com.tripleJTec.rotinaplus.ui.auth.MainActivity;
import com.tripleJTec.rotinaplus.ui.home.HomeActivity;

public class User {
    private String id;
    private String nome;
    private String email;
    private String senha;

    public User(String id, String nome, String email, String senha) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
