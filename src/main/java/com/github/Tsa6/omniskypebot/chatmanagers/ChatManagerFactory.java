package com.github.Tsa6.omniskypebot.chatmanagers;

import com.skype.Chat;

public interface ChatManagerFactory {
	
	ChatManager createChatManager(Chat chat);
	
}
