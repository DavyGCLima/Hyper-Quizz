package com.example.davy.projetoic.Persistence;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.example.davy.projetoic.HomeActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class LoginService {
    /**
     * URL de conexão com o servidor
     */
    private static final String url = "http://192.168.15.235:8080/webServiceIc/serv";
                                        //"http://192.168.15.192:8080/webServiceIc/serv";
                                        //"http://10.0.2.2:8080/webServiceIc/serv";
    private static final int readTimeOut = 15000;
    private static final int conectTimeOut = 15000;

    /**
     * Valida o acesso no servidor
     * @param email email do usuário
     * @param senha senha de acesso do usuário
     * @return retorna verdadeiro caso o acesso seja autorizado, falso se as credencias não possuem cadastro váido
     * @throws Exception retorna um conjunto de escessoões caso ocorra algum erro no processo de conexão
     */
    public static boolean valdiarAcesso(String email, String senha) throws Exception {
        String retorno;

        //objetos
        URL apiEnd = new URL(url);
        int codigoResposta;
        HttpURLConnection conexao;
        InputStream is;

        //coneção web
        conexao = (HttpURLConnection) apiEnd.openConnection();
        conexao.setRequestMethod("POST");
        conexao.addRequestProperty("tipo","login");
        conexao.addRequestProperty("email", email);
        conexao.addRequestProperty("senha",senha);
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
        Log.i("Infome","INFORME ================ "+retorno);
        is.close();
        conexao.disconnect();
        if(retorno.equals("true"))
            return true;
        if(retorno.equals(""))
            throw new Exception("Erro na resposta do servidor");
        return false;
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
        Log.i("Infome","INFORME ================ "+retorno);
        is.close();
        conexao.disconnect();
        if(retorno.equals("cadastrado"))
            return "1";
        if(retorno.equals(""))
            throw new Exception("Erro na resposta do servidor");
        return retorno;
    }

    public static void logar(Activity context){
        Intent it = new Intent(context, HomeActivity.class);
        context.startActivity(it);
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
