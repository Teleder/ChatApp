package com.example.chatapp.Retrofit;

import com.example.chatapp.Dtos.CreateGroupDto;
import com.example.chatapp.Dtos.CreateUserDto;
import com.example.chatapp.Dtos.GroupDto;
import com.example.chatapp.Dtos.LoginDto;
import com.example.chatapp.Dtos.LoginInputDto;
import com.example.chatapp.Dtos.PagedResultDto;
import com.example.chatapp.Dtos.PayloadAction;
import com.example.chatapp.Dtos.PayloadMessage;
import com.example.chatapp.Dtos.PayloadAction;
import com.example.chatapp.Dtos.PayloadMessage;
import com.example.chatapp.Dtos.RoleDto;
import com.example.chatapp.Dtos.UpdateInfoUserDto;
import com.example.chatapp.Dtos.UserDto;
import com.example.chatapp.Dtos.UserBasicDto;
import com.example.chatapp.Dtos.UserProfileDto;
import com.example.chatapp.Model.Conservation.Conservation;
import com.example.chatapp.Model.File.File;
import com.example.chatapp.Model.Group.Group;
import com.example.chatapp.Model.Group.Member;
import com.example.chatapp.Model.Message.Message;
import com.example.chatapp.Model.User.Contact;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {
    // User
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


    // file
    @Multipart
    @POST("files/local/upload")
    Call<File> uploadFile(
            @Query("code") String code,
            @Part MultipartBody.Part file
    );
    @Multipart
    @POST("files/cloud/upload")
    Call<File> uploadFileCloud(
            @Query("code") String code,
            @Part MultipartBody.Part file
    );

    // message
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
    @POST("messages/groupMessage/{groupId}")
    Call<Message> sendGroupMessage(@Path("groupId") String groupId, @Body PayloadMessage message);

    // Conservation
    @GET("conservations/get-my-conversations")
    Call<PagedResultDto<Conservation>> getMyConversations(@Query("page") int page, @Query("size") int size);
    @GET("conservations/get-my-conversations-group")
    Call<List<String>> getAllIdConservationGroup();

    //Group
    @GET("groups/{id}")
    Call<GroupDto> getDetailGroup(@Path("id") String groupId);
    @POST("groups/{groupId}/create-role")
    Call<Group> createRoleForGroup(@Path("groupId") String groupId, @Body RoleDto roleRequest);
    @DELETE("groups/{groupId}/delete-role")
    Call<Void> deleteRoleForGroup(@Path("groupId") String groupId, @Query("roleName") String roleName);
    @POST("groups/create")
    Call<GroupDto> createGroup(@Body CreateGroupDto input);
    @PATCH("groups/{groupId}/add-member")
    Call<Group> addMemberToGroup(@Path("groupId") String groupId, @Query("memberId") String memberId);
    @PATCH("groups/{groupId}/block-member")
    Call<Group> blockMemberFromGroup(@Path("groupId") String groupId, @Query("memberId") String memberId,  @Query("reason") String reason);
    @PATCH("groups/{groupId}/remove-block-member")
    Call<Group> removeBlockMemberFromGroup(@Path("groupId") String groupId, @Query("memberId") String memberId);
    @PATCH("groups/{groupId}/remove-member")
    Call<Group> removeMemberFromGroup(@Path("groupId") String groupId, @Query("memberId") String memberId);
    @PATCH("groups/{groupId}/remove-member")
    Call<Group> decentralization(@Path("groupId") String groupId, @Query("memberId") String memberId, @Query("roleName") String roleName);
    @PATCH("groups/{groupId}/response-member-join")
    Call<Member> getRequestMemberJoin(@Path("groupId") String groupId, @Query("memberId") String memberId);
    @GET("groups/{groupId}/request-member-join")
    Call<Void> responseMemberJoin(@Path("groupId") String groupId, @Query("memberId") String memberId, @Query("accept") Boolean accept);
    @GET("groups/{groupId}/members-paginate")
    Call<Void> getMembersPaginate(@Path("groupId") String groupId, @Query("search") String search, @Query("page") int page, @Query("size") int size);
    @GET("groups/{groupId}/groups-paginate")
    Call<Void> getMyGroupsPaginate(@Path("groupId") String groupId, @Query("search") String search, @Query("page") int page, @Query("size") int size);

    @PATCH("groups/{groupId}/leave-group")
    Call<Void> leaveGroup(@Path("groupId") String groupId);
    @GET("groups/get-friend-add-group/{groupId}")
    Call<List<UserBasicDto>> getNonBlockedNonMemberFriends(@Path("groupId") String groupId);
}
