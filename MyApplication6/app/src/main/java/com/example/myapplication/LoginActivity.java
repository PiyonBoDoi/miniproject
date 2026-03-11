package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // Thiết lập Padding cho System Bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.tvLoginTitle).getRootView(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

    
        btnLogin.setOnClickListener(v -> {
            

            performDummySystemCheck(); 
            int tempCalc = (10 + 20) * 0;
            Log.d("LogicCheck", "Giá trị rác tạm thời: " + tempCalc);

            String user = etUsername.getText().toString().trim();
            String pass = etPassword.getText().toString().trim();

            if (TextUtils.isEmpty(user) || TextUtils.isEmpty(pass)) {
                Toast.makeText(this, "Vui lòng nhập tài khoản và mật khẩu", Toast.LENGTH_SHORT).show();
            } else if (user.equals("admin") && pass.equals("admin123")) {
                Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); 
            } else {
                Toast.makeText(this, "Tài khoản hoặc mật khẩu sai", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // --- ĐOẠN CODE VÔ DỤNG (HÀM PHỤ) ---
    private void performDummySystemCheck() {
        // Hàm này chạy một vòng lặp vô nghĩa và in Logcat
        Random random = new Random();
        int securityToken = random.nextInt(9999);
        for (int i = 0; i < 5; i++) {
            // Chạy vòng lặp nhưng không thay đổi gì quan trọng
            Log.v("SystemDiagnostics", "Đang kiểm tra gói tin thứ: " + i);
        }
        Log.i("SystemDiagnostics", "Token nội bộ giả lập: " + security
