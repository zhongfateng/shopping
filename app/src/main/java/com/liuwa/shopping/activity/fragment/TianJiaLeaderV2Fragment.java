package com.liuwa.shopping.activity.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ScrollView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.liuwa.shopping.R;
import com.liuwa.shopping.adapter.AddHeaderAdapter;
import com.liuwa.shopping.client.Constants;
import com.liuwa.shopping.client.LKAsyncHttpResponseHandler;
import com.liuwa.shopping.client.LKHttpRequest;
import com.liuwa.shopping.client.LKHttpRequestQueue;
import com.liuwa.shopping.client.LKHttpRequestQueueDone;
import com.liuwa.shopping.model.HeaderApplayModel;
import com.liuwa.shopping.util.Md5SecurityUtil;
import com.liuwa.shopping.view.MyListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.TreeMap;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TianJiaLeaderV2Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TianJiaLeaderV2Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TianJiaLeaderV2Fragment extends Fragment implements AddHeaderAdapter.OnCartClick{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    boolean mIsPrepare = false;		//视图还没准备好
    boolean mIsVisible= false;		//不可见
    boolean mIsFirstLoad = true;	//第一次加载
    // TODO: Rename and change types of parameters
    private ArrayList<HeaderApplayModel> mParam1=new ArrayList<HeaderApplayModel>();
    private String mParam2;
    public AddHeaderAdapter adapter;

    private OnFragmentInteractionListener mListener;

    public PullToRefreshListView pullToRefreshListView;

    public TianJiaLeaderV2Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TianJiaLeaderV2Fragment newInstance(String param2) {
        TianJiaLeaderV2Fragment fragment = new TianJiaLeaderV2Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.fragment_add_header_layout, container, false);
        pullToRefreshListView = (PullToRefreshListView) rootView.findViewById(R.id.pullToListView);
        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getTuangou();
                pullToRefreshListView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pullToRefreshListView.onRefreshComplete();
                    }
                }, 1000);

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });
        adapter=new AddHeaderAdapter(getActivity(),mParam1,mParam2);
        adapter.setOnCartClick(this);
        pullToRefreshListView.setAdapter(adapter);
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
        }
    }
    public void onGress(HeaderApplayModel model){
        if (mListener != null) {
            mListener.agress(model);
        }
    }
    public void onDelete(HeaderApplayModel model){
        if (mListener != null) {
            mListener.delete(model);
        }
    }
    public void onScDelete(HeaderApplayModel model){
        if (mListener != null) {
            mListener.sc(model);
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
    public void agressClick(HeaderApplayModel model) {
        onGress(model);
    }

    @Override
    public void deleteClick(HeaderApplayModel model) {
        onDelete(model);
    }

    @Override
    public void scClick(HeaderApplayModel model) {
        onScDelete(model);
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
        void agress(HeaderApplayModel model);
        void delete(HeaderApplayModel model);
        void sc(HeaderApplayModel model);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mIsPrepare = true;
        lazyLoad();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //isVisibleToUser这个boolean值表示:该Fragment的UI 用户是否可见
        if (isVisibleToUser) {
            mIsVisible = true;
            lazyLoad();
        } else {
            mIsVisible = false;
        }
    }

    private void lazyLoad() {
        //这里进行三个条件的判断，如果有一个不满足，都将不进行加载
        if (!mIsPrepare || !mIsVisible||!mIsFirstLoad) {
            return;
        }
        getTuangou();
        //数据加载完毕,恢复标记,防止重复加载
        mIsFirstLoad = false;
    }
    public void getTuangou(){
        TreeMap<String, Object> baseParam = new TreeMap<String, Object>();
        baseParam.put("timespan", System.currentTimeMillis()+"");
        baseParam.put("sign", Md5SecurityUtil.getSignature(baseParam));
        HashMap<String, Object> tuangouMap = new HashMap<String, Object>();
        tuangouMap.put(Constants.kMETHODNAME,Constants.ShenQing);
        tuangouMap.put(Constants.kPARAMNAME, baseParam);
        LKHttpRequest tuangouReq = new LKHttpRequest(tuangouMap, getTuanGouHandler());
        new LKHttpRequestQueue().addHttpRequest(tuangouReq)
                .executeQueue(null, new LKHttpRequestQueueDone(){
                    @Override
                    public void onComplete() {
                        super.onComplete();
                    }
                });
    }
    private LKAsyncHttpResponseHandler getTuanGouHandler(){
        return new LKAsyncHttpResponseHandler(){
            @Override
            public void successAction(Object obj) {
                String json=(String)obj;
                try {
                    JSONObject job= new JSONObject(json);
                    int code =	job.getInt("code");
                    if(code==Constants.CODE) {
                        JSONObject object=job.getJSONObject("data");
                        Gson localGson = new GsonBuilder().disableHtmlEscaping()
                                .create();
                        if(mParam2.equals("1")) {
                            mParam1.clear();
                            mParam1.addAll((Collection<? extends HeaderApplayModel>) (localGson.fromJson(object.getJSONArray("memlistty").toString(),
                                    new TypeToken<ArrayList<HeaderApplayModel>>() {
                                    }.getType())));
                        }else if(mParam2.equals("2")){
                            mParam1.clear();
                            mParam1.addAll((Collection<? extends HeaderApplayModel>)(localGson.fromJson(object.getJSONArray("memlistsq").toString(),
                                    new TypeToken<ArrayList<HeaderApplayModel>>() {
                                    }.getType())));
                        }
                        adapter.notifyDataSetChanged();
                        pullToRefreshListView.onRefreshComplete();
                    }
                    else {
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mIsFirstLoad=true;
        mIsPrepare=false;
        mIsVisible = false;
    }
}
