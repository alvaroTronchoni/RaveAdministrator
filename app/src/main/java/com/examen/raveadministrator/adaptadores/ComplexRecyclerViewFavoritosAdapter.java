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

import com.examen.raveadministrator.R;
import com.examen.raveadministrator.activitisYfragments.FragmentArtista;
import com.examen.raveadministrator.activitisYfragments.FragmentFestival;
import com.examen.raveadministrator.activitisYfragments.FragmentFestivalArtistas;
import com.examen.raveadministrator.activitisYfragments.FragmentFestivales;
import com.examen.raveadministrator.modelos.Artista;
import com.examen.raveadministrator.modelos.Festival;
import com.examen.raveadministrator.modelos.Header;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ComplexRecyclerViewFavoritosAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // The items to display in your RecyclerView
    private final List<Object> items;
    private Context context;

    private final int FESTIVAL = 0, ARTISTA = 1, HEADER = 2;

    // Provide a suitable constructor (depends on the kind of dataset)
    public ComplexRecyclerViewFavoritosAdapter(List<Object> items) {
        this.items = items;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return this.items.size();
    }

    //Returns the view type of the item at position for the purposes of view recycling.
    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof Festival) {
            return FESTIVAL;
        } else if (items.get(position) instanceof Artista) {
            return ARTISTA;
        } else if (items.get(position) instanceof Header) {
            return HEADER;
        }
        return -1;
    }

    /**
     * This method creates different RecyclerView.ViewHolder objects based on the item view type.\
     *
     * @param viewGroup ViewGroup container for the item
     * @param viewType type of view to be inflated
     * @return viewHolder to be inflated
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        context = viewGroup.getContext();

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        switch (viewType) {
            case FESTIVAL:
                View v1 = inflater.inflate(R.layout.elemento_festival, viewGroup, false);
                viewHolder = new ViewHolderFestival(v1);
                break;
            case ARTISTA:
                View v2 = inflater.inflate(R.layout.elemento_artista, viewGroup, false);
                viewHolder = new ViewHolderArtista(v2);
                break;
            case HEADER:
                View v3 = inflater.inflate(R.layout.elemento_header, viewGroup, false);
                viewHolder = new ViewHolderHeader(v3);
                break;
        }
        return viewHolder;
    }

    /**
     * This method internally calls onBindViewHolder(ViewHolder, int) to update the
     * RecyclerView.ViewHolder contents with the item at the given position
     * and also sets up some private fields to be used by RecyclerView.
     *
     * @param viewHolder The type of RecyclerView.ViewHolder to populate
     * @param position Item position in the viewgroup.
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case FESTIVAL:
                ViewHolderFestival vh1 = (ViewHolderFestival) viewHolder;
                configureViewHolder1(vh1, position);
                break;
            case ARTISTA:
                ViewHolderArtista vh2 = (ViewHolderArtista) viewHolder;
                configureViewHolder2(vh2, position);
                break;
            case HEADER:
                ViewHolderHeader vh3 = (ViewHolderHeader) viewHolder;
                configureViewHolder3(vh3, position);
                break;
        }
    }

    private void configureViewHolder1(final ViewHolderFestival vh1, int position) {
        final Festival festival = (Festival) items.get(position);
        if (festival != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                vh1.logoFestival.setTransitionName("logo_transition" + position);
            }
            vh1.getNombreFestival().setText(festival.getNombreFestival());
            vh1.getUbicacionFestival().setText(festival.getUbicacionFestival());
            Picasso.with(context).load(festival.getLogoUrl()).into(vh1.getLogoFestival());
            Picasso.with(context).load(festival.getBackUrl()).into(vh1.getBack_image());
            vh1.rl.setOnClickListener(new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View v) {
                    String logoTransitionName;
                    logoTransitionName = vh1.logoFestival.getTransitionName();

                    Bundle bundle = new Bundle();
                    bundle.putString("NombreTransicionLogo", logoTransitionName);
                    bundle.putSerializable("Festival", festival);

                    // Get access to or create instances to each fragment
                    FragmentFestivales fragmentFestivales = new FragmentFestivales();
                    FragmentFestival fragmentFestival = new FragmentFestival();
                    fragmentFestival.setArguments(bundle);
                    // Check that the device is running lollipop
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        // Inflate transitions to apply
                        Transition changeTransform = TransitionInflater.from(context).
                                inflateTransition(R.transition.change_image_transform);
                        Transition slideTransform = TransitionInflater.from(context).
                                inflateTransition(android.R.transition.slide_bottom).setDuration(changeTransform.getDuration());

                        // Setup exit transition on first fragment
                        fragmentFestivales.setSharedElementReturnTransition(changeTransform);
                        fragmentFestivales.setExitTransition(slideTransform);

                        // Setup enter transition on second fragment
                        fragmentFestival.setSharedElementEnterTransition(changeTransform);
                        fragmentFestival.setEnterTransition(slideTransform);

                        // Add second fragment by replacing first
                        FragmentTransaction ft = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, fragmentFestival)
                                .addToBackStack(null)
                                .addSharedElement(vh1.logoFestival, logoTransitionName);
                        // Apply the transaction
                        ft.commit();
                    }
                }
            });
        }
    }

    private void configureViewHolder2(final ViewHolderArtista vh2, int position) {
        final Artista artista = (Artista) items.get(position);
        if (artista != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                vh2.perfilArtista.setTransitionName("profile_transition" + position);
            }
            vh2.populartyArtist.setText(artista.getPopularity());
            vh2.nombreArtista.setText(artista.getNombreArtista());
            vh2.generoArtista.setText(artista.getGeneroArtista());
            Picasso.with(context).load(artista.getUrlPerfil()).fit().into(vh2.perfilArtista);
            vh2.rl.setOnClickListener(new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View v) {
                    String logoTransitionName;
                    logoTransitionName = vh2.perfilArtista.getTransitionName();

                    Bundle bundle = new Bundle();
                    bundle.putString("NombreTransicionProfile", logoTransitionName);
                    bundle.putSerializable("Artista", artista);

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
                                .addSharedElement(vh2.perfilArtista, logoTransitionName);
                        // Apply the transaction
                        ft.commit();
                    }
                }
            });
        }
    }

    private void configureViewHolder3(ViewHolderHeader vh3, int position) {
        Header header = (Header) items.get(position);
        if (header != null) {
            vh3.getNombreHeader().setText(header.getNombreHeader());
        }
    }
}
