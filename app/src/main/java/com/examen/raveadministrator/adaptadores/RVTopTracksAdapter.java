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
import com.examen.raveadministrator.modelos.TopTrack;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RVTopTracksAdapter extends RecyclerView.Adapter<RVTopTracksAdapter.TopTrackViewHolder>{

    private final List<TopTrack> topTracks;
    private Context context;

    public RVTopTracksAdapter(List<TopTrack> topTracks){
        this.topTracks = topTracks;
    }

    public static class TopTrackViewHolder extends RecyclerView.ViewHolder{
        final TextView nombreTopTrack;
        final TextView duracionTopTrack;
        final ImageView imageTopTrack;
        final TextView artistasTopTrack;
        final TextView popularityTopTrack;
        final RelativeLayout rl;

        TopTrackViewHolder(View itemView) {
            super(itemView);
            nombreTopTrack = (TextView)itemView.findViewById(R.id.nombre_top_track);
            duracionTopTrack = (TextView)itemView.findViewById(R.id.duracion_top_track);
            imageTopTrack = (ImageView)itemView.findViewById(R.id.image_top_track);
            artistasTopTrack = (TextView)itemView.findViewById(R.id.artistas_top_track);
            popularityTopTrack = (TextView)itemView.findViewById(R.id.popularity_top_track);
            rl = (RelativeLayout)itemView.findViewById(R.id.rlTopTrack);
        }
    }

    @Override
    public int getItemCount() {
        return topTracks.size();
    }

    @Override
    public TopTrackViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.elemento_top_track, viewGroup, false);
        return new TopTrackViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final TopTrackViewHolder topTrackViewHolder, final int i) {
        String nombreTopTrack = topTracks.get(i).getName();
        if(nombreTopTrack.contains(" -")){
            int firstIndex = topTracks.get(i).getName().indexOf(" -");
            nombreTopTrack = nombreTopTrack.substring(0, firstIndex);
        }
        topTrackViewHolder.nombreTopTrack.setText(nombreTopTrack);
        int duration_ms = Integer.parseInt(topTracks.get(i).getDuration_ms());
        int seconds = duration_ms / 1000 % 60 ;
        int minutes = (duration_ms / (1000*60)) % 60;
        topTrackViewHolder.duracionTopTrack.setText(minutes+":"+seconds);
        RelativeLayout rl = topTrackViewHolder.rl;
        String imageUrl = topTracks.get(i).getImageUrl().replaceFirst("s", "");
        Picasso.with(context).load(imageUrl).fit().into(topTrackViewHolder.imageTopTrack);
        String artistas = topTracks.get(i).getArtists();
        artistas = artistas.substring(0, artistas.length() - 2);
        topTrackViewHolder.artistasTopTrack.setText(artistas);
        topTrackViewHolder.popularityTopTrack.setText(topTracks.get(i).getPopularity());
        topTrackViewHolder.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int startIndex = topTracks.get(i).getUri().lastIndexOf(':');
                String trackUrl = topTracks.get(i).getUri().substring(startIndex + 1);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://open.spotify.com/track/" + trackUrl));
                context.startActivity(intent);
            }
        });
    }

}