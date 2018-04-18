package com.example.davy.projetoic.Persistence;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.davy.projetoic.utils.AndroidUtils;

import java.util.List;

public class GetListaProvasTask extends AsyncTask<String, Void, List> {
    private Context context;

    public GetListaProvasTask(Context context) {
        this.context = context;
    }

    @Override
    protected List doInBackground(String... strings) {
        try {
            if (!AndroidUtils.isNeworkAvailble(context))
                Toast.makeText(context, "Não foi possivel conectar-se a rede", Toast.LENGTH_SHORT).show();
            List list;
            list = ProvaService.getListaProvas(strings[0]);
            if(list == null || list.size() == 0){
                onCancelled();
            }
            return list;
        }catch (Exception ex){
            ex.printStackTrace();
            Toast.makeText(context, "Erro desconhecio: "+ex.getMessage(), Toast.LENGTH_LONG).show();
        }
        return null;
    }

    @Override
    protected void onPostExecute(List list) {


    }
}
