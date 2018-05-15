package com.example.asd76.okonomiorgel.Response;

import com.example.asd76.okonomiorgel.Item.SheetBoardListItem;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by asd76 on 2018-03-16.
 */

public interface OkonomiOrgelService {

    //회원가입
    @FormUrlEncoded
    @POST("/Mregister")
    Call<Register_info> signUp(@Field("name") String id, @Field("email") String email, @Field("password") String pw);

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
    Call<Post> getSheetBoardPost(@Field("post_id") int post_id);

    @FormUrlEncoded
    @POST("/MshareScore")
    Call<ArrayList<Score_info>> getSheetBoardSheet(@Field("score_id") int score_id);

    //악보 구매
    @FormUrlEncoded
    @POST("/Mpurchase")
    Call<purchaseCheck> sheetPurchase(@Field("user_id") int user_id, @Field("score_id") int score_id, @Field("post_id") int post_id);

    @FormUrlEncoded
    @POST("/MpurchaseList")
    Call<ArrayList<Score_info>> getPurchaseSheet(@Field("user_id") int user_id);

    @FormUrlEncoded
    @POST("/MretainSheet")
    Call<ArrayList<Score_info>> getWroteSheet(@Field("user_id") int user_id);

    //구매내역
    @FormUrlEncoded
    @POST("/history")
    Call<ArrayList<Purchase_history>> getHistory(@Field("user_id") int user_id);

    @FormUrlEncoded
    @POST("/like")
    Call<ArrayList<Like_record>> getLikeRecord(@Field("user_id") int user_id);

    //악보 편집
    @FormUrlEncoded
    @POST("/???")
    Call<Check> editSheet(@Field("user_id") int user_id, @Field("scoreString") String scoreString, @Field("socre_id") int score_id);

    //악보 생성
    @FormUrlEncoded
    @POST()
    Call<Check> createSheet(@Field("user_id") int user_id);

}
