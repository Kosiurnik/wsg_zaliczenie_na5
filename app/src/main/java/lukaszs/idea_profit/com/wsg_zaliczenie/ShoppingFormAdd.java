package lukaszs.idea_profit.com.wsg_zaliczenie;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

public class ShoppingFormAdd extends AppCompatActivity {

    EditText etItemName;
    EditText etItemDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_form_add);

        etItemName = (EditText) findViewById(R.id.etItemName);
        etItemDescription = (EditText) findViewById(R.id.etItemDescription);
    }

    private boolean checkForm(){
        if(etItemName.getText().toString().trim().length()==0){
            etItemName.setError("Nazwa nie może być pusta");
            etItemName.requestFocus();
            return false;
        }
        if(etItemDescription.getText().toString().trim().length()==0){
            etItemDescription.setError("Opis nie może być pusty");
            etItemDescription.requestFocus();
            return false;
        }
        return true;
    }

    public void btFormAddAccept_Click(View view){
        if(checkForm()){
            String userID = MainActivity.preferences.getString(MainActivity.PREFERENCES_USERID, "");
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.put("user", userID);
            params.put("name", etItemName.getText().toString());
            params.put("desc", etItemDescription.getText().toString());

            client.post("http://dev.imagit.pl/wsg_zaliczenie/api/item/add", params, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String response = new String(responseBody);
                    Log.d("error",response);
                    if(response.equals("OK")){
                        showToast("Dodano pozycję");
                        ShoppingFormAdd.this.finish();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            });

        }
    }

    public void btFormAddCancel_Click(View view){
        etItemName.setText("");
        etItemDescription.setText("");
        this.finish();
    }

    public void showToast(String text){
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}
