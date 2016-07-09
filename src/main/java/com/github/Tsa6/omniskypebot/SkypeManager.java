package com.github.Tsa6.omniskypebot;

import com.github.Tsa6.omniskypebot.chatmanagers.ChatManagerFactory;
import com.github.Tsa6.omniskypebot.chatmanagers.ChatManager;
import com.skype.Chat;
import com.skype.ChatMessage;
import com.skype.ChatMessageListener;
import com.skype.GlobalChatListener;
import com.skype.Skype;
import com.skype.SkypeException;
import com.skype.User;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class SkypeManager {
	
	private final String[] contacts;
	private final ChatManagerFactory cmf;
	private final HashMap<Chat, ChatManager> managers;
	private final ArrayList<String> recentMessageIds;
	
	public SkypeManager(ChatManagerFactory cmf, String... contacts) throws SkypeException {
		this.contacts = contacts;
		this.recentMessageIds = new ArrayList<>();
		this.cmf = cmf;
		managers = new HashMap<>();
		start();
	}
	
	private void start() throws SkypeException {
		Skype.addGlobalChatListener(new GlobalChatListener() {
			@Override
			public void newChatStarted(Chat chat, User[] users) {
				System.out.println("[Info] New chat started with users "+Arrays.deepToString(users));
				if(checkUsers(users)) {
					managers.put(chat, cmf.createChatManager(chat));
				}
			}

			@Override
			public void userLeft(Chat chat, User user) { }

			@Override
			public void userAdded(Chat chat, User user) { }
			
		});
		
		Skype.addChatMessageListener(new ChatMessageListener() {
			@Override
			public void chatMessageReceived(ChatMessage cm) throws SkypeException {Chat chat = cm.getChat();
				if(!recentMessageIds.contains(cm.getId()) && checkUsers(chat.getAllMembers())) {
					recentMessageIds.add(cm.getId());
					while(recentMessageIds.size() > 20) {
						recentMessageIds.remove(20);
					}
					
					System.out.println("[Info] Chat message recieved from "+cm.getSenderDisplayName()+": "+cm.getContent());
				
					if(!managers.containsKey(chat)) {
						managers.put(chat, cmf.createChatManager(chat));
					}
					managers.get(chat).respond(cm.getContent());
				}
			}

			@Override
			public void chatMessageSent(ChatMessage cm) throws SkypeException { }
			
		});
	}
	
	private boolean checkUsers(User... users) {
		for(User user : users) {
			String userId = user.getId();
			for(String contact : contacts) {
				if(!userId.equals(contact)) {
					return false;
				}
			}
		}
		return true;
	}
}
