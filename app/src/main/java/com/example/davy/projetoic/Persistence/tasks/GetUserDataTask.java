package com.example.davy.projetoic.Persistence.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.example.davy.projetoic.Persistence.UserService;
import com.example.davy.projetoic.R;
import com.example.davy.projetoic.utils.AlertDialogFragment;

import org.json.JSONException;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

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

    private String makeScriptActXErr(String[] params){
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
                "          ['"+ mContext.get().getString(R.string.right)+"', "+params[0]+"]," +
                "          ['"+ mContext.get().getString(R.string.wrong)+"', "+params[1]+"]" +
                "        ]);" +
                "        var options = {'title':'"+ mContext.get().getString(R.string.titleGraphRightWrong)+"'," +
                "                       'width':450," +
                "                       'height':350," +
                "                       'titleFontSize':22," +
                "                       'toltipFontSize':14,"+
                "                       'legendFontSize':14"+
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
