package com.example.minoru.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.minoru.R;
import com.example.minoru.models.lavagens;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class adapterLavagem extends FirestoreRecyclerAdapter<lavagens,adapterLavagem.vecHolder> {
    private OnItemClickListener listener;

    public adapterLavagem(@NonNull FirestoreRecyclerOptions<lavagens> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull adapterLavagem.vecHolder holder, int position, @NonNull lavagens model) {
        holder.modeloTv.setText(model.getModelo());
        holder.placaTv.setText(model.getPlaca());
        //holder.modeloTv.setText(model.getModelo());

        if(model.getVeiculo().equals("MOTO")){
            holder.imgvec.setImageResource(R.drawable.ic_moto);
        }
    }

    @NonNull
    @Override
    public vecHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.veiculo_lavador,parent,false);
        return new vecHolder(v);
    }


    class vecHolder extends RecyclerView.ViewHolder{
        TextView placaTv,modeloTv;
        ImageView imgvec;



        public vecHolder(@NonNull View itemView) {
            super(itemView);
            modeloTv = itemView.findViewById(R.id.text_modelo);
            placaTv = itemView.findViewById(R.id.text_placa);
            imgvec = itemView.findViewById(R.id.imageView_veiculo);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null){
                        listener.onItemClick(getSnapshots().getSnapshot(position),position );
                    }
                }
            });
        }
    }
    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

}
