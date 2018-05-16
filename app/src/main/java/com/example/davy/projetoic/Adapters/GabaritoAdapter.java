package com.example.davy.projetoic.Adapters;

import android.content.Context;
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
    String[] mAnswers;
    ArrayList<String> mOptions;
    Context mContext;

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

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolderGabarito h = (ViewHolderGabarito) holder;
        h.mNameQuest.setText(String.valueOf(position+1));
        h.mOptionSelect.setText(mAnswers[position]);
        h.mAnswer.setText(mOptions.get(position));
        try{
            YoYo.with(Techniques.FadeInDown)
                    .duration(700)
                    .playOn(h.itemView);
        }catch(Exception e){
            Toast.makeText(mContext, "Ocorreu um erro na animação", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return 0;
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
