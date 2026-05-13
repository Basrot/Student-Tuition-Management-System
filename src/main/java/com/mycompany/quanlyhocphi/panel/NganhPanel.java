package com.mycompany.quanlyhocphi.panel;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class NganhPanel extends JPanel {

    public JTable table;
    public JTextField txtMa, txtTen, txtVietTat, txtTim;
    public JComboBox<String> cbKhoa;
    public JButton btnThem, btnSua, btnXoa, btnXuat, btnTim;

    public NganhPanel() {
        setLayout(null);
        setPreferredSize(new Dimension(1200, 700));
        setBackground(new Color(200, 230, 240));

        // ===== TIÊU ĐỀ =====
        JLabel title = new JLabel("QUẢN LÝ NGÀNH", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setBounds(0, 10, 1200, 40);
        add(title);

        // ===== TÌM KIẾM =====
        JLabel lbTim = new JLabel("Tìm kiếm:");
        lbTim.setBounds(30, 60, 80, 25);
        add(lbTim);

        txtTim = new JTextField();
        txtTim.setBounds(110, 60, 300, 25);
        add(txtTim);

        btnTim = new JButton("Tìm");
        btnTim.setBounds(430, 60, 80, 25);
        add(btnTim);

        // ===== TABLE =====
        table = new JTable(new DefaultTableModel(
                new Object[]{"Mã ngành", "Tên ngành", "Viết tắt", "Mã khoa"}, 0
        ));
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(20, 100, 1160, 380);
        add(sp);

        // ===== FORM =====
        JLabel lbMa = new JLabel("Mã ngành:");
        lbMa.setBounds(30, 510, 100, 25);
        add(lbMa);

        txtMa = new JTextField();
        txtMa.setBounds(120, 510, 300, 25);
        add(txtMa);

        JLabel lbTen = new JLabel("Tên ngành:");
        lbTen.setBounds(30, 550, 100, 25);
        add(lbTen);

        txtTen = new JTextField();
        txtTen.setBounds(120, 550, 300, 25);
        add(txtTen);

        JLabel lbVT = new JLabel("Viết tắt:");
        lbVT.setBounds(450, 510, 100, 25);
        add(lbVT);

        txtVietTat = new JTextField();
        txtVietTat.setBounds(530, 510, 150, 25);
        txtVietTat.setEditable(true);
        add(txtVietTat);

        JLabel lbKhoa = new JLabel("Khoa:");
        lbKhoa.setBounds(450, 550, 100, 25);
        add(lbKhoa);

        cbKhoa = new JComboBox<>();
        cbKhoa.setBounds(530, 550, 300, 25);
        add(cbKhoa);

        // ===== BUTTON =====
        btnThem = new JButton("Thêm");
        btnThem.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnThem.setBounds(30, 600, 100, 35);
        add(btnThem);

        btnSua = new JButton("Sửa");
        btnSua.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSua.setBounds(160, 600, 100, 35);
        add(btnSua);

        btnXoa = new JButton("Xóa");
        btnXoa.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnXoa.setBounds(290, 600, 100, 35);
        add(btnXoa);

        btnXuat = new JButton("Xuất");
        btnXuat.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnXuat.setBounds(420, 600, 100, 35);
        add(btnXuat);

        // ===== GIỚI HẠN VIẾT TẮT: 2 CHỮ CÁI =====
        txtVietTat.addKeyListener(new java.awt.event.KeyAdapter() {

            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                char c = e.getKeyChar();

                // chỉ cho nhập chữ
                if (!Character.isLetter(c)) {
                    e.consume();
                    return;
                }

                // tối đa 2 ký tự
                if (txtVietTat.getText().length() >= 2) {
                    e.consume();
                }
            }

            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                txtVietTat.setText(txtVietTat.getText().toUpperCase());
            }
        });
    }
}
