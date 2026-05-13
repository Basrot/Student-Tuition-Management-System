package com.mycompany.quanlyhocphi.view;

import com.mycompany.quanlyhocphi.controller.DangKyHocPhanController;
import com.mycompany.quanlyhocphi.controller.Diem_controller;
import com.mycompany.quanlyhocphi.controller.Giangvien_controller;
import com.mycompany.quanlyhocphi.controller.Hocphi_controller;
import com.mycompany.quanlyhocphi.controller.KhoaController;
import com.mycompany.quanlyhocphi.controller.LopHocPhan_controller;
import com.mycompany.quanlyhocphi.controller.Lop_controller;
import com.mycompany.quanlyhocphi.controller.MonHocPhanController;
import com.mycompany.quanlyhocphi.controller.Namhoc_controller;
import com.mycompany.quanlyhocphi.controller.NganhController;
import com.mycompany.quanlyhocphi.controller.PhanQuyenController;
import com.mycompany.quanlyhocphi.controller.Phieudangky_controller;
import com.mycompany.quanlyhocphi.controller.SinhVienController;
import com.mycompany.quanlyhocphi.controller.TraCuuCongNoController;
import com.mycompany.quanlyhocphi.controller.MienGiam_controller;
import com.mycompany.quanlyhocphi.controller.DonGiaTinChi_controller;

import com.mycompany.quanlyhocphi.panel.DK_MHP_Panel;
import com.mycompany.quanlyhocphi.panel.DangKyHocPhanPanel;
import com.mycompany.quanlyhocphi.panel.Diem_panel;
import com.mycompany.quanlyhocphi.panel.Giangvien_panel;
import com.mycompany.quanlyhocphi.panel.Hocphi_panel;
import com.mycompany.quanlyhocphi.panel.KhoaPanel;
import com.mycompany.quanlyhocphi.panel.LopHocPhan_panel;
import com.mycompany.quanlyhocphi.panel.Lop_panel;
import com.mycompany.quanlyhocphi.panel.Namhoc_panel;
import com.mycompany.quanlyhocphi.panel.NganhPanel;
import com.mycompany.quanlyhocphi.panel.PhanQuyenPanel;
import com.mycompany.quanlyhocphi.panel.Phieudangky_panel;
import com.mycompany.quanlyhocphi.panel.SinhVienPanel;
import com.mycompany.quanlyhocphi.panel.ThongKePanel;
import com.mycompany.quanlyhocphi.panel.TraCuuCongNoPanel;
import com.mycompany.quanlyhocphi.panel.MienGiam_panel;
import com.mycompany.quanlyhocphi.panel.DonGiaTinChi_panel;

import javax.swing.*;
import java.awt.*;

public class MainView extends JFrame {

    private JPanel content;

    public MainView(String username) {
        setTitle("QUẢN LÝ HỌC PHÍ - UTT");
        setSize(1400, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        Color blue = new Color(153, 217, 234);

        /* ===== HEADER ===== */
        JPanel header = new JPanel(new BorderLayout());
        header.setPreferredSize(new Dimension(0, 70));
        header.setBackground(blue);

        JLabel title = new JLabel(
                "TRƯỜNG ĐẠI HỌC CÔNG NGHỆ GIAO THÔNG VẬN TẢI",
                JLabel.CENTER
        );
        title.setFont(new Font("Arial", Font.BOLD, 20));
        header.add(title, BorderLayout.CENTER);

        add(header, BorderLayout.NORTH);

        /* ===== MENU ===== */
        JPanel menu = new JPanel();
        menu.setPreferredSize(new Dimension(220, 0));
        menu.setBackground(blue);
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));

        // ===== NÚT SINH VIÊN =====
        menu.add(createButton("SINH VIÊN", () -> {
            SinhVienPanel p = new SinhVienPanel();
            new SinhVienController(p);   // ⭐ BẮT BUỘC PHẢI CÓ
            showPanel(p);
        }));

        menu.add(createButton("LỚP", ()->{
           Lop_panel p = new Lop_panel();
            new Lop_controller(p);
            showPanel(p);
        }));
        menu.add(createButton("KHOA", () -> {
            KhoaPanel p = new KhoaPanel();
            new KhoaController(p);
            showPanel(p);
        }));
        
        menu.add(createButton("NGÀNH",() -> {
            NganhPanel p = new NganhPanel();
            new NganhController(p);
            showPanel(p);
        }));
        
        menu.add(createButton("MÔN HỌC PHẦN", () -> {
            DK_MHP_Panel p = new DK_MHP_Panel();
            new MonHocPhanController(p);
             showPanel(p);
        }));
        
        menu.add(createButton("LỚP HỌC PHẦN",  ()->{
            LopHocPhan_panel p = new LopHocPhan_panel();
            new LopHocPhan_controller(p);
            showPanel(p);
        }));
        
        
        menu.add(createButton("GIẢNG VIÊN", ()->{
            Giangvien_panel p = new Giangvien_panel();
            new Giangvien_controller(p);
            showPanel(p);
        }));
        menu.add(createButton("MIỄN GIẢM HỌC PHÍ", () -> {
            MienGiam_panel p = new MienGiam_panel();
            new MienGiam_controller(p);
            showPanel(p);
        }));
        
        menu.add(createButton("ĐƠN GIÁ TÍN CHỈ", () -> {
            DonGiaTinChi_panel p = new DonGiaTinChi_panel();
            new DonGiaTinChi_controller(p);
            showPanel(p);
        }));
        menu.add(createButton("HỌC PHÍ", ()->{
            Hocphi_panel p = new Hocphi_panel();
            new Hocphi_controller(p);
            showPanel(p);
        }));
        menu.add(createButton("ĐIỂM", () ->{
            Diem_panel p = new Diem_panel();
            new Diem_controller(p);
            showPanel(p);
        }));
        menu.add(createButton("NĂM HỌC", () ->{
                Namhoc_panel p = new Namhoc_panel();
                new Namhoc_controller(p);
                showPanel(p);
                }));
        menu.add(createButton("THỐNG KÊ", () -> {
            ThongKePanel p = new ThongKePanel();
            
            showPanel(p);
        }));
//        menu.add(createButton("LỊCH HỌC", null));
//        menu.add(createButton("TRA CỨU CÔNG NỢ", () -> {
//            TraCuuCongNoPanel p = new TraCuuCongNoPanel();
//            
//            showPanel(p);
//        }));

        menu.add(createButton("PHÂN QUYỀN", () -> {
            PhanQuyenPanel p = new PhanQuyenPanel();
            new PhanQuyenController(p);
            showPanel(p);
        }));

        menu.add(Box.createVerticalGlue());

        JButton btnLogout = createButton("ĐĂNG XUẤT", () -> {
            dispose();
            new LoginView().setVisible(true);
        });
        menu.add(btnLogout);

        add(menu, BorderLayout.WEST);

        /* ===== CONTENT ===== */
        content = new JPanel(new BorderLayout());
        content.setBackground(Color.WHITE);
        add(content, BorderLayout.CENTER);

        /* ===== MỞ MẶC ĐỊNH TRANG SINH VIÊN ===== */
        SinhVienPanel sv = new SinhVienPanel();
        new SinhVienController(sv);    //  – LOAD SQL
        showPanel(sv);
    }

    /* ===== LOAD PANEL ===== */
    private void showPanel(JPanel panel) {
        content.removeAll();
        content.add(panel, BorderLayout.CENTER);
        content.revalidate();
        content.repaint();
    }

    private JButton createButton(String text, Runnable action) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(200, 35));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);

        if (action != null) btn.addActionListener(e -> action.run());
        return btn;
    }
}
