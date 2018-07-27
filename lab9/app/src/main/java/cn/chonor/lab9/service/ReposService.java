package cn.chonor.lab9.service;

import java.util.ArrayList;

import cn.chonor.lab9.model.Repositories;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Chonor on 2017/12/23.
 */

public interface ReposService {
    @GET("/users/{user}/repos")
    Observable<ArrayList<Repositories>> getRepos(@Path("user") String user);
}
