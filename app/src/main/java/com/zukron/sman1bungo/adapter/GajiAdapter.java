package com.zukron.sman1bungo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.zukron.sman1bungo.R;
import com.zukron.sman1bungo.model.Gaji;
import com.zukron.sman1bungo.util.Tools;

import java.util.ArrayList;

public class GajiAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Gaji> gajiList;
    private GajiAdapter.onEditSelectedItem onEditSelectedItem;
    private GajiAdapter.onDeleteSelectedItem onDeleteSelectedItem;

    public GajiAdapter(Context context, ArrayList<Gaji> gajiList, GajiAdapter.onEditSelectedItem onEditSelectedItem, GajiAdapter.onDeleteSelectedItem onDeleteSelectedItem) {
        this.context = context;
        this.gajiList = gajiList;
        this.onEditSelectedItem = onEditSelectedItem;
        this.onDeleteSelectedItem = onDeleteSelectedItem;
    }

    public interface onEditSelectedItem {
        void onEditSelected(Gaji gaji);
    }

    public interface onDeleteSelectedItem {
        void onDeleteSelected(Gaji gaji);
    }

    @Override
    public int getCount() {
        return gajiList.size();
    }

    @Override
    public Object getItem(int position) {
        return gajiList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.item_gaji, parent, false);
        }

        Gaji gaji = (Gaji) getItem(position);
        ViewHolder viewHolder = new ViewHolder(itemView);

        viewHolder.tvIdGajiItem.setText(gaji.getIdGaji());
        viewHolder.tvGajiPokokItem.setText(Tools.toIdr(gaji.getGajiPokok()).toString());
        viewHolder.btnEditGajiItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEditSelectedItem.onEditSelected(gajiList.get(position));
            }
        });

        viewHolder.btnDeleteGajiItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeleteSelectedItem.onDeleteSelected(gajiList.get(position));
            }
        });

        return itemView;
    }

    private class ViewHolder {
        TextView tvIdGajiItem, tvGajiPokokItem;
        Button btnEditGajiItem, btnDeleteGajiItem;

        ViewHolder(View item) {
            tvIdGajiItem = item.findViewById(R.id.tv_id_gaji_item);
            tvGajiPokokItem = item.findViewById(R.id.tv_gaji_pokok_item);

            btnEditGajiItem = item.findViewById(R.id.btn_edit_gaji_item);
            btnDeleteGajiItem = item.findViewById(R.id.btn_delete_gaji_item);
        }
    }
}
