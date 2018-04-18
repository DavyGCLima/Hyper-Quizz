package com.example.davy.projetoic.Persistence;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.davy.projetoic.HomeActivity;
import com.example.davy.projetoic.TiposProva;
import com.example.davy.projetoic.utils.AndroidUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Davy Lima on 11/04/2018.
 */

public class GetTipoProvaTask extends AsyncTask<Void, Void, ArrayList>{

    private HomeActivity context;
    private ProgressBar progressBar;

    public GetTipoProvaTask(HomeActivity context, ProgressBar progressBar) {
        this.context = context;
        this.progressBar = progressBar;
    }

    @Override
    protected void onPreExecute() {
        if(progressBar != null)
            exibirProgress(true);
        super.onPreExecute();
    }

    @Override
    protected ArrayList doInBackground(Void... voids) {
        publishProgress();
        try {
            if(!AndroidUtils.isNeworkAvailble(context))
                Toast.makeText(context, "Não foi possivel conectar-se a rede", Toast.LENGTH_SHORT).show();
            ArrayList list;
            list = ProvaService.getTipoProva();
            if(list == null || list.size() == 0){
              onCancelled();
            }
            publishProgress();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Erro desconhecio: "+e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return null;
    }

    @Override
    /**
     * Inicia a activity de tipo prova passando os parametros necessários
     * @param Recebe a lista contendo os tipos de prova do doInBackgroung
     */
    protected void onPostExecute(ArrayList list) {
        if(list != null && list.size() > 0){
            Intent it = new Intent(context, TiposProva.class);
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("lista", list);
            it.putExtras(bundle);
            context.startActivity(it);
        }
        super.onPostExecute(list);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        if (progressBar != null)
            progressBar.incrementProgressBy(50);
        super.onProgressUpdate(values);
    }


    @Override
    protected void onCancelled() {
        super.onCancelled();
        Toast.makeText(context, "Nenhuma prova encontrada", Toast.LENGTH_SHORT);
    }

    private void exibirProgress(boolean exibir) {
        progressBar.setVisibility(exibir ? View.VISIBLE : View.GONE);
    }
}
