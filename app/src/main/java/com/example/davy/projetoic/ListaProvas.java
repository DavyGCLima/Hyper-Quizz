package com.example.davy.projetoic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.davy.projetoic.Adapters.ListaProvaAdapter;
import com.example.davy.projetoic.Interfaces.RecyclerViewClickListener;
import com.example.davy.projetoic.Persistence.tasks.GetProvaTask;

import java.util.ArrayList;

public class ListaProvas extends AppCompatActivity implements RecyclerViewClickListener {

    RecyclerView recyclerView;
    ProgressBar progressBar;
    ArrayList<String> lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_provas);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getString(R.string.titleListaProvas));

        recyclerView = findViewById(R.id.listaProvas);

        //set adaper passando dados do bundle do intent
        //lista = (ArrayList)getIntent().getStringArrayListExtra("lista");
        populateList();
        ListaProvaAdapter adapter = new ListaProvaAdapter(lista,this);
        adapter.setRecyclerViewClickListener(this);

        progressBar = findViewById(R.id.progressListProvas);

        this.recyclerView.setAdapter(adapter);
        this.recyclerView.setItemAnimator(new DefaultItemAnimator());
        this.recyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        );
        RecyclerView.LayoutManager layout = new LinearLayoutManager(this, LinearLayout.VERTICAL, false);
        this.recyclerView.setLayoutManager(layout);
    }

    private void populateList() {
        lista = new ArrayList<>();
        if(getIntent().getStringExtra("prova").equals("ENAD")){
            loadListENAD();
        }else{
            loadListENEM();
        }
    }

    private void loadListENAD(){
        for(String s : getResources().getStringArray(R.array.listENAD)){
            lista.add(s);
        }
    }

    private void loadListENEM(){
        for(String s : getResources().getStringArray(R.array.listENEM)){
            lista.add(s);
        }
    }

    @Override
    public void onClickListener(View view, int position) {
        //ação de buscar a prova
        String s = lista.get(position);
        new GetProvaTask(this, progressBar).execute(s);
        //Toast.makeText(this, "Selecionado: "+position, Toast.LENGTH_SHORT).show();
    }

}
