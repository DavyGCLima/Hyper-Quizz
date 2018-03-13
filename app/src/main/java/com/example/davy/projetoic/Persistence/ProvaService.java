package com.example.davy.projetoic.Persistence;

import android.content.Context;
import android.content.res.Resources;
import android.widget.Toast;

import com.example.davy.projetoic.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * Created by reida on 02/03/2018.
 */

public class ProvaService {
    public static final String PORVA = "prova.json";
    public static final int body = 0;
    public static final int answer = 6;
    public static final int img = 7;

    public  static Prova getProva(String param, Context context) throws IOException {
        String prova = readFile(param, context);
        JSONObject p;
        try {
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

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(context, R.string.erroCarregaJson, Toast.LENGTH_SHORT);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, R.string.erroCarregaJsonInesperado, Toast.LENGTH_SHORT);
            return null;
        }
    }

    public static ArrayList<ArrayList> getQuestoes(Prova prova) throws IOException {
        ArrayList<ArrayList> questsStr = new ArrayList<>();
        for(int i =0; i < prova.getNumQuest(); i++){
            questsStr.add(new ArrayList());
        }
        List<Prova.Questoes> quests = prova.getQuests();
        for(int i = 0; i < prova.getNumQuest(); i++){
            questsStr.get(i).set(1,quests.get(i).getOptionA());
            questsStr.get(i).set(2,quests.get(i).getOptionB());
            questsStr.get(i).set(3,quests.get(i).getOptionC());
            questsStr.get(i).set(4,quests.get(i).getOptionD());
            questsStr.get(i).set(5,quests.get(i).getOptionE());
        }
        return questsStr;
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


}
