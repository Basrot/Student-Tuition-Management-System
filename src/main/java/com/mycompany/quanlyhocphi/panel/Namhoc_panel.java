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
public class Namhoc_panel extends JPanel {
        public JTextField txtMHK;
     public JComboBox<String> cbHK, cbNH;
     public JButton btnThem, btnSua, btnXoa, btnXuat;
        public JTable table;
        public DefaultTableModel model;

    public Namhoc_panel() {
        
        JLabel title = new JLabel("QUẢN LÝ NĂM HỌC", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);
        setLayout(null);
    setPreferredSize(new Dimension(1000, 600));
    
    JLabel lbMHK = new JLabel("Mã học kì");
    lbMHK.setBounds(30, 30, 100, 25);
    add(lbMHK);
    
    txtMHK = new JTextField();
    txtMHK.setBounds(140, 30, 200, 25);
    add(txtMHK);
    
    JLabel lbHK = new JLabel("Học kì");
    lbHK.setBounds(30, 70, 100, 25);
    add(lbHK);

    cbHK = new JComboBox(new String []{"","Học kì 1", "Học kì 2"});             
    cbHK.setBounds(140, 70, 200, 25);
    add(cbHK);
    
    JLabel lbNH = new JLabel("Năm học");
    lbNH.setBounds(30, 120, 100, 25);
    add(lbNH);
    
    cbNH = new JComboBox(new String [] {"","2025-2026", "2026-2027"});
    cbNH.setBounds(140, 120, 200, 25);
    add(cbNH);
    
    btnThem = new JButton("Thêm");
    btnThem.setBounds(30, 270, 100, 30);
    add(btnThem);

    btnSua = new JButton("Sửa");
    btnSua.setBounds(150, 270, 100, 30);
    add(btnSua);

    btnXoa = new JButton("Xóa");
    btnXoa.setBounds(30, 310, 100, 30);
    add(btnXoa);

    btnXuat = new JButton("Xuất");
    btnXuat.setBounds(150, 310, 100, 30);
    add(btnXuat);
    
     model = new DefaultTableModel(
        new String[]{"Mã học kì" , "Học kì","Năm học"}, 0
    );
     table = new JTable(model);

    JScrollPane sp = new JScrollPane(table);
    sp.setBounds(380, 30, 500, 300);
    add(sp);
    
    }
     
        
        
        
    }
