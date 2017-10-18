package lab.s2jh.aud.web;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.util.JsonUtils;
import lab.s2jh.module.sys.entity.DataDict;
import lab.s2jh.module.sys.service.DataDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * 数据字典接口
 * Created by zhuhui on 17-6-28.
 */
@Controller
@RequestMapping(value = "/pub/data")
public class DataController {

    @Autowired
    private DataDictService dataDictService;

    public DataController() {
    }

//    @MetaData("枚举数据集合")
//    @RequestMapping(value = "/enums")
//    public HttpHeaders enums() {
//        ServletContext sc = ServletActionContext.getServletContext();
//        this.setModel(sc.getAttribute("enums"));
//        return this.buildDefaultHttpHeaders();
//    }

    @MetaData("数据字典数据集合")
    @RequestMapping(value = "/dictDatas")
    @ResponseBody
    public String dictDatas() {
        ArrayList datas = Lists.newArrayList();
        List dataDicts = this.dataDictService.findAllCached();

        HashMap data;
        for (Iterator var3 = dataDicts.iterator(); var3.hasNext(); datas.add(data)) {
            DataDict dataDict = (DataDict) var3.next();
            data = Maps.newHashMap();
            data.put("primaryKey", dataDict.getPrimaryKey());
            data.put("primaryValue", dataDict.getPrimaryValue());
            DataDict parent = dataDict.getParent();
            if (parent != null) {
                data.put("parentPrimaryKey", parent.getPrimaryKey());
            }
        }
        return JsonUtils.writeValueAsString(datas);
    }
}