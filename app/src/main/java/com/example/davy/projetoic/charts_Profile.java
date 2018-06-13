package com.example.davy.projetoic;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.example.davy.projetoic.Persistence.tasks.GetUserDataTask;


public class charts_Profile extends Fragment {
    private static final String USEREMAIL = "userID";
    private static final String TOKEN = "token";

    private String mUserEmail;
    private WebView mWbGrafico;
    private String mToken;
    private ProgressBar mProgressBar;

    public charts_Profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param email The user ID for search data from service
     * @return A new instance of fragment charts_Profile.
     */
    public static charts_Profile newInstance(String email, String token) {
        charts_Profile fragment = new charts_Profile();
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
        mWbGrafico = layout.findViewById(R.id.wbGrafico);
        mProgressBar = layout.findViewById(R.id.progressBarProfileFragment);
        new GetUserDataTask(mProgressBar, getActivity(), mWbGrafico).execute(mUserEmail, mToken);
        return layout;
    }

}
