package com.mycompany.quanlyhocphi.panel;

import com.mycompany.quanlyhocphi.controller.ThongKeController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ChiTietCongNoDialog extends JDialog {

    JTable table;
    JLabel lblMaSV, lblTongNo;

    public ChiTietCongNoDialog(String maSV) {
        setTitle("CHI TIẾT ĐĂNG KÝ & HỌC PHÍ");
        setSize(800, 450);
        setLocationRelativeTo(null);
        setModal(true);
        setLayout(new BorderLayout(5, 5));

        // ===== HEADER =====
        JPanel header = new JPanel(new GridLayout(2, 1));
        lblMaSV = new JLabel("Mã SV: " + maSV);
        lblMaSV.setFont(new Font("Arial", Font.BOLD, 14));

        lblTongNo = new JLabel();
        lblTongNo.setFont(new Font("Arial", Font.BOLD, 14));
        lblTongNo.setForeground(Color.RED);

        header.add(lblMaSV);
        header.add(lblTongNo);
        add(header, BorderLayout.NORTH);

        // ===== TABLE =====
        table = new JTable();
        add(new JScrollPane(table), BorderLayout.CENTER);

        loadData(maSV);
    }

    private void loadData(String maSV) {

        DefaultTableModel model = new DefaultTableModel(
                new String[]{
                        "Tên môn",
                        "Số TC",
                        "Học phí",
                        "Học kỳ",
                        "Ngày ĐK",
                        "Trạng thái"
                }, 0);

        List<Object[]> list = ThongKeController.chiTietDangKy(maSV);

        double tongNo = 0;

        for (Object[] o : list) {
            String trangThai = o[5].toString();
            double hocPhi = (double) o[2];

            if (trangThai.equalsIgnoreCase("Chưa đóng")) {
                tongNo += hocPhi;
            }

            model.addRow(new Object[]{
                    o[0],              // Tên môn
                    o[1],              // Số tín chỉ
                    vnd(hocPhi),       // Học phí
                    o[3],              // Học kỳ
                    o[4],              // Ngày đăng ký
                    trangThai          // Trạng thái
            });
        }

        table.setModel(model);
        lblTongNo.setText("TỔNG CÒN NỢ: " + vnd(tongNo));
    }

    private String vnd(double t) {
        return NumberFormat.getCurrencyInstance(
                new Locale("vi", "VN")).format(t);
    }
}