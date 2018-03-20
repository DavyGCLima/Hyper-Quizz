package com.example.davy.projetoic.Persistence;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.davy.projetoic.HomeActivity;
import com.example.davy.projetoic.QuestoesActivity;

import org.json.JSONException;

import java.io.IOException;

public class GetProvaTask extends AsyncTask<Integer,Void,Prova> {

    private HomeActivity context;
    private ProgressBar progressBar;

    public GetProvaTask(HomeActivity context, ProgressBar progressBar) {
        this.context = context;
        this.progressBar = progressBar;
    }

    public GetProvaTask(HomeActivity context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        if(progressBar != null)
            exibirProgress(true);
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Prova prova) {
        if(prova != null) {
            Intent it = new Intent(context, QuestoesActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("Prova", prova);
            it.putExtras(bundle);
            context.startActivity(it);
            super.onPostExecute(prova);
        }else{
            Toast.makeText(context, "Erro ao efetuar o download", Toast.LENGTH_SHORT).show();
            //context.finish();
        }
        if (progressBar != null)
            exibirProgress(false);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        if (progressBar != null)
            progressBar.incrementProgressBy(50);
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled(Prova prova) {
        super.onCancelled(prova);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        Toast.makeText(context, "Erro ao efetuar o download", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected Prova doInBackground(Integer... integers) {
        publishProgress();
        try {
            publishProgress();
            Prova prova = ProvaService.getProva(integers[0], context);
            publishProgress();
            return prova;
        } catch (IOException e) {
            Log.e("Erro de conexão: ", e.getMessage());
            e.printStackTrace();
            return null;
        }catch (JSONException e){
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
        progressBar.setVisibility(exibir ? View.VISIBLE : View.GONE);
    }

}