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
import com.zukron.sman1bungo.model.Kelas;
import com.zukron.sman1bungo.util.api.KelasEndpoint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class KelasDao {
    private Context context;
    private KelasDao.onListener onListener;
    private boolean isRequestFinished = false;
    private RequestQueue requestQueue;

    public KelasDao(Context context, KelasDao.onListener onListener) {
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

    public void get(String idKelas) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, KelasEndpoint.get(idKelas), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                isRequestFinished = true;
                try {
                    JSONObject kelasJson = new JSONObject(response);
                    JSONObject dataJson = kelasJson.getJSONObject("data");

                    Kelas kelas = new Kelas(
                            dataJson.getString("id_kelas"),
                            dataJson.getString("nama"),
                            dataJson.getString("wali_kelas")
                    );

                    String message = kelasJson.getString("message");
                    onListener.kelasResponse(kelas);
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
        StringRequest stringRequest = new StringRequest(Request.Method.GET, KelasEndpoint.get("ALL"), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                isRequestFinished = true;
                ArrayList<Kelas> kelasList = new ArrayList<>();
                try {
                    JSONObject kelasJson = new JSONObject(response);
                    JSONArray dataArray = kelasJson.getJSONArray("data");

                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject datasJson = dataArray.getJSONObject(i);
                        kelasList.add(new Kelas(
                                datasJson.getString("id_kelas"),
                                datasJson.getString("nama"),
                                datasJson.getString("wali_kelas")
                        ));
                    }

                    String message = kelasJson.getString("message");
                    onListener.kelasListResponse(kelasList);
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

    public void post(final Kelas kelas) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, KelasEndpoint.post(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                isRequestFinished = true;
                try {
                    JSONObject kelasJson = new JSONObject(response);
                    String message = kelasJson.getString("message");

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
                params.put("nama", kelas.getNama());
                params.put("wali_kelas", kelas.getWaliKelas());

                return params;
            }
        };

        executeRequestQueue(stringRequest);
    }

    public void put(final Kelas kelas) {
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, KelasEndpoint.put(kelas.getIdKelas()), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                isRequestFinished = true;
                try {
                    JSONObject kelasJson = new JSONObject(response);
                    String message = kelasJson.getString("message");

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
                params.put("nama", kelas.getNama());
                params.put("wali_kelas", kelas.getWaliKelas());

                return params;
            }
        };

        executeRequestQueue(stringRequest);
    }

    public void delete(String idKelas) {
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, KelasEndpoint.delete(idKelas), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                isRequestFinished = true;
                try {
                    JSONObject kelasJson = new JSONObject(response);
                    String message = kelasJson.getString("message");

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
        void kelasResponse(Kelas kelas);

        /***
         * need getAll method
         */
        void kelasListResponse(ArrayList<Kelas> kelasList);

        void messageResponse(int method, String message);

        void errorResponse(VolleyError error);
    }

    private void executeRequestQueue(StringRequest stringRequest) {
        // if low connection
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        this.requestQueue.add(stringRequest);
    }
}
