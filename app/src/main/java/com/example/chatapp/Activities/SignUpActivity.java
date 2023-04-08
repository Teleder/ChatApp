package com.example.chatapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.chatapp.R;
import com.example.chatapp.Retrofit.APIService;
import com.example.chatapp.Retrofit.RetrofitClient;
import com.example.chatapp.Retrofit.TokenManager;
import com.example.chatapp.Dtos.CreateUserDto;
import com.example.chatapp.Dtos.UserDto;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SignUpActivity extends AppCompatActivity {
    Button btnsignup;
    APIService apiService;
    EditText firstname;
    EditText lastname;
    EditText password;
    EditText email;
    EditText phone;
    EditText confirmpassword;
    private RetrofitClient retrofitClient;
    private TokenManager tokenManager;
    private Retrofit retrofit;
    ProgressBar processBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        retrofitClient = RetrofitClient.getInstance(getApplicationContext());
        tokenManager = retrofitClient.getTokenManager();
        retrofit = retrofitClient.getRetrofit();
        findViewById(R.id.textSignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivities(new Intent[]{new Intent(SignUpActivity.this, LoginActivity.class)});
            }
        });
        btnsignup = (Button) findViewById(R.id.buttonSignUp);
        firstname = (EditText) findViewById(R.id.inputFirstName);
        lastname = (EditText) findViewById(R.id.inputLastName);
        password = (EditText) findViewById(R.id.inputPassword);
        confirmpassword = (EditText) findViewById(R.id.inputConfirmPassword);
        email = (EditText) findViewById(R.id.inputEmail);
        phone = (EditText) findViewById(R.id.inputPhone);
        processBar = (ProgressBar)  findViewById(R.id.processBar);
        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
    }

    private void register() {
        final String fname = firstname.getText().toString();
        final String lname = lastname.getText().toString();
        final String pass = password.getText().toString();
        final String confirmpass = confirmpassword.getText().toString();
        final String emailuser = email.getText().toString();
        final String phoneuser = phone.getText().toString();
        if (TextUtils.isEmpty(fname)) {
            firstname.setError("Please enter your firstname");
            firstname.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(lname)) {
            lastname.setError("Please enter your lastname");
            lastname.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(pass)) {
            password.setError("Please enter your password");
            password.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(confirmpass)) {
            confirmpassword.setError("Please enter your confirmpassword");
            confirmpassword.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(emailuser)) {
            email.setError("Please enter your email");
            email.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(phoneuser)) {
            phone.setError("Please enter your phone");
            phone.requestFocus();
            return;
        }
        if (!pass.equals(confirmpass)) {
            confirmpassword.setError("Please enter your equal password");
            confirmpassword.requestFocus();
            return;
        }
        apiService = retrofit.create(APIService.class);
        apiService.createUser(new CreateUserDto(fname, lname, phoneuser, emailuser, "string", pass)).enqueue(new Callback<UserDto>() {
            @Override
            public void onResponse(Call<UserDto> call, Response<UserDto> response) {
                processBar.setVisibility(View.VISIBLE);
                if (response.isSuccessful()) {
                    try {
                        Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                        finish();
                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserDto> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}