package com.wave.backend.file.repository;

import com.wave.backend.file.entity.FileAttachment;
import com.wave.backend.message.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FileAttachmentRepository
        extends JpaRepository<FileAttachment, Long> {

    List<FileAttachment> findByMessage(Message message);

    List<FileAttachment> findByMessageId(Long messageId);

    boolean existsByMessageId(Long messageId);

    void deleteByMessage(Message message);

    @Query("""
            SELECT attachment.storedFilename
            FROM FileAttachment attachment
            WHERE attachment.message.channel.id = :channelId
            """)
    List<String> findStoredFilenamesByChannelId(
            @Param("channelId") Long channelId
    );

    @Query("""
            SELECT attachment.storedFilename
            FROM FileAttachment attachment
            WHERE attachment.message.channel.workspace.id = :workspaceId
            """)
    List<String> findStoredFilenamesByWorkspaceId(
            @Param("workspaceId") Long workspaceId
    );

}
