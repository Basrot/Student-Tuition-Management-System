package com.mycompany.quanlyhocphi.panel;

import com.mycompany.quanlyhocphi.controller.ThongKeController;
import com.mycompany.quanlyhocphi.model.ThongKeModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ThongKePanel extends JPanel {

    JLabel lblTongSV, lblTongThu, lblDaThu, lblConNo;
    JTable tblSV, tblKhoa, tblNganh;
    JComboBox<String> cbTrangThai;

    public ThongKePanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ===== PANEL TRÊN =====
        JPanel top = new JPanel(new GridLayout(1, 4, 10, 10));
        lblTongSV = new JLabel();
        lblTongThu = new JLabel();
        lblDaThu = new JLabel();
        lblConNo = new JLabel();

        top.add(lblTongSV);
        top.add(lblTongThu);
        top.add(lblDaThu);
        top.add(lblConNo);
        add(top, BorderLayout.NORTH);

        // ===== CENTER =====
        JPanel center = new JPanel(new GridLayout(1, 2, 10, 10));
        center.add(taoBieuDo());

        JPanel right = new JPanel(new BorderLayout(5, 5));
        cbTrangThai = new JComboBox<>(new String[]{
                "Chưa đóng / Đóng thiếu",
                "Đã đóng đủ"
        });
        right.add(cbTrangThai, BorderLayout.NORTH);

        tblSV = new JTable();
        right.add(new JScrollPane(tblSV), BorderLayout.CENTER);

        center.add(right);
        add(center, BorderLayout.CENTER);

        // ===== BOTTOM =====
        JPanel bottom = new JPanel(new GridLayout(1, 2, 10, 10));
        tblKhoa = new JTable();
        tblNganh = new JTable();
        bottom.add(new JScrollPane(tblKhoa));
        bottom.add(new JScrollPane(tblNganh));
        add(bottom, BorderLayout.SOUTH);

        // ===== LOAD DATA =====
        loadTongQuan();
        loadBangSV();
        loadTheoKhoa();
        loadTheoNganh();

        cbTrangThai.addActionListener(e -> loadBangSV());

        // ===== DOUBLE CLICK OPEN DETAIL =====
        tblSV.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && tblSV.getSelectedRow() != -1) {
                    int row = tblSV.getSelectedRow();
                    String maSV = tblSV.getValueAt(row, 0).toString();
                    new ChiTietCongNoDialog(maSV).setVisible(true);
                }
            }
        });
    }

    // ===== FORMAT VNĐ =====
    private String vnd(double t) {
        return NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(t);
    }

    // ===== TỔNG QUAN =====
    private void loadTongQuan() {
        lblTongSV.setText("Tổng SV: " +
                (int) ThongKeController.getGiaTri("SELECT COUNT(*) FROM sinhvien"));
        lblTongThu.setText("Tổng học phí: " + vnd(
                ThongKeController.getGiaTri("SELECT IFNULL(SUM(TongHocPhi),0) FROM congno")));
        lblDaThu.setText("Đã thu: " + vnd(
                ThongKeController.getGiaTri("SELECT IFNULL(SUM(DaThu),0) FROM congno")));
        lblConNo.setText("Còn nợ: " + vnd(
                ThongKeController.getGiaTri("SELECT IFNULL(SUM(ConNo),0) FROM congno")));
    }

    // ===== BIỂU ĐỒ =====
    private ChartPanel taoBieuDo() {
        DefaultPieDataset ds = new DefaultPieDataset();
        ds.setValue("Đã thu", ThongKeController.getGiaTri(
                "SELECT IFNULL(SUM(DaThu),0) FROM congno"));
        ds.setValue("Còn nợ", ThongKeController.getGiaTri(
                "SELECT IFNULL(SUM(ConNo),0) FROM congno"));

        JFreeChart chart = ChartFactory.createPieChart(
                "TỶ LỆ THU HỌC PHÍ", ds, true, true, false);

        return new ChartPanel(chart);
    }

    // ===== TABLE SINH VIÊN =====
    private void loadBangSV() {
        String dk = cbTrangThai.getSelectedIndex() == 0
                ? "WHERE cn.DaThu < cn.TongHocPhi"
                : "WHERE cn.DaThu = cn.TongHocPhi";

        List<ThongKeModel> list = ThongKeController.getDanhSach(dk);

        DefaultTableModel m = new DefaultTableModel(
                new String[]{"Mã SV", "Họ tên", "Lớp", "Khoa", "Tổng HP", "Đã thu", "Còn nợ"}, 0);

        for (ThongKeModel t : list) {
            m.addRow(new Object[]{
                    t.maSV,
                    t.hoTen,
                    t.lop,
                    t.khoa,
                    vnd(t.tongHocPhi),
                    vnd(t.daThu),
                    vnd(t.conNo)
            });
        }
        tblSV.setModel(m);
    }

    // ===== THEO KHOA =====
    private void loadTheoKhoa() {
        DefaultTableModel m = new DefaultTableModel(
                new String[]{"Khoa", "Số SV", "Tổng nợ"}, 0);

        ThongKeController.getTheoKhoa()
                .forEach(o -> m.addRow(new Object[]{o[0], o[1], vnd((double) o[2])}));

        tblKhoa.setModel(m);
    }

    // ===== THEO NGÀNH =====
    private void loadTheoNganh() {
        DefaultTableModel m = new DefaultTableModel(
                new String[]{"Ngành", "Số SV"}, 0);

        ThongKeController.getTheoNganh()
                .forEach(o -> m.addRow(new Object[]{o[0], o[1]}));

        tblNganh.setModel(m);
    }
}