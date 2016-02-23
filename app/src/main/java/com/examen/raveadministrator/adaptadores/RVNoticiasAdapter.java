package com.examen.raveadministrator.adaptadores;

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
import com.examen.raveadministrator.activitisYfragments.FragmentNoticia;
import com.examen.raveadministrator.activitisYfragments.FragmentNoticias;
import com.examen.raveadministrator.modelos.Noticia;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RVNoticiasAdapter extends RecyclerView.Adapter<RVNoticiasAdapter.NoticiaViewHolder>{

    private final List<Noticia> noticias;
    private Context context;

    public RVNoticiasAdapter(List<Noticia> noticias){
        this.noticias = noticias;
    }

    public static class NoticiaViewHolder extends RecyclerView.ViewHolder{
        final ImageView back_image;
        final TextView tituloNoticia;
        final TextView contenidoNoticia;
        final RelativeLayout rl;

        NoticiaViewHolder(View itemView) {
            super(itemView);
            back_image = (ImageView)itemView.findViewById(R.id.back_image);
            tituloNoticia = (TextView)itemView.findViewById(R.id.titulo_noticia);
            contenidoNoticia = (TextView)itemView.findViewById(R.id.contenido_noticia);
            rl = (RelativeLayout)itemView.findViewById(R.id.rlNoticia);
        }
    }

    @Override
    public int getItemCount() {
        return noticias.size();
    }

    @Override
    public NoticiaViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.elemento_noticia, viewGroup, false);
        return new NoticiaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final NoticiaViewHolder noticiaViewHolder, final int i) {
        noticiaViewHolder.tituloNoticia.setText(noticias.get(i).getName());
        noticiaViewHolder.contenidoNoticia.setText(noticias.get(i).getSummary());
        RelativeLayout rl = noticiaViewHolder.rl;
        if(noticias.get(i).getImage_url().contains("http:") || noticias.get(i).getImage_url().contains("https:")){
            Picasso.with(context).load(noticias.get(i).getImage_url()).fit().centerCrop().into(noticiaViewHolder.back_image);
        }else{
            String imageUrl = "http:"+noticias.get(i).getImage_url();
            Picasso.with(context).load(imageUrl).fit().centerCrop().into(noticiaViewHolder.back_image);
        }
        noticiaViewHolder.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putSerializable("Noticia", noticias.get(i));

                // Get access to or create instances to each fragment
                FragmentNoticias fragmentNoticias = new FragmentNoticias();
                FragmentNoticia fragmentNoticia = new FragmentNoticia();
                fragmentNoticia.setArguments(bundle);
                // Check that the device is running lollipop
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    // Inflate transitions to apply
                    Transition slideTransform = TransitionInflater.from(context).
                            inflateTransition(android.R.transition.slide_bottom);

                    // Setup exit transition on first fragment
                    fragmentNoticias.setExitTransition(slideTransform);

                    // Setup enter transition on second fragment
                    fragmentNoticia.setEnterTransition(slideTransform);
                }

                    // Add second fragment by replacing first
                    FragmentTransaction ft = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, fragmentNoticia)
                            .addToBackStack(null);
                    // Apply the transaction
                    ft.commit();
                }
        });
    }

}