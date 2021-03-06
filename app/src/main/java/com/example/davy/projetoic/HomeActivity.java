package com.example.davy.projetoic;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.davy.projetoic.Persistence.DBService;
import com.example.davy.projetoic.Persistence.ProvaService;
import com.example.davy.projetoic.utils.AndroidUtils;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarHome);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        progressBar = findViewById(R.id.progressBrar);
        progressBar.setVisibility(View.GONE);

        DBService db = new DBService(this);
        String[] user = db.getUser();

        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_home);
        TextView emailUser = headerView.findViewById(R.id.email_perfil_nav);
        TextView nameUser = headerView.findViewById(R.id.nome_perfil_nav);
        nameUser.setText(user[DBService.NAME]);
        emailUser.setText(user[DBService.EMAIL]);

        Fragment fragHistory = Fragment_History.newInstance(user[DBService.EMAIL], user[DBService.TOKEN]);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.container_graph_home, fragHistory);
        ft.commit();

        Button btnIniciarTeste = findViewById(R.id.btn_iniciar_teste);
        btnIniciarTeste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openQuestsActivity();
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.tools_Drawer) {
            openProfileActivity();
        } else if (id == R.id.quests_drawer) {
            openQuestsActivity();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openQuestsActivity(){
            new GetTipoProvaTask(this, progressBar).execute();
    }

    private void openProfileActivity(){
        Intent it = new Intent(this, Profile.class);
        this.startActivity(it);
    }

    public class GetTipoProvaTask extends AsyncTask<Void , Void, ArrayList> {

        private HomeActivity mContext;
        private final ThreadLocal<ProgressBar> progressBar = new ThreadLocal<ProgressBar>();
        private String mToken;

        public GetTipoProvaTask(HomeActivity context, ProgressBar progressBar) {
            this.mContext = context;
            this.progressBar.set(progressBar);
        }

        @Override
        protected void onPreExecute() {
            if(progressBar.get() != null)
                exibirProgress(true);
            DBService db = new DBService(mContext);
            String[] user = db.getUser();
            mToken = user[2];
            if(!AndroidUtils.isNeworkAvailble(mContext))
                Toast.makeText(mContext, mContext.getString(R.string.errorResponseFromServer), Toast.LENGTH_SHORT).show();
            super.onPreExecute();
        }

        @Override
        protected ArrayList doInBackground(Void... voids) {
            publishProgress();
            try {
                ArrayList list;
                list = ProvaService.getTipoProva(mToken, mContext);
                publishProgress();
                return list;
            } catch (Exception e) {
                e.printStackTrace();
                onCancelled();
            }
            return null;
        }

        @Override
        /**
         * Inicia a activity de tipo prova passando os parametros necessários
         * @param Recebe a lista contendo os tipos de prova do doInBackgroung
         */
        protected void onPostExecute(ArrayList list) {
            if(list != null && list.size() > 0){
                Intent it = new Intent(mContext, TiposProva.class);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("lista", list);
                it.putExtras(bundle);
                exibirProgress(false);
                mContext.startActivity(it);
            }else
                exibirProgress(false);
            Toast.makeText(mContext, "Não foi possivel recuperar os dados", Toast.LENGTH_SHORT).show();
            super.onPostExecute(list);
        }



        @Override
        protected void onProgressUpdate(Void... values) {
            if (progressBar.get() != null)
                progressBar.get().incrementProgressBy(50);
            super.onProgressUpdate(values);
        }


        private void exibirProgress(boolean exibir) {
            progressBar.get().setVisibility(exibir ? View.VISIBLE : View.GONE);
        }
    }
}
