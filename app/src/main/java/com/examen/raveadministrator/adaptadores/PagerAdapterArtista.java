package com.examen.raveadministrator.adaptadores;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.examen.raveadministrator.activitisYfragments.FragmentArtistaArtistasSimilares;
import com.examen.raveadministrator.activitisYfragments.FragmentArtistaDescripcion;
import com.examen.raveadministrator.activitisYfragments.FragmentArtistaFotos;
import com.examen.raveadministrator.activitisYfragments.FragmentArtistaMusica;

public class PagerAdapterArtista extends FragmentStatePagerAdapter {
    private final int mNumOfTabs;

    public PagerAdapterArtista(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new FragmentArtistaDescripcion();
            case 1:
                return new FragmentArtistaMusica();
            case 2:
                return new FragmentArtistaArtistasSimilares();
            case 3:
                return new FragmentArtistaFotos();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}