package com.arenatiket.android.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arenatiket.android.BackSearchResultActivity;
import com.arenatiket.android.ContactInfoActivity;
import com.arenatiket.android.R;
import com.arenatiket.android.api.JsonCache;
import com.arenatiket.android.fragment.SearchResultFragment;
import com.arenatiket.android.model.Airline;
import com.arenatiket.android.model.Ticket;
import com.arenatiket.android.utils.Constants;
import com.arenatiket.android.utils.Filter;
import com.arenatiket.android.utils.MyApplication;
import com.arenatiket.android.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Ticket} and makes a call to the
 * specified {@link SearchResultFragment.OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    public static int MODE_GO = 11;
    public static int MODE_BACK = 12;
    private final List<Ticket> mValues;
    private final SearchResultFragment.OnListFragmentInteractionListener mListener;
    private final int mode;
    private final Bundle bundle;
    private int clickedPosition = -1;
    private Context context;
    private int pageIndex;


    public MyItemRecyclerViewAdapter(List<Ticket> items, SearchResultFragment.OnListFragmentInteractionListener mListener,
                                     int mode, Bundle bundle) {
        mValues = items;
        this.mListener = mListener;
        this.mode = mode;
        this.bundle = bundle;
        pageIndex = bundle.getInt("pageIndex");
        Utils.logd("bunde MyItemRecyclerViewAdapter " + pageIndex);
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_result_item_detail, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);

        final Ticket ticket = mValues.get(position);

        try {
            Airline airline = Constants.airlines.get(holder.mItem.getFlights().getJSONObject(0).getString("airlines"));
            if (Filter.mSelectedFlightItemCodes.contains(airline.getCode())) {
                showTicketTable(holder, holder.mItem, position, "go");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        holder.departPortTextView.setText(mValues.get(position).id);
//        holder.arrivalPortTextView.setText(mValues.get(position).content);

        if (position != clickedPosition) {
            holder.infoDetailLayout.setVisibility(View.GONE);
        } else {
            holder.infoDetailLayout.setVisibility(View.VISIBLE);
        }

        holder.chooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ContactInfoActivity.class);
                String isReturn = bundle.getBundle("args").getString("isReturn");
                JSONObject ticketJson = new JSONObject();
                try {
                    ticketJson.put("fares", ticket.getFares());
                    ticketJson.put("flights", ticket.getFlights());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (isReturn.equals("0")) {
                    intent = new Intent(context, ContactInfoActivity.class);
                    intent.putExtra("searchParams", bundle.getBundle("args"));
                    MyApplication.selectedDepartItem = ticket;
                    JsonCache.saveSelectedGoTicket(context, ticketJson.toString());
                } else {
                    if (mode == MODE_GO) {
                        MyApplication.selectedGoIndex = pageIndex;
                        Utils.logd("selectedGoIndex " + pageIndex + " track ID " + MyApplication.trackIds.get(pageIndex));
                        intent = new Intent(context, BackSearchResultActivity.class);
                        intent.putExtra("searchParams", bundle.getBundle("args"));
                        intent.putExtra("selectedGoDate", bundle.getString("date"));
                        MyApplication.selectedDepartItem = ticket;
                        JsonCache.saveSelectedGoTicket(context, ticketJson.toString());
                    } else if (mode == MODE_BACK) {
                        intent = new Intent(context, ContactInfoActivity.class);
                        intent.putExtra("searchParams", bundle.getBundle("args"));
                        MyApplication.selectedReturnItem = ticket;
                        JsonCache.saveSelectedBackTicket(context, ticketJson.toString());
                    }
                }

                context.startActivity(intent);
            }
        });

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    clickedPosition = position;
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                    onItemClickListener(holder.chooseButton, holder.mItem, position);
                    if (holder.infoDetailLayout.getVisibility() != View.GONE) {
                        holder.infoDetailLayout.setVisibility(View.GONE);
                    } else {
                        holder.infoDetailLayout.setVisibility(View.VISIBLE);
                    }
                    holder.chooseButton.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            holder.chooseButton.requestFocus();
                        }
                    }, 10);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Ticket item);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView departPortTextView;
        public final TextView arrivalPortTextView;
        private final LinearLayout infoDetailLayout;
        private final LinearLayout chooseButton;
        private final TextView hargaDiskon1TextView;
        private final TextView hargaDiskon2TextView;
        private final TextView hargaTotalTextView;
        private final ImageView airlineLogoImageView;
        private final TextView departTimeTextView;
        private final TextView arriveTimeTextView;
        private final LinearLayout transitPointLayout;
        private final LinearLayout transitRouteLayout;
        private final TextView departPortDetailTextView;
        private final TextView arrivalPortDetailTextView;
        private final TextView airlineCodeNDurationTextView;
        private final TextView airlineCodeNDuration2TextView;
        private final LinearLayout transitPoint2Layout;
        private final LinearLayout transitRoute2Layout;
        private final TextView airlineCodeNDuration3TextView;
        private final TextView transitPointTextView;
        private final TextView transitPoint2TextView;
        private final TextView transitDurationTextView;
        private final TextView transitDuration2TextView;
        private final TextView mealsTextView;
        private final ImageView mealsImageView;
        private final ImageView taxImageView;
        private final ImageView baggageImageView;
        private final TextView baggageTextView;
        private final TextView taxTextView;

        public Ticket mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            airlineLogoImageView = (ImageView) view.findViewById(R.id.airlineLogoImageView);
            departPortTextView = (TextView) view.findViewById(R.id.departPortTextView);
            arrivalPortTextView = (TextView) view.findViewById(R.id.arrivalPortTextView);
            departTimeTextView = (TextView) view.findViewById(R.id.departTimeTextView);
            arriveTimeTextView = (TextView) view.findViewById(R.id.arriveTimeTextView);
            hargaTotalTextView = (TextView) view.findViewById(R.id.hargaTotalTextView);
            hargaTotalTextView.setPaintFlags(hargaTotalTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            hargaDiskon1TextView = (TextView) view.findViewById(R.id.hargaDiskon1TextView);
            hargaDiskon2TextView = (TextView) view.findViewById(R.id.hargaDiskon2TextView);
            infoDetailLayout = (LinearLayout) view.findViewById(R.id.detailInfoLayout);

            transitPointLayout = (LinearLayout) view.findViewById(R.id.transitPointLayout);
            transitRouteLayout = (LinearLayout) view.findViewById(R.id.transitRouteLayout);
            transitPoint2Layout = (LinearLayout) view.findViewById(R.id.transitPoint2Layout);
            transitRoute2Layout = (LinearLayout) view.findViewById(R.id.transitRoute2Layout);
            transitPointTextView = (TextView) view.findViewById(R.id.transitPointTextView);
            transitPoint2TextView = (TextView) view.findViewById(R.id.transitPoint2TextView);
            transitDurationTextView = (TextView) view.findViewById(R.id.transitDurationTextView);
            transitDuration2TextView = (TextView) view.findViewById(R.id.transitDuration2TextView);

            departPortDetailTextView = (TextView) view.findViewById(R.id.departPortDetailTextView);
            arrivalPortDetailTextView = (TextView) view.findViewById(R.id.arrivalPortDetailTextView);

            airlineCodeNDurationTextView = (TextView) view.findViewById(R.id.airlineCodeNDurationTextView);
            airlineCodeNDuration2TextView = (TextView) view.findViewById(R.id.airlineCodeNDuration2TextView);
            airlineCodeNDuration3TextView = (TextView) view.findViewById(R.id.airlineCodeNDuration3TextView);

            baggageImageView = (ImageView) view.findViewById(R.id.baggageImageView);
            baggageTextView = (TextView) view.findViewById(R.id.baggageTextView);
            taxImageView = (ImageView) view.findViewById(R.id.taxImageView);
            taxTextView = (TextView) view.findViewById(R.id.taxTextView);
            mealsImageView = (ImageView) view.findViewById(R.id.mealsImageView);
            mealsTextView = (TextView) view.findViewById(R.id.mealsTextView);

            chooseButton = (LinearLayout) view.findViewById(R.id.chooseButton);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + arrivalPortTextView.getText() + "'";
        }
    }

    public void onItemClickListener(LinearLayout chooseButton, Ticket item, int position) {

    }


    private static float hargaMinBack;
    private static int minTake;
    private static int maxTake;
    static int inc = 0;

    private void showTicketTable(ViewHolder holder, Ticket ticket, int j, String direction) throws JSONException {
        String flight_num_str = "";
        String id = ticket.getId();
        JSONArray flights = ticket.getFlights();
        JSONObject fares = ticket.getFares();
        Map<String, String> values = ticket.getValues();


        int unix_depart_time = (Integer.parseInt(flights.getJSONObject(0).getString("depart_time").substring(0, 2)) * 3600)
                + (Integer.parseInt(flights.getJSONObject(0).getString("depart_time").substring(3, 5)) * 60);

        String departPort = flights.getJSONObject(0).getString("depart_port");
        String arrivePort = flights.getJSONObject(flights.length() - 1).getString("arrive_port");
        String departCity = flights.getJSONObject(0).getString("depart_city");
        String arriveCity = flights.getJSONObject(flights.length() - 1).getString("arrive_city");
        String departTime = flights.getJSONObject(0).getString("depart_time");
        String arriveTime = flights.getJSONObject(flights.length() - 1).getString("arrive_time");
        holder.departPortTextView.setText(departPort);
        holder.arrivalPortTextView.setText(arrivePort);
        holder.hargaTotalTextView.setText(Utils.getMoneyFormat(Math.round(Float.parseFloat(values.get("total"))), true));
        String hargaDiskon = values.get("harga_diskon");
        String hargaDiskon1 = Utils.getMoneyFormat(Math.round(Float.parseFloat(hargaDiskon.substring(0, hargaDiskon.length() - 3))), false);
        String hargaDiskon2 = hargaDiskon.substring(hargaDiskon.length() - 3);
        holder.hargaDiskon1TextView.setText(hargaDiskon1);
        holder.hargaDiskon2TextView.setText("." + hargaDiskon2);
//        Utils.logd("transit " + (flights.length() - 1));
        String airlines = flights.getJSONObject(0).getString("airlines");
        Airline airline = Constants.airlines.get(airlines);
        Utils.logd("airlines== " + airlines);
        holder.airlineLogoImageView.setImageDrawable(context.getResources().getDrawable(airline.getLogoResourceId()));
        holder.departTimeTextView.setText(departTime);
        holder.arriveTimeTextView.setText(arriveTime);

        holder.departPortDetailTextView.setText(departCity + " (" + departPort + ")");
        holder.arrivalPortDetailTextView.setText(arriveCity + " (" + arrivePort + ")");

        String travelDuration = "";

        holder.transitPointLayout.setVisibility(View.GONE);
        holder.transitRouteLayout.setVisibility(View.GONE);
        holder.transitPoint2Layout.setVisibility(View.GONE);
        holder.transitRoute2Layout.setVisibility(View.GONE);

        String numDigit = flights.getJSONObject(0).getString("flight_num").substring(0, 2);
        if (numDigit.contains("ID")) {
            holder.airlineLogoImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.batik));
        } else if (numDigit.contains("IW")) {
            holder.airlineLogoImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.win));
        } else if (numDigit.contains("IN")) {
            holder.airlineLogoImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.namair));
        }

        if (!Constants.baggages.get(numDigit).equals("0")) {
            holder.baggageImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.luggage_on_icon));
            holder.baggageTextView.setText("bagasi " + Constants.baggages.get(numDigit) + " kg");
            holder.baggageTextView.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
        } else {
            holder.baggageImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.luggage_off_icon));
            holder.baggageTextView.setText("tanpa bagasi");
            holder.baggageTextView.setPaintFlags(holder.baggageTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.baggageTextView.setTextColor(context.getResources().getColor(R.color.gray13));
        }

        if (Math.round(Float.parseFloat(ticket.getValues().get("harga_tax"))) > 0) {
            holder.taxImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.tax_on_icon));
            holder.taxTextView.setText("airport tax");
            holder.taxTextView.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
        } else {
            holder.taxImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.tax_off_icon));
            holder.taxTextView.setText("airport tax");
            holder.taxTextView.setPaintFlags(holder.taxTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.taxTextView.setTextColor(context.getResources().getColor(R.color.gray13));
        }

        if (Constants.meals.get(numDigit).equals("1")) {
            holder.mealsImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.meal_on_icon));
            holder.mealsTextView.setText("makan");
            holder.mealsTextView.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
        } else {
            holder.mealsImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.meal_off_icon));
            holder.mealsTextView.setText("makan");
            holder.mealsTextView.setPaintFlags(holder.mealsTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.mealsTextView.setTextColor(context.getResources().getColor(R.color.gray13));
        }


        if (flights.length() == 1) {
            Utils.logd("satu");

            String[] wPer = flights.getJSONObject(0).getString("flight_duration").split(":");
            travelDuration = wPer[0] == "00" ? wPer[1] + "menit" : wPer[0] + "j " + wPer[1] + "m";

            String flightNum = flights.getJSONObject(0).getString("flight_num");
            Utils.logd("airlines " + airlines + "flight_num " + flightNum);

            holder.transitPointLayout.setVisibility(View.GONE);
            holder.transitRouteLayout.setVisibility(View.GONE);
            holder.transitPoint2Layout.setVisibility(View.GONE);
            holder.transitRoute2Layout.setVisibility(View.GONE);

            holder.airlineCodeNDurationTextView.setText(flightNum + " " + travelDuration);
        } else if (flights.length() >= 2) {
            Utils.logd("dua");
            departTime = flights.getJSONObject(1).getString("depart_datetime");
            arriveTime = flights.getJSONObject(0).getString("arrive_datetime");

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            long diffMillis = 0l;
            try {
                Date departDateTime = format.parse(departTime);
                Date arriveDateTime = format.parse(arriveTime);
                diffMillis = departDateTime.getTime() - arriveDateTime.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            int Hours = (int) diffMillis / (1000 * 60 * 60);
            int Mins = (int) (diffMillis / (1000 * 60)) % 60;

            String diff = Hours + "j" + Mins + "m";

            String[] wPer = flights.getJSONObject(0).getString("flight_duration").split(":");
            travelDuration = wPer[0] == "00" ? wPer[1] + "menit" : wPer[0] + "j " + wPer[1] + "m";

            String flightNum = flights.getJSONObject(0).getString("flight_num");

            wPer = flights.getJSONObject(1).getString("flight_duration").split(":");
            String travelDuration2 = wPer[0] == "00" ? wPer[1] + "menit" : wPer[0] + "j " + wPer[1] + "m";
            String flightNum2 = flights.getJSONObject(1).getString("flight_num");

            Utils.logd("travelDuration " + flightNum2 + " " + travelDuration2);

            holder.airlineCodeNDurationTextView.setText(flightNum + " " + travelDuration);
            holder.airlineCodeNDuration2TextView.setText(flightNum2 + " " + travelDuration2);
            holder.transitPointTextView.setText(flights.getJSONObject(1).getString("depart_city"));
            holder.transitDurationTextView.setText("transit " + diff);

            holder.transitPointLayout.setVisibility(View.VISIBLE);
            holder.transitRouteLayout.setVisibility(View.VISIBLE);


            if (flights.length() >= 3) {
                Utils.logd("tiga");
                departTime = flights.getJSONObject(flights.length() - 1).getString("depart_datetime");
                arriveTime = flights.getJSONObject(1).getString("arrive_datetime");

                try {
                    Date departDateTime = format.parse(departTime);
                    Date arriveDateTime = format.parse(arriveTime);
                    diffMillis = departDateTime.getTime() - arriveDateTime.getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Hours = (int) diffMillis / (1000 * 60 * 60);
                Mins = (int) (diffMillis / (1000 * 60)) % 60;

                diff = Hours + "j" + Mins + "m";

                wPer = flights.getJSONObject(flights.length() - 1).getString("flight_duration").split(":");
                String travelDuration3 = wPer[0] == "00" ? wPer[1] + "menit" : wPer[0] + "j " + wPer[1] + "m";
                String flightNum3 = flights.getJSONObject(2).getString("flight_num");

                holder.airlineCodeNDuration3TextView.setText(flightNum3 + " " + travelDuration3);
                holder.transitPoint2TextView.setText(flights.getJSONObject(flights.length() - 1).getString("depart_city"));
                holder.transitDuration2TextView.setText("transit " + diff);

                holder.transitPoint2Layout.setVisibility(View.VISIBLE);
                holder.transitRoute2Layout.setVisibility(View.VISIBLE);

            } else {
                holder.transitPoint2Layout.setVisibility(View.GONE);
                holder.transitRoute2Layout.setVisibility(View.GONE);
            }
        }
    }


    private static int priceTotal(JSONObject prices_ad, boolean b) {
        return 0;
    }

    private static String priceValue(JSONObject prices_ch) {
        return "";
    }

    private static String priceValue(int prices_ch) {
        return "";
    }

    private static String fomatMoney(String delimiter, String arg2, String arg3) {
        return "";
    }
}
