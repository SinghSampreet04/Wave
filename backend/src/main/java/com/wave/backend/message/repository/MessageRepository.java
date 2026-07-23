package com.wave.backend.message.repository;

import com.wave.backend.channel.entity.Channel;
import com.wave.backend.message.entity.Message;
import com.wave.backend.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.time.LocalDateTime;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByChannelOrderByCreatedAtAsc(Channel channel);

    List<Message> findByChannelAndParentMessageIsNullOrderByCreatedAtAsc(
            Channel channel
    );

    Page<Message> findByChannelOrderByCreatedAtDesc(
            Channel channel,
            Pageable pageable
    );

    @Query("""
            SELECT m
            FROM Message m
            WHERE m.channel = :channel
            AND m.parentMessage IS NULL
            AND (:beforeId IS NULL OR m.id < :beforeId)
            ORDER BY m.id DESC
            """)
    List<Message> findHistory(
            @Param("channel") Channel channel,
            @Param("beforeId") Long beforeId,
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
            AND m.deleted = false
            AND m.parentMessage IS NULL
            AND EXISTS (
                SELECT wm.id
                FROM WorkspaceMember wm
                WHERE wm.workspace = m.channel.workspace
                AND wm.user = :user
            )
            AND (
                m.channel.isPrivate = false
                OR EXISTS (
                    SELECT cm.id
                    FROM ChannelMember cm
                    WHERE cm.channel = m.channel
                    AND cm.user = :user
                )
            )
            ORDER BY m.createdAt DESC
            """)
    List<Message> searchAccessibleByKeyword(
            String keyword,
            User user
    );

    @Query("""
            SELECT m
            FROM Message m
            WHERE m.channel.id = :channelId
            AND LOWER(m.content)
            LIKE LOWER(CONCAT('%', :keyword, '%'))
            AND m.deleted = false
            AND m.parentMessage IS NULL
            AND EXISTS (
                SELECT wm.id
                FROM WorkspaceMember wm
                WHERE wm.workspace = m.channel.workspace
                AND wm.user = :user
            )
            AND (
                m.channel.isPrivate = false
                OR EXISTS (
                    SELECT cm.id
                    FROM ChannelMember cm
                    WHERE cm.channel = m.channel
                    AND cm.user = :user
                )
            )
            ORDER BY m.createdAt DESC
            """)
    List<Message> searchAccessibleByChannelAndKeyword(
            Long channelId,
            String keyword,
            User user
    );

    @Query("""
            SELECT DISTINCT m
            FROM Message m
            WHERE LOWER(m.content) LIKE LOWER(CONCAT('%', :keyword, '%'))
            AND m.deleted = false
            AND m.parentMessage IS NULL
            AND (:workspaceId IS NULL OR m.channel.workspace.id = :workspaceId)
            AND (:channelId IS NULL OR m.channel.id = :channelId)
            AND (:senderId IS NULL OR m.sender.id = :senderId)
            AND (:fromDate IS NULL OR m.createdAt >= :fromDate)
            AND (:toDate IS NULL OR m.createdAt <= :toDate)
            AND (:hasAttachment IS NULL OR
                 (:hasAttachment = true AND EXISTS (
                     SELECT fa.id FROM FileAttachment fa WHERE fa.message = m
                 )) OR
                 (:hasAttachment = false AND NOT EXISTS (
                     SELECT fa.id FROM FileAttachment fa WHERE fa.message = m
                 )))
            AND (:hasReaction IS NULL OR
                 (:hasReaction = true AND EXISTS (
                     SELECT r.id FROM Reaction r WHERE r.message = m
                 )) OR
                 (:hasReaction = false AND NOT EXISTS (
                     SELECT r.id FROM Reaction r WHERE r.message = m
                 )))
            AND EXISTS (
                SELECT wm.id FROM WorkspaceMember wm
                WHERE wm.workspace = m.channel.workspace AND wm.user = :user
            )
            AND (
                m.channel.isPrivate = false OR EXISTS (
                    SELECT cm.id FROM ChannelMember cm
                    WHERE cm.channel = m.channel AND cm.user = :user
                )
            )
            """)
    Page<Message> searchAccessible(
            String keyword,
            Long workspaceId,
            Long channelId,
            Long senderId,
            LocalDateTime fromDate,
            LocalDateTime toDate,
            Boolean hasAttachment,
            Boolean hasReaction,
            User user,
            Pageable pageable
    );

}
