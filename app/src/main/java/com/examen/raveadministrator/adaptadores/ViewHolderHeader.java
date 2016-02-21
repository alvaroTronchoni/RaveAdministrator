package com.examen.raveadministrator.adaptadores;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.examen.raveadministrator.R;


class ViewHolderHeader extends RecyclerView.ViewHolder{
    private TextView nombreHeader;

    ViewHolderHeader(View itemView) {
        super(itemView);
        nombreHeader = (TextView) itemView.findViewById(R.id.nombre_header);
    }

    public TextView getNombreHeader() {
        return nombreHeader;
    }

    public void setNombreHeader(TextView nombreHeader) {
        this.nombreHeader = nombreHeader;
    }
}
