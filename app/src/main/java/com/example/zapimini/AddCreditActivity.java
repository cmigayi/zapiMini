package com.example.zapimini;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;

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
    public static final int PICK_CONTACT    = 1;

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

                    Credit credit = new Credit(
                            0,
                            business.getId(),
                            user.getId(),
                            activityAddCreditBinding.name.getText().toString(),
                            "",
                            "",
                            Double.parseDouble(activityAddCreditBinding.amountValue.getText().toString()),
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (PICK_CONTACT) :
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor cursor = getContentResolver().query(contactData, null,
                            null, null, null);
                    if (cursor.moveToFirst()) {
                        String contactId = cursor.getString(cursor.getColumnIndex(
                                        ContactsContract.Contacts._ID));

                        String phoneName = cursor.getString(cursor.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

                        if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER))) > 0)
                        {
                            Cursor phones = getContentResolver().query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID +
                                            " = " + contactId, null, null);

                            String phoneNumber = phones.getString(cursor.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Phone.NUMBER));

                            Log.d(mAddCreditActivity, "Contact: "+phoneName+" "+phoneNumber);
                        }
                    }
                    cursor.close();
                }
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
            intent.putExtra("message_2", "Item: "+credit.getName()+
                    ", Amount: ksh."+credit.getAmount());
            startActivity(intent);
            finish();
        }catch(Exception e){
            Log.d(mAddCreditActivity, "Error: "+e.getMessage());
            displayError("");
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
}
