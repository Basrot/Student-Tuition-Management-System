package com.mycompany.quanlyhocphi.panel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class LopHocPhan_panel extends JPanel {

    public JComboBox<String> cbKhoa, cbNganh, cbKhoaHoc, cbLop, cbHocKy, cbMon, cbGV;
    public JTextField txtMaLHP, txtTenHP, txtSTC, txtTim;
    public JButton btnTao, btnXoa, btnTim, btnXuat;

    public JTable table;      // Lớp học phần
    public JTable tableSV;    // Sinh viên
    public DefaultTableModel model, modelSV;

    public LopHocPhan_panel() {
        setLayout(null);
        setPreferredSize(new Dimension(1100, 650));
        
 JLabel title = new JLabel("QUẢN LÝ LỚP HỌC PHẦN", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setBounds(0, 5, 1100, 30);
        add(title);

        // ===== TÌM KIẾM (NGAY DƯỚI TIÊU ĐỀ) =====
        JLabel lbTim = new JLabel("Tìm kiếm:");
        lbTim.setBounds(360, 40, 70, 25);
        add(lbTim);

        txtTim = new JTextField();
        txtTim.setBounds(430, 40, 220, 25);
        add(txtTim);

        btnTim = new JButton("Tìm");
        btnTim.setBounds(660, 40, 80, 25);
        add(btnTim);

        // ===== FORM =====
        int x1 = 30, y = 70;

        addLabel("Khoa", x1, y += 30); cbKhoa = addCombo(x1, y);
        addLabel("Ngành", x1, y += 30); cbNganh = addCombo(x1, y);
        addLabel("Khóa", x1, y += 30); cbKhoaHoc = addCombo(x1, y);
        addLabel("Lớp", x1, y += 30); cbLop = addCombo(x1, y);
        addLabel("Học kỳ", x1, y += 30); cbHocKy = addCombo(x1, y);
        addLabel("Môn học", x1, y += 30); cbMon = addCombo(x1, y);
        addLabel("Giảng viên", x1, y += 30); cbGV = addCombo(x1, y);

        addLabel("Mã LHP", x1, y += 30);
        txtMaLHP = new JTextField();
        txtMaLHP.setEditable(false);
        txtMaLHP.setBounds(x1 + 100, y, 200, 25);
        add(txtMaLHP);

        addLabel("Tên HP", x1, y += 30);
        txtTenHP = new JTextField();
        txtTenHP.setBounds(x1 + 100, y, 200, 25);
        add(txtTenHP);

        addLabel("Số TC", x1, y += 30);
        txtSTC = new JTextField();
        txtSTC.setEditable(false);
        txtSTC.setBounds(x1 + 100, y, 200, 25);
        add(txtSTC);

        // ===== BUTTON =====
        btnTao = new JButton("Tạo LHP");
        btnTao.setBounds(30, y += 40, 120, 35);
        add(btnTao);

        btnXuat = new JButton("Xuất");
        btnXuat.setBounds(155, y, 90, 35);
        add(btnXuat);

        btnXoa = new JButton("Xóa");
        btnXoa.setBounds(250, y, 90, 35);
        add(btnXoa);

        // ===== TABLE LỚP HỌC PHẦN =====
        model = new DefaultTableModel(
            new String[]{"Mã LHP", "Tên HP", "Lớp", "GV", "Sĩ số"}, 0
        );
        table = new JTable(model);
        JScrollPane spLHP = new JScrollPane(table);
        spLHP.setBounds(360, 70, 700, 250);
        add(spLHP);

        // ===== TABLE SINH VIÊN =====
        modelSV = new DefaultTableModel(
            new String[]{"Mã SV", "Họ tên", "Lớp", "Ngày ĐK", "Trạng thái"}, 0
        );
        tableSV = new JTable(modelSV);
        JScrollPane spSV = new JScrollPane(tableSV);
        spSV.setBounds(360, 340, 700, 250);
        add(spSV);
    }

    // ================= UTIL =================
    private void addLabel(String t, int x, int y) {
        JLabel lb = new JLabel(t);
        lb.setBounds(x, y, 100, 25);
        add(lb);
    }

    private JComboBox<String> addCombo(int x, int y) {
        JComboBox<String> cb = new JComboBox<>();
        cb.setBounds(x + 100, y, 200, 25);
        add(cb);
        return cb;
    }

}
