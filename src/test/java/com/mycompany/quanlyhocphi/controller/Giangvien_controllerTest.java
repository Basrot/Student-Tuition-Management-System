package com.mycompany.quanlyhocphi.controller;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class Giangvien_controllerTest {

    private Giangvien_controller controller;

    @Before
    public void setUp() {
        controller = new Giangvien_controller(null);
    }

    // R1: Mã giảng viên rỗng
    @Test
    public void testMaGVEmpty() {
        assertEquals("Các ô không được để trống!",
                controller.validateGiangVienForTest("", "Nguyen Van A", "CNTT", "CNPM", "MH01", false));
    }

    // R2: Tên giảng viên rỗng
    @Test
    public void testTenGVEmpty() {
        assertEquals("Các ô không được để trống!",
                controller.validateGiangVienForTest("GV01", "", "CNTT", "CNPM", "MH01", false));
    }

    // R3: Khoa rỗng
    @Test
    public void testKhoaEmpty() {
        assertEquals("Các ô không được để trống!",
                controller.validateGiangVienForTest("GV01", "Nguyen Van A", "", "CNPM", "MH01", false));
    }

    // R4: Ngành rỗng
    @Test
    public void testNganhEmpty() {
        assertEquals("Các ô không được để trống!",
                controller.validateGiangVienForTest("GV01", "Nguyen Van A", "CNTT", "", "MH01", false));
    }

    // R5: Môn học rỗng
    @Test
    public void testMonEmpty() {
        assertEquals("Các ô không được để trống!",
                controller.validateGiangVienForTest("GV01", "Nguyen Van A", "CNTT", "CNPM", "", false));
    }

    // R6: Mã giảng viên trùng
    @Test
    public void testMaGVTrung() {
        assertEquals("Mã giảng viên trùng",
                controller.validateGiangVienForTest("GV01", "Nguyen Van A", "CNTT", "CNPM", "MH01", true));
    }

    // R7: Hợp lệ
    @Test
    public void testValidInput() {
        assertEquals("Hợp lệ",
                controller.validateGiangVienForTest("GV02", "Nguyen Van A", "CNTT", "CNPM", "MH01", false));
    }
}