package com.example.crudmysql;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static android.content.ContentValues.TAG;
public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    public static final String URLSELECT = "http://192.168.0.3/CRUDMysql/select.php";
    public static final String URLDELETE = "http://192.168.0.3/CRUDMysql/delete.php";
    public static final String URLEDIT = "http://192.168.0.3/CRUDMysql/edit.php";
    public static final String URLINSERT = "http://192.168.0.3/CRUDMysql/insert.php";
    ListView list;
    SwipeRefreshLayout swipe;
    List<Data> itemList = new ArrayList<Data>();
    MhsAdapter adapter;
    LayoutInflater inflater;
    EditText tid,tname,thobby,tpassword;
    String vid, vname, vhobby, vpassword;
    FloatingActionButton fab;
    private Button btnlogout;
    private PreferenceHelper preferenceHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnlogout = (Button) findViewById(R.id.btn);
        preferenceHelper = new PreferenceHelper(this);
        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferenceHelper.putIsLogin(false);
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                MainActivity.this.finish();
            }
        });

        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe);
        list = (ListView) findViewById(R.id.list);

        adapter = new MhsAdapter(MainActivity.this, itemList);
        list.setAdapter(adapter);

        swipe.setOnRefreshListener(this);

        swipe.post(new Runnable() {
                       @Override
                       public void run() {
                           swipe.setRefreshing(true);
                           itemList.clear();
                           adapter.notifyDataSetChanged();
                           callVolley();
                       }
                   }
        );
        fab = findViewById(R.id.fab);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialogForm("","","","","Create");
            }
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final String idx = itemList.get(position).getId();
                final CharSequence[] pilihanAksi = {"Delete", "Edit"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setItems(pilihanAksi, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:

                                Delete(idx);
                                break;

                            case 1:

                                EditData(idx);
                                break;
                        }

                    }
                }).show();
                return false;
            }
        });


    }
    public void EditData(String id){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLEDIT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jObj = new JSONObject(response);

                            String idx = jObj.getString("id");
                            String namex = jObj.getString("name");
                            String hobbyx = jObj.getString("hobby");
                            String passwordx = jObj.getString("password");

                            dialogForm(idx, namex, hobbyx, passwordx, "UPDATE");

                            adapter.notifyDataSetChanged();

                        }catch (JSONException e) {
                            // JSON error
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Failed to connect to the server, check your connection settings", Toast.LENGTH_LONG).show();
            }
        })
        {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();


                params.put("id", id);

                return params;
            }

        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(stringRequest);
    }
    public void dialogForm(String id, String name, String hobby, String password, String button){
        AlertDialog.Builder dialogForm = new AlertDialog.Builder(MainActivity.this);
        inflater = getLayoutInflater();
        View viewDialog = inflater.inflate(R.layout.form_mahasiswa, null);
        dialogForm.setView(viewDialog);
        dialogForm.setCancelable(true);
        dialogForm.setTitle("Form Mahasiswa");

        tid = (EditText) viewDialog.findViewById(R.id.inId);
        tname = (EditText) viewDialog.findViewById(R.id.inName);
        thobby = (EditText) viewDialog.findViewById(R.id.inHobby);
        tpassword = (EditText) viewDialog.findViewById(R.id.inPassword);

        if (id.isEmpty()){
            tid.setText(null);
            tname.setText(null);
            thobby.setText(null);
            tpassword.setText(null);
        }else{
            tid.setText(id);
            tname.setText(name);
            thobby.setText(hobby);
            tpassword.setText(password);
        }

        dialogForm.setPositiveButton(button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                vid = tid.getText().toString();
                vname = tname.getText().toString();
                vhobby = thobby.getText().toString();
                vpassword = tpassword.getText().toString();

                save();
                dialog.dismiss();
            }
        });
        dialogForm.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                tid.setText(null);
                tname.setText(null);
                thobby.setText(null);
                tpassword.setText(null);
            }
        });
        dialogForm.show();

    }
    public void save(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLINSERT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        callVolley();
                        Toast.makeText(MainActivity.this, response, Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "failed to connect to the server, check your connection settings", Toast.LENGTH_LONG).show();
            }
        })
        {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();

                if (vid.isEmpty()) {
                    params.put("name", vname);
                    params.put("hobby", vhobby);
                    params.put("password", vpassword);
                    return params;
                }else{
                    params.put("id", vid);
                    params.put("name", vname);
                    params.put("hobby", vhobby);
                    params.put("password", vpassword);
                    return params;
                }
            }

        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(stringRequest);

    }
    public void Delete(String id){


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLDELETE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(MainActivity.this, response, Toast.LENGTH_LONG).show();
                        callVolley();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "failed to connect to the server, check your connection settings", Toast.LENGTH_LONG).show();
            }
        })
        {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();


                params.put("id", id);

                return params;
            }

        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(stringRequest);
    }
    @Override
    public void onRefresh() {
        itemList.clear();
        adapter.notifyDataSetChanged();
        callVolley();

    }
    private void callVolley() {
        itemList.clear();
        adapter.notifyDataSetChanged();
        swipe.setRefreshing(true);


        JsonArrayRequest jArr = new JsonArrayRequest(URLSELECT, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                // Parsing json
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);

                        Data item = new Data();

                        item.setId(obj.getString("id"));
                        item.setName(obj.getString("name"));
                        item.setHobby(obj.getString("hobby"));
                        item.setPassword(obj.getString("password"));


                        itemList.add(item);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


                adapter.notifyDataSetChanged();

                swipe.setRefreshing(false);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                swipe.setRefreshing(false);
            }
        });


        RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        mRequestQueue.add(jArr);

    }


}