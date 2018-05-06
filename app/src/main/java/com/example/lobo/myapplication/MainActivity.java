package com.example.lobo.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.lobo.myapplication.entities.Something;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener{

    EditText edt, edt1, edt2;
    TextView txt;
    Button btn_agregar, btn_verLista, btn_consultar, btn_borrar, btn_actualizar ;
    RadioGroup rg;
    ProgressDialog progress;
    Intent intent;
    StringRequest stringRequest;
    JsonObjectRequest jsonObjectRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edt = (EditText) findViewById(R.id.text_add);
        edt1 = (EditText) findViewById(R.id.text_consult);
        edt2 = (EditText) findViewById(R.id.text_des_consulted);

        txt = (TextView) findViewById(R.id.textView);

        rg = (RadioGroup) findViewById(R.id.radioGroup);

        btn_agregar = (Button) findViewById(R.id.btn_add);
        btn_consultar = (Button) findViewById(R.id.btn_select1);
        btn_verLista = (Button) findViewById(R.id.btn_select_all);
        btn_actualizar = (Button) findViewById(R.id.btn_update);
        btn_borrar = (Button) findViewById(R.id.btn_delete);

        btn_borrar.setOnClickListener(this);
        btn_actualizar.setOnClickListener(this);
        btn_agregar.setOnClickListener(this);
        btn_consultar.setOnClickListener(this);
        btn_verLista.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_add:
                webServiceInsert();
                break;

            case R.id.btn_delete:
                webServiceDelete();
                break;

            case R.id.btn_update:
                webServiceUpdate();
                break;

            case R.id.btn_select1:
                if(!edt1.getText().toString().equals(""))
                    webServiceSelect();
                break;
            case R.id.btn_select_all:
                intent = new Intent(getApplicationContext(), Main2Activity.class);
                startActivity(intent);
                break;

            default:
                break;
        }
    }

    private void webServiceInsert() {
        progress = new ProgressDialog(this);
        progress.setMessage("Cargando....");
        progress.show();

        String ip = getString(R.string.ip);
        String url=ip+"/something/insert.php?";
        stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        progress.hide();

                        if (response.trim().equalsIgnoreCase("Registrado")){
                            edt.setText("");
                            Toast.makeText(getBaseContext(),"Se ha registrado con exito",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getBaseContext(),"No se ha registrado ",Toast.LENGTH_SHORT).show();
                            Log.i("RESPUESTA: ",""+response);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(),"No se ha podido conectar",Toast.LENGTH_SHORT).show();
                progress.hide();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                String description = edt.getText().toString();

                Map<String, String> parametros = new HashMap<>();
                parametros.put("description", description);

                return parametros;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getIntanciaVolley(getBaseContext()).addToRequestQueue(stringRequest);
    }

    private void webServiceSelect() {
        progress=new ProgressDialog(this);
        progress.setMessage("Cargando...");
        progress.show();

        final String ip=getString(R.string.ip);
        String url=ip+"/something/select_by_id.php?id="+edt1.getText().toString();

        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progress.hide();

                Something some=new Something();

                JSONArray json=response.optJSONArray("something");
                JSONObject jsonObject=null;

                try {
                    jsonObject=json.getJSONObject(0);
                    some.setId(Integer.valueOf(jsonObject.optString("id")));
                    some.setDescription(jsonObject.optString("description"));
                    if(!some.getDescription().equals("no registrado") && some.getId()!=0) {
                        txt.setText(String.valueOf(some.getId()));
                        edt2.setText(some.getDescription());
                        txt.setVisibility(View.VISIBLE);
                        edt2.setVisibility(View.VISIBLE);
                        rg.setVisibility(View.VISIBLE);
                        edt1.setText("");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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

    private void webServiceUpdate() {
        progress.setMessage("Cargando...");
        progress.show();

        String ip=getString(R.string.ip);

        String url=ip+"/something/update.php?";

        stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.hide();

                if (response.trim().equalsIgnoreCase("actualiza")){
                    edt2.setText("");
                    txt.setText("");
                    edt2.setVisibility(View.INVISIBLE);
                    txt.setVisibility(View.INVISIBLE);
                    rg.setVisibility(View.INVISIBLE);
                    Toast.makeText(getBaseContext(),"Se ha Actualizado con exito",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getBaseContext(),"No se ha Actualizado ",Toast.LENGTH_SHORT).show();
                    Log.i("RESPUESTA: ",""+response);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(),"No se ha podido conectar",Toast.LENGTH_SHORT).show();
                progress.hide();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String id = txt.getText().toString();
                String description=edt2.getText().toString();

                Map<String,String> parametros=new HashMap<>();
                parametros.put("id",id);
                parametros.put("description",description);

                return parametros;
            }
        };
        //request.add(stringRequest);
        VolleySingleton.getIntanciaVolley(this).addToRequestQueue(stringRequest);
    }

    private void webServiceDelete() {
        progress.setMessage("Cargando...");
        progress.show();

        String ip=getString(R.string.ip);

        String url=ip+"/something/delete.php?id="+txt.getText().toString();

        stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.hide();

                if (response.trim().equalsIgnoreCase("elimina")){
                    txt.setText("");
                    edt2.setText("");
                    txt.setVisibility(View.INVISIBLE);
                    edt2.setVisibility(View.INVISIBLE);
                    rg.setVisibility(View.INVISIBLE);
                    Toast.makeText(getBaseContext(),"Se ha Eliminado con exito",Toast.LENGTH_SHORT).show();
                }else{
                    if (response.trim().equalsIgnoreCase("noExiste")){
                        Toast.makeText(getBaseContext(),"No se encuentra la persona ",Toast.LENGTH_SHORT).show();
                        Log.i("RESPUESTA: ",""+response);
                    }else{
                        Toast.makeText(getBaseContext(),"No se ha Eliminado ",Toast.LENGTH_SHORT).show();
                        Log.i("RESPUESTA: ",""+response);
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(),"No se ha podido conectar",Toast.LENGTH_SHORT).show();
                progress.hide();
            }
        });
        //request.add(stringRequest);
        VolleySingleton.getIntanciaVolley(this).addToRequestQueue(stringRequest);
    }
}
