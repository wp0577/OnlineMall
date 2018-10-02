package com.wp.search.service.message;

import com.wp.search.service.SearchItemService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * @program: WpMall
 * @description:
 * @author: Pan wu
 * @create: 2018-10-02 16:56
 **/
public class ItemAddMessageListener implements MessageListener {

    @Autowired
    private SearchItemService searchService;

    @Override
    public void onMessage(Message message) {
        //取消息内容
        try {
            TextMessage textMessage = (TextMessage) message;
            String text = textMessage.getText();
            //employ api to add new item to index
            searchService.addDocument(Long.parseLong(text));

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
