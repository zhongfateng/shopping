package com.liuwa.shopping.activity.fragment;

import android.content.Context;
import android.media.midi.MidiOutputPort;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.liuwa.shopping.R;
import com.liuwa.shopping.model.PeiSongTime;
import com.liuwa.shopping.util.DisplayHelper;
import com.liuwa.shopping.util.ListUtils;
import com.liuwa.shopping.util.ScreenUtil;
import com.liuwa.shopping.view.WheelView.ModelWheelView;
import com.liuwa.shopping.view.WheelView.ModelWheelView.OnWheelViewListener;

import java.util.ArrayList;
import java.util.Collections;


/**
 * author : wizardev
 * e-mail : wizarddev@163.com
 * time   : 2017/09/06
 * desc   :
 * version: 1.0
 */
public class DialogFragmentSelectTimeBottom extends DialogFragment implements  View.OnClickListener{
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
    private OnFragmentInteractionListener mListener;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private TextView tv_cancel;
    private TextView tv_ok;
    private ModelWheelView wheelView,wheelViewChild;
    private String selectStr;
    private ArrayList<PeiSongTime> peiSongTimes;

    public DialogFragmentSelectTimeBottom() {
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
    public static DialogFragmentSelectTimeBottom newInstance(ArrayList<PeiSongTime> list) {
        DialogFragmentSelectTimeBottom fragment = new DialogFragmentSelectTimeBottom();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1,list);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            peiSongTimes=(ArrayList<PeiSongTime>) getArguments().getSerializable(ARG_PARAM1);
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
        View view = inflater.inflate(R.layout.activity_select_peisong_item_layout, container, false);
        tv_cancel=(TextView)view.findViewById(R.id.tv_cancel);
        tv_ok=(TextView)view.findViewById(R.id.tv_ok);
        wheelView=(ModelWheelView)view.findViewById(R.id.ww_view);
        wheelViewChild=(ModelWheelView)view.findViewById(R.id.ww_view_child);
        wheelView.setOnWheelViewListener(listener);
        wheelViewChild.setOnWheelViewListener(listener2);
        tv_cancel.setOnClickListener(this);
        tv_ok.setOnClickListener(this);
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
        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);
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

    public DialogFragmentSelectTimeBottom setMargin(int margin) {
        this.margin = margin;
        return this;
    }

    public DialogFragmentSelectTimeBottom setWidth(int width) {
        this.width = width;
        return this;
    }

    public DialogFragmentSelectTimeBottom setHeight(int height) {
        this.height = height;
        return this;
    }

    public DialogFragmentSelectTimeBottom setDimAmount(float dimAmount) {
        this.dimAmount = dimAmount;
        return this;
    }

    public DialogFragmentSelectTimeBottom setShowBottom(boolean showBottom) {
        this.showBottom = showBottom;
        return this;
    }

    public DialogFragmentSelectTimeBottom setOutCancel(boolean outCancel) {
        this.outCancel = outCancel;
        return this;
    }

    public DialogFragmentSelectTimeBottom setAnimStyle(@StyleRes int animStyle) {
        this.animStyle = animStyle;
        return this;
    }

    public DialogFragmentSelectTimeBottom show(FragmentManager manager) {
        super.show(manager, String.valueOf(System.currentTimeMillis()));
        return this;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(PeiSongTime.ChildModel model) {
        if (mListener != null) {
            mListener.onFragmentInteraction(model);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DialogFragmentSelectTimeBottom.OnFragmentInteractionListener) {
            mListener = (DialogFragmentSelectTimeBottom.OnFragmentInteractionListener) context;
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
                 if(model==null){
                     Toast.makeText(getActivity(),"请选择配送时间",Toast.LENGTH_SHORT).show();
                     return;
                 }
                onButtonPressed( model);
                dismiss();
                break;
            case R.id.tv_cancel:
                dismiss();
                break;
        }
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
        void onFragmentInteraction(PeiSongTime.ChildModel model);
    }
    //装在数据
    public void loadData(){

        wheelView.getLayoutParams().width = ScreenUtil.getWindowsWidth(getActivity()) / 2;
        wheelViewChild.getLayoutParams().width = ScreenUtil.getWindowsWidth(getActivity()) / 2;
        ArrayList<String> list=new ArrayList<>();
        for(PeiSongTime model:peiSongTimes){
            list.add(model.day);
        }
        wheelView.setOffset(1);
        wheelView.setItems(list);
        ArrayList<String> list2=new ArrayList<>();
       for(PeiSongTime.ChildModel model:peiSongTimes.get(0).dayval)
       {
           list2.add(model.pstime);
       }
        wheelViewChild.setItems(list2);
        wheelViewChild.setOffset(1);
    }
    public int i=0;
    public int j=0;
    public PeiSongTime.ChildModel model;
    OnWheelViewListener listener=  new OnWheelViewListener(){
        @Override
        public void onSelected(int selectedIndex, String item) {
            super.onSelected(selectedIndex, item);
             i=selectedIndex-1;
            ArrayList<String> list=new ArrayList<>();
            for(PeiSongTime.ChildModel model:peiSongTimes.get(selectedIndex-1).dayval)
            {
                list.add(model.pstime);
            }
            wheelViewChild.replace(list);
            wheelViewChild.setOffset(1);
        }
    };
    OnWheelViewListener listener2=  new OnWheelViewListener(){
        @Override
        public void onSelected(int selectedIndex, String item) {
            super.onSelected(selectedIndex, item);
            j=selectedIndex-1;
            model= peiSongTimes.get(i).dayval.get(j);
            selectStr=item;
        }
    };
}