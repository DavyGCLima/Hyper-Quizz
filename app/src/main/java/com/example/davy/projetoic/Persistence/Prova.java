package com.example.davy.projetoic.Persistence;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Prova implements Serializable{
    int numQuest;
    ArrayList<Questoes> quests;
    String name;

    public Prova (){
        quests =  new ArrayList<Questoes>();
    }

    public int getNumQuest() {
        return numQuest;
    }

    public ArrayList<Questoes> getQuests() {
        return quests;
    }

    public Questoes getQuestao(int position){
        return quests.get(position);
    }

    public String getName() {
        return name;
    }

    public void setNumQuest(int numQuest) {
        this.numQuest = numQuest;
    }

    public void setQuests(ArrayList<Questoes> quests) {
        this.quests = quests;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static class Questoes implements Serializable {
        String body;
        String optionA;
        String optionB;
        String optionC;
        String optionD;
        String optionE;
        String answer;
        byte[] image;

        public Questoes() {

        }

        public String getBody() {
            return body;
        }

        public String getOptionA() {
            return optionA;
        }

        public String getOptionB() {
            return optionB;
        }

        public String getOptionC() {
            return optionC;
        }

        public String getOptionD() {
            return optionD;
        }

        public String getOptionE() {
            return optionE;
        }

        public String getAnswer() {
            return answer;
        }

        public byte[] getImage() {
            if (image != null) {
                //ByteArrayOutputStream out = new ByteArrayOutputStream();
                //image.compress(Bitmap.CompressFormat.JPEG, 100, out);
                //byte[] img = out.toByteArray();
                //return img;
                return image;
            }else
                return null;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public void setOptionA(String optionA) {
            this.optionA = optionA;
        }

        public void setOptionB(String optionB) {
            this.optionB = optionB;
        }

        public void setOptionC(String optionC) {
            this.optionC = optionC;
        }

        public void setOptionD(String optionD) {
            this.optionD = optionD;
        }

        public void setOptionE(String optionE) {
            this.optionE = optionE;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        public void setImage(String image) throws Exception {
                if(image.equals(""))
                    this.image = null;
                byte[] decode = Base64.decode(image, Base64.DEFAULT);
                //Bitmap bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);
            //this.image = bitmap;
            this.image = decode;

        }
    }
}