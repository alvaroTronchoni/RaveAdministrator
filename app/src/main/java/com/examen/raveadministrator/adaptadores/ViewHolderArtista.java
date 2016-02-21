package com.examen.raveadministrator.adaptadores;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.examen.raveadministrator.R;


class ViewHolderArtista extends RecyclerView.ViewHolder{
    final TextView populartyArtist;
    final TextView nombreArtista;
    final TextView generoArtista;
    final ImageView perfilArtista;
    final RelativeLayout rl;

    ViewHolderArtista(View itemView) {
        super(itemView);
        populartyArtist = (TextView) itemView.findViewById(R.id.popularity_artist);
        nombreArtista = (TextView) itemView.findViewById(R.id.nombre_artista);
        generoArtista = (TextView) itemView.findViewById(R.id.genero_artista);
        perfilArtista = (ImageView) itemView.findViewById(R.id.perfil_artista);
        rl = (RelativeLayout) itemView.findViewById(R.id.rlArtista);
    }
}