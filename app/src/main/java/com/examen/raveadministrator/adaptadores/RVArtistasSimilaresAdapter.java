package com.examen.raveadministrator.adaptadores;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.examen.raveadministrator.R;
import com.examen.raveadministrator.modelos.ArtistaSimilar;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RVArtistasSimilaresAdapter extends RecyclerView.Adapter<RVArtistasSimilaresAdapter.ArtistaSimilarViewHolder>{

    private final List<ArtistaSimilar> artistasSimilares;
    private Context context;

    public RVArtistasSimilaresAdapter(List<ArtistaSimilar> artistaSimilars){
        this.artistasSimilares = artistaSimilars;
    }

    public static class ArtistaSimilarViewHolder extends RecyclerView.ViewHolder{
        final TextView nombreArtistaSimilar;
        final TextView matchArtistaSimilar;
        final ImageView perfilArtistaSimilar;
        final TextView popularitySimilarArtist;
        final RelativeLayout rl;

        ArtistaSimilarViewHolder(View itemView) {
            super(itemView);
            nombreArtistaSimilar = (TextView)itemView.findViewById(R.id.nombre_artista_similar);
            matchArtistaSimilar = (TextView)itemView.findViewById(R.id.match_artista_similar);
            perfilArtistaSimilar = (ImageView)itemView.findViewById(R.id.perfil_artista_similar);
            popularitySimilarArtist = (TextView)itemView.findViewById(R.id.popularity_similar_artist);
            rl = (RelativeLayout)itemView.findViewById(R.id.rlArtistaSimilar);
        }
    }

    @Override
    public int getItemCount() {
        return artistasSimilares.size();
    }

    @Override
    public ArtistaSimilarViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.elemento_artista_similar, viewGroup, false);
        return new ArtistaSimilarViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ArtistaSimilarViewHolder artistaSimilarViewHolder, final int i) {
        artistaSimilarViewHolder.nombreArtistaSimilar.setText(artistasSimilares.get(i).getNombre());
        artistaSimilarViewHolder.matchArtistaSimilar.setText(artistasSimilares.get(i).getMatch());
        artistaSimilarViewHolder.popularitySimilarArtist.setText(artistasSimilares.get(i).getPopularity());
        RelativeLayout rl = artistaSimilarViewHolder.rl;
        if(!artistasSimilares.get(i).getImageUrl().equals("")){
            Picasso.with(context).load(artistasSimilares.get(i).getImageUrl()).fit().into(artistaSimilarViewHolder.perfilArtistaSimilar);
        }
        artistaSimilarViewHolder.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(artistasSimilares.get(i).getUrl()));
                context.startActivity(browserIntent);
            }
        });
    }

}