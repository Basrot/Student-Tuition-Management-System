package com.mycompany.quanlyhocphi.panel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.Dimension;

public class DangKyHocPhanPanel extends JPanel {

    public JComboBox<String> cbKhoa, cbNganh, cbKhoaHoc, cbLop;
    public JComboBox<String> cbHocKy, cbMon, cbGV;
    public JTextField txtMaLHP, txtTenHP, txtSTC;
    public JButton btnTaoLHP, btnXoa;
    public JTable table;
    public DefaultTableModel model;

    public DangKyHocPhanPanel() {
        setLayout(null);
        setPreferredSize(new Dimension(1100, 600));

        int x1 = 30, x2 = 160, y = 30, h = 25;

        add(label("Khoa", x1, y));
        cbKhoa = combo(x2, y); add(cbKhoa); y+=40;

        add(label("Ngành", x1, y));
        cbNganh = combo(x2, y); add(cbNganh); y+=40;

        add(label("Khóa", x1, y));
        cbKhoaHoc = combo(x2, y); add(cbKhoaHoc); y+=40;

        add(label("Lớp", x1, y));
        cbLop = combo(x2, y); add(cbLop); y+=40;

        add(label("Học kỳ", x1, y));
        cbHocKy = combo(x2, y); add(cbHocKy); y+=40;

        add(label("Môn học", x1, y));
        cbMon = combo(x2, y); add(cbMon); y+=40;

        add(label("Giảng viên", x1, y));
        cbGV = combo(x2, y); add(cbGV); y+=40;

        add(label("Mã LHP", x1, y));
        txtMaLHP = text(x2, y); add(txtMaLHP); y+=40;

        add(label("Tên HP", x1, y));
        txtTenHP = text(x2, y); add(txtTenHP); y+=40;

        add(label("Số TC", x1, y));
        txtSTC = text(x2, y); txtSTC.setEditable(false); add(txtSTC); y+=50;

        btnTaoLHP = new JButton("Tạo lớp học phần");
        btnTaoLHP.setBounds(30, y, 160, 35);
        add(btnTaoLHP);

        btnXoa = new JButton("Xóa");
        btnXoa.setBounds(200, y, 120, 35);
        add(btnXoa);

        model = new DefaultTableModel(
            new String[]{"Mã LHP","Tên HP","Lớp","Học kỳ","Giảng viên","Sĩ số"}, 0
        );
        table = new JTable(model);
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(360, 30, 700, 520);
        add(sp);
    }

    private JLabel label(String t, int x, int y) {
        JLabel l = new JLabel(t);
        l.setBounds(x, y, 120, 25);
        return l;
    }

    private JComboBox<String> combo(int x, int y) {
        JComboBox<String> c = new JComboBox<>();
        c.setBounds(x, y, 180, 25);
        return c;
    }

    private JTextField text(int x, int y) {
        JTextField t = new JTextField();
        t.setBounds(x, y, 180, 25);
        return t;
    }
}
