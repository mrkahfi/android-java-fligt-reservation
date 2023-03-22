package com.arenatiket.android.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.arenatiket.android.R;
import com.arenatiket.android.adapter.MyItemRecyclerViewAdapter;
import com.arenatiket.android.adapter.ViewPagerAdapter;
import com.arenatiket.android.api.JsonCache;
import com.arenatiket.android.customlayout.DividerItemDecoration;
import com.arenatiket.android.customlayout.LinearLayoutManagerWithSmoothScroller;
import com.arenatiket.android.customlayout.VerticalSpaceItemDecoration;
import com.arenatiket.android.model.Airline;
import com.arenatiket.android.model.Ticket;
import com.arenatiket.android.utils.Constants;
import com.arenatiket.android.utils.Filter;
import com.arenatiket.android.utils.MyApplication;
import com.arenatiket.android.utils.Utils;
import com.daasuu.ahp.AnimateHorizontalProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class SearchResultFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String MODE = "mode";
    private static final int GO = 1;
    private static final int BACK = 2;
    private static final int API_STATE_RUN = 1;
    private static final int API_STATE_FINISH = 2;
    private static final int API_STATE_ERROR = 3;
    public static final int LOWEST_PRICE = 0;
    public static final int DEPART_TIME = 1;
    public static final int ARRIVE_TIME = 2;
    public static final int DURATION = 3;
    private static Bundle bundle;
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private int mode;
    private String url = Constants.baseApiUrl + "android/search?accesskey=780b4191985eab0fa83b6bf3adbaaf52&client=arenatiket";
    private JsonObjectRequest request;
    private JSONArray nextProgress;
    private int state;
    private JSONArray finishedProgress;
    private boolean loaded;
    private boolean notFound;
    private int discountPro;
    public Map<String, String> goDataContainer = new HashMap<String, String>();
    public Map<String, String> backDataContainer = new HashMap<String, String>();
    private int discountEco;
    private int discountBus;
    private JSONObject resultObj = new JSONObject();
    private int index;
    private String isReturn = "";
    private HashMap<String, String> flash = new HashMap<>();
    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Utils.logd("message : " + error.toString());
        }
    };

    protected List<Ticket> items = new ArrayList<Ticket>();

    private String title = "";
    private int rate;
    private double totalRate;
    private int minPrice;
    private int maxPrice;

    private boolean isDefaultIndex;
    protected Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            resultObj = response;
            totalRate = 0d;
            Utils.longLog("response : " + response.toString());
            try {
                if (Math.round(Float.parseFloat(response.getString("status"))) == 1) {
                    JSONObject results = response.getJSONObject("results");
                    String trackId = results.getString("track_id");
                    Utils.logd("pageIndex : " + pageIndex + " track ID : " + trackId);
                    Long trackTime = results.getLong("track_time");
                    if (mode == MyItemRecyclerViewAdapter.MODE_GO) {
                        MyApplication.trackIds.put(pageIndex, trackId);
                        MyApplication.trackTimes.put(pageIndex, trackTime);
                    }
                    JSONArray progresses = results.getJSONArray("progress");

                    JSONArray progressesRequest = new JSONArray();
                    for (int i = 0; i < progresses.length(); i++) {
                        final JSONObject progress = progresses.getJSONObject(i);
                        int ratePerProgress = (progress.getInt("numerator") / progress.getInt("denominator"));
//                        Utils.logd("ratePerProgress: " + ratePerProgress);

                        totalRate = (totalRate + (int) ratePerProgress);
//                        Utils.logd("totalRate: " + totalRate);
                        int percentage = (int) (totalRate / ((double) progresses.length()) * 100d);
                        if (totalRate != 0d) progressBar.setProgressWithAnim(percentage);
                        if (percentage >= 30) {

                            if (isDefaultIndex) {
                                JsonObjectRequest newRequest1 = mode == MyItemRecyclerViewAdapter.MODE_GO ?
                                        MyApplication.goRequestsQueue.get(pageIndex + 1) : MyApplication.backRequestsQueue.get(pageIndex + 1);
                                final JsonObjectRequest newRequest2 = mode == MyItemRecyclerViewAdapter.MODE_GO ?
                                        MyApplication.goRequestsQueue.get(pageIndex - 1) : MyApplication.backRequestsQueue.get(pageIndex - 1);
                                if (newRequest1 != null) {
                                    Utils.logd("REQUEST " + (pageIndex + 1) + " : " + newRequest1.getUrl());
                                    if (activity != null) {
                                        MyApplication.goQueue.add(newRequest1);
                                        if (mode == MyItemRecyclerViewAdapter.MODE_GO) {
                                            MyApplication.goRequestsQueue.put(pageIndex + 1, null);
                                        } else {
                                            MyApplication.backRequestsQueue.put(pageIndex + 1, null);
                                        }
                                    }
                                    if (newRequest2 != null) {
                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                Utils.logd("REQUEST " + (pageIndex - 1) + " : " + newRequest2.getUrl());
                                                if (activity != null)
                                                    MyApplication.goQueue.add(newRequest2);
                                                if (mode == MyItemRecyclerViewAdapter.MODE_GO) {
                                                    MyApplication.goRequestsQueue.put(pageIndex - 1, null);
                                                } else {
                                                    MyApplication.backRequestsQueue.put(pageIndex - 1, null);
                                                }
                                            }
                                        }, 1000);
                                    }
                                } else if (newRequest2 != null) {
                                    Utils.logd("REQUEST " + (pageIndex - 1) + " : " + newRequest2.getUrl());
                                    if (activity != null)
                                        MyApplication.goQueue.add(newRequest2);
                                }
                            } else {
                                JsonObjectRequest newRequest1 = mode == MyItemRecyclerViewAdapter.MODE_GO ?
                                        MyApplication.goRequestsQueue.get(pageIndex > MyApplication.defaultIndex
                                                ? pageIndex + 1 : pageIndex - 1) :
                                        MyApplication.backRequestsQueue.get(pageIndex > MyApplication.defaultBackIndex
                                                ? pageIndex + 1 : pageIndex - 1);
                                if (newRequest1 != null) {
                                    Utils.logd("newRequest1 null");
                                    if (activity != null)
                                        MyApplication.goQueue.add(newRequest1);
                                    if (mode == MyItemRecyclerViewAdapter.MODE_GO) {
                                        Utils.logd("REQUEST " + (pageIndex > MyApplication.defaultIndex
                                                ? pageIndex + 1 : pageIndex - 1) + " : " + newRequest1.getUrl());
                                        MyApplication.goRequestsQueue.put(pageIndex > MyApplication.defaultIndex
                                                ? pageIndex + 1 : pageIndex - 1, null);
                                    } else {
                                        Utils.logd("newRequest1 NOT null");
                                        Utils.logd("REQUEST " + (pageIndex > MyApplication.defaultBackIndex
                                                ? pageIndex + 1 : pageIndex - 1) + " : " + newRequest1.getUrl());
                                        MyApplication.backRequestsQueue.put(pageIndex > MyApplication.defaultBackIndex
                                                ? pageIndex + 1 : pageIndex - 1, null);
                                    }
                                }
                            }
                        }

                        items = JsonCache.collectData(items, progresses.getJSONObject(i), i, flash, "go");
//                        Utils.logd("items collectData " + pageIndex + " : " + items.size());

                        if (items.size() > 0) {
                            title = Utils.getMoneyFormat(Math.round(Float.parseFloat(items.get(0).getValues().get("harga_diskon"))), false);
                            minPrice = Math.round(Float.parseFloat(items.get(0).getValues().get("harga_diskon")));
                            maxPrice = Math.round(Float.parseFloat(items.get(items.size() - 1).getValues().get("harga_diskon")));
                            Filter.minPrice = minPrice;
                            Filter.maxPrice = maxPrice;
//                            Utils.logd("title: " + title);
                            if (mListener != null) {
                                mListener.onTitleChangeListener(pageIndex, title);
                                mListener.onMaxPriceChangeListener(Math.round(Float.parseFloat(items.get(items.size() - 1).getValues().get("harga_diskon"))));
                            }
                        }
//                        Utils.logd("percentage: " + (percentage));
                        if (percentage >= 100) {
                            progressBar.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    searchingStatusLayout.setVisibility(View.GONE);
                                }
                            }, 1000);
                            if (items.size() == 0) {
                                if (mListener != null) {
                                    mListener.onTitleChangeListener(pageIndex, "");
                                }
                                emptyResultTextView.setVisibility(View.VISIBLE);
                            }
                        }

                        if (mode == MyItemRecyclerViewAdapter.MODE_GO) {
                            MyApplication.backItems = JsonCache.collectData(MyApplication.backItems, progresses.getJSONObject(i), i, flash, "back");
                        }

                        if (progress.getInt("state") == 1) {
                            progressesRequest.put(progress);
                        }

                        adapter.notifyDataSetChanged();
                    }

                    if (progressesRequest.length() > 0) {
                        url = url + "&track_id=" + trackId + "&track_time=" + trackTime + "&progress=" + progressesRequest.toString();

                        request = new JsonObjectRequest(Request.Method.GET, url, responseListener, errorListener) {
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                HashMap<String, String> params = new HashMap<String, String>();
                                String creds = String.format("%s:%s", "trial", "fauzan");
                                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                                params.put("Authorization", auth);
                                return params;
                            }

                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<>();
                                params.put("accesskey", "780b4191985eab0fa83b6bf3adbaaf52");
                                return params;
                            }
                        };

                        request.setRetryPolicy(new DefaultRetryPolicy(
                                0,
                                Constants.DEFAULT_MAX_RETRIES,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (activity != null) {
                                    MyApplication.goQueue.add(request);
                                }
                            }
                        }, 100);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    private MyItemRecyclerViewAdapter adapter;
    private int pageIndex;
    private boolean requested;
    private AnimateHorizontalProgressBar progressBar;
    private LinearLayout searchingStatusLayout;
    private TextView emptyResultTextView;
    private FragmentActivity activity;
    private String nextActivity = "";

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static SearchResultFragment newInstance(int count, int pageIndex, String date, int mode, Bundle args) {
        SearchResultFragment fragment = new SearchResultFragment();
        bundle = new Bundle();
        bundle.putInt(ARG_COLUMN_COUNT, count);
        bundle.putInt(MODE, mode);
        bundle.putInt("pageIndex", pageIndex);
        bundle.putBundle("args", args);
        bundle.putString("date", date);
        fragment.setArguments(bundle);
        Utils.logd("id : " + bundle.getInt("pageIndex") + " ||  date : " + date);
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
//        Utils.logd("userVisible");
        if (items != null) {
            Utils.logd("items userVisible " + pageIndex + " : " + items.size());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.logd("onCreate");
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            mode = getArguments().getInt(MODE);
            pageIndex = getArguments().getInt("pageIndex");

            if (mode == MyItemRecyclerViewAdapter.MODE_GO) {
                isDefaultIndex = pageIndex == MyApplication.defaultIndex;
            } else {
                isDefaultIndex = pageIndex == MyApplication.defaultBackIndex;
            }
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Utils.logd("onCreateView");
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        Context context = view.getContext();
        searchingStatusLayout = (LinearLayout) view.findViewById(R.id.searchingStatusLayout);
        progressBar = (AnimateHorizontalProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setMaxWithAnim(100);
        emptyResultTextView = (TextView) view.findViewById(R.id.emptyResultTextView);
        recyclerView = (RecyclerView) view.findViewById(R.id.list);

//        if (isDefaultIndex && mode == MyItemRecyclerViewAdapter.MODE_BACK) {
//        }

        Log.d("test", "mColuemnCount " + mColumnCount);
        if (mode == MyItemRecyclerViewAdapter.MODE_GO) {
            if (getArguments() != null && !requested) {
                performRequest();
            }
        } else if (mode == MyItemRecyclerViewAdapter.MODE_BACK) {
            if (getArguments() != null) {
                performRequest();
            }

            if (isDefaultIndex) {
                items = MyApplication.backItems;
                searchingStatusLayout.setVisibility(View.GONE);

                title = Utils.getMoneyFormat(Math.round(Float.parseFloat(items.get(0).getValues().get("harga_diskon"))), false);
                minPrice = Math.round(Float.parseFloat(items.get(0).getValues().get("harga_diskon")));
                maxPrice = Math.round(Float.parseFloat(items.get(items.size() - 1).getValues().get("harga_diskon")));
                Filter.minPrice = minPrice;
                Filter.maxPrice = maxPrice;
//                            Utils.logd("title: " + title);
                if (mListener != null) {
                    mListener.onTitleChangeListener(pageIndex, title);
                    mListener.onMinPriceChangeListener(Math.round(Float.parseFloat(items.get(0).getValues().get("harga_diskon"))));
                    mListener.onMaxPriceChangeListener(Math.round(Float.parseFloat(items.get(items.size() - 1).getValues().get("harga_diskon"))));

                }
            }

        }

        Log.d("test", "mColumnCount " + mColumnCount);

        // Set the adapter
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManagerWithSmoothScroller(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(5));
        recyclerView.addItemDecoration(new DividerItemDecoration(activity, R.drawable.divider));
        Utils.logd("bunde " + pageIndex);
        adapter = new MyItemRecyclerViewAdapter(items, mListener, mode, getArguments() != null ? getArguments() : new Bundle()) {
            @Override
            public void onItemClickListener(LinearLayout chooseButton, Ticket item, int position) {
                super.onItemClickListener(chooseButton, item, position);
                if (recyclerView != null) {
                    Rect scrollBounds = new Rect();
                    recyclerView.getHitRect(scrollBounds);
                    if (chooseButton.getLocalVisibleRect(scrollBounds)) {
                        // Any portion of the imageView, even a single pixel, is within the visible window
                    } else {
                        notifyDataSetChanged();
                    }
                }
            }
        };
        recyclerView.setAdapter(adapter);
        return view;
    }

    private void performRequest() {

        Bundle args = getArguments().getBundle("args");
        String date = getArguments().getString("date");

        String fromCode = args.getString("fromCode");
        String toCode = args.getString("toCode");
        String backDate = args.getString("backDate");
        isReturn = args.getString("isReturn");
        String adultCount = args.getString("adultCount");
        String childCount = args.getString("childCount");
        String infantCount = args.getString("infantCount");
        Utils.logd("VISIBLE pageIndex : " + pageIndex + " ||  date : " + date);
//            Utils.logd("goDate : " + pageIndex);

        if (mode == MyItemRecyclerViewAdapter.MODE_GO) {
            url = url + "&from_code=" + fromCode + "&to_code=" + toCode + "&date_go=" + date + "&is_return=" + isReturn + "&date_back=" + backDate
                    + "&adult_count=" + adultCount + "&child_count=" + childCount + "&infant_count=" + infantCount;
        } else {
            url = url + "&from_code=" + toCode + "&to_code=" + fromCode + "&date_go=" + date + "&is_return=0&date_back=&adult_count="
                    + adultCount + "&child_count=" + childCount + "&infant_count=" + infantCount;
        }

        flash.put("go_choose", "");
        flash.put("back_choose", "");

        request = new JsonObjectRequest(Request.Method.GET, url, responseListener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                String creds = String.format("%s:%s", "trial", "fauzan");
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                params.put("Authorization", auth);
                return params;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("accesskey", "780b4191985eab0fa83b6bf3adbaaf52");
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                Constants.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if (mode == MyItemRecyclerViewAdapter.MODE_GO) {
            if (isDefaultIndex) {
                Utils.logd("REQUEST " + pageIndex + " : " + url);
                MyApplication.goQueue.add(request);
                requested = true;
            } else {
                MyApplication.goRequestsQueue.put(pageIndex, request);
            }
        } else {
            Utils.logd("PUT " + pageIndex + " : " + url);
            if (!requested) {
                MyApplication.backRequestsQueue.put(pageIndex, request);
                requested = true;
                if (pageIndex == MyApplication.defaultBackIndex + 1) {
                    JsonObjectRequest newRequest1 = MyApplication.backRequestsQueue.get(pageIndex);
                    if (newRequest1 != null) {
                        Utils.logd("REQUEST " + (pageIndex) + " : " + newRequest1.getUrl());
                        MyApplication.goQueue.add(newRequest1);
                        requested = true;
                        MyApplication.backRequestsQueue.put(pageIndex, null);

                    }
                }
                if (pageIndex == MyApplication.defaultBackIndex - 1) {
                    final JsonObjectRequest newRequest2 = MyApplication.backRequestsQueue.get(pageIndex);
                    if (newRequest2 != null) {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Utils.logd("REQUEST " + (pageIndex) + " : " + newRequest2.getUrl());
                                if (activity != null)
                                    MyApplication.goQueue.add(newRequest2);
                                MyApplication.backRequestsQueue.put(pageIndex, null);
                                requested = true;
                            }
                        }, 1000);
                    }
                }
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
        this.activity = getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        activity = null;
        if (mode == MyItemRecyclerViewAdapter.MODE_GO) {
            MyApplication.goRequestsQueue.clear();
        } else {
            MyApplication.backRequestsQueue.clear();
        }
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Ticket item);

        void onTitleChangeListener(int position, String title);

        void onMinPriceChangeListener(int harga_diskon);

        void onMaxPriceChangeListener(int harga_diskon);
    }

    @Override
    public void onResume() {
        super.onResume();

        Utils.logd("onResume on SearchResultFragment pageIndex: " + pageIndex);
        Utils.logd("sortMode " + Filter.sortMode);
        Utils.logd("nextActivity = " + MyApplication.nextActivity);
        if (MyApplication.nextActivity.equals("search_option")) {

            Utils.logd("items size " + items.size());
            ArrayList<Ticket> filteredItems = new ArrayList<>();

            Utils.logd("Filter.minPrice " + Filter.minPrice + ", minPrice " + minPrice);
            Utils.logd("Filter.maxPrice " + Filter.maxPrice + ", maxPrice " + maxPrice);

            boolean isFiltered = (items.size() > 0 && (Filter.mSelectedFlightItemCodes.size() != Constants.airlines.size() ||
                    Filter.mSelectedClassItems.size() != Constants.classes.size()
                    || Filter.mSelectedTransitItems.size() != Constants.transits.size() || Filter.directFlight ||
                    Filter.minPrice > minPrice || Filter.maxPrice < maxPrice));

            if (isFiltered) {
                Utils.logd("not equal");
                try {
                    for (Ticket ticket : items) {
                        Airline airline = Constants.airlines.get(ticket.getFlights().getJSONObject(0).getString("airlines"));
                        if (Filter.mSelectedFlightItemCodes.contains(airline.getCode())) {
                            filteredItems.add(ticket);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                for (Iterator<Ticket> it = filteredItems.iterator(); it.hasNext(); ) {
                    Ticket ticket = it.next();
                    String kelas = ticket.getValues().get("kelas");
                    if (!Filter.mSelectedClassItemCodes.contains(kelas)) {
                        it.remove();
                    }
                }

                if (!Filter.directFlight) {
                    Utils.logd("semua penerbangan");
                    for (Iterator<Ticket> it = filteredItems.iterator(); it.hasNext(); ) {
                        Ticket ticket = it.next();
                        String transit = (ticket.getFlights().length() - 1) + "";
                        if (!Filter.mSelectedTransitItemCodes.contains(transit) && !transit.equals("0")) {
                            it.remove();
                        }
                    }
                } else {
                    Utils.logd("hanya yang langsung");
                    for (Iterator<Ticket> it = filteredItems.iterator(); it.hasNext(); ) {
                        Ticket ticket = it.next();
                        int transit = ticket.getFlights().length() - 1;
                        if (transit > 0) {
                            it.remove();
                        }
                    }
                }

                for (Iterator<Ticket> it = filteredItems.iterator(); it.hasNext(); ) {
                    Ticket ticket = it.next();
                    int price = Math.round(Float.parseFloat(ticket.getValues().get("harga_diskon")));
                    if (price < Filter.minPrice || price > Filter.maxPrice) {
                        it.remove();
                    }
                }


                Utils.logd("filtered size " + filteredItems.size());

                MyItemRecyclerViewAdapter filteredAdapter = new MyItemRecyclerViewAdapter(filteredItems, mListener, mode, bundle) {
                    @Override
                    public void onItemClickListener(LinearLayout chooseButton, Ticket item, int position) {
                        super.onItemClickListener(chooseButton, item, position);
                        if (recyclerView != null) {
                            Rect scrollBounds = new Rect();
                            recyclerView.getHitRect(scrollBounds);
                            if (!chooseButton.getLocalVisibleRect(scrollBounds)) {
                                notifyDataSetChanged();
                            }
                        }
                    }
                };
                recyclerView.setAdapter(filteredAdapter);
            }

            switch (Filter.sortMode) {
                case LOWEST_PRICE:
                    Collections.sort(items, new Comparator<Ticket>() {
                        @Override
                        public int compare(final Ticket object1, final Ticket object2) {
                            int price1 = Math.round(Float.parseFloat(object1.getValues().get("harga_diskon")));
                            int price2 = Math.round(Float.parseFloat(object2.getValues().get("harga_diskon")));
                            return (int) (price1 - price2);
                        }
                    });
                    Collections.sort(filteredItems, new Comparator<Ticket>() {
                        @Override
                        public int compare(final Ticket object1, final Ticket object2) {
                            int price1 = Math.round(Float.parseFloat(object1.getValues().get("harga_diskon")));
                            int price2 = Math.round(Float.parseFloat(object2.getValues().get("harga_diskon")));
                            return (int) (price1 - price2);
                        }
                    });
                    adapter.notifyDataSetChanged();
                    break;
                case DEPART_TIME:
                    Collections.sort(items, new Comparator<Ticket>() {
                        @Override
                        public int compare(final Ticket object1, final Ticket object2) {
                            try {
                                String departTime1 = object1.getFlights().getJSONObject(0).getString("depart_time");
                                String departTime2 = object2.getFlights().getJSONObject(0).getString("depart_time");
                                String pattern = "HH:mm";
                                SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
                                Date one = dateFormat.parse(departTime1);
                                Date two = dateFormat.parse(departTime2);
                                return one.compareTo(two);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                return 0;
                            } catch (ParseException e) {
                                e.printStackTrace();
                                return 0;
                            }
                        }
                    });
                    Collections.sort(filteredItems, new Comparator<Ticket>() {
                        @Override
                        public int compare(final Ticket object1, final Ticket object2) {
                            try {
                                String departTime1 = object1.getFlights().getJSONObject(0).getString("depart_time");
                                String departTime2 = object2.getFlights().getJSONObject(0).getString("depart_time");
                                String pattern = "HH:mm";
                                SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
                                Date one = dateFormat.parse(departTime1);
                                Date two = dateFormat.parse(departTime2);
                                return one.compareTo(two);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                return 0;
                            } catch (ParseException e) {
                                e.printStackTrace();
                                return 0;
                            }
                        }
                    });
                    adapter.notifyDataSetChanged();
                    break;
                case ARRIVE_TIME:
                    Collections.sort(items, new Comparator<Ticket>() {
                        @Override
                        public int compare(final Ticket object1, final Ticket object2) {
                            try {
                                String departTime1 = object1.getFlights().getJSONObject(0).getString("arrive_time");
                                String departTime2 = object2.getFlights().getJSONObject(0).getString("arrive_time");
                                String pattern = "HH:mm";
                                SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
                                Date one = dateFormat.parse(departTime1);
                                Date two = dateFormat.parse(departTime2);
                                return one.compareTo(two);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                return 0;
                            } catch (ParseException e) {
                                e.printStackTrace();
                                return 0;
                            }
                        }
                    });
                    Collections.sort(filteredItems, new Comparator<Ticket>() {
                        @Override
                        public int compare(final Ticket object1, final Ticket object2) {
                            try {
                                String departTime1 = object1.getFlights().getJSONObject(0).getString("arrive_time");
                                String departTime2 = object2.getFlights().getJSONObject(0).getString("arrive_time");
                                String pattern = "HH:mm";
                                SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
                                Date one = dateFormat.parse(departTime1);
                                Date two = dateFormat.parse(departTime2);
                                return one.compareTo(two);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                return 0;
                            } catch (ParseException e) {
                                e.printStackTrace();
                                return 0;
                            }
                        }
                    });
                    adapter.notifyDataSetChanged();
                    break;
                case DURATION:
                    Collections.sort(items, new Comparator<Ticket>() {
                        @Override
                        public int compare(final Ticket object1, final Ticket object2) {
                            try {
                                String duration1 = object1.getValues().get("travel_duration");
                                Utils.logd("travelDuration " + duration1);
                                String duration2 = object2.getValues().get("travel_duration");
                                String pattern = "HH:mm";
                                SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
                                Date one = dateFormat.parse(duration1);
                                Date two = dateFormat.parse(duration2);
                                return one.compareTo(two);
                            } catch (ParseException e) {
                                e.printStackTrace();
                                return 0;
                            }
                        }
                    });
                    Collections.sort(filteredItems, new Comparator<Ticket>() {
                        @Override
                        public int compare(final Ticket object1, final Ticket object2) {
                            try {
                                String duration1 = object1.getValues().get("travel_duration");
                                Utils.logd("travelDuration " + duration1);
                                String duration2 = object2.getValues().get("travel_duration");
                                String pattern = "HH:mm";
                                SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
                                Date one = dateFormat.parse(duration1);
                                Date two = dateFormat.parse(duration2);
                                return one.compareTo(two);
                            } catch (ParseException e) {
                                e.printStackTrace();
                                return 0;
                            }
                        }
                    });
                    adapter.notifyDataSetChanged();
                    break;
                default:
                    Collections.sort(items, new Comparator<Ticket>() {
                        @Override
                        public int compare(final Ticket object1, final Ticket object2) {
                            int price1 = Math.round(Float.parseFloat(object1.getValues().get("harga_diskon")));
                            int price2 = Math.round(Float.parseFloat(object2.getValues().get("harga_diskon")));
                            return (int) (price1 - price2);
                        }
                    });
                    adapter.notifyDataSetChanged();

            }
            adapter.notifyDataSetChanged();
        }
    }
}
