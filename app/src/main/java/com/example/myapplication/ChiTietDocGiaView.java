package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.IOException;
import java.time.LocalDate;

public class ChiTietDocGiaView extends AppCompatActivity {

    DocGia dgHienTai;
    ImageView ctAnh;
    ImageButton taiAnh,xoaAnh;
    EditText ten,cmnd,ngay,thang,nam;
    RadioButton gtnam,nu;
    ThuVienDB csdl;
    String trangThaiActivity;
    String anhHT;
    MenuItem itThemMoi,itLuu,itXoa;
    private final int PICK_IMAGE_REQUEST = 71;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_doc_gia_view);
        anhXa();
        Intent itNhanDL= this.getIntent();
        String strNhan=itNhanDL.getStringExtra("MaGui");
        trangThaiActivity=strNhan;
        setDataOnCpntOncreate(strNhan);
        suKien();
    }



    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ct_doc_gia_menu,menu);
        itThemMoi=menu.findItem(R.id.itThemCT);
        itLuu=menu.findItem(R.id.itLuuCT);
        itXoa=menu.findItem(R.id.itXoaCT);
        if(trangThaiActivity.equals("them"))
        {
            itThemMoi.setEnabled(false);
            itXoa.setEnabled(false);
        }
        else
        {
            itThemMoi.setEnabled(true);
            itXoa.setEnabled(true);
        }
        return super.onCreateOptionsMenu(menu);
    }



    private void anhXa() {
        ctAnh=findViewById(R.id.ctAnh);
        taiAnh=findViewById(R.id.ctTaiAnh);
        xoaAnh=findViewById(R.id.ctXoaAnh);
        ten=findViewById(R.id.ctTen);
        cmnd=findViewById(R.id.ctCMND);
        ngay=findViewById(R.id.ctNgayDate);
        thang=findViewById(R.id.ctThangDate);
        nam=findViewById(R.id.ctNameDate);
        gtnam=findViewById(R.id.ctNAM);
        nu=findViewById(R.id.ctNU);
        csdl= new ThuVienDB(this, null,null,1);
    }

    private void setDataOnCpntOncreate(String maStr)
    {
        if(!maStr.equals("them"))
        {
            Integer madocgia = Integer.parseInt(maStr);
            dgHienTai=csdl.getDocGiaByMa(madocgia);
            showDocGia(dgHienTai);
        }
        else
        {
            dgHienTai=null;
            ten.setText("");
            cmnd.setText("");
            gtnam.setChecked(true);
            LocalDate hnay= LocalDate.now();
            ngay.setText(String.valueOf(hnay.getDayOfMonth()));
            thang.setText(String.valueOf(hnay.getMonthValue()));
            nam.setText(String.valueOf(hnay.getYear()));
            ctAnh.setImageResource(R.drawable.default_avatar);

        }
    }

    private void showDocGia(DocGia dg)
    {
        ten.setText(dg.getTenDG()+"");
        cmnd.setText(dg.getCMND()+"");
        anhHT=dg.getAnhDG();
        if(dg.isGioiTinh())
            gtnam.setChecked(true);
        else
            nu.setChecked(true);
        String ngayNhan=dg.getNgayTao()+"";
        String tachNgay=ngayNhan.substring(0,2);
        ngay.setText(tachNgay);
        String tachThang=ngayNhan.substring(3,5);
        thang.setText(tachThang);
        String tachNam=ngayNhan.substring(6,10);
        nam.setText(tachNam);
        showAnhDG(anhHT,ctAnh);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.itThoatct:
                    veDSDocGia();
                break;
            case R.id.itLuuCT:
                luuDG();
                break;
            case R.id.itXoaCT:
                if(trangThaiActivity.equals("them")==false&&dgHienTai!=null)
                {
                    boolean xoaThanhCong=csdl.deleteDocGia(dgHienTai.getMaDG());
                    if(xoaThanhCong)
                    {
                        thoatSauXoa();
                    }
                    else
                    {
                        Toast.makeText(
                                getBaseContext(), "xóa thất bại",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.itThemCT:
                dgHienTai=null;
                ten.setText("");
                cmnd.setText("");
                gtnam.setChecked(true);
                ctAnh.setImageResource(R.drawable.default_avatar);
                LocalDate hnay= LocalDate.now();
                ngay.setText(String.valueOf(hnay.getDayOfMonth()));
                thang.setText(String.valueOf(hnay.getMonthValue()));
                nam.setText(String.valueOf(hnay.getYear()));
                trangThaiActivity="them";
                itThemMoi.setEnabled(false);
                break;

            default:
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean daSuaDocGiaHT()
    {
        boolean kq=false;
        if(dgHienTai!=null)
        {
            String tenNhap=ten.getText().toString().trim();
            String cmndNhap=cmnd.getText().toString().trim();
            boolean gtNamMoi=gtnam.isChecked()?true:false;
            String ngayNhan=ngay.getText().toString().trim();
            String thangNhan=thang.getText().toString().trim();
            String namNhap=nam.getText().toString().trim();
            if(ngayNhan.length()<2) ngayNhan="0"+ngayNhan;
            if(thangNhan.length()<2)thangNhan="0"+thangNhan;

            String tenCu=dgHienTai.getTenDG()+"";
            String cmndCu=dgHienTai.getCMND()+"";
            boolean gtCu=dgHienTai.isGioiTinh();
            String ngayNhanCu=dgHienTai.getNgayTao()+"";
            String tachNgayCu=ngayNhanCu.substring(0,2);
            String tachThangCu=ngayNhanCu.substring(3,5);
            String tachNamCu=ngayNhanCu.substring(6,10);
            String anhCu=dgHienTai.getAnhDG();

            if(!tenNhap.equals(tenCu)) {
                kq = true;
                return kq;
            }
            else
                kq=false;

           if(!cmndNhap.equals(cmndCu)) {
                kq = true;
                return kq;
            }
            else
                kq=false;

            if(gtNamMoi!=gtCu) {
                kq = true;
                return kq;
            }
            else
                kq=false;

            if(!namNhap.equals(tachNamCu)) {
                kq = true;
                return kq;
            }
            else
                kq=false;

            if(!thangNhan.equals(tachThangCu)) {
                kq = true;
                return kq;
            }
            else
                kq=false;

            if(!ngayNhan.equals(tachNgayCu)) {
                kq = true;
                return kq;
            }
            else
                kq=false;

            if(!anhCu.equals(anhHT))
            {
                kq = true;
                return kq;
            }
            else
                kq=false;
        }
        return kq;
    }

    private void luuDG()
    {
        if(trangThaiActivity.equals("them"))//lưu thêm
        {
            boolean ttChuan=xacThucThongTinNhap();
           final int maMoi;
            if(ttChuan) {
                String tenNhap=ten.getText().toString().trim();
                String cmndNhap=cmnd.getText().toString().trim();
                boolean gtNhap = gtnam.isChecked()?true:false;
                String ngayNhan=ngay.getText().toString().trim();
                String thangNhan=thang.getText().toString().trim();
                String namNhap=nam.getText().toString().trim();
                if(ngayNhan.length()<2) ngayNhan="0"+ngayNhan;
                if(thangNhan.length()<2)thangNhan="0"+thangNhan;
                String ngayNhap=ngayNhan+"-"+thangNhan+"-"+namNhap;
                DocGia dgMoi= new DocGia(tenNhap,gtNhap,cmndNhap,ngayNhap,anhHT);
                maMoi=csdl.insertDocGia(dgMoi);
            }
            else
            {
                trangThaiActivity="them";
                maMoi=-1;
                Toast.makeText(
                        getBaseContext(), "xin vui lòng nhập dữ liệu đúng chuẩn",
                        Toast.LENGTH_SHORT).show();

                return;
            }
            if(maMoi>0)
            {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                trangThaiActivity="them";
                                itThemMoi.setEnabled(false);
                                dgHienTai=null;
                                ten.setText("");
                                cmnd.setText("");
                                gtnam.setChecked(true);
                                LocalDate hnay= LocalDate.now();
                                ngay.setText(String.valueOf(hnay.getDayOfMonth()));
                                thang.setText(String.valueOf(hnay.getMonthValue()));
                                nam.setText(String.valueOf(hnay.getYear()));
                                anhHT="";// tiếp tục thêm
                                ctAnh.setImageResource(R.drawable.default_avatar);
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                Intent itve = new Intent(getBaseContext(), DanhSachDocGiaView.class);
                                startActivity(itve);
                                break;
                        }
                    }
                };
                AlertDialog.Builder ab = new AlertDialog.Builder(this);
                ab.setMessage("ĐÃ THÊM THÀNH CÔNG. BẠN MUỐN TIẾP TỤC THÊM HAY TRỞ VỀ ?").setPositiveButton("TIẾP TỤC", dialogClickListener)
                        .setNegativeButton("TRỞ VỀ", dialogClickListener).show();

            }
            else
            {
                Toast.makeText(
                        getBaseContext(), "thêm thất bại",
                        Toast.LENGTH_SHORT).show();
                trangThaiActivity="them";
            }

        }
        else//lưu sửa
        {
            if(trangThaiActivity.equals("them")==false&&daSuaDocGiaHT()==true) {
                boolean ttChuan=xacThucThongTinNhap();
                if(ttChuan) {
                    boolean updateThanhCong;
                    DocGia dgUpdate=dgHienTai;
                    String tenNhap=ten.getText().toString().trim();
                    dgUpdate.setTenDG(tenNhap);

                    String cmndNhap=cmnd.getText().toString().trim();
                    dgUpdate.setCMND(cmndNhap);
                    dgUpdate.setGioiTinh(gtnam.isChecked());
                    String ngayNhan=ngay.getText().toString().trim();
                    String thangNhan=thang.getText().toString().trim();
                    String namNhap=nam.getText().toString().trim();
                    if(ngayNhan.length()<2) ngayNhan="0"+ngayNhan;
                    if(thangNhan.length()<2)thangNhan="0"+thangNhan;
                    dgUpdate.setNgayTao(ngayNhan+"-"+thangNhan+"-"+namNhap);

                    dgUpdate.setAnhDG(anhHT);

                    updateThanhCong=csdl.updateDocGia(dgUpdate);
                    if(updateThanhCong) {
                        dgHienTai=dgUpdate;
                        trangThaiActivity=dgHienTai.getMaDG().toString();
                        Toast.makeText(
                                getBaseContext(), "Cập nhật thành công",
                                Toast.LENGTH_SHORT).show();
                    }
                    else {
                        trangThaiActivity=dgHienTai.getMaDG().toString();
                        Toast.makeText(
                                getBaseContext(), "Cập nhật thất bại do lỗi chương trình",
                                Toast.LENGTH_SHORT).show();
                    }

                }
                    else
                {
                    Toast.makeText(
                            getBaseContext(), "Cập nhật chưa thành công! vui lòng điền đúng thông tin",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            else {
                inThongBaoMotChieu("chưa có dữ liệu nào thay đổi");
            }
        }


    }

    private void veDSDocGia() {
        //XEM VÀ ĐÃ CÓ THAY ĐỔI DỮ LIỆU
        if (trangThaiActivity.equals("them")) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            boolean ttChuan = xacThucThongTinNhap();
                            final int maMoi;
                            if (ttChuan) {
                                String tenNhap = ten.getText().toString().trim();
                                String cmndNhap = cmnd.getText().toString().trim();
                                boolean gtNhap = gtnam.isChecked() ? true : false;
                                String ngayNhan = ngay.getText().toString().trim();
                                String thangNhan = thang.getText().toString().trim();
                                String namNhap = nam.getText().toString().trim();
                                if (ngayNhan.length() < 2) ngayNhan = "0" + ngayNhan;
                                if (thangNhan.length() < 2) thangNhan = "0" + thangNhan;
                                String ngayNhap = ngayNhan + "-" + thangNhan + "-" + namNhap;
                                DocGia dgMoi = new DocGia(tenNhap, gtNhap, cmndNhap, ngayNhap, anhHT);
                                maMoi = csdl.insertDocGia(dgMoi);
                            } else {
                                trangThaiActivity = "them";
                                maMoi = -1;
                                Toast.makeText(
                                        getBaseContext(), "xin vui lòng nhập dữ liệu đúng chuẩn",
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (maMoi > 0) {
                                Toast.makeText(
                                        getBaseContext(), "đã thêm thành công",
                                        Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(
                                        getBaseContext(), "thêm thất bại do lỗi chương trình",
                                        Toast.LENGTH_SHORT).show();
                                trangThaiActivity = "them";
                            }
                            Intent itve1 = new Intent(getBaseContext(), DanhSachDocGiaView.class);
                            startActivity(itve1);
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            Intent itve2 = new Intent(getBaseContext(), DanhSachDocGiaView.class);
                            startActivity(itve2);
                            break;
                    }
                }
            };
            AlertDialog.Builder ab = new AlertDialog.Builder(this);
            ab.setMessage("THÔNG TIN THÊM MỚI CHƯA ĐƯỢC LƯU. BẠN CÓ MUỐN LƯU KHÔNG?").setPositiveButton("CÓ", dialogClickListener)
                    .setNegativeButton("KHÔNG", dialogClickListener).show();
        }
        else
        {
            if(trangThaiActivity.equals("them")==false&&daSuaDocGiaHT()==true)
            {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                boolean ttChuan= xacThucThongTinNhap();
                                boolean updateThanhCong;
                                if(ttChuan)
                                {
                                    DocGia dgUpdate=dgHienTai;
                                    String tenNhap=ten.getText().toString().trim();
                                    dgUpdate.setTenDG(tenNhap);
                                    String cmndNhap=cmnd.getText().toString().trim();
                                    dgUpdate.setCMND(cmndNhap);
                                    dgUpdate.setGioiTinh(gtnam.isChecked());
                                    String ngayNhan=ngay.getText().toString().trim();
                                    String thangNhan=thang.getText().toString().trim();
                                    String namNhap=nam.getText().toString().trim();
                                    if(ngayNhan.length()<2) ngayNhan="0"+ngayNhan;
                                    if(thangNhan.length()<2)thangNhan="0"+thangNhan;
                                    dgUpdate.setNgayTao(ngayNhan+"-"+thangNhan+"-"+namNhap);
                                    dgUpdate.setAnhDG(anhHT);
                                    updateThanhCong=csdl.updateDocGia(dgUpdate);
                                }
                                else
                                {
                                    updateThanhCong=false;
                                }
                                if(ttChuan&&updateThanhCong)
                                {
                                    Intent itve = new Intent(getBaseContext(), DanhSachDocGiaView.class);
                                    Toast.makeText(
                                            getBaseContext(), "cập nhật thành công",
                                            Toast.LENGTH_SHORT).show();
                                    startActivity(itve);

                                }
                                else
                                {
                                    if (ttChuan)
                                    {
                                        Toast.makeText(
                                                getBaseContext(), "dư liệu đã đúng chuẩn nhưng bị lỗi xử lý",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Toast.makeText(
                                                getBaseContext(), "Cập nhật chưa thành công! vui lòng điền đúng thông tin",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                }
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                //THÊM MỚI
                                Intent itve = new Intent(getBaseContext(), DanhSachDocGiaView.class);
                                startActivity(itve);
                                break;
                        }
                    }
                };
                AlertDialog.Builder ab = new AlertDialog.Builder(this);
                ab.setMessage("THÔNG TIN ĐÃ BỊ THAY ĐỔI. BẠN CÓ MUỐN LƯU KHÔNG ?").setPositiveButton("CÓ", dialogClickListener)
                        .setNegativeButton("KHÔNG", dialogClickListener).show();
            }
            else
            {
                Intent itve = new Intent(getBaseContext(), DanhSachDocGiaView.class);
                startActivity(itve);
            }
        }

    }
    private void suKien() {
        xoaAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(anhHT.length()!=0)
                {
                    anhHT="";
                    showAnhDG(anhHT,ctAnh);

                }
                else
                {
                    inThongBaoMotChieu("đây là ảnh mặc định không thể xóa");
                }
            }
        });

        taiAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
    }
    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            Uri filePath = data.getData();
            anhHT=filePath.toString();
            showAnhDG(anhHT,ctAnh);
        }
        else
        {
            showAnhDG(anhHT,ctAnh);
        }
    }

    private boolean xacThucThongTinNhap()
    {
        boolean kq=true;
        String tenNhap=ten.getText().toString().trim();
        if(tenNhap.length()<1) {
            inThongBaoMotChieu("lưu thất bại! tên đăng nhập không được để trống");
            return false;
        }
        else
            kq=true;

        String cmndNhap=cmnd.getText().toString().trim();
        if(cmndNhap.length()!=9){
            inThongBaoMotChieu("lưu thất bại! cmnd phải dài đúng 9 chữ số");
            return false;
        }
        else
            kq=true;

       int namNhap=(int)Float.parseFloat(nam.getText().toString().trim());
       int thangNhap=(int)Float.parseFloat(thang.getText().toString().trim());
       int ngayNhap=  (int)Float.parseFloat(ngay.getText().toString().trim());
        //inThongBaoMotChieu("nam nhập" +namNhap);
      if(!functionLibary.laNgayHopLe(ngayNhap,thangNhap,namNhap))
       {
           inThongBaoMotChieu("lưu thất bại! ngày nhập vào không hợp lệ");
           return false;

       }
      else
          kq=true;

        return kq;
    }
    private void inThongBaoMotChieu(String cauThongBao)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(ChiTietDocGiaView.this).create();
        alertDialog.setTitle("Lỗi !");
        alertDialog.setMessage(cauThongBao);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();

    }
    private  void thoatSauXoa()
    {
        Intent itve = new Intent(getBaseContext(), DanhSachDocGiaView.class);
        Toast.makeText(
                getBaseContext(), "xóa thành công",
                Toast.LENGTH_SHORT).show();
        startActivity(itve);
    }

    @Override
    public void onBackPressed() {
        veDSDocGia();
        //super.onBackPressed();
    }


    private void showAnhDG(String pathAnh,ImageView imageView)
    {
        //trường hợp ảnh rỗng
        if(pathAnh.length()<=0)
        {
            int idAnh = getResources().getIdentifier("default_avatar", "drawable", getPackageName());
            String anhStringpath = functionLibary.getURLForResource(idAnh);
            Uri anhUri = Uri.parse(anhStringpath);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), anhUri);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            //trường hợp 1 trong 9 ảnh khởi đầu
            if(pathAnh.contains(".jpg")&&pathAnh.length()<=10)
                {
                   String anhHien=pathAnh.substring(0,pathAnh.length()-4);
                   //xoá phần đuôi mở rộng jpg
                        int idAnh = getResources().getIdentifier(anhHien, "drawable", getPackageName());
                        String anhStringpath = functionLibary.getURLForResource(idAnh);
                        Uri anhUri = Uri.parse(anhStringpath);
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), anhUri);
                            imageView.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                }
            else
            {
                Uri anhUri = Uri.parse(pathAnh);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), anhUri);
                    imageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}