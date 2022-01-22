package com.vodbyte.movie.mvp.model.service;

import com.vodbyte.movie.config.Constant;
import com.vodbyte.movie.mvp.model.dto.HotKeyDTO;
import com.vodbyte.movie.mvp.model.dto.SearchDTO;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SearchService {
    @GET(Constant.SEARCH)
    Observable<SearchDTO> search(@Query("word") String word,
                                 @Query("page") int page);

    @GET(Constant.HOT_KEYS)
    Observable<HotKeyDTO> getHotKeys();
}
