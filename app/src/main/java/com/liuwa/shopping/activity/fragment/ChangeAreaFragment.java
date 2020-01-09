package com.liuwa.shopping.activity.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.liuwa.shopping.R;
import com.liuwa.shopping.model.SheQuModel;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChangeAreaFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChangeAreaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChangeAreaFragment extends DialogFragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public SheQuModel model1;
    public SheQuModel model2;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private TextView tv_cancel,tv_confirm;
    public TextView tv_current;
    public TextView tv_select;
    private OnFragmentInteractionListener mListener;

    public ChangeAreaFragment() {
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
    public static ChangeAreaFragment newInstance(SheQuModel model1,SheQuModel model2) {
        ChangeAreaFragment fragment = new ChangeAreaFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1,model1);
        args.putSerializable(ARG_PARAM2,model2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            model1 = (SheQuModel) getArguments().getSerializable(ARG_PARAM1);
            model2 = (SheQuModel) getArguments().getSerializable(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.activity_change_layout, container, false);
        tv_current=(TextView)rootView.findViewById(R.id.tv_current);
        tv_select=(TextView)rootView.findViewById(R.id.tv_select);
        tv_confirm=(TextView) rootView.findViewById(R.id.tv_confirm);
        tv_cancel=(TextView)rootView.findViewById(R.id.tv_cancel);
        tv_current.setText(model2.region);
        tv_select.setText(model1.region);
        tv_cancel.setOnClickListener(this);
        tv_confirm.setOnClickListener(this);
        return rootView;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(SheQuModel model) {
        if (mListener != null) {
            mListener.onFragmentInteraction(model);
        }
    }
    public void onButtonCancel() {
        if (mListener != null) {
            mListener.onCancel();
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
        switch (v.getId()){
            case R.id.tv_cancel:
                onButtonCancel();
                dismiss();
                break;
            case R.id.tv_confirm:
                onButtonPressed(model2);
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
        void onFragmentInteraction(SheQuModel model);
        void onCancel();
    }
}
