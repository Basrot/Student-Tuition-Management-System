/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlyhocphi.panel;
import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author Admin
 */
public class XemthongtinSV_panel extends JPanel {
     public JTextField txtMSV, txtHT, txtNS, txtLOP, txtEMAIL;
    public JComboBox<String> cbGT;
    public JComboBox<String> cbKHOA;
    public ButtonGroup groupSex;
    public JTable table;
    public JButton btnDMK, btnSua, btnDX, btnCapnhat;
    public DefaultTableModel model;
    
    public XemthongtinSV_panel() {
        setLayout(null);
        setPreferredSize(new Dimension(1000, 600));
        
        
        
        
         JLabel lbMSV = new JLabel("Mã sinh viên");
        lbMSV.setBounds(300, 30, 100, 25);
         add(lbMSV);

        txtMSV = new JTextField();              
        txtMSV.setBounds(440, 30, 200, 25);
        add(txtMSV);
        txtMSV.setEditable(false);
        
        JLabel lbTSV = new JLabel("Tên sinh viên");
        lbTSV.setBounds(300, 70, 100, 25);
         add(lbTSV);

        txtHT = new JTextField();              
        txtHT.setBounds(440, 70, 200, 25);
        add(txtHT);
         txtHT.setEditable(false);
        
        JLabel lbNS = new JLabel("Ngày sinh");
        lbNS.setBounds(300, 110, 200, 25);
         add(lbNS);

        txtNS = new JTextField();              
        txtNS.setBounds(440, 110, 200, 25);
        add(txtNS);
        txtNS.setEditable(false);
        
        JLabel lbGT = new JLabel("Giới tính ");
        lbGT.setBounds(300, 160, 100, 25);
         add(lbGT);

        cbGT = new JComboBox<>(new String[] {"Nam", "Nữ"});
        cbGT.setBounds(440, 160,100,25);
        add(cbGT);
        cbGT.setEnabled(false);
        
        JLabel lbLOP = new JLabel("Lớp");
        lbLOP.setBounds(300, 210, 200, 25);
         add(lbLOP);

        txtLOP = new JTextField();              
        txtLOP.setBounds(440, 210, 200, 25);
        add(txtLOP);
        txtLOP.setEditable(false);
        
        JLabel lbKHOA = new JLabel("Khoa ");
        lbKHOA.setBounds(300, 260, 100, 30);
        add(lbKHOA);
        
        cbKHOA = new JComboBox<>(new String[]{"CNTT", "HTTT", "CT", "DT", "GT", "KT", "XT"});
        cbKHOA.setBounds(440,260,200,30);
        add(cbKHOA);
        cbKHOA.setEnabled(false);
        
      /*  model = new DefaultTableModel(
        new String []{"Mã sinh viên","Tên sinh viên", "Ngày sinh", "Giới tính", "Lớp" , "Khoa", "Email"}
        ,0 );
        table = new JTable(model);
        
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(230, 350, 600, 50);
        add(sp); */
        
        btnSua = new JButton("Sửa");
        btnSua.setBounds(300, 400, 100, 30);
        add(btnSua);
        
        
        btnDMK = new JButton("Đổi mật khẩu");
        btnDMK.setBounds(450, 400, 110, 30);
        add(btnDMK);
        
        btnDX = new JButton("Đăng xuất");
        btnDX.setBounds(600, 400, 110, 30);
        add(btnDX);
        
       
        
}
        public void setEditable(boolean b) {
        txtMSV.setEditable(b);
        txtHT.setEditable(b);
        txtNS.setEditable(b);
        txtLOP.setEditable(b);
        cbGT.setEnabled(false); 
        cbKHOA.setEnabled(false);

        
        
    }
    
    
    
    
}
