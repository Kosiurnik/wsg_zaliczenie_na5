package lukaszs.idea_profit.com.wsg_zaliczenie;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class HomePage extends AppCompatActivity {

    private ListView listLista;
    private ArrayList<ShoppingList> shoppingList;
    private ArrayAdapter<ShoppingList> shoppingAdapter;
    private String userID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        listLista = (ListView) findViewById(R.id.listLista);
        listLista.setClickable(true);
        userID = MainActivity.preferences.getString(MainActivity.PREFERENCES_USERID, "");
        if(userID.equals("")){
            saveUserID();
            userID = MainActivity.preferences.getString(MainActivity.PREFERENCES_USERID, "");
        }
        //reloadList();

    }

    @Override
    public void onResume(){
        super.onResume();
        reloadList();
    }

    public void reloadList(){
        shoppingList = new ArrayList<ShoppingList>();
        shoppingAdapter = new ShoppingListAdapter(this,R.layout.shopping_list_item,shoppingList);
        getShopping();
    }

    private void saveUserID(){
        Bundle extras = getIntent().getExtras();
        String userID = extras.getString("userID");
        SharedPreferences.Editor preferencesEditor = MainActivity.preferences.edit();
        preferencesEditor.putString(MainActivity.PREFERENCES_USERID, userID);
        preferencesEditor.commit();
    }

    private void getShopping(){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://dev.imagit.pl/wsg_zaliczenie/api/items/" + userID, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String JSON = new String(responseBody);
                JSONArray jArray = null;
                try{
                    jArray = new JSONArray(JSON);
                    for(int i=0;i<jArray.length();i++){
                        JSONObject jObject = jArray.getJSONObject(i);
                        int itemID = jObject.getInt("ITEM_ID");
                        String itemName = jObject.getString("ITEM_NAME");
                        String itemDescription = jObject.getString("ITEM_DESCRIPTION");
                        int itemUserID = jObject.getInt("ITEM_USER");
                        ShoppingList entry = new ShoppingList(itemID,itemName,itemDescription,itemUserID);
                        shoppingList.add(entry);
                    }
                    listLista.setAdapter(shoppingAdapter);
                }catch(JSONException e){
                    showToast("Wystąpił błąd w trakcie ładowania listy");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                showToast("Wystąpił błąd w trakcie ładowania listy");
            }
        });
    }

    public void btShoppingAdd_Click(View view){
        Intent intent=new Intent(getApplicationContext(), ShoppingFormAdd.class);
        intent.putExtra("userID", userID);
        startActivity(intent);
    }

    public void showToast(String text){
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}
