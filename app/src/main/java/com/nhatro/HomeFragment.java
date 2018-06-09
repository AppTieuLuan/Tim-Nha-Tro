package com.nhatro;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.nhatro.model.LocDL;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    boolean moDanhSach = true; // Đánh dấu mở danh sách hay ko
    TextView txtCheDoXem;
    ImageView imgBanDo;
    int REQUEST_CODE = 1;

    //android.support.v4.app.FragmentManager fragmentManager = getChildFragmentManager();
    private int minSlider = 0, soNguoiO = 1;
    private int maxSlider = 10000000;
    private int maxArea = 200;
    private int minArea = 0;
    private int chonSapXep = 0, tempChon = 0;
    private boolean[] selectedFacilitiess = {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false};
    private boolean giogiac = false;
    int namnu = 3;

    ArrayList<Integer> lstChonQuanHuyen;

    LocDL locDL;

    private boolean isFragmentMap = false; // Biến đánh dấu cho biết đã add fragmnent home chưa
    private boolean changeFilter = false;
    private int tinhTP; // Biến đánh dấu đang lọc theo tỉnh tp nào...
    private String tenTP; // Tên TP đang tìm kiếm
    private boolean timPhongTro, timNhaNguyenCan, timTimOGhep; // check đánh dấu các tin cần lọc
    MapFragment mapFragment = new MapFragment();
    ListFragment listFragment = new ListFragment();
    android.support.v4.app.FragmentManager fragmentManager1;

    public HomeFragment() {
        // Required empty public constructor
    }

    LinearLayout btnSapXep, btnBoLoc, btnMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_home, container, false);
        final CharSequence[] sapXep = new CharSequence[]{"Mới nhất", "Giá từ thấp đến cao", "Giá từ cao xuống thấp"};

        lstChonQuanHuyen = new ArrayList<>();

        locDL = new LocDL();
        tinhTP = 50;
        Bundle bundlemap2 = new Bundle();
        bundlemap2.putInt("giamin", locDL.getGiamin());
        bundlemap2.putInt("giamax", locDL.getGiamax());
        bundlemap2.putInt("dientichmin", locDL.getDientichmin());
        bundlemap2.putInt("dientichmax", locDL.getDientichmax());
        bundlemap2.putInt("songuoio", locDL.getSonguoio());
        bundlemap2.putString("loaitin", locDL.getLoaitin());
        bundlemap2.putString("tiennghi", locDL.getTiennghi());
        bundlemap2.putInt("doituong", locDL.getDoituong());
        bundlemap2.putInt("giogiac", locDL.getGiogiac());
        bundlemap2.putInt("idtp", locDL.getIdtp());
        bundlemap2.putString("idqh", "");
        bundlemap2.putInt("trang", 1);
        listFragment.setArguments(bundlemap2);

        fragmentManager1 = getChildFragmentManager();
        fragmentManager1.beginTransaction().add(R.id.framDanhSach, listFragment).commit();

        btnSapXep = (LinearLayout) v.findViewById(R.id.btnSapXep);
        btnBoLoc = (LinearLayout) v.findViewById(R.id.btnBoLoc);
        btnMap = (LinearLayout) v.findViewById(R.id.btnMap);
        txtCheDoXem = (TextView) v.findViewById(R.id.txtCheDoXem);
        imgBanDo = (ImageView) v.findViewById(R.id.imgBanDo);

        tinhTP = 50;
        tenTP = "Hồ Chí Minh";
        timNhaNguyenCan = false;
        timPhongTro = false;
        timTimOGhep = false;
        btnBoLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Filter.class);

                Bundle bundle = new Bundle();
                bundle.putBoolean("giogiac", giogiac);
                bundle.putBoolean("moDanhSach", moDanhSach); // nếu mở danh sách sẽ hiện thị mục chọn tp, quận huyện
                bundle.putBoolean("timNhaNguyenCan", timNhaNguyenCan);
                bundle.putBoolean("timPhongTro", timPhongTro);
                bundle.putBoolean("timTimOGhep", timTimOGhep);
                bundle.putIntegerArrayList("lstChonQuanHuyen", lstChonQuanHuyen);
                bundle.putString("tenTP", tenTP);
                bundle.putInt("maxPrice", maxSlider);
                bundle.putInt("minPrice", minSlider);
                bundle.putInt("namnu", namnu);
                bundle.putInt("minArea", minArea);
                bundle.putInt("maxArea", maxArea);
                bundle.putBooleanArray("arrFacilities", selectedFacilitiess);
                bundle.putInt("tinhTP", tinhTP);
                bundle.putInt("soNguoiO", soNguoiO);
                intent.putExtra("data", bundle);

                startActivityForResult(intent, REQUEST_CODE);


                //show it
                //bottomSheetDialogFragment.show(getChildFragmentManager(), bottomSheetDialogFragment.getTag());
            }
        });
        btnSapXep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Sắp xếp")
                        .setSingleChoiceItems(sapXep, chonSapXep, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                tempChon = which;
                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                locDL.setTrang(1);
                                chonSapXep = tempChon;
                                locDL.setOrderby(chonSapXep + 1);
                                if (moDanhSach) {
                                    listFragment.filterData(locDL);
                                } else {
                                    mapFragment.loadData(locDL);
                                }
                            }
                        });
                builder.create().show();
            }
        });
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (moDanhSach) {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.weight = 1.5f;
                    btnMap.setLayoutParams(params);
                    btnBoLoc.setLayoutParams(params);
                    btnSapXep.setVisibility(View.GONE);
                    moDanhSach = false;
                    txtCheDoXem.setText("Danh sách");
                    imgBanDo.setImageResource(R.drawable.icon_list);
                    locDL.setDanhsach(0);
                    if (isFragmentMap) {
                        fragmentManager1.beginTransaction().hide(listFragment).show(mapFragment).commit();
                        if (changeFilter) {
                            mapFragment.loadData(locDL);
                            changeFilter = false;
                        }
                    } else {
                        isFragmentMap = true;


                       /* Bundle bundle = new Bundle();
                        bundle.putString("DocNum", docNum);   //parameters are (key, value).*/

                        // mFrag.setArguments(bundle);

//                        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
//                        transaction.replace(R.id.framDanhSach, new MapFragment());
//                        transaction.commit();
                        Bundle bundlemap = new Bundle();
                        bundlemap.putInt("giamin", locDL.getGiamin());
                        bundlemap.putInt("giamax", locDL.getGiamax());
                        bundlemap.putInt("dientichmin", locDL.getDientichmin());
                        bundlemap.putInt("dientichmax", locDL.getDientichmax());
                        bundlemap.putInt("songuoio", locDL.getSonguoio());
                        bundlemap.putString("loaitin", locDL.getLoaitin());
                        bundlemap.putString("tiennghi", locDL.getTiennghi());
                        bundlemap.putInt("doituong", locDL.getDoituong());
                        bundlemap.putInt("giogiac", locDL.getGiogiac());

                        mapFragment.setArguments(bundlemap);
                        fragmentManager1.beginTransaction().add(R.id.framDanhSach, mapFragment).commit();
                        fragmentManager1.beginTransaction().hide(listFragment).show(mapFragment).commit();
                        //mapFragment.loadData(locDL);
                    }


                } else {
                    locDL.setDanhsach(1);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.weight = 1f;
                    btnMap.setLayoutParams(params);
                    btnBoLoc.setLayoutParams(params);
                    btnSapXep.setVisibility(View.VISIBLE);
                    moDanhSach = true;
                    txtCheDoXem.setText("Bản đồ");
                    imgBanDo.setImageResource(R.drawable.icon_maps);

//                    FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
//                    transaction.replace(R.id.framDanhSach, new ListFragment());
//                    transaction.commit();

                    fragmentManager1.beginTransaction().hide(mapFragment).show(listFragment).commit();
                    if (changeFilter) {
                        listFragment.filterData(locDL);
                        changeFilter = false;
                    }
                }

            }
        });


        /////

//        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
//        transaction.replace(R.id.framDanhSach, new ListFragment());
//        transaction.commit();


        ////
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (requestCode == REQUEST_CODE) {
            Bundle bundle = data.getBundleExtra("data");
            tenTP = bundle.getString("tenTP");
            soNguoiO = bundle.getInt("soNguoiO");
            locDL.setTrang(1);
            maxSlider = bundle.getInt("maxPrice");
            minSlider = bundle.getInt("minPrice");
            selectedFacilitiess = bundle.getBooleanArray("arrFacilities");
            changeFilter = bundle.getBoolean("changeFilter");
            minArea = bundle.getInt("minArea");
            maxArea = bundle.getInt("maxArea");
            tinhTP = bundle.getInt("tinhTP");
            lstChonQuanHuyen = bundle.getIntegerArrayList("lstChonQuanHuyen");
            timNhaNguyenCan = bundle.getBoolean("timNhaNguyenCan");
            timPhongTro = bundle.getBoolean("timPhongTro");
            timTimOGhep = bundle.getBoolean("timTimOGhep");
            namnu = bundle.getInt("namnu");
            giogiac = bundle.getBoolean("giogiac");

            if (giogiac) {
                locDL.setGiogiac(1);
            }
            locDL.setIdtp(tinhTP);
            locDL.setGiamin(minSlider);
            locDL.setGiamax(maxSlider);
            locDL.setDientichmin(minArea);
            locDL.setDientichmax(maxArea);
            locDL.setSonguoio(soNguoiO);
            locDL.setDoituong(namnu);

            String tempLocTin = "(";
            if (!timTimOGhep && !timPhongTro && !timNhaNguyenCan) {
                tempLocTin = "";
            } else {
                if (timNhaNguyenCan) {
                    tempLocTin = tempLocTin + "3,";
                }
                if (timPhongTro) {
                    tempLocTin = tempLocTin + "1,";
                }
                if (timTimOGhep) {
                    tempLocTin = tempLocTin + "2,";
                }

                tempLocTin = tempLocTin.substring(0, tempLocTin.length() - 1);
                tempLocTin = tempLocTin + ")";
            }

            locDL.setLoaitin(tempLocTin);
            String idqh = "";
            if (lstChonQuanHuyen.size() > 0) {
                idqh = idqh + "(";
                for (int i = 0; i < lstChonQuanHuyen.size(); i++) {
                    idqh = idqh + lstChonQuanHuyen.get(i) + ",";
                }
                idqh = idqh.substring(0, idqh.length() - 1);
                idqh = idqh + ")";
            }
            locDL.setTiennghi(bundle.getString("tiennghi"));
            locDL.setIdqh(idqh);

            if (moDanhSach) {
                locDL.setDanhsach(1);

                listFragment.filterData(locDL);

//                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
//                transaction.replace(R.id.framDanhSach, new ListFragment());
//                transaction.commit();
            } else {

                locDL.setDanhsach(0);
                //sss.setDanhsach(0);
                mapFragment.loadData(locDL);
//                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
//                transaction.replace(R.id.framDanhSach, new MapFragment());
//                transaction.commit();
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (!hidden) {
            /*SharedPreferences sharedPreferences = getContext().getSharedPreferences("Mydata", Context.MODE_PRIVATE);
            String user = sharedPreferences.getString("MyUser", "");*//*

            getUserInfo();

            if (users == null) {
                changeduser = true;
                layout1.setVisibility(View.VISIBLE);
                contain.setVisibility(View.GONE);
            } else {
                if (olduser != Integer.parseInt(users.getId())) {
                    trang = 1;
                    olduser = Integer.parseInt(users.getId());
                    NotifyFragment.GetNews getNews = new NotifyFragment.GetNews();
                    getNews.execute();
                } else {
                    layout1.setVisibility(View.GONE);
                    contain.setVisibility(View.VISIBLE);
                }


            }*/

            SharedPreferences sharedPreferences = getContext().getSharedPreferences("Mydata", Context.MODE_PRIVATE);
            int ss = sharedPreferences.getInt("ischangedState", -1);

            //Toast.makeText(getContext(), String.valueOf(ss), Toast.LENGTH_SHORT).show();

            if (ss == 1) {


                if (moDanhSach) {
                    SharedPreferences mPrefs = getActivity().getSharedPreferences("Mydata", getContext().MODE_PRIVATE);
                    SharedPreferences.Editor editor = mPrefs.edit();
                    editor.putInt("ischangedState", 0);
                    editor.commit();
                    listFragment.filterData(locDL);
                } else {

                }
            }


        }

    }
}
