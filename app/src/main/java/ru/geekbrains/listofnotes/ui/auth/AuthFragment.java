package ru.geekbrains.listofnotes.ui.auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;

import ru.geekbrains.listofnotes.R;

public class AuthFragment extends Fragment {

    public static final int AUTH_REQUEST_ID = 1;
    public static final String AUTH_RESULT = "AUTH_RESULT";

    public static AuthFragment newInstance() {
        return new AuthFragment();
    }

    public static AuthData getAuthorizeData(Context context) {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(context);
        if (account != null) {
            return new AuthData(account.getDisplayName(), account.getEmail());
        }
        return null;
    }

    private GoogleSignInClient googleSignInClient;

    public static void unSign(Context context) {
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .build();

        GoogleSignIn.getClient(context, options).signOut();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(requireContext(), options);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_auth, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.sign_in)
                .setOnClickListener(v -> {
                    Intent intent = googleSignInClient.getSignInIntent();
                    startActivityForResult(intent, AUTH_REQUEST_ID);
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AUTH_REQUEST_ID) {
            Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = accountTask.getResult();
                getParentFragmentManager().setFragmentResult(AUTH_RESULT, new Bundle());
            } catch (Exception ex) {
                Toast.makeText(requireContext(), "Auth Failed", Toast.LENGTH_LONG).show();
            }
        }
    }
}
