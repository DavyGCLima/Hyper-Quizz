package com.example.davy.projetoic.Persistence.tasks;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.davy.projetoic.HomeActivity;
import com.example.davy.projetoic.Persistence.DBService;
import com.example.davy.projetoic.Persistence.ProvaService;
import com.example.davy.projetoic.R;
import com.example.davy.projetoic.TiposProva;
import com.example.davy.projetoic.utils.AndroidUtils;

import java.util.ArrayList;

/**
 * Created by Davy Lima on 11/04/2018.
 */

public class GetTipoProvaTask extends AsyncTask<Void , Void, ArrayList>{

    private HomeActivity mContext;
    private final ThreadLocal<ProgressBar> progressBar = new ThreadLocal<ProgressBar>();
    private String mToken;

    public GetTipoProvaTask(HomeActivity context, ProgressBar progressBar) {
        this.mContext = context;
        this.progressBar.set(progressBar);
    }

    @Override
    protected void onPreExecute() {
        if(progressBar.get() != null)
            exibirProgress(true);
        DBService db = new DBService(mContext);
        String[] user = db.getUser();
        mToken = user[2];
        if(!AndroidUtils.isNeworkAvailble(mContext))
            Toast.makeText(mContext, mContext.getString(R.string.errorResponseFromServer), Toast.LENGTH_SHORT).show();
        super.onPreExecute();
    }

    @Override
    protected ArrayList doInBackground(Void... voids) {
        publishProgress();
        try {
            ArrayList list;
            list = ProvaService.getTipoProva(mToken);
            publishProgress();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            onCancelled();
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
            Intent it = new Intent(mContext, TiposProva.class);
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("lista", list);
            it.putExtras(bundle);
            exibirProgress(false);
            mContext.startActivity(it);
        }else
            exibirProgress(false);
            Toast.makeText(mContext, "Não foi possivel recuperar os dados", Toast.LENGTH_SHORT).show();
        super.onPostExecute(list);
    }



    @Override
    protected void onProgressUpdate(Void... values) {
        if (progressBar.get() != null)
            progressBar.get().incrementProgressBy(50);
        super.onProgressUpdate(values);
    }


    private void exibirProgress(boolean exibir) {
        progressBar.get().setVisibility(exibir ? View.VISIBLE : View.GONE);
    }
}
