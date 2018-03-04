package lukaszs.idea_profit.com.wsg_zaliczenie;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    public static final String PREFERENCES_NAME = "loginPreferences";
    public static final String PREFERENCES_USERNAME = "prefLogin";
    public static final String PREFERENCES_USERPASS = "prefPass";
    public static final String PREFERENCES_USERID = "prefUID";

    private EditText etUserName;
    private EditText etUserPass;

    public static SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = getSharedPreferences(PREFERENCES_NAME, Activity.MODE_PRIVATE);
        etUserName = (EditText) findViewById(R.id.etUserName);
        etUserPass = (EditText) findViewById(R.id.etUserPass);
        loadLoginData();
    }

    //klik w przycisk logowania
    public void loginClick(View view){
        if(etUserName.getText().toString().trim().length()==0){
            etUserName.setError("Nazwa użytkownika nie może być pusta");
            etUserName.requestFocus();
        }
        if(etUserPass.getText().toString().trim().length()==0){
            etUserPass.setError("Hasło nie może być puste");
        }

        else{
            showToast("Trwa logowanie...");
            AsyncHttpClient client = new AsyncHttpClient();
            client.get("http://dev.imagit.pl/wsg_zaliczenie/api/login/"+etUserName.getText().toString()+"/"+etUserPass.getText(), new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                    String response_ = new String(response);
                    if(!android.text.TextUtils.isDigitsOnly(response_)){
                        showToast("Nazwa lub hasło niepoprawne");
                    }else{
                        saveLoginData();
                        Intent intent=new Intent(getApplicationContext(), HomePage.class);
                        intent.putExtra("userID", response_);
                        startActivity(intent);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                    // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                    showToast("Błąd w trakcie komunikacji z bazą danych");
                }
            });
        }
    }

    public void registerClick(View view){
        Intent intent=new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(intent);
    }

    //komunikat
    public void showToast(String text){
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    //zapamiętanie danych logowania
    private void saveLoginData() {
        SharedPreferences.Editor preferencesEditor = preferences.edit();
        String formUserName = etUserName.getText().toString();
        String formUserPass = etUserPass.getText().toString();
        preferencesEditor.putString(PREFERENCES_USERNAME, formUserName);
        preferencesEditor.putString(PREFERENCES_USERPASS, formUserPass);
        preferencesEditor.commit();
    }

    //załadowanie danych logowania
    private void loadLoginData() {
        String preferenceUserName = preferences.getString(PREFERENCES_USERNAME, "");
        String preferenceUserPass = preferences.getString(PREFERENCES_USERPASS, "");

        etUserName.setText(preferenceUserName);
        etUserPass.setText(preferenceUserPass);
    }
}
