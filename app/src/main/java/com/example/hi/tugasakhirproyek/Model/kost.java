package com.example.hi.tugasakhirproyek.Model;

/**
 * Created by Hi on 19/01/2018.
 */

public class kost {
    public String id_kost,nama_kost,jenis_kost,notelp_pemilik, deskripsi,luas_kamar,alamat_kost,foto;
    public int stok_kamar,biaya_sewa;
    public double latitude, longitude;

    public kost(){

    }
    public kost(String id_kost, String nama_kost, String jenis_kost, String notelp_pemilik, String deskripsi, String luas_kamar, String alamat_kost, String foto, int stok_kamar, int biaya_sewa, double latitude, double longitude) {

        this.id_kost = id_kost;
        this.nama_kost = nama_kost;
        this.jenis_kost = jenis_kost;
        this.notelp_pemilik = notelp_pemilik;
        this.deskripsi = deskripsi;
        this.luas_kamar = luas_kamar;
        this.alamat_kost = alamat_kost;
        this.foto = foto;
        this.stok_kamar = stok_kamar;
        this.biaya_sewa = biaya_sewa;
        this.latitude = latitude;
        this.longitude = longitude;

    }

    public String getId_kost() {
        return id_kost;
    }

    public void setId_kost(String id_kost) {
        this.id_kost = id_kost;
    }

    public String getNama_kost() {
        return nama_kost;
    }

    public void setNama_kost(String nama_kost) {
        this.nama_kost = nama_kost;
    }

    public String getJenis_kost() {
        return jenis_kost;
    }

    public void setJenis_kost(String jenis_kost) {
        this.jenis_kost = jenis_kost;
    }

    public String getNotelp_pemilik() {
        return notelp_pemilik;
    }

    public void setNotelp_pemilik(String notelp_pemilik) {
        this.notelp_pemilik = notelp_pemilik;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getLuas_kamar() {
        return luas_kamar;
    }

    public void setLuas_kamar(String luas_kamar) {
        this.luas_kamar = luas_kamar;
    }

    public String getAlamat_kost() {
        return alamat_kost;
    }

    public void setAlamat_kost(String alamat_kost) {
        this.alamat_kost = alamat_kost;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public int getStok_kamar() {
        return stok_kamar;
    }

    public void setStok_kamar(int stok_kamar) {
        this.stok_kamar = stok_kamar;
    }

    public int getBiaya_sewa() {
        return biaya_sewa;
    }

    public void setBiaya_sewa(int biaya_sewa) {
        this.biaya_sewa = biaya_sewa;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
