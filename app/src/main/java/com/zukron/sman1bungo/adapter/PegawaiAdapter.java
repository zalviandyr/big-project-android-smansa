package com.zukron.sman1bungo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.zukron.sman1bungo.R;
import com.zukron.sman1bungo.model.Pegawai;

import java.util.ArrayList;

public class PegawaiAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Pegawai> pegawaiList;
    private PegawaiAdapter.onEditSelectedItem onEditSelectedItem;
    private PegawaiAdapter.onDeleteSelectedItem onDeleteSelectedItem;

    public PegawaiAdapter(Context context, ArrayList<Pegawai> pegawaiList, PegawaiAdapter.onEditSelectedItem onEditSelectedItem, PegawaiAdapter.onDeleteSelectedItem onDeleteSelectedItem) {
        this.context = context;
        this.pegawaiList = pegawaiList;
        this.onEditSelectedItem = onEditSelectedItem;
        this.onDeleteSelectedItem = onDeleteSelectedItem;
    }

    public interface onEditSelectedItem {
        void onEditSelected(Pegawai pegawai);
    }

    public interface onDeleteSelectedItem {
        void onDeleteSelected(Pegawai pegawai);
    }

    @Override
    public int getCount() {
        return pegawaiList.size();
    }

    @Override
    public Object getItem(int position) {
        return pegawaiList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.item_pegawai, parent, false);
        }

        Pegawai pegawai = (Pegawai) getItem(position);
        ViewHolder viewHolder = new ViewHolder(itemView);

        // data nama
        String namaPegawai = pegawai.getFirstName() + " " + pegawai.getLastName();

        viewHolder.tvNamaPegawaiItem.setText(namaPegawai);
        viewHolder.tvJabatanPegawaiItem.setText(pegawai.getJabatan());
        viewHolder.btnEditPegawaiItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEditSelectedItem.onEditSelected(pegawaiList.get(position));
            }
        });

        viewHolder.btnDeletePegawaiItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeleteSelectedItem.onDeleteSelected(pegawaiList.get(position));
            }
        });

        return itemView;
    }

    private class ViewHolder {
        TextView tvNamaPegawaiItem, tvJabatanPegawaiItem;
        Button btnEditPegawaiItem, btnDeletePegawaiItem;

        ViewHolder(View item) {
            tvNamaPegawaiItem = item.findViewById(R.id.tv_nama_pegawai_item);
            tvJabatanPegawaiItem = item.findViewById(R.id.tv_jabatan_pegawai_item);

            btnEditPegawaiItem = item.findViewById(R.id.btn_edit_pegawai_item);
            btnDeletePegawaiItem = item.findViewById(R.id.btn_delete_pegawai_item);
        }
    }
}
