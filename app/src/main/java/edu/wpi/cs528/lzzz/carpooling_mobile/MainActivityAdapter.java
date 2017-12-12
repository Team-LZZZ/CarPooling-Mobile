package edu.wpi.cs528.lzzz.carpooling_mobile;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.wpi.cs528.lzzz.carpooling_mobile.model.CarPool;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivityAdapter extends RecyclerView.Adapter<MainActivityAdapter.ViewHolder>{

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        @Bind(R.id.offer_name_textview)
        public TextView mOfferTextView;

        @Bind(R.id.departure_textview)
        public TextView mDepartrueTestview;

        @Bind(R.id.destination_textview)
        public TextView mDestinationTextView;
        @Bind(R.id.TVavailables_seat)
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

    public MainActivityAdapter(List<CarPool> carPools)
    {
        //this.mContext = context;
        this.carPools = carPools;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup viewGroup, int i )
    {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.carpool_card_view, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i )
    {
        CarPool c = carPools.get(i);
//        c.getStartLocation().setName(c.getStartLocation().getStreetNumber() + ", " +
//                                        c.getStartLocation().getStreet() + ", " +
//                                        c.getStartLocation().getCity() + ", " +
//                                        c.getStartLocation().getState() + ", " +
//                                        c.getStartLocation().getZip());
//        c.getTargetLocation().setName(c.getTargetLocation().getStreetNumber() + ", " +
//                                        c.getTargetLocation().getStreet() + ", " +
//                                        c.getTargetLocation().getCity() + ", " +
//                                        c.getTargetLocation().getState() + ", " +
//                                        c.getTargetLocation().getZip());
        int avaibleSeat = c.getCar().getSeatsLimit() - c.getReserverList().size();

        viewHolder.mOfferTextView.setText(c.getOfferer().getUsername());
        viewHolder.mDepartrueTestview.setText(c.getStartLocation().getName());
        viewHolder.mDestinationTextView.setText(c.getTargetLocation().getName());
        viewHolder.mCarInfoTextView.setText(c.getCar().getMake()  + "     "  + c.getCar().getModel() + "     " + avaibleSeat);
//        viewHolder.mImageView.setImageDrawable(mContext.getDrawable(p.getImageResourceId(mContext)));
        viewHolder.view.setOnClickListener(new  View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(mContext, ReservationActivity.class);
                i.putExtra("carPoolId", 1);
                i.putExtra("reserveMode", true);
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