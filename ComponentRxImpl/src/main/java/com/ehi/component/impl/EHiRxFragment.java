package com.ehi.component.impl;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.ehi.component.error.IntentResultException;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.SingleEmitter;

/**
 * 跳转界面拿数据结合 RxJava2 的 Fragment
 * <p>
 * time   : 2018/11/03
 *
 * @author : xiaojinzi 30212
 * @hide
 */
public final class EHiRxFragment extends Fragment {

    @NonNull
    private Map<Integer, SingleEmitter<Intent>> singleEmitterMap = new HashMap<>();

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 根据 requestCode 获取发射器
        SingleEmitter<Intent> singleEmitter = singleEmitterMap.get(requestCode);

        if (singleEmitter != null) {
            if (!singleEmitter.isDisposed()) {
                if (data == null) {
                    singleEmitter.onError(new IntentResultException("the result data is null"));
                } else {
                    singleEmitter.onSuccess(data);
                }
            }
        }
        singleEmitterMap.remove(requestCode);
    }

    public boolean isContainsSingleEmitter(int requestCode) {
        return singleEmitterMap.containsKey(requestCode);
    }

    public void setSingleEmitter(@NonNull SingleEmitter<Intent> singleEmitter, @NonNull int requestCode) throws IntentResultException {

        if (isContainsSingleEmitter(requestCode)) {
            throw new IntentResultException("request&result code: " + requestCode + " can't be same");
        }
        singleEmitterMap.put(requestCode, singleEmitter);
    }

}