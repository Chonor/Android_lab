package cn.chonor.lab9.service;



import java.util.ArrayList;

import cn.chonor.lab9.model.Github;
import cn.chonor.lab9.model.Repositories;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;



/**
 * Created by Chonor on 2017/12/22.
 */

public interface GithubService {
    @GET("/users/{user}")
    Observable<Github> getUser(@Path("user") String user);
}

