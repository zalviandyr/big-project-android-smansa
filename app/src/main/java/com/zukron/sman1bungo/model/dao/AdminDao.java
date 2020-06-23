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
import com.zukron.sman1bungo.model.Admin;
import com.zukron.sman1bungo.util.api.AdminEndpoint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdminDao {
    private Context context;
    private AdminDao.onListener onListener;
    private boolean isRequestFinished = false;
    private RequestQueue requestQueue;

    public AdminDao(Context context, AdminDao.onListener onListener) {
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

    public void getId(String idAdmin) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AdminEndpoint.getId(idAdmin), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                isRequestFinished = true;
                try {
                    JSONObject adminJson = new JSONObject(response);
                    Admin admin = new Admin(
                            adminJson.getString("id_pegawai"),
                            adminJson.getString("first_name"),
                            adminJson.getString("last_name"),
                            LocalDate.parse(adminJson.getString("tanggal_lahir")),
                            adminJson.getString("no_hp"),
                            adminJson.getString("jekel"),
                            adminJson.getString("id_gaji"),
                            adminJson.getString("username")
                    );

                    onListener.adminResponse(admin);
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
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AdminEndpoint.getUsername(username), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                isRequestFinished = true;
                try {
                    JSONObject adminJson = new JSONObject(response);
                    Admin admin = new Admin(
                            adminJson.getString("id_pegawai"),
                            adminJson.getString("first_name"),
                            adminJson.getString("last_name"),
                            LocalDate.parse(adminJson.getString("tanggal_lahir")),
                            adminJson.getString("no_hp"),
                            adminJson.getString("jekel"),
                            adminJson.getString("id_gaji"),
                            adminJson.getString("username")
                    );

                    onListener.adminResponse(admin);
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
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AdminEndpoint.getId("ALL"), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                isRequestFinished = true;
                ArrayList<Admin> adminList = new ArrayList<>();
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject adminJson = array.getJSONObject(i);
                        adminList.add(new Admin(
                                adminJson.getString("id_pegawai"),
                                adminJson.getString("first_name"),
                                adminJson.getString("last_name"),
                                LocalDate.parse(adminJson.getString("tanggal_lahir")),
                                adminJson.getString("no_hp"),
                                adminJson.getString("jekel"),
                                adminJson.getString("id_gaji"),
                                adminJson.getString("username")
                        ));
                    }

                    onListener.adminListResponse(adminList);
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

    public void postPartial(final Admin pegawai) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AdminEndpoint.postPartial(), new Response.Listener<String>() {
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
                params.put("id_gaji", pegawai.getIdGaji());

                return params;
            }
        };

        executeRequestQueue(stringRequest);
    }

    public void postFull(final Admin pegawai) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AdminEndpoint.postFull(), new Response.Listener<String>() {
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
                params.put("id_gaji", pegawai.getIdGaji());
                params.put("username", pegawai.getUsername());

                return params;
            }
        };

        executeRequestQueue(stringRequest);
    }

    public void putPartial(final Admin pegawai) {
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, AdminEndpoint.putPartial(pegawai.getIdAdmin()), new Response.Listener<String>() {
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
                params.put("id_pegawai", pegawai.getIdAdmin());
                params.put("first_name", pegawai.getFirstName());
                params.put("last_name", pegawai.getLastName());
                params.put("tanggal_lahir", String.valueOf(pegawai.getTanggalLahir()));
                params.put("jekel", pegawai.getJekel());
                params.put("id_gaji", pegawai.getIdGaji());

                return params;
            }
        };

        executeRequestQueue(stringRequest);
    }

    public void putFull(final Admin pegawai) {
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, AdminEndpoint.putFull(pegawai.getIdAdmin()), new Response.Listener<String>() {
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
                params.put("id_pegawai", pegawai.getIdAdmin());
                params.put("first_name", pegawai.getFirstName());
                params.put("last_name", pegawai.getLastName());
                params.put("tanggal_lahir", String.valueOf(pegawai.getTanggalLahir()));
                params.put("no_hp", pegawai.getNoHp());
                params.put("jekel", pegawai.getJekel());
                params.put("id_gaji", pegawai.getIdGaji());
                params.put("username", pegawai.getUsername());

                return params;
            }
        };

        executeRequestQueue(stringRequest);
    }

    public void delete(String idAdmin) {
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, AdminEndpoint.delete(idAdmin), new Response.Listener<String>() {
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
        void adminResponse(Admin admin);

        /***
         * need getAll method
         */
        void adminListResponse(ArrayList<Admin> adminList);

        void defaultResponse(String response);

        void errorResponse(VolleyError error);
    }

    private void executeRequestQueue(StringRequest stringRequest) {
        // if low connection
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        this.requestQueue.add(stringRequest);
    }
}
