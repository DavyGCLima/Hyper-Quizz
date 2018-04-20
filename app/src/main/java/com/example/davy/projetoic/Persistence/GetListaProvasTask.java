package com.example.davy.projetoic.Persistence;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.davy.projetoic.ListaProvas;
import com.example.davy.projetoic.utils.AndroidUtils;

import java.util.ArrayList;

public class GetListaProvasTask extends AsyncTask<String, Void, ArrayList> {

    private Context context;
    private ProgressBar progressBar;

    public GetListaProvasTask(Context context, ProgressBar progressBar) {
        this.context = context;
        this.progressBar = progressBar;
    }

    @Override
    protected void onPreExecute() {
        if(progressBar != null)
            exibirProgress(true);
        if (!AndroidUtils.isNeworkAvailble(context))
            Toast.makeText(context, "Não foi possivel conectar-se a rede", Toast.LENGTH_SHORT).show();
        super.onPreExecute();
    }

    @Override
    protected ArrayList doInBackground(String... strings) {
        try {
            ArrayList list;
            list = ProvaService.getListaProvas(strings[0]);
            return list;
        }catch (Exception ex){
            ex.printStackTrace();
            onCancelled();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        if (progressBar != null)
            progressBar.incrementProgressBy(50);
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(ArrayList list) {
        if(list == null)
            Toast.makeText(context, "Não foi possivel recuperar provas no servidor",Toast.LENGTH_SHORT).show();
        else if(list.size() == 0)
            Toast.makeText(context, "Não há provas para ste tipo no servidor",Toast.LENGTH_SHORT).show();
        else{
            Intent it = new Intent(context, ListaProvas.class);
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("lista", list);
            it.putExtras(bundle);
            context.startActivity(it);
        }
    }

    @Override
    protected void onCancelled() {
        Toast.makeText(context, "Erro desconhecido", Toast.LENGTH_LONG).show();
        super.onCancelled();
    }

    private void exibirProgress(boolean exibir) {
        progressBar.setVisibility(exibir ? View.VISIBLE : View.GONE);
    }
}
