package com.example.davy.projetoic;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.davy.projetoic.Interfaces.UpdateDate;
import com.example.davy.projetoic.Persistence.DBService;
import com.example.davy.projetoic.Persistence.UserService;
import com.example.davy.projetoic.utils.AlertDialogFragment;
import com.example.davy.projetoic.utils.AndroidUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Cadastro extends AppCompatActivity implements UpdateDate{

    private AutoCompleteTextView mName;
    private EditText mEmail;
    private EditText mPassword;
    private EditText mComfirm_Password;
    private ImageButton mMale;
    private ImageButton mFamale;
    private Button mData;
    private Spinner mSpnUF;
    private Spinner mSpnCit;
    private JSONArray mEstados;
    private TextView mSex;
    private char sex;
    private static int mYear, mMonth, mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cadastro);
        mName = findViewById(R.id.nomeCadastro);
        mEmail = findViewById(R.id.txt_email_cadastro);
        mPassword = findViewById(R.id.password);
        mComfirm_Password = findViewById(R.id.confirm_password);
        mMale = findViewById(R.id.btn_cad_male);
        mFamale = findViewById(R.id.btn_cad_famale);
        mSpnUF = findViewById(R.id.spn_estados);
        mSpnCit = findViewById(R.id.spn_cidades);
        mSex = findViewById(R.id.txt_cad_Sex);
        final Button btnCadastro = findViewById(R.id.btnCadastro);
        mData = findViewById(R.id.txt_cad_age);

        mData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new  DatePickerFragment().show(getSupportFragmentManager(), "datePicker");
            }
        });

        mFamale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sex = 'f';
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    v.setBackground(getDrawable(R.color.colorPrimary));
                    mMale.setBackground(getDrawable(R.color.transparent));
                    mMale.setImageAlpha(125);
                    mFamale.setImageAlpha(255);
                    v.setSelected(true);
                    mMale.setSelected(false);
                }
            }
        });

        mMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sex = 'm';
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    v.setBackground(getDrawable(R.color.colorPrimary));
                    mFamale.setBackground(getDrawable(R.color.transparent));
                    mFamale.setImageAlpha(125);
                    mMale.setImageAlpha(255);
                    v.setSelected(true);
                    mFamale.setSelected(false);
                }
            }
        });

        getUFs();

        btnCadastro.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View v) {
                if(isValidSex() && isValidDate())
                    cadastrar(mName.getText().toString(), mEmail.getText().toString(), mPassword.getText().toString()
                        , mComfirm_Password.getText().toString(), mSpnUF.getSelectedItem().toString()
                        , mSpnCit.getSelectedItem().toString(), getSex(), mData.getText().toString());
            }
        });
    }


    private boolean isValidDate() {
        mData.setError(null);
        if(mData.getText().toString().contains("/"))
            return true;
        else{
            mData.setError(getString(R.string.requiredField));
            mData.requestFocus();
            return false;
        }
    }

    private boolean isValidSex(){
        if(mFamale.isSelected())
            return true;
        else if(mMale.isSelected())
            return true;
        else{
            mSex.setError(getString(R.string.requiredField));
            mSex.requestFocus();
            return false;
        }
    }

    private char getSex() {
        return sex;
    }


    private void getUFs() {
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.cidades);
            BufferedReader bR = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            StringBuilder responseStrBuilder = new StringBuilder();

            while ((line = bR.readLine()) != null) {
                responseStrBuilder.append(line);
            }
            inputStream.close();

            JSONObject cidades = new JSONObject(responseStrBuilder.toString());
            mEstados = cidades.getJSONArray("estados");
            String[] descricoes = new String[mEstados.length()];
            ArrayList<JSONObject> estados = new ArrayList<>();
            for(int i = 0; i < mEstados.length(); i++){
                estados.add(mEstados.getJSONObject(i));
                descricoes[i] = mEstados.getJSONObject(i).get("nome").toString();
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    R.layout.support_simple_spinner_dropdown_item, descricoes);
            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            mSpnUF.setAdapter(adapter);
            mSpnUF.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    try{
                        populateSpnCit(position);
                        YoYo.with(Techniques.BounceIn)
                                .duration(1000)
                                .playOn(mSpnCit);
                    }catch (JSONException e){
                        Toast.makeText(Cadastro.this, getString(R.string.errorpopulateCities), Toast.LENGTH_SHORT).show();
                    }catch(Exception e){
                        Toast.makeText(Cadastro.this, "Ocorreu um erro na animação", Toast.LENGTH_SHORT).show();
                    }
                    mSpnCit.setVisibility(View.VISIBLE);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    mSpnUF.requestFocus();
                }
            });

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    private void populateSpnCit(int position) throws JSONException {
        JSONObject jsonEstado = mEstados.getJSONObject(position);
        JSONArray jsonCidades = jsonEstado.getJSONArray("cidades");
        List<String> cidades = new ArrayList<>();

        for(int i = 0; i < jsonCidades.length(); i++){
            cidades.add((String) jsonCidades.get(i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.support_simple_spinner_dropdown_item, cidades);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mSpnCit.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        finish();
    }

    private void cadastrar(String nome, String email, String senha, String senhaConfirm, String estado, String cidade, char sexo, String dataNasc) {
        View focusView;
        mName.setError(null);
        mEmail.setError(null);
        mPassword.setError(null);
        mComfirm_Password.setError(null);
        mSex.setError(null);
        if(isValidEmail(email)) {
            if (isValidName(nome)) {
                if (isValidPassword(senha, senhaConfirm)) {
                    CadastroTask mCadastroTask = new CadastroTask(email, senha, nome, estado, cidade, sexo, dataNasc,this);
                    mCadastroTask.execute();
                }
            } else {
                focusView = mName;
                mName.setError(getString(R.string.error_invalid_name));
                focusView.requestFocus();
            }
        }else {
            mEmail.setError(getString(R.string.requiredField));
            mEmail.requestFocus();
        }
    }

    private boolean isValidPassword(String senha, String senhaConfirm) {
        if(senha != null && senhaConfirm != null){
            if(senha.length() >= 6) {
                if (senha.equals(senhaConfirm))
                    return true;
                else {
                    mComfirm_Password.setError(getString(R.string.fildsPasswordNoMatch));
                    mComfirm_Password.requestFocus();
                    return false;
                }
            }else{
                mPassword.setError(getString(R.string.passwordLess));
                mPassword.requestFocus();
                return false;
            }
        }else{
            mPassword.setError(getString(R.string.requiredField));
            mComfirm_Password.setError(getString(R.string.requiredField));
            mComfirm_Password.requestFocus();
            mPassword.requestFocus();
            return false;
        }
    }

    private boolean isValidName(String nome) {
        return nome.length() >= 3;
    }

    private boolean isValidEmail(String email){
        if(email != null){
            if(email.contains("@")) {
                return email.endsWith(".com");
            }
        }
        return false;
    }

    @Override
    public void updateDateResource(String date) {
        mData.setText(date);
    }

    public class CadastroTask extends AsyncTask<Void, Void, String> {

        private final String mEmail;
        private final String mPassword;
        private final String mNome;
        private final String mEstado;
        private final String mCidade;
        private final char mSexo;
        private final String mDate;
        private Context mContext;

        public CadastroTask(String email, String senha, String nome, String estado, String cidade, char sexo, String dataNasc, Context cadastro) {
            mEmail = email;
            mPassword = senha;
            mNome = nome;
            mContext = cadastro;
            mEstado = estado;
            mCidade = cidade;
            mSexo = sexo;
            mDate = dataNasc;
        }

        @Override
        protected void onPreExecute() {
            if(!AndroidUtils.isNeworkAvailble(Cadastro.this))
                new AlertDialogFragment(getString(R.string.semServicoRede), Cadastro.this).show(Cadastro.this.getFragmentManager(), "alert");
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                String retorno = UserService.cadastrar(mNome, mEmail, mPassword, mEstado, mCidade, mSexo, mDate);
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
                    DBService db = new DBService(mContext);
                    db.insert(mEmail, mPassword);
                    UserService.openMainActivity(Cadastro.this);
                } catch (Exception e) {
                    e.printStackTrace();
                    String s = getString(R.string.erroInesperadoCarregaMainAct);
                    new AlertDialogFragment(s, Cadastro.this).show(Cadastro.this.getFragmentManager(), "alert");
                }
            }else if(result.equals("Usuário já existe"))
                new AlertDialogFragment(result, Cadastro.this).show(Cadastro.this.getFragmentManager(), "alert");
            super.onPostExecute(result);
        }

        @Override
        protected void onCancelled(String s) {
            new AlertDialogFragment(s, Cadastro.this).show(Cadastro.this.getFragmentManager(), "alert");
            super.onCancelled(s);
        }
    }


    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

        private UpdateDate mContext;

        @Override
        public void onAttach(Context context) {
            if(context instanceof UpdateDate){
                mContext = (UpdateDate) context;
            }else{
                throw new ClassCastException(context.toString()
                        + " The activity must implements interface UpdateDate");
            }
            super.onAttach(context);
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            super.onCreateDialog(savedInstanceState);
            final Calendar calendario = Calendar.getInstance();
            mYear = calendario.get(Calendar.YEAR);
            mMonth = calendario.get(Calendar.MONTH);
            mDay = calendario.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, mYear, mMonth, mDay);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            mYear = year;
            mMonth = month+1;
            mDay = dayOfMonth;
            updateDate();
        }

        @Override
        public int show(FragmentTransaction transaction, String tag) {
            return super.show(transaction, tag);
        }

        private void updateDate(){
            String date = new StringBuilder().append(mDay).append("/").append(mMonth).append("/")
                    .append(mYear).toString();
            mContext.updateDateResource(date);
        }
    }

}