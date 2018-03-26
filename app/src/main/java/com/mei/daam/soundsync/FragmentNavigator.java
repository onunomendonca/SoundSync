package com.mei.daam.soundsync;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by D01 on 26/03/2018.
 */

public class FragmentNavigator {

    private static final String REQUEST_CODE_EXTRA = "11";
    private final FragmentManager fragmentManager;
    private final int containerId;

    public FragmentNavigator(FragmentManager fragmentManager, @IdRes int containerId){
        this.fragmentManager = fragmentManager;
        this.containerId = containerId;
    }

    public void navigateForResult(Fragment fragment, int requestCode, boolean replace) {
        Bundle extras = fragment.getArguments();
        if (extras == null) {
            extras = new Bundle();
        }
        extras.putInt(FragmentNavigator.REQUEST_CODE_EXTRA, requestCode);
        fragment.setArguments(extras);
        navigateTo(fragment, replace);
    }

    /**
     * Only use this method when it is navigating to the first fragment in the activity.
     */
    public void navigateToWithoutBackSave(Fragment fragment, boolean replace) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();//setAnimation

        if (replace) {
            fragmentTransaction = fragmentTransaction.replace(containerId, fragment);
        } else {
            fragmentTransaction = fragmentTransaction.add(containerId, fragment);
        }

        fragmentTransaction.commit();
    }

    public void navigateToCleaningBackStack(Fragment fragment, boolean replace) {
        cleanBackStack();
        navigateToWithoutBackSave(fragment, replace);
    }

    public void navigateTo(Fragment fragment, boolean replace) {

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                //setAnimation
                .addToBackStack(null);

        if (replace) {
            fragmentTransaction = fragmentTransaction.replace(containerId, fragment);
        } else {
            fragmentTransaction = fragmentTransaction.add(containerId, fragment);
        }

        fragmentTransaction.commit();
    }

    public boolean popBackStack() {
        return fragmentManager.popBackStackImmediate();
    }

    public void cleanBackStack() {
        for (int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
            fragmentManager.popBackStack();
        }
        fragmentManager.executePendingTransactions();
    }

    public boolean cleanBackStackUntil(String fragmentTag) {
        if (fragmentManager.getBackStackEntryCount() == 0) {
            return false;
        }

        boolean popped = false;

        while (fragmentManager.getBackStackEntryCount() > 0 && !popped) {
            if (fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1)
                    .getName()
                    .equals(fragmentTag)) {
                popped = true;
            }
            fragmentManager.popBackStackImmediate();
        }
        return popped;
    }

    public Fragment peekLast() {
        if (fragmentManager.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry backStackEntry =
                    fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1);
            return fragmentManager.findFragmentByTag(backStackEntry.getName());
        }

        return null;
    }

    public Fragment getFragment() {
        return fragmentManager.findFragmentById(containerId);
    }
}
