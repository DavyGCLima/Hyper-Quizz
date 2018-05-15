package com.example.davy.projetoic;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.example.davy.projetoic.Persistence.LoginService;
import com.example.davy.projetoic.utils.AndroidUtils;

public class Cadastro extends AppCompatActivity {

    private AutoCompleteTextView mNome;
    private Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        extras = getIntent().getExtras();
        mNome = findViewById(R.id.nomeCadastro);
        //TextView TextView = findViewById(R.id.textoAuxiliar);
        final Button btnCadastro = findViewById(R.id.btnCadastro);
        btnCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrar(mNome.getText().toString(), extras.getString("email"), extras.getString("senha"));
            }
        });

    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        finish();
    }

    private void cadastrar(String nome, String email, String senha) {
        View focusView = null;
        mNome.setError(null);
        if (isValidName(nome)) {
            CadastroTask mCadastroTask = new CadastroTask(email, senha, nome);
            mCadastroTask.execute();
        }else{
            focusView = mNome;
            mNome.setError(getString(R.string.error_invalid_name));
            focusView.requestFocus();
        }
    }

    private boolean isValidName(String nome) {
        return nome.length() > 4;
    }

    public class CadastroTask extends AsyncTask<Void, Void, String> {

        private final String mEmail;
        private final String mPassword;
        private final String mNome;

        CadastroTask(String email, String senha, String nome) {
            mEmail = email;
            mPassword = senha;
            mNome = nome;
        }

        @Override
        protected void onPreExecute() {
            if(!AndroidUtils.isNeworkAvailble(Cadastro.this))
                new AlertDialogFragment(getString(R.string.semServicoRede)).show(Cadastro.this.getFragmentManager(), "alert");
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                String retorno = LoginService.cadastrar(mNome, mEmail, mPassword);
                return retorno;
            } catch (Exception e) {
                e.printStackTrace();
                onCancelled(getString(R.string.erroComectarServidor));
            }
            return getString(R.string.erroCarregaJsonInesperado);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("Cadastrado")) {
                try {
                    //realizar login
                    LoginService.openMainActivity(Cadastro.this);
                } catch (Exception e) {
                    e.printStackTrace();
                    String s = getString(R.string.erroInesperadoCarregaMainAct);
                    new AlertDialogFragment(s).show(Cadastro.this.getFragmentManager(), "alert");
                }
            }else if(result.equals("Usuário já existe"))
                new AlertDialogFragment(result).show(Cadastro.this.getFragmentManager(), "alert");
            super.onPostExecute(result);
        }

        @Override
        protected void onCancelled(String s) {
            System.out.println("INFO =====>>>> onCancelled");
            new AlertDialogFragment(s).show(Cadastro.this.getFragmentManager(), "alert");
            super.onCancelled(s);
        }
    }


    @SuppressLint("ValidFragment")
    public static class AlertDialogFragment extends DialogFragment {
        String mMessege;

        public AlertDialogFragment(String mMessege) {
            this.mMessege = mMessege;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(mMessege)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AlertDialogFragment.this.dismiss();
                        }
                    });
            return builder.create();
        }
    }
}