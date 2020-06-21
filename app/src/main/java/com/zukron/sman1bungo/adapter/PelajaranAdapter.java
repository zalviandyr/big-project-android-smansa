package com.zukron.sman1bungo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.zukron.sman1bungo.R;
import com.zukron.sman1bungo.model.Pelajaran;

import java.util.ArrayList;

public class PelajaranAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Pelajaran> pelajaranList;
    private PelajaranAdapter.onEditSelectedItem onEditSelectedItem;
    private PelajaranAdapter.onDeleteSelectedItem onDeleteSelectedItem;

    public PelajaranAdapter(Context context, ArrayList<Pelajaran> pelajaranList, PelajaranAdapter.onEditSelectedItem onEditSelectedItem, PelajaranAdapter.onDeleteSelectedItem onDeleteSelectedItem) {
        this.context = context;
        this.pelajaranList = pelajaranList;
        this.onEditSelectedItem = onEditSelectedItem;
        this.onDeleteSelectedItem = onDeleteSelectedItem;
    }

    public interface onEditSelectedItem {
        void onEditSelected(Pelajaran pelajaran);
    }

    public interface onDeleteSelectedItem {
        void onDeleteSelected(Pelajaran pelajaran);
    }

    @Override
    public int getCount() {
        return pelajaranList.size();
    }

    @Override
    public Object getItem(int position) {
        return pelajaranList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.item_pelajaran, parent, false);
        }

        Pelajaran pelajaran = (Pelajaran) getItem(position);
        ViewHolder viewHolder = new ViewHolder(itemView);

        viewHolder.tvIdPelajaranItem.setText(pelajaran.getIdPelajaran());
        viewHolder.tvNamaPelajaranItem.setText(pelajaran.getNama());
        viewHolder.btnEditPelajaranItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEditSelectedItem.onEditSelected(pelajaranList.get(position));
            }
        });

        viewHolder.btnDeletePelajaranItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeleteSelectedItem.onDeleteSelected(pelajaranList.get(position));
            }
        });

        return itemView;
    }

    private class ViewHolder {
        TextView tvIdPelajaranItem, tvNamaPelajaranItem;
        Button btnEditPelajaranItem, btnDeletePelajaranItem;

        ViewHolder(View item) {
            tvIdPelajaranItem = item.findViewById(R.id.tv_id_pelajaran_item);
            tvNamaPelajaranItem = item.findViewById(R.id.tv_nama_pelajaran_item);

            btnEditPelajaranItem = item.findViewById(R.id.btn_edit_pelajaran_item);
            btnDeletePelajaranItem = item.findViewById(R.id.btn_delete_pelajaran_item);
        }
    }
}
