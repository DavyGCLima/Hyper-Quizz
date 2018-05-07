package com.example.davy.projetoic.Persistence;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginService {
    private static final String url = "http://192.168.15.235:8080/webServiceIc/serv";
                                        //"http://192.168.15.192:8080/webServiceIc/serv";
                                        //"http://10.0.2.2:8080/webServiceIc/serv";
    private static final int readTimeOut = 15000;
    private static final int conectTimeOut = 15000;

    public static boolean valdiarAcesso(String email, String senha) throws Exception {
        String retorno = "";

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
        if(retorno.equals("true"))
            return true;
        if(retorno.equals(""))
            throw new Exception("Erro na resposta do servidor");
        is.close();
        conexao.disconnect();
        return false;
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
