package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RoomAdapter.OnRoomClickListener {

    private RecyclerView recyclerView;
    private RoomAdapter adapter;
    private List<Room> allRooms;
    private List<Room> displayedRooms;
    private FloatingActionButton fabAdd;
    private TabLayout tabLayout;
    private LinearLayout layoutStatistics;
    private TextView tvTotalRevenue, tvRentedCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initData();
        initViews();
        filterRooms(0); // Mặc định hiển thị tất cả
    }

    private void initData() {
        allRooms = new ArrayList<>();
        allRooms.add(new Room("101", "Phòng 101", 1500000, false, "", "", "", "", "", ""));
        allRooms.add(new Room("102", "Phòng 102", 2000000, true, "Nguyễn Văn A", "0987654321", "15/05/1998", "Nam", "Thái Bình", "12"));
        allRooms.add(new Room("103", "Phòng 103", 1800000, true, "Trần Thị B", "0123456789", "20/10/1995", "Nữ", "Nam Định", "6"));
        
        displayedRooms = new ArrayList<>(allRooms);
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        fabAdd = findViewById(R.id.fabAdd);
        tabLayout = findViewById(R.id.tabLayout);
        layoutStatistics = findViewById(R.id.layoutStatistics);
        tvTotalRevenue = findViewById(R.id.tvTotalRevenue);
        tvRentedCount = findViewById(R.id.tvRentedCount);

        adapter = new RoomAdapter(displayedRooms, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        fabAdd.setOnClickListener(v -> showEditRoomDialog(null, -1));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                filterRooms(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void filterRooms(int tabIndex) {
        displayedRooms.clear();
        if (tabIndex == 0) { // Tất cả
            displayedRooms.addAll(allRooms);
            layoutStatistics.setVisibility(View.GONE);
        } else if (tabIndex == 1) { // Còn trống
            for (Room r : allRooms) {
                if (!r.isRented()) displayedRooms.add(r);
            }
            layoutStatistics.setVisibility(View.GONE);
        } else if (tabIndex == 2) { // Đã thuê
            double totalRevenue = 0;
            int count = 0;
            for (Room r : allRooms) {
                if (r.isRented()) {
                    displayedRooms.add(r);
                    totalRevenue += r.getPrice();
                    count++;
                }
            }
            tvTotalRevenue.setText(String.format("Tổng doanh thu: %,.0f VNĐ", totalRevenue));
            tvRentedCount.setText("Số phòng đang thuê: " + count);
            layoutStatistics.setVisibility(View.VISIBLE);
        }
        adapter.notifyDataSetChanged();
    }

    private void showRoomDetailsDialog(Room room, int positionInDisplayedList) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_room_details, null);
        builder.setView(view);
        builder.setTitle("Chi tiết phòng");

        TextView tvDetId = view.findViewById(R.id.tvDetId);
        TextView tvDetName = view.findViewById(R.id.tvDetName);
        TextView tvDetPrice = view.findViewById(R.id.tvDetPrice);
        TextView tvDetStatus = view.findViewById(R.id.tvDetStatus);
        
        LinearLayout layoutTenantDetails = view.findViewById(R.id.layoutTenantDetails);
        TextView tvDetTenant = view.findViewById(R.id.tvDetTenant);
        TextView tvDetPhone = view.findViewById(R.id.tvDetPhone);
        ImageButton btnCall = view.findViewById(R.id.btnCall);
        TextView tvDetBirthDate = view.findViewById(R.id.tvDetBirthDate);
        TextView tvDetGender = view.findViewById(R.id.tvDetGender);
        TextView tvDetAddress = view.findViewById(R.id.tvDetAddress);
        TextView tvDetRentalTime = view.findViewById(R.id.tvDetRentalTime);

        tvDetId.setText("Mã phòng: " + room.getId());
        tvDetName.setText("Tên phòng: " + room.getName());
        tvDetPrice.setText(String.format("Giá thuê: %,.0f VNĐ", room.getPrice()));
        tvDetStatus.setText("Tình trạng: " + (room.isRented() ? "Đã thuê" : "Còn trống"));
        
        if (room.isRented()) {
            layoutTenantDetails.setVisibility(View.VISIBLE);
            tvDetTenant.setText("Người thuê: " + room.getTenantName());
            tvDetPhone.setText("Số điện thoại: " + room.getPhoneNumber());
            tvDetBirthDate.setText("Ngày sinh: " + room.getBirthDate());
            tvDetGender.setText("Giới tính: " + room.getGender());
            tvDetAddress.setText("Địa chỉ: " + room.getAddress());
            tvDetRentalTime.setText("Thời gian thuê: " + room.getRentalTime() + " tháng");

            // Sự kiện click nút gọi điện
            btnCall.setOnClickListener(v -> {
                String phone = room.getPhoneNumber();
                if (!TextUtils.isEmpty(phone)) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + phone));
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Không có số điện thoại", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            layoutTenantDetails.setVisibility(View.GONE);
        }

        builder.setPositiveButton("Sửa", (dialog, which) -> showEditRoomDialog(room, positionInDisplayedList));
        builder.setNeutralButton("Xóa", (dialog, which) -> confirmDelete(room, positionInDisplayedList));
        builder.setNegativeButton("Đóng", (dialog, which) -> dialog.dismiss());
        
        builder.create().show();
    }

    private void showEditRoomDialog(Room room, int positionInDisplayedList) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_room, null);
        builder.setView(view);

        EditText etRoomId = view.findViewById(R.id.etRoomId);
        EditText etRoomName = view.findViewById(R.id.etRoomName);
        EditText etRoomPrice = view.findViewById(R.id.etRoomPrice);
        CheckBox cbIsRented = view.findViewById(R.id.cbIsRented);
        EditText etTenantName = view.findViewById(R.id.etTenantName);
        EditText etPhoneNumber = view.findViewById(R.id.etPhoneNumber);
        EditText etBirthDate = view.findViewById(R.id.etBirthDate);
        EditText etGender = view.findViewById(R.id.etGender);
        EditText etAddress = view.findViewById(R.id.etAddress);
        EditText etRentalTime = view.findViewById(R.id.etRentalTime);

        if (room != null) {
            builder.setTitle("Cập nhật thông tin");
            etRoomId.setText(room.getId());
            etRoomId.setEnabled(false);
            etRoomName.setText(room.getName());
            etRoomPrice.setText(String.valueOf(room.getPrice()));
            cbIsRented.setChecked(room.isRented());
            etTenantName.setText(room.getTenantName());
            etPhoneNumber.setText(room.getPhoneNumber());
            etBirthDate.setText(room.getBirthDate());
            etGender.setText(room.getGender());
            etAddress.setText(room.getAddress());
            etRentalTime.setText(room.getRentalTime());
        } else {
            builder.setTitle("Thêm phòng mới");
        }

        builder.setPositiveButton("Lưu", (dialog, which) -> {
            String id = etRoomId.getText().toString().trim();
            String name = etRoomName.getText().toString().trim();
            String priceStr = etRoomPrice.getText().toString().trim();
            boolean isRented = cbIsRented.isChecked();
            String tenantName = etTenantName.getText().toString().trim();
            String phoneNumber = etPhoneNumber.getText().toString().trim();
            String birthDate = etBirthDate.getText().toString().trim();
            String gender = etGender.getText().toString().trim();
            String address = etAddress.getText().toString().trim();
            String rentalTime = etRentalTime.getText().toString().trim();

            if (validateData(id, name, priceStr)) {
                double price = Double.parseDouble(priceStr);
                Room newRoom = new Room(id, name, price, isRented, tenantName, phoneNumber, birthDate, gender, address, rentalTime);

                if (room == null) {
                    allRooms.add(newRoom);
                    Toast.makeText(MainActivity.this, "Đã thêm phòng", Toast.LENGTH_SHORT).show();
                } else {
                    // Cập nhật trong list gốc
                    int indexInAll = allRooms.indexOf(room);
                    if (indexInAll != -1) allRooms.set(indexInAll, newRoom);
                    Toast.makeText(MainActivity.this, "Đã cập nhật", Toast.LENGTH_SHORT).show();
                }
                filterRooms(tabLayout.getSelectedTabPosition());
            }
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private boolean validateData(String id, String name, String priceStr) {
        if (TextUtils.isEmpty(id) || TextUtils.isEmpty(name) || TextUtils.isEmpty(priceStr)) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ Mã, Tên và Giá", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void confirmDelete(Room room, int positionInDisplayedList) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa phòng " + room.getName() + "?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    allRooms.remove(room);
                    filterRooms(tabLayout.getSelectedTabPosition());
                    Toast.makeText(MainActivity.this, "Đã xóa", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    @Override
    public void onRoomClick(int position) {
        showRoomDetailsDialog(displayedRooms.get(position), position);
    }

    @Override
    public void onRoomLongClick(int position) {
        confirmDelete(displayedRooms.get(position), position);
    }
}
