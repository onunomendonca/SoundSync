package com.mei.daam.soundsync;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
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
        MainActivity.myLayout = (ConstraintLayout) view.findViewById(R.id.myLayout);
        MainActivity.animationDrawable = (AnimationDrawable) MainActivity.myLayout.getBackground();
        MainActivity.animationDrawable.setEnterFadeDuration(4500);
        MainActivity.animationDrawable.setExitFadeDuration(4500);
        MainActivity.animationDrawable.start();

        fragmentNavigator = ((MainActivity) getActivity()).getFragmentNavigator();
        novoGrupo = (Button) view.findViewById(R.id.novo_grupo_btn);
        juntarGrupo = (Button) view.findViewById(R.id.juntar_grupo_btn);
        novoGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentNavigator.navigateTo(new CreateGroupFragment(), true);
            }
        });
        juntarGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentNavigator.navigateTo(new JoinGroupFragment(), true);
            }
        });
    }
}
