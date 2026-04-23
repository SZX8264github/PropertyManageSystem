package com.yjq.programmer.dao;

import com.yjq.programmer.domain.Fee;
import com.yjq.programmer.domain.FeeExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FeeMapper {
    int countByExample(FeeExample example);

    int deleteByExample(FeeExample example);

    int deleteByPrimaryKey(String id);

    int insert(Fee record);

    int insertSelective(Fee record);

    List<Fee> selectByExample(FeeExample example);

    Fee selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") Fee record, @Param("example") FeeExample example);

    int updateByExample(@Param("record") Fee record, @Param("example") FeeExample example);

    int updateByPrimaryKeySelective(Fee record);

    int updateByPrimaryKey(Fee record);
}
