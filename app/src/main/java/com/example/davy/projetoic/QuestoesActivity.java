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
import com.example.davy.projetoic.Persistence.GetImagemTask;
import com.example.davy.projetoic.Persistence.Prova;

public class QuestoesActivity extends AppCompatActivity {

    static Integer acertos = 0;
    static String[] answers;
    private static ViewPager mViewPager;
    protected Prova prova;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    public static void nextPage(){
        mViewPager.setCurrentItem(mViewPager.getCurrentItem()+1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questoes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
        Bundle extras = getIntent().getExtras();
            assert extras != null;
            prova = (Prova)extras.getSerializable("Prova");
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), prova);
        if(savedInstanceState == null) {
            acertos = 0;
            answers = new String[prova.getNumQuest()];
        }else {
            acertos = savedInstanceState.getInt("acertos");
            answers = savedInstanceState.getStringArray("answers");
        }
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setOffscreenPageLimit(2);
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
            finish();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("acertos", acertos);
        outState.putStringArray("answers", answers);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, String.valueOf(prova.getNumQuest()), Toast.LENGTH_SHORT).show();
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

        private ListView listQuests;
        private ImageView imageView;


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
            //args.putByteArray("IMG", questoes.getImage());
            args.putString("IMGID", questoes.getImage());
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
            quest.setMovementMethod(new ScrollingMovementMethod());
            quest.setText(getArguments().getString("BODY"));

            listQuests = rootView.findViewById(R.id.list_quests);
            //quando criar o adapter, dever passar as opções e respostas
            listQuests.setAdapter(new OptionsQuestAdapter(getContext(), getArguments().getString("OPA"),
                    getArguments().getString("OPB"), getArguments().getString("OPC"),
                    getArguments().getString("OPD"), getArguments().getString("OPE")));

            imageView = rootView.findViewById(R.id.imageView);
            imageView.setVisibility(View.INVISIBLE);
            if (getArguments().getString("IMGID") != null && !getArguments().getString("IMGID").equals("")){
                //byte[] img = getArguments().getByteArray("IMG");
                String simg = getArguments().getString("IMGID");
                if(simg != null) {
                    //busca no banco a imagem com o id
                    new GetImagemTask(getContext(), imageView).execute(simg);
                }
            }else{
                imageView.setVisibility(View.GONE);
            }

            listQuests.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                //seleciona a resposta
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String s = (String) adapterView.getAdapter().getItem(i);
                    String resposta = getArguments().getString("ANSWER").toLowerCase();
                    String selecao = s.substring(0,1).toLowerCase();
                    if(selecao.equals(resposta)) {
                        acertos++;
                        Toast.makeText(getActivity(), acertos.toString(), Toast.LENGTH_SHORT).show();
                    }
                    nextPage();
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
            return questsFragment.newInstance(position, prova.getQuestao(position));
        }

        @Override
        public int getCount() {
            return prova.getNumQuest();
        }
    }
}
