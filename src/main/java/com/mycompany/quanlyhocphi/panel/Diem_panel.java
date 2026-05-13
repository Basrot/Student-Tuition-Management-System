/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlyhocphi.panel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author Admin
 */
public class Diem_panel extends JPanel {
    public JTextField  txtCC, txtGK, txtCK, txtTB;
    public JButton btnLuu, btnXuat, btnTK;
    public JComboBox<String> cbKHOA, cbNganh  ;
    public JTable table;
    public DefaultTableModel model;
    
    public Diem_panel() {
        JLabel title = new JLabel("QUẢN LÝ LOP", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);
        setLayout(null);
         setPreferredSize(new Dimension(1000, 600));
    
         
         JLabel lbKhoa = new JLabel("Khoa");
        lbKhoa.setBounds(30, 70, 100, 25);
        add(lbKhoa);

        cbKHOA = new JComboBox<>();
        cbKHOA.setBounds(140, 70, 200, 25);
        add(cbKHOA);
         
         

        JLabel lbNganh = new JLabel("Ngành");
        lbNganh.setBounds(450, 70, 100, 25);
        add(lbNganh);

        cbNganh = new JComboBox<>();
        cbNganh.setBounds(540, 70, 200, 25);
        add(cbNganh);
        
        

        
        btnTK = new JButton("Tìm kiếm");
        btnTK.setBounds(800, 70, 100, 30);
        add(btnTK);
         
          model = new DefaultTableModel(
        new String[]{"Mã sinh viên", "Tên sinh viên", "Điểm chuyên cần", "Điểm giữa kì","Điểm cuối kì", "Điểm trung bình",
        }, 0
    );
          table = new JTable(model);

    JScrollPane sp = new JScrollPane(table);
    sp.setBounds(50, 200, 900, 200);
    add(sp);
    
    JLabel lbCC = new JLabel("Điểm chuyên cần");
    lbCC.setBounds(50, 450, 200, 25);
    add(lbCC);
    
    txtCC = new JTextField();
    txtCC.setBounds(200, 450, 110, 30);
    add(txtCC);
    
    JLabel lbGK = new JLabel("Điểm giữa kì");
    lbGK.setBounds(400, 450, 200, 25);
    add(lbGK);
    
    txtGK = new JTextField();
    txtGK.setBounds(500, 450, 110, 30);
    add(txtGK);
    
    JLabel lbCK = new JLabel("Cuối giữa kì");
    lbCK.setBounds(750, 450, 200, 25);
    add(lbCK);
    
    txtCK = new JTextField();
    txtCK.setBounds(850, 450, 110, 30);
    add(txtCK);
    
    btnLuu = new JButton("Lưu");
    btnLuu.setBounds(50, 550, 100, 30);
    add(btnLuu);

    btnXuat = new JButton("Xuất");
    btnXuat.setBounds(650, 550, 100, 30);
    add(btnXuat);
    
}
}
