package com.arenatiket.android;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arenatiket.android.customlayout.ChildView;
import com.arenatiket.android.model.Airline;
import com.arenatiket.android.model.Contact;
import com.arenatiket.android.model.Passanger;
import com.arenatiket.android.model.Ticket;
import com.arenatiket.android.utils.Constants;
import com.arenatiket.android.utils.MyApplication;
import com.arenatiket.android.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class BookingSummaryActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private LinearLayout bookButton;
    private LinearLayout backDetailInfoLayout;
    private Bundle bundle;
    private String isReturn;
    private TextView departPortTextView;
    private TextView arrivalPortTextView;
    private ImageView airlineLogoImageView;
    private TextView departTimeTextView;
    private TextView arriveTimeTextView;
    private View transitPointLayout;
    private LinearLayout transitRouteLayout;
    private View transitPoint2Layout;
    private LinearLayout transitRoute2Layout;
    private ImageView baggageImageView;
    private TextView baggageTextView;
    private ImageView taxImageView;
    private TextView taxTextView;
    private TextView mealsTextView;
    private ImageView mealsImageView;
    private TextView departPortDetailTextView;
    private TextView arrivalPortDetailTextView;
    private TextView transitPointTextView;
    private TextView transitPoint2TextView;
    private TextView backDepartPortDetailTextView;
    private TextView backArrivalPortDetailTextView;
    private TextView backDepartPortTextView;
    private TextView backArrivalPortTextView;
    private ImageView backAirlineLogoImageView;
    private TextView backDepartTimeTextView;
    private TextView backArriveTimeTextView;
    private View backTransitPointLayout;
    private TextView backTransitPointTextView;
    private LinearLayout backTransitRouteLayout;
    private View backTransitPoint2Layout;
    private TextView backTransitPoint2TextView;
    private LinearLayout backTransitRoute2Layout;
    private ImageView backBaggageImageView;
    private TextView backBaggageTextView;
    private ImageView backTaxImageView;
    private TextView backTaxTextView;
    private ImageView backMealsImageView;
    private TextView backMealsTextView;
    private TextView airlineCodeTextView;
    private TextView backAirlineCodeTextView;
    private TextView keberangkatanTextView;
    private TextView kepulanganTextView;
    private Contact contact;
    private TextView contactNameTextView;
    private TextView contactEmailTextView;
    private TextView contactPhoneTextView;
    private LinearLayout backFareLayout;
    private LinearLayout passangerLayout;
    private TextView backFareTextView;
    private TextView backSummaryTextView;
    private TextView departSummaryTextView;
    private TextView departFareTextView;
    private TextView hargaDiskon1TextView;
    private TextView hargaDiskon2TextView;
    private TextView termTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_summary);

        MyApplication.goRequestsQueue.clear();
        MyApplication.backRequestsQueue.clear();

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        TextView titleTextView = (TextView) toolbar.findViewById(R.id.toolbarTitleTextView);
        titleTextView.setText("Pemesanan Anda");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        keberangkatanTextView = (TextView) findViewById(R.id.keberangkatanTextView);
        airlineCodeTextView = (TextView) findViewById(R.id.airlineCodeTextView);
        departPortDetailTextView = (TextView) findViewById(R.id.departPortDetailTextView);
        arrivalPortDetailTextView = (TextView) findViewById(R.id.arrivalPortDetailTextView);
        departPortTextView = (TextView) findViewById(R.id.departPortTextView);
        arrivalPortTextView = (TextView) findViewById(R.id.arrivalPortTextView);
        airlineLogoImageView = (ImageView) findViewById(R.id.airlineLogoImageView);
        departTimeTextView = (TextView) findViewById(R.id.departTimeTextView);
        arriveTimeTextView = (TextView) findViewById(R.id.arriveTimeTextView);
        transitPointLayout = findViewById(R.id.transitPointLayout);
        transitPointTextView = (TextView) findViewById(R.id.transitPointTextView);
        transitRouteLayout = (LinearLayout) findViewById(R.id.transitRouteLayout);
        transitPoint2Layout = findViewById(R.id.transitPoint2Layout);
        transitPoint2TextView = (TextView) findViewById(R.id.transitPoint2TextView);
        transitRoute2Layout = (LinearLayout) findViewById(R.id.transitRoute2Layout);
        baggageImageView = (ImageView) findViewById(R.id.baggageImageView);
        baggageTextView = (TextView) findViewById(R.id.baggageTextView);
        taxImageView = (ImageView) findViewById(R.id.taxImageView);
        taxTextView = (TextView) findViewById(R.id.taxTextView);
        mealsTextView = (TextView) findViewById(R.id.mealsTextView);
        mealsImageView = (ImageView) findViewById(R.id.mealsImageView);


        kepulanganTextView = (TextView) findViewById(R.id.kepulanganTextView);
        backAirlineCodeTextView = (TextView) findViewById(R.id.backAirlineCodeTextView);
        backDepartPortDetailTextView = (TextView) findViewById(R.id.backDepartPortDetailTextView);
        backArrivalPortDetailTextView = (TextView) findViewById(R.id.backArrivalPortDetailTextView);
        backDepartPortTextView = (TextView) findViewById(R.id.backDepartPortTextView);
        backArrivalPortTextView = (TextView) findViewById(R.id.backArrivalPortTextView);
        backAirlineLogoImageView = (ImageView) findViewById(R.id.backAirlineLogoImageView);
        backDepartTimeTextView = (TextView) findViewById(R.id.backDepartTimeTextView);
        backArriveTimeTextView = (TextView) findViewById(R.id.backArriveTimeTextView);
        backTransitPointLayout = findViewById(R.id.backTransitPointLayout);
        backTransitPointTextView = (TextView) findViewById(R.id.backTransitPointTextView);
        backTransitRouteLayout = (LinearLayout) findViewById(R.id.backTransitRouteLayout);
        backTransitPoint2Layout = findViewById(R.id.backTransitPoint2Layout);
        backTransitPoint2TextView = (TextView) findViewById(R.id.backTransitPoint2TextView);
        backTransitRoute2Layout = (LinearLayout) findViewById(R.id.backTransitRoute2Layout);
        backBaggageImageView = (ImageView) findViewById(R.id.backBaggageImageView);
        backBaggageTextView = (TextView) findViewById(R.id.backBaggageTextView);
        backTaxImageView = (ImageView) findViewById(R.id.backTaxImageView);
        backTaxTextView = (TextView) findViewById(R.id.backTaxTextView);
        backMealsImageView = (ImageView) findViewById(R.id.backMealsImageView);
        backMealsTextView = (TextView) findViewById(R.id.backMealsTextView);

        hargaDiskon1TextView = (TextView) findViewById(R.id.hargaDiskon1TextView);
        hargaDiskon2TextView = (TextView) findViewById(R.id.hargaDiskon2TextView);

        bookButton = (LinearLayout) findViewById(R.id.bookButton);
        backDetailInfoLayout = (LinearLayout) findViewById(R.id.backDetailInfoLayout);

        contactNameTextView = (TextView) findViewById(R.id.contactNameTextView);
        contactEmailTextView = (TextView) findViewById(R.id.contactEmailTextView);
        contactPhoneTextView = (TextView) findViewById(R.id.contactPhoneTextView);

        passangerLayout = (LinearLayout) findViewById(R.id.passangerLayout);

        departSummaryTextView = (TextView) findViewById(R.id.departSummaryTextView);
        departFareTextView = (TextView) findViewById(R.id.departFareTextView);

        backSummaryTextView = (TextView) findViewById(R.id.backSummaryTextView);
        backFareTextView = (TextView) findViewById(R.id.backFareTextView);
        backFareLayout = (LinearLayout) findViewById(R.id.backFareLayout);
        termTextView = (TextView) findViewById(R.id.termTextView);

        termTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BookingSummaryActivity.this);
                builder.setTitle("Syarat dan Ketentuan");
                builder.setMessage(Html.fromHtml("<p>Arenatiket.com menyediakan berbagai layanan dengan ketentuan-ketentuan sebagaimana tertuang di dalam dokumen berikut ini. Saat Anda berkunjung ke situs Arenatiket.com dan memanfaatkan berbagai fitur layanan yang disediakan oleh Arenatiket.com, dengan ini, Anda sudah menyetujui ketentuan-ketentuan yang berlaku. Oleh karena itu, pastikan Anda telah membaca dengan seksama ketentuan-ketentuan yang berlaku.</p>\n" +
                        "<ol>\n" +
                        "<li>Tiket yang dikirimkan dalam bentuk elektronik tiket (e-tiket) dan akan dikirim melalui email yang diinputkan saat melakukan pemesanan.</li>\n" +
                        "<li>Saat cek in /harus dilengkapi dengan data identitas Asli asli baik berupa KTP, SIM ataupun Passport yang masih berlaku dan menunjukkan etiket yang telah dikirim oleh arenatiket baik diprint atau ditunjukkan melalui smartphone.</li>\n" +
                        "<li>Arenatiket.com tidak bertanggung jawab apabila terdapat penolakan cek in atas dasar data penumpang yang berbeda, keterlambatan cek in, kesalahan pemesanan tiket.</li>\n" +
                        "<li>Arenatiket.com tidak bertanggung jawab atas segala tuntutan terhadap tidak terpenuhinya, atau tidak memuaskannya suatu produk dan jasa yang dibeli atas nama Anda oleh Arenatiket.com dari penyedia jasa pihak ketiga dan distributor, namun tidak terbatas pada, maskapai penerbangan, perjalanan darat, acara konser, asuransi perusahaan, pihah hotel dan entitas lainnya. Pada waktu perusahaan penerbangan dan penyedia jasa perjalanan lainnya memindahkan penumpang ke penerbangan lain atau merubah jadwal penerbangan, untuk itu Arenatiket.com tidak bertanggung jawab atas insiden tersebut.</li>\n" +
                        "<li>Dengan membeli tiket di arenatiket.com berarti anda telah menyepakati untuk melakukan transfer dana sesuai dengan nominal yang tertera dan sebelum batas akhir pembayaran yang telah ditentukan.</li>\n" +
                        "</ol>"));
                builder.setPositiveButton("SETUJU", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });

        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BookingSummaryActivity.this, BookingProgressActivity.class);
                intent.putExtra("contact_person", contact);
                intent.putExtra("searchParams", bundle);
                startActivity(intent);
            }
        });


        bundle = getIntent().getBundleExtra("searchParams");
        contact = (Contact) getIntent().getSerializableExtra("contact_person");
        isReturn = bundle.getString("isReturn");

        if (MyApplication.selectedDepartItem != null) {
            Ticket ticket = MyApplication.selectedDepartItem;
            try {
                showGoTicketTable(ticket);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        if (isReturn.equals("1")) {
            backFareLayout.setVisibility(View.VISIBLE);
            backDetailInfoLayout.setVisibility(View.VISIBLE);
            if (MyApplication.selectedReturnItem != null) {
                Utils.logd("return NOT null");
                Ticket ticket = MyApplication.selectedReturnItem;
                try {
                    showBackTicketTable(ticket);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Utils.logd("return null");
            }
        } else {
            backFareLayout.setVisibility(View.GONE);
            backDetailInfoLayout.setVisibility(View.GONE);
        }

        if (contact != null) {
            showContact(contact);
        }

        if (MyApplication.passangers != null) {
            showPassangers(MyApplication.passangers);
        }

        showFareSummary();
    }

    private void showFareSummary() {
        Ticket departTicket = MyApplication.selectedDepartItem;
        JSONArray flights = departTicket.getFlights();
        Map<String, String> departValues = departTicket.getValues();
        try {
            int totalPrice = 0;
            int backTotalPrice = 0;

            departSummaryTextView.setText(flights.getJSONObject(0).getString("depart_port") + " > "
                    + flights.getJSONObject(flights.length() - 1).getString("arrive_port") + " x "
                    + MyApplication.passangers.size() + " orang");
            String departPrice = departValues.get("harga_diskon");
            Utils.logd("harga_diskon " + departPrice);
            String departPriceCh = departValues.get("harga_diskon_ch");
            String departPriceIn = departValues.get("harga_diskon_in");
            int price_ad = Integer.parseInt(departPrice);
            int price_ch = !departPriceCh.equals("0") ? Integer.parseInt(departPriceCh) : price_ad;
            int price_in = !departPriceIn.equals("0") ? Integer.parseInt(departPriceIn) : price_ad;

            int adultCount = Integer.parseInt(bundle.getString("adultCount"));
            int childCount = Integer.parseInt(bundle.getString("childCount"));
            int infantCount = Integer.parseInt(bundle.getString("infantCount"));

            totalPrice = (price_ad * adultCount) + (price_ch * childCount) + (price_in * infantCount);

            departFareTextView.setText(Utils.getMoneyFormat(totalPrice, true));

            if (MyApplication.selectedReturnItem != null) {
                Ticket backTicket = MyApplication.selectedReturnItem;
                JSONArray backlights = backTicket.getFlights();
                Map<String, String> backValues = backTicket.getValues();

                backSummaryTextView.setText(backlights.getJSONObject(0).getString("depart_port") + " > "
                        + backlights.getJSONObject(backlights.length() - 1).getString("arrive_port") + " x "
                        + MyApplication.passangers.size() + " orang");

                String backPrice = backValues.get("harga_diskon");
                Utils.logd("back harga_diskon " + backPrice);
                String backPriceCh = backValues.get("harga_diskon_ch");
                String backPriceIn = backValues.get("harga_diskon_in");
                int back_price_ad = Integer.parseInt(backPrice);
                int back_price_ch = !backPriceCh.equals("0") ? Integer.parseInt(backPriceCh) : back_price_ad;
                int back_price_in = !backPriceIn.equals("0") ? Integer.parseInt(backPriceIn) : back_price_ad;

                backTotalPrice = (back_price_ad * adultCount) + (back_price_ch * childCount) + (back_price_in * infantCount);

                totalPrice = totalPrice + backTotalPrice;

                backFareTextView.setText(Utils.getMoneyFormat(backTotalPrice, true));
            }

            String totalHargaText = totalPrice + "";
            String totalHarga1 = Utils.getMoneyFormat(Integer.parseInt(totalHargaText.substring(0, totalHargaText.length() - 3)), false);
            String totalHarga2 = totalHargaText.substring(totalHargaText.length() - 3);
            hargaDiskon1TextView.setText(totalHarga1);
            hargaDiskon2TextView.setText("." + totalHarga2);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showContact(Contact contact) {
        contactNameTextView.setText(contact.getTitleText() + contact.getName());
        contactEmailTextView.setText(contact.getEmail());
        contactPhoneTextView.setText(contact.getPhone());
    }

    private void showPassangers(ArrayList<Passanger> passangers) {
        Utils.logd("penumpang size " + passangers.size());
        for (Passanger passanger : passangers) {
            ChildView child = new ChildView(this);
            child.setText(passanger.getTitleText() + passanger.getName(), passanger.getBirthDate());
            passangerLayout.addView(child);
        }
    }


    private void showGoTicketTable(Ticket ticket) throws JSONException {
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
        departPortTextView.setText(departPort);
        arrivalPortTextView.setText(arrivePort);

        Airline airline = Constants.airlines.get(flights.getJSONObject(0).getString("airlines"));

        String departDateText = flights.getJSONObject(0).getString("depart_datetime");
        String[] splitted1 = departDateText.split(" ");
        String[] splitted = splitted1[0].split("-");
        String monthName = Utils.getMonthShortName(Integer.valueOf(splitted[1]) - 1);
        keberangkatanTextView.setText("Keberangkatan - " + splitted[2] + " " + monthName + " " + splitted[0]);

        airlineLogoImageView.setImageDrawable(getResources().getDrawable(airline.getLogoResourceId()));
        airlineCodeTextView.setText(flights.getJSONObject(0).getString("flight_num"));
        departTimeTextView.setText(departTime);
        arriveTimeTextView.setText(arriveTime);

        String travelDuration = "";

        transitPointLayout.setVisibility(View.GONE);
        transitRouteLayout.setVisibility(View.GONE);
        transitPoint2Layout.setVisibility(View.GONE);
        transitRoute2Layout.setVisibility(View.GONE);

        departPortDetailTextView.setText(departCity);
        arrivalPortDetailTextView.setText(arriveCity);


        String numDigit = flights.getJSONObject(0).getString("flight_num").substring(0, 2);
        if (numDigit.contains("ID")) {
            airlineLogoImageView.setImageDrawable(getResources().getDrawable(R.drawable.batik));
        } else if (numDigit.contains("IW")) {
            airlineLogoImageView.setImageDrawable(getResources().getDrawable(R.drawable.win));
        } else if (numDigit.contains("IN")) {
            airlineLogoImageView.setImageDrawable(getResources().getDrawable(R.drawable.namair));
        }

        if (!Constants.baggages.get(numDigit).equals("0")) {
            baggageImageView.setImageDrawable(getResources().getDrawable(R.drawable.luggage_on_icon));
            baggageTextView.setText("bagasi " + Constants.baggages.get(numDigit) + " kg");
            baggageTextView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        } else {
            baggageImageView.setImageDrawable(getResources().getDrawable(R.drawable.luggage_off_icon));
            baggageTextView.setText("tanpa bagasi");
            baggageTextView.setPaintFlags(baggageTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            baggageTextView.setTextColor(getResources().getColor(R.color.gray13));
        }

        if (Integer.parseInt(ticket.getValues().get("harga_tax")) > 0) {
            taxImageView.setImageDrawable(getResources().getDrawable(R.drawable.tax_on_icon));
            taxTextView.setText("airport tax");
            taxTextView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        } else {
            taxImageView.setImageDrawable(getResources().getDrawable(R.drawable.tax_off_icon));
            taxTextView.setText("airport tax");
            taxTextView.setPaintFlags(taxTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            taxTextView.setTextColor(getResources().getColor(R.color.gray13));
        }

        if (Constants.meals.get(numDigit).equals("1")) {
            mealsImageView.setImageDrawable(getResources().getDrawable(R.drawable.meal_on_icon));
            mealsTextView.setText("makan");
            mealsTextView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        } else {
            mealsImageView.setImageDrawable(getResources().getDrawable(R.drawable.meal_off_icon));
            mealsTextView.setText("makan");
            mealsTextView.setPaintFlags(mealsTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            mealsTextView.setTextColor(getResources().getColor(R.color.gray13));
        }


        if (flights.length() == 1) {

            String[] wPer = flights.getJSONObject(0).getString("flight_duration").split(":");
            travelDuration = wPer[0] == "00" ? wPer[1] + "menit" : wPer[0] + "j " + wPer[1] + "m";

            String flightNum = flights.getJSONObject(0).getString("flight_num");

            transitPointLayout.setVisibility(View.GONE);
            transitRouteLayout.setVisibility(View.GONE);
            transitPoint2Layout.setVisibility(View.GONE);
            transitRoute2Layout.setVisibility(View.GONE);

        } else if (flights.length() >= 2) {
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

            transitPointTextView.setText(flights.getJSONObject(1).getString("depart_city"));

            transitPointLayout.setVisibility(View.VISIBLE);
            transitRouteLayout.setVisibility(View.VISIBLE);


            if (flights.length() >= 3) {
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

                transitPoint2TextView.setText(flights.getJSONObject(flights.length() - 1).getString("depart_city"));

                transitPoint2Layout.setVisibility(View.VISIBLE);
                transitRoute2Layout.setVisibility(View.VISIBLE);

            } else {
                transitPoint2Layout.setVisibility(View.GONE);
                transitRoute2Layout.setVisibility(View.GONE);
            }
        }
    }

    private void showBackTicketTable(Ticket ticket) throws JSONException {
        String flight_num_str = "";
        String id = ticket.getId();
        JSONArray flights = ticket.getFlights();
        JSONObject fares = ticket.getFares();
        Map<String, String> values = ticket.getValues();


        int unix_backDepart_time = (Integer.parseInt(flights.getJSONObject(0).getString("depart_time").substring(0, 2)) * 3600)
                + (Integer.parseInt(flights.getJSONObject(0).getString("depart_time").substring(3, 5)) * 60);

        String backDepartPort = flights.getJSONObject(0).getString("depart_port");
        String backArrivePort = flights.getJSONObject(flights.length() - 1).getString("arrive_port");
        String backDepartCity = flights.getJSONObject(0).getString("depart_city");
        String backArriveCity = flights.getJSONObject(flights.length() - 1).getString("arrive_city");
        String backDepartTime = flights.getJSONObject(0).getString("depart_time");
        String backArriveTime = flights.getJSONObject(flights.length() - 1).getString("arrive_time");
        backDepartPortTextView.setText(backDepartPort);
        backArrivalPortTextView.setText(backArrivePort);

        Airline airline = Constants.airlines.get(flights.getJSONObject(0).getString("airlines"));
        String backDepartDateText = flights.getJSONObject(0).getString("depart_datetime");
        String[] splitted1 = backDepartDateText.split(" ");
        String[] splitted = splitted1[0].split("-");
        String monthName = Utils.getMonthShortName(Integer.valueOf(splitted[1]) - 1);
        kepulanganTextView.setText("Kepulangan - " + splitted[2] + " " + monthName + " " + splitted[0]);

        backAirlineLogoImageView.setImageDrawable(getResources().getDrawable(airline.getLogoResourceId()));
        backAirlineCodeTextView.setText(flights.getJSONObject(0).getString("flight_num"));
        backDepartTimeTextView.setText(backDepartTime);
        backArriveTimeTextView.setText(backArriveTime);

        String travelDuration = "";

        backTransitPointLayout.setVisibility(View.GONE);
        backTransitRouteLayout.setVisibility(View.GONE);
        backTransitPoint2Layout.setVisibility(View.GONE);
        backTransitRoute2Layout.setVisibility(View.GONE);

        backDepartPortDetailTextView.setText(backDepartCity);
        backArrivalPortDetailTextView.setText(backArriveCity);


        String numDigit = flights.getJSONObject(0).getString("flight_num").substring(0, 2);
        if (numDigit.contains("ID")) {
            airlineLogoImageView.setImageDrawable(getResources().getDrawable(R.drawable.batik));
        } else if (numDigit.contains("IW")) {
            airlineLogoImageView.setImageDrawable(getResources().getDrawable(R.drawable.win));
        } else if (numDigit.contains("IN")) {
            airlineLogoImageView.setImageDrawable(getResources().getDrawable(R.drawable.namair));
        }

        if (!Constants.baggages.get(numDigit).equals("0")) {
            backBaggageImageView.setImageDrawable(getResources().getDrawable(R.drawable.luggage_on_icon));
            backBaggageTextView.setText("bagasi " + Constants.baggages.get(numDigit) + " kg");
            backBaggageTextView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        } else {
            backBaggageImageView.setImageDrawable(getResources().getDrawable(R.drawable.luggage_off_icon));
            backBaggageTextView.setText("tanpa bagasi");
            backBaggageTextView.setPaintFlags(backBaggageTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            backBaggageTextView.setTextColor(getResources().getColor(R.color.gray13));
        }

        if (Integer.parseInt(ticket.getValues().get("harga_tax")) > 0) {
            backTaxImageView.setImageDrawable(getResources().getDrawable(R.drawable.tax_on_icon));
            backTaxTextView.setText("airport tax");
            backTaxTextView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        } else {
            backTaxImageView.setImageDrawable(getResources().getDrawable(R.drawable.tax_off_icon));
            backTaxTextView.setText("airport tax");
            backTaxTextView.setPaintFlags(backTaxTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            backTaxTextView.setTextColor(getResources().getColor(R.color.gray13));
        }

        if (Constants.meals.get(numDigit).equals("1")) {
            backMealsImageView.setImageDrawable(getResources().getDrawable(R.drawable.meal_on_icon));
            backMealsTextView.setText("makan");
            backMealsTextView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        } else {
            backMealsImageView.setImageDrawable(getResources().getDrawable(R.drawable.meal_off_icon));
            backMealsTextView.setText("makan");
            backMealsTextView.setPaintFlags(backMealsTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            backMealsTextView.setTextColor(getResources().getColor(R.color.gray13));
        }


        if (flights.length() == 1) {

            String[] wPer = flights.getJSONObject(0).getString("flight_duration").split(":");
            travelDuration = wPer[0] == "00" ? wPer[1] + "menit" : wPer[0] + "j " + wPer[1] + "m";

            String flightNum = flights.getJSONObject(0).getString("flight_num");

            backTransitPointLayout.setVisibility(View.GONE);
            backTransitRouteLayout.setVisibility(View.GONE);
            backTransitPoint2Layout.setVisibility(View.GONE);
            backTransitRoute2Layout.setVisibility(View.GONE);

        } else if (flights.length() >= 2) {
            backDepartTime = flights.getJSONObject(1).getString("depart_datetime");
            backArriveTime = flights.getJSONObject(0).getString("arrive_datetime");

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            long diffMillis = 0l;
            try {
                Date backDepartDateTime = format.parse(backDepartTime);
                Date backArriveDateTime = format.parse(backArriveTime);
                diffMillis = backDepartDateTime.getTime() - backArriveDateTime.getTime();
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

            backTransitPointTextView.setText(flights.getJSONObject(1).getString("depart_city"));

            backTransitPointLayout.setVisibility(View.VISIBLE);
            backTransitRouteLayout.setVisibility(View.VISIBLE);


            if (flights.length() >= 3) {
                backDepartTime = flights.getJSONObject(flights.length() - 1).getString("depart_datetime");
                backArriveTime = flights.getJSONObject(1).getString("arrive_datetime");

                try {
                    Date backDepartDateTime = format.parse(backDepartTime);
                    Date backArriveDateTime = format.parse(backArriveTime);
                    diffMillis = backDepartDateTime.getTime() - backArriveDateTime.getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Hours = (int) diffMillis / (1000 * 60 * 60);
                Mins = (int) (diffMillis / (1000 * 60)) % 60;

                diff = Hours + "j" + Mins + "m";

                wPer = flights.getJSONObject(flights.length() - 1).getString("flight_duration").split(":");
                String travelDuration3 = wPer[0] == "00" ? wPer[1] + "menit" : wPer[0] + "j " + wPer[1] + "m";
                String flightNum3 = flights.getJSONObject(2).getString("flight_num");

                backTransitPoint2TextView.setText(flights.getJSONObject(flights.length() - 1).getString("depart_city"));

                backTransitPoint2Layout.setVisibility(View.VISIBLE);
                backTransitRoute2Layout.setVisibility(View.VISIBLE);

            } else {
                backTransitPoint2Layout.setVisibility(View.GONE);
                backTransitRoute2Layout.setVisibility(View.GONE);
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
        }
        return false;

    }


}
