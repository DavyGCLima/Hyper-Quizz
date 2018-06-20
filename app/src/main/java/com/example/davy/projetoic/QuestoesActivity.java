package com.example.davy.projetoic;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.davy.projetoic.Adapters.OptionsQuestAdapter;
import com.example.davy.projetoic.Interfaces.ContainerViewPager;
import com.example.davy.projetoic.Persistence.DBService;
import com.example.davy.projetoic.Persistence.tasks.GetImagemTask;
import com.example.davy.projetoic.Persistence.UserService;
import com.example.davy.projetoic.Persistence.Prova;
import com.example.davy.projetoic.utils.AlertDialogFragment;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class QuestoesActivity extends AppCompatActivity implements ContainerViewPager {

    private static Integer acertos = 0;
    private static boolean[] verificador;
    private static String[] mAnswers;
    private ViewPager mViewPager;
    protected Prova prova;
    AsyncTask<Void, Void, String> task;

    @Override
    public void nextPage(){
        if(mViewPager.getCurrentItem()+1 == prova.getNumQuest())
            gabaritar();
        else
            mViewPager.setCurrentItem(mViewPager.getCurrentItem()+1);
    }

    private void gabaritar() {
        boolean cont = true;
        for (int i = 0; i < verificador.length; i++) {
            if(verificador[i] == false) {
                cont = false;
                mViewPager.setCurrentItem(i);
                new AlertDialogFragment(getString(R.string.blancQuestion), this);
            }
        }
       if(cont) {
           acertos = 0;
           ArrayList options = new ArrayList();
           for (int i = 0; i < prova.getNumQuest(); i++) {
               if (mAnswers[i].equals(prova.getQuestao(i).getAnswer().toLowerCase()))
                   acertos++;
               options.add(prova.getQuestao(i).getAnswer());
           }
           task = new atualizaEstatisticaUsuarioTask(acertos, (prova.getNumQuest() - acertos), this).execute();
           Intent it = new Intent(this, FinalizaProva.class);
           it.putExtra("acertos", acertos.toString());
           int erros = (prova.getNumQuest() - acertos);
           it.putExtra("erros", String.valueOf(erros));
           it.putStringArrayListExtra("options", options);
           it.putExtra("answers", mAnswers);
           it.putExtra("title", prova.getName());
           startActivity(it);
       }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questoes);
        Toolbar toolbar = findViewById(R.id.toolbarQuestoes);
        setSupportActionBar(toolbar);
        try {
            Bundle extras = getIntent().getExtras();
            assert extras != null;
            prova = (Prova)extras.getSerializable("Prova");
            int totalPages = prova.getNumQuest();
            verificador = new boolean[prova.getNumQuest()];
            for (boolean b : verificador)
                b = false;

            SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), prova);
            if(savedInstanceState == null) {
                acertos = 0;
                mAnswers = null;
                mAnswers = new String[prova.getNumQuest()];
            }else {
                acertos = savedInstanceState.getInt("acertos");
                mAnswers = savedInstanceState.getStringArray("mAnswers");
            }
            mViewPager = (ViewPager) findViewById(R.id.containerQuestoes);
            mViewPager.setOffscreenPageLimit(2);
            mViewPager.setAdapter(mSectionsPagerAdapter);

        }catch (Exception e){
            e.printStackTrace();
            Log.e("Erro No APP: ", e.getMessage());
            finish();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("acertos", acertos);
        outState.putStringArray("mAnswers", mAnswers);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, String.valueOf(prova.getNumQuest()), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(task != null)
            task.cancel(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_questoes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_finalizar_prova) {
            gabaritar();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class questsFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        private ListView mListQuests;
        private ImageView mImageView;
        private ContainerViewPager mContainer;

        @Override
        public void onAttach(Context context) {
            if(context instanceof ContainerViewPager){
                mContainer = (ContainerViewPager) context;
            }else{
                throw new ClassCastException(context.toString()
                        + " A actividade deve implementar a interface ContainerViewPager");
            }
            super.onAttach(context);
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static questsFragment newInstance(int sectionNumber, Prova.Questoes questao) {
            questsFragment fragment = new questsFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            //coloco a questão
            args.putString("BODY", questao.getBody());
            args.putString("OPA", questao.getOptionA());
            args.putString("OPB", questao.getOptionB());
            args.putString("OPC", questao.getOptionC());
            args.putString("OPD", questao.getOptionD());
            args.putString("OPE", questao.getOptionE());
            args.putString("ANSWER" ,questao.getAnswer());
            //args.putByteArray("IMG", questoes.getImage());
            args.putString("IMGID", questao.getImage());
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_questoes, container, false);
            TextView numberQuest = rootView.findViewById(R.id.txtquest);
            //carregar aqui o corpo da questão
            numberQuest.setText(getString(R.string.txtQuestao, getArguments().getInt(ARG_SECTION_NUMBER)+1));
            TextView quest = rootView.findViewById(R.id.textQuestao);
            quest.setMovementMethod(new ScrollingMovementMethod());
            quest.setText(getArguments().getString("BODY"));

            mListQuests = rootView.findViewById(R.id.list_quests);
            //quando criar o adapter, dever passar as opções e respostas
            mListQuests.setAdapter(new OptionsQuestAdapter(getContext(), getArguments().getString("OPA"),
                    getArguments().getString("OPB"), getArguments().getString("OPC"),
                    getArguments().getString("OPD"), getArguments().getString("OPE")));

            mImageView = rootView.findViewById(R.id.imageView);
            mImageView.setVisibility(View.INVISIBLE);
            if (getArguments().getString("IMGID") != null && !Objects.equals(getArguments().getString("IMGID"), "")){
                String simg = getArguments().getString("IMGID");
                if(simg != null) {
                    //busca no banco a imagem com o id
                    new GetImagemTask(getContext(), mImageView).execute(simg);
                }
            }else{
                mImageView.setVisibility(View.GONE);
            }

            mListQuests.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                //seleciona a resposta
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String s = (String) adapterView.getAdapter().getItem(i);
                    String resposta = Objects.requireNonNull(getArguments().getString("ANSWER")).toLowerCase();
                    String selecao = s.substring(0, 1).toLowerCase();
                    mAnswers[getArguments().getInt(ARG_SECTION_NUMBER)] = selecao;
                    verificador[getArguments().getInt(ARG_SECTION_NUMBER)] = true;
                    if (selecao.equals(resposta)) {
                        acertos++;
                    } else
                        acertos--;
                    mContainer.nextPage();
                }
            });
            return rootView;
        }

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        Prova mProva;

        public SectionsPagerAdapter(FragmentManager fm, Prova prova) {
            super(fm);
            this.mProva = prova;
        }


        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a questsFragment (defined as a static inner class below).
            return questsFragment.newInstance(position, mProva.getQuestao(position));
        }

        @Override
        public int getCount() {
            return mProva.getNumQuest();
        }
    }

    private class atualizaEstatisticaUsuarioTask extends AsyncTask<Void, Void, String>{

        int acertos;
        int erros;
        QuestoesActivity mContext;

        public atualizaEstatisticaUsuarioTask(int acertos, int erros, QuestoesActivity context) {
            this.acertos = acertos;
            this.erros = erros;
            this.mContext = context;
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                DBService db = new DBService(mContext);
                final String[] user = db.getUser();
                String email = user[DBService.EMAIL];
                String token = user[DBService.TOKEN];
                String retorno = UserService.saveDataAfterTest(acertos, erros, email, token, mContext);
                return retorno;
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                return mContext.getString(R.string.erroComectarServidor);
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(!s.equals("ok") && !mContext.isFinishing() && isCancelled()){
                new AlertDialogFragment(s, QuestoesActivity.this).show(QuestoesActivity.this.getFragmentManager(), "alert");
            }
        }
    }
}
