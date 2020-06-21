package com.zukron.sman1bungo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.zukron.sman1bungo.R;
import com.zukron.sman1bungo.model.Siswa;
import com.zukron.sman1bungo.model.Kelas;

import java.util.ArrayList;

public class SiswaAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Siswa> siswaList;
    private ArrayList<Kelas> kelasList;
    private SiswaAdapter.onEditSelectedItem onEditSelectedItem;
    private SiswaAdapter.onDeleteSelectedItem onDeleteSelectedItem;

    public SiswaAdapter(Context context, ArrayList<Siswa> siswaList, ArrayList<Kelas> kelasList, SiswaAdapter.onEditSelectedItem onEditSelectedItem, SiswaAdapter.onDeleteSelectedItem onDeleteSelectedItem) {
        this.context = context;
        this.siswaList = siswaList;
        this.kelasList = kelasList;
        this.onEditSelectedItem = onEditSelectedItem;
        this.onDeleteSelectedItem = onDeleteSelectedItem;
    }

    public interface onEditSelectedItem {
        void onEditSelected(Siswa siswa);
    }

    public interface onDeleteSelectedItem {
        void onDeleteSelected(Siswa siswa);
    }

    @Override
    public int getCount() {
        return siswaList.size();
    }

    @Override
    public Object getItem(int position) {
        return siswaList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.item_siswa, parent, false);
        }

        Siswa siswa = (Siswa) getItem(position);
        ViewHolder viewHolder = new ViewHolder(itemView);

        // data nama
        String namaSiswa = siswa.getFirstName() + " " + siswa.getLastName();
        // data kelas
        String kelasSiswa = "";
        for (Kelas kelas : kelasList) {
            // mencari id yang sama
            if (kelas.getIdKelas().equals(siswa.getIdKelas()))
                kelasSiswa = kelas.getNama();
        }

        viewHolder.tvNamaSiswaItem.setText(namaSiswa);
        viewHolder.tvNisnSiswaItem.setText(siswa.getNisn());
        viewHolder.tvKelasSiswaItem.setText(kelasSiswa);
        viewHolder.btnEditSiswaItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEditSelectedItem.onEditSelected(siswaList.get(position));
            }
        });

        viewHolder.btnDeleteSiswaItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeleteSelectedItem.onDeleteSelected(siswaList.get(position));
            }
        });

        return itemView;
    }

    private class ViewHolder {
        TextView tvNamaSiswaItem, tvNisnSiswaItem, tvKelasSiswaItem;
        Button btnEditSiswaItem, btnDeleteSiswaItem;

        ViewHolder(View item) {
            tvNamaSiswaItem = item.findViewById(R.id.tv_nama_siswa_item);
            tvNisnSiswaItem = item.findViewById(R.id.tv_nisn_siswa_item);
            tvKelasSiswaItem = item.findViewById(R.id.tv_kelas_siswa_item);

            btnEditSiswaItem = item.findViewById(R.id.btn_edit_siswa_item);
            btnDeleteSiswaItem = item.findViewById(R.id.btn_delete_siswa_item);
        }
    }
}
