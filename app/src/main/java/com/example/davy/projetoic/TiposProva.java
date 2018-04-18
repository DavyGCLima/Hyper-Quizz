package com.example.davy.projetoic;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.davy.projetoic.Persistence.GetListaProvasTask;

import java.util.ArrayList;

public class TiposProva extends AppCompatActivity {

    RadioGroup grupo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipos_prova);

        grupo = findViewById(R.id.radioGroupTipoProva);//new RadioGroup(this);
        ArrayList<String> lista = getIntent().getStringArrayListExtra("lista");
       for(int i = 0; i < lista.size(); i++) {
           RadioButton rbtn = new RadioButton(this);
           rbtn.setText(lista.get(i));
           grupo.addView(rbtn);
       }
        Button btnEscolher = findViewById(R.id.btnTipoProvaOK);
        final Context con = this;
        btnEscolher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (grupo.getCheckedRadioButtonId() == -1)
                    Toast.makeText(getBaseContext(), "Selecione um tipo de prova", Toast.LENGTH_SHORT).show();
                else {
                    RadioButton btn = grupo.findViewById(grupo.getCheckedRadioButtonId());
                    new GetListaProvasTask(con).execute((String) btn.getText());
                }
            }
        });

    }

}
