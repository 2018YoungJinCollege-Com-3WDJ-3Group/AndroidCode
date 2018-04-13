package com.example.asd76.okonomiorgel.Response;

import com.example.asd76.okonomiorgel.Item.SheetBoardListItem;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by asd76 on 2018-03-16.
 */

public interface OkonomiOrgelService {

    //회원가입
    @FormUrlEncoded
    @POST("/register")
    Call<Register> signUp(@Field("name") String id, @Field("email") String email, @Field("password") String pw);

    //아이디 중복 체크
    @FormUrlEncoded
    @POST("/Mcheckname")
    Call<Check> duplicateCheck(@Field("user_id") String id);

    //로그인
    @FormUrlEncoded
    @POST("/Mlogin")
    Call<User_info> login(@Field("name") String id, @Field("password") String password);

    //악보 게시판 리스트
    @GET("/Mshare")
    Call<ArrayList<SheetBoardListItem>> getSheetBoardList(@Query("pageid") int listStartNum);

    //악보 게시판 게시글
    @FormUrlEncoded
    @POST("/Mshare/show")
    Call<ArrayList<Post>> getSheetBoardPost(@Field("post_id") int post_id);

    @FormUrlEncoded
    @POST("/MshareScore")
    Call<ArrayList<Sheet>> getSheetBoardSheet(@Field("score_id") int score_id);

    //악보 구매
    @FormUrlEncoded
    @POST("/Mpurchase")
    Call<purchaseCheck> sheetPurchase(@Field("user_id") int user_id, @Field("score_id") int score_id);

    @FormUrlEncoded
    @POST("/MpurchaseSheet")
    Call<ArrayList<Sheet>> getPurchaseSheet(@Field("user_id") int user_id);

    @FormUrlEncoded
    @POST("/MretainSheet")
    Call<ArrayList<Sheet>> getWroteSheet(@Field("user_id") int user_id);
}
