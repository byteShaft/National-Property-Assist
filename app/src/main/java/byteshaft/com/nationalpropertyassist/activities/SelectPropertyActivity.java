package byteshaft.com.nationalpropertyassist.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import byteshaft.com.nationalpropertyassist.AppGlobals;
import byteshaft.com.nationalpropertyassist.R;
import byteshaft.com.nationalpropertyassist.database.AddPropertyDetailsDatabase;

public class SelectPropertyActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView mListView;
    private ArrayList<HashMap<Integer, String[]>> data;
    private ArrayList<Integer> ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_property_dialog);
        mListView = (ListView) findViewById(R.id.property_list);
        AddPropertyDetailsDatabase addPropertyDetailsDatabase = new
                AddPropertyDetailsDatabase(getApplicationContext());
        data = addPropertyDetailsDatabase.getAddressOfProperties();
        ids = addPropertyDetailsDatabase.getIdOfSaveProperty();
        ArrayAdapter<String> arrayAdapter = new Adapter(this,
                android.R.layout.simple_list_item_1, data, ids);
        mListView.setAdapter(arrayAdapter);
        mListView.setOnItemClickListener(this);
        AppGlobals.serverIdForProperty = 2112;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AppGlobals.serverIdForProperty = 2112;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        AppGlobals.serverIdForProperty = Integer.parseInt(data.get(i).get(ids.get(i))[1]);
        finish();
    }

    class Adapter extends ArrayAdapter<String> {

        private ArrayList<HashMap<Integer, String[]>> arrayList;
        private ArrayList<Integer> ids;

        public Adapter(Context context, int resource, ArrayList<HashMap<Integer, String[]>> arrayList,
                       ArrayList<Integer> ids) {
            super(context, resource);
            this.arrayList = arrayList;
            this.ids = ids;
        }



        @Override
        public int getCount() {
            return ids.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                LayoutInflater inflater = getLayoutInflater();
                convertView = inflater.inflate(R.layout.propery_select_delegate, parent, false);
                holder = new ViewHolder();
                holder.hiddenId = (TextView) convertView.findViewById(R.id.id_hidden);
                holder.address = (TextView) convertView.findViewById(R.id.address);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.address.setText(String.valueOf(ids.get(position)));
            holder.address.setText(arrayList.get(position).get(ids.get(position))[2]);
            return convertView;
        }
    }

    class ViewHolder {
        public TextView hiddenId;
        public TextView address;

    }
}
