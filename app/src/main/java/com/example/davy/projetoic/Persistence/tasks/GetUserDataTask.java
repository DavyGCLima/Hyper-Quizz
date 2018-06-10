package com.example.davy.projetoic.Persistence.tasks;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.example.davy.projetoic.Persistence.UserService;
import com.example.davy.projetoic.R;
import com.example.davy.projetoic.utils.AlertDialogFragment;

import org.json.JSONException;

import java.io.IOException;

public class GetUserDataTask extends AsyncTask<String, String[], String[]> {

    private final ProgressBar mProgress;
    private Activity mContext;
    private WebView mWb;

    public GetUserDataTask(ProgressBar progress, Activity context, WebView wb) {
        mProgress = progress;
        mContext = context;
        mWb = wb;
    }

    @Override
    protected String[] doInBackground(String... strings) {
        try {
            return UserService.getUserData(strings[0], strings[1]);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            cancel(true);
            onCancelled();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        exibirProgress(true);
    }

    @Override
    protected void onPostExecute(String[] strings) {
        super.onPostExecute(strings);

        String url = "https://chart.googleapis.com/chart?" +
                "cht=p3&" + //define o tipo do gráfico "linha"
                "chtt=Acertos X Erros&"+ //define o titulo do grafico
                "chs=360x380&" + //define o tamanho da imagem
                "chd=t:"+strings[0]+","+strings[1]+"&" + //valor de cada coluna do gráfico
                "chds=a&"+
                "chdl=Acertos e Erros&" + //legenda do gráfico
                "chl=Acertos|Erros";

        WebSettings ws = mWb.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setSupportZoom(false);
        mWb.loadUrl(url);
        exibirProgress(false);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        exibirProgress(false);
        new AlertDialogFragment(mContext.getString(R.string.errorResponseFromServer)).show(mContext.getFragmentManager(), "msgErr");
    }

    private void exibirProgress(boolean exibir) {
        mProgress.setVisibility(exibir ? View.VISIBLE : View.GONE);
    }
}
