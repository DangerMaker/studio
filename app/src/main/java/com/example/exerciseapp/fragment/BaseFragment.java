package com.example.exerciseapp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;

import butterknife.ButterKnife;

/**
 * Created by lyjq on 2016/3/24.
 */
public abstract class BaseFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutRes(), container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    protected abstract int getLayoutRes();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    protected int getArgumentsInt(String key) {
        int i = 0;
        if (getArguments().containsKey(key)) {
            i = getArguments().getInt(key);
        }
        return i;
    }

    protected String getArgumentsString(String key) {
        String str = "";
        if (getArguments().containsKey(key)) {
            str = getArguments().getString(key);
        }
        return str;
    }

    protected Serializable getArgumentsSerializable(String key) {
        Serializable object = null;
        if (getArguments().containsKey(key)) {
            object = getArguments().getSerializable(key);
        }
        return object;
    }

    protected boolean getArgumentsBoolean(String key) {
        boolean b = false;
        if (getArguments().containsKey(key)) {
            b = getArguments().getBoolean(key);
        }
        return b;
    }
}
