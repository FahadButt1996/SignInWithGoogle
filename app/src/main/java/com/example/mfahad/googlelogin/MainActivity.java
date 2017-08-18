package com.example.mfahad.googlelogin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener{
    TextView name ;
    TextView email;
    private GoogleApiClient googleApiClient;
    SignInButton googleSignIn;
    Button logout;
    ImageView imageView;
    LinearLayout linearLayout;
    private static final int Req_Code = 9001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        googleSignIn = (SignInButton) findViewById(R.id.sign_in_button);
        googleSignIn.setOnClickListener(this);
        logout = (Button) findViewById(R.id.logout);
        imageView = (ImageView) findViewById(R.id.imageView);
        linearLayout = (LinearLayout)findViewById(R.id.innerLayout);
        name = (TextView)findViewById(R.id.name);
        email = (TextView)findViewById(R.id.email);
        linearLayout.setVisibility(View.GONE);
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,googleSignInOptions).build();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                                linearLayout.setVisibility(View.GONE);
                                googleSignIn.setVisibility(View.VISIBLE);
                                imageView.setImageDrawable(null);
                            }
                        });
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent i = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(i , Req_Code);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection Fialed", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==Req_Code )
        {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess())
            {
                linearLayout.setVisibility(View.VISIBLE);
                googleSignIn.setVisibility(View.GONE);
                GoogleSignInAccount account = result.getSignInAccount();
                String n = account.getDisplayName();
                String e = account.getEmail();
                name.setText("Name "+ n);
                email.setText("Email "+e);

                String img_url = "";

                try {
                    img_url = account.getPhotoUrl().toString();
                    Glide.with(this).load(img_url).into(imageView);
                }catch (Exception exp)
                {
                }
//                Toast.makeText(this, name + email + img_url , Toast.LENGTH_SHORT).show();
            }
        }
    }
}
