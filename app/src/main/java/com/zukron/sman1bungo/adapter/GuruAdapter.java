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
import com.zukron.sman1bungo.model.Pelajaran;

import java.util.ArrayList;

public class GuruAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Guru> guruList;
    private ArrayList<Pelajaran> pelajaranList;
    private GuruAdapter.onEditSelectedItem onEditSelectedItem;
    private GuruAdapter.onDeleteSelectedItem onDeleteSelectedItem;

    public GuruAdapter(Context context, ArrayList<Guru> guruList, ArrayList<Pelajaran> pelajaranList, GuruAdapter.onEditSelectedItem onEditSelectedItem, GuruAdapter.onDeleteSelectedItem onDeleteSelectedItem) {
        this.context = context;
        this.guruList = guruList;
        this.pelajaranList = pelajaranList;
        this.onEditSelectedItem = onEditSelectedItem;
        this.onDeleteSelectedItem = onDeleteSelectedItem;
    }

    public interface onEditSelectedItem {
        void onEditSelected(Guru guru);
    }

    public interface onDeleteSelectedItem {
        void onDeleteSelected(Guru guru);
    }

    @Override
    public int getCount() {
        return guruList.size();
    }

    @Override
    public Object getItem(int position) {
        return guruList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.item_guru, parent, false);
        }

        Guru guru = (Guru) getItem(position);
        ViewHolder viewHolder = new ViewHolder(itemView);

        // data nama
        String namaGuru = guru.getFirstName() + " " + guru.getLastName();
        // data mata pelajaran
        String mataPelajaran = "";
        for (Pelajaran pelajaran : pelajaranList) {
            // mencari id yang sama
            if (pelajaran.getIdPelajaran().equals(guru.getIdPelajaran()))
                mataPelajaran = pelajaran.getNama();
        }

        viewHolder.tvNamaGuruItem.setText(namaGuru);
        viewHolder.tvNipGuruItem.setText(guru.getNip());
        viewHolder.tvMataPelajaranGuruItem.setText(mataPelajaran);
        viewHolder.tvGolonganGuruItem.setText(guru.getGolongan());
        viewHolder.btnEditGuruItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEditSelectedItem.onEditSelected(guruList.get(position));
            }
        });

        viewHolder.btnDeleteGuruItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeleteSelectedItem.onDeleteSelected(guruList.get(position));
            }
        });

        return itemView;
    }

    private class ViewHolder {
        TextView tvNamaGuruItem, tvNipGuruItem, tvMataPelajaranGuruItem, tvGolonganGuruItem;
        Button btnEditGuruItem, btnDeleteGuruItem;

        ViewHolder(View item) {
            tvNamaGuruItem = item.findViewById(R.id.tv_nama_guru_item);
            tvNipGuruItem = item.findViewById(R.id.tv_nip_guru_item);
            tvMataPelajaranGuruItem = item.findViewById(R.id.tv_mata_pelajaran_guru_item);
            tvGolonganGuruItem = item.findViewById(R.id.tv_golongan_guru_item);

            btnEditGuruItem = item.findViewById(R.id.btn_edit_guru_item);
            btnDeleteGuruItem = item.findViewById(R.id.btn_delete_guru_item);
        }
    }
}
