package com.example.hi.tugasakhirproyek.Model;

/**
 * Created by Hi on 20/01/2018.
 */

public class info_terbaru {
    public String id_info, info_gambar, judul_gambar;

    public info_terbaru()
    {

    }
    public info_terbaru(String id_info, String info_gambar, String judul_gambar) {
        this.id_info = id_info;
        this.info_gambar = info_gambar;
        this.judul_gambar = judul_gambar;
    }

    public String getId_info() {
        return id_info;
    }

    public void setId_info(String id_info) {
        this.id_info = id_info;
    }

    public String getInfo_gambar() {
        return info_gambar;
    }

    public void setInfo_gambar(String info_gambar) {
        this.info_gambar = info_gambar;
    }

    public String getJudul_gambar() {
        return judul_gambar;
    }

    public void setJudul_gambar(String judul_gambar) {
        this.judul_gambar = judul_gambar;
    }
}
