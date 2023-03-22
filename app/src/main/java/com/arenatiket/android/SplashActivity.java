package com.arenatiket.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.arenatiket.android.api.JsonCache;
import com.arenatiket.android.utils.Constants;
import com.arenatiket.android.utils.MyApplication;
import com.arenatiket.android.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SplashActivity extends Activity {

    private ImageView splash;
    protected SharedPreferences preferences;
    private boolean isScheduled;
    private String ports;
    private String nationalityUrl = Constants.baseApiUrl + "android/attrs/nationality?accesskey=780b4191985eab0fa83b6bf3adbaaf52";
    private String airportUrl = Constants.baseApiUrl + "android/attrs/airports?accesskey=780b4191985eab0fa83b6bf3adbaaf52";
    private String travellerUrl = Constants.baseApiUrl + "android/attrs/traveller?accesskey=780b4191985eab0fa83b6bf3adbaaf52";
    private String intTravellerUrl = Constants.baseApiUrl + "android/attrs/traveller_international?accesskey=780b4191985eab0fa83b6bf3adbaaf52";
    private String mealsUrl = Constants.baseApiUrl + "android/attrs/facilities_meal?accesskey=780b4191985eab0fa83b6bf3adbaaf52";
    private String baggageUrl = Constants.baseApiUrl + "android/attrs/bagage_weights?accesskey=780b4191985eab0fa83b6bf3adbaaf52";
    private String titleUrl = Constants.baseApiUrl + "android/attrs/titles?accesskey=780b4191985eab0fa83b6bf3adbaaf52";
    private String airlinesUrl = Constants.baseApiUrl + "android/attrs/airlines?accesskey=780b4191985eab0fa83b6bf3adbaaf52";
    private String banksUrl = Constants.baseApiUrl + "android/attrs/banks?accesskey=780b4191985eab0fa83b6bf3adbaaf52";
    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Utils.logd("message : " + error.toString());
        }
    };
    private JSONArray bankJsonArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        startAnimation();
        preferences = getSharedPreferences("UserAccount", MODE_PRIVATE);
        isScheduled = preferences.getBoolean("isScheduled", false);

        getInfo();
    }

    private void getNationality() {
        Utils.logd("nationalityUrl " + nationalityUrl);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, nationalityUrl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Utils.longLog("nationality response : " + response.toString());
                try {
                    JSONArray jsonArray = response.getJSONArray("results");
                    JsonCache.saveNationalities(SplashActivity.this, jsonArray.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, errorListener) {
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
                Constants.MY_SOCKET_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(request);
    }

    private void getAirport() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, airportUrl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Utils.longLog("airportUrl response : " + response.toString());
                try {
                    JSONArray jsonArray = response.getJSONArray("results");
                    JsonCache.saveAirports(SplashActivity.this, jsonArray.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, errorListener) {
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
                Constants.MY_SOCKET_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(request);
    }

    private void getTraveller() {
        Utils.logd("travellerUrl " + travellerUrl);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, travellerUrl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Utils.longLog("travellerUrl response : " + response.toString());
                try {
                    JsonCache.setTravellers(SplashActivity.this, response.getJSONObject("results").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, errorListener) {
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
                Constants.MY_SOCKET_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(request);
    }

    private void getInternationalTraveller() {
        Utils.logd("intTravellerUrl " + intTravellerUrl);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, intTravellerUrl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Utils.longLog("intTravellerUrl response : " + response.toString());
                try {
                    JsonCache.setIntTravellers(SplashActivity.this, response.getJSONObject("results").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, errorListener) {
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
                Constants.MY_SOCKET_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(request);
    }

    private void getMeals() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, mealsUrl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Utils.longLog("mealsUrl response : " + response.toString());
                try {
                    JSONObject results = response.getJSONObject("results");
                    Iterator<?> keys = results.keys();
                    while (keys.hasNext()) {
                        String key = (String) keys.next();
                        Utils.logd("key " + key + ", value: " + results.getInt(key));
                        Constants.meals.put(key, "" + results.getInt(key));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, errorListener) {
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
                Constants.MY_SOCKET_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(request);
    }

    private void getBaggage() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, mealsUrl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Utils.longLog("mealsUrl response : " + response.toString());
            }
        }, errorListener) {
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
                Constants.MY_SOCKET_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(request);
    }

    private void getTitles() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, titleUrl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Utils.longLog("titleUrl response : " + response.toString());
            }
        }, errorListener) {
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
                Constants.MY_SOCKET_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(request);
    }

    private void getAirlines() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, airlinesUrl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Utils.longLog("airlinesUrl response : " + response.toString());
            }
        }, errorListener) {
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
                Constants.MY_SOCKET_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(request);
    }

    private void getBanks() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, banksUrl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Utils.longLog("banksUrl response : " + response.toString());
                try {
                    bankJsonArray = response.getJSONArray("results");
                    JsonCache.saveBanks(SplashActivity.this, bankJsonArray.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, errorListener) {
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
                Constants.MY_SOCKET_TIMEOUT_MS,
                Constants.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(request);
    }

    public void startAnimation() {
        Animation fade2 = AnimationUtils.loadAnimation(this, R.anim.fade_in2);
        if (fade2 == null) {
            Log.d("fade2", "null");
        }
        splash = (ImageView) findViewById(R.id.splash_imageView1);
        if (splash == null) {
            Log.d("Splash", "null");
        }
        splash.startAnimation(fade2);

        fade2.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation arg0) {
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationEnd(Animation args0) {
                if (JsonCache.getAirports(SplashActivity.this).size() > Constants.DEFAULT_FROM_AIRPORT_POSITION) {
                    startActivity(new Intent(SplashActivity.this, SearchActivity.class));
                    SplashActivity.this.finish();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
                    builder.setTitle("Error");
                    builder.setMessage("Gagal mengambil informasi airport");
                    builder.setPositiveButton("Coba lagi", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getInfo();
                        }
                    });
                    builder.setNegativeButton("Tutup", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    startAnimation();
                    builder.show();
                }
            }
        });
    }

    private void getInfo() {
        getNationality();
        getAirport();
        getTraveller();
        getInternationalTraveller();
        getBaggage();
        getMeals();
        getTitles();
        getAirlines();
        getBanks();
    }

}
