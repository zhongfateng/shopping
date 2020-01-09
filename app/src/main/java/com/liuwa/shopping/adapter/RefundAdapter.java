package com.liuwa.shopping.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liuwa.shopping.R;
import com.liuwa.shopping.client.Constants;
import com.liuwa.shopping.model.OrderModel;
import com.liuwa.shopping.model.OrderProductItem;
import com.liuwa.shopping.model.ShoppingCartModel;
import com.liuwa.shopping.util.ImageShowUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by ZFT on 2019/8/27.
 */

public class RefundAdapter extends BaseAdapter {

    private boolean isShow = true;//是否显示编辑/完成
    private List<OrderProductItem> shoppingCartModelList;
    private CheckInterface checkInterface;
    private Context context;

    public RefundAdapter(Context context) {
        this.context = context;
    }

    public void setShoppingCartModelList(List<OrderProductItem> shoppingCartModelList) {
        this.shoppingCartModelList = shoppingCartModelList;
        notifyDataSetChanged();
    }

    /**
     * 单选接口
     *
     * @param checkInterface
     */
    public void setCheckInterface(CheckInterface checkInterface) {
        this.checkInterface = checkInterface;
    }


    @Override
    public int getCount() {
        return shoppingCartModelList == null ? 0 : shoppingCartModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return shoppingCartModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    /**
     * 是否显示可编辑
     *
     * @param flag
     */
    public void isShow(boolean flag) {
        isShow = flag;
        notifyDataSetChanged();
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_cart_list_item_v3_layout, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final OrderProductItem shoppingCartModel = shoppingCartModelList.get(position);
        boolean choosed = shoppingCartModel.isChoosed();
        if (choosed){
            holder.ckOneChose.setChecked(true);
        }else{
            holder.ckOneChose.setChecked(false);
        }
//        holder.ckOneChose.setChecked(true);
//        holder.ckOneChose.setClickable(false);

        holder.tvCommodityAttr.setText(shoppingCartModel.guiGe+"");
        holder.tvCommodityName.setText(shoppingCartModel.proName);
        holder.tvCommodityPrice.setText("￥"+shoppingCartModel.buyPrice+"");
        holder.tvCommodityNum.setText("x"+shoppingCartModel.buyNum);
        ImageShowUtil.showImage(shoppingCartModel.fristimg,holder.ivShowPic);
        //单选框按钮
//        holder.ckOneChose.setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        shoppingCartModel.setChoosed(((CheckBox) v).isChecked());
//                        checkInterface.checkGroup(position, ((CheckBox) v).isChecked());//向外暴露接口
//                    }
//                }
//        );

        //删除弹窗
        holder.tvCommodityDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alert = new AlertDialog.Builder(context).create();
                alert.setTitle("操作提示");
                alert.setMessage("您确定要将这些商品从购物车中移除吗？");
                alert.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        });
                alert.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                alert.show();
            }
        });
        int type=Integer.parseInt(shoppingCartModel.type);
        if(type>=5){
            holder.ckOneChose.setVisibility(View.INVISIBLE);
        }else{
            holder.ckOneChose.setVisibility(View.VISIBLE);
        }
        //判断是否在编辑状态下
        if (isShow) {
            holder.tvCommodityName.setVisibility(View.VISIBLE);
            holder.rlEdit.setVisibility(View.GONE);
            holder.tvCommodityNum.setVisibility(View.VISIBLE);
            holder.tvCommodityDelete.setVisibility(View.GONE);
        } else {
            holder.tvCommodityName.setVisibility(View.VISIBLE);
            holder.rlEdit.setVisibility(View.VISIBLE);
            holder.tvCommodityNum.setVisibility(View.GONE);
            holder.tvCommodityDelete.setVisibility(View.VISIBLE);
        }

        return convertView;
    }
    //初始化控件
    class ViewHolder {
        ImageView ivShowPic,tvCommodityDelete;
        TextView tvCommodityName, tvCommodityAttr, tvCommodityPrice, tvCommodityNum, tvCommodityShowNum,ivSub, ivAdd;
        CheckBox ckOneChose;
        LinearLayout rlEdit;
        public ViewHolder(View itemView) {
            ckOneChose = (CheckBox) itemView.findViewById(R.id.ck_chose);
            ivShowPic = (ImageView) itemView.findViewById(R.id.iv_show_pic);
            ivSub = (TextView) itemView.findViewById(R.id.iv_sub);
            ivAdd = (TextView) itemView.findViewById(R.id.iv_add);
            tvCommodityName = (TextView) itemView.findViewById(R.id.tv_commodity_name);
            tvCommodityAttr = (TextView) itemView.findViewById(R.id.tv_commodity_attr);
            tvCommodityPrice = (TextView) itemView.findViewById(R.id.tv_commodity_price);
            tvCommodityNum = (TextView) itemView.findViewById(R.id.tv_commodity_num);
            tvCommodityShowNum = (TextView) itemView.findViewById(R.id.tv_commodity_show_num);
            tvCommodityDelete = (ImageView) itemView.findViewById(R.id.tv_commodity_delete);
            rlEdit = (LinearLayout) itemView.findViewById(R.id.rl_edit);
        }
    }
    /**
     * 复选框接口
     */
    public interface CheckInterface {
        /**
         * 组选框状态改变触发的事件
         *
         * @param position  元素位置
         * @param isChecked 元素选中与否
         */
        void checkGroup(int position, boolean isChecked);
    }


    /**
     * 改变数量的接口
     */
    public interface ModifyCountInterface {
        /**
         * 增加操作
         *
         * @param position      元素位置
         * @param showCountView 用于展示变化后数量的View
         * @param isChecked     子元素选中与否
         */
        void doIncrease(int position, View showCountView, boolean isChecked);

        /**
         * 删减操作
         *
         * @param position      元素位置
         * @param showCountView 用于展示变化后数量的View
         * @param isChecked     子元素选中与否
         */
        void doDecrease(int position, View showCountView, boolean isChecked);

        /**
         * 删除子item
         *
         * @param position
         */
        void childDelete(int position);
    }
}