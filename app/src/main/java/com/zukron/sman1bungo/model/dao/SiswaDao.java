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
import com.zukron.sman1bungo.model.Siswa;
import com.zukron.sman1bungo.util.api.SiswaEndpoint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SiswaDao {
    private Context context;
    private SiswaDao.onListener onListener;
    private boolean isRequestFinished = false;
    private RequestQueue requestQueue;

    public SiswaDao(Context context, SiswaDao.onListener onListener) {
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

    public void getNisn(String nisn) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, SiswaEndpoint.getNisn(nisn), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                isRequestFinished = true;
                try {
                    JSONObject siswaJson = new JSONObject(response);
                    Siswa siswa = new Siswa(
                            siswaJson.getString("nisn"),
                            siswaJson.getString("first_name"),
                            siswaJson.getString("last_name"),
                            siswaJson.getString("jekel"),
                            siswaJson.getString("email"),
                            siswaJson.getString("no_hp"),
                            LocalDate.parse(siswaJson.getString("tanggal_lahir")),
                            siswaJson.getString("kota_lahir"),
                            siswaJson.getString("provinsi_lahir"),
                            siswaJson.getString("id_kelas"),
                            siswaJson.getString("username")
                    );

                    onListener.siswaResponse(siswa);
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
        StringRequest stringRequest = new StringRequest(Request.Method.GET, SiswaEndpoint.getUsername(username), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                isRequestFinished = true;
                try {
                    JSONObject siswaJson = new JSONObject(response);
                    Siswa siswa = new Siswa(
                            siswaJson.getString("nisn"),
                            siswaJson.getString("first_name"),
                            siswaJson.getString("last_name"),
                            siswaJson.getString("jekel"),
                            siswaJson.getString("email"),
                            siswaJson.getString("no_hp"),
                            LocalDate.parse(siswaJson.getString("tanggal_lahir")),
                            siswaJson.getString("kota_lahir"),
                            siswaJson.getString("provinsi_lahir"),
                            siswaJson.getString("id_kelas"),
                            siswaJson.getString("username")
                    );

                    onListener.siswaResponse(siswa);
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
        StringRequest stringRequest = new StringRequest(Request.Method.GET, SiswaEndpoint.getNisn("ALL"), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                isRequestFinished = true;
                ArrayList<Siswa> siswaList = new ArrayList<>();
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject siswaJson = array.getJSONObject(i);
                        siswaList.add(new Siswa(
                                siswaJson.getString("nisn"),
                                siswaJson.getString("first_name"),
                                siswaJson.getString("last_name"),
                                siswaJson.getString("jekel"),
                                siswaJson.getString("email"),
                                siswaJson.getString("no_hp"),
                                LocalDate.parse(siswaJson.getString("tanggal_lahir")),
                                siswaJson.getString("kota_lahir"),
                                siswaJson.getString("provinsi_lahir"),
                                siswaJson.getString("id_kelas"),
                                siswaJson.getString("username")
                        ));
                    }

                    onListener.siswaListResponse(siswaList);
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

    public void postPartial(final Siswa siswa) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SiswaEndpoint.postPartial(), new Response.Listener<String>() {
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
                params.put("nisn", siswa.getNisn());
                params.put("first_name", siswa.getFirstName());
                params.put("last_name", siswa.getLastName());
                params.put("jekel", siswa.getJekel());
                params.put("tanggal_lahir", String.valueOf(siswa.getTanggalLahir()));
                params.put("id_kelas", siswa.getIdKelas());

                return params;
            }
        };

        executeRequestQueue(stringRequest);
    }

    public void postFull(final Siswa siswa) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SiswaEndpoint.postFull(), new Response.Listener<String>() {
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
                params.put("nisn", siswa.getNisn());
                params.put("first_name", siswa.getFirstName());
                params.put("last_name", siswa.getLastName());
                params.put("jekel", siswa.getJekel());
                params.put("email", siswa.getEmail());
                params.put("no_hp", siswa.getNoHp());
                params.put("tanggal_lahir", String.valueOf(siswa.getTanggalLahir()));
                params.put("kota_lahir", siswa.getKotaLahir());
                params.put("provinsi_lahir", siswa.getProvinsiLahir());
                params.put("id_kelas", siswa.getIdKelas());
                params.put("username", siswa.getUsername());

                return params;
            }
        };

        executeRequestQueue(stringRequest);
    }

    public void putPartial(final Siswa siswa) {
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, SiswaEndpoint.putPartial(siswa.getNisn()), new Response.Listener<String>() {
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
                params.put("nisn", siswa.getNisn());
                params.put("first_name", siswa.getFirstName());
                params.put("last_name", siswa.getLastName());
                params.put("jekel", siswa.getJekel());
                params.put("tanggal_lahir", String.valueOf(siswa.getTanggalLahir()));
                params.put("id_kelas", siswa.getIdKelas());

                return params;
            }
        };

        executeRequestQueue(stringRequest);
    }

    public void putFull(final Siswa siswa) {
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, SiswaEndpoint.putFull(siswa.getNisn()), new Response.Listener<String>() {
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

                params.put("first_name", siswa.getFirstName());
                params.put("last_name", siswa.getLastName());
                params.put("jekel", siswa.getJekel());
                params.put("email", siswa.getEmail());
                params.put("no_hp", siswa.getNoHp());
                params.put("tanggal_lahir", String.valueOf(siswa.getTanggalLahir()));
                params.put("kota_lahir", siswa.getKotaLahir());
                params.put("provinsi_lahir", siswa.getProvinsiLahir());
                params.put("id_kelas", siswa.getIdKelas());
                params.put("username", siswa.getUsername());

                return params;
            }
        };

        executeRequestQueue(stringRequest);
    }

    public void delete(String nisn) {
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, SiswaEndpoint.delete(nisn), new Response.Listener<String>() {
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
        void siswaResponse(Siswa siswa);

        /***
         * need getAll method
         */
        void siswaListResponse(ArrayList<Siswa> siswaList);

        void defaultResponse(String response);

        void errorResponse(VolleyError error);
    }

    private void executeRequestQueue(StringRequest stringRequest) {
        // if low connection
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        this.requestQueue.add(stringRequest);
    }
}
