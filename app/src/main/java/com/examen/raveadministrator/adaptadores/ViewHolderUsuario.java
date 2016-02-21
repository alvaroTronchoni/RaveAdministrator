package com.examen.raveadministrator.adaptadores;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.examen.raveadministrator.R;


class ViewHolderUsuario extends RecyclerView.ViewHolder{
    private TextView nombreUsuario;
    private TextView correoUsuario;
    private ImageView perfilUsuario;
    private ImageView ic_usuario;
    private RelativeLayout rl;

    ViewHolderUsuario(View itemView) {
        super(itemView);
        nombreUsuario = (TextView)itemView.findViewById(R.id.nombre_usuario);
        correoUsuario = (TextView)itemView.findViewById(R.id.correo_usuario);
        perfilUsuario = (ImageView)itemView.findViewById(R.id.perfil_usuario);
        ic_usuario = (ImageView)itemView.findViewById(R.id.ic_usuario);
        rl = (RelativeLayout)itemView.findViewById(R.id.rlUsuario);
    }

    public TextView getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(TextView nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public TextView getCorreoUsuario() {
        return correoUsuario;
    }

    public void setCorreoUsuario(TextView correoUsuario) {
        this.correoUsuario = correoUsuario;
    }

    public ImageView getPerfilUsuario() {
        return perfilUsuario;
    }

    public void setPerfilUsuario(ImageView perfilUsuario) {
        this.perfilUsuario = perfilUsuario;
    }

    public ImageView getIc_usuario() {
        return ic_usuario;
    }

    public void setIc_usuario(ImageView ic_usuario) {
        this.ic_usuario = ic_usuario;
    }

    public RelativeLayout getRl() {
        return rl;
    }

    public void setRl(RelativeLayout rl) {
        this.rl = rl;
    }
}
