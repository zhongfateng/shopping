package com.liuwa.shopping.activity.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.liuwa.shopping.R;
import com.liuwa.shopping.activity.ProductDetailActivity;
import com.liuwa.shopping.adapter.FavoriateProductAdapter;
import com.liuwa.shopping.client.Constants;
import com.liuwa.shopping.client.LKAsyncHttpResponseHandler;
import com.liuwa.shopping.client.LKHttpRequest;
import com.liuwa.shopping.client.LKHttpRequestQueue;
import com.liuwa.shopping.client.LKHttpRequestQueueDone;
import com.liuwa.shopping.model.BaseDataModel;
import com.liuwa.shopping.model.CategoryModel;
import com.liuwa.shopping.model.ProductModel;
import com.liuwa.shopping.util.Md5SecurityUtil;
import com.liuwa.shopping.view.MyGridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProductShowByCategoryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProductShowByCategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductShowByCategoryFragment extends Fragment implements FavoriateProductAdapter.OnCartClick{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private View rootView;
    private static final String ARG_PARAM1 = "model";
    boolean mIsPrepare = false;		//视图还没准备好
    boolean mIsVisible= false;		//不可见
    boolean mIsFirstLoad = true;	//第一次加载
    private int page=1;
    private int pageSize=10;
    private PullToRefreshScrollView pullToRefreshScrollView;
    private MyGridView gv_favoriate_list;
    private FavoriateProductAdapter fpAdapter;
    private ArrayList<ProductModel> proList = new ArrayList<ProductModel>();

    // TODO: Rename and change types of parameters
    private CategoryModel mParam1;
    public BaseDataModel<ProductModel>  baseModel;
    private OnFragmentInteractionListener mListener;

    public ProductShowByCategoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductShowByCategoryFragment newInstance(CategoryModel model) {
        ProductShowByCategoryFragment fragment = new ProductShowByCategoryFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1,model);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = (CategoryModel) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {
             rootView =inflater.inflate(R.layout.fragment_product_show_by_category_item_layout, container, false);
        }
        pullToRefreshScrollView = (PullToRefreshScrollView) rootView.findViewById(R.id.pullToScrollView);
        gv_favoriate_list        = (MyGridView)rootView.findViewById(R.id.gv__list);
        fpAdapter                 =  new FavoriateProductAdapter(getActivity(),proList);
        pullToRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                loadData();
                pullToRefreshScrollView.onRefreshComplete();
            }
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page++;
                loadDataMore();
                pullToRefreshScrollView.onRefreshComplete();
            }
        });
        fpAdapter.setOnCartClick(this);
        gv_favoriate_list.setAdapter(fpAdapter);
        gv_favoriate_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProductModel model=(ProductModel)parent.getAdapter().getItem(position);
                Intent intent=new Intent(getActivity(), ProductDetailActivity.class);
                intent.putExtra("model",model);
                getActivity().startActivity(intent);
            }
        });
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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

    @Override
    public void cartOnClick(ProductModel model) {
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
        void onFragmentInteraction(Uri uri);
    }
    private void lazyLoad() {
        //这里进行三个条件的判断，如果有一个不满足，都将不进行加载
        if (!mIsPrepare || !mIsVisible||!mIsFirstLoad) {
            return;
        }
        loadData();
        //数据加载完毕,恢复标记,防止重复加载
        mIsFirstLoad = false;
    }
    //根据分类加载商品列表
    private void loadDataMore(){
        TreeMap<String, Object> productParam = new TreeMap<String, Object>();
        productParam.put("page",page);
        productParam.put("rows",pageSize);
        productParam.put("classesid",mParam1.getProClassesId());
        productParam.put("type",1);
        productParam.put("timespan", System.currentTimeMillis()+"");
        productParam.put("sign", Md5SecurityUtil.getSignature(productParam));
        HashMap<String, Object> requestCategoryMap = new HashMap<String, Object>();
        requestCategoryMap.put(Constants.kMETHODNAME,Constants.PRODUCTLIST);
        requestCategoryMap.put(Constants.kPARAMNAME, productParam);
        LKHttpRequest categoryReq = new LKHttpRequest(requestCategoryMap, getMoreHandler());
        new LKHttpRequestQueue().addHttpRequest(categoryReq)
                .executeQueue(null, new LKHttpRequestQueueDone(){

                    @Override
                    public void onComplete() {
                        super.onComplete();
                    }

                });
    }

    //根据分类加载商品列表
    private void loadData(){
        TreeMap<String, Object> productParam = new TreeMap<String, Object>();
        productParam.put("page",1);
        productParam.put("rows",pageSize);
        productParam.put("classesid",mParam1.getProClassesId());
        productParam.put("type",1);
        productParam.put("timespan", System.currentTimeMillis()+"");
        productParam.put("sign", Md5SecurityUtil.getSignature(productParam));
        HashMap<String, Object> requestCategoryMap = new HashMap<String, Object>();
        requestCategoryMap.put(Constants.kMETHODNAME,Constants.PRODUCTLIST);
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

    private LKAsyncHttpResponseHandler getMoreHandler(){
        return new LKAsyncHttpResponseHandler(){

            @Override
            public void successAction(Object obj) {
                String json=(String)obj;
                try {
                    JSONObject  job= new JSONObject(json);
                    int code =	job.getInt("code");
                    if(code==Constants.CODE)
                    {
                        JSONObject jsonObject = job.getJSONObject("data");
                        Gson localGson = new GsonBuilder().disableHtmlEscaping()
                                .create();
                        baseModel = localGson.fromJson(jsonObject.toString(),
                                new TypeToken<BaseDataModel<ProductModel>>() {
                                }.getType());
                        proList.addAll(baseModel.list);
                        fpAdapter.notifyDataSetChanged();

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
                        JSONObject jsonObject = job.getJSONObject("data");
                        Gson localGson = new GsonBuilder().disableHtmlEscaping()
                                .create();
                        proList.clear();
                        baseModel = localGson.fromJson(jsonObject.toString(),
                                new TypeToken<BaseDataModel<ProductModel>>() {
                                }.getType());
                        proList.addAll(baseModel.list);
                        fpAdapter.notifyDataSetChanged();

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
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mIsFirstLoad=true;
        mIsPrepare=false;
        mIsVisible = false;
    }

}
