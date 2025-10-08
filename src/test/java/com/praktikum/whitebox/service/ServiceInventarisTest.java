package com.praktikum.whitebox.service;

import com.praktikum.whitebox.model.Produk;
import com.praktikum.whitebox.repository.RepositoryProduk;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test Service Inventaris dengan Mocking")
public class ServiceInventarisTest {

    @Mock
    private RepositoryProduk mockRepositoryProduk;

    private ServiceInventory serviceInventaris;
    private Produk produkTest;

    @BeforeEach
    void setUp() {
        serviceInventaris = new ServiceInventory(mockRepositoryProduk);
        produkTest = new Produk("PROD001", "Laptop Gaming", "Elektronik",
                15000000, 10, 5);
    }

    // ========== Tambah Produk ==========
    @Test
    @DisplayName("Tambah produk berhasil - semua kondisi valid")
    void testTambahProdukBerhasil() {
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.empty());
        when(mockRepositoryProduk.simpan(produkTest)).thenReturn(true);

        boolean hasil = serviceInventaris.tambahProduk(produkTest);

        assertTrue(hasil);
        verify(mockRepositoryProduk).cariByKode("PROD001");
        verify(mockRepositoryProduk).simpan(produkTest);
    }

    @Test
    @DisplayName("Tambah produk gagal - produk sudah ada")
    void testTambahProdukGagalSudahAda() {
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.of(produkTest));

        boolean hasil = serviceInventaris.tambahProduk(produkTest);

        assertFalse(hasil);
        verify(mockRepositoryProduk).cariByKode("PROD001");
        verify(mockRepositoryProduk, never()).simpan(any());
    }

    @Test
    @DisplayName("Tambah produk gagal - produk tidak valid")
    void testTambahProdukGagalProdukTidakValid() {
        Produk produkInvalid = new Produk("", "", "", 0, -1, 0);

        boolean hasil = serviceInventaris.tambahProduk(produkInvalid);

        assertFalse(hasil);
        verify(mockRepositoryProduk, never()).simpan(any());
    }

    // ========== Hapus Produk ==========
    @Test
    @DisplayName("Hapus produk gagal - kode tidak valid")
    void testHapusProdukKodeTidakValid() {
        boolean hasil = serviceInventaris.hapusProduk("");
        assertFalse(hasil);
        verify(mockRepositoryProduk, never()).hapus(anyString());
    }

    @Test
    @DisplayName("Hapus produk gagal - produk tidak ditemukan")
    void testHapusProdukTidakDitemukan() {
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.empty());

        boolean hasil = serviceInventaris.hapusProduk("PROD001");

        assertFalse(hasil);
    }

    @Test
    @DisplayName("Hapus produk gagal - stok masih ada")
    void testHapusProdukStokMasihAda() {
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.of(produkTest));

        boolean hasil = serviceInventaris.hapusProduk("PROD001");

        assertFalse(hasil);
        verify(mockRepositoryProduk, never()).hapus("PROD001");
    }

    @Test
    @DisplayName("Hapus produk berhasil - stok 0")
    void testHapusProdukBerhasil() {
        Produk produkKosong = new Produk("PROD001", "Mouse", "Elektronik", 10000, 0, 1);
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.of(produkKosong));
        when(mockRepositoryProduk.hapus("PROD001")).thenReturn(true);

        boolean hasil = serviceInventaris.hapusProduk("PROD001");

        assertTrue(hasil);
        verify(mockRepositoryProduk).hapus("PROD001");
    }

    // ========== Cari Produk ==========
    @Test
    void testCariProdukByKodeValid() {
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.of(produkTest));

        Optional<Produk> hasil = serviceInventaris.cariProdukByKode("PROD001");

        assertTrue(hasil.isPresent());
        assertEquals("Laptop Gaming", hasil.get().getNama());
    }

    @Test
    void testCariProdukByKodeInvalid() {
        Optional<Produk> hasil = serviceInventaris.cariProdukByKode("");
        assertFalse(hasil.isPresent());
    }

    @Test
    void testCariProdukByNama() {
        when(mockRepositoryProduk.cariByNama("Laptop")).thenReturn(List.of(produkTest));

        List<Produk> hasil = serviceInventaris.cariProdukByNama("Laptop");

        assertEquals(1, hasil.size());
        assertEquals("Laptop Gaming", hasil.get(0).getNama());
    }

    @Test
    void testCariProdukByKategori() {
        when(mockRepositoryProduk.cariByKategori("Elektronik")).thenReturn(List.of(produkTest));

        List<Produk> hasil = serviceInventaris.cariProdukByKategori("Elektronik");

        assertEquals(1, hasil.size());
        assertEquals("Laptop Gaming", hasil.get(0).getNama());
    }

    // ========== Update Stok ==========
    @Test
    void testUpdateStokBerhasil() {
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.of(produkTest));
        when(mockRepositoryProduk.updateStok("PROD001", 20)).thenReturn(true);

        boolean hasil = serviceInventaris.updateStok("PROD001", 20);

        assertTrue(hasil);
        verify(mockRepositoryProduk).updateStok("PROD001", 20);
    }

    @Test
    void testUpdateStokGagalKodeInvalid() {
        boolean hasil = serviceInventaris.updateStok("", 5);
        assertFalse(hasil);
    }

    @Test
    void testUpdateStokGagalProdukTidakAda() {
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.empty());

        boolean hasil = serviceInventaris.updateStok("PROD001", 5);

        assertFalse(hasil);
    }

    // ========== Keluar Stok ==========
    @Test
    void testKeluarStokBerhasil() {
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.of(produkTest));
        when(mockRepositoryProduk.updateStok("PROD001", 5)).thenReturn(true);

        boolean hasil = serviceInventaris.keluarStok("PROD001", 5);

        assertTrue(hasil);
    }

    @Test
    void testKeluarStokGagalJumlahTidakValid() {
        boolean hasil = serviceInventaris.keluarStok("PROD001", 0);
        assertFalse(hasil);
    }

    @Test
    void testKeluarStokGagalProdukTidakAktif() {
        produkTest.setAktif(false);
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.of(produkTest));

        boolean hasil = serviceInventaris.keluarStok("PROD001", 5);

        assertFalse(hasil);
    }

    @Test
    void testKeluarStokGagalStokKurang() {
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.of(produkTest));

        boolean hasil = serviceInventaris.keluarStok("PROD001", 20);

        assertFalse(hasil);
    }

    // ========== Masuk Stok ==========
    @Test
    void testMasukStokBerhasil() {
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.of(produkTest));
        when(mockRepositoryProduk.updateStok("PROD001", 15)).thenReturn(true);

        boolean hasil = serviceInventaris.masukStok("PROD001", 5);

        assertTrue(hasil);
    }

    @Test
    void testMasukStokGagalJumlahTidakValid() {
        boolean hasil = serviceInventaris.masukStok("PROD001", 0);
        assertFalse(hasil);
    }

    @Test
    void testMasukStokGagalProdukTidakAktif() {
        produkTest.setAktif(false);
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.of(produkTest));

        boolean hasil = serviceInventaris.masukStok("PROD001", 5);

        assertFalse(hasil);
    }

    // ========== Get Produk ==========
    @Test
    void testGetProdukStokMenipis() {
        when(mockRepositoryProduk.cariProdukStokMenipis()).thenReturn(List.of(produkTest));

        List<Produk> hasil = serviceInventaris.getProdukStokMenipis();

        assertEquals(1, hasil.size());
    }

    @Test
    void testGetProdukStokHabis() {
        when(mockRepositoryProduk.cariProdukStokHabis()).thenReturn(List.of(produkTest));

        List<Produk> hasil = serviceInventaris.getProdukStokHabis();

        assertEquals(1, hasil.size());
    }

    // ========== Hitung Inventaris ==========
    @Test
    void testHitungTotalNilaiInventaris() {
        Produk p1 = new Produk("P1", "Laptop", "Elektronik", 10000, 2, 1);
        Produk p2 = new Produk("P2", "Mouse", "Elektronik", 5000, 3, 1);
        Produk p3 = new Produk("P3", "Keyboard", "Elektronik", 2000, 5, 1);
        p3.setAktif(false);

        when(mockRepositoryProduk.cariSemua()).thenReturn(Arrays.asList(p1, p2, p3));

        double total = serviceInventaris.hitungTotalNilaiInventaris();

        assertEquals((10000 * 2) + (5000 * 3), total, 0.001);
    }

    @Test
    void testHitungTotalStok() {
        Produk p1 = new Produk("P1", "Laptop", "Elektronik", 10000, 2, 1);
        Produk p2 = new Produk("P2", "Mouse", "Elektronik", 5000, 3, 1);
        Produk p3 = new Produk("P3", "Keyboard", "Elektronik", 2000, 5, 1);
        p3.setAktif(false);

        when(mockRepositoryProduk.cariSemua()).thenReturn(Arrays.asList(p1, p2, p3));

        int totalStok = serviceInventaris.hitungTotalStok();

        assertEquals(2 + 3, totalStok);
    }
}
