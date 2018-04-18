package com.example.davy.projetoic.Persistence;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import com.example.davy.projetoic.R;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by reida on 02/03/2018.
 */

public class ProvaService {

    public static final String url = /*"http://172.16.44.2:8080/webServiceIC/serv";*/"http://10.0.2.2:8080/webServiceIC/serv";

    public  static Prova getProva(String tipoProva, Context context) throws Exception {
        //String prova = readFile(param, context);
        //faz a requisição
        String prova = getJSONFromAPI(url, tipoProva);
        //constroi uma prova
        JSONObject p;

        p = new JSONObject(prova);

        if(!p.isNull("ERRO"))
            Toast.makeText(context, p.getString("ERRO"), Toast.LENGTH_SHORT);
        else {
            Prova pr = new Prova();
            pr.setName(p.getString("name"));
            pr.setNumQuest(p.getInt("numQuests"));
            JSONArray questsJsonArray = p.getJSONArray("quests");
            for (int i = 0; i < questsJsonArray.length(); i++) {
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
        return null;
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

    public static ArrayList getTipoProva()throws  Exception{
        String json = getJSONFromAPI(url, "listar");
        JSONObject jsonObject = new JSONObject(json);
        JSONArray tipos = jsonObject.getJSONArray("tipo");
        ArrayList list = new ArrayList();
        for(int i = 0; i < tipos.length(); i++){
            list.add(tipos.getString(i));
        }
        return list;
    }

    public static ArrayList getListaProvas(String tipoProva) throws Exception {
        String retorno = getListaProvasJson(url, tipoProva);
        JSONObject jsonObject = new JSONObject(retorno);
        return null;
    }

    public static String getListaProvasJson(String url, String tipoProva) throws Exception {
        String retorno = "";
        try {
            //objetos
            URL apiEnd = new URL(url);
            int codigoResposta;
            HttpURLConnection conexao;
            InputStream is;

            //coneção web
            conexao = (HttpURLConnection) apiEnd.openConnection();
            conexao.setRequestMethod("POST");
            conexao.addRequestProperty("tipo","listarProvas");
            conexao.addRequestProperty("tipoProva",tipoProva);
            conexao.setReadTimeout(15000);
            conexao.setConnectTimeout(15000);
            conexao.connect();

            //valida a resposta
            codigoResposta = conexao.getResponseCode();
            if(codigoResposta < HttpURLConnection.HTTP_BAD_REQUEST){
                is = conexao.getInputStream();
            }else{
                is = conexao.getErrorStream();
            }

            retorno = converterInputStreamToString(is);
            Log.i("Infome","INFORME ================ "+retorno);
            if(retorno.equals(""))
                throw new Exception("Erro na resposta do servidor");
            is.close();
            conexao.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

        return retorno;
    }


    //Realiza a requisição no servidor e espera o retorno do json em resposta
    public static String getJSONFromAPI(String url, String tipoReq) throws Exception{
        String retorno = "";
        try {
            //objetos
            URL apiEnd = new URL(url);
            int codigoResposta;
            HttpURLConnection conexao;
            InputStream is;

            //coneção web
            conexao = (HttpURLConnection) apiEnd.openConnection();
            conexao.setRequestMethod("POST");
            conexao.addRequestProperty("tipo",tipoReq);
            conexao.setReadTimeout(15000);
            conexao.setConnectTimeout(15000);
            //conexao.setRequestProperty();
            conexao.connect();

            //valida a resposta
            codigoResposta = conexao.getResponseCode();
            if(codigoResposta < HttpURLConnection.HTTP_BAD_REQUEST){
                is = conexao.getInputStream();
            }else{
                is = conexao.getErrorStream();
            }

            retorno = converterInputStreamToString(is);
            Log.i("Infome","INFORME ================ "+retorno);
            if(retorno.equals(""))
                throw new Exception("Erro na resposta do servidor");
            is.close();
            conexao.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

        return retorno;
    }

    //converte um input stream em string para processamento no app
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
