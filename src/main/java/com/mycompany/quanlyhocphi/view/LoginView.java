package com.mycompany.quanlyhocphi.view;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {

    public JTextField txtUser;
    public JPasswordField txtPass;
    public JButton btnLogin, btnForget;

    public LoginView() {
        setTitle("Hệ thống QLSV - Đăng nhập");
        setSize(850, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ===== PANEL TRÁI =====
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(52, 152, 219));
        leftPanel.setPreferredSize(new Dimension(350, 0));
        leftPanel.setLayout(new BorderLayout());

        JLabel lblImage = new JLabel();
        lblImage.setHorizontalAlignment(JLabel.CENTER);
        // CHƯA CÓ ẢNH -> bạn add sau
        lblImage.setIcon(new ImageIcon("src/img/login.png"));
        leftPanel.add(lblImage, BorderLayout.CENTER);

        add(leftPanel, BorderLayout.WEST);

        // ===== PANEL PHẢI =====
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("ĐĂNG NHẬP");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(52, 152, 219));

        txtUser = new JTextField();
        txtPass = new JPasswordField();

        btnLogin = new JButton("Đăng nhập");
        btnLogin.setBackground(new Color(52, 152, 219));
        btnLogin.setForeground(Color.WHITE);

        btnForget = new JButton("Quên mật khẩu?");
        btnForget.setBorderPainted(false);
        btnForget.setContentAreaFilled(false);
        btnForget.setForeground(new Color(52, 152, 219));

        gbc.gridx = 0; gbc.gridy = 0;
        rightPanel.add(title, gbc);

        gbc.gridy++;
        rightPanel.add(new JLabel("Tài khoản"), gbc);

        gbc.gridy++;
        rightPanel.add(txtUser, gbc);

        gbc.gridy++;
        rightPanel.add(new JLabel("Mật khẩu"), gbc);

        gbc.gridy++;
        rightPanel.add(txtPass, gbc);

        gbc.gridy++;
        rightPanel.add(btnLogin, gbc);

        gbc.gridy++;
        rightPanel.add(btnForget, gbc);

        add(rightPanel, BorderLayout.CENTER);
    }
}
