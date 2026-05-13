package com.mycompany.quanlyhocphi.view;

import javax.swing.*;
import java.awt.*;

public class ForgetPasswordView extends JFrame {

    public JTextField txtUser;
    public JTextField txtOTP;
    public JPasswordField txtNewPass;
    public JButton btnSendOTP;
    public JButton btnVerifyOTP;
    public JButton btnUpdatePass;
    public JButton btnBack;

    public ForgetPasswordView() {
        setTitle("Khôi phục mật khẩu");
        setSize(500, 380);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // ===== TITLE =====
        JLabel lblTitle = new JLabel("KHÔI PHỤC MẬT KHẨU", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(new Color(33, 150, 243));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));
        add(lblTitle, BorderLayout.NORTH);

        // ===== CENTER =====
        JPanel center = new JPanel(new GridBagLayout());
        center.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ===== USERNAME =====
        gbc.gridx = 0; gbc.gridy = 0;
        center.add(new JLabel("Tài khoản:"), gbc);

        txtUser = new JTextField(12); // 👈 nhỏ gọn
        gbc.gridx = 1;
        center.add(txtUser, gbc);

        btnSendOTP = new JButton("OTP");
        btnSendOTP.setPreferredSize(new Dimension(70, 26));
        gbc.gridx = 2;
        center.add(btnSendOTP, gbc);

        // ===== OTP =====
        gbc.gridx = 0; gbc.gridy++;
        center.add(new JLabel("OTP:"), gbc);

        txtOTP = new JTextField(8); // 👈 bé xinh 😆
        gbc.gridx = 1;
        center.add(txtOTP, gbc);

        btnVerifyOTP = new JButton("Verify");
        btnVerifyOTP.setPreferredSize(new Dimension(70, 26));
        gbc.gridx = 2;
        center.add(btnVerifyOTP, gbc);

        // ===== NEW PASSWORD =====
        gbc.gridx = 0; gbc.gridy++;
        center.add(new JLabel("Mật khẩu mới:"), gbc);

        txtNewPass = new JPasswordField(15);
        txtNewPass.setEnabled(false);
        gbc.gridx = 1; gbc.gridwidth = 2;
        center.add(txtNewPass, gbc);

        // ===== UPDATE BUTTON =====
        btnUpdatePass = new JButton("Cập nhật mật khẩu");
        btnUpdatePass.setEnabled(false);
        btnUpdatePass.setBackground(new Color(33, 150, 243));
        btnUpdatePass.setForeground(Color.WHITE);
        btnUpdatePass.setFocusPainted(false);

        gbc.gridx = 1; gbc.gridy++; gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 6, 6, 6);
        center.add(btnUpdatePass, gbc);

        add(center, BorderLayout.CENTER);

        // ===== BOTTOM =====
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnBack = new JButton("← Quay lại");
        bottom.add(btnBack);
        add(bottom, BorderLayout.SOUTH);
    }
}