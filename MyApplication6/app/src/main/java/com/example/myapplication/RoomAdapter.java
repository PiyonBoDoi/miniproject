package com.example.myapplication;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {

    private List<Room> roomList;
    private OnRoomClickListener listener;

    public interface OnRoomClickListener {
        void onRoomClick(int position);
        void onRoomLongClick(int position);
    }

    public RoomAdapter(List<Room> roomList, OnRoomClickListener listener) {
        this.roomList = roomList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        Room room = roomList.get(position);
        holder.tvRoomName.setText(room.getName());
        holder.tvRoomPrice.setText(String.format("%,.0f VNĐ", room.getPrice()));
        
        int color;
        String statusText;
        if (room.isRented()) {
            color = Color.parseColor("#E53935"); // Red
            statusText = "ĐÃ THUÊ";
            holder.tvTenantSummary.setText("Người thuê: " + room.getTenantName());
            holder.tvTenantSummary.setVisibility(View.VISIBLE);
        } else {
            color = Color.parseColor("#43A047"); // Green
            statusText = "TRỐNG";
            holder.tvTenantSummary.setText("Chưa có người thuê");
            holder.tvTenantSummary.setVisibility(View.GONE);
        }

        holder.statusIndicator.setBackgroundColor(color);
        holder.tvRoomStatusLabel.setText(statusText);
        
        // Tạo background bo góc cho label trạng thái
        GradientDrawable shape = new GradientDrawable();
        shape.setCornerRadius(8);
        shape.setColor(color);
        holder.tvRoomStatusLabel.setBackground(shape);

        holder.itemView.setOnClickListener(v -> listener.onRoomClick(position));
        holder.itemView.setOnLongClickListener(v -> {
            listener.onRoomLongClick(position);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    public static class RoomViewHolder extends RecyclerView.ViewHolder {
        TextView tvRoomName, tvRoomPrice, tvTenantSummary, tvRoomStatusLabel;
        View statusIndicator;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRoomName = itemView.findViewById(R.id.tvRoomName);
            tvRoomPrice = itemView.findViewById(R.id.tvRoomPrice);
            tvTenantSummary = itemView.findViewById(R.id.tvTenantSummary);
            tvRoomStatusLabel = itemView.findViewById(R.id.tvRoomStatusLabel);
            statusIndicator = itemView.findViewById(R.id.statusIndicator);
        }
    }
}
