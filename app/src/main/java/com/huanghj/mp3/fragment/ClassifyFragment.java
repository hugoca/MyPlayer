package com.huanghj.mp3.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.huanghj.mp3.LocalListActivity;
import com.huanghj.mp3.MainActivity;
import com.huanghj.mp3.R;
import com.huanghj.mp3.adapter.RecyclerViewAdapter;
import com.huanghj.mp3.util.MyEvent;

/**
 * A simple {@link Fragment} subclass.
 */

public class ClassifyFragment extends Fragment {

    public static final String[]  LANGUAGE={"国语","粤语","英语","韩语","法语","泰语","小语种"};
    public static final String[]  STYLE={"流行","爵士","民谣","电子","摇滚","说唱","布鲁斯","金属","轻音乐","古典","新世纪","R&B"};
    public static final String[] WHERE={"清晨","黄昏","夜晚","旅行","休憩","运动","工作","学习","夜店","酒吧","散步","聚会"};
    public static final String[] THEME={"70后","80后","90后","经典","吉他","钢琴","神曲","毕业","对唱","KTV"};
    private GridView gridView_la,gridView_st,gridView_wh,gridView_th;
    private MyGridAdapter myGridAdapter;

    private ImageView imageView1,imageView2,imageView3,imageView4;

    public ClassifyFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_classify, container, false);
        gridView_la= (GridView) view.findViewById(R.id.language_grid);
        gridView_st= (GridView) view.findViewById(R.id.style_grid);
        gridView_wh= (GridView) view.findViewById(R.id.where_grid);
        gridView_th= (GridView) view.findViewById(R.id.theme_grid);
        imageView1= (ImageView) view.findViewById(R.id.class_image1);
        imageView2= (ImageView) view.findViewById(R.id.class_image2);
        imageView3= (ImageView) view.findViewById(R.id.class_image3);
        imageView4= (ImageView) view.findViewById(R.id.class_image4);

        myGridAdapter=new MyGridAdapter(getActivity(),LANGUAGE);
        gridView_la.setAdapter(myGridAdapter);
        gridView_st.setAdapter(new MyGridAdapter(getActivity(),STYLE));
        gridView_wh.setAdapter(new MyGridAdapter(getActivity(),WHERE));
        gridView_th.setAdapter(new MyGridAdapter(getActivity(),THEME));
        gridView_la.setOnItemClickListener(new MyItemClickListener());
        gridView_st.setOnItemClickListener(new MyItemClickListener());
        gridView_wh.setOnItemClickListener(new MyItemClickListener());
        gridView_th.setOnItemClickListener(new MyItemClickListener());
        return view;
    }

    class MyItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent=new Intent(getActivity(), LocalListActivity.class);
            intent.putExtra(MainActivity.LIST,MainActivity.NET);
            startActivity(intent);
        }
    }

    static class MyGridAdapter extends BaseAdapter {
        private String[] lists;

        private Context context;
        public MyGridAdapter(Context context,String[] list){
            this.context=context;
            lists=list;
        }
        @Override
        public int getCount() {
            return lists.length;
        }

        @Override
        public Object getItem(int position) {
            return lists[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null) {  //利用convertView来优化结果，ListView中也常利用
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(R.layout.grid_item, null);
            }

            TextView view2= (TextView) convertView.findViewById(R.id.item);
            view2.setText(lists[position]);

            return convertView;
        }
    }


}
