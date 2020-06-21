package com.zukron.sman1bungo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.zukron.sman1bungo.R;
import com.zukron.sman1bungo.model.Guru;
import com.zukron.sman1bungo.model.Kelas;

import java.util.ArrayList;

public class KelasAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Kelas> kelasList;
    private ArrayList<Guru> guruList;
    private KelasAdapter.onEditSelectedItem onEditSelectedItem;
    private KelasAdapter.onDeleteSelectedItem onDeleteSelectedItem;

    public KelasAdapter(Context context, ArrayList<Kelas> kelasList, ArrayList<Guru> guruList, KelasAdapter.onEditSelectedItem onEditSelectedItem, KelasAdapter.onDeleteSelectedItem onDeleteSelectedItem) {
        this.context = context;
        this.kelasList = kelasList;
        this.guruList = guruList;
        this.onEditSelectedItem = onEditSelectedItem;
        this.onDeleteSelectedItem = onDeleteSelectedItem;
    }

    public interface onEditSelectedItem {
        void onEditSelected(Kelas kelas);
    }

    public interface onDeleteSelectedItem {
        void onDeleteSelected(Kelas kelas);
    }

    @Override
    public int getCount() {
        return kelasList.size();
    }

    @Override
    public Object getItem(int position) {
        return kelasList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.item_kelas, parent, false);
        }

        Kelas kelas = (Kelas) getItem(position);
        ViewHolder viewHolder = new ViewHolder(itemView);

        viewHolder.tvIdKelasItem.setText(kelas.getIdKelas());
        viewHolder.tvNamaKelasItem.setText(kelas.getNama());
        // mencari nama guru yang menjadi wali kelas
        for (Guru guru : guruList) {
            if (guru.getNip().equals(kelas.getWaliKelas())){
                String namaWaliKelas = guru.getFirstName() + " " + guru.getLastName();
                viewHolder.tvWaliKelasItem.setText(namaWaliKelas);
            }
        }

        viewHolder.btnEditKelasItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEditSelectedItem.onEditSelected(kelasList.get(position));
            }
        });

        viewHolder.btnDeleteKelasItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeleteSelectedItem.onDeleteSelected(kelasList.get(position));
            }
        });

        return itemView;
    }

    private class ViewHolder {
        TextView tvIdKelasItem, tvNamaKelasItem, tvWaliKelasItem;
        Button btnEditKelasItem, btnDeleteKelasItem;

        ViewHolder(View item) {
            tvIdKelasItem = item.findViewById(R.id.tv_id_kelas_item);
            tvNamaKelasItem = item.findViewById(R.id.tv_nama_kelas_item);
            tvWaliKelasItem = item.findViewById(R.id.tv_wali_kelas_item);

            btnEditKelasItem = item.findViewById(R.id.btn_edit_kelas_item);
            btnDeleteKelasItem = item.findViewById(R.id.btn_delete_kelas_item);
        }
    }
}
