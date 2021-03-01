package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class DanhSachDocGiaView extends AppCompatActivity {
    ListView listView;
    ThuVienDB csdl;
    ArrayList<DocGia> arrayList;
    ArrayList<DocGia> mangGoc;
    ThuVienAdt aDapter;
    MenuItem itDaChon,itChonTatCa,itXoaMucDaChon,itHuyChon,itTimKiem;
    SearchView searchView;
    boolean mutiSelection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_doc_gia_view);
        anhXa();
       suKien();
    }

    private void suKien() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                   if(!mutiSelection) {
                       String magui = String.valueOf(id);
                       Intent itgui = new Intent(getBaseContext(), ChiTietDocGiaView.class);
                       itgui.putExtra("MaGui", magui);
                       startActivity(itgui);
                   }
                   else
                   {
                       //nếu đã được checked thì bỏ checked và ngược lại
                       if(arrayList.get(position).isChecked())
                       {
                           arrayList.get(position).setChecked(false);
                           aDapter.notifyDataSetChanged();
                       }
                       else
                       {
                           arrayList.get(position).setChecked(true);
                           aDapter.notifyDataSetChanged();
                       }
                       int demDaChon=0;
                       for(DocGia r:arrayList)
                       {
                           if(r.isChecked())demDaChon++;
                       }
                       if(demDaChon==0)// nếu hủy hết chọn trả về single selection
                       {
                           mutiSelection=false;
                           itDaChon.setVisible(false);
                           itChonTatCa.setEnabled(false);
                           itXoaMucDaChon.setEnabled(false);
                           itHuyChon.setEnabled(false);
                       }
                       else
                       {
                           itDaChon.setVisible(true);
                           itDaChon.setTitle("ĐÃ CHỌN ( "+demDaChon+" )");
                       }

                   }
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mutiSelection=true;
                itChonTatCa.setEnabled(true);
                itXoaMucDaChon.setEnabled(true);
                itHuyChon.setEnabled(true);
                return false;
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ds_doc_gia_menu,menu);
        itDaChon=menu.findItem(R.id.itDaChon);
        itChonTatCa=menu.findItem(R.id.itChonTatCa);
        itXoaMucDaChon=menu.findItem(R.id.itXoaMucDaChon);
        itHuyChon=menu.findItem(R.id.itHuyChon);
       itTimKiem= menu.findItem(R.id.itTimDocGia);
       searchView=(SearchView) itTimKiem.getActionView();
       searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
           @Override
           public boolean onQueryTextSubmit(String query) {
               showSearchResult(query);
               return false;
           }

           @Override
           public boolean onQueryTextChange(String newText) {
               showSearchResult(newText);
               return false;
           }
       });

        return super.onCreateOptionsMenu(menu);
    }
    

    public void showSearchResult(String strResult)
    {
        arrayList.clear();
        mangGoc.clear();

        mangGoc.addAll(csdl.getArrDocGia());
        setTrangThaiBanDau();
        if(strResult.length()!=0||strResult!=null)
        {
            strResult.trim().toLowerCase();
            for(DocGia r : mangGoc)
            {
                r.setChecked(false);
                String tendg=r.getTenDG().trim().toLowerCase()+"";
                if(tendg.contains(strResult))
                {
                    arrayList.add(r);
                }
            }
        }
        else
        {
            arrayList.addAll(mangGoc);
        }
        aDapter.notifyDataSetChanged();

    }

    private void anhXa() {
        listView=findViewById(R.id.lvDSDocGia);
        csdl = new ThuVienDB(this,null,null,1);
        arrayList= new ArrayList<>();
        mangGoc=new ArrayList<>();
        arrayList.addAll(csdl.getArrDocGia());

        aDapter= new ThuVienAdt(this,arrayList);
        listView.setAdapter(aDapter);
        mutiSelection=false;

    }
private void setTrangThaiBanDau()
{
    mutiSelection=false;
    itDaChon.setVisible(false);
    itChonTatCa.setEnabled(false);
    itHuyChon.setEnabled(false);
    itXoaMucDaChon.setEnabled(false);
}


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itThoat:
                finishAffinity();
                break;// ? return true;
            case R.id.itTaoLaiDLTestApp:
                taoLaiDuLieu();
                break;
            case R.id.itChonTatCa:
                for(DocGia r:arrayList)
                {
                    r.setChecked(true);
                }
                itDaChon.setTitle("ĐÃ CHỌN ( "+arrayList.size()+" )");
                aDapter.notifyDataSetChanged();
                break;
            case R.id.itHuyChon:
                for(DocGia r:arrayList)
                {
                    r.setChecked(false);
                }
                itDaChon.setVisible(false);
                mutiSelection=false;
                itDaChon.setVisible(false);
                itChonTatCa.setEnabled(false);
                itXoaMucDaChon.setEnabled(false);
                itHuyChon.setEnabled(false);
                aDapter.notifyDataSetChanged();
                break;
            case R.id.itXoaMucDaChon:
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                        for(DocGia r:arrayList)
                        {
                                if(r.isChecked())
                                {
                                    Integer maXoa=r.getMaDG();
                                    csdl.deleteDocGia(maXoa);
                                }
                        }
                        arrayList.clear();
                        arrayList.addAll(csdl.getArrDocGia());
                        itDaChon.setVisible(false);
                        mutiSelection=false;
                        itDaChon.setVisible(false);
                        itChonTatCa.setEnabled(false);
                        itXoaMucDaChon.setEnabled(false);
                        itHuyChon.setEnabled(false);
                        aDapter.notifyDataSetChanged();
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                for(DocGia r:arrayList)
                                {
                                    r.setChecked(false);
                                }
                                itDaChon.setVisible(false);
                                mutiSelection=false;
                                itDaChon.setVisible(false);
                                itChonTatCa.setEnabled(false);
                                itXoaMucDaChon.setEnabled(false);
                                itHuyChon.setEnabled(false);
                                aDapter.notifyDataSetChanged();
                                break;
                        }
                    }
                };
                AlertDialog.Builder ab = new AlertDialog.Builder(this);
                ab.setMessage("BẠN CÓ CHẮC MUỐN XÓA ?").setPositiveButton("CÓ", dialogClickListener)
                        .setNegativeButton("KHÔNG", dialogClickListener).show();
                break;
            case R.id.itThemMoiDG:
                moChiTietDocGiaDeThem();
                break;
            case R.id.home:

                break;
            default:

        }
        return super.onOptionsItemSelected(item);
    }

    private void moChiTietDocGiaDeThem()
    {
        Intent itgui = new Intent(getBaseContext(), ChiTietDocGiaView.class);
        itgui.putExtra("MaGui", "them");
        startActivity(itgui);
    }
    private  void taoLaiDuLieu()
    {
        csdl.taoLaiBangDeTest();
        arrayList.clear();
        arrayList.addAll(csdl.getArrDocGia());
        itDaChon.setVisible(false);
        mutiSelection=false;
        itDaChon.setVisible(false);
        itChonTatCa.setEnabled(false);
        itXoaMucDaChon.setEnabled(false);
        itHuyChon.setEnabled(false);
        aDapter.notifyDataSetChanged();
    }

    public void inThongBaoToast(String chuoiCanIn)
    {
        Toast.makeText(
                getBaseContext(), chuoiCanIn,
                Toast.LENGTH_SHORT).show();
    }

    private void inThongBaoMotChieu(String cauThongBao)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(DanhSachDocGiaView.this).create();
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

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}