package com.niit.vhr.controller.per;

import com.niit.vhr.model.Ec;
import com.niit.vhr.model.RespBean;
import com.niit.vhr.model.RespPageBean;
import com.niit.vhr.model.Sector;
import com.niit.vhr.service.per.EcService;
import com.niit.vhr.service.per.SectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/personnel/ec")
public class EcController {
    @Autowired
    EcService ecService;
    @Autowired
    SectorService sectorService;
    @GetMapping("/")
    public RespPageBean getEcByPage(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size, Ec ec) {
        return ecService.getEcByPage(page, size, ec);
    }
    @GetMapping("/ec")
    public List<Ec> getAllEc() {
        return ecService.getAllEc();
    }
    @GetMapping("/sector")
    public List<Sector> getAllSector() {
        return sectorService.getAllSector();}
    @PostMapping("/")
    public RespBean addEc(@RequestBody Ec ec) {
        if (ecService.addEc(ec) == 1) {
            return RespBean.ok("添加成功!");
        }
        return RespBean.error("添加失败!");
    }
    @DeleteMapping("/{id}")
    public RespBean deleteEcById(@PathVariable Integer id) {
        if (ecService.deleteEcById(id) == 1) {
            return RespBean.ok("删除成功!");
        }
        return RespBean.error("删除失败!");
    }
    @PutMapping("/")
    public RespBean updateEc(@RequestBody Ec ec) {
        if (ecService.updateEc(ec) == 1) {
            return RespBean.ok("更新成功!");
        }
        return RespBean.error("更新失败!");
    }
}
