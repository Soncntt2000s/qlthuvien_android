package com.example.myapplication;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import java.util.ArrayList;
import java.util.List;
import androidx.annotation.Nullable;

public class ThuVienDB extends SQLiteOpenHelper {
    private static final String tenCSDL = "ThuVienCSDL";
    private static final int sqlVersion = 1;
    private static final String tblDocGia = "docgia";



    public ThuVienDB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context,tenCSDL, factory, sqlVersion);
    }

    // Phương thức này tự động gọi nếu khi tạo đối tượng
    // nếu trong csdl chưa có bảng docgia thì sẽ tự động tạo
    @Override
    public void onCreate(SQLiteDatabase db) {
        // chuỗi mặc định tạo bảng khi khởi chạy đối tượng
        taoBangDocGia(db);


    }
    // thức này tự động gọi khi đã có DB trên Storage, nhưng phiên bản khác
    //với DATABASE_VERSION
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if(newVersion>oldVersion)
        {
            xoaBangDocGia(db);
            taoBangDocGia(db);
        }
        onCreate(db);
    }



    //Lấy một đối tượng đọc giả khi biết maDG
    public DocGia getDocGiaByMa(Integer maDG) {
        DocGia kq = null;
        SQLiteDatabase db = getReadableDatabase();
        String sql ="SELECT TenDG,GioiTinh,CMND,NgayTao,AnhDG FROM docgia WHERE MaDG= ?;";
        Cursor bangDL = db.rawQuery(sql,new String[]{maDG.toString()+""});
        if (bangDL.getCount() > 0) // nếu bảng dữ liệu có kế quả
        {
            // lấy dòng dữ liệu đầu tiên
            bangDL.moveToFirst();
            String nten= bangDL.getString(0);//1
            boolean ngioitinh=bangDL.getInt(1)==1?true:false;//2 true là nam , false là nữ
            String ncmnd=bangDL.getString(2);//3
            String nngaylap=bangDL.getString(3);//3
            String nanh=bangDL.getString(4);//4
            //tạo đối tượng để trả về cho tên hàm
            kq=new DocGia(maDG,nten,ngioitinh,ncmnd,nngaylap,nanh);
            //(Integer maDG, String tenDG, boolean gioiTinh, String cMND, String ngayTao, String anhDG) {
        }
        bangDL.close();
        return kq;// nếu truy vấn không có trả về 1 đối tượng null
    }


    //thêm đọc giả . trả về mã đọc giả vừa thêm nếu thành công . thất bại trả về -1
    public int insertDocGia(DocGia dg) {
        int idMoi=dg.getMaDG();
        ContentValues insertValues = new ContentValues();
        insertValues.put("TenDG",dg.getTenDG()+"");
        int gt = dg.isGioiTinh()?1:0;
        insertValues.put("GioiTinh",gt);
        insertValues.put("CMND",dg.getCMND()+"");
        insertValues.put("NgayTao",dg.getNgayTao()+"");
        insertValues.put("AnhDG",dg.getAnhDG()+"");
        SQLiteDatabase db = getWritableDatabase();
        idMoi= (int)db.insert(tblDocGia,"",insertValues);
        return idMoi;
    }

    public List<DocGia> getArrDocGia()
    {
        DocGia docNap = null ; // tạo đối tượng để nạp vào cho list trả về
        List<DocGia> kq = new ArrayList<>();
        String sql= "SELECT MaDG,TenDG,GioiTinh,CMND,NgayTao,AnhDG FROM "+ tblDocGia  ;
        SQLiteDatabase db = getReadableDatabase();
        Cursor bangDL = db.rawQuery(sql,null);
        // đưa con trỏ về dòng dữ liệu đầu tiền
        while (bangDL.moveToNext()) // chừng nào chưa chạy đến dòng cuối cùng
        {
            int nma = bangDL.getInt(0);
            String nten= bangDL.getString(1);//1
            boolean ngioitinh=bangDL.getInt(2)>0?true:false;//2 true là nam , false là nữ
            String ncmnd=bangDL.getString(3);//3
            String nngaylap=bangDL.getString(4);//4
            String nanh=bangDL.getString(5);//5
            //tạo đối tượng để trả về cho tên hàm
            docNap=new DocGia(nma,nten,ngioitinh,ncmnd,nngaylap,nanh);
            //(Integer maDG, String tenDG, boolean gioiTinh, String cMND, String ngaySinh, String sDT, String ngayTao, String anhDG) {
            // nạp đọc giả vào mảng
            kq.add(docNap);
        }
        bangDL.close();
        return  kq;
    }

    public boolean deleteDocGia(Integer maDGxoa) { // xóa thành công trả về true , ngược lại false
        SQLiteDatabase db = getWritableDatabase();
        int dong =db.delete(tblDocGia,"MaDG=?",new String[]{maDGxoa.toString()+""});
        boolean xoaThanhCong=dong>0?true:false;
        return xoaThanhCong;
    }


    public boolean updateDocGia(DocGia dgUp) //  update thành công trả về true thất bại trả về false
    {
        ContentValues upDateValues = new ContentValues();
        Integer maU = dgUp.getMaDG();
        upDateValues.put("TenDG",dgUp.getTenDG()+"");//1
        int gtU = dgUp.isGioiTinh()?1:0;
        upDateValues.put("GioiTinh",gtU);//2
        upDateValues.put("CMND",dgUp.getCMND()+"");//3
        upDateValues.put("NgayTao",dgUp.getNgayTao()+"");//4
        upDateValues.put("AnhDG",dgUp.getAnhDG()+"");//5
        SQLiteDatabase db = getWritableDatabase();
        int dongAnhHuong = db.update(tblDocGia,upDateValues,"MaDG=?",new String[]{maU.toString()+""});
        boolean upThanhCong = dongAnhHuong>0? true:false;
        return  upThanhCong;
    }
//(Integer maDG, String tenDG, boolean gioiTinh, String cMND, String ngaySinh, String sDT, String ngayTao, String anhDG) {



    public void taoLaiBangDeTest()
    {
        SQLiteDatabase db = getWritableDatabase();
        xoaBangDocGia(db);
        taoBangDocGia(db);
        taoMang9TestApp(db);
    }

    public void taoBangDocGia(SQLiteDatabase db)
    {
        String queryCreateTableDocGia =
                "CREATE TABLE IF NOT EXISTS " +tblDocGia  + " ( " +
                        "MaDG INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "TenDG VARCHAR (250) NOT NULL, " +
                        "GioiTinh BOOLEAN DEFAULT 1, "+
                        "CMND VARCHAR(10) UNIQUE ,"+
                        "NgayTao VARCHAR(14) DEFAULT ('25-08-2020'),"+
                        "AnhDG VARCHAR(300) "+
                        ")";
        db.execSQL(queryCreateTableDocGia);
    }
    public void xoaBangDocGia(SQLiteDatabase db)
    {
        //Xoá bảng cũ
        db.execSQL("DROP TABLE IF EXISTS " + tblDocGia);
    }
    public void taoMang9TestApp(SQLiteDatabase db) // TẠO RA 18 ĐỌC GIẢ ĐỂ TEST APP
    {
        db.execSQL("INSERT INTO docgia (TenDG,GioiTinh,CMND,NgayTao,AnhDG)" +
                        " VALUES (?,?,?,?,?)",
                new String[]{"Nguyễn Thị Chuyên", "0","845235846","24-08-2020", "adr1.jpg"});
        db.execSQL("INSERT INTO docgia (TenDG,GioiTinh,CMND,NgayTao,AnhDG)" +
                        " VALUES (?,?,?,?,?)",
                new String[]{"Đoàn Minh Tuấn", "1","845236512","24-08-2020", "adr2.jpg"});
        db.execSQL("INSERT INTO docgia (TenDG,GioiTinh,CMND,NgayTao,AnhDG)" +
                        " VALUES (?,?,?,?,?)",
                new String[]{"Trần Phương My", "0","845236513","24-08-2020", "adr3.jpg"});
        db.execSQL("INSERT INTO docgia (TenDG,GioiTinh,CMND,NgayTao,AnhDG)" +
                        " VALUES (?,?,?,?,?)",
                new String[]{"Đỗ Nguyên Khôi", "1","845236514","24-08-2020", "adr4.jpg"});
        db.execSQL("INSERT INTO docgia (TenDG,GioiTinh,CMND,NgayTao,AnhDG)" +
                        " VALUES (?,?,?,?,?)",
                new String[]{"Lê Như Phượng", "0","845236515","24-08-2020", "adr5.jpg"});
        db.execSQL("INSERT INTO docgia (TenDG,GioiTinh,CMND,NgayTao,AnhDG)" +
                        " VALUES (?,?,?,?,?)",
                new String[]{"Nguyễn Hồng Sơn", "1","845236516","24-08-2020", "adr6.jpg"});
        db.execSQL("INSERT INTO docgia (TenDG,GioiTinh,CMND,NgayTao,AnhDG)" +
                        " VALUES (?,?,?,?,?)",
                new String[]{"đọc giả 7", "0","845236517","24-08-2020", "adr7.jpg"});
        db.execSQL("INSERT INTO docgia (TenDG,GioiTinh,CMND,NgayTao,AnhDG)" +
                        " VALUES (?,?,?,?,?)",
                new String[]{"đọc giả 8", "1","845236518","24-08-2020", "adr8.jpg"});
        db.execSQL("INSERT INTO docgia (TenDG,GioiTinh,CMND,NgayTao,AnhDG)" +
                        " VALUES (?,?,?,?,?)",
                new String[]{"đọc giả 9", "0","845236519","24-08-2020", "adr9.jpg"});

    }
}
