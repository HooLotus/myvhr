package com.niit.vhr.service.per;

import com.niit.vhr.mapper.SectorMapper;
import com.niit.vhr.model.Nation;
import com.niit.vhr.model.Sector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SectorService {
    @Autowired
    SectorMapper sectorMapper;
    public List<Sector> getAllSector() {
        return sectorMapper.getAllSector();
    }
}
