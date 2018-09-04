package mychevroletconnect.com.chevroletapp.app;




import java.util.List;
import java.util.Map;

import mychevroletconnect.com.chevroletapp.model.data.Garage;
import mychevroletconnect.com.chevroletapp.model.data.Schedule;
import mychevroletconnect.com.chevroletapp.model.data.User;
import mychevroletconnect.com.chevroletapp.model.response.AdvisorListResponse;
import mychevroletconnect.com.chevroletapp.model.response.AppointmentListResponse;
import mychevroletconnect.com.chevroletapp.model.response.CarListResponse;
import mychevroletconnect.com.chevroletapp.model.response.DealerContactListResponse;
import mychevroletconnect.com.chevroletapp.model.response.DealerListResponse;
import mychevroletconnect.com.chevroletapp.model.response.GarageListResponse;
import mychevroletconnect.com.chevroletapp.model.response.LoginResponse;
import mychevroletconnect.com.chevroletapp.model.response.PastAppointmentListResponse;
import mychevroletconnect.com.chevroletapp.model.response.PmsListResponse;
import mychevroletconnect.com.chevroletapp.model.response.ResultResponse;
import mychevroletconnect.com.chevroletapp.model.response.ScheduleListResponse;
import mychevroletconnect.com.chevroletapp.model.response.ServiceListResponse;
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
    @POST(Endpoints.CLIENT)
    Call<LoginResponse> updateUserCode(@Field(Constants.TAG) String tag,
                                       @Field(Constants.USER_ID) String user_id);



    @FormUrlEncoded
    @POST(Endpoints.CLIENT)
    Call<ResultResponse> checkEmail(@Field(Constants.TAG) String tag, @Field(Constants.EMAIL) String email);

    @FormUrlEncoded
    @POST(Endpoints.CLIENT)
    Call<ResultResponse> changePassword(@Field(Constants.TAG) String tag,
                                        @Field(Constants.USER_ID) String user_id,
                                        @Field(Constants.OLD_PASSWORD) String oldpassword,
                                        @Field(Constants.PASSWORD) String password);

    @FormUrlEncoded
    @POST(Endpoints.CLIENT)
    Call<LoginResponse> updateUser(@Field(Constants.TAG) String tag,
                          @Field(Constants.USER_ID) String user_id,
                          @Field(Constants.FIRST_NAME)String firstName,
                          @Field(Constants.MIDDLENAME)String middleName,
                          @Field(Constants.LAST_NAME) String lastName,
                          @Field(Constants.BIRTHDAY) String birthday,
                          @Field(Constants.CONTACT) String contact,
                          @Field(Constants.ADDRESS) String address,
                          @Field(Constants.CITIZENSHIP)String citizenship,
                          @Field(Constants.OCCUPATION) String occupation,
                          @Field(Constants.GENDER) String gender,
                          @Field(Constants.CIVIL_STATUS)String civil);

    @Multipart
    @POST("upload.php?")
    Call<ResultResponse> uploadFile(@Part MultipartBody.Part file, @Part("name") RequestBody name);





    //Garage API

    @FormUrlEncoded
    @POST(Endpoints.GARAGE)
    Call<ResultResponse> registerCar(@Field(Constants.TAG) String tag,
                                     @Field(Constants.GARAGE_CHASSIS) String garage_chassis,
                                     @Field(Constants.GARAGE_MODEL) String garage_model,
                                     @Field(Constants.GARAGE_YEAR_MODEL) String garage_year,
                                     @Field(Constants.GARAGE_PLATE) String garage_plate,
                                     @Field(Constants.GARAGE_PURCHASE) String garage_purchase,
                                     @Field(Constants.USER_ID) String user_id,
                                     @Field(Constants.GARAGE_NAME) String garage_name
    );

    @FormUrlEncoded
    @POST(Endpoints.GARAGE)
    Call<ResultResponse> editCar(@Field(Constants.TAG) String tag,
                                     @Field(Constants.GARAGE_CHASSIS) String garage_chassis,
                                     @Field(Constants.GARAGE_MODEL) String garage_model,
                                     @Field(Constants.GARAGE_YEAR_MODEL) String garage_year,
                                     @Field(Constants.GARAGE_PLATE) String garage_plate,
                                     @Field(Constants.GARAGE_PURCHASE) String garage_purchase,
                                     @Field(Constants.GARAGE_ID) String car_id,
                                     @Field(Constants.GARAGE_NAME) String garage_name
    );


    @FormUrlEncoded
    @POST(Endpoints.GARAGE)
    Call<ResultResponse> deleteCar(@Field(Constants.TAG) String tag, @Field(Constants.GARAGE_ID) String garage_id,@Field(Constants.USER_ID) String user_id );



    @FormUrlEncoded
    @POST(Endpoints.GARAGE)
    Call<GarageListResponse> getGarageList(@Field(Constants.TAG) String tag, @Field(Constants.USER_ID) String garage_id );


    //DATA API
    @FormUrlEncoded
    @POST(Endpoints.DATA)
    Call<DealerListResponse> getDealerList(@Field(Constants.TAG) String tag, @Field(Constants.USER_ID) String dealer_id );

    @FormUrlEncoded
    @POST(Endpoints.DATA)
    Call<ServiceListResponse> getServiceList(@Field(Constants.TAG) String tag, @Field(Constants.USER_ID) String dealer_id );

    @FormUrlEncoded
    @POST(Endpoints.DATA)
    Call<CarListResponse> getCarList(@Field(Constants.TAG) String tag, @Field(Constants.USER_ID) String dealer_id );


    @FormUrlEncoded
    @POST(Endpoints.DATA)
    Call<PmsListResponse> getPMSList(@Field(Constants.TAG) String tag, @Field(Constants.USER_ID) String pms_id );


    @FormUrlEncoded
    @POST(Endpoints.DATA)
    Call<AdvisorListResponse> getAdvisorList(@Field(Constants.TAG) String tag, @Field(Constants.DEALER_ID) String advisor_id );

    @FormUrlEncoded
    @POST(Endpoints.DATA)
    Call<DealerContactListResponse> getDealerContactList(@Field(Constants.TAG) String tag, @Field(Constants.DEALER_ID) String advisor_id );


    //Schedule
    @FormUrlEncoded
    @POST(Endpoints.SCHEDULE)
    Call<ScheduleListResponse> getTimeslot(@Field(Constants.TAG) String tag,
                                           @Field(Constants.USER_ID) String user_id,
                                           @Field(Constants.DEALER_ID) String dealer_id ,
                                           @Field(Constants.DATE) String date_id  );

    @FormUrlEncoded
    @POST(Endpoints.SCHEDULE)
    Call<ResultResponse> registerReservation(@Field(Constants.TAG) String tag,
                                     @Field(Constants.USER_ID) String userID,
                                     @Field(Constants.GARAGE_ID) String garID,
                                     @Field(Constants.SCHEDULE_ID) String schedID,
                                     @Field(Constants.DEALER_ID) String dealerID,
                                     @Field(Constants.ADVISOR_ID) String advisorID,
                                     @Field(Constants.SERVICE_ID) String serviceID,
                                     @Field(Constants.PMS_SERVICE_ID) String pmsID,
                                             @Field(Constants.RESERVE_DATE) String date,
                                             @Field(Constants.RESERVE_REMARK) String remark

                                             );

    @FormUrlEncoded
    @POST(Endpoints.SCHEDULE)
    Call<AppointmentListResponse> getAppointmentList(@Field(Constants.TAG) String tag, @Field(Constants.USER_ID) String user_id );



    @FormUrlEncoded
    @POST(Endpoints.SCHEDULE)
    Call<PastAppointmentListResponse> getAppointmentListPast(@Field(Constants.TAG) String tag, @Field(Constants.USER_ID) String user_id );

    @FormUrlEncoded
    @POST(Endpoints.SCHEDULE)
    Call<ResultResponse> cancelReservation(@Field(Constants.TAG) String tag,
                                 @Field(Constants.RESERVATION_ID) String garage_name
    );

    @FormUrlEncoded
    @POST(Endpoints.SCHEDULE)
    Call<ResultResponse> reschedReservation(@Field(Constants.TAG) String tag,
                                           @Field(Constants.RESERVATION_ID) String garage_name,
                                            @Field(Constants.SCHEDULE_ID) String schedID,
                                            @Field(Constants.RESERVE_DATE) String date,
                                             @Field(Constants.GARAGE_ID) String garID
    );


    //Contact

    @FormUrlEncoded
    @POST(Endpoints.CONTACT_US)
    Call<ResultResponse> contactUs(@Field(Constants.TAG) String tag,
                                   @Field(Constants.CONTACT_METHOD) String contact_method,
                                   @Field(Constants.FIRST_NAME)String firstName,
                                   @Field(Constants.LAST_NAME)String lname,
                                   @Field(Constants.EMAIL) String email,
                                   @Field(Constants.CONTACT) String contact,
                                   @Field(Constants.CONCERN) String concern);

    @FormUrlEncoded
    @POST(Endpoints.CONTACT_US)
    Call<ResultResponse> testDrive(@Field(Constants.TAG) String tag,
                                   @Field(Constants.DEALER_ID) String did,
                                   @Field(Constants.CONTACT_METHOD) String contact_method,
                                   @Field(Constants.CAR_MODEL)String car_model,
                                   @Field(Constants.DEALER_NAME)String dealername,
                                   @Field(Constants.FIRST_NAME)String firstName,
                                   @Field(Constants.LAST_NAME)String lname,
                                   @Field(Constants.EMAIL) String email,
                                   @Field(Constants.CONTACT) String contact,
                                   @Field(Constants.CONCERN) String concern);

    @FormUrlEncoded
    @POST(Endpoints.CONTACT_US)
    Call<ResultResponse> partsInquire(@Field(Constants.TAG) String tag,
                                   @Field(Constants.DEALER_ID) String did,
                                   @Field(Constants.CONTACT_METHOD) String contact_method,
                                   @Field(Constants.CAR_MODEL)String car_model,
                                      @Field(Constants.YEAR_MODEL)String year_model,
                                      @Field(Constants.PLATE_NUM)String plate_num,
                                      @Field(Constants.CHASSIS_NUM)String chassis_num,
                                   @Field(Constants.DEALER_NAME)String dealer_name,
                                   @Field(Constants.FIRST_NAME)String firstName,
                                   @Field(Constants.LAST_NAME)String lname,
                                   @Field(Constants.EMAIL) String email,
                                   @Field(Constants.CONTACT) String contact,
                                   @Field(Constants.CONCERN) String concern);

}
