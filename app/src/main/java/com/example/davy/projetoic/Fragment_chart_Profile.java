package com.example.davy.projetoic;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
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


public class Fragment_chart_Profile extends Fragment {
    private static final String USEREMAIL = "userID";
    private static final String TOKEN = "token";

    private String mUserEmail;
    private String mToken;

    public Fragment_chart_Profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param email The user ID for search data from service
     * @return A new instance of fragment Fragment_chart_Profile.
     */
    public static Fragment_chart_Profile newInstance(String email, String token) {
        Fragment_chart_Profile fragment = new Fragment_chart_Profile();
        Bundle args = new Bundle();
        args.putString(USEREMAIL, email);
        args.putString(TOKEN, token);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUserEmail = getArguments().getString(USEREMAIL);
            mToken = getArguments().getString(TOKEN);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_charts__profile, container, false);
        WebView mWbGrafico = layout.findViewById(R.id.wbGrafico);
        ProgressBar mProgressBar = layout.findViewById(R.id.progressBarProfileFragment);
        new GetUserDataTask(mProgressBar, getActivity(), mWbGrafico).execute(mUserEmail, mToken);
        return layout;
    }

    public class GetUserDataTask extends AsyncTask<String, String[], String> {

        private final ProgressBar mProgress;
        private final AtomicReference<Activity> mContext = new AtomicReference<Activity>();
        private WebView mWb;

        public GetUserDataTask(ProgressBar progress, Activity context, WebView wb) {
            mProgress = progress;
            mContext.set(context);
            mWb = wb;
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                String[] userData = UserService.getUserData(strings[0], strings[1]);
                return makeScriptActXErr(userData);
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

        private String makeScriptActXErr(String[] params) {
            String script = "<html>" +
                    "  <head>" +
                    //   Load the AJAX API
                    "    <script type=\"text/javascript\" src=\"https://www.gstatic.com/charts/loader.js\"></script>" +
                    "    <script type=\"text/javascript\">" +
                    "      google.charts.load('current', {'packages':['corechart']});" +
                    "      google.charts.setOnLoadCallback(drawChart);" +
                    "      function drawChart() {" +
                    "        var data = new google.visualization.DataTable();" +
                    "        data.addColumn('string', 'Topping');" +
                    "        data.addColumn('number', 'Slices');" +
                    "        data.addRows([" +
                    "          ['" + mContext.get().getString(R.string.right) + "', " + params[0] + "]," +
                    "          ['" + mContext.get().getString(R.string.wrong) + "', " + params[1] + "]" +
                    "        ]);" +
                    "        var options = {'title':'" + mContext.get().getString(R.string.titleGraphRightWrong) + "'," +
                    "                       'width':320," +
                    "                       'height':260," +
                    "                       'titleFontSize':22," +
                    "                       'toltipFontSize':14," +
                    "                       'legendFontSize':14" +
                    "                      };" +
                    "        var chart = new google.visualization.PieChart(document.getElementById('chart_div'));" +
                    "        chart.draw(data, options);" +
                    "      }" +
                    "    </script>" +
                    "  </head>" +
                    "  <body>" +
                    //    Div that will hold the pie chart"
                    "    <div id=\"chart_div\"></div>" +
                    "  </body>" +
                    "</html>";
            return script;
        }
    }
}
