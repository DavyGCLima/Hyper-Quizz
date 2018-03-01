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
        public static questsFragment newInstance(int sectionNumber) {
            questsFragment fragment = new questsFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_questoes, container, false);
            TextView numberQuest = (TextView) rootView.findViewById(R.id.txtquest);
            numberQuest.setText("Questão: " + getArguments().getInt(ARG_SECTION_NUMBER));
            TextView quest = (TextView) rootView.findViewById(R.id.textQuestao);
            quest.setText("Se sua aplicação tem todas as perguntas embarcadas no projeto (offline), você pode, por exemplo, criar um Array de Objetos e, a cada pergunta respondida, atualiza a tela com a próxima pergunta, armazenando a resposta do usuário em um Array separado. ");


            listQuests = rootView.findViewById(R.id.list_quests);
            listQuests.setAdapter(new OptionsQuestAdapter(getContext()));
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

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

        }

        /*private void setupRecycler(View rootView) {

            mRecyclerView = (RecyclerView) rootView.findViewById(R.id.list_quests);
            // Configurando o gerenciador de layout para ser uma lista.
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            // Adiciona o adapter que irá anexar os objetos à lista.
            // Está sendo criado com lista vazia, pois será preenchida posteriormente.
            mAdapter = new OptionsQuestAdapter(getContext(), new ArrayList<>(0));
            mRecyclerView.setAdapter(mAdapter);

            // Configurando um dividr entre linhas, para uma melhor visualização.
            mRecyclerView.addItemDecoration(
                    new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

            mRecyclerView.addOnItemTouchListener(new RecyclerItemClickLIstener(getContext(), mRecyclerView ,new RecyclerItemClickLIstener.OnItemClickListener() {
                        @Override public void onItemClick(View view, int position) {
                            OptionsQuestAdapter adapter = (OptionsQuestAdapter) mRecyclerView.getAdapter();
                            String s = (String)adapter.getItem(position);
                            Toast.makeText(getActivity(), "Selecionado  "+s, Toast.LENGTH_SHORT).show();
                        }

                        @Override public void onLongItemClick(View view, int position) {
                            // do whatever
                        }
                    })
            );

        }*/

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a questsFragment (defined as a static inner class below).
            return questsFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 5;
        }
    }
}
