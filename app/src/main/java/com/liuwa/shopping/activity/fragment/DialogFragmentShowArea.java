package com.liuwa.shopping.activity.fragment;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.liuwa.shopping.R;
import com.liuwa.shopping.adapter.AddressItemAdapter;
import com.liuwa.shopping.adapter.GuiGeAdapter;
import com.liuwa.shopping.client.Constants;
import com.liuwa.shopping.client.LKAsyncHttpResponseHandler;
import com.liuwa.shopping.client.LKHttpRequest;
import com.liuwa.shopping.client.LKHttpRequestQueue;
import com.liuwa.shopping.client.LKHttpRequestQueueDone;
import com.liuwa.shopping.model.AreaModel;
import com.liuwa.shopping.model.ProductChildModel;
import com.liuwa.shopping.util.DisplayHelper;
import com.liuwa.shopping.util.Md5SecurityUtil;
import com.liuwa.shopping.util.MoneyUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.TreeMap;


/**
 * author : wizardev
 * e-mail : wizarddev@163.com
 * time   : 2017/09/06
 * desc   :
 * version: 1.0
 */
public class DialogFragmentShowArea extends DialogFragment implements AdapterView.OnItemClickListener{
    private static final String MARGIN = "margin";
    private static final String WIDTH = "width";
    private static final String HEIGHT = "height";
    private static final String DIM = "dim_amount";
    private static final String BOTTOM = "show_bottom";
    private static final String CANCEL = "out_cancel";
    private static final String ANIM = "anim_style";
    private static final String LAYOUT = "layout_id";
    private int margin;//左右边距
    private int width;//宽度
    private int height;//高度
    private float dimAmount = 0.5f;//灰度深浅
    private boolean showBottom = true;//是否底部显示
    private boolean outCancel = true;//是否点击外部取消
    @StyleRes
    private int animStyle;
    @LayoutRes
    protected int layoutId;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    public ArrayList<AreaModel> areaModels=new ArrayList<>();

    // TODO: Rename and change types of parameters
    private String mParam1="";

    private OnFragmentInteractionListener mListener;
    private ListView listView;
    private AddressItemAdapter adapter;

    public DialogFragmentShowArea() {
        // Required empty public constructor
    }


    //public abstract void convertView(ViewHolder holder, MyBaseDialogFragment dialog);

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DialogFragmentShowArea newInstance(String param1) {
        DialogFragmentShowArea fragment = new DialogFragmentShowArea();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.NiceDialog);

        //恢复保存的数据
        if (savedInstanceState != null) {
            margin = savedInstanceState.getInt(MARGIN);
            width = savedInstanceState.getInt(WIDTH);
            height = savedInstanceState.getInt(HEIGHT);
            dimAmount = savedInstanceState.getFloat(DIM);
            showBottom = savedInstanceState.getBoolean(BOTTOM);
            outCancel = savedInstanceState.getBoolean(CANCEL);
            animStyle = savedInstanceState.getInt(ANIM);
            layoutId = savedInstanceState.getInt(LAYOUT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_list_layout, container, false);
        listView=(ListView)view.findViewById(R.id.lv_show_list);
        listView.setOnItemClickListener(this);
        adapter=new AddressItemAdapter(getActivity(),areaModels);
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initParams();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadData();
    }
    /**
     * 屏幕旋转等导致DialogFragment销毁后重建时保存数据
     *
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(MARGIN, margin);
        outState.putInt(WIDTH, width);
        outState.putInt(HEIGHT, height);
        outState.putFloat(DIM, dimAmount);
        outState.putBoolean(BOTTOM, showBottom);
        outState.putBoolean(CANCEL, outCancel);
        outState.putInt(ANIM, animStyle);
        outState.putInt(LAYOUT, layoutId);
    }

    private void initParams() {
        Window window = getDialog().getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            //调节灰色背景透明度[0-1]，默认0.5f
            lp.dimAmount = dimAmount;
            //是否在底部显示
            if (showBottom) {
                lp.gravity = Gravity.BOTTOM;
                if (animStyle == 0) {
                    animStyle = R.style.DefaultAnimation;
                }
            }

            //设置dialog宽度
            if (width == 0) {
                lp.width = DisplayHelper.getScreenWidth(getActivity()) - 2 * DisplayHelper.dp2px(getActivity(), margin);
            } else {
                lp.width = DisplayHelper.dp2px(getActivity(), width);
            }
            //设置dialog高度
            if (height == 0) {
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            } else {
                lp.height = DisplayHelper.dp2px(getActivity(), height);
            }

            //设置dialog进入、退出的动画
            window.setWindowAnimations(animStyle);
            window.setAttributes(lp);
        }
        setCancelable(outCancel);
    }

    public DialogFragmentShowArea setMargin(int margin) {
        this.margin = margin;
        return this;
    }

    public DialogFragmentShowArea setWidth(int width) {
        this.width = width;
        return this;
    }

    public DialogFragmentShowArea setHeight(int height) {
        this.height = height;
        return this;
    }

    public DialogFragmentShowArea setDimAmount(float dimAmount) {
        this.dimAmount = dimAmount;
        return this;
    }

    public DialogFragmentShowArea setShowBottom(boolean showBottom) {
        this.showBottom = showBottom;
        return this;
    }

    public DialogFragmentShowArea setOutCancel(boolean outCancel) {
        this.outCancel = outCancel;
        return this;
    }

    public DialogFragmentShowArea setAnimStyle(@StyleRes int animStyle) {
        this.animStyle = animStyle;
        return this;
    }

    public DialogFragmentShowArea show(FragmentManager manager) {
        super.show(manager, String.valueOf(System.currentTimeMillis()));
        return this;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String id,String areaname) {
        if (mListener != null) {
            mListener.onFragmentInteraction(id,areaname);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DialogFragmentShowArea.OnFragmentInteractionListener) {
            mListener = (DialogFragmentShowArea.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AreaModel model= (AreaModel) parent.getAdapter().getItem(position);
        onButtonPressed(model.areaid,model.areaname);
        dismiss();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String id, String  areaname);
    }
    private void loadData(){
        TreeMap<String, Object> productParam = new TreeMap<String, Object>();
        if(mParam1.length()!=0){
            productParam.put("fatherid",mParam1);
        }
        productParam.put("timespan", System.currentTimeMillis()+"");
        productParam.put("sign", Md5SecurityUtil.getSignature(productParam));
        HashMap<String, Object> requestCategoryMap = new HashMap<String, Object>();
        if(mParam1.length()!=0){
            requestCategoryMap.put(Constants.kMETHODNAME,Constants.QU);
        }else if(mParam1.length()==0){
            requestCategoryMap.put(Constants.kMETHODNAME,Constants.City);
        }
        requestCategoryMap.put(Constants.kPARAMNAME, productParam);
        LKHttpRequest categoryReq = new LKHttpRequest(requestCategoryMap, getProductHandler());
        new LKHttpRequestQueue().addHttpRequest(categoryReq)
                .executeQueue(null, new LKHttpRequestQueueDone(){

                    @Override
                    public void onComplete() {
                        super.onComplete();
                    }

                });
    }

    private LKAsyncHttpResponseHandler getProductHandler(){
        return new LKAsyncHttpResponseHandler(){

            @Override
            public void successAction(Object obj) {
                String json=(String)obj;
                try {
                    JSONObject  job= new JSONObject(json);
                    int code =	job.getInt("code");
                    if(code==Constants.CODE)
                    {
                        JSONArray jsonObject = job.getJSONArray("data");
                        Gson localGson = new GsonBuilder().disableHtmlEscaping()
                                .create();
                        areaModels.clear();
                        areaModels.addAll((Collection <? extends  AreaModel>) localGson.fromJson(jsonObject.toString(),
                            new TypeToken<ArrayList<AreaModel>>() {
                            }.getType()));
                        adapter.notifyDataSetChanged();

                    }
                    else
                    {
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        };
    }
}