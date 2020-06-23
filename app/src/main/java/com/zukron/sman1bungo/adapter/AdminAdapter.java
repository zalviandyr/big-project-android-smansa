package com.zukron.sman1bungo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.zukron.sman1bungo.R;
import com.zukron.sman1bungo.model.Admin;

import java.util.ArrayList;

public class AdminAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Admin> adminList;
    private AdminAdapter.onEditSelectedItem onEditSelectedItem;
    private AdminAdapter.onDeleteSelectedItem onDeleteSelectedItem;

    public AdminAdapter(Context context, ArrayList<Admin> adminList, AdminAdapter.onEditSelectedItem onEditSelectedItem, AdminAdapter.onDeleteSelectedItem onDeleteSelectedItem) {
        this.context = context;
        this.adminList = adminList;
        this.onEditSelectedItem = onEditSelectedItem;
        this.onDeleteSelectedItem = onDeleteSelectedItem;
    }

    public interface onEditSelectedItem {
        void onEditSelected(Admin admin);
    }

    public interface onDeleteSelectedItem {
        void onDeleteSelected(Admin admin);
    }

    @Override
    public int getCount() {
        return adminList.size();
    }

    @Override
    public Object getItem(int position) {
        return adminList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.item_admin, parent, false);
        }

        Admin admin = (Admin) getItem(position);
        ViewHolder viewHolder = new ViewHolder(itemView);

        // data nama
        String namaAdmin = admin.getFirstName() + " " + admin.getLastName();

        viewHolder.tvNamaAdminItem.setText(namaAdmin);
        viewHolder.btnEditAdminItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEditSelectedItem.onEditSelected(adminList.get(position));
            }
        });

        viewHolder.btnDeleteAdminItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeleteSelectedItem.onDeleteSelected(adminList.get(position));
            }
        });

        return itemView;
    }

    private class ViewHolder {
        TextView tvNamaAdminItem;
        Button btnEditAdminItem, btnDeleteAdminItem;

        ViewHolder(View item) {
            tvNamaAdminItem = item.findViewById(R.id.tv_nama_admin_item);

            btnEditAdminItem = item.findViewById(R.id.btn_edit_admin_item);
            btnDeleteAdminItem = item.findViewById(R.id.btn_delete_admin_item);
        }
    }
}
