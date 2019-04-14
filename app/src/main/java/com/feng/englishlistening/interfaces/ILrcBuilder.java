package com.feng.englishlistening.interfaces;

import com.feng.englishlistening.impl.LrcRow;

import java.util.List;

/**
 * 解析歌词，得到LrcRow的集合
 */
public interface ILrcBuilder {
    /**
     * 解析歌词，得到LrcRow的集合
     * @param rawLrc lrc内容
     * @return LrcRow的集合
     */
    List<LrcRow> getLrcRows(String rawLrc);
}
