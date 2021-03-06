package adompo.ayyash.behay;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import adompo.ayyash.behay.AktifitasFisikUser.AktifitasFisikUserAdapter;
import adompo.ayyash.behay.AktifitasFisikUser.AktifitasFisikUserModel;

import static adompo.ayyash.behay.AktifitasFisikUser.AktifitasFisikUserAdapter.AktifitasFisikUserDeleteClickHandler;


public class AktivitasFisik extends Fragment implements AktifitasFisikUserDeleteClickHandler {

    private FloatingActionButton fbAdd;
    private ProgressDialog progressDialog;
    private RecyclerView rv_item;
    private AktifitasFisikUserAdapter mAdapter;
    private String ambilEmail;

    public static AktivitasFisik newInstance() {
        AktivitasFisik fragment = new AktivitasFisik();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PrefManager prefManager = new PrefManager(getActivity());
        ambilEmail = prefManager.getActiveEmail();
    }

    @Override
    public void onResume() {
        super.onResume();

        getAktifitasIhsan(ConfigUmum.GET_AKTIVITAS_FISIK + ambilEmail);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_aktivitas_fisik, container, false);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Silahkan Tunggu...");

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mAdapter = new AktifitasFisikUserAdapter(getActivity(), this);
        rv_item = (RecyclerView) rootView.findViewById(R.id.rv_aktifitas_fisik);
        rv_item.setLayoutManager(llm);
        rv_item.setAdapter(mAdapter);

        fbAdd = (FloatingActionButton) rootView.findViewById(R.id.fb_add);
        fbAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ListActivity.class);
                startActivity(i);
            }
        });

        return rootView;
    }

    public void getAktifitasIhsan(String url) {
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                GsonBuilder builder = new GsonBuilder();
                Gson mGson = builder.create();
                System.out.println("aaaa : " + response);
                AktifitasFisikUserModel model = mGson.fromJson(response, AktifitasFisikUserModel.class);

                mAdapter.setData(model.listAktifitasFisik);
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        queue.add(stringRequest);
    }

    @Override
    public void onClick(final int aktifitasId, String aktifitasText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Konfirmasi");
        builder.setMessage("Apakah anda yakin ingin menghapus Aktifitas\n" + aktifitasText + " ?");
        builder.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                deleteData(ConfigUmum.URL_DELETE_ACTIVITY + aktifitasId);
            }
        });
        builder.setNegativeButton("Batalkan", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // do nothing
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void deleteData(String url) {
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                getAktifitasIhsan(ConfigUmum.GET_AKTIVITAS_FISIK + ambilEmail);
                Log.d("uye jadi hapus", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e("uye gagal hapus", error.toString());
            }
        });
        queue.add(stringRequest);
    }
}