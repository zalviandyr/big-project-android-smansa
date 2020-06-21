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
import com.zukron.sman1bungo.model.Pegawai;
import com.zukron.sman1bungo.model.User;
import com.zukron.sman1bungo.util.api.LoginRegisterEndpoint;

import org.json.JSONException;
import org.json.JSONObject;
import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoginRegisterDao {
    private Context context;
    private LoginRegisterDao.onListener onListener;
    private boolean isRequestFinished = false;
    private RequestQueue requestQueue;

    public LoginRegisterDao(Context context, LoginRegisterDao.onListener onListener) {
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

    public void get(String username, String password) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, LoginRegisterEndpoint.get(username, password), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                isRequestFinished = true;
                try {
                    JSONObject userJson = new JSONObject(response);
                    User user = new User(
                            userJson.getString("username"),
                            userJson.getString("password"),
                            userJson.getString("level"),
                            LocalDate.parse(userJson.getString("last_login")),
                            LocalDate.parse(userJson.getString("created"))
                    );

                    onListener.userResponse(user);
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

    public void postGuru(final String username, final String password, final String nip) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, LoginRegisterEndpoint.post("GURU"), new Response.Listener<String>() {
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

                params.put("username", username);
                params.put("password", password);
                params.put("level", "Guru");
                params.put("nip", nip);

                return params;
            }
        };

        executeRequestQueue(stringRequest);
    }

    public void postPegawai(final String username, final String password, final String idPegawai) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, LoginRegisterEndpoint.post("PEGAWAI"), new Response.Listener<String>() {
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

                params.put("username", username);
                params.put("password", password);
                params.put("level", "Pegawai");
                params.put("id_pegawai", idPegawai);

                return params;
            }
        };

        executeRequestQueue(stringRequest);
    }

    public void postSiswa(final String username, final String password, final String nisn) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, LoginRegisterEndpoint.post("SISWA"), new Response.Listener<String>() {
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

                params.put("username", username);
                params.put("password", password);
                params.put("level", "Siswa");
                params.put("nisn", nisn);

                return params;
            }
        };

        executeRequestQueue(stringRequest);
    }

    public interface onListener {
        /***
         * need get method
         */
        void userResponse(User user);

        void defaultResponse(String response);

        void errorResponse(VolleyError error);
    }

    private void executeRequestQueue(StringRequest stringRequest) {
        // if low connection
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        this.requestQueue.add(stringRequest);
    }
}
