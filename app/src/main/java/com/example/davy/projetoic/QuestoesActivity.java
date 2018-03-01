package com.example.davy.projetoic;

import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.davy.projetoic.Adapters.OptionsQuestAdapter;

import java.util.ArrayList;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questoes);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

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

        RecyclerView mRecyclerView;
        private OptionsQuestAdapter mAdapter;

        private ListView listQuests;
        int num;

        public questsFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static questsFragment newInstance(int sectionNumber, ArrayList<String> quest) {
            questsFragment fragment = new questsFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            //coloco a questão
            args.putStringArrayList(QUEST, quest);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_questoes, container, false);
            TextView numberQuest = (TextView) rootView.findViewById(R.id.txtquest);
            //carregar aqui o corpo da questão
            numberQuest.setText("Questão: " + getArguments().getInt(ARG_SECTION_NUMBER));
            TextView quest = (TextView) rootView.findViewById(R.id.textQuestao);
            quest.setText(getArguments().getStringArrayList(QUEST).get(0));


            listQuests = rootView.findViewById(R.id.list_quests);
            //quando criar o adapter, dever passar as opções e respostas
            listQuests.setAdapter(new OptionsQuestAdapter(getContext(), getArguments().getStringArrayList(QUEST).get(1),
                    getArguments().getStringArrayList(QUEST).get(2), getArguments().getStringArrayList(QUEST).get(3),
                    getArguments().getStringArrayList(QUEST).get(4), getArguments().getStringArrayList(QUEST).get(4)));
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

        ArrayList<ArrayList> prova;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            searchQuestions();
        }

        public void searchQuestions(){
            //buscar no banco uma prova e dividir uma questão para cada fragment
            prova = new ArrayList();
            //adiciona uma questao
            prova.add(new ArrayList<String>());
            ArrayList questao = prova.get(0);
            //corpo
            questao.add("A afirmação a ponderação é hoje a mais desmoralizada das virtudes deve ser entendida, no contexto, como ");
            //a
            questao.add("uma constatação já consensual, a partir da tendência dominante de se afirmar que, se uma coisa é isso, é também aquilo.");
            //b
            questao.add("a valorização do discernimento público que permite distinguir, metaforicamente falando, um abacaxi de um pepino.  ");
            //c
            questao.add("a constatação de que está ocorrendo uma negação de análises mais equilibradas, por conta da violência dos radicalismos.");
            //d
            questao.add("uma forma de repúdio às redes sociais, quando estas expõem sem subterfúgios nossos comportamentos opressivos ancestrais. ");
            //e
            questao.add("uma crítica violenta, dirigida àqueles que entendem o equilíbrio de julgamento como subproduto da perplexidade.");

            //adiciona uma questao
            prova.add(new ArrayList<String>());
            ArrayList questao2 = prova.get(1);
            //corpo
            questao2.add("Ao se referir, metaforicamente, às duas ações do fogo selvagem (3° parágrafo), o autor do texto coloca em evidência");
            //a
            questao2.add("o aparente desacordo de ações contraditórias que, de fato, se complementam num momento de ponderação.");
            //b
            questao2.add("a natureza violenta de ações e reações que se regem pelos mesmos paradigmas de brutalidade.");
            //c
            questao2.add("a contraposição entre ideais que são defendidos com argumentos igualmente ponderáveis.");
            //d
            questao2.add("a violência de opiniões contrárias, num percurso ao fim do qual elas acabarão por produzir o mesmo efeito positivo.");
            //e
            questao2.add("o avanço e o retrocesso simultâneos que as ações ponderadas acabam por impor ao ritmo da história contemporânea.");

            //adiciona uma questao
            prova.add(new ArrayList<String>());
            ArrayList questao3 = prova.get(2);
            //corpo
            questao3.add(" 2Ao se referir, metaforicamente, às duas ações do fogo selvagem (3° parágrafo), o autor do texto coloca em evidência");
            //a
            questao3.add(" 2 o aparente desacordo de ações contraditórias que, de fato, se complementam num momento de ponderação.");
            //b
            questao3.add(" 2 a natureza violenta de ações e reações que se regem pelos mesmos paradigmas de brutalidade.");
            //c
            questao3.add("2 a contraposição entre ideais que são defendidos com argumentos igualmente ponderáveis.");
            //d
            questao3.add("2 a violência de opiniões contrárias, num percurso ao fim do qual elas acabarão por produzir o mesmo efeito positivo.");
            //e
            questao3.add("2 o avanço e o retrocesso simultâneos que as ações ponderadas acabam por impor ao ritmo da história contemporânea.");


        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a questsFragment (defined as a static inner class below).
            return questsFragment.newInstance(position + 1, prova.get(position));
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return prova.size();
        }
    }
}
