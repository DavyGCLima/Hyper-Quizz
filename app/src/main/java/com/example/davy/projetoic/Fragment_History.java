package com.example.davy.projetoic;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.example.davy.projetoic.Persistence.UserService;
import com.example.davy.projetoic.utils.AlertDialogFragment;

import org.json.JSONException;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

public class Fragment_History extends Fragment {

    private static final String USEREMAIL = "userID";
    private static final String TOKEN = "token";

    private String mUserEmail;
    private String mToken;

    public static Fragment_History newInstance(String email, String token) {
        Bundle args = new Bundle();
        args.putString(USEREMAIL, email);
        args.putString(TOKEN, token);
        Fragment_History fragment = new Fragment_History();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUserEmail = getArguments().getString(USEREMAIL);
            mToken = getArguments().getString(TOKEN);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_history, container, false);
        WebView mWbGrafico = layout.findViewById(R.id.wbGraficoHistorico);
        ProgressBar mProgressBar = layout.findViewById(R.id.progressBarProfileFragmentHistorico);
        new GetUserHistoryTask(mProgressBar, getActivity(), mWbGrafico).execute(mUserEmail, mToken);
        return layout;
    }

    public class GetUserHistoryTask extends AsyncTask<String, String[], String> {

        private final ProgressBar mProgress;
        private final AtomicReference<Activity> mContext = new AtomicReference<Activity>();
        private WebView mWb;

        public GetUserHistoryTask(ProgressBar progress, Activity context, WebView wb) {
            mProgress = progress;
            mContext.set(context);
            mWb = wb;
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                String[] userData = UserService.getHistory(strings[0], strings[1]);
                return biuldGraph(userData);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                onCancelled();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            exibirProgress(true);
        }

        @Override
        protected void onPostExecute(String script) {
            super.onPostExecute(script);
            WebSettings ws = mWb.getSettings();
            ws.setJavaScriptEnabled(true);
            ws.setSupportZoom(false);
            mWb.loadData(script, "text/html", "UTF-8");
            exibirProgress(false);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            exibirProgress(false);
            new AlertDialogFragment(mContext.get().getString(R.string.errorResponseFromServer), mContext.get()).show(mContext.get().getFragmentManager(), "msgErr");
        }

        private void exibirProgress(boolean exibir) {
            mProgress.setVisibility(exibir ? View.VISIBLE : View.GONE);
        }

        private String biuldGraph(String[] params) {
            String script = "    <html>" +
                    "  <head>" +
                    "    <script type=\"text/javascript\" src=\"https://www.gstatic.com/charts/loader.js\"></script>" +
                    "    <script type=\"text/javascript\">  " +
                    "       google.charts.load('current', {'packages':['line']});" +
                    "      google.charts.setOnLoadCallback(drawChart);" +
                    "    function drawChart() {" +
                    "      var data = new google.visualization.DataTable();" +
                    "      data.addColumn('number', 'Provas');" +
                    "      data.addColumn('number', 'Acertos');" +
                    "      data.addRows([" +
                    "        [1,  "+params[0]+"]," +
                    "        [2,  "+params[1]+"]," +
                    "        [3,  "+params[2]+"]," +
                    "        [4,  "+params[3]+"]," +
                    "        [5,  "+params[4]+"]," +
                    "      ]);" +
                    "      var options = {" +
                    "        chart: {" +
                    "          title: 'Histórico de acertos'," +
                    "        }," +
                    "        width: 360," +
                    "        height: 290" +
                    "      };" +
                    "      var chart = new google.charts.Line(document.getElementById('linechart_material'));" +
                    "      chart.draw(data, google.charts.Line.convertOptions(options));" +
                    "    }" +
                    "</script>" +
                    "  </head>" +
                    "  <body>" +
                    "    <div id=linechart_material style=width: 360px; height: 290px></div>" +
                    "  </body>" +
                    "</html>";
            return script;
        }
    }
}
