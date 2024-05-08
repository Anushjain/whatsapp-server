package com.anush.whatsapp.repos;

import com.anush.whatsapp.domain.Message;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {
    Page<Message> findByChatroomIdOrderByDateCreatedDesc(UUID chatroomId, PageRequest pageRequest);
}
