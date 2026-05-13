package com.mycompany.quanlyhocphi.controller;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class MonHocPhanControllerTest {

    private MonHocPhanController controller;

    @Before
    public void setUp() {
        controller = new MonHocPhanController(null);
    }

    @Test
    public void testKhoaEmpty() {
        assertEquals("Các ô không được để trống!",
                controller.validateMonHoc("", "CNPM", "MH01", "Lap trinh Java", "3"));
    }

    @Test
    public void testNganhEmpty() {
        assertEquals("Các ô không được để trống!",
                controller.validateMonHoc("CNTT", "", "MH01", "Lap trinh Java", "3"));
    }

    @Test
    public void testMaMonEmpty() {
        assertEquals("Các ô không được để trống!",
                controller.validateMonHoc("CNTT", "CNPM", "", "Lap trinh Java", "3"));
    }

    @Test
    public void testTenMonEmpty() {
        assertEquals("Các ô không được để trống!",
                controller.validateMonHoc("CNTT", "CNPM", "MH01", "", "3"));
    }

    @Test
    public void testSoTinChiEmpty() {
        assertEquals("Các ô không được để trống!",
                controller.validateMonHoc("CNTT", "CNPM", "MH01", "Lap trinh Java", ""));
    }

    @Test
    public void testSoTinChiNotNumber() {
        assertEquals("Số tín chỉ không quá 10 tín",
                controller.validateMonHoc("CNTT", "CNPM", "MH01", "Lap trinh Java", "abc"));
    }

    @Test
    public void testSoTinChiDecimal() {
        assertEquals("Số tín chỉ không quá 10 tín",
                controller.validateMonHoc("CNTT", "CNPM", "MH01", "Lap trinh Java", "1.5"));
    }

    @Test
    public void testSoTinChiTooLarge() {
        assertEquals("Số tín chỉ không quá 10 tín",
                controller.validateMonHoc("CNTT", "CNPM", "MH01", "Lap trinh Java", "11"));
    }

    @Test
    public void testValidInput() {
        assertEquals("Hợp lệ",
                controller.validateMonHoc("CNTT", "CNPM", "MH01", "Lap trinh Java", "3"));
    }
}