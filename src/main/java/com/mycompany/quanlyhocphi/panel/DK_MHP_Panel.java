/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlyhocphi.panel;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author Admin
 */
public class DK_MHP_Panel extends JPanel {
      public JTextField txtMa, txtTen, txtSoTC, txtTim;
      public JComboBox<String> cbKHOA, cbNganh;
    public JButton btnThem, btnSua, btnXoa, btnXuat, btnTim;
    public JTable table;
    public DefaultTableModel model;
    
   public DK_MHP_Panel() {
    setLayout(null);
    setPreferredSize(new Dimension(1000, 600));
      // ================= TIÊU ĐỀ =================
        JLabel lbTitle = new JLabel("QUẢN LÝ MÔN HỌC",JLabel.CENTER);
        lbTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lbTitle.setBounds(30, 10, 400, 30);
        add(lbTitle);

        // ================= TÌM KIẾM =================
        JLabel lbTim = new JLabel("Tìm:");
        lbTim.setBounds(30, 50, 50, 25);
        add(lbTim);

        txtTim = new JTextField();
        txtTim.setBounds(80, 50, 180, 25);
        add(txtTim);

        btnTim = new JButton("Tìm");
        btnTim.setBounds(270, 50, 70, 25);
        add(btnTim);

        // ================= KHOA =================
        JLabel lbKHOA = new JLabel("Khoa");
        lbKHOA.setBounds(30, 90, 100, 25);
        add(lbKHOA);

        cbKHOA = new JComboBox<>();
        cbKHOA.setBounds(140, 90, 200, 25);
        add(cbKHOA);

        // ================= NGÀNH =================
        JLabel lbNganh = new JLabel("Ngành");
        lbNganh.setBounds(30, 130, 100, 25);
        add(lbNganh);

        cbNganh = new JComboBox<>();
        cbNganh.setBounds(140, 130, 200, 25);
        add(cbNganh);

        // ================= MÃ MÔN =================
        JLabel lbMMH = new JLabel("Mã môn học");
        lbMMH.setBounds(30, 170, 100, 25);
        add(lbMMH);

        txtMa = new JTextField();
        txtMa.setBounds(140, 170, 200, 25);
        add(txtMa);

        // ================= TÊN MÔN =================
        JLabel lbTMH = new JLabel("Tên môn học");
        lbTMH.setBounds(30, 210, 100, 25);
        add(lbTMH);

        txtTen = new JTextField();
        txtTen.setBounds(140, 210, 200, 25);
        add(txtTen);

        // ================= SỐ TÍN CHỈ =================
        JLabel lbSoTC = new JLabel("Số tín chỉ");
        lbSoTC.setBounds(30, 250, 100, 25);
        add(lbSoTC);

        txtSoTC = new JTextField();
        txtSoTC.setBounds(140, 250, 200, 25);
        add(txtSoTC);

        // ================= BUTTON =================
        btnThem = new JButton("Thêm");
        btnThem.setBounds(30, 300, 100, 30);
        add(btnThem);

        btnSua = new JButton("Sửa");
        btnSua.setBounds(150, 300, 100, 30);
        add(btnSua);

        btnXoa = new JButton("Xóa");
        btnXoa.setBounds(30, 350, 100, 30);
        add(btnXoa);

        btnXuat = new JButton("Xuất");
        btnXuat.setBounds(150, 350, 100, 30);
        add(btnXuat);

        // ================= TABLE =================
        model = new DefaultTableModel(
            new String[]{"Khoa", "Ngành", "Mã môn", "Tên môn", "Số tín chỉ"}, 0
        );

        table = new JTable(model);
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(380, 90, 600, 350);
        add(sp);
    }
}
