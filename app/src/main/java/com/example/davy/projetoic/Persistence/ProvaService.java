package com.example.davy.projetoic.Persistence;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import com.example.davy.projetoic.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by reida on 02/03/2018.
 */

public class ProvaService {
    public static final String PORVA = "prova.json";
    public static final String url = "http://webservice.com.br/servico/{tipo}";

    public  static Prova getProva(int tipoProva, Context context) throws Exception {
        //String prova = readFile(param, context);
        String tipo = getTipo(tipoProva);
        //montar a url
        url.replace("{tipo}",tipo);
        //faz a requisição
        String prova = getJSONFromAPI(url);
        //constroi uma prova
        JSONObject p;

        p = new JSONObject(prova);
        Prova pr = new Prova();
        pr.setName(p.getString("name"));
        pr.setNumQuest(p.getInt("numQuests"));
        JSONArray questsJsonArray = p.getJSONArray("quests");
        for(int i = 0; i < questsJsonArray.length(); i++){
            JSONObject q = questsJsonArray.getJSONObject(i);
            Prova.Questoes questao = new Prova.Questoes();
            questao.setBody(q.optString("body"));
            questao.setOptionA(q.optString("optionA"));
            questao.setOptionB(q.optString("optionB"));
            questao.setOptionC(q.optString("optionC"));
            questao.setOptionD(q.optString("optionD"));
            questao.setOptionE(q.optString("optionE"));
            questao.setAnswer(q.optString("answer"));
            questao.setImage(q.optString("image"));
            pr.quests.add(questao);
        }
        return pr;
    }

    private static String readFile(String jsonPath, Context context) throws FileNotFoundException {
        InputStream inputStream = context.getResources().openRawResource(R.raw.prova);
        String prova = "";
        try {
            // json is UTF-8 by default
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
            prova = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context,"Erro: "+e.getMessage(),Toast.LENGTH_SHORT);
            // Oops
        }
        finally {
            try{
                if(inputStream != null)inputStream.close();
            }catch(Exception squish){}
        }

        return prova;
    }


    private static String getTipo(int tipoProva){
        if(tipoProva == R.string.enad)
            return "enad";
        else
            return "enem";
    }

    public static String getJSONFromAPI(String url){
        String retorno = "";
        try {
            URL apiEnd = new URL(url);
            int codigoResposta;
            HttpURLConnection conexao;
            InputStream is;

            conexao = (HttpURLConnection) apiEnd.openConnection();
            conexao.setRequestMethod("GET");
            conexao.setReadTimeout(15000);
            conexao.setConnectTimeout(15000);
            conexao.connect();

            codigoResposta = conexao.getResponseCode();
            if(codigoResposta < HttpURLConnection.HTTP_BAD_REQUEST){
                is = conexao.getInputStream();
            }else{
                is = conexao.getErrorStream();
            }

            retorno = converterInputStreamToString(is);
            is.close();
            conexao.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

        return retorno;
    }

    private static String converterInputStreamToString(InputStream is){
        StringBuffer buffer = new StringBuffer();
        try{
            BufferedReader br;
            String linha;

            br = new BufferedReader(new InputStreamReader(is));
            while((linha = br.readLine())!=null){
                buffer.append(linha);
            }

            br.close();
        }catch(IOException e){
            e.printStackTrace();
        }

        return buffer.toString();
    }


}
