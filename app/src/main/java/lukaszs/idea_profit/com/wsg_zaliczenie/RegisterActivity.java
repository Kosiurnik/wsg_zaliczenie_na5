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

public class RegisterActivity extends AppCompatActivity {

    private EditText etUserName;
    private EditText etUserPassword;
    private EditText etUserRepeatPassword;
    private EditText etUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUserName = (EditText) findViewById(R.id.etUserName);
        etUserPassword = (EditText) findViewById(R.id.etUserPassword);
        etUserRepeatPassword = (EditText) findViewById(R.id.etUserRepeatPassword);
        etUserEmail = (EditText) findViewById(R.id.etUserEmail);
    }

    public void doRegister(View view) {
        if (checkForm()) {
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.put("login", etUserName.getText().toString());
            params.put("pass", etUserPassword.getText().toString());
            params.put("email", etUserEmail.getText().toString());

            client.post("http://dev.imagit.pl/wsg_zaliczenie/api/register", params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String response = new String(responseBody);
                    Log.d("error",response);
                    if(response.equals("OK")){
                        showToast("Konto zarejestrowane!");
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    String response = new String(responseBody);
                    showToast("Błąd rejestracji: "+response);
                    Log.d("error",response);
                }
            });
        }
    }

    private boolean checkForm(){
        if(etUserName.getText().toString().trim().length()==0){
            etUserName.setError("Nazwa użytkownika nie może być pusta");
            etUserName.requestFocus();
            return false;
        }
        if(etUserPassword.getText().toString().trim().length()==0){
            etUserPassword.setError("Hasło nie może być puste");
            etUserPassword.requestFocus();
            return false;
        }
        if(etUserEmail.getText().toString().trim().length()==0){
            etUserEmail.setError("E-Mail nie może być pusty");
            etUserEmail.requestFocus();
            return false;
        }
        if(etUserRepeatPassword.getText().toString().trim().length()==0){
            etUserRepeatPassword.setError("Musisz powtórzyć hasło");
            etUserRepeatPassword.requestFocus();
            return false;
        }
        if(!etUserRepeatPassword.getText().toString().equals(etUserPassword.getText().toString())){
            etUserRepeatPassword.setError("Powtórzone hasło się nie zgadza");
            etUserRepeatPassword.requestFocus();
            return false;
        }
        return true;
    }

    public void clickCancel(View view){
        etUserName.setText("");
        etUserPassword.setText("");
        etUserRepeatPassword.setText("");
        this.finish();
    }

    //komunikat
    public void showToast(String text){
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }


}
