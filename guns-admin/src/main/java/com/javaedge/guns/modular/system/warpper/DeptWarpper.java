package com.javaedge.guns.modular.system.warpper;

import com.javaedge.guns.core.common.constant.factory.ConstantFactory;
import com.javaedge.guns.core.base.warpper.BaseControllerWarpper;
import com.javaedge.guns.core.util.ToolUtil;

import java.util.Map;

/**
 * 部门列表的包装
 *
 * @author JavaEdge
 * @date 2017年4月25日 18:10:31
 */
public class DeptWarpper extends BaseControllerWarpper {

    public DeptWarpper(Object list) {
        super(list);
    }

    @Override
    public void warpTheMap(Map<String, Object> map) {

        Integer pid = (Integer) map.get("pid");

        if (ToolUtil.isEmpty(pid) || pid.equals(0)) {
            map.put("pName", "--");
        } else {
            map.put("pName", ConstantFactory.me().getDeptName(pid));
        }
    }

}
