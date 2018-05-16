package com.example.davy.projetoic.Adapters;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.davy.projetoic.R;

import java.util.ArrayList;

public class GabaritoAdapter extends RecyclerView.Adapter {
    private String[] mAnswers;
    private ArrayList<String> mOptions;
    private Context mContext;

    public GabaritoAdapter(String[] answers, ArrayList<String> options, Context context) {
        this.mAnswers = answers;
        this.mOptions = options;
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.gabarito_row, parent, false);
        ViewHolderGabarito holder = new ViewHolderGabarito(view);
        return holder;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolderGabarito h = (ViewHolderGabarito) holder;
        h.mNameQuest.setText(String.valueOf(position+1));
        h.mOptionSelect.setText(mAnswers[position]);
        h.mAnswer.setText(mOptions.get(position));
        if(mAnswers[position].toLowerCase().equals(mOptions.get(position).toLowerCase()))
            h.mOptionSelect.setTextColor(mContext.getColor(R.color.approved));
        else
            h.mOptionSelect.setTextColor(mContext.getColor(R.color.error));
        try{
            YoYo.with(Techniques.RollIn)
                    .duration(1000)
                    .playOn(h.itemView);
        }catch(Exception e){
            Toast.makeText(mContext, "Ocorreu um erro na animação", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return mOptions.size();
    }

    protected class ViewHolderGabarito extends RecyclerView.ViewHolder{

        TextView mNameQuest;
        TextView mOptionSelect;
        TextView mAnswer;

        public ViewHolderGabarito(View itemView) {
            super(itemView);
            mNameQuest = itemView.findViewById(R.id.text_questao_gabarito);
            mOptionSelect = itemView.findViewById(R.id.text_questao_select);
            mAnswer = itemView.findViewById(R.id.text_questao_answer);
        }
    }
}
