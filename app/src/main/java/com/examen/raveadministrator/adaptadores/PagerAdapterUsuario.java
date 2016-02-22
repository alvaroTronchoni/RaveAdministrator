package com.examen.raveadministrator.adaptadores;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.examen.raveadministrator.activitisYfragments.FragmentUsuarioAmigos;
import com.examen.raveadministrator.activitisYfragments.FragmentUsuarioCalendario;
import com.examen.raveadministrator.activitisYfragments.FragmentUsuarioDescripcion;
import com.examen.raveadministrator.activitisYfragments.FragmentUsuarioFavoritos;

public class PagerAdapterUsuario extends FragmentStatePagerAdapter {
    private final int mNumOfTabs;

    public PagerAdapterUsuario(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new FragmentUsuarioDescripcion();
            case 1:
                return new FragmentUsuarioAmigos();
            case 2:
                return new FragmentUsuarioFavoritos();
            case 3:
                return new FragmentUsuarioCalendario();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}