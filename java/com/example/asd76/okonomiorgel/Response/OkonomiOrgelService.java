package com.example.asd76.okonomiorgel.Response;

import com.example.asd76.okonomiorgel.Item.SheetBoardItem;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by asd76 on 2018-03-16.
 */

public interface OkonomiOrgelService {

    //회원가입
    @FormUrlEncoded
    @POST("/Mregister")
    Call<RegisterInfo> signUp(@Field("name") String id, @Field("email") String email, @Field("password") String pw);

    //아이디 중복 체크
    @FormUrlEncoded
    @POST("/Mcheckname")
    Call<CheckBoolean> duplicateCheck(@Field("user_name") String id);

    //로그인
    @FormUrlEncoded
    @POST("/Mlogin")
    Call<LoginInfo> login(@Field("name") String id, @Field("password") String password);

    //악보 게시판 리스트
    @GET("/Mshare")
    Call<ArrayList<SheetBoardItem>> getSheetBoardList(@Query("pageid") int listStartNum);

    //악보 게시판 게시글
    @FormUrlEncoded
    @POST("/Mshare/show")
    Call<ScoreBoard_post> getSheetBoardPost(@Field("post_id") int post_id, @Field("user_id") int user_id);

    //악보 구매
    @FormUrlEncoded
    @POST("/Mpurchase")
    Call<CheckString> sheetPurchase(@Field("user_id") int user_id, @Field("score_id") int score_id, @Field("post_id") int post_id);

    @FormUrlEncoded
    @POST("/MpurchaseList")
    Call<ArrayList<ScoreInfo>> getPurchaseSheet(@Field("user_id") int user_id);

    @FormUrlEncoded
    @POST("/MretainSheet")
    Call<ArrayList<ScoreInfo>> getWroteSheet(@Field("user_id") int user_id);

    //구매내역
    @FormUrlEncoded
    @POST("/MorderList")
    Call<ArrayList<PurchaseRecords>> getPurchaseRecords(@Field("user_id") int user_id);

    @FormUrlEncoded
    @POST("/MfavoriteList")
    Call<ArrayList<LikeRecord>> getLikeRecords(@Field("user_id") int user_id);

    //악보 편집
    @FormUrlEncoded
    @POST("/MupdateScore")
    Call<CheckBoolean> editSheet(@Field("user_id") int user_id, @Field("score_string") String scoreString, @Field("score_id") int score_id);

    @FormUrlEncoded
    @POST("/MlikePush")
    Call<pushLike> likePush(@Field("user_id") int user_id, @Field("post_id") int post_id);

    @POST("/Mranking")
    Call<ArrayList<RankingRecord>> getRankingRecords();

    @GET("/Mpost")
    Call<ArrayList<FreeBoardRecord>> getFreeBoardRecords();

    @FormUrlEncoded
    @POST("/Mpost/show")
    Call<FreeBoardRecord> getFreeBoardPost(@Field("post_id") int post_id);

    //주소로부터 이미지 획득
    @GET
    Call<ResponseBody> getImage(@Url String url);
}
