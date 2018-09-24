package com.wp.common;

import java.io.Serializable;
import java.util.List;

/**
 * @program: WpMall
 * @description: return page as json obejct to front-end
 * @author: Pan wu
 * @create: 2018-09-24 01:07
 **/
public class PageResult implements Serializable {

    /*Easyui中datagrid控件要求的数据格式为：
    {total:”2”,rows:[{“id”:”1”,”name”:”张三”},{“id”:”2”,”name”:”李四”}]}
    */
    private Integer total;
    private List rows;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List getRows() {
        return rows;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }
}

