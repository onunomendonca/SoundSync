package com.mei.daam.soundsync;

import android.view.View;
import android.widget.Button;

/**
 * Created by D01 on 25/05/2018.
 */

public class MainScreenPresenter {


    private final FragmentNavigator fragmentNavigator;
    private final Button novoGrupo;
    private final Button juntarGrupo;

    public MainScreenPresenter(FragmentNavigator fragmentNavigator, Button novoGrupo, Button juntarGrupo) {
        this.fragmentNavigator = fragmentNavigator;
        this.novoGrupo = novoGrupo;
        this.juntarGrupo = juntarGrupo;
    }


    public void setListeners() {
        novoGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentNavigator.navigateTo(new CreateGroupFragment());
            }
        });
        juntarGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentNavigator.navigateTo(new JoinGroupFragment());
            }
        });
    }
}
