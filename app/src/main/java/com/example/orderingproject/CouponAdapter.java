package com.example.orderingproject;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.orderingproject.Dialog.CustomMenuOptionDialog;
import com.example.orderingproject.Dialog.CustomMenuOptionDialogListener;
import com.example.orderingproject.Dto.ResultDto;
import com.example.orderingproject.Dto.RetrofitService;
import com.example.orderingproject.Dto.request.CouponSerialNumberDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import lombok.SneakyThrows;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.CustomViewHolder> {

    ArrayList<CouponData> arrayCouponList;
    Context context;

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView tvCouponPrice, tvCouponInfo;
        CardView cardView;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            tvCouponInfo = itemView.findViewById(R.id.tv_coupon_info);
            tvCouponPrice = itemView.findViewById(R.id.tv_coupon_price);
            cardView = itemView.findViewById(R.id.cv_coupon);
        }
    }

    public CouponAdapter(ArrayList<CouponData> arrayList, Context context) {
        this.arrayCouponList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_coupon, parent, false);
        return new CustomViewHolder(view);
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {

        int priceSplited = 0;
        holder.tvCouponPrice.setText(Utillity.computePrice(arrayCouponList.get(position).getValue()));

        switch (arrayCouponList.get(position).getValue()){
            case 30000:
                priceSplited = 3;
                break;
            case 50000:
                priceSplited = 5;
                break;
            case 100000:
                priceSplited = 10;
                break;
        }

        holder.tvCouponInfo.setText(String.format("오더링 회원이면 누구나! %d만원 쿠폰", priceSplited));

        if(CouponActivity.fromPayment) {
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CouponActivity.showProgress((Activity)context);

                    Intent intent = new Intent(context, PaymentActivity.class);
                    intent.putExtra("couponId", arrayCouponList.get(position).getCouponId());
                    intent.putExtra("couponValue", arrayCouponList.get(position).getValue());

                    ((Activity) context).setResult(RESULT_OK, intent);;
                    Log.e("couponId",arrayCouponList.get(position).getCouponId() + " // 선택 적용됨");
                    CouponActivity.hideProgress();
                    ((Activity) context).finish();

                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return (arrayCouponList != null ? arrayCouponList.size() : 0);
    }

}

