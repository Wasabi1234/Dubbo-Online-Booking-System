package com.javaedge.guns.modular.system.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.javaedge.guns.core.base.controller.BaseController;
import com.javaedge.guns.core.common.annotion.BussinessLog;
import com.javaedge.guns.core.common.annotion.Permission;
import com.javaedge.guns.core.common.constant.Const;
import com.javaedge.guns.core.common.constant.dictmap.DictMap;
import com.javaedge.guns.core.common.constant.factory.ConstantFactory;
import com.javaedge.guns.core.common.exception.BizExceptionEnum;
import com.javaedge.guns.core.exception.GunsException;
import com.javaedge.guns.core.log.LogObjectHolder;
import com.javaedge.guns.core.util.ToolUtil;
import com.javaedge.guns.modular.system.model.Dict;
import com.javaedge.guns.modular.system.service.IDictService;
import com.javaedge.guns.modular.system.warpper.DictWarpper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * 字典控制器
 *
 * @author JavaEdge
 * @Date 2017年4月26日 12:55:31
 */
@Controller
@RequestMapping("/dict")
public class DictController extends BaseController {

    private String PREFIX = "/system/dict/";

    @Autowired
    private IDictService dictService;

    /**
     * 跳转到字典管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "dict.html";
    }

    /**
     * 跳转到添加字典
     */
    @RequestMapping("/dict_add")
    public String deptAdd() {
        return PREFIX + "dict_add.html";
    }

    /**
     * 跳转到修改字典
     */
    @Permission(Const.ADMIN_NAME)
    @RequestMapping("/dict_edit/{dictId}")
    public String deptUpdate(@PathVariable Integer dictId, Model model) {
        Dict dict = dictService.selectById(dictId);
        model.addAttribute("dict", dict);
        List<Dict> subDicts = dictService.selectList(new EntityWrapper<Dict>().eq("pid", dictId));
        model.addAttribute("subDicts", subDicts);
        LogObjectHolder.me().set(dict);
        return PREFIX + "dict_edit.html";
    }

    /**
     * 新增字典
     *
     * @param dictValues 格式例如   "1:启用;2:禁用;3:冻结"
     */
    @BussinessLog(value = "添加字典记录", key = "dictName,dictValues", dict = DictMap.class)
    @RequestMapping(value = "/add")
    @Permission(Const.ADMIN_NAME)
    @ResponseBody
    public Object add(String dictCode,String dictTips,String dictName, String dictValues) {
        if (ToolUtil.isOneEmpty(dictCode,dictName, dictValues)) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        this.dictService.addDict(dictCode,dictName,dictTips,dictValues);
        return SUCCESS_TIP;
    }

    /**
     * 获取所有字典列表
     */
    @RequestMapping(value = "/list")
    @Permission(Const.ADMIN_NAME)
    @ResponseBody
    public Object list(String condition) {
        List<Map<String, Object>> list = this.dictService.list(condition);
        return super.warpObject(new DictWarpper(list));
    }

    /**
     * 字典详情
     */
    @RequestMapping(value = "/detail/{dictId}")
    @Permission(Const.ADMIN_NAME)
    @ResponseBody
    public Object detail(@PathVariable("dictId") Integer dictId) {
        return dictService.selectById(dictId);
    }

    /**
     * 修改字典
     */
    @BussinessLog(value = "修改字典", key = "dictName,dictValues", dict = DictMap.class)
    @RequestMapping(value = "/update")
    @Permission(Const.ADMIN_NAME)
    @ResponseBody
    public Object update(Integer dictId,String dictCode,String dictName, String dictTips,String dictValues) {
        if (ToolUtil.isOneEmpty(dictId, dictCode, dictName, dictValues)) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        dictService.editDict(dictId, dictCode,dictName, dictTips,dictValues);
        return SUCCESS_TIP;
    }

    /**
     * 删除字典记录
     */
    @BussinessLog(value = "删除字典记录", key = "dictId", dict = DictMap.class)
    @RequestMapping(value = "/delete")
    @Permission(Const.ADMIN_NAME)
    @ResponseBody
    public Object delete(@RequestParam Integer dictId) {

        //缓存被删除的名称
        LogObjectHolder.me().set(ConstantFactory.me().getDictName(dictId));

        this.dictService.delteDict(dictId);
        return SUCCESS_TIP;
    }

}
