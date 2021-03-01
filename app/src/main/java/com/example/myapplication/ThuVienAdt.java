package com.example.myapplication;

import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.BaseAdapter;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.transform.dom.DOMLocator;
public class ThuVienAdt extends BaseAdapter {
    //TRƯỜNG THUỘC TÍNH CỦA Adapter cho Listview Danh Sách ĐỌC GIẢ
    private Context contextApp;
    private List<DocGia> arrDocGia;
    private String TAG;

    //PHƯƠNG THỨC KHỞI TẠO
    public ThuVienAdt(Context contextApp, List<DocGia> arrDocGia) {
        this.contextApp = contextApp;
        this.arrDocGia = arrDocGia;
        TAG= contextApp.getClass().getSimpleName();
    }
    public ThuVienAdt() {
        this.contextApp = null;
        this.arrDocGia = new ArrayList<>();
    }

    public List<DocGia> getArrDocGia() {
        return arrDocGia;
    }
    public void setArrDocGia(List<DocGia> arrDocGia) {
        this.arrDocGia = arrDocGia;
    }

    public Context getContextApp() {
        return contextApp;
    }

    public void setContextApp(Context contextApp) {
        this.contextApp = contextApp;
    }

    // ghi đè các phương thức của lớp ảo BaseAdapter
    @Override
    public int getCount() {// trả về số lượng phần tử trong AdapterView = số phần tử trong list đọc giả truyền vào
        return arrDocGia.size();
    }
    @Override
    public Object getItem(int position) {// lấy về 1 đối tượng tại 1 ví trí trong mảng
        return arrDocGia.get(position);
    }// position là vị trí trong mảng
    @Override
    public long getItemId(int position) {
        return arrDocGia.get(position).getMaDG();
    }// lấy id của 1 đối tượng trong mảng với position ( vị trí của đối tượng trong mảng )

    @Override//ghi đè phương thức trả về view từng Item cho View Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        View lvItemView;
        // tạo view trả về cho View Adapter
        if(convertView==null)
            lvItemView=View.inflate(parent.getContext(), R.layout.doc_gia_row,null);
        else lvItemView=convertView;

        //ánh xạ các thành phần trong row_doc_gia để quản lý
        ImageView imgAnhDocGia= lvItemView.findViewById(R.id.imgAnhDocGia);
        TextView txtCMND =lvItemView.findViewById(R.id.txtCMND);
        TextView txtGioiTinh =lvItemView.findViewById(R.id.txtGioiTinh);
        TextView txtHoTen =lvItemView.findViewById(R.id.txtHoTen);
        TextView txtNgayTao =lvItemView.findViewById(R.id.txtNgayTao);

        //lấy ra đối tượng theo vị trí để truy suất thông tin
        DocGia docGia=(DocGia) getItem(position);

        // HIỂN THỊ CÁC THÔNG TIN CỦA ĐỐI TƯỢNG LÊN VIEW ( 1 ITEM CỦA LISTVIEW )
        //hiển thị các thông tin dạng text
        String cMND=" CMND: " +docGia.getCMND();
        String gioiTinh = docGia.isGioiTinh()?"nam":"nữ";
        String hoTen = docGia.getTenDG()+"";
        String ngayTao =" Ngày tạo : " +docGia.getNgayTao();

        String tenAnh = docGia.getAnhDG().trim()+"";
        // set dữ liệu vào
        txtCMND.setText(cMND);
        txtGioiTinh.setText(gioiTinh);
        txtHoTen.setText(hoTen);
        txtNgayTao.setText(ngayTao);
        showAnhDG(tenAnh,imgAnhDocGia);
        //KHU VỰC CÀI ĐẶT CHO Multiple Checked
        if(docGia.isChecked())
        {// mỗi lần cập nhật thì AdapterView sẽ duyệt cả List DocGia để tô màu những chỗ bị đánh màu
            lvItemView.setBackgroundColor(parent.getResources().getColor(R.color.my_green_listview_item,null));
        }
        else
        {// mỗi khi cập nhật những vị trí checked = false sé được trả lại màu trùng màu nền context
            lvItemView.setBackgroundColor(Color.TRANSPARENT);
            // màu trùng với màu nền ListView ( mẹ của Item )
        }
        return lvItemView;
    }

    private void showAnhDG(String pathAnh,ImageView imageView)
    {
        //trường hợp ảnh rỗng
        if(pathAnh.length()<=0)
        {
            int idAnh = contextApp.getResources().getIdentifier("default_avatar", "drawable", contextApp.getPackageName());
            String anhStringpath = functionLibary.getURLForResource(idAnh);
            Uri anhUri = Uri.parse(anhStringpath);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(contextApp.getContentResolver(), anhUri);
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
                int idAnh = contextApp.getResources().getIdentifier(anhHien, "drawable", contextApp.getPackageName());
                String anhStringpath = functionLibary.getURLForResource(idAnh);
                Uri anhUri = Uri.parse(anhStringpath);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(contextApp.getContentResolver(), anhUri);
                    imageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                Uri anhUri = Uri.parse(pathAnh);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(contextApp.getContentResolver(), anhUri);
                    imageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
