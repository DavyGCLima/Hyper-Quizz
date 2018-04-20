package com.example.davy.projetoic.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.davy.projetoic.R;

import java.util.ArrayList;
import java.util.List;

public class ListaProvaAdapter extends RecyclerView.Adapter {

    private ArrayList<ArrayList<String>> lista;
    private Context context;

    public ListaProvaAdapter(ArrayList<ArrayList<String>> lista,Context context){
       this.lista = lista;
       this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_line_lista_provas, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder h = (ViewHolder) holder;
        h.nome.setText(lista.get(position).get(2));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder{

        //parametros da lista
        TextView nome;
        public ViewHolder(View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.textListaProva);
        }
    }
}
