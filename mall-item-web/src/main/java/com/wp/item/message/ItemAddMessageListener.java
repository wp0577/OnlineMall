package com.wp.item.message;

import com.wp.item.pojo.Item;
import com.wp.pojo.TbItem;
import com.wp.pojo.TbItemDesc;
import com.wp.service.ItemService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: WpMall
 * @description:
 * @author: Pan wu
 * @create: 2018-10-02 16:56
 **/
public class ItemAddMessageListener implements MessageListener {


    @Autowired
    private FreeMarkerConfigurer freeMarkerConfig;
    @Autowired
    private ItemService itemService;


    @Override
    public void onMessage(Message message) {
        //if add item, we will listen and build static html file by freemarker
        System.out.println("-------now create static html file now by freemarker--------");
        try {
            //get id from message
            TextMessage textMessage = (TextMessage) message;
            String id = textMessage.getText();
            //get item and itemDesc by id
            TbItem itemById = itemService.getItemById(Long.valueOf(id));
            TbItemDesc itemDescById = itemService.getItemDescById(Long.parseLong(id));
            Item item = new Item(itemById);
            //从spring容器中获得FreeMarkerConfigurer对象。
            //从FreeMarkerConfigurer对象中获得Configuration对象。
            Configuration configuration = freeMarkerConfig.getConfiguration();
            //使用Configuration对象获得Template对象。
            Template template = configuration.getTemplate("item.ftl");
            //创建数据集
            Map map = new HashMap();
            map.put("item", item);
            map.put("itemDesc", itemDescById);
            //创建输出文件的Writer对象。
            Writer out = new FileWriter(new File("/Users/wupan/Documents/workspace/wpmall-ftl/item/"+id+".html"));
            //调用模板对象的process方法，生成文件。
            template.process(map, out);
            //关闭流。
            out.close();
            System.out.println("-------create static html file now by freemarker successd--------");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("-------create static html file failed--------");
        }

    }
}
