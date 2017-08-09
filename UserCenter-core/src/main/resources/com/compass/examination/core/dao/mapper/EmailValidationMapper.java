package com.compass.examination.core.dao.mapper;

import com.compass.examination.pojo.po.EmailValidation;
import com.compass.examination.pojo.po.EmailValidationExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface EmailValidationMapper {
    int countByExample(EmailValidationExample example);

    int deleteByExample(EmailValidationExample example);

    int deleteByPrimaryKey(Long id);

    int insert(EmailValidation record);

    int insertSelective(EmailValidation record);

    List<EmailValidation> selectByExample(EmailValidationExample example);

    EmailValidation selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") EmailValidation record, @Param("example") EmailValidationExample example);

    int updateByExample(@Param("record") EmailValidation record, @Param("example") EmailValidationExample example);

    int updateByPrimaryKeySelective(EmailValidation record);

    int updateByPrimaryKey(EmailValidation record);
}