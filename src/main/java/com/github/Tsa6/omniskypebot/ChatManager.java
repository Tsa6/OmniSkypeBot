/*
 */
package com.github.Tsa6.omniskypebot;

import com.skype.SkypeException;

/**
 *
 * @author Taizo Simpson
 */
public interface ChatManager {
	
	void respond(String message) throws SkypeException;
	
}
