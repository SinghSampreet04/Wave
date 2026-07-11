package com.wave.backend.message.repository;

import com.wave.backend.channel.entity.Channel;
import com.wave.backend.message.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByChannelOrderByCreatedAtAsc(Channel channel);

}