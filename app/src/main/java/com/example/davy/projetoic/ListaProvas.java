package com.example.davy.projetoic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.davy.projetoic.Adapters.ListaProvaAdapter;

import java.util.ArrayList;

public class ListaProvas extends AppCompatActivity {

    Button btnSelecionarProva;
    RecyclerView lista;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_provas);
        btnSelecionarProva = findViewById(R.id.btnSelecionarProva);
        lista = findViewById(R.id.listaProvas);

        //set adaper passando dados do bundle do intent
        ArrayList lista = getIntent().getStringArrayListExtra("lista");
        this.lista.setAdapter(new ListaProvaAdapter(lista,this));

        RecyclerView.LayoutManager layout = new LinearLayoutManager(this, LinearLayout.VERTICAL, false);
        this.lista.setLayoutManager(layout);
    }
}
