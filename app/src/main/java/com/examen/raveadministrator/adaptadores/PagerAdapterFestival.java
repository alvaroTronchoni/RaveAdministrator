package com.examen.raveadministrator.adaptadores;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.examen.raveadministrator.activitisYfragments.FragmentFestivalArtistas;
import com.examen.raveadministrator.activitisYfragments.FragmentFestivalDescripcion;
import com.examen.raveadministrator.activitisYfragments.FragmentFestivalFotos;
import com.examen.raveadministrator.activitisYfragments.FragmentFestivalUbicacion;

public class PagerAdapterFestival extends FragmentStatePagerAdapter {
    private final int mNumOfTabs;

    public PagerAdapterFestival(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new FragmentFestivalDescripcion();
            case 1:
                return new FragmentFestivalArtistas();
            case 2:
                return new FragmentFestivalUbicacion();
            case 3:
                return new FragmentFestivalFotos();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}