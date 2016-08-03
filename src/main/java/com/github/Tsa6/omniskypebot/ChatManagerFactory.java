package com.github.Tsa6.omniskypebot;

import com.skype.Chat;

public interface ChatManagerFactory {
	
	ChatManager createChatManager(Chat chat);
	
}
