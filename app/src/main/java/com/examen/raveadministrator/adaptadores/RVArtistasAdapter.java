package com.examen.raveadministrator.adaptadores;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.examen.raveadministrator.R;
import com.examen.raveadministrator.activitisYfragments.FragmentArtista;
import com.examen.raveadministrator.activitisYfragments.FragmentFestivalArtistas;
import com.examen.raveadministrator.modelos.Artista;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RVArtistasAdapter extends RecyclerView.Adapter<RVArtistasAdapter.ArtistaViewHolder> {

    private final List<Artista> artistas;
    private Context context;

    public RVArtistasAdapter(List<Artista> artistas) {
        this.artistas = artistas;
    }

    public static class ArtistaViewHolder extends RecyclerView.ViewHolder{
        final ImageView back_image;
        final TextView populartyArtist;
        final TextView nombreArtista;
        final TextView generoArtista;
        final ImageView perfilArtista;
        final RelativeLayout rl;

        ArtistaViewHolder(View itemView) {
            super(itemView);
            back_image = (ImageView) itemView.findViewById(R.id.back_image);
            populartyArtist = (TextView) itemView.findViewById(R.id.popularity_artist);
            nombreArtista = (TextView) itemView.findViewById(R.id.nombre_artista);
            generoArtista = (TextView) itemView.findViewById(R.id.genero_artista);
            perfilArtista = (ImageView) itemView.findViewById(R.id.perfil_artista);
            rl = (RelativeLayout) itemView.findViewById(R.id.rlArtista);
        }
    }

    @Override
    public int getItemCount() {
        return artistas.size();
    }

    @Override
    public ArtistaViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.elemento_artista, viewGroup, false);
        return new ArtistaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ArtistaViewHolder artistaViewHolder, final int i) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            artistaViewHolder.perfilArtista.setTransitionName("profile_transition" + i);
        }
        artistaViewHolder.populartyArtist.setText(artistas.get(i).getPopularity());
        artistaViewHolder.nombreArtista.setText(artistas.get(i).getNombreArtista());
        artistaViewHolder.generoArtista.setText(artistas.get(i).getGeneroArtista());
        RelativeLayout rl = artistaViewHolder.rl;
        Picasso.with(context).load(artistas.get(i).getUrlPerfil()).fit().into(artistaViewHolder.perfilArtista);
        artistaViewHolder.rl.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                String logoTransitionName = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    logoTransitionName = artistaViewHolder.perfilArtista.getTransitionName();
                }

                Bundle bundle = new Bundle();
                bundle.putString("NombreTransicionProfile", logoTransitionName);
                bundle.putSerializable("Artista", artistas.get(i));

                // Get access to or create instances to each fragment
                FragmentFestivalArtistas fragmentFestivalArtistas = new FragmentFestivalArtistas();
                FragmentArtista fragmentArtista = new FragmentArtista();
                fragmentArtista.setArguments(bundle);
                // Check that the device is running lollipop
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    // Inflate transitions to apply
                    Transition changeTransform = TransitionInflater.from(context).
                            inflateTransition(R.transition.change_image_transform);
                    Transition slideTransform = TransitionInflater.from(context).
                            inflateTransition(android.R.transition.slide_bottom).setDuration(changeTransform.getDuration());

                    // Setup exit transition on first fragment
                    fragmentFestivalArtistas.setSharedElementReturnTransition(changeTransform);
                    fragmentFestivalArtistas.setExitTransition(slideTransform);

                    // Setup enter transition on second fragment
                    fragmentArtista.setSharedElementEnterTransition(changeTransform);
                    fragmentArtista.setEnterTransition(slideTransform);

                    // Add second fragment by replacing first
                    FragmentTransaction ft = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, fragmentArtista)
                            .addToBackStack(null)
                            .addSharedElement(artistaViewHolder.perfilArtista, logoTransitionName);
                    // Apply the transaction
                    ft.commit();
                }
            }
        });
    }
}