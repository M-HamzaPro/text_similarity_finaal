package com.example.fypwebhost;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText editTextName, editTextEmail, editTextPassword, editTextVerificationCode;
    Button buttonAdd, buttonVerify;
    TextView textViewLogin;
    RadioGroup radioGroupType;
    RadioButton radioButtonTeacher, radioButtonStudent;
    String randomNumber, verificationCode,
    nameForSignUp, emailForSignUp, passwordForSignUp;
    int user_type;
    TextInputLayout verifyGroup, nameGroup, emailGroup, passwordGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        verifyGroup = findViewById(R.id.verifyGroup);
        nameGroup = findViewById(R.id.nameGroup);
        emailGroup= findViewById(R.id.emailGroup);
        passwordGroup = findViewById(R.id.passwordGroup);
        verifyGroup.setVisibility(View.INVISIBLE);

        buttonVerify = findViewById(R.id.buttonVerifySignUp);
        buttonVerify.setVisibility(View.INVISIBLE);
        editTextVerificationCode = findViewById(R.id.editTextVerificationCode);


        radioGroupType = (RadioGroup)findViewById(R.id.radioGroupType);

        radioButtonStudent = (RadioButton)findViewById(R.id.radio_students);
        radioButtonTeacher = (RadioButton)findViewById(R.id.radio_teacher) ;

        textViewLogin = (TextView) findViewById(R.id.textViewLogin);
        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, login.class);
                startActivity(intent);
            }
        });
        editTextName =(EditText) findViewById(R.id.txtName);
        editTextEmail =(EditText) findViewById(R.id.txtEmail);
        editTextPassword =(EditText) findViewById(R.id.txtPassword);


        buttonAdd = (Button) findViewById(R.id.btnAdd);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nameForSignUp = editTextName.getText().toString().trim();;
                emailForSignUp = editTextEmail.getText().toString().trim();;
                passwordForSignUp = editTextPassword.getText().toString().trim();;

                verificationCode = verifyUser();
                String verifyCode = editTextVerificationCode.getText().toString();
                if(verifyCode.isEmpty())
                {
                    Toast.makeText(MainActivity.this, "enter code", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    editTextVerificationCode.setText(null);
                }

            }
        });
        buttonVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = editTextVerificationCode.getText().toString().trim();

                if(code.contains(randomNumber))
                {
                    signUpUser();
                    Intent intent = new Intent(MainActivity.this, login.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Code not correct", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void signUpUser() {

        final String name = nameForSignUp;
        final String email = emailForSignUp;
        final String password = passwordForSignUp;

        if(name.isEmpty() || email.isEmpty() || password.isEmpty())
        {
            Toast.makeText(this, "fill all text" , Toast.LENGTH_SHORT).show();
        }
        else
        {


            StringRequest request = new StringRequest(Request.Method.POST, "https://temp321.000webhostapp.com/connect/Connect.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(response.equalsIgnoreCase("Account Created"))
                            {
                                Toast.makeText(MainActivity.this, "Account Created", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String > params = new HashMap<String, String>();

                    params.put("Name", name);
                    params.put("Email", email);
                    params.put("Password", password);
                    params.put("userType", String.valueOf(user_type));

                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
            requestQueue.add(request);
        }
    }

    private String verifyUser()
    {
        final String email = editTextEmail.getText().toString().trim();


                    StringRequest request = new StringRequest(Request.Method.POST, "https://temp321.000webhostapp.com/connect/verifyUser.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(response.contains("Email verified cheema"))
                            {
                                Toast.makeText(MainActivity.this, "Email Verified", Toast.LENGTH_SHORT).show();
                                String[] data = response.split(",");
                                randomNumber = data[1];
                            }
                            else
                            {
                                Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String > params = new HashMap<String, String>();

                    params.put("Email", email);

                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
            requestQueue.add(request);

            nameGroup.setVisibility(View.INVISIBLE);
            emailGroup.setVisibility(View.INVISIBLE);
            passwordGroup.setVisibility(View.INVISIBLE);
            radioGroupType.setVisibility(View.INVISIBLE);
            buttonVerify.setVisibility(View.VISIBLE);
            buttonAdd.setVisibility(View.INVISIBLE);
            verifyGroup.setVisibility(View.VISIBLE);
            textViewLogin.setVisibility(View.INVISIBLE);


            return randomNumber;
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        user_type = -1;

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_teacher:
                if (checked)
                    user_type = 1;
                break;
            case R.id.radio_students:
                if (checked)
                    user_type = 0;
                break;
        }
//        Toast.makeText(MainActivity.this, user_type, Toast.LENGTH_SHORT).show();
    }

}