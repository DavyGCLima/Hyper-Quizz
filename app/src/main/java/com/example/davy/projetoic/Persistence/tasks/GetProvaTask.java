package com.example.davy.projetoic.Persistence.tasks;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.davy.projetoic.ListaProvas;
import com.example.davy.projetoic.Persistence.DBService;
import com.example.davy.projetoic.Persistence.Prova;
import com.example.davy.projetoic.Persistence.ProvaService;
import com.example.davy.projetoic.QuestoesActivity;
import com.example.davy.projetoic.utils.AndroidUtils;

import org.json.JSONException;

import java.io.IOException;

public class GetProvaTask extends AsyncTask<String,Void,Prova> {

    private ListaProvas mContext;
    private final ThreadLocal<ProgressBar> mProgressBar = new ThreadLocal<ProgressBar>();
    private String mToken;

    public GetProvaTask(ListaProvas context, ProgressBar progressBar) {
        this.mContext = context;
        this.mProgressBar.set(progressBar);
    }

    @Override
    protected void onPreExecute() {
        if(mProgressBar.get() != null)
            exibirProgress(true);
        DBService db = new DBService(mContext);
        final String[] user = db.getUser();
        mToken = user[2];
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Prova prova) {
        if(prova != null) {
            Intent it = new Intent(mContext, QuestoesActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("Prova", prova);
            it.putExtras(bundle);
            try{
                mContext.startActivity(it);
            }catch(Exception e){
                e.printStackTrace();
            }
            super.onPostExecute(prova);
        }else{
            Toast.makeText(mContext, "Erro ao efetuar o download", Toast.LENGTH_SHORT).show();
            //mContext.finish();
        }
        if (mProgressBar.get() != null)
            exibirProgress(false);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        if (mProgressBar.get() != null)
            mProgressBar.get().incrementProgressBy(50);
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        Toast.makeText(mContext, "Erro ao efetuar o download", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected Prova doInBackground(String... entrada) {
        publishProgress();
        try {
            if(!AndroidUtils.isNeworkAvailble(mContext))
                return null;
            publishProgress();
            Prova prova = ProvaService.getProva(entrada[0], mToken, mContext);
            if(prova.equals(null))
                onCancelled();
            publishProgress();
            return prova;
        } catch (java.net.ConnectException e){
            Toast.makeText(mContext,"Erro ao conectar",Toast.LENGTH_LONG);
            return null;
        } catch (IOException e) {
            Log.e("Erro de conexão: ", e.getMessage());
            e.printStackTrace();
            return null;
        }catch (JSONException e) {
            e.printStackTrace();
            Log.e("Erro conv JSON: ", e.getMessage());
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Erro de conexão inesp: ", e.getMessage());
            return null;
        }
    }
    private void exibirProgress(boolean exibir) {
        mProgressBar.get().setVisibility(exibir ? View.VISIBLE : View.GONE);
    }

}