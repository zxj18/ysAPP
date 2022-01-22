package com.vodbyte.movie.manager;

public interface AndroidDownloadManagerListener {
    void onPrepare();
    void onSuccess(String path);
    void onFailed(Throwable throwable);
}
