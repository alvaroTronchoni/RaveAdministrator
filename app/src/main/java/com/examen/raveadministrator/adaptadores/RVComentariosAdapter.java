package com.examen.raveadministrator.adaptadores;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.examen.raveadministrator.R;
import com.examen.raveadministrator.RoundedImageView;
import com.examen.raveadministrator.modelos.Comentario;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RVComentariosAdapter extends RecyclerView.Adapter<RVComentariosAdapter.ComentarioViewHolder> {

    private final List<Comentario> comentarios;
    private Context context;

    public RVComentariosAdapter(List<Comentario> comentarios) {
        this.comentarios = comentarios;
    }

    public static class ComentarioViewHolder extends RecyclerView.ViewHolder{
        final TextView nombreAutor;
        final RoundedImageView perfilUsuario;
        final TextView fecha;
        final TextView texto;

        ComentarioViewHolder(View itemView) {
            super(itemView);
            nombreAutor = (TextView) itemView.findViewById(R.id.nombre_autor);
            perfilUsuario = (RoundedImageView) itemView.findViewById(R.id.perfil_autor);
            fecha = (TextView) itemView.findViewById(R.id.fecha);
            texto = (TextView) itemView.findViewById(R.id.texto);
        }
    }

    @Override
    public int getItemCount() {
        return comentarios.size();
    }

    @Override
    public ComentarioViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.elemento_comentario, viewGroup, false);
        return new ComentarioViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ComentarioViewHolder comentarioViewHolder, final int i) {
        comentarioViewHolder.nombreAutor.setText(comentarios.get(i).getNombreAutor());
        if(comentarios.get(i).getPerfilUsuario().equals("NADA")){
            Picasso.with(context).load(R.drawable.avatar).into(comentarioViewHolder.perfilUsuario);
        }else{
            Bitmap bitmap = StringToBitMap(comentarios.get(i).getPerfilUsuario());
            comentarioViewHolder.perfilUsuario.setImageBitmap(bitmap);
        }
        comentarioViewHolder.fecha.setText(comentarios.get(i).getFecha());
        comentarioViewHolder.texto.setText(comentarios.get(i).getTexto());
    }

    /**
     * @param encodedString
     * @return bitmap (from given string)
     */
    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }
}