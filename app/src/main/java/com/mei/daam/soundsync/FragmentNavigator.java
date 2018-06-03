package com.mei.daam.soundsync;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class FragmentNavigator {

    private final FragmentManager fragmentManager;
    private final int containerId;

    public FragmentNavigator(FragmentManager fragmentManager, @IdRes int containerId) {
        this.fragmentManager = fragmentManager;
        this.containerId = containerId;
    }

    /**
     * Only use this method when it is navigating to the first fragment in the activity.
     */
    public void navigateToWithoutBackSave(Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();//setAnimation

        fragmentTransaction = fragmentTransaction.replace(containerId, fragment);
        fragmentTransaction.commit();
    }

    public void navigateToCleaningBackStack(Fragment fragment) {
        cleanBackStack();
        navigateToWithoutBackSave(fragment);
    }

    public void navigateTo(Fragment fragment) {

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                //setAnimation
                .addToBackStack(null);

        fragmentTransaction = fragmentTransaction.replace(containerId, fragment);

        fragmentTransaction.commit();
    }

    public void cleanBackStack() {
        for (int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
            fragmentManager.popBackStack();
        }
        fragmentManager.executePendingTransactions();
    }

}
