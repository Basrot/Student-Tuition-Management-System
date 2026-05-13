package com.mycompany.quanlyhocphi.panel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class Lop_panel extends JPanel {

    public JTextField txtML, txtTL, txtSS;
    public JButton btnThem, btnSua, btnXoa, btnXuat;
    public JComboBox<String> cbKHOA, cbNganh;
    public JTable table, tableSV;
    public DefaultTableModel model, modelSV;

    public Lop_panel() {
        setLayout(null);
        setPreferredSize(new Dimension(1000, 600));

        JLabel title = new JLabel("QUẢN LÝ LỚP", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setBounds(0, 0, 1000, 30);
        add(title);

        JLabel lbML = new JLabel("Mã lớp");
        lbML.setBounds(30, 40, 100, 25);
        add(lbML);

        txtML = new JTextField();
        txtML.setBounds(140, 40, 200, 25);
        add(txtML);

        JLabel lbTL = new JLabel("Tên lớp");
        lbTL.setBounds(30, 80, 100, 25);
        add(lbTL);

        txtTL = new JTextField();
        txtTL.setBounds(140, 80, 200, 25);
        add(txtTL);

        JLabel lbKhoa = new JLabel("Khoa");
        lbKhoa.setBounds(30, 120, 100, 25);
        add(lbKhoa);

        cbKHOA = new JComboBox<>();
        cbKHOA.setBounds(140, 120, 200, 25);
        add(cbKHOA);

        JLabel lbNganh = new JLabel("Ngành");
        lbNganh.setBounds(30, 160, 100, 25);
        add(lbNganh);

        cbNganh = new JComboBox<>();
        cbNganh.setBounds(140, 160, 200, 25);
        add(cbNganh);

        JLabel lbSS = new JLabel("Sĩ số");
        lbSS.setBounds(30, 200, 100, 25);
        add(lbSS);

        txtSS = new JTextField();
        txtSS.setBounds(140, 200, 200, 25);
        add(txtSS);

        btnThem = new JButton("Thêm");
        btnThem.setBounds(30, 250, 100, 30);
        add(btnThem);

        btnSua = new JButton("Sửa");
        btnSua.setBounds(140, 250, 100, 30);
        add(btnSua);

        btnXoa = new JButton("Xóa");
        btnXoa.setBounds(30, 290, 100, 30);
        add(btnXoa);

        btnXuat = new JButton("Xuất Excel");
        btnXuat.setBounds(140, 290, 120, 30);
        add(btnXuat);

        // ===== BẢNG LỚP =====
        model = new DefaultTableModel(
                new String[]{"Mã lớp", "Tên lớp", "Khoa", "Ngành", "Sĩ số"}, 0
        );
        table = new JTable(model);
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(380, 40, 580, 250);
        add(sp);

        // ===== LABEL SINH VIÊN =====
        JLabel lbSV = new JLabel("Danh sách sinh viên của lớp");
        lbSV.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbSV.setBounds(380, 300, 300, 25);
        add(lbSV);

        // ===== BẢNG SINH VIÊN =====
        modelSV = new DefaultTableModel(
                new String[]{"Mã SV", "Họ tên", "Giới tính", "Email"}, 0
        );
        tableSV = new JTable(modelSV);
        JScrollPane spSV = new JScrollPane(tableSV);
        spSV.setBounds(380, 330, 580, 200);
        add(spSV);
    }
}
