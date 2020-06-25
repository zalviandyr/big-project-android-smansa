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
import com.zukron.sman1bungo.model.Gaji;
import com.zukron.sman1bungo.util.api.GajiEndpoint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GajiDao {
    private Context context;
    private GajiDao.onListener onListener;
    private boolean isRequestFinished = false;
    private RequestQueue requestQueue;

    public GajiDao(Context context, GajiDao.onListener onListener) {
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

    public void get(String idGaji) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, GajiEndpoint.get(idGaji), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                isRequestFinished = true;
                try {
                    JSONObject gajiJson = new JSONObject(response);
                    JSONObject dataJson = gajiJson.getJSONObject("data");

                    Gaji gaji = new Gaji(
                            dataJson.getString("id_gaji"),
                            dataJson.getInt("gaji_pokok")
                    );
                    String message = gajiJson.getString("message");

                    onListener.gajiResponse(gaji);
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

    public void getAll() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, GajiEndpoint.get("ALL"), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                isRequestFinished = true;
                ArrayList<Gaji> gajiList = new ArrayList<>();
                try {
                    JSONObject gajiJson = new JSONObject(response);
                    JSONArray gajiArray = gajiJson.getJSONArray("data");

                    for (int i = 0; i < gajiArray.length(); i++) {
                        JSONObject dataJson = gajiArray.getJSONObject(i);
                        gajiList.add(new Gaji(
                                dataJson.getString("id_gaji"),
                                dataJson.getInt("gaji_pokok")
                        ));
                    }
                    String message = gajiJson.getString("message");

                    onListener.gajiListResponse(gajiList);
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

    public void post(final Gaji gaji) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GajiEndpoint.post(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                isRequestFinished = true;
                try {
                    JSONObject gajiJson = new JSONObject(response);
                    String message = gajiJson.getString("message");

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
                params.put("gaji_pokok", String.valueOf(gaji.getGajiPokok()));

                return params;
            }
        };

        executeRequestQueue(stringRequest);
    }

    public void put(final Gaji gaji) {
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, GajiEndpoint.put(gaji.getIdGaji()), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                isRequestFinished = true;
                try {
                    JSONObject gajiJson = new JSONObject(response);
                    String message = gajiJson.getString("message");

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
                params.put("gaji_pokok", String.valueOf(gaji.getGajiPokok()));

                return params;
            }
        };

        executeRequestQueue(stringRequest);
    }

    public void delete(String idGaji) {
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, GajiEndpoint.delete(idGaji), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                isRequestFinished = true;
                try {
                    JSONObject gajiJson = new JSONObject(response);
                    String message = gajiJson.getString("message");

                    onListener.messageResponse(Request.Method.DELETE, message);
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

    public interface onListener {
        /***
         * need get method
         */
        void gajiResponse(Gaji gaji);

        /***
         * need getAll method
         */
        void gajiListResponse(ArrayList<Gaji> gajiList);

        void messageResponse(int method, String message);

        void errorResponse(VolleyError error);
    }

    private void executeRequestQueue(StringRequest stringRequest) {
        // if low connection
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        this.requestQueue.add(stringRequest);
    }
}
