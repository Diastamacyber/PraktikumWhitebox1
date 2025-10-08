package com.praktikum.whitebox.util;

import com.praktikum.whitebox.model.Kategori;
import com.praktikum.whitebox.model.Produk;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ValidationUtilsTest {
    // =====================
    // isValidKodeProduk
    // =====================
    @Test
    @DisplayName("Test kode produk null, kosong, valid, dan panjang ekstrem")
    void testIsValidKodeProduk() {
        assertFalse(ValidationUtils.isValidKodeProduk(null));      // null
        assertFalse(ValidationUtils.isValidKodeProduk(""));        // kosong
        assertFalse(ValidationUtils.isValidKodeProduk("  "));      // spasi
        assertTrue(ValidationUtils.isValidKodeProduk("ABC123"));   // valid
        assertFalse(ValidationUtils.isValidKodeProduk("AB"));      // < 3 char
        assertFalse(ValidationUtils.isValidKodeProduk("ABCDEFGHIJK")); // > 10 char
        assertFalse(ValidationUtils.isValidKodeProduk("ABC!"));    // simbol
    }

    // =====================
    // isValidNama
    // =====================
    @Test
    @DisplayName("Test nama produk null, kosong, panjang min-max")
    void testIsValidNama() {
        assertFalse(ValidationUtils.isValidNama(null));       // null
        assertFalse(ValidationUtils.isValidNama(""));         // kosong
        assertFalse(ValidationUtils.isValidNama("AB"));       // < 3 char
        assertTrue(ValidationUtils.isValidNama("ABC"));       // batas bawah
        assertTrue(ValidationUtils.isValidNama("A".repeat(100))); // batas atas
        assertFalse(ValidationUtils.isValidNama("A".repeat(101))); // > 100
    }

    // =====================
    // isValidHarga
    // =====================
    @Test
    @DisplayName("Test harga batas bawah dan validasi positif")
    void testIsValidHarga() {
        assertFalse(ValidationUtils.isValidHarga(0));      // 0 tidak valid
        assertTrue(ValidationUtils.isValidHarga(1));       // batas bawah valid
        assertTrue(ValidationUtils.isValidHarga(99.99));   // positif
        assertFalse(ValidationUtils.isValidHarga(-10));    // negatif
    }

    // =====================
    // isValidStok & isValidStokMinimum
    // =====================
    @Test
    @DisplayName("Test stok dan stok minimum (negatif, nol, positif)")
    void testIsValidStok() {
        assertTrue(ValidationUtils.isValidStok(0));   // nol valid
        assertTrue(ValidationUtils.isValidStok(10));  // positif valid
        assertFalse(ValidationUtils.isValidStok(-1)); // negatif tidak valid
    }

    @Test
    @DisplayName("Test stok minimum batas bawah")
    void testIsValidStokMinimum() {
        assertTrue(ValidationUtils.isValidStokMinimum(0));   // nol valid
        assertTrue(ValidationUtils.isValidStokMinimum(5));   // positif valid
        assertFalse(ValidationUtils.isValidStokMinimum(-1)); // negatif tidak valid
    }

    // =====================
    // isValidProduk
    // =====================
    @Test
    @DisplayName("Test validasi produk lengkap")
    void testIsValidProduk() {
        Produk p = new Produk();
        p.setKode("PRD01");
        p.setNama("Produk A");
        p.setKategori("Kategori1"); // validasi nama kategori juga dipakai disini
        p.setHarga(10000);
        p.setStok(5);
        p.setStokMinimum(1);
        assertTrue(ValidationUtils.isValidProduk(p));

        // produk null
        assertFalse(ValidationUtils.isValidProduk(null));

        // invalid kode
        p.setKode("!!");
        assertFalse(ValidationUtils.isValidProduk(p));
    }

    // =====================
    // isValidKategori
    // =====================
    @Test
    @DisplayName("Test validasi kategori lengkap")
    void testIsValidKategori() {
        Kategori k = new Kategori();
        k.setKode("KTG01");
        k.setNama("Kategori A");
        k.setDeskripsi("Deskripsi kategori");
        assertTrue(ValidationUtils.isValidKategori(k));

        // null
        assertFalse(ValidationUtils.isValidKategori(null));

        // invalid kode
        k.setKode("##");
        assertFalse(ValidationUtils.isValidKategori(k));

        // deskripsi > 500 karakter
        k.setKode("KTG01");
        k.setNama("Kategori A");
        k.setDeskripsi("A".repeat(501));
        assertFalse(ValidationUtils.isValidKategori(k));

        // deskripsi null → valid
        k.setDeskripsi(null);
        assertTrue(ValidationUtils.isValidKategori(k));
    }

    // =====================
    // isValidPersentase
    // =====================
    @Test
    @DisplayName("Test persentase batas bawah dan atas")
    void testIsValidPersentase() {
        assertTrue(ValidationUtils.isValidPersentase(0));   // batas bawah
        assertTrue(ValidationUtils.isValidPersentase(100)); // batas atas
        assertTrue(ValidationUtils.isValidPersentase(50));  // valid tengah
        assertFalse(ValidationUtils.isValidPersentase(-1)); // < 0
        assertFalse(ValidationUtils.isValidPersentase(101));// > 100
    }

    // =====================
    // isValidKuantitas
    // =====================
    @Test
    @DisplayName("Test kuantitas valid dan tidak valid")
    void testIsValidKuantitas() {
        assertTrue(ValidationUtils.isValidKuantitas(1));  // batas bawah
        assertTrue(ValidationUtils.isValidKuantitas(10));// valid
        assertFalse(ValidationUtils.isValidKuantitas(0));// nol tidak valid
        assertFalse(ValidationUtils.isValidKuantitas(-5));// negatif
    }
}
