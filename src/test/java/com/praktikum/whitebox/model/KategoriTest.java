package com.praktikum.whitebox.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KategoriTest {

    @Test
    void testDefaultConstructorAndSettersGetters() {
        Kategori kategori = new Kategori();
        kategori.setKode("K01");
        kategori.setNama("Elektronik");
        kategori.setDeskripsi("Barang Elektronik");
        kategori.setAktif(true);

        assertEquals("K01", kategori.getKode());
        assertEquals("Elektronik", kategori.getNama());
        assertEquals("Barang Elektronik", kategori.getDeskripsi());
        assertTrue(kategori.isAktif());
    }

    @Test
    void testParameterizedConstructor() {
        Kategori kategori = new Kategori("K02", "Makanan", "Produk makanan");
        assertEquals("K02", kategori.getKode());
        assertEquals("Makanan", kategori.getNama());
        assertEquals("Produk makanan", kategori.getDeskripsi());
        assertTrue(kategori.isAktif()); // default true
    }

    @Test
    void testToString() {
        Kategori kategori = new Kategori("K03", "Minuman", "Produk minuman");
        String toString = kategori.toString();
        assertTrue(toString.contains("K03"));
        assertTrue(toString.contains("Minuman"));
        assertTrue(toString.contains("Produk minuman"));
        assertTrue(toString.contains("true"));
    }

    @Test
    void testHashCode() {
        Kategori kategori1 = new Kategori("K04", "Pakaian", "Produk pakaian");
        Kategori kategori2 = new Kategori("K04", "Lain", "Deskripsi lain");
        assertEquals(kategori1.hashCode(), kategori2.hashCode());
    }

    @Test
    void testEqualsSameObject() {
        Kategori kategori = new Kategori("K05", "Alat Tulis", "Perlengkapan kantor");
        assertTrue(kategori.equals(kategori)); // harus true
    }

    @Test
    void testEqualsNullObject() {
        Kategori kategori = new Kategori("K06", "Furniture", "Perabotan rumah");
        assertFalse(kategori.equals(null)); // harus false
    }

    @Test
    void testEqualsDifferentClass() {
        Kategori kategori = new Kategori("K07", "A", "B");
        assertFalse(kategori.equals("StringLain")); // harus false
    }

    @Test
    void testEqualsDifferentKode() {
        Kategori kategori1 = new Kategori("K08", "A", "B");
        Kategori kategori2 = new Kategori("K09", "A", "B");
        assertFalse(kategori1.equals(kategori2)); // harus false
    }

    @Test
    void testEqualsSameKode() {
        Kategori kategori1 = new Kategori("K10", "A", "B");
        Kategori kategori2 = new Kategori("K10", "X", "Y");
        assertTrue(kategori1.equals(kategori2)); // hanya cek berdasarkan kode
    }
}
