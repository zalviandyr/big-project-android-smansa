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
import com.zukron.sman1bungo.model.Pelajaran;
import com.zukron.sman1bungo.util.api.PelajaranEndpoint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PelajaranDao {
    private Context context;
    private PelajaranDao.onListener onListener;
    private boolean isRequestFinished = false;
    private RequestQueue requestQueue;

    public PelajaranDao(Context context, PelajaranDao.onListener onListener) {
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

    public void get(String idPelajaran) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, PelajaranEndpoint.get(idPelajaran), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                isRequestFinished = true;
                try {
                    JSONObject pelajaranJson = new JSONObject(response);
                    Pelajaran pelajaran = new Pelajaran(
                            pelajaranJson.getString("id_pelajaran"),
                            pelajaranJson.getString("nama")
                    );

                    onListener.pelajaranResponse(pelajaran);
                    onListener.defaultResponse(response);
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
        StringRequest stringRequest = new StringRequest(Request.Method.GET, PelajaranEndpoint.get("ALL"), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                isRequestFinished = true;
                ArrayList<Pelajaran> pelajaranList = new ArrayList<>();
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject pelajaranJson = jsonArray.getJSONObject(i);
                        pelajaranList.add(new Pelajaran(
                                pelajaranJson.getString("id_pelajaran"),
                                pelajaranJson.getString("nama")
                        ));
                    }

                    onListener.pelajaranListResponse(pelajaranList);
                    onListener.defaultResponse(response);
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

    public void post(final Pelajaran pelajaran) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, PelajaranEndpoint.post(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                isRequestFinished = true;
                onListener.defaultResponse(response);
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
                params.put("nama", pelajaran.getNama());

                return params;
            }
        };

        executeRequestQueue(stringRequest);
    }

    public void put(final Pelajaran pelajaran) {
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, PelajaranEndpoint.put(pelajaran.getIdPelajaran()), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                isRequestFinished = true;
                onListener.defaultResponse(response);
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
                params.put("nama", pelajaran.getNama());

                return params;
            }
        };

        executeRequestQueue(stringRequest);
    }

    public void delete(String idPelajaran) {
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, PelajaranEndpoint.delete(idPelajaran), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                isRequestFinished = true;
                onListener.defaultResponse(response);
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
        void pelajaranResponse(Pelajaran pelajaran);

        /***
         * need getAll method
         */
        void pelajaranListResponse(ArrayList<Pelajaran> pelajaranList);

        void defaultResponse(String response);

        void errorResponse(VolleyError error);
    }

    private void executeRequestQueue(StringRequest stringRequest) {
        // if low connection
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        this.requestQueue.add(stringRequest);
    }
}
