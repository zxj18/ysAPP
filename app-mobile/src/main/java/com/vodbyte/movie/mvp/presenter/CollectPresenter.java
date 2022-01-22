package com.vodbyte.movie.mvp.presenter;

import android.util.Log;

import androidx.annotation.MainThread;

import com.vodbyte.movie.base.BasePresenter;
import com.vodbyte.movie.bean.VodBean;
import com.vodbyte.movie.mvp.contract.ICollectContract;
import com.vodbyte.movie.mvp.contract.IHistoryContract;
import com.vodbyte.movie.mvp.model.db.CollectBean;
import com.vodbyte.movie.mvp.model.db.HistoryBean;
import com.vodbyte.movie.mvp.model.dto.PageDTO;
import com.vodbyte.movie.mvp.model.vo.CollectVO;
import com.vodbyte.movie.mvp.model.vo.HistoryVO;

import org.litepal.LitePal;
import org.litepal.crud.callback.CountCallback;
import org.litepal.crud.callback.FindMultiCallback;
import org.litepal.crud.callback.UpdateOrDeleteCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

public class CollectPresenter extends BasePresenter<ICollectContract.View>
        implements ICollectContract.Presenter {

    private List<CollectVO> mNewBookList;
    private List<CollectVO> mOldBookList;
    private PageDTO mPage;
    private int mTotalPage;
    private final int PAGE_SIZE = 30;
    private boolean isStartEdit;


    public CollectPresenter(){
        mNewBookList = new ArrayList<>();
        mOldBookList = new ArrayList<>();
    }

    @MainThread
    @Override
    public void refresh() {
        LitePal.countAsync(CollectBean.class)
                .listen(new CountCallback() {
                    @Override
                    public void onFinish(int count) {
                        mTotalPage = count / PAGE_SIZE;
                        if (mTotalPage * PAGE_SIZE < count){
                            mTotalPage++;
                        }
                        mPage = new PageDTO(mTotalPage,0);
                        getCollectData();
                    }
                });
    }

    @Override
    public void getCollectData() {
        if (mPage.hasMore()){
            // 删除数据时只会删除CollectBean表中的数据而不会删除BookBean表中的数据
            // 不然在删除历史记录时把可能会把收藏的小说删除导致收藏丢失

            // 1.先向CollectBean表查询历史小说的ID
            LitePal.limit(PAGE_SIZE)
                    .offset(PAGE_SIZE * mPage.getCurrentPage())
                    .order("time desc")
                    .findAsync(CollectBean.class)
                    .listen(new FindMultiCallback<CollectBean>() {
                @Override
                public void onFinish(List<CollectBean> list) {

                    mOldBookList.clear();
                    mOldBookList.addAll(mNewBookList);
                    if (mPage.getCurrentPage() == 0){
                        //第一页，刷新
                        mNewBookList = new ArrayList<>();
                    }

                    for (int i = 0; i < list.size(); i++) {
                        VodBean vodBean = LitePal.where("vid = ?",list.get(i).getVodPrimaryKey() + "").findFirst(VodBean.class);
                        if (vodBean != null) {
                            mNewBookList.add(new CollectVO(vodBean));
                        }
                    }

                    mPage.setCurrentPage(mPage.getCurrentPage() + 1);
                    if (isAttachView()){
                        getMVPView().setCollectData(mOldBookList, mNewBookList);
                        if (list.size() < PAGE_SIZE){
                            getMVPView().noMore();
                        }
                        if (isStartEdit){
                            //此时正在编辑
                            startEdit();
                        }
                    }

                }
            });
        }else {
            getMVPView().noMore();
        }

    }

    @Override
    public void startEdit() {
        isStartEdit = true;
        mOldBookList.clear();
        mOldBookList.addAll(mNewBookList);
        ListIterator<CollectVO> listIterator = mNewBookList.listIterator();
        while (listIterator.hasNext()){
            CollectVO c = listIterator.next().clone();
            c.setStartSelect(true);
            listIterator.set(c);
        }
        if (isAttachView()){
            getMVPView().setCollectData(mOldBookList, mNewBookList);
        }
    }

    @Override
    public void endEdit() {
        isStartEdit = false;
        refresh();
    }

    @Override
    public void deleteBooks() {
        List<CollectVO> temp = new ArrayList<>();
        mOldBookList.clear();
        mOldBookList.addAll(mNewBookList);
        for (CollectVO c : mNewBookList) {
            if (c.isSelect()) {
                temp.add(c);
                c.getVodBean().deleteAsync().listen(new UpdateOrDeleteCallback() {
                    @Override
                    public void onFinish(int rowsAffected) {
                        LitePal.where("vodPrimaryKey = ?",String.valueOf(c.getVodBean().getVid()))
                                .findFirst(CollectBean.class).delete();
                    }
                });
            }
        }
        mNewBookList.removeAll(temp);
        if (isAttachView()){
            getMVPView().setCollectData(mOldBookList, mNewBookList);
        }
    }

    @Override
    public void selectAll(boolean isSelect) {
        mOldBookList.clear();
        mOldBookList.addAll(mNewBookList);
        ListIterator<CollectVO> listIterator = mNewBookList.listIterator();
        while (listIterator.hasNext()){
            CollectVO c = listIterator.next().clone();
            c.setSelect(isSelect);
            listIterator.set(c);
        }
        if (isAttachView()){
            getMVPView().setCollectData(mOldBookList, mNewBookList);
        }
    }


}
