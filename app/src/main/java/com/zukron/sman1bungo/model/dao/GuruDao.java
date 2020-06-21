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
import com.zukron.sman1bungo.model.Guru;
import com.zukron.sman1bungo.util.api.GuruEndpoint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GuruDao {
    private Context context;
    private GuruDao.onListener onListener;
    private boolean isRequestFinished = false;
    private RequestQueue requestQueue;

    public GuruDao(Context context, GuruDao.onListener onListener) {
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

    public void getNip(String nip) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, GuruEndpoint.getNip(nip), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                isRequestFinished = true;
                try {
                    JSONObject guruJson = new JSONObject(response);
                    Guru guru = new Guru(
                            guruJson.getString("nip"),
                            guruJson.getString("first_name"),
                            guruJson.getString("last_name"),
                            guruJson.getString("email"),
                            guruJson.getString("no_hp"),
                            guruJson.getString("jekel"),
                            LocalDate.parse(guruJson.getString("tanggal_lahir")),
                            guruJson.getString("provinsi_lahir"),
                            guruJson.getString("kota_lahir"),
                            guruJson.getString("golongan"),
                            guruJson.getString("id_pelajaran"),
                            guruJson.getString("username"),
                            guruJson.getString("id_gaji")
                    );

                    onListener.guruResponse(guru);
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
        StringRequest stringRequest = new StringRequest(Request.Method.GET, GuruEndpoint.getUsername(username), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                isRequestFinished = true;
                try {
                    JSONObject guruJson = new JSONObject(response);
                    Guru guru = new Guru(
                            guruJson.getString("nip"),
                            guruJson.getString("first_name"),
                            guruJson.getString("last_name"),
                            guruJson.getString("email"),
                            guruJson.getString("no_hp"),
                            guruJson.getString("jekel"),
                            LocalDate.parse(guruJson.getString("tanggal_lahir")),
                            guruJson.getString("provinsi_lahir"),
                            guruJson.getString("kota_lahir"),
                            guruJson.getString("golongan"),
                            guruJson.getString("id_pelajaran"),
                            guruJson.getString("username"),
                            guruJson.getString("id_gaji")
                    );

                    onListener.guruResponse(guru);
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
        StringRequest stringRequest = new StringRequest(Request.Method.GET, GuruEndpoint.getNip("ALL"), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                isRequestFinished = true;
                ArrayList<Guru> guruList = new ArrayList<>();
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject guruJson = array.getJSONObject(i);
                        guruList.add(new Guru(
                                guruJson.getString("nip"),
                                guruJson.getString("first_name"),
                                guruJson.getString("last_name"),
                                guruJson.getString("email"),
                                guruJson.getString("no_hp"),
                                guruJson.getString("jekel"),
                                LocalDate.parse(guruJson.getString("tanggal_lahir")),
                                guruJson.getString("provinsi_lahir"),
                                guruJson.getString("kota_lahir"),
                                guruJson.getString("golongan"),
                                guruJson.getString("id_pelajaran"),
                                guruJson.getString("username"),
                                guruJson.getString("id_gaji")
                        ));
                    }

                    onListener.guruListResponse(guruList);
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

    public void postPartial(final Guru guru) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GuruEndpoint.postPartial(), new Response.Listener<String>() {
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
                params.put("nip", guru.getNip());
                params.put("first_name", guru.getFirstName());
                params.put("last_name", guru.getLastName());
                params.put("jekel", guru.getJekel());
                params.put("tanggal_lahir", String.valueOf(guru.getTanggalLahir()));
                params.put("golongan", guru.getGolongan());
                params.put("id_pelajaran", guru.getIdPelajaran());
                params.put("id_gaji", guru.getIdGaji());

                return params;
            }
        };

        executeRequestQueue(stringRequest);
    }

    public void postFull(final Guru guru) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GuruEndpoint.postFull(), new Response.Listener<String>() {
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
                params.put("nip", guru.getNip());
                params.put("first_name", guru.getFirstName());
                params.put("last_name", guru.getLastName());
                params.put("email", guru.getEmail());
                params.put("no_hp", guru.getNoHp());
                params.put("jekel", guru.getJekel());
                params.put("tanggal_lahir", String.valueOf(guru.getTanggalLahir()));
                params.put("provinsi_lahir", guru.getProvinsiLahir());
                params.put("kota_lahir", guru.getKotaLahir());
                params.put("golongan", guru.getGolongan());
                params.put("id_pelajaran", guru.getIdPelajaran());
                params.put("username", guru.getUsername());
                params.put("id_gaji", guru.getIdGaji());

                return params;
            }
        };

        executeRequestQueue(stringRequest);
    }

    public void putPartial(final Guru guru) {
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, GuruEndpoint.putPartial(guru.getNip()), new Response.Listener<String>() {
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
                params.put("first_name", guru.getFirstName());
                params.put("last_name", guru.getLastName());
                params.put("jekel", guru.getJekel());
                params.put("tanggal_lahir", String.valueOf(guru.getTanggalLahir()));
                params.put("golongan", guru.getGolongan());
                params.put("id_pelajaran", guru.getIdPelajaran());
                params.put("id_gaji", guru.getIdGaji());

                return params;
            }
        };

        executeRequestQueue(stringRequest);
    }

    public void putFull(final Guru guru) {
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, GuruEndpoint.putFull(guru.getNip()), new Response.Listener<String>() {
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
                params.put("first_name", guru.getFirstName());
                params.put("last_name", guru.getLastName());
                params.put("email", guru.getEmail());
                params.put("no_hp", guru.getNoHp());
                params.put("jekel", guru.getJekel());
                params.put("tanggal_lahir", String.valueOf(guru.getTanggalLahir()));
                params.put("provinsi_lahir", guru.getProvinsiLahir());
                params.put("kota_lahir", guru.getKotaLahir());
                params.put("golongan", guru.getGolongan());
                params.put("id_pelajaran", guru.getIdPelajaran());
                params.put("username", guru.getUsername());
                params.put("id_gaji", guru.getIdGaji());
                return params;
            }
        };

        executeRequestQueue(stringRequest);
    }

    public void delete(String idKelas) {
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, GuruEndpoint.delete(idKelas), new Response.Listener<String>() {
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
        void guruResponse(Guru guru);

        /***
         * need getAll method
         */
        void guruListResponse(ArrayList<Guru> guruList);

        void defaultResponse(String response);

        void errorResponse(VolleyError error);
    }

    private void executeRequestQueue(StringRequest stringRequest) {
        // if low connection
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        this.requestQueue.add(stringRequest);
    }
}
