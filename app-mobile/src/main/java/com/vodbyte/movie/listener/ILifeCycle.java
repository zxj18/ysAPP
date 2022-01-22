package com.vodbyte.movie.listener;


public interface ILifeCycle {
    void onCreate();

    void onReStart();

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onDestroy();
}
