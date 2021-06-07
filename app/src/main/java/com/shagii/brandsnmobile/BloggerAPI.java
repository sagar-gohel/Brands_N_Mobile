package com.shagii.brandsnmobile;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class BloggerAPI {

    private static final String key = "AIzaSyCEQ663mPKECvZRpWIPmP-asTblCr_Cyog";
    private static final String url = "https://www.googleapis.com/blogger/v3/blogs/8772673180280332178/posts/";

    public static PostService postService = null;

    public static PostService getService()
    {
        //it is Single turn pattern for One time run
        if(postService == null)
        {
            //Create Object only one time
            Retrofit retrofit = new  Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            postService =  retrofit.create(PostService.class);

        }
        return postService;
    }

    public interface PostService {
        @GET("?key=" + key)
        Call<PostList> getPostList();

    }
}
