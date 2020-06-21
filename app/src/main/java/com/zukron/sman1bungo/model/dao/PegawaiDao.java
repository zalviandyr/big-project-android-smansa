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
import com.zukron.sman1bungo.model.Pegawai;
import com.zukron.sman1bungo.util.api.PegawaiEndpoint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PegawaiDao {
    private Context context;
    private PegawaiDao.onListener onListener;
    private boolean isRequestFinished = false;
    private RequestQueue requestQueue;

    public PegawaiDao(Context context, PegawaiDao.onListener onListener) {
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

    public void getId(String idPegawai) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, PegawaiEndpoint.getId(idPegawai), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                isRequestFinished = true;
                try {
                    JSONObject pegawaiJson = new JSONObject(response);
                    Pegawai pegawai = new Pegawai(
                            pegawaiJson.getString("id_pegawai"),
                            pegawaiJson.getString("first_name"),
                            pegawaiJson.getString("last_name"),
                            LocalDate.parse(pegawaiJson.getString("tanggal_lahir")),
                            pegawaiJson.getString("no_hp"),
                            pegawaiJson.getString("jekel"),
                            pegawaiJson.getString("jabatan"),
                            pegawaiJson.getString("id_gaji"),
                            pegawaiJson.getString("username")
                    );

                    onListener.pegawaiResponse(pegawai);
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

    public void getUsername(String username) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, PegawaiEndpoint.getUsername(username), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                isRequestFinished = true;
                try {
                    JSONObject pegawaiJson = new JSONObject(response);
                    Pegawai pegawai = new Pegawai(
                            pegawaiJson.getString("id_pegawai"),
                            pegawaiJson.getString("first_name"),
                            pegawaiJson.getString("last_name"),
                            LocalDate.parse(pegawaiJson.getString("tanggal_lahir")),
                            pegawaiJson.getString("no_hp"),
                            pegawaiJson.getString("jekel"),
                            pegawaiJson.getString("jabatan"),
                            pegawaiJson.getString("id_gaji"),
                            pegawaiJson.getString("username")
                    );

                    onListener.pegawaiResponse(pegawai);
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
        StringRequest stringRequest = new StringRequest(Request.Method.GET, PegawaiEndpoint.getId("ALL"), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                isRequestFinished = true;
                ArrayList<Pegawai> pegawaiList = new ArrayList<>();
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject pegawaiJson = array.getJSONObject(i);
                        pegawaiList.add(new Pegawai(
                                pegawaiJson.getString("id_pegawai"),
                                pegawaiJson.getString("first_name"),
                                pegawaiJson.getString("last_name"),
                                LocalDate.parse(pegawaiJson.getString("tanggal_lahir")),
                                pegawaiJson.getString("no_hp"),
                                pegawaiJson.getString("jekel"),
                                pegawaiJson.getString("jabatan"),
                                pegawaiJson.getString("id_gaji"),
                                pegawaiJson.getString("username")
                        ));
                    }

                    onListener.pegawaiListResponse(pegawaiList);
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

    public void postPartial(final Pegawai pegawai) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, PegawaiEndpoint.postPartial(), new Response.Listener<String>() {
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
                params.put("first_name", pegawai.getFirstName());
                params.put("last_name", pegawai.getLastName());
                params.put("tanggal_lahir", String.valueOf(pegawai.getTanggalLahir()));
                params.put("jekel", pegawai.getJekel());
                params.put("jabatan", pegawai.getJabatan());
                params.put("id_gaji", pegawai.getIdGaji());

                return params;
            }
        };

        executeRequestQueue(stringRequest);
    }

    public void postFull(final Pegawai pegawai) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, PegawaiEndpoint.postFull(), new Response.Listener<String>() {
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
                params.put("first_name", pegawai.getFirstName());
                params.put("last_name", pegawai.getLastName());
                params.put("tanggal_lahir", String.valueOf(pegawai.getTanggalLahir()));
                params.put("no_hp", pegawai.getNoHp());
                params.put("jekel", pegawai.getJekel());
                params.put("jabatan", pegawai.getJabatan());
                params.put("id_gaji", pegawai.getIdGaji());
                params.put("username", pegawai.getUsername());

                return params;
            }
        };

        executeRequestQueue(stringRequest);
    }

    public void putPartial(final Pegawai pegawai) {
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, PegawaiEndpoint.putPartial(pegawai.getIdPegawai()), new Response.Listener<String>() {
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
                params.put("id_pegawai", pegawai.getIdPegawai());
                params.put("first_name", pegawai.getFirstName());
                params.put("last_name", pegawai.getLastName());
                params.put("tanggal_lahir", String.valueOf(pegawai.getTanggalLahir()));
                params.put("jekel", pegawai.getJekel());
                params.put("jabatan", pegawai.getJabatan());
                params.put("id_gaji", pegawai.getIdGaji());

                return params;
            }
        };

        executeRequestQueue(stringRequest);
    }

    public void putFull(final Pegawai pegawai) {
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, PegawaiEndpoint.putFull(pegawai.getIdPegawai()), new Response.Listener<String>() {
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
                params.put("id_pegawai", pegawai.getIdPegawai());
                params.put("first_name", pegawai.getFirstName());
                params.put("last_name", pegawai.getLastName());
                params.put("tanggal_lahir", String.valueOf(pegawai.getTanggalLahir()));
                params.put("no_hp", pegawai.getNoHp());
                params.put("jekel", pegawai.getJekel());
                params.put("jabatan", pegawai.getJabatan());
                params.put("id_gaji", pegawai.getIdGaji());
                params.put("username", pegawai.getUsername());

                return params;
            }
        };

        executeRequestQueue(stringRequest);
    }

    public void delete(String idPegawai) {
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, PegawaiEndpoint.delete(idPegawai), new Response.Listener<String>() {
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
        void pegawaiResponse(Pegawai pegawai);

        /***
         * need getAll method
         */
        void pegawaiListResponse(ArrayList<Pegawai> pegawaiList);

        void defaultResponse(String response);

        void errorResponse(VolleyError error);
    }

    private void executeRequestQueue(StringRequest stringRequest) {
        // if low connection
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        this.requestQueue.add(stringRequest);
    }
}
