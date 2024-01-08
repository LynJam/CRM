package cn.edu.scnu.service;

import cn.edu.scnu.pojo.Message;
import cn.edu.scnu.pojo.Timeline;
import cn.edu.scnu.vo.MessageVo;
import java.util.List;
import java.util.Map;
import org.bson.types.ObjectId;

public interface MessageService {

    public List<MessageVo> fetchRoomHistoricalMsg(String curOldestMsgId, String roomId, int size);

    public Map<String, List<MessageVo>> fetchUserNewMsg(String userId, String deviceType) ;

    Message sendMessage(Message msg, String roomId, List<String> userIds, String deviceType);
    public Message sendMessage(Message msg, String roomId, List<String> receiverIds);
}
