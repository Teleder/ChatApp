package com.example.chatapp.Retrofit;

import com.example.chatapp.Dtos.CreateUserDto;
import com.example.chatapp.Dtos.LoginDto;
import com.example.chatapp.Dtos.LoginInputDto;
import com.example.chatapp.Dtos.PagedResultDto;
import com.example.chatapp.Dtos.UpdateInfoUserDto;
import com.example.chatapp.Dtos.UserDto;
import com.example.chatapp.Model.User.Contact;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIService {
    @POST("users")
    Call<UserDto> createUser(@Body CreateUserDto user);
    @POST("auth/login")
    Call<LoginDto> loginUser(@Body LoginInputDto login);
    @PATCH("users/profile")
    Call<UserDto> updateProfile(@Body UpdateInfoUserDto updateInfoUserDto);
    @GET("users/contact-waiting-accept")
    Call<PagedResultDto<Contact>> getContactRequestSend(@Query("displayName") String displayName, @Query("page") int page, @Query("size") int size);
    @FormUrlEncoded
    @PATCH("users/respond-to-request-for-contacts")
    Call<Boolean> AcceptContact(@Field("contactId") String contactId, @Field("accept") boolean accept);
}
