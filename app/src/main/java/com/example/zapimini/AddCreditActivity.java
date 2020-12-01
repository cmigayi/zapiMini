package com.example.zapimini;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.zapimini.commons.CreditCalculation;
import com.example.zapimini.commons.DateTimeUtils;
import com.example.zapimini.data.Business;
import com.example.zapimini.data.Credit;
import com.example.zapimini.data.User;
import com.example.zapimini.databinding.ActivityAddCreditBinding;
import com.example.zapimini.localDatabases.CreditLocalDb;
import com.example.zapimini.localStorage.BusinessLocalStorage;
import com.example.zapimini.localStorage.UserLocalStorage;
import com.example.zapimini.presenters.AddCreditActivityPresenter;
import com.example.zapimini.views.AddCreditActivityView;

public class AddCreditActivity extends AppCompatActivity
        implements View.OnClickListener, AddCreditActivityView {
    final static String mAddCreditActivity = "AddAddCreditActivity";
    public static final int PICK_CONTACT = 1;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    ActivityAddCreditBinding activityAddCreditBinding;
    Intent intent;

    AddCreditActivityPresenter presenter;

    UserLocalStorage userLocalStorage;
    User user;

    String item;
    double amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityAddCreditBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_add_credit);

        Toolbar toolbar = findViewById(R.id.tool_bar);
        toolbar.setTitle("Add credit");
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        userLocalStorage = new UserLocalStorage(this);
        authUser(userLocalStorage);
        user = userLocalStorage.getLoggedInUser();

        CreditLocalDb creditLocalDb = new CreditLocalDb(this);
        presenter = new AddCreditActivityPresenter(creditLocalDb, this);

        activityAddCreditBinding.submit.setOnClickListener(this);
        activityAddCreditBinding.importBtn.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        intent = new Intent(AddCreditActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case android.R.id.home:
                intent = new Intent(AddCreditActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submit:
                if(validateInputs()) {
                    BusinessLocalStorage businessLocalStorage = new BusinessLocalStorage(this);
                    Business business = businessLocalStorage.getBusiness();

                    double creditAmount = Double.parseDouble(activityAddCreditBinding.amountValue.
                            getText().toString());
                    double balance = new CreditCalculation().getCreditBalance(
                           creditAmount, 0.0
                    );

                    Credit credit = new Credit(
                            0,
                            business.getId(),
                            user.getId(),
                            activityAddCreditBinding.name.getText().toString(),
                            activityAddCreditBinding.phone.getText().toString(),
                            creditAmount,
                            0.0,
                            balance,
                            selectedCreditOption(activityAddCreditBinding.creditOptions),
                            new DateTimeUtils().getTodayDateTime()
                    );
                    presenter.createCredit(credit);
                }
                break;
            case R.id.import_btn:
                intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, PICK_CONTACT);
                break;
        }
    }

    @Override
    public ProgressBar getProgressbar() {
        return activityAddCreditBinding.progressBar;
    }

    @Override
    public void authUser(UserLocalStorage userLocalStorage) {
        if(!userLocalStorage.isUserLogged()){
            intent = new Intent(AddCreditActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean validateInputs() {
        boolean validated = false;
        if(activityAddCreditBinding.name.getText().toString().equals("") ||
                activityAddCreditBinding.amountValue.getText().toString().equals("")){
            displayError("Credit item or amount is invalid!");
        }else {
            item = activityAddCreditBinding.name.getText().toString();
            amount = Double.parseDouble(
                    activityAddCreditBinding.amountValue.getText().toString());
            validated = true;
        }
        return validated;
    }

    @Override
    public void createdCredit(Credit credit) {
        try{
            Log.d(mAddCreditActivity, "Done");
            intent = new Intent(AddCreditActivity.this,
                    AddCreditConfirmationActivity.class);
            intent.putExtra("message", "You have added this credit to expense reports successfully!");
            intent.putExtra("message_2", "Item: "+credit.getName()+", "+
                    credit.getPhone()+", Amount: ksh."+credit.getAmount());
            intent.putExtra("type", credit.getType());
            startActivity(intent);
            finish();
        }catch(Exception e){
            Log.d(mAddCreditActivity, "Error: "+e.getMessage());
            displayError("There was an error. Please try again!");
        }
    }

    @Override
    public String selectedCreditOption(RadioGroup radioGroup) {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton) findViewById(selectedId);
        return radioButton.getText().toString();
    }

    @Override
    public void displayError(String message) {
        activityAddCreditBinding.progressBar.setVisibility(View.GONE);
        activityAddCreditBinding.errorTv.setText(message);
        activityAddCreditBinding.errorTv.setVisibility(View.VISIBLE);
    }

    private void getContact(Uri uri){
        Cursor cursor = getContentResolver().query(uri,
                null, null, null, null);
        if(cursor.moveToFirst()){
            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

            String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

            if (hasPhone.equalsIgnoreCase("1"))
            {
                Cursor phone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,
                        null, null);
                phone.moveToFirst();
                String phoneNumber = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

//                Log.d(mAddCreditActivity, "Contacts");
//                Log.d(mAddCreditActivity, "name: "+name);
//                Log.d(mAddCreditActivity, "number: "+phoneNumber);
                activityAddCreditBinding.name.setText(name);
                activityAddCreditBinding.phone.setText(phoneNumber);
                Toast.makeText(this, name+":"+phoneNumber+" imported", Toast.LENGTH_LONG).show();
            }

        }else{
            Log.d(mAddCreditActivity, "No contacts");
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_CONTACT){
            switch(resultCode){
                case RESULT_OK:
                    // Contact picked
                    Log.d(mAddCreditActivity, "code: "+data.getData());
                    getContact(data.getData());
                    break;
            }
        }
    }
}
