package com.example.davy.projetoic;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.davy.projetoic.Adapters.GabaritoAdapter;

public class FinalizaProva extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finaliza_prova);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getIntent().getStringExtra("title"));
        setSupportActionBar(toolbar);

        TextView acertos = findViewById(R.id.text_fim_prova_acertos);
        acertos.setText(new StringBuilder().append(
                getString(R.string.acertos_fim_prova)).append(": ").append(
                        getIntent().getStringExtra("acertos")).toString());
        acertos.setTextColor(getColor(R.color.approved));
        TextView erros = findViewById(R.id.text_fim_prova_erros);
        erros.setTextColor(getColor(R.color.error));
        erros.setText(new StringBuilder().append(
                getString(R.string.erros_fim_prova)).append(": ").append(
                        getIntent().getStringExtra("erros")).toString());

        mRecyclerView = findViewById(R.id.gabarito_fim_prova);
        GabaritoAdapter gabaritoAdapter = new GabaritoAdapter(getIntent().getStringArrayExtra("answers")
                , getIntent().getStringArrayListExtra("options"), this);
        mRecyclerView.setAdapter(gabaritoAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        );
        RecyclerView.LayoutManager layout = new LinearLayoutManager(this, LinearLayout.VERTICAL, false);
        mRecyclerView.setLayoutManager(layout);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }
}