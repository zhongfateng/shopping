package com.liuwa.shopping.activity.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.liuwa.shopping.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link IntegralDialogFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link IntegralDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IntegralDialogFragment extends DialogFragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private ImageView img_close;
    private TextView gotoPay;
    private TextView tv_title,tv_content;

    private OnFragmentInteractionListener mListener;

    public IntegralDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment IntegralDialogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IntegralDialogFragment newInstance(String param1) {
        IntegralDialogFragment fragment = new IntegralDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.activity_dialog_layout, container, false);
        img_close=(ImageView) rootView.findViewById(R.id.img_close);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tv_title=(TextView)rootView.findViewById(R.id.tv_title);
        tv_content=(TextView) rootView.findViewById(R.id.tv_content);
        gotoPay=(TextView)rootView.findViewById(R.id.gotoPay);
        gotoPay.setOnClickListener(this);
        return rootView;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(mParam1.equals("0")){
            tv_title.setText("很抱歉，您的积分不足！");
            tv_content.setText("充值获取更多积分");
            gotoPay.setText("去充值");
        }else if(mParam1.equals("1")){
            tv_title.setText("恭喜您，兑换成功！");
            tv_content.setText("订单已提交至后台处理");
            gotoPay.setText("返回积分商城");
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String tag) {
        if (mListener != null) {
            mListener.onFragmentInteraction(tag);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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
        mListener.onFragmentInteraction(mParam1);
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
        void onFragmentInteraction(String tag);
    }
}
