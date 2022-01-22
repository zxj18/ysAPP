package com.vodbyte.movie.mvp.model;

import com.vodbyte.movie.mvp.model.vo.CollectVO;

import java.util.List;

import io.reactivex.Observer;

public class CollectModel {
    public void updateAll(List<CollectVO> list, Observer<CollectVO> observer) {
//        Observable.fromIterable(list)
//                .map(new Function<CollectVO, ComicDTO>() {
//                    @Override
//                    public ComicDTO apply(CollectVO collectVO) throws Exception {
//                        Observable<ComicDTO> observable = RetrofitUtil.bind(Constant.BASE_URL_WEB_JSON)
//                                .create(ComicService.class)
//                                .getComic(collectVO.getBookBean().getBookId());
//                        return observable.blockingFirst();
//                    }
//                }).map(new Function<ComicDTO, CollectVO>() {
//                    @Override
//                    public CollectVO apply(ComicDTO comicDTO) throws Exception {
//                        try {
//                            if (comicDTO.isSuccess()){
//                                ChapterBean netRecentChapter = comicDTO.toLastChapter();
//                                VodBean vodBean = LitePal
//                                        .where("bookId = ?",String.valueOf(comicDTO.getData().get(0).getComicid()))
//                                        .findFirst(VodBean.class);
//                                //本地最新话ID
//                              //  long localRecentChapterId = vodBean.getRecentChapterId();
//                                //网络最新话
////                                if (netRecentChapter.getChapterId() > localRecentChapterId){
////                                    //有更新
//////                                    vodBean.setUpdate(true);
//////                                    vodBean.setRecentChapter(netRecentChapter.getChapterNum());
//////                                    vodBean.setRecentChapterId(netRecentChapter.getChapterId());
////                                    vodBean.update(vodBean.getPrimaryKey());
////                                }
//                                return new CollectVO(vodBean);
//                            }else {
//                                throw new Exception("ComicInfoDTO not success !");
//                            }
//                        }catch (Exception e){
//                            e.printStackTrace();
//                            throw e;
//                        }
//                    }
//                }).subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .unsubscribeOn(Schedulers.io())
//                        .subscribe(observer);
    }
}
