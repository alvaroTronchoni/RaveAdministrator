package com.examen.raveadministrator.adaptadores;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.examen.raveadministrator.R;
import com.examen.raveadministrator.activitisYfragments.FragmentFestivales;
import com.examen.raveadministrator.activitisYfragments.FragmentFestivalesClasificacion;
import com.examen.raveadministrator.modelos.Clasificacion;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RVClasificacionesAdapter extends RecyclerView.Adapter<RVClasificacionesAdapter.ClasificacionViewHolder> {

    private final List<Clasificacion> clasificaciones;
    private Context context;

    public RVClasificacionesAdapter(List<Clasificacion> clasificaciones) {
        this.clasificaciones = clasificaciones;
    }

    public static class ClasificacionViewHolder extends RecyclerView.ViewHolder{
        final ImageView back_image;
        final ImageView logoClasificacion;
        final TextView nombreClasificacion;
        final RelativeLayout rl;

        ClasificacionViewHolder(View itemView) {
            super(itemView);
            back_image = (ImageView) itemView.findViewById(R.id.back_image);
            nombreClasificacion = (TextView) itemView.findViewById(R.id.nombre_clasificacion);
            logoClasificacion = (ImageView) itemView.findViewById(R.id.logo_clasificacion);
            rl = (RelativeLayout) itemView.findViewById(R.id.rlClasificacion);
        }
    }

    @Override
    public int getItemCount() {
        return clasificaciones.size();
    }

    @Override
    public ClasificacionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.elemento_clasificacion, viewGroup, false);
        return new ClasificacionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ClasificacionViewHolder clasificacionViewHolder, final int i) {
        clasificacionViewHolder.nombreClasificacion.setText(clasificaciones.get(i).getNombreClasificacion());
        RelativeLayout rl = clasificacionViewHolder.rl;
        Picasso.with(context).load(clasificaciones.get(i).getIdLogo()).fit().centerCrop().into(clasificacionViewHolder.logoClasificacion);
        Picasso.with(context).load(clasificaciones.get(i).getIdBack()).fit().centerCrop().into(clasificacionViewHolder.back_image);
        clasificacionViewHolder.rl.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("Clasificacion", clasificaciones.get(i));

                // Get access to or create instances to each fragment
                FragmentFestivalesClasificacion fragmentFestivalesClasificacion = new FragmentFestivalesClasificacion();
                FragmentFestivales fragmentFestivales = new FragmentFestivales();
                fragmentFestivales.setArguments(bundle);

                // Add second fragment by replacing first
                FragmentTransaction ft = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                        .replace(R.id.container, fragmentFestivales)
                        .addToBackStack(null);
                // Apply the transaction
                ft.commit();
            }
        });
    }

}
