package com.mycompany.quanlyhocphi.panel;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class KhoaPanel extends JPanel {

    public JTable table;
    public JTextField txtMaKhoa, txtTenKhoa, txtTim;
    public JButton btnThem, btnSua, btnXoa, btnXuat, btnTim;

    public KhoaPanel() {
        setLayout(null);
        setPreferredSize(new Dimension(1200, 700));
        setBackground(new Color(240, 240, 240)); // NỀN GIỐNG LỚP

        // ===== TIÊU ĐỀ =====
        JLabel title = new JLabel("QUẢN LÝ KHOA", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setBounds(0, 10, 1200, 40);
        add(title);

        // ===== THANH TÌM KIẾM =====
        JLabel lbTim = new JLabel("Tìm kiếm:");
        lbTim.setBounds(30, 60, 80, 25);
        add(lbTim);

        txtTim = new JTextField();
        txtTim.setBounds(110, 60, 400, 25);
        add(txtTim);

        btnTim = new JButton("Tìm");
        btnTim.setBounds(530, 60, 80, 25);
        add(btnTim);

        // ===== TABLE (THU NHỎ LẠI) =====
        table = new JTable(new DefaultTableModel(
                new Object[]{"Mã khoa", "Tên khoa"}, 0
        ));
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(60, 100, 1080, 300);
        add(sp);

        // ===== FORM NHẬP (ĐƯA LÊN TRÊN) =====
        JLabel lbMa = new JLabel("Mã khoa:");
        lbMa.setBounds(30, 420, 100, 25);
        add(lbMa);

        txtMaKhoa = new JTextField();
        txtMaKhoa.setBounds(120, 420, 400, 25);
        add(txtMaKhoa);

        JLabel lbTen = new JLabel("Tên khoa:");
        lbTen.setBounds(30, 460, 100, 25);
        add(lbTen);

        txtTenKhoa = new JTextField();
        txtTenKhoa.setBounds(120, 460, 400, 25);
        add(txtTenKhoa);

        // ===== NÚT CHỨC NĂNG (XÍCH LÊN) =====
        btnThem = new JButton("Thêm");
        btnThem.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnThem.setBounds(30, 510, 100, 35);
        add(btnThem);

        btnSua = new JButton("Sửa");
        btnSua.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSua.setBounds(200, 510, 100, 35);
        add(btnSua);

        btnXoa = new JButton("Xóa");
        btnXoa.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnXoa.setBounds(370, 510, 100, 35);
        add(btnXoa);

        btnXuat = new JButton("Xuất");
        btnXuat.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnXuat.setBounds(540, 510, 100, 35);
        add(btnXuat);
    }
}
