package edu.wpi.cs528.lzzz.carpooling_mobile;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.wpi.cs528.lzzz.carpooling_mobile.model.CarPool;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        @Bind(R.id.offer_name_textview)
        public TextView mOfferTextView;
        @Bind(R.id.address_textview)
        public TextView mAddressTextView;
        @Bind(R.id.car_info_textview)
        public TextView mCarInfoTextView;
        public View view;

        public ViewHolder( View v )

        {
            super(v);
            ButterKnife.bind(this,v);
            this.view = v;
        }
    }

    private List<CarPool> carPools;

    private Context mContext;

    public MyAdapter( Context context ,  List<CarPool> carPools)
    {
        this.mContext = context;
        this.carPools = carPools;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup viewGroup, int i )
    {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i )
    {
        CarPool c = carPools.get(i);
        viewHolder.mOfferTextView.setText(c.getOfferer().getUsername());
        viewHolder.mAddressTextView.setText("From     " + c.getStartLocation().getName() + "       To     "+ c.getTargetLocation().getName());
        viewHolder.mCarInfoTextView.setText(c.getCar().getMake()  + "     "  + c.getCar().getModel() + "     " + c.getCar().getPlate());
//        viewHolder.mImageView.setImageDrawable(mContext.getDrawable(p.getImageResourceId(mContext)));
        viewHolder.view.setOnClickListener(new  View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(mContext, ReservationActivity.class);
                mContext.startActivity(i);
            }
        });
    }


    @Override
    public int getItemCount()
    {
        return carPools == null ? 0 : carPools.size();
    }


}
