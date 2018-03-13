package com.example.davy.projetoic;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.davy.projetoic.Adapters.OptionsQuestAdapter;
import com.example.davy.projetoic.Persistence.Prova;
import com.example.davy.projetoic.Persistence.ProvaService;

import java.io.IOException;
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
        try {
            Prova prova = ProvaService.getProva(ProvaService.PORVA, this);
            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),prova);

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
        } catch (IOException e) {
            Toast.makeText(this, "Erro ao obeter prova", Toast.LENGTH_SHORT);
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
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
