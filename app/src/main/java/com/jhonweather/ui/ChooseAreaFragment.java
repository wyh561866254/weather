package com.jhonweather.ui;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.jhonweather.R;
import com.jhonweather.db.City;
import com.jhonweather.db.County;
import com.jhonweather.db.Province;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/1/10.
 */

public class ChooseAreaFragment extends Fragment {
    public static final int LEVEL_PROVINCE= 0;
    public static final int LEVEL_CITY= 1;
    public static final int LEVEL_COUNTY= 2;
    private ProgressDialog progressDialog;
    private TextView titleText;
    private Button backButton;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> dataList= new ArrayList<>();
    private List<Province> provinceList;
    private List<City> cityList;
    private List<County> countyList;
    private Province selectedProvince;
    private City selectedCity;
    private int currentLevel;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.choose_area,container,false);
        titleText = (TextView) view.findViewById(R.id.back_button);
        backButton= ((Button) view.findViewById(R.id.back_button));
        listView  = ((ListView) view.findViewById(R.id.list_view));
        adapter   = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,dataList);
        return view;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentLevel== LEVEL_PROVINCE){
                    selectedProvince= provinceList.get(position);
                    queryCites();

                }else if (currentLevel==LEVEL_CITY){
                    selectedCity = cityList.get(position);
                    queryCounties();


                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentLevel==LEVEL_COUNTY){
                    queryCites();
                }
                else if (currentLevel==LEVEL_CITY){
                    queryProvinces();
                }
            }
        });


    }



    /**
      * 查询全国所有的省，优先从数据库查询，如果没有查询到再去服务器上查询
      */

    private void queryProvinces() {
        titleText.setText("中国");
        backButton.setVisibility(View.GONE);
        provinceList= DataSupport.findAll(Province.class);
        if (provinceList.size()>0){
            dataList.clear();
            for (Province province :provinceList){

                dataList.add(province.getProvinceName());

            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel=LEVEL_PROVINCE;

        }else{
            String address = "http://guolin.tech/api/china";
            queryFromSever(address,"province");
        }




    }


    //查询选中省内所有的市，优先从数据库查询，如没有再到服务器查询

    private void queryCites() {
        titleText.setText(selectedProvince.getProvinceName());
        backButton.setVisibility(View.VISIBLE);
        cityList=DataSupport.where("provinceid = ? ",
                String.valueOf(selectedProvince.getId())).find(City.class);
        if (cityList.size()>0){
            dataList.clear();
            for (City city :cityList){
                dataList.add(city.getCityName());

            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel=LEVEL_CITY;
        }else{
            int provinceCode= selectedProvince.getProvinceCode();
            String address= "http://guolin.tech/api/china"+provinceCode;
            queryFromSever(address,"city");

        }


    }

        //查询选中城市中所有的县，优先从数据库查询，如没有再到服务器查询
    private void queryCounties() {
        titleText.setText(selectedCity.getCityName());
        backButton.setVisibility(View.VISIBLE);
        countyList=DataSupport.where("cityid= ?",String.valueOf(selectedCity.getId()))
                .find(County.class);
        if (countyList.size()>0){

            dataList.clear();
            for (County county :countyList){
                dataList.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel=LEVEL_COUNTY;
        }else{
            int provinceCode = selectedProvince.getProvinceCode();
            int cityCode = selectedCity.getCityCode();
            String address ="http://guolin.tech/api/china"+provinceCode +"/"+cityCode;
            queryFromSever(address,"county");
        }
    }

    private void queryFromSever(String address, String province) {
    }
}
