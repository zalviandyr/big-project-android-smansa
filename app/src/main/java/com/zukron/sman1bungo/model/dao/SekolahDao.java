package com.zukron.sman1bungo.model.dao;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.zukron.sman1bungo.model.Sekolah;
import com.zukron.sman1bungo.util.api.SekolahEndpoint;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SekolahDao {
    private Context context;
    private SekolahDao.onListener onListener;
    private boolean isRequestFinished = false;
    private RequestQueue requestQueue;

    public SekolahDao(Context context, SekolahDao.onListener onListener) {
        this.context = context;
        this.onListener = onListener;

        this.requestQueue = Volley.newRequestQueue(this.context);
    }

    /***
     * return false jika dipanggil diluar interface method response
     */
    public boolean isRequestFinished() {
        return this.isRequestFinished;
    }

    public void get() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, SekolahEndpoint.get(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                isRequestFinished = true;
                try {
                    JSONObject sekolahJson = new JSONObject(response);
                    JSONObject dataJson = sekolahJson.getJSONObject("data");

                    Sekolah sekolah = new Sekolah(
                            dataJson.getString("nama"),
                            dataJson.getString("alamat"),
                            dataJson.getString("kota"),
                            dataJson.getString("provinsi"),
                            dataJson.getDouble("longitude"),
                            dataJson.getDouble("latitude")
                    );
                    String message = sekolahJson.getString("message");

                    onListener.sekolahResponse(sekolah);
                    onListener.messageResponse(Request.Method.GET, message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onListener.errorResponse(error);
            }
        });

        executeRequestQueue(stringRequest);
    }

    public void post(final Sekolah sekolah) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SekolahEndpoint.post(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                isRequestFinished = true;
                try {
                    JSONObject sekolahJson = new JSONObject(response);
                    String message = sekolahJson.getString("message");

                    onListener.messageResponse(Request.Method.POST, message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onListener.errorResponse(error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("nama", sekolah.getNama());
                params.put("alamat", sekolah.getAlamat());
                params.put("kota", sekolah.getKota());
                params.put("provinsi", sekolah.getProvinsi());
                params.put("longitude", String.valueOf(sekolah.getLongitude()));
                params.put("latitude", String.valueOf(sekolah.getLatitude()));

                return params;
            }
        };

        executeRequestQueue(stringRequest);
    }

    public void put(final Sekolah sekolah) {
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, SekolahEndpoint.put(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                isRequestFinished = true;
                try {
                    JSONObject sekolahJson = new JSONObject(response);
                    String message = sekolahJson.getString("message");

                    onListener.messageResponse(Request.Method.PUT, message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onListener.errorResponse(error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("nama", sekolah.getNama());
                params.put("alamat", sekolah.getAlamat());
                params.put("kota", sekolah.getKota());
                params.put("provinsi", sekolah.getProvinsi());
                params.put("longitude", String.valueOf(sekolah.getLongitude()));
                params.put("latitude", String.valueOf(sekolah.getLatitude()));

                return params;
            }
        };

        executeRequestQueue(stringRequest);
    }

    public interface onListener {
        /***
         * need get method
         */
        void sekolahResponse(Sekolah sekolah);

        void messageResponse(int method, String message);

        void errorResponse(VolleyError error);
    }

    private void executeRequestQueue(StringRequest stringRequest) {
        // if low connection
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        this.requestQueue.add(stringRequest);
    }
}
