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
public class SVDKTC_panel extends JPanel {
    public JTextField txtTK;
    public JTable table1;
    public JTable table2;
    public  JButton btnDK, btnXUAT, btnTK, btnHUY;
    public DefaultTableModel modelMonMo;
    public DefaultTableModel modelDaDK;
    
    public SVDKTC_panel(){
        setLayout(null);
        setPreferredSize(new Dimension(1000, 600));
        
        JLabel lbDSMHM = new JLabel("Danh sách môn học mở");
        lbDSMHM.setBounds(30, 30, 200, 50);
        add(lbDSMHM);
        
        modelMonMo = new DefaultTableModel(
          new String[]{"Mã phiếu", "Mã học phần", "Tên học phần", "Ngày đăng ký", "Học kỳ",
        "Số tín chỉ"
          }  ,0    
        );
        btnDK = new JButton("Đăng ký");
        btnDK.setBounds(980,170,110,60);
        add(btnDK);
        
        
        
        table1 = new JTable(modelMonMo);
        
        JScrollPane sp = new JScrollPane(table1);
        sp.setBounds(50, 90, 900, 200);
        add(sp);
        
        modelDaDK = new DefaultTableModel(
          new String[]{"Mã phiếu", "Mã học phần", "Tên học phần", "Ngày đăng ký", "Học kỳ",
        "Số tín chỉ"
          }  ,0    
        );
        
        btnHUY = new JButton("Huỷ đăng ký");
        btnHUY.setBounds(980,500,110,30);
        add(btnHUY);
         
        
        btnXUAT = new JButton("Xuất excel");
        btnXUAT.setBounds(980,590,110,30);
        add(btnXUAT);
        
        txtTK = new JTextField();
        txtTK.setBounds(850,400,110,25);
        add(txtTK);
        
        
        btnTK = new JButton("Tìm kiếm");
        btnTK.setBounds(980,400,110,25);
        add(btnTK);
        
        JLabel lbDSDK = new JLabel("Danh sách học phần đã đăng ký");
        lbDSDK.setBounds(30, 350, 200, 25);
        add(lbDSDK);
        table2 = new JTable(modelDaDK);
        
        JScrollPane sp1 = new JScrollPane(table2);
        sp1.setBounds(50, 450, 900, 200);
        add(sp1);
    }
    
}
