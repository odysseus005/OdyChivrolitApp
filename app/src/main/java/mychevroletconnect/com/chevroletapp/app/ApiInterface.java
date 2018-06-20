package mychevroletconnect.com.chevroletapp.app;




import java.util.Map;

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
                                  @Field(Constants.EMAIL) String username,
                                  @Field(Constants.PASSWORD) String password,
                                  @Field(Constants.FIRST_NAME) String firstName,
                                  @Field(Constants.LAST_NAME) String lastName,
                                  @Field(Constants.CONTACT) String contact,
                                  @Field(Constants.BIRTHDAY) String birthday,
                                  @Field(Constants.ADDRESS) String address,
                                  @Field(Constants.POSITION) String position,
                                  @Field(Constants.BUSINESS_ID) String businessID
    );

    @FormUrlEncoded
    @POST(Endpoints.CLIENT)
    Call<ResultResponse> registerCar(@Field(Constants.TAG) String tag,
                                     @Field(Constants.BUSINESS_NAME) String bName,
                                     @Field(Constants.BUSINESS_ADDRESS) String bAddress,
                                     @Field(Constants.BUSINESS_CONTACT) String bContact,
                                     @Field(Constants.BUSINESS_DESCRIPTION) String bDescription
    );


}
