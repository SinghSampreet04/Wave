package com.wave.backend.channel.repository;

import com.wave.backend.channel.entity.Channel;
import com.wave.backend.workspace.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChannelRepository extends JpaRepository<Channel, Long> {

    List<Channel> findByWorkspace(Workspace workspace);

}