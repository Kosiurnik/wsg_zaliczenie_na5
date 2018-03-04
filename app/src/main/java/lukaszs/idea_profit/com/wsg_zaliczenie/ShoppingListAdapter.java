package lukaszs.idea_profit.com.wsg_zaliczenie;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by LS on 03.03.2018.
 */

public class ShoppingListAdapter extends ArrayAdapter<ShoppingList> {

    private Activity activity;
    private ArrayList<ShoppingList> listShopping;
    private static LayoutInflater inflater = null;

    public ShoppingListAdapter(Activity activity, int textViewResourceId, ArrayList<ShoppingList> _listShopping) {
        super(activity, textViewResourceId, _listShopping);
        try{
            this.activity = activity;
            this.listShopping = _listShopping;
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }catch(Exception e){}
    }

    public static class ViewHolder {
        public TextView item_name;
        public TextView item_description;
        public Button btDelete;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final ViewHolder holder;
        final int pos = position;
        try{
            if (convertView == null) {
                view = inflater.inflate(R.layout.shopping_list_item, null);
                holder = new ViewHolder();
                holder.item_name = (TextView) view.findViewById(R.id.lbIName);
                holder.item_description = (TextView) view.findViewById(R.id.lbIDesc);
                holder.btDelete = (Button) view.findViewById(R.id.btDelete);
                holder.btDelete.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case DialogInterface.BUTTON_POSITIVE:
                                        AsyncHttpClient client = new AsyncHttpClient();
                                        client.get("http://dev.imagit.pl/wsg_zaliczenie/api/item/delete/"+listShopping.get(pos).getItemUser()+"/"+listShopping.get(pos).getItemID(), new AsyncHttpResponseHandler() {

                                            @Override
                                            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                                                String response_ = new String(response);
                                                if(response_.equals("OK")){
                                                    showToast("Usunięto wpis");
                                                    Intent intent = activity.getIntent();
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                                    activity.finish();
                                                    activity.startActivity(intent);
                                                }else{
                                                    showToast("Wpis nie istnieje, lub jest przypisany do innego użytkownika");
                                                }
                                            }

                                            @Override
                                            public void onFailure(int statusCode, Header[] headers, byte[] response, Throwable error) {showToast("Wystąpił błąd wprzy usuwaniu wpisu.");}
                                        });

                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        break;
                                }
                            }
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        builder.setMessage("Czy chcesz usunąć wpis "+listShopping.get(pos).getItemName()+"?").setPositiveButton("Tak", dialogClickListener)
                                .setNegativeButton("Nie", dialogClickListener).show();
                    }
                });
                view.setTag(holder);
            }else{
                holder = (ViewHolder) view.getTag();
            }
            holder.item_name.setText(listShopping.get(position).getItemName());
            holder.item_description.setText(listShopping.get(position).getItemDescription());
        }catch(Exception e){}
        return view;
    }

    public void showToast(String text){
        Context context = activity.getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}
