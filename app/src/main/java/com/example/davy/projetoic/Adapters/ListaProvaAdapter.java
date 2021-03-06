package com.example.davy.projetoic.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.davy.projetoic.Interfaces.RecyclerViewClickListener;
import com.example.davy.projetoic.ListaProvas;
import com.example.davy.projetoic.R;

import java.util.ArrayList;

public class ListaProvaAdapter extends RecyclerView.Adapter {

    private ArrayList<String> lista;
    private ListaProvas context;
    private RecyclerViewClickListener mRecyclerViewClickListener;

    /*public ListaProvaAdapter(ArrayList<ArrayList<String>> lista, ListaProvas context){
       this.lista = lista;
       this.context = context;
    }*/
    public ListaProvaAdapter(ArrayList<String> lista, ListaProvas context){
        this.lista = lista;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_line_lista_provas, parent, false);
        ViewHolderProvaAdapter holder = new ViewHolderProvaAdapter(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolderProvaAdapter h = (ViewHolderProvaAdapter) holder;
        //h.nome.setText(lista.get(position).get(2));
        h.nome.setText(lista.get(position));
        try{
            YoYo.with(Techniques.FadeInDown)
                    .duration(500)
                    .playOn(h.itemView);
        }catch(Exception e){
            Toast.makeText(context, "Ocorreu um erro na animação", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public void setRecyclerViewClickListener(RecyclerViewClickListener listener){
        mRecyclerViewClickListener = listener;
    }

    protected class ViewHolderProvaAdapter extends RecyclerView.ViewHolder implements View.OnClickListener{

        //parametros da lista
        TextView nome;
        public ViewHolderProvaAdapter(View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.textListaProva);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mRecyclerViewClickListener != null){
                mRecyclerViewClickListener.onClickListener(v, getAdapterPosition());
                YoYo.with(Techniques.FadeIn).duration(500).playOn(v);

            }
        }
    }
}
