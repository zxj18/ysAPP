package com.vodbyte.movie.mvp.presenter;

import android.util.Log;

import androidx.annotation.MainThread;

import com.vodbyte.movie.base.BasePresenter;
import com.vodbyte.movie.bean.VodBean;
import com.vodbyte.movie.mvp.contract.IHistoryContract;
import com.vodbyte.movie.mvp.model.db.CollectBean;
import com.vodbyte.movie.mvp.model.db.HistoryBean;
import com.vodbyte.movie.mvp.model.dto.PageDTO;
import com.vodbyte.movie.mvp.model.vo.HistoryVO;

import org.litepal.LitePal;
import org.litepal.crud.callback.CountCallback;
import org.litepal.crud.callback.FindMultiCallback;
import org.litepal.crud.callback.UpdateOrDeleteCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

public class HistoryPresenter extends BasePresenter<IHistoryContract.View>
        implements IHistoryContract.Presenter {

    private List<HistoryVO> mNewBookList;
    private List<HistoryVO> mOldBookList;
    private PageDTO mPage;
    private int mTotalPage;
    private final int PAGE_SIZE = 30;
    private boolean isStartEdit;


    public HistoryPresenter(){
        mNewBookList = new ArrayList<>();
        mOldBookList = new ArrayList<>();
    }

    @MainThread
    @Override
    public void refresh() {
        LitePal.countAsync(HistoryBean.class)
                .listen(new CountCallback() {
                    @Override
                    public void onFinish(int count) {
                        mTotalPage = count / PAGE_SIZE;
                        if (mTotalPage * PAGE_SIZE < count){
                            mTotalPage++;
                        }
                        mPage = new PageDTO(mTotalPage,0);
                        getHistoryData();
                    }
                });
    }

    @Override
    public void getHistoryData() {
        if (mPage.hasMore()){
            // 删除数据时只会删除HistoryBean表中的数据而不会删除BookBean表中的数据
            // 不然在删除历史记录时把可能会把收藏的小说删除导致收藏丢失

            // 1.先向HistoryBean表查询历史小说的ID
            LitePal.limit(PAGE_SIZE)
                    .offset(PAGE_SIZE * mPage.getCurrentPage())
                    .order("time desc")
                    .findAsync(HistoryBean.class)
                    .listen(new FindMultiCallback<HistoryBean>() {
                @Override
                public void onFinish(List<HistoryBean> list) {

                    mOldBookList.clear();
                    mOldBookList.addAll(mNewBookList);
                    if (mPage.getCurrentPage() == 0){
                        //第一页，刷新
                        mNewBookList = new ArrayList<>();
                    }

                    for (int i = 0; i < list.size(); i++) {
                        VodBean vodBean = LitePal.where("vid = ?",list.get(i).getvodPrimaryKey() + "").findFirst(VodBean.class);
                        if (vodBean != null) {
                            mNewBookList.add(new HistoryVO(vodBean));
                        }
                    }

                    mPage.setCurrentPage(mPage.getCurrentPage() + 1);
                    if (isAttachView()){
                        getMVPView().setHistoryData(mOldBookList, mNewBookList);
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
        ListIterator<HistoryVO> listIterator = mNewBookList.listIterator();
        while (listIterator.hasNext()){
            HistoryVO c = listIterator.next().clone();
            c.setStartSelect(true);
            listIterator.set(c);
        }
        if (isAttachView()){
            getMVPView().setHistoryData(mOldBookList, mNewBookList);
        }
    }

    @Override
    public void endEdit() {
        isStartEdit = false;
        refresh();
    }

    @Override
    public void deleteBooks() {
        List<HistoryVO> temp = new ArrayList<>();
        mOldBookList.clear();
        mOldBookList.addAll(mNewBookList);
        for (HistoryVO c : mNewBookList) {
            if (c.isSelect()) {
                temp.add(c);
                c.getVodBean().deleteAsync().listen(new UpdateOrDeleteCallback() {
                    @Override
                    public void onFinish(int rowsAffected) {
                        LitePal.where("vodPrimaryKey = ?",String.valueOf(c.getVodBean().getVid()))
                                .findFirst(HistoryBean.class).delete();
                    }
                });
            }
        }
        mNewBookList.removeAll(temp);
        if (isAttachView()){
            getMVPView().setHistoryData(mOldBookList, mNewBookList);
        }
    }

    @Override
    public void selectAll(boolean isSelect) {
        mOldBookList.clear();
        mOldBookList.addAll(mNewBookList);
        ListIterator<HistoryVO> listIterator = mNewBookList.listIterator();
        while (listIterator.hasNext()){
            HistoryVO c = listIterator.next().clone();
            c.setSelect(isSelect);
            listIterator.set(c);
        }
        if (isAttachView()){
            getMVPView().setHistoryData(mOldBookList, mNewBookList);
        }
    }


}
