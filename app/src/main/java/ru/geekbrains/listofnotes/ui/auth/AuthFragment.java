package ru.geekbrains.listofnotes.ui.auth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.vk.api.sdk.VK;
import com.vk.api.sdk.VKApiCallback;
import com.vk.api.sdk.auth.VKScope;
import com.vk.sdk.api.users.UsersService;
import com.vk.sdk.api.users.dto.NameCaseParam;
import com.vk.sdk.api.users.dto.UsersFields;
import com.vk.sdk.api.users.dto.UsersUserXtrCounters;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ru.geekbrains.listofnotes.R;
import ru.geekbrains.listofnotes.domain.Callback;

public class AuthFragment extends Fragment {

    public static final String AUTH_RESULT = "AUTH_RESULT";
    private GoogleSignInClient googleSignInClient;

    public static AuthFragment newInstance() {
        return new AuthFragment();
    }

    public static boolean getAuthorizeData(Context context, Callback<AuthData> callback) {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(context);
        if (account != null) {
            if (callback != null) {
                callback.onSuccess(new AuthData(account.getDisplayName(), account.getEmail(), account.getPhotoUrl()));
            }
            return true;
        }
        if (VK.isLoggedIn()) {
            if (callback != null) {
                VK.execute((new UsersService()).usersGet(Arrays.asList(String.valueOf(VK.getUserId())),
                        Arrays.asList(UsersFields.PHOTO_200),
                        NameCaseParam.NOMINATIVE), new VKApiCallback<List<UsersUserXtrCounters>>() {
                    @Override
                    public void success(List<UsersUserXtrCounters> usersUserXtrCounters) {
                        UsersUserXtrCounters us = usersUserXtrCounters.get(0);
                        String name = us.getFirstName();
                        String phone = us.getMobilePhone();
                        String urlPhoto = us.getPhoto200();
                        AuthData authData = new AuthData(
                                (name != null) ? name : "",
                                (phone != null) ? phone : "",
                                (urlPhoto != null) ? Uri.parse(urlPhoto) : null);
                        callback.onSuccess(authData);
                    }

                    @Override
                    public void fail(@NotNull Exception e) {

                    }
                });

            }
            return true;
        }
        return false;
    }

    public static void unSign(Context context) {
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .build();

        GoogleSignIn.getClient(context, options).signOut();
        if (VK.isLoggedIn()) {
            VK.logout();
        }
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

        ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                    try {
                        GoogleSignInAccount account = accountTask.getResult();
                        getParentFragmentManager().setFragmentResult(AUTH_RESULT, new Bundle());
                    } catch (Exception ex) {
                        Toast.makeText(requireContext(), "Auth Failed", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        view.findViewById(R.id.sign_in)
                .setOnClickListener(v -> {
                    Intent intent = googleSignInClient.getSignInIntent();
                    launcher.launch(intent);
                });
        view.findViewById(R.id.sign_in_vk)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        VK.login(requireActivity(), Collections.singletonList(VKScope.NOTES));
                    }
                });
    }
}
