package com.examen.raveadministrator.adaptadores;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.examen.raveadministrator.R;
import com.examen.raveadministrator.RoundedImageView;


class ViewHolderFestival extends RecyclerView.ViewHolder{
    private ImageView back_image;
    private TextView nombreFestival;
    private TextView ubicacionFestival;
    RoundedImageView logoFestival;
    RelativeLayout rl;

    ViewHolderFestival(View itemView) {
        super(itemView);
        back_image = (ImageView) itemView.findViewById(R.id.back_image);
        nombreFestival = (TextView) itemView.findViewById(R.id.nombre_festival);
        ubicacionFestival = (TextView) itemView.findViewById(R.id.ubicacion_festival);
        logoFestival = (RoundedImageView) itemView.findViewById(R.id.logo_festival);
        rl = (RelativeLayout) itemView.findViewById(R.id.rlFestival);
    }

    public ImageView getBack_image() {
        return back_image;
    }

    public void setBack_image(ImageView back_image) {
        this.back_image = back_image;
    }

    public TextView getNombreFestival() {
        return nombreFestival;
    }

    public void setNombreFestival(TextView nombreFestival) {
        this.nombreFestival = nombreFestival;
    }

    public TextView getUbicacionFestival() {
        return ubicacionFestival;
    }

    public void setUbicacionFestival(TextView ubicacionFestival) {
        this.ubicacionFestival = ubicacionFestival;
    }

    public RoundedImageView getLogoFestival() {
        return logoFestival;
    }

    public void setLogoFestival(RoundedImageView logoFestival) {
        this.logoFestival = logoFestival;
    }

    public RelativeLayout getRl() {
        return rl;
    }

    public void setRl(RelativeLayout rl) {
        this.rl = rl;
    }
}