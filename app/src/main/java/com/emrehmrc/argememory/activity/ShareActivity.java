package com.emrehmrc.argememory.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.emrehmrc.argememory.R;
import com.emrehmrc.argememory.adapter.DepartmentSpinnerAdapter;
import com.emrehmrc.argememory.connection.ConnectionClass;
import com.emrehmrc.argememory.helper.Utils;
import com.emrehmrc.argememory.model.DepartmentModel;
import com.emrehmrc.argememory.popup.DepartmentPopup;

import java.util.ArrayList;

public class ShareActivity extends AppCompatActivity {

    public static final int DEPARTMENT = 1;  // The request code
    ActionBar actionBar;
    Button btnSave, btnCancel;
    EditText edtTask;
    ConnectionClass connectionClass;
    String companiesid;
    ArrayList<DepartmentModel> depList;
    TextView txtDep, txtPers, txtTag;
    Spinner spnDep, spnPers, spnTag;
    private SharedPreferences loginPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        init();
        setListeners();
    }

    private void setListeners() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        txtDep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                departmentPopup();
            }
        });
    }

    private void init() {
        // Getting actionbar
        actionBar = getSupportActionBar();
        // Setting up logo over actionbar
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setLogo(R.drawable.logo_ico);
        // setting actionbar title
        actionBar.setTitle(R.string.share);
        // setting actionbar subtitle
        // actionBar.setSubtitle("Giriş");
        loginPreferences = getSharedPreferences(Utils.LOGIN, MODE_PRIVATE);
        companiesid = loginPreferences.getString(Utils.COMPANIESID, "");
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        edtTask = findViewById(R.id.edtTask);
        txtDep = findViewById(R.id.txtDepartment);
        txtPers = findViewById(R.id.txtPersonel);
        txtTag = findViewById(R.id.txtTag);
        spnDep = findViewById(R.id.spnDep);
        spnPers = findViewById(R.id.spnPers);
        spnTag = findViewById(R.id.spnTag);
        connectionClass = new ConnectionClass();
        depList = new ArrayList<>();
    }

    private void departmentPopup() {
        Intent i = new Intent(getApplicationContext(), DepartmentPopup.class);
        startActivityForResult(i, DEPARTMENT);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        // Finding search menu item
        final MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        // Setting hint over search menu
        searchView.setQueryHint("Arama yap....");
        // Calling query listener on search menu
        SearchView.OnQueryTextListener textListener = new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                // Showing text that is entered in search menu
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return true;

            }
        };

        // Implementing query listener
        searchView.setOnQueryTextListener(textListener);
        return true;
    }

    // Implementing click listeners over all menu icons and displaying there
    // texts
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search://do something
                break;


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DEPARTMENT) {

            try {
                depList = (ArrayList<DepartmentModel>) data.getSerializableExtra("deplist");
            }
           catch (Exception ex){
                depList.add(new DepartmentModel(false,"Seçilmedi","-1"));
           }
            DepartmentSpinnerAdapter customAdapter = new DepartmentSpinnerAdapter(depList, getApplicationContext());
            spnDep.setAdapter(customAdapter);


        }

    }
}
