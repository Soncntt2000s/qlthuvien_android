package com.example.myapplication;

public class DocGia {
    private Integer maDG;//0
    private String tenDG;//1
    private boolean gioiTinh;//2 true là nam , false là nữ
    private  String CMND;//3
    private String ngayTao;//6
    private String anhDG;//7
    private boolean checked;//8 . thuộc tính sử dụng cho Multiple Seletion ListView
    // thuộc tính checked chỉ thuộc về đối tượng để quản lý trong list chứ không cần quản lý trong csdl

    //constructor đầy đủ

    public DocGia(Integer maDG, String tenDG, boolean gioiTinh, String cMND, String ngayTao, String anhDG, boolean checked) {
        this.maDG = maDG;
        this.tenDG = tenDG;
        this.gioiTinh = gioiTinh;
        this.CMND = cMND;
        this.ngayTao = ngayTao;
        this.anhDG = anhDG;
        this.checked = checked;
    }
    //constructor thiếu checked(false)
    public DocGia(Integer maDG, String tenDG, boolean gioiTinh, String cMND, String ngayTao, String anhDG) {
        this.maDG = maDG;
        this.tenDG = tenDG;
        this.gioiTinh = gioiTinh;
        this.CMND = cMND;
        this.ngayTao = ngayTao;
        this.anhDG = anhDG;
        this.checked = false;
    }

    // constructor thiếu id (-1), thiếu checked (false)
    public DocGia(String tenDG, boolean gioiTinh, String cMND, String ngayTao, String anhDG) {
        this.maDG = -1;
        this.tenDG = tenDG;
        this.gioiTinh = gioiTinh;
        this.CMND = cMND;
        this.ngayTao = ngayTao;
        this.anhDG = anhDG;
        this.checked =false;
    }


    //KHU VỰC PROPERTY
    public Integer getMaDG() {
        return maDG;
    }

    public void setMaDG(Integer maDG) {
        this.maDG = maDG;
    }

    public String getTenDG() {
        return tenDG;
    }

    public void setTenDG(String tenDG) {
        this.tenDG = tenDG;
    }

    public boolean isGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(boolean gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getCMND() {
        return CMND;
    }

    public void setCMND(String cMND) {
        this.CMND = cMND;
    }


    public String getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(String ngayTao) {
        this.ngayTao = ngayTao;
    }

    public String getAnhDG() {
        return anhDG;
    }

    public void setAnhDG(String anhDG) {
        this.anhDG = anhDG;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public String toString() {
        return "DocGia{" +
                "maDG=" + maDG +
                ", tenDG='" + tenDG + '\'' +
                ", gioiTinh=" + gioiTinh +
                ", CMND='" + CMND + '\'' +
                ", ngayTao='" + ngayTao + '\'' +
                ", anhDG='" + anhDG + '\'' +
                ", checked=" + checked +
                '}';
    }
}
