package com.liuwa.shopping.activity.fragment;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.liuwa.shopping.R;
import com.liuwa.shopping.adapter.GuiGeAdapter;
import com.liuwa.shopping.model.ProductChildModel;
import com.liuwa.shopping.util.DisplayHelper;
import com.liuwa.shopping.util.MoneyUtils;
import com.liuwa.shopping.view.HorizontalListView;

import java.math.BigDecimal;
import java.util.ArrayList;


/**
 * author : wizardev
 * e-mail : wizarddev@163.com
 * time   : 2017/09/06
 * desc   :
 * version: 1.0
 */
public class DialogFragmentFromBottom extends DialogFragment implements  View.OnClickListener,AdapterView.OnItemClickListener{
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
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private ArrayList<ProductChildModel> mParam2=new ArrayList<>();

    private OnFragmentInteractionListener mListener;
    private TextView tv_name;
    private TextView tv_price;
    private TextView iv_sub,iv_add,tv_commodity_show_num,tv_ok;
    private GridView hlv;
    private GuiGeAdapter adapter;
    private ImageView img_show;
    private ImageView img_close;
    //选择的数量和属性
    private String selectProid;
    private int num=1;

    public DialogFragmentFromBottom() {
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
    public static DialogFragmentFromBottom newInstance(String param1, ArrayList<ProductChildModel> prochildid) {
        DialogFragmentFromBottom fragment = new DialogFragmentFromBottom();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        //args.putParcelableArrayList(ARG_PARAM2, prochildid);
        args.putSerializable(ARG_PARAM2,prochildid);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = (ArrayList<ProductChildModel>)getArguments().getSerializable(ARG_PARAM2);
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
        View view = inflater.inflate(R.layout.activity_bottom_product_detail_layout, container, false);
        tv_name=(TextView)view.findViewById(R.id.tv_name);
        img_show=(ImageView)view.findViewById(R.id.img_show);
        img_close=(ImageView)view.findViewById(R.id.img_close);
        tv_price=(TextView)view.findViewById(R.id.tv_price);
        iv_sub=(TextView)view.findViewById(R.id.iv_sub);
        iv_add=(TextView)view.findViewById(R.id.iv_add);
        tv_ok=(TextView)view.findViewById(R.id.tv_ok);
        tv_commodity_show_num=(TextView)view.findViewById(R.id.tv_commodity_show_num);
        hlv=(GridView)view.findViewById(R.id.hlv);
        hlv.setOnItemClickListener(this);
        tv_ok.setOnClickListener(this);
        iv_sub.setOnClickListener(this);
        iv_add.setOnClickListener(this);
        img_close.setOnClickListener(this);
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

    public DialogFragmentFromBottom setMargin(int margin) {
        this.margin = margin;
        return this;
    }

    public DialogFragmentFromBottom setWidth(int width) {
        this.width = width;
        return this;
    }

    public DialogFragmentFromBottom setHeight(int height) {
        this.height = height;
        return this;
    }

    public DialogFragmentFromBottom setDimAmount(float dimAmount) {
        this.dimAmount = dimAmount;
        return this;
    }

    public DialogFragmentFromBottom setShowBottom(boolean showBottom) {
        this.showBottom = showBottom;
        return this;
    }

    public DialogFragmentFromBottom setOutCancel(boolean outCancel) {
        this.outCancel = outCancel;
        return this;
    }

    public DialogFragmentFromBottom setAnimStyle(@StyleRes int animStyle) {
        this.animStyle = animStyle;
        return this;
    }

    public DialogFragmentFromBottom show(FragmentManager manager) {
        super.show(manager, String.valueOf(System.currentTimeMillis()));
        return this;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String selectProid,int num) {
        if (mListener != null) {
            mListener.onFragmentInteraction(selectProid,num);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DialogFragmentFromBottom.OnFragmentInteractionListener) {
            mListener = (DialogFragmentFromBottom.OnFragmentInteractionListener) context;
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_ok:
                 if(selectProid==null||selectProid.length()==0){
                     Toast.makeText(getActivity(),"请选择属性",Toast.LENGTH_SHORT).show();
                     return;
                 }
                onButtonPressed(selectProid,num);
                dismiss();
                break;
            case R.id.iv_add:
                num++;
                tv_commodity_show_num.setText(num+"");
                break;
            case R.id.img_close:
                dismiss();
                break;
            case R.id.iv_sub:
                if (num == 1) {
                    return;
                }
                num--;
                tv_commodity_show_num.setText(num+"");
                break;

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        adapter.setSelectedPosition(position);
        adapter.notifyDataSetChanged();
        ProductChildModel model= (ProductChildModel) parent.getAdapter().getItem(position);
        selectProid=model.proChildId;
        tv_price.setText(MoneyUtils.formatAmountAsString(new BigDecimal(model.salePrice)));
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
        void onFragmentInteraction(String prochildid,int num);
    }
    //装在数据
    public void loadData(){
        tv_name.setText(mParam1+"");
        adapter=new GuiGeAdapter(getActivity(),mParam2);
        hlv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}