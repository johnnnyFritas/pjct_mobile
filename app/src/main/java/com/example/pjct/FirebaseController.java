package com.example.pjct;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Map;

public class FirebaseController {

    protected FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    protected ArrayList<Map<String, Object>> buscarCliente(String email) {
        ArrayList<Map<String, Object>> listaResultados = new ArrayList<>();
        firebaseFirestore.collection("users")
                .whereEqualTo("email", email)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for(QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Map<String, Object> dados = doc.getData();
                        listaResultados.add(dados);
                    }
                });
        return listaResultados;
    }

    protected void registrarCliente(String nome, String email, String password) {

    }

}
