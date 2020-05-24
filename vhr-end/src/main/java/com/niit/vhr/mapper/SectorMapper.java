package com.niit.vhr.mapper;


import com.niit.vhr.model.Sector;

import java.util.List;

public interface SectorMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Sector record);

    int insertSelective(Sector record);

    Sector selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Sector record);

    int updateByPrimaryKey(Sector record);

    List<Sector> getAllSector();
}