-- Workspace/channel removal is an explicit owner-only operation. Cascades keep
-- dependent collaboration data consistent and avoid partially deleted trees.

DO $$
DECLARE
    fk RECORD;
    existing_constraint TEXT;
BEGIN
    FOR fk IN
        SELECT *
        FROM (
            VALUES
                ('workspace_members', 'workspace_id', 'workspaces', 'id', 'fk_workspace_members_workspace_delete'),
                ('workspace_invitations', 'workspace_id', 'workspaces', 'id', 'fk_workspace_invitations_workspace_delete'),
                ('channels', 'workspace_id', 'workspaces', 'id', 'fk_channels_workspace_delete'),
                ('channel_members', 'channel_id', 'channels', 'id', 'fk_channel_members_channel_delete'),
                ('channel_invitations', 'channel_id', 'channels', 'id', 'fk_channel_invitations_channel_delete'),
                ('messages', 'channel_id', 'channels', 'id', 'fk_messages_channel_delete'),
                ('messages', 'parent_message_id', 'messages', 'id', 'fk_messages_parent_delete'),
                ('file_attachments', 'message_id', 'messages', 'id', 'fk_file_attachments_message_delete'),
                ('mentions', 'message_id', 'messages', 'id', 'fk_mentions_message_delete'),
                ('message_reads', 'message_id', 'messages', 'id', 'fk_message_reads_message_delete'),
                ('pinned_messages', 'message_id', 'messages', 'id', 'fk_pinned_messages_message_delete'),
                ('reactions', 'message_id', 'messages', 'id', 'fk_reactions_message_delete')
        ) AS constraints_to_replace(
            child_table,
            child_column,
            parent_table,
            parent_column,
            replacement_name
        )
    LOOP
        SELECT constraint_row.conname
        INTO existing_constraint
        FROM pg_constraint constraint_row
        JOIN pg_class child
            ON child.oid = constraint_row.conrelid
        JOIN pg_class parent
            ON parent.oid = constraint_row.confrelid
        JOIN pg_attribute child_column
            ON child_column.attrelid = child.oid
            AND child_column.attnum = ANY(constraint_row.conkey)
        WHERE constraint_row.contype = 'f'
          AND child.relname = fk.child_table
          AND child_column.attname = fk.child_column
          AND parent.relname = fk.parent_table
        LIMIT 1;

        IF existing_constraint IS NOT NULL THEN
            EXECUTE format(
                'ALTER TABLE %I DROP CONSTRAINT %I',
                fk.child_table,
                existing_constraint
            );
        END IF;

        EXECUTE format(
            'ALTER TABLE %I ADD CONSTRAINT %I FOREIGN KEY (%I) REFERENCES %I(%I) ON DELETE CASCADE',
            fk.child_table,
            fk.replacement_name,
            fk.child_column,
            fk.parent_table,
            fk.parent_column
        );

        existing_constraint := NULL;
    END LOOP;
END
$$;
