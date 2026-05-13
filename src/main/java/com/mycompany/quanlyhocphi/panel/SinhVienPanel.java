package com.mycompany.quanlyhocphi.panel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class SinhVienPanel extends JPanel {

    public JTable table;
    public JTextField txtMaSV, txtHoTen, txtQueQuan, txtKhoa;
    public JComboBox<String> cbGioiTinh, cbNganh, cbKhoa;
    public JSpinner spNgaySinh;
    public JButton btnAdd, btnUpdate, btnDelete;

    public SinhVienPanel() {
        setLayout(new BorderLayout(10,10));

        // ===== TABLE =====
        table = new JTable(new DefaultTableModel(
            new Object[]{"Mã SV","Họ tên","Giới tính","Ngành","Khoa","Lớp"}, 0
        ));
        add(new JScrollPane(table), BorderLayout.CENTER);

        // ===== FORM =====
        JPanel form = new JPanel(new GridLayout(4,4,8,8));

        txtMaSV = new JTextField();
        txtMaSV.setEnabled(false);
        txtHoTen = new JTextField();
        txtQueQuan = new JTextField();
        txtKhoa = new JTextField();

        cbGioiTinh = new JComboBox<>(new String[]{"Nam","Nữ"});
        cbNganh = new JComboBox<>();
        cbKhoa = new JComboBox<>();

        spNgaySinh = new JSpinner(new SpinnerDateModel());

        form.add(new JLabel("Mã SV")); form.add(txtMaSV);
        form.add(new JLabel("Họ tên")); form.add(txtHoTen);

        form.add(new JLabel("Giới tính")); form.add(cbGioiTinh);
        form.add(new JLabel("Ngày sinh")); form.add(spNgaySinh);

        form.add(new JLabel("Quê quán")); form.add(txtQueQuan);
        form.add(new JLabel("Ngành")); form.add(cbNganh);

        form.add(new JLabel("Khoa")); form.add(cbKhoa);
        form.add(new JLabel("Khóa")); form.add(txtKhoa);

        add(form, BorderLayout.SOUTH);

        // ===== BUTTON =====
        JPanel btnPanel = new JPanel();

        btnAdd = new JButton("Thêm");
        btnUpdate = new JButton("Sửa");
        btnDelete = new JButton("Xóa");

        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);

        add(btnPanel, BorderLayout.NORTH);
    }
}
