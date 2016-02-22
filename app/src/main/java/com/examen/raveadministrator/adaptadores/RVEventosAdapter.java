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
import com.examen.raveadministrator.modelos.Evento;

import java.util.List;

public class RVEventosAdapter extends RecyclerView.Adapter<RVEventosAdapter.EventoViewHolder> {

    private final List<Evento> eventos;
    private Context context;

    public RVEventosAdapter(List<Evento> eventos) {
        this.eventos = eventos;
    }

    public static class EventoViewHolder extends RecyclerView.ViewHolder{
        final TextView nombreEvento;
        final TextView ticketStatus;
        final TextView dia_evento;
        final TextView mes_evento;
        final ImageView ic_evento;
        final TextView fechaEvento;
        final RelativeLayout rlEvento;

        EventoViewHolder(View itemView) {
            super(itemView);
            nombreEvento = (TextView) itemView.findViewById(R.id.nombre_evento);
            ticketStatus = (TextView) itemView.findViewById(R.id.ticket_status);
            dia_evento = (TextView) itemView.findViewById(R.id.dia_evento);
            mes_evento = (TextView) itemView.findViewById(R.id.mes_evento);
            ic_evento = (ImageView) itemView.findViewById(R.id.ic_evento);
            fechaEvento = (TextView) itemView.findViewById(R.id.fecha_evento);
            rlEvento = (RelativeLayout) itemView.findViewById(R.id.rlEvento);
        }
    }

    @Override
    public int getItemCount() {
        return eventos.size();
    }

    @Override
    public EventoViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.elemento_evento, viewGroup, false);
        return new EventoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final EventoViewHolder eventoViewHolder, final int i) {
        int lastIndex = eventos.get(i).getNombreEvento().lastIndexOf("@ ");
        int lastIndexEnd = eventos.get(i).getNombreEvento().lastIndexOf(" in ");
        String nombreEvento = eventos.get(i).getNombreEvento().substring(lastIndex + 2, lastIndexEnd);
        eventoViewHolder.nombreEvento.setText(nombreEvento);
        String ticket_status = eventos.get(i).getTicket_status();
        if(ticket_status.equals("available")){
            ticket_status = "Disponible";
        }else{
            ticket_status = "No disponible";
        }
        eventoViewHolder.ticketStatus.setText(ticket_status);
        String[] arrayFecha = eventos.get(i).getFecha_evento().split(", ");
        String dia = arrayFecha[0].substring(0, 3);
        eventoViewHolder.dia_evento.setText(dia);
        int firstIndex = arrayFecha[1].lastIndexOf(" ");
        String mes = arrayFecha[1].substring(0, 3)+arrayFecha[1].substring(firstIndex);
        eventoViewHolder.mes_evento.setText(mes);
        int lastIndexHora = arrayFecha[2].lastIndexOf(" ");
        String fechaEvento = arrayFecha[2].substring(lastIndexHora+1)+" - "+ eventos.get(i).getNombreEvento().substring(lastIndexEnd+4);
        eventoViewHolder.fechaEvento.setText(fechaEvento);
        eventoViewHolder.ic_evento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("geo:0,0?q=" + eventos.get(i).getLat() + ",+" + eventos.get(i).getLng() + "(" + eventos.get(i).getNombreEvento() + ")");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            }
        });
        eventoViewHolder.rlEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(eventos.get(i).getTicket_url()));
                context.startActivity(browserIntent);
            }
        });
    }
}