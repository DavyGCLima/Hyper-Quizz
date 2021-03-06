package com.example.davy.projetoic.Persistence.tasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.davy.projetoic.Persistence.DBService;
import com.example.davy.projetoic.Persistence.ProvaService;
import com.example.davy.projetoic.QuestoesActivity;
import com.example.davy.projetoic.utils.AndroidUtils;

public class GetImagemTask extends AsyncTask <String, Void, String>{

    private Context context;
    private ImageView imageView;
    private final String[] user;

    public GetImagemTask(Context context, ImageView imageView) {
        this.context = context;
        this.imageView = imageView;
        DBService db = new DBService(context);
        user = db.getUser();
    }

    @Override
    protected String doInBackground(String... strings) {
        if(!AndroidUtils.isNeworkAvailble(context))
            return null;
        try {
            String token = user[2];
            String imagemQuest = ProvaService.getImagem(strings[0], token);
            return imagemQuest;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(s != null){
            byte[] decode = Base64.decode(s, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);
            imageView.setImageBitmap(bitmap);
            imageView.setVisibility(View.VISIBLE);
        }else{
            Toast.makeText(context, "Não foi possivel efeturar o download da imagem", Toast.LENGTH_SHORT).show();
        }
    }
}
