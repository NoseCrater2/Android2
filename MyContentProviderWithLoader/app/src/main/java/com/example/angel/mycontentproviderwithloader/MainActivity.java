package com.example.angel.mycontentproviderwithloader;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.Manifest;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

    private static final String TAG = "ContentProviderDemo";

   // private int MY_PERMISSIONS_REQUEST_READ_CONTACTS=20;

    private boolean firstTimeLoaded=false;
    private EditText editTextContactName;

    private TextView textViewQueryResult;
    private Button buttonLoadData, buttonAddContact,buttonRemoveContact,buttonUpdateContact;
    private ContentResolver contentResolver;
    private CursorLoader mContactsLoader;

    private String[] mColumnProjection = new String[]{
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
            ContactsContract.Contacts.CONTACT_STATUS,
            ContactsContract.Contacts.HAS_PHONE_NUMBER};

    private String mSelectionCluse = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " = ?";

    private String[] mSelectionArguments = new String[]{"Ajay"};

    private String mOrderBy = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.

    private GoogleApiClient client;*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewQueryResult = (TextView) findViewById(R.id.textViewQueryResult);
        editTextContactName=(EditText)findViewById(R.id.editTextContactName);

        buttonLoadData = (Button) findViewById(R.id.buttonLoadData);

        buttonAddContact=(Button)findViewById(R.id.buttonAddContact);
        buttonRemoveContact=(Button)findViewById(R.id.buttonRemoveContact);
        buttonUpdateContact=(Button)findViewById(R.id.buttonUpdateContact);

        buttonLoadData.setOnClickListener(this);

        buttonAddContact.setOnClickListener(this);
        buttonRemoveContact.setOnClickListener(this);
        buttonUpdateContact.setOnClickListener(this);

        ContentResolver contentResolver=getContentResolver();
        /*Cursor cursor=contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                mColumnProjection,
                null,
                null,
                null);

        if(cursor!=null && cursor.getCount()>0){
            StringBuilder stringBuilderQueryResult=new StringBuilder("");
            while (cursor.moveToNext()){
                stringBuilderQueryResult.append(cursor.getString(0)+" , "+cursor.getString(1)+" , "+cursor.getString(2)+"\n");
            }
            textViewQueryResult.setText(stringBuilderQueryResult.toString());
        }else{
            textViewQueryResult.setText("No Contacts in device");
        }*/
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        if (id == 1) {
            return new CursorLoader(MainActivity.this, ContactsContract.Contacts.CONTENT_URI,
                    mColumnProjection, null, null, null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor != null && cursor.getCount() > 0) {
            StringBuilder stringBuilderQueryResult = new StringBuilder("");
            //cursor.moveToFirst();
            while (cursor.moveToNext()) {
                stringBuilderQueryResult.append(cursor.getString(0) + " , " + cursor.getString(1) + " , " + cursor.getString(2) + "\n");
            }
            textViewQueryResult.setText(stringBuilderQueryResult.toString());
        } else {
            textViewQueryResult.setText("No Contacts in device");
        }
        cursor.close();

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonLoadData:
                if(firstTimeLoaded==false){
                    getLoaderManager().initLoader(1, null,this);
                    firstTimeLoaded=true;
                }else{
                    getLoaderManager().restartLoader(1,null,this);
                }

                break;
            case R.id.buttonAddContact: addContact();
                break;
            case R.id.buttonRemoveContact:removeContacts();
                break;
            case R.id.buttonUpdateContact: updateContact();
                break;
            default:
                break;
        }

    }

    private void addContact() {
        ArrayList<ContentProviderOperation> cops=new ArrayList<ContentProviderOperation>();

        cops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE,"accountname@gmail.com")
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, "com.google")
                .build());
        cops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, editTextContactName.getText().toString())
                .build());

        try{
            getContentResolver().applyBatch(ContactsContract.AUTHORITY,cops);
            Toast.makeText(this,"contacto agregado",Toast.LENGTH_LONG);
        }catch (Exception exception){
            Log.i(TAG,exception.getMessage());
        }
    }

    private void updateContact(){

        String [] updateValue=editTextContactName.getText().toString().split(" ");
        ContentProviderResult[] result=null;

        String targetString=null;
        String newString=null;
        if(updateValue.length==2){

            targetString=updateValue[0];
            newString=updateValue[1];

            String where= ContactsContract.RawContacts._ID + " = ? ";
            String [] params= new String[] {targetString};

            ContentResolver contentResolver=getContentResolver();
            ContentValues contentValues=new ContentValues();
            contentValues.put(ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY,newString);
            contentResolver.update(ContactsContract.RawContacts.CONTENT_URI,contentValues, where,params);
        }
    }

    private void removeContacts(){
        String whereClause=ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY+ " = '"+editTextContactName.getText().toString()+"'";
        getContentResolver().delete(ContactsContract.RawContacts.CONTENT_URI,whereClause,null);
        Toast.makeText(this,"contacto eliminado",Toast.LENGTH_LONG);
    }

    private void addContactsViaIntents(){
        String tempContactText=editTextContactName.getText().toString();
        if(tempContactText!=null && !tempContactText.equals("") && tempContactText.length()>0 ){
            Intent intent=new Intent(ContactsContract.Intents.Insert.ACTION);
            intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
            intent.putExtra(ContactsContract.Intents.Insert.NAME,tempContactText);
            startActivity(intent);
        }
    }
}
