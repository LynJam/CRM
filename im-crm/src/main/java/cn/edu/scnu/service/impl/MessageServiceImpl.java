package cn.edu.scnu.service.impl;

import cn.edu.scnu.pojo.Message;
import cn.edu.scnu.pojo.Timeline;
import cn.edu.scnu.pojo.TimelineSynchroNode;
import cn.edu.scnu.pojo.User;
import cn.edu.scnu.repository.MessageRepository;
import cn.edu.scnu.repository.TimelineRepository;
import cn.edu.scnu.repository.TimelineSynchroNodeRepository;
import cn.edu.scnu.repository.UserRepository;
import cn.edu.scnu.service.MessageService;
import cn.edu.scnu.util.SnowflakeDistributeId;
import cn.edu.scnu.vo.MessageVo;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private TimelineRepository timelineRepository;
    @Autowired
    private TimelineSynchroNodeRepository synchroNodeRepository;
    @Autowired
    private UserRepository userRepository;
    private Cache<String, User> userCache = Caffeine.newBuilder()
        .expireAfterWrite(10, TimeUnit.MINUTES)
        .maximumSize(1000)
        .build();

    @Override
    public List<MessageVo> fetchRoomHistoricalMsg(String curOldestMsgId, String roomId, int size) {
        List<Timeline> timelines;
        if ("-1".equals(curOldestMsgId)) { // 表示客户端 roomId 会话一条消息记录都没有
            timelines = timelineRepository.findHistoricalRecord(roomId, size);
            log.info(timelines.toString());
        } else {
            Timeline timeline = timelineRepository.findRoomTimelineByMsgId(curOldestMsgId, roomId);
            timelines = timelineRepository.findHistoricalRecord(timeline.getId(), roomId, size);
        }
        List<String> msgIds = timelines.stream()
            .map(Timeline::getMessageId)
            .collect(Collectors.toList());
        // TODO 替换 Message -> MessageVo, msg 的顺序需要考虑
        List<Message> msglist = messageRepository.findMsgIn(msgIds);
        List<MessageVo> res = of(msglist).stream()
            .sorted(Comparator.comparing(MessageVo::getMessageId))
            .collect(Collectors.toList());
        return res;
    }

    @Override
    public Map<String, List<MessageVo>> fetchUserNewMsg(String userId, String deviceType) {
        TimelineSynchroNode s = synchroNodeRepository.find(userId, deviceType);
        // 获取当前用户在该设备的同步节点, 若查询不到，表示第一次用该设备登录，需插入一条新的记录
        TimelineSynchroNode synchroNode = synchroNodeRepository.find(userId, deviceType);
        ObjectId lastTimelineId;
        // 到用户信箱中查找最新消息
        boolean newDevice = synchroNode == null || synchroNode.getLastTimelineId() == null;
        if (newDevice) {
            // 新设备，全拉
            lastTimelineId = new ObjectId(new Date(0));
        } else {
            lastTimelineId = synchroNode.getLastTimelineId();
        }
        List<Timeline> timeline = timelineRepository.findTimelinesGreaterThanId(lastTimelineId, userId);
        if (timeline.isEmpty()) {
            // 已经是最新消息了
            return null;
        }
        // 更新用户在该设备的同步节点(timelineId, timestamp)
        Timeline lastTimeline = timeline.get(timeline.size() - 1);
        if (newDevice) {
            synchroNodeRepository.save(userId, deviceType, lastTimeline.getId());
        } else {
            synchroNodeRepository.update(userId, deviceType, lastTimeline.getId());
        }

        // 映射对应的消息内容，并按房间分类，房间内消息按id升序
        List<String> msgIds = timeline.stream()
            .map(Timeline::getMessageId)
            .collect(Collectors.toList());
        List<Message> msglist = messageRepository.findMsgIn(msgIds);
        List<MessageVo> msgVos = of(msglist);
        Map<String, List<MessageVo>> room2Msg = msgVos.stream()
            .sorted(Comparator.comparing(MessageVo::getMessageId))
            .collect(Collectors.groupingBy(MessageVo::getRoomId));
        return room2Msg;
    }

    @Override
    public Message sendMessage(Message msg, String roomId, List<String> userIds, String deviceType) {
        Message saveMsg = sendMessage(msg, roomId, userIds);
        // 发送者 timeline 更新
        Timeline timeline = timelineRepository.findUserTimelineByMsgId(saveMsg.getMessageId(), msg.getSenderId());
        synchroNodeRepository.update(msg.getSenderId(), deviceType, timeline.getId());
        return saveMsg;
    }

    @Override
    @Transactional
    public Message sendMessage(Message msg, String roomId, List<String> receiverIds) {
        String msgId = SnowflakeDistributeId.getGlobalId();
        msg.setMessageId(msgId);
        // 消息保存
        Message saveMsg = messageRepository.save(msg);
        // 用户信箱
        timelineRepository.writeUserTimelines(msgId, receiverIds); // 发送者也要写，用于多端同步
        // 房间信箱
        timelineRepository.writeRoomTimeline(msgId, roomId);
        return saveMsg;
    }

    private List<MessageVo> of(List<Message> messages) {
        List<MessageVo> msgVos = messages.stream()
            .map(msg -> {
                String userId = msg.getSenderId();
                User user = userCache.get(userId, id -> userRepository.findUserByUserId(id));
                MessageVo vo = MessageVo.of(msg);
                vo.setAvatar(user.getAvatar());
                vo.setUsername(user.getUsername());
                return vo;
            })
            .collect(Collectors.toList());
        return msgVos;
    }
}
