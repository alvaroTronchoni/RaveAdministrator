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
import com.examen.raveadministrator.RoundedImageView;
import com.examen.raveadministrator.activitisYfragments.FragmentFestival;
import com.examen.raveadministrator.activitisYfragments.FragmentFestivales;
import com.examen.raveadministrator.modelos.Festival;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RVFestivalesAdapter extends RecyclerView.Adapter<RVFestivalesAdapter.FestivalViewHolder> {

    private final List<Festival> festivales;
    private Context context;

    public RVFestivalesAdapter(List<Festival> festivales) {
        this.festivales = festivales;
    }

    public static class FestivalViewHolder extends RecyclerView.ViewHolder{
        final ImageView back_image;
        final TextView nombreFestival;
        final TextView ubicacionFestival;
        final RoundedImageView logoFestival;
        final RelativeLayout rl;

        FestivalViewHolder(View itemView) {
            super(itemView);
            back_image = (ImageView) itemView.findViewById(R.id.back_image);
            nombreFestival = (TextView) itemView.findViewById(R.id.nombre_festival);
            ubicacionFestival = (TextView) itemView.findViewById(R.id.ubicacion_festival);
            logoFestival = (RoundedImageView) itemView.findViewById(R.id.logo_festival);
            rl = (RelativeLayout) itemView.findViewById(R.id.rlFestival);
        }
    }

    @Override
    public int getItemCount() {
        return festivales.size();
    }

    @Override
    public FestivalViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.elemento_festival, viewGroup, false);
        return new FestivalViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final FestivalViewHolder festivalViewHolder, final int i) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            festivalViewHolder.logoFestival.setTransitionName("logo_transition" + i);
        }
        festivalViewHolder.nombreFestival.setText(festivales.get(i).getNombreFestival());
        festivalViewHolder.ubicacionFestival.setText(festivales.get(i).getUbicacionFestival());
        RelativeLayout rl = festivalViewHolder.rl;
        Picasso.with(context).load(festivales.get(i).getLogoUrl()).fit().into(festivalViewHolder.logoFestival);
        Picasso.with(context).load(festivales.get(i).getBackUrl()).fit().centerCrop().into(festivalViewHolder.back_image);
        festivalViewHolder.rl.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                String logoTransitionName = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    logoTransitionName = festivalViewHolder.logoFestival.getTransitionName();
                }

                Bundle bundle = new Bundle();
                bundle.putString("NombreTransicionLogo", logoTransitionName);
                bundle.putSerializable("Festival", festivales.get(i));

                // Get access to or create instances to each fragment
                FragmentFestivales fragmentFestivales = new FragmentFestivales();
                FragmentFestival fragmentFestival = new FragmentFestival();
                fragmentFestival.setArguments(bundle);

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
                }

                    // Add second fragment by replacing first
                    FragmentTransaction ft = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, fragmentFestival)
                            .addToBackStack(null);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                ft.addSharedElement(festivalViewHolder.logoFestival, logoTransitionName);
                            }
                    // Apply the transaction
                    ft.commit();
                }
        });
    }

}
