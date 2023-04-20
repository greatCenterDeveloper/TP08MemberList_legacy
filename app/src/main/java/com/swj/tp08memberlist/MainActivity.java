package com.swj.tp08memberlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText diaEtName;
    RadioGroup diaRgGender;
    RadioButton genderRb;
    Spinner diaSpNation;
    String spSelectedNation = "";
    int spSelectedNationArrNumber = 0;
    Button diaBtnMemberAdd, diaBtnCancel;
    MyAdapter adapter;
    ArrayAdapter arrayAdapter;

    TextView tvNoData;
    ListView listView;

    SearchView searchView;

    ArrayList<Item> itemArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvNoData = findViewById(R.id.tv_no_data);
        listView = findViewById(R.id.listview);
        registerForContextMenu(listView);
    }

    // onCreate 메소드가 실행된 후 자동으로 Option Menu를 만드는 작업을 하는
    // 이 콜백 메소드가 자동 호출
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option, menu);

        MenuItem menuItem = menu.findItem(R.id.menu_search);
        searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("이름을 입력하세요.");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ArrayList<Item> searchResult = new ArrayList<>();
                for(int i=0; i<itemArrayList.size(); i++) {
                    Item item = itemArrayList.get(i);
                    String name = item.name;
                    if(name.contains(query))
                        searchResult.add(item);
                }
                adapter = new MyAdapter(MainActivity.this, searchResult);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                adapter = new MyAdapter(MainActivity.this, itemArrayList);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                //searchView.onActionViewCollapsed();
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }


    // 옵션 메뉴의 특정 아이템을 클릭했을 때 호출
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menu_member_add) { // 멤버 추가 다이얼로그
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(R.layout.dialog);
            AlertDialog dialog = builder.create();
            dialog.show();

            diaEtName = dialog.findViewById(R.id.dia_et_name);
            diaRgGender = dialog.findViewById(R.id.dia_rg_gender);
            diaSpNation = dialog.findViewById(R.id.dia_sp_nation);
            diaBtnMemberAdd = dialog.findViewById(R.id.dia_btn_member_add);
            diaBtnCancel = dialog.findViewById(R.id.dia_btn_cancel);

            arrayAdapter = ArrayAdapter.createFromResource
                    (this, R.array.nation, R.layout.spinner_item);
            diaSpNation.setAdapter(arrayAdapter);
            diaSpNation.setDropDownVerticalOffset(110);

            diaSpNation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    String[] nationArr = getResources().getStringArray(R.array.nation);
                    spSelectedNation = nationArr[i];
                    spSelectedNationArrNumber = i;
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {}
            });

            diaBtnMemberAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tvNoData.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);

                    int genderId = diaRgGender.getCheckedRadioButtonId();
                    genderRb = dialog.findViewById(genderId);

                    itemArrayList.add(0,
                            new Item(
                                    diaEtName.getText().toString(),
                                    spSelectedNation,
                                    genderRb.getText().toString(),
                                    R.drawable.flag_australia + spSelectedNationArrNumber));
                    adapter = new MyAdapter(MainActivity.this, itemArrayList);
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    listView.setSelection(0);
                    dialog.dismiss();
                }
            });

            diaBtnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
        } // if(item.getItemId() == R.id.menu_member_add)
        return super.onOptionsItemSelected(item);
    } // onOptionsItemSelected 메소드

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.context, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;

        if(item.getItemId() == R.id.context_menu_delete) {
            itemArrayList.remove(index);
            adapter.notifyDataSetChanged();
        } else if(item.getItemId() == R.id.context_menu_modify)
            Toast.makeText(this, "modify : " + index, Toast.LENGTH_SHORT).show();
        else if(item.getItemId() == R.id.context_menu_info)
            Toast.makeText(this, "info : " + index, Toast.LENGTH_SHORT).show();
        return super.onContextItemSelected(item);
    }
}