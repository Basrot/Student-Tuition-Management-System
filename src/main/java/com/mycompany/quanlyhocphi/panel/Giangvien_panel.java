/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlyhocphi.panel;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
/**
 *
 * @author Admin
 */
public class Giangvien_panel extends JPanel {
    public JTextField txtMGV, txtTGV;
    public JButton btnThem, btnSua, btnXoa, btnXuat;
    public JComboBox<String> cbKHOA, cbNganh, cbMon;
    public JTable table;
    public DefaultTableModel model;

    public Giangvien_panel() {
        
        JLabel title = new JLabel("QUẢN LÝ GIANG VIEN", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);
        setLayout(null);
    setPreferredSize(new Dimension(1000, 600));
    
    JLabel lbMGV = new JLabel("Mã giảng viên");
    lbMGV.setBounds(30, 30, 100, 25);
    add(lbMGV);
    
    txtMGV = new JTextField();              // ✅ KHÔNG có JTextField phía trước
    txtMGV.setBounds(140, 30, 200, 25);
    add(txtMGV);
    
    JLabel lbTGV = new JLabel("Tên giảng viên");
    lbTGV.setBounds(30, 70, 100, 25);
    add(lbTGV);

    txtTGV = new JTextField();             // ✅
    txtTGV.setBounds(140, 70, 200, 25);
    add(txtTGV);
    
    JLabel lbKhoa = new JLabel("Khoa");
        lbKhoa.setBounds(30, 110, 100, 25);
        add(lbKhoa);

        cbKHOA = new JComboBox<>();
        cbKHOA.setBounds(140, 110, 200, 25);
        add(cbKHOA);

        JLabel lbNganh = new JLabel("Ngành");
        lbNganh.setBounds(30, 150, 100, 25);
        add(lbNganh);

        cbNganh = new JComboBox<>();
        cbNganh.setBounds(140, 150, 200, 25);
        add(cbNganh);
        
         JLabel lbMH = new JLabel("Môn học");
        lbMH.setBounds(30, 190, 100, 25);
        add(lbMH);

        cbMon = new JComboBox<>();
        cbMon.setBounds(140, 190, 200, 25);
        add(cbMon);
        
                btnThem = new JButton("Thêm");
           btnThem.setBounds(30, 270, 100, 30);
           add(btnThem);

           btnSua = new JButton("Sửa");
           btnSua.setBounds(200, 270, 100, 30);
           add(btnSua);

           btnXoa = new JButton("Xóa");
           btnXoa.setBounds(30, 310, 100, 30);
           add(btnXoa);

           btnXuat = new JButton("Xuất");
           btnXuat.setBounds(200, 310, 100, 30);
           add(btnXuat);
           
           model = new DefaultTableModel(
        new String[]{"Mã giảng viên", "Tên giảng viên", "Khoa", "Ngành","Môn học"}, 0
    );
          table = new JTable(model);

    JScrollPane sp = new JScrollPane(table);
    sp.setBounds(380, 30, 500, 300);
    add(sp); 
    }
    
    
}
