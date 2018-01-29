package com.example.hi.tugasakhirproyek.Model;

/**
 * Created by gombloh on 29/01/18.
 */

public class pemesanan {
    String id_pemesan, email, id_kost, nama_kost, status;

    public pemesanan() {
    }

    public pemesanan(String id_pemesan, String email, String id_kost, String nama_kost, String status) {
        this.id_pemesan = id_pemesan;
        this.email = email;
        this.id_kost = id_kost;
        this.nama_kost = nama_kost;
        this.status = status;
    }

    public String getId_pemesan() {
        return id_pemesan;
    }

    public void setId_pemesan(String id_pemesan) {
        this.id_pemesan = id_pemesan;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
