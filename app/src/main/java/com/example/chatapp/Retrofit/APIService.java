package com.example.chatapp.Retrofit;

import com.example.chatapp.Dtos.CreateUserDto;
import com.example.chatapp.Dtos.LoginDto;
import com.example.chatapp.Dtos.LoginInputDto;
import com.example.chatapp.Dtos.UserDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface APIService {
    @POST("users")
    Call<UserDto> createUser(@Body CreateUserDto user);
    @POST("auth/login")
    Call<LoginDto> loginUser(@Body LoginInputDto login);
}
