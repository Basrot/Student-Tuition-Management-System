package com.mycompany.quanlyhocphi.panel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MienGiam_panel extends JPanel {

    // ===== Đối tượng miễn giảm =====
    public JTextField txtMaDT, txtTenDT, txtTyLeGiam;
    public JButton btnThem, btnSua, btnXoa;

    public JTable tblDoiTuong;
    public DefaultTableModel modelDT;

    // ===== Gán cho sinh viên =====
    public JTextField txtMaSV, txtTenSV;
    public JComboBox<String> cbDoiTuong;
    public JButton btnGan, btnHuyGan, btnXuat;

    public JTable tblSinhVien;
    public DefaultTableModel modelSV;

    public MienGiam_panel() {
        setLayout(new BorderLayout(10, 10));

        /* ================== PANEL TRÁI: ĐỐI TƯỢNG ================== */
        JPanel left = new JPanel(new BorderLayout(5, 5));
        left.setPreferredSize(new Dimension(400, 0));

        JPanel form = new JPanel(new GridLayout(4, 2, 5, 5));
        form.setBorder(BorderFactory.createTitledBorder("Đối tượng miễn giảm"));

        form.add(new JLabel("Mã đối tượng:"));
        txtMaDT = new JTextField();
        form.add(txtMaDT);

        form.add(new JLabel("Tên đối tượng:"));
        txtTenDT = new JTextField();
        form.add(txtTenDT);

        form.add(new JLabel("Tỷ lệ giảm (0 → 1):"));
        txtTyLeGiam = new JTextField();
        form.add(txtTyLeGiam);

        JPanel btnPanel = new JPanel();
        btnThem = new JButton("Thêm");
        btnSua = new JButton("Sửa");
        btnXoa = new JButton("Xóa");
        btnPanel.add(btnThem);
        btnPanel.add(btnSua);
        btnPanel.add(btnXoa);

        left.add(form, BorderLayout.NORTH);
        left.add(btnPanel, BorderLayout.CENTER);

        modelDT = new DefaultTableModel(
                new String[]{"Mã DT", "Tên đối tượng", "Tỷ lệ giảm"}, 0);
        tblDoiTuong = new JTable(modelDT);
        left.add(new JScrollPane(tblDoiTuong), BorderLayout.SOUTH);

        /* ================== PANEL PHẢI: SINH VIÊN ================== */
        JPanel right = new JPanel(new BorderLayout(5, 5));

        JPanel topSV = new JPanel(new GridLayout(3, 2, 5, 5));
        topSV.setBorder(BorderFactory.createTitledBorder("Gán miễn giảm cho sinh viên"));

        topSV.add(new JLabel("Mã SV:"));
        txtMaSV = new JTextField();
        topSV.add(txtMaSV);

        topSV.add(new JLabel("Họ tên:"));
        txtTenSV = new JTextField();
        txtTenSV.setEditable(false);
        topSV.add(txtTenSV);

        topSV.add(new JLabel("Đối tượng:"));
        cbDoiTuong = new JComboBox<>();
        topSV.add(cbDoiTuong);

        JPanel btnSV = new JPanel();
        btnGan = new JButton("Gán");
        btnHuyGan = new JButton("Hủy gán");
        btnXuat = new JButton("Xuất DS miễn giảm");
        btnSV.add(btnGan);
        btnSV.add(btnHuyGan);
        btnSV.add(btnXuat);

        modelSV = new DefaultTableModel(
                new String[]{"Mã SV", "Họ tên", "Đối tượng", "Tỷ lệ giảm"}, 0);
        tblSinhVien = new JTable(modelSV);

        right.add(topSV, BorderLayout.NORTH);
        right.add(btnSV, BorderLayout.CENTER);
        right.add(new JScrollPane(tblSinhVien), BorderLayout.SOUTH);

        add(left, BorderLayout.WEST);
        add(right, BorderLayout.CENTER);
    }
}
