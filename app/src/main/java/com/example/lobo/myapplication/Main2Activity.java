package com.example.lobo.myapplication;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.lobo.myapplication.entities.Something;
import com.example.lobo.myapplication.entities.SomethingAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {

    RecyclerView recyclerSomething;
    ArrayList<Something> listSome;

    ProgressDialog progress;

    JsonObjectRequest jsonObjectRequest;
    StringRequest stringRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        recyclerSomething= (RecyclerView) findViewById(R.id.rv);
        recyclerSomething.setLayoutManager(new LinearLayoutManager(this));
        recyclerSomething.setHasFixedSize(true);
        progress=new ProgressDialog(this);
        listSome = new ArrayList<>();
        webServiceLlenarLista();
    }


    private void webServiceLlenarLista(){progress=new ProgressDialog(this);
        progress.setMessage("Cargando...");
        progress.show();

        final String ip=getString(R.string.ip);
        String url=ip+"/something/select_all.php";

        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progress.hide();

                Something some = null;

                JSONArray json=response.optJSONArray("something");
                JSONObject jsonObject;

                try {

                    for (int i=0;i<json.length();i++){
                        some = new Something();
                        jsonObject = null;
                        jsonObject=json.getJSONObject(i);

                        some.setId(jsonObject.optInt("id"));
                        some.setDescription(jsonObject.optString("description"));
                        listSome.add(some);
                    }
                    SomethingAdapter adapter=new SomethingAdapter(listSome);
                    recyclerSomething.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getBaseContext(), "No se ha podido establecer conexión con el servidor" +
                            " "+response, Toast.LENGTH_LONG).show();
                    progress.hide();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(), "No se puede conectar "+error.toString(), Toast.LENGTH_LONG).show();
                System.out.println();
                progress.hide();
                Log.d("ERROR: ", error.toString());
            }
        });

        // request.add(jsonObjectRequest);
        VolleySingleton.getIntanciaVolley(this).addToRequestQueue(jsonObjectRequest);
    }
}
