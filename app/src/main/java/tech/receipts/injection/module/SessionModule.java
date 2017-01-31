package tech.receipts.injection.module;

import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import tech.receipts.components.NetworkStateManager;
import tech.receipts.data.AuthProvider;
import tech.receipts.data.api.auth.AuthApiService;
import tech.receipts.data.api.auth.AuthFromPasswordCaller;
import tech.receipts.data.api.auth.AuthFromRefreshTokenCaller;
import tech.receipts.data.model.Auth;

@Module
public class SessionModule {

    @Provides
    @Singleton
    AuthProvider provideSessionHandler(AuthFromPasswordCaller authFromPasswordCaller, AuthFromRefreshTokenCaller authFromRefreshTokenCaller, SharedPreferences sharedPreferences, Retrofit retrofit, Auth auth) {
        return new AuthProvider(authFromPasswordCaller, authFromRefreshTokenCaller, sharedPreferences, retrofit, auth);
    }

    @Provides
    @Singleton
    AuthFromPasswordCaller provideAuthFromPasswordProvider(AuthApiService authApiService, NetworkStateManager networkStateManager, Retrofit retrofit) {
        return new AuthFromPasswordCaller(authApiService, networkStateManager, retrofit);
    }

    @Provides
    @Singleton
    AuthFromRefreshTokenCaller provideAuthFromRefreshTokenProvider(AuthApiService authApiService, NetworkStateManager networkStateManager, Retrofit retrofit) {
        return new AuthFromRefreshTokenCaller(authApiService, networkStateManager, retrofit);
    }

    @Provides
    @Singleton
    Auth provideAuth(SharedPreferences sharedPreferences) {
        String accessToken = sharedPreferences.getString(AuthProvider.ACCESS_TOKEN, null);
        String refreshToken = sharedPreferences.getString(AuthProvider.REFRESH_TOKEN, null);

        Auth auth = new Auth();
        auth.setAccessToken(accessToken);
        auth.setRefreshToken(refreshToken);
        return auth;
    }
}
