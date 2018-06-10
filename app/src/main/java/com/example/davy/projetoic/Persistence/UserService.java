package com.example.davy.projetoic.Persistence;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.davy.projetoic.HomeActivity;
import com.example.davy.projetoic.R;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UserService {
    /**
     * URL de conexão com o servidor
     */
    private static final String url = //"http://172.16.44.3:8080/webServiceIc/serv";
                                        //"http://192.168.15.192:8080/webServiceIc/serv";
                                        "http://10.0.2.2:8080/webServiceIc/serv";
    private static final int readTimeOut = 15000;
    private static final int conectTimeOut = 15000;

    /**
     * Valida o acesso no servidor
     * @param email email do usuário
     * @param senha senha de acesso do usuário
     * @return retorna verdadeiro caso o acesso seja autorizado, falso se as credencias não possuem cadastro váido
     * @throws Exception retorna um conjunto de escessoões caso ocorra algum erro no processo de conexão
     */
    public static String valdiarAcesso(String email, String senha) throws Exception {
        String retorno;

        HttpURLConnection connection;

        //coneção web
        connection = prepareConection();
        connection.addRequestProperty("tipo","login");
        connection.addRequestProperty("email", email);
        connection.addRequestProperty("senha",senha);

        retorno = connect(connection);
        if(retorno.equals(""))
            throw new Exception("Erro na resposta do servidor");

        JSONObject json = new JSONObject(retorno);
        String token = json.getString("token");
        return token;

    }

    /**
     *
     * @param nome
     * @param email
     * @param senha
     * @return
     * @throws Exception
     */
    public static String cadastrar(String nome, String email, String senha) throws Exception {
        String retorno;

        //objetos
        URL apiEnd = new URL(url);
        int codigoResposta;
        HttpURLConnection conexao;
        InputStream is;

        //coneção web
        conexao = (HttpURLConnection) apiEnd.openConnection();
        conexao.setRequestMethod("POST");
        conexao.addRequestProperty("tipo","cadastro");
        conexao.addRequestProperty("senha",senha);
        conexao.addRequestProperty("email",email);
        conexao.addRequestProperty("nome",nome);
        conexao.setReadTimeout(readTimeOut);
        conexao.setConnectTimeout(conectTimeOut);
        conexao.connect();

        //valida a resposta
        codigoResposta = conexao.getResponseCode();
        if(codigoResposta < HttpURLConnection.HTTP_BAD_REQUEST){
            is = conexao.getInputStream();
        }else{
            is = conexao.getErrorStream();
        }

        retorno = converterInputStreamToString(is);
        Log.i("Infome","INFORME CADASTRO ================ "+retorno);
        is.close();
        conexao.disconnect();
        if(retorno.equals("Cadastrado"))
            return retorno;
        else if(retorno.equals(""))
            throw new Exception("Erro na resposta do servidor");
        return retorno;
    }

    private static HttpURLConnection prepareConection() throws IOException {
        //objetos
        URL apiEnd = new URL(url);
        HttpURLConnection conexao;
        //coneção web
        conexao = (HttpURLConnection) apiEnd.openConnection();
        conexao.setRequestMethod("POST");
        return conexao;
    }

    private static String connect(HttpURLConnection connection) throws IOException {
        String retorno;
        InputStream is;
        int codigoResposta;

        System.out.println("CONNECT");
        connection.setReadTimeout(readTimeOut);
        connection.setConnectTimeout(conectTimeOut);
        connection.connect();

        //valida a resposta
        codigoResposta = connection.getResponseCode();
        if(codigoResposta < HttpURLConnection.HTTP_BAD_REQUEST){
            is = connection.getInputStream();
        }else{
            is = connection.getErrorStream();
        }

        retorno = converterInputStreamToString(is);
        Log.i("Infome","INFORME ================ "+retorno);
        is.close();
        connection.disconnect();
        return retorno;
    }

    public static void openMainActivity(Activity context){
        Intent it = new Intent(context, HomeActivity.class);
        context.startActivity(it);
    }

    public static String saveDataAfterTest(int acertos, int erros, String email, Context context) throws IOException {
        HttpURLConnection con = prepareConection();
        con.addRequestProperty("tipo", "atualizarEstatisticasUsuario");
        con.addRequestProperty("acertos", String.valueOf(acertos));
        con.addRequestProperty("erros", String.valueOf(erros));
        con.addRequestProperty("email", email);
        String retorno = connect(con);
        switch (retorno) {
            case "ok":
                return context.getString(R.string.updateData);
            case "":
                return context.getString(R.string.errorResponseFromServer);
            default:
                return retorno;
        }
    }

    public static String[] getUserData(String userId, String token) throws IOException, JSONException {
        HttpURLConnection con = prepareConection();
        con.addRequestProperty("tipo", "getDadosUsuario");
        con.addRequestProperty("token", token);
        con.addRequestProperty("idUsuario", userId);
        String retorno = connect(con);
        String[] dados = new String[2];
        if(retorno != null && !retorno.equals("")){
            JSONObject json = new JSONObject(retorno);
            dados[0] = json.getString("acertos");
            dados[1] = json.getString("erros");
            return dados;

        }else
            throw new IOException("Erro");
    }

    /**
     * coverte um inputStream em uma string para ser processada
     * @param is imput a ser convertido
     * @return String representante do imput
     */
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
