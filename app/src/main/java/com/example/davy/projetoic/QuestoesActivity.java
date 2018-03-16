package com.example.davy.projetoic;

import android.content.Context;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.davy.projetoic.Adapters.OptionsQuestAdapter;
import com.example.davy.projetoic.Persistence.Prova;
import com.example.davy.projetoic.Persistence.ProvaService;

import org.json.JSONException;

import java.io.IOException;


public class QuestoesActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    protected Prova prova;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questoes);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        try {
            //deverá ser colocado aqui o aprametro para qual prova deve ser carregada
            //Prova prova = ProvaService.getProva(R.string.enem, this);

            //parametro do execute é o tipo da prova R.string.enad

            progressBar = findViewById(R.id.progressBar);
            /*Prova prova =*/ new GetProvaTask(this, mSectionsPagerAdapter).execute(R.string.enad).get();

            //mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),prova);

            // Set up the ViewPager with the sections adapter.
            mViewPager = (ViewPager) findViewById(R.id.container);
            mViewPager.setAdapter(mSectionsPagerAdapter);

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            Log.e("Erro No APP: ", e.getMessage());
        }
    }

    private void exibirProgress(boolean exibir) {
        progressBar.setVisibility(exibir ? View.VISIBLE : View.GONE);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class GetProvaTask extends AsyncTask<Integer,Void,Prova> {

        private QuestoesActivity context;
        private SectionsPagerAdapter sectionsPagerAdapter;

        public GetProvaTask(QuestoesActivity context, SectionsPagerAdapter sectionsPagerAdapter) {
            this.context = context;
            this.sectionsPagerAdapter = sectionsPagerAdapter;
        }

        @Override
        protected void onPreExecute() {
            exibirProgress(true);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Prova prova) {
            if(prova != null) {
                super.onPostExecute(prova);
                //devolver a prova para o adapter
                sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), prova);
            }else{
                Toast.makeText(context, "Erro ao efetuar o download", Toast.LENGTH_SHORT).show();
                finish();
            }
            exibirProgress(false);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            progressBar.incrementProgressBy(25);
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(Prova prova) {
            super.onCancelled(prova);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Toast.makeText(context, "Erro ao efetuar o download", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Prova doInBackground(Integer... integers) {
            publishProgress();
            try {
                Prova prova = ProvaService.getProva(integers[0], context);
                publishProgress();
                return prova;
            } catch (IOException e) {
                Log.e("Erro de conexão: ", e.getMessage());
                e.printStackTrace();
                return null;
            }catch (JSONException e){
                e.printStackTrace();
                Log.e("Erro conv JSON: ", e.getMessage());
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("Erro de conexão inesp: ", e.getMessage());
                return null;
            }
        }
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
        private static final String QUEST = "Quest";

        private ListView listQuests;
        int num;

        public questsFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static questsFragment newInstance(int sectionNumber, Prova.Questoes questoes) {
            questsFragment fragment = new questsFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            //coloco a questão
            args.putString("BODY", questoes.getBody());
            args.putString("OPA", questoes.getOptionA());
            args.putString("OPB", questoes.getOptionB());
            args.putString("OPC", questoes.getOptionC());
            args.putString("OPD", questoes.getOptionD());
            args.putString("OPE", questoes.getOptionE());
            args.putString("ANSWER" ,questoes.getAnswer());
            args.putString("IMG", questoes.getImage());
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_questoes, container, false);
            TextView numberQuest = (TextView) rootView.findViewById(R.id.txtquest);
            //carregar aqui o corpo da questão
            numberQuest.setText(getString(R.string.txtQuestao, getArguments().getInt(ARG_SECTION_NUMBER)));
            TextView quest = (TextView) rootView.findViewById(R.id.textQuestao);
            quest.setText(getArguments().getString("BODY"));


            listQuests = rootView.findViewById(R.id.list_quests);
            //quando criar o adapter, dever passar as opções e respostas
            listQuests.setAdapter(new OptionsQuestAdapter(getContext(), getArguments().getString("OPA"),
                    getArguments().getString("OPB"), getArguments().getString("OPC"),
                    getArguments().getString("OPD"), getArguments().getString("OPE")));
            listQuests.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                //seleciona a resposta
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String s = (String) adapterView.getAdapter().getItem(i);
                    Toast.makeText(getContext(), "Selecionado " + s, Toast.LENGTH_SHORT).show();
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

        //ArrayList<ArrayList> prova;
        Prova prova;

        public SectionsPagerAdapter(FragmentManager fm, Prova prova) {
            super(fm);
            this.prova = prova;
        }


        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a questsFragment (defined as a static inner class below).
            return questsFragment.newInstance(position + 1, prova.getQuestao(position));
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return prova.getNumQuest();
        }
    }
}
