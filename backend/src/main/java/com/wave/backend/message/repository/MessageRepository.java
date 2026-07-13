package com.wave.backend.message.repository;

import com.wave.backend.channel.entity.Channel;
import com.wave.backend.message.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByChannelOrderByCreatedAtAsc(Channel channel);

    Page<Message> findByChannelOrderByCreatedAtDesc(
            Channel channel,
            Pageable pageable
    );

    List<Message> findByParentMessageOrderByCreatedAtAsc(
            Message parentMessage
    );

    @Query("""
            SELECT m
            FROM Message m
            WHERE LOWER(m.content)
            LIKE LOWER(CONCAT('%', :keyword, '%'))
            ORDER BY m.createdAt DESC
            """)
    List<Message> searchByKeyword(String keyword);

    @Query("""
            SELECT m
            FROM Message m
            WHERE m.channel.id = :channelId
            AND LOWER(m.content)
            LIKE LOWER(CONCAT('%', :keyword, '%'))
            ORDER BY m.createdAt DESC
            """)
    List<Message> searchByChannelAndKeyword(
            Long channelId,
            String keyword
    );

}