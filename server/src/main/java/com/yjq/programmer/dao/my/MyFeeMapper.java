package com.yjq.programmer.dao.my;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2024-06-12 18:09
 */
public interface MyFeeMapper {

    // 根据时间范围和状态获取缴费数据
    Integer getFeeTotalByDateAndState(@Param("queryMap") Map<String, Object> queryMap, @Param("stateList") List<Integer> stateList);

}
