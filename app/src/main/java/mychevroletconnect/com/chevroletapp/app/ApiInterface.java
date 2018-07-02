package mychevroletconnect.com.chevroletapp.app;




import java.util.Map;

import mychevroletconnect.com.chevroletapp.model.data.User;
import mychevroletconnect.com.chevroletapp.model.response.LoginResponse;
import mychevroletconnect.com.chevroletapp.model.response.ResultResponse;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


public interface ApiInterface {

    @FormUrlEncoded
    @POST(Endpoints.CLIENT)
    Call<LoginResponse> login(@Field(Constants.TAG) String tag,
                              @Field(Constants.EMAIL) String username,
                              @Field(Constants.PASSWORD) String password);


    @FormUrlEncoded
    @POST(Endpoints.CLIENT)
    Call<ResultResponse> register(@Field(Constants.TAG) String tag,
                                  @Field(Constants.EMAIL) String email,
                                  @Field(Constants.PASSWORD) String password,
                                  @Field(Constants.FIRST_NAME)String firstName,
                                  @Field(Constants.MIDDLENAME)String middleName,
                                  @Field(Constants.LAST_NAME) String lastName,
                                  @Field(Constants.BIRTHDAY) String birthday,
                                  @Field(Constants.CONTACT) String contact,
                                  @Field(Constants.ADDRESS) String address,
                                  @Field(Constants.CITIZENSHIP)String citizenship,
                                  @Field(Constants.OCCUPATION) String occupation,
                                  @Field(Constants.GENDER) String gender,
                                  @Field(Constants.CIVIL_STATUS)String civil
    );

    @FormUrlEncoded
    @POST(Endpoints.GARAGE)
    Call<ResultResponse> registerCar(@Field(Constants.TAG) String tag,
                                     @Field(Constants.GARAGE_CHASSIS) String garage_chassis,
                                     @Field(Constants.GARAGE_MODEL) String garage_model,
                                     @Field(Constants.GARAGE_YEAR_MODEL) String garage_year,
                                     @Field(Constants.GARAGE_PLATE) String garage_plate,
                                     @Field(Constants.GARAGE_PURCHASE) String garage_purchase,
                                     @Field(Constants.USER_ID) String user_id
    );


    @FormUrlEncoded
    @POST(Endpoints.CLIENT)
    Call<ResultResponse> checkEmail(@Field(Constants.TAG) String tag, @Field(Constants.EMAIL) String email);

    @FormUrlEncoded
    @POST(Endpoints.CLIENT)
    Call<ResultResponse> changePassword(@Field(Constants.TAG) String tag,
                                        @Field(Constants.USER_ID) String user_id,
                                        @Field(Constants.PASSWORD) String password);

    @FormUrlEncoded
    @POST(Endpoints.CLIENT)
    Call<User> updateUser(@Field(Constants.TAG) String tag,
                          @Field(Constants.USER_ID) String user_id,
                          @Field(Constants.FIRST_NAME) String first_name,
                          @Field(Constants.LAST_NAME) String last_name,
                          @Field(Constants.CONTACT) String contact,
                          @Field(Constants.BIRTHDAY) String birthday,
                          @Field(Constants.ADDRESS) String address);

    @Multipart
    @POST("upload.php?")
    Call<ResultResponse> uploadFile(@Part MultipartBody.Part file, @Part("name") RequestBody name);



}
