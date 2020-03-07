package com.naveed.AnimatedZipUnZip.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.naveed.AnimatedZipUnZip.R;
import com.naveed.AnimatedZipUnZip.Models.FilesModelClass;

import java.util.ArrayList;

public class Adapter_ZipFiles extends RecyclerView.Adapter<Adapter_ZipFiles.MyViewHolder> {

    ArrayList<FilesModelClass> foldername;
    ArrayList<FilesModelClass> TempArraylist;
    Context context;
    interfaceCLicklistner onfolderlistner;
    String check;

    public Adapter_ZipFiles(ArrayList<FilesModelClass> foldername, Context context,
                            interfaceCLicklistner onfolderlistner ) {
        this.foldername = foldername;
        this.TempArraylist = foldername;
        this.context = context;
        this.onfolderlistner = onfolderlistner;
        setHasStableIds(true);
        this.check = check;

    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.rcv_list_item_layout, parent , false);
        return new MyViewHolder(v , onfolderlistner);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.foldername.setText(foldername.get(position).getName());

           holder.img.setImageDrawable(foldername.get(position).getItemIcon());

    }


    @Override
    public int getItemCount() {
        return foldername.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView foldername;
        ImageView img, playicon;
        interfaceCLicklistner onfolderlistner;
        CheckBox checkBox;
        public MyViewHolder(@NonNull View itemView, interfaceCLicklistner onfolderlistner) {
            super(itemView);
            foldername = itemView.findViewById(R.id.internal_foldername);
            foldername.setSelected(true);
            img = itemView.findViewById(R.id.img);
            playicon = itemView.findViewById(R.id.imgPlayVidesdasd);
            this.onfolderlistner = onfolderlistner;
            checkBox = itemView.findViewById(R.id.rcv_check);
            checkBox.setClickable(false);
            checkBox.setVisibility(View.GONE);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onfolderlistner.onClickListner(getAdapterPosition());
        }
    }

    public interface interfaceCLicklistner
    {
        void onClickListner(int position);
    }
}
