package com.mei.daam.soundsync;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by D01 on 26/03/2018.
 */

public class MainScreenFragment extends Fragment {

    private Button novoGrupo;
    private Button juntarGrupo;
    private FragmentNavigator fragmentNavigator;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_screen,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //For the animated Background

        fragmentNavigator = ((MainActivity) getActivity()).getFragmentNavigator();
        novoGrupo = (Button) view.findViewById(R.id.novo_grupo_btn);
        juntarGrupo = (Button) view.findViewById(R.id.juntar_grupo_btn);
        new MainScreenPresenter(fragmentNavigator, novoGrupo, juntarGrupo).setListeners();
    }
}
