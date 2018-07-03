package com.example.davy.projetoic.Persistence;

import android.content.Context;
import android.util.Log;
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
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


/**
 * Created by Davy Lima on 02/03/2018.
 */

public class ProvaService {

    private static final String url = //"http://172.16.44.3:8080/webServiceIc/serv";
                                        //"http://192.168.15.192:8080/webServiceIc/serv";
                                        "http://10.0.2.2:8080/webServiceIc/serv";
    private static final int readTimeOut = 15000;
    private static final int conectTimeOut = 15000;

    public  static Prova getProva(String tipoProva, String token, Context context) throws Exception {
        String prova = getJSONFromAPI("buscarProva", token, tipoProva);
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

    public static String getImagem(String imageId, String token) throws Exception {
        String json = getImagemQuest(imageId, token);
        JSONObject img = new JSONObject(json);
        if(img.has("ERRO"))
            return null;
        else
            return img.getString("img");
    }

    /*private static String readFile(String jsonPath, Context context) throws FileNotFoundException {
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
    }*/

    public static ArrayList<String> getTipoProva(String token, Context context)throws  Exception{
        String json = getJSONFromAPI("listar", token, null);
        if(isNewToken(json, context)) {
            DBService db = new DBService(context);
            final String[] user = db.getUser();
            String newToken = user[2];
            return getTipoProva(newToken, context);
        }else {
            JSONObject jsonObject = new JSONObject(json);
            if (jsonObject.has("ERRO")) {
                throw new Exception(jsonObject.getString("ERRO"));
            } else {
                JSONArray tipos = jsonObject.getJSONArray("tipo");
                ArrayList<String> list = new ArrayList<>();
                for (int i = 0; i < tipos.length(); i++) {
                    list.add(tipos.getString(i));
                }
                return list;
            }
        }
    }

    public static ArrayList getListaProvas(String tipoProva, String token) throws Exception {
        String retorno = getListaProvasJson(token, tipoProva);
        JSONObject jsonObject = new JSONObject(retorno);
        if(jsonObject.has("ERRO")){
            throw new Exception(jsonObject.getString("ERRO"));
        }else {
            JSONArray provas = jsonObject.getJSONArray("prova");
            ArrayList<ArrayList<String>> list = new ArrayList<>();
            for (int i = 0; i < provas.length(); i++) {
                ArrayList<String> linha = new ArrayList<>();
                JSONObject p = provas.getJSONObject(i);
                linha.add(p.getString("idProva"));
                linha.add(p.getString("idTipoProva"));
                linha.add(p.getString("nome"));
                linha.add(p.getString("qtdQuestoes"));
                list.add(linha);
            }
            return list;
        }
    }

    private static String getListaProvasJson( String token, String tipoProva) throws Exception {
        String retorno = "";
        try {
            //objetos
            HttpURLConnection conexao;

            //coneção web
            conexao = prepareConection();
            conexao.addRequestProperty("tipo","listarProvas");
            conexao.addRequestProperty("token", token);
            OutputStream out = conexao.getOutputStream();
            JSONObject json = new JSONObject();
            json.put("tipoProva",tipoProva);
            out.write(json.toString().getBytes("UTF-8"));
            retorno = connect(conexao);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

        return retorno;
    }

    private static String getImagemQuest(String imageId, String token) throws Exception{
        String retorno = "";
        try{
            //objetos
            HttpURLConnection conexao;

            //coneção web
            conexao = prepareConection();
            conexao.addRequestProperty("tipo","getImageQuest");
            conexao.addRequestProperty("token", token);
            OutputStream out = conexao.getOutputStream();
            JSONObject json = new JSONObject();
            json.put("imageId", imageId);
            out.write(json.toString().getBytes("UTF-8"));
            retorno = connect(conexao);

        } catch (IOException e){
            e.printStackTrace();
        }

        return retorno;
    }


    //Realiza a requisição no servidor e espera o retorno do json em resposta
    private static String getJSONFromAPI( String tipoReq, String token, String extParam) throws Exception{
        String retorno = "";
        try {
            //objetos
            HttpURLConnection conexao;

            //coneção web
            conexao = prepareConection();
            conexao.addRequestProperty("tipo",tipoReq);
            conexao.addRequestProperty("token", token);
            OutputStream out = conexao.getOutputStream();
            JSONObject json = new JSONObject();
            if(extParam != null) {
                json.put("tipoProva", extParam);
                out.write(json.toString().getBytes("UTF-8"));
            }
            retorno = connect(conexao);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

        return retorno;
    }

    private static boolean isNewToken(String token, Context context) {
        try {
            JSONObject jsonToken = new JSONObject(token);
            if(token.contains("newToken")) {
                String newToken = jsonToken.getString("newToken");
                DBService db = new DBService(context);
                final String[] user = db.getUser();
                db.update(user[DBService.EMAIL], newToken, user[DBService.NAME]);
                return true;
            }else
                return false;
        } catch (JSONException e) {
            return false;
        }
    }

    private static HttpURLConnection prepareConection() throws IOException {
        //objetos
        URL apiEnd = new URL(url);
        HttpURLConnection con;
        //coneção web
        con = (HttpURLConnection) apiEnd.openConnection();
        con.setRequestMethod("POST");
        con.setReadTimeout(readTimeOut);
        con.setConnectTimeout(conectTimeOut);
        return con;
    }

    private static String connect(HttpURLConnection connection) throws IOException {
        String retorno;
        InputStream is;
        int codigoResposta;

        connection.connect();

        //valida a resposta
        codigoResposta = connection.getResponseCode();
        if(codigoResposta < HttpURLConnection.HTTP_BAD_REQUEST){
            is = connection.getInputStream();
        }else{
            is = connection.getErrorStream();
        }

        retorno = converterInputStreamToString(is);
        Log.i("Infome","INFORME CADASTRO ================ "+retorno);
        is.close();
        connection.disconnect();
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
