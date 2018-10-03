package com.wp.item.pojo;

import com.wp.pojo.TbItem;
import org.apache.commons.lang3.StringUtils;

public class Item extends TbItem {


    public Item() {
    }

    public Item(TbItem tbItem) {
        this.setBarcode(tbItem.getBarcode());
        this.setCid(tbItem.getCid());
        this.setCreated(tbItem.getCreated());
        this.setId(tbItem.getId());
        this.setImage(tbItem.getImage());
        this.setNum(tbItem.getNum());
        this.setPrice(tbItem.getPrice());
        this.setSellPoint(tbItem.getSellPoint());
        this.setStatus(tbItem.getStatus());
        this.setTitle(tbItem.getTitle());
        this.setUpdated(tbItem.getUpdated());
    }

    public String[] getImages() {
        String image = this.getImage();
        if(image !=null && StringUtils.isNotBlank(image)) {
            String[] split = image.split(",");
            return split;
        }
        return null;
    }

}
