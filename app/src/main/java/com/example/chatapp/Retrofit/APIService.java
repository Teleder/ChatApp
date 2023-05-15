package com.example.chatapp.Retrofit;

import com.example.chatapp.Dtos.CreateUserDto;
import com.example.chatapp.Dtos.LoginDto;
import com.example.chatapp.Dtos.LoginInputDto;
import com.example.chatapp.Dtos.PagedResultDto;
import com.example.chatapp.Dtos.PayloadAction;
import com.example.chatapp.Dtos.PayloadMessage;
import com.example.chatapp.Dtos.PayloadAction;
import com.example.chatapp.Dtos.PayloadMessage;
import com.example.chatapp.Dtos.UpdateInfoUserDto;
import com.example.chatapp.Dtos.UserDto;
import com.example.chatapp.Dtos.UserBasicDto;
import com.example.chatapp.Dtos.UserProfileDto;
import com.example.chatapp.Model.File.File;
import com.example.chatapp.Model.Message.Message;
import com.example.chatapp.Model.User.Contact;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {
    @POST("users")
    Call<UserDto> createUser(@Body CreateUserDto user);
    @POST("auth/login")
    Call<LoginDto> loginUser(@Body LoginInputDto login);
    @PATCH("users/profile")
    Call<UserDto> updateProfile(@Body UpdateInfoUserDto updateInfoUserDto);
    @GET("users/contact-waiting-accept")
    Call<PagedResultDto<Contact>> getContactRequestSend(@Query("page") int page, @Query("size") int size);
    @FormUrlEncoded
    @PATCH("users/respond-to-request-for-contacts")
    Call<Boolean> AcceptContact(@Field("contactId") String contactId, @Field("accept") boolean accept);
    @GET("users/contacts")
    Call<PagedResultDto<UserBasicDto>> getContacts(@Query("displayName") String displayName, @Query("page") int page, @Query("size") int size);
    @PATCH("users/remove-contact")
    Call<Boolean> UnFriend(@Query("contactId") String contactId);
    @GET("users/search")
    Call<List<UserBasicDto>> SearchFriend(@Query("searchText") String searchText);
    @Multipart
    @POST("files/local/upload")
    Call<File> uploadFile(
            @Query("code") String code,
            @Part MultipartBody.Part file
    );
    @POST("messages/privateMessage/{recipientId}")
    Call<Message> sendPrivateMessage(@Path("recipientId") String recipientId, @Body PayloadMessage message);
    @GET("messages/{code}")
    Call<PagedResultDto<Message>> findMessagesWithPaginationAndSearch(
            @Path("code") String code,
            @Query("page") int page,
            @Query("size") int size,
            @Query("content") String content
    );
    @POST("messages/sendAction")
    Call<ResponseBody> sendAction(@Body PayloadAction payloadAction);
    @PATCH("users/add-contact")
    Call<Boolean> AddFriend(@Query("contactId") String contactId);
    @PATCH("users/remove-request-friend")
    Call<UserProfileDto> RemoveRequestAddFriend(@Query("contactId") String contactId);
}
