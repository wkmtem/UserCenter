package com.compass.examination.core.dao.mapper;

import com.compass.examination.pojo.po.Tenant;
import com.compass.examination.pojo.po.TenantExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TenantMapper {
    int countByExample(TenantExample example);

    int deleteByExample(TenantExample example);

    int deleteByPrimaryKey(String id);

    int insert(Tenant record);

    int insertSelective(Tenant record);

    List<Tenant> selectByExample(TenantExample example);

    Tenant selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") Tenant record, @Param("example") TenantExample example);

    int updateByExample(@Param("record") Tenant record, @Param("example") TenantExample example);

    int updateByPrimaryKeySelective(Tenant record);

    int updateByPrimaryKey(Tenant record);
}