package com.liuwa.shopping.activity.fragment;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.liuwa.shopping.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UpdateNameFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UpdateNameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateNameFragment extends DialogFragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private TextView tv_cancel,tv_confirm;
    public EditText et_input_name;

    private OnFragmentInteractionListener mListener;

    public UpdateNameFragment() {
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
    public static UpdateNameFragment newInstance(String mParam1) {
        UpdateNameFragment fragment = new UpdateNameFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, mParam1);
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
        View rootView=inflater.inflate(R.layout.activity_update_name_layout, container, false);
        et_input_name=(EditText)rootView.findViewById(R.id.et_input_name);
        tv_confirm=(TextView) rootView.findViewById(R.id.tv_confirm);
        tv_cancel=(TextView)rootView.findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(this);
        tv_confirm.setOnClickListener(this);
        return rootView;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        et_input_name.setText(mParam1);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onPressedOk(String name) {
        if (mListener != null) {
            mListener.onOk(name);
        }
    }
    public void onPressedCancel() {
        if (mListener != null) {
            mListener.onCancle();
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
                onPressedCancel();
                dismiss();
                break;
            case R.id.tv_confirm:
                String str= et_input_name.getText().toString();
                if(str==null||str.length()==0)
                {
                    Toast.makeText(getActivity(), "请输入昵称",Toast.LENGTH_SHORT).show();
                    return;
                }
                onPressedOk(str);
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
        void onOk(String name);
        void onCancle();
    }
}
