package gabry147.bots.broadcaster_bot.tasks;

import gabry147.bots.broadcaster_bot.Broadcaster_bot;
import gabry147.bots.broadcaster_bot.entities.UserEntity;
import gabry147.bots.broadcaster_bot.entities.extra.UserRole;

import org.apache.log4j.Logger;
import org.telegram.telegrambots.api.methods.ForwardMessage;
import org.telegram.telegrambots.api.methods.groupadministration.GetChatAdministrators;
import org.telegram.telegrambots.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.api.methods.groupadministration.KickChatMember;
import org.telegram.telegrambots.api.methods.groupadministration.LeaveChat;
import org.telegram.telegrambots.api.methods.pinnedmessages.PinChatMessage;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.*;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import java.util.*;
import java.util.regex.Pattern;

public class UpdateTask implements Runnable {

    public static Logger logger=Logger.getLogger(UpdateTask.class);

    private Broadcaster_bot bot;
    private Update update;

    public UpdateTask(Broadcaster_bot bot, Update update){
        this.bot=bot;
        this.update=update;
    }

    public void run() {
    	if(update.hasMessage()) {    		
    		Message message = update.getMessage();
    		logger.info("Message from @"+message.getFrom().getUserName()+ " ("+message.getFrom().getId()+")");
    		long chatId = message.getChat().getId().longValue();
    		long userId = message.getFrom().getId().longValue();
    		
    		UserEntity userEntity = UserEntity.getById(userId);
    		if(userEntity != null) {
    			if(userEntity.getRole().equals(UserRole.BANNED)) {
    				Thread.currentThread().interrupt();
    				return;
    			}
    			updateUserDbInfo(message.getFrom());
    		}   		
    		int botId = getBotID();
    		
    		if(chatId != userId) {
    			LeaveChat leaveChat = new LeaveChat();
    			leaveChat.setChatId(chatId);
    			try {
					bot.leaveChat(leaveChat);
				} catch (TelegramApiException e) {
					logger.error(e);
				}
			}
			
			String[] alphanumericalSplit = message.getText().split(" ");

			// \n needed if first line doesn't have spaces
			String[] commandSplit = alphanumericalSplit[0].split("\n")[0].split("@");			
			
			if(commandSplit.length>1 && commandSplit[0].startsWith("/")){
				String botUsername = null;
				try {
					botUsername = bot.getMe().getUserName();
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
                if(!commandSplit[1].equals(botUsername)){
                	logger.info("message from another bot");
                	Thread.currentThread().interrupt();
                    return;
                }
			}
			// save command and remove "/"
			String command = commandSplit[0].substring(1).toUpperCase();

			if( command.equals( "START" ) ) {
				//save user and set notification to true
				updateUserDbInfo(message.getFrom());
				sendTelegramHtmlMessage(chatId, "<b>Welcome in TrentEat</b> \n"
						+ "forward your position to get agritur near you (/range for set distance)"
						+ "/place <place name> for agritur near <place name>"
						+ "/agritur <agritur full name> for realtime info"
						+ "/like <agritur full name> for a good agritur"
						+ "/dislike <agritur full name> for a bad agritur"
						+ "/findForMe for recommendation", true);
				Thread.currentThread().interrupt();
				return;
			}
			
			//user is in db
			if(userEntity != null) {
				if(userEntity.getRole().compareTo(UserRole.USER) <= 0) {
    				
    			}
				/*
				 *  Commands for APPROVER
				 */
    			if(userEntity.getRole().compareTo(UserRole.SUPERADMIN) <= 0) {
    				if( command.equals( PrivateCommand.APPROVA.toString() ) ) {
    					String userToPromoteStringID = alphanumericalSplit[1];
    					long userToPromoteID = Long.valueOf(userToPromoteStringID);
    					promoteUser(chatId, userToPromoteID, userEntity);
    					Thread.currentThread().interrupt();
    					return;
    					
    				}
    				else if( command.equals( PrivateCommand.LIMITA.toString() ) ) {
    					String userToDemoteStringID = alphanumericalSplit[1];
    					long userToDemoteID = Long.valueOf(userToDemoteStringID);
    					demoteUser(chatId, userToDemoteID, userEntity);
    					Thread.currentThread().interrupt();
    					return;
    				}
    				else if( command.equals( PrivateCommand.BAN.toString() ) ) {
    					String userToBanStringID = alphanumericalSplit[1];
    					long userToBanID = Long.valueOf(userToBanStringID);
    					banUser(chatId, userToBanID, userEntity);
    					Thread.currentThread().interrupt();
    					return;
    				}
    				else if( command.equals( PrivateCommand.USERS.toString() ) ) {
    					List<UserEntity> members = UserEntity.getAll();
    					sendUserInfoList(chatId, members);
    					Thread.currentThread().interrupt();
    					return;
    				}
    				else if( command.equals( PrivateCommand.CANCELLAUTENTE.toString() ) ) {
    					String userToDeleteString = alphanumericalSplit[1];
    					long userToDeleteID = Long.valueOf(userToDeleteString);
    					deleteUser(chatId, userToDeleteID);
    					Thread.currentThread().interrupt();
    					return;
    				}
    			}   			
    		} //end if userEntity != null	
    	} //end if update.hasMessage()
    	Thread.currentThread().interrupt();
    } // end run


	private String sanitize(String toSanitize) {
    	//replace & must be first or it will destroy all sanitizations
    	return toSanitize.replaceAll("&","&amp;").replaceAll("<","&lt;").replaceAll(">","&gt;");
    }
    
    private void updateUserDbInfo(User user) {
    	UserEntity dbUser = UserEntity.getById(user.getId().longValue());
    	if(dbUser == null) {
    		dbUser = new UserEntity();
    		dbUser.setUserId(user.getId().longValue());
    		dbUser.setRole(UserRole.USER);
    	}
		dbUser.setUsername(user.getUserName());
    	UserEntity.saveUser(dbUser);
    }

    
    private void sendTelegramMessage(long chatId, String text) {
    	sendTelegramHtmlMessage(chatId, text, false);
    }
    
    private void sendTelegramHtmlMessage(long chatId, String text, boolean markdown) {
    	SendMessage reply = new SendMessage();
		reply.setChatId(chatId);
		reply.enableHtml(markdown);
		reply.setText(text);
		try {
			bot.sendMessage(reply);
		} catch (TelegramApiException e) {
			logger.error("Error sending text message");
			e.printStackTrace();
		}
    }
    
    private void sendUserInfoList(long chatId, List<UserEntity> members) {
    	SendMessage reply = new SendMessage();
		reply.setChatId(chatId);
		reply.enableHtml(true);
		String text = "";
		
		for(UserEntity u : members) {
			String username = "USER_NON_SALVATO_";
			if(u.getUsername() != null) username = "@"+sanitize(u.getUsername());
			text = text + username +"  <code>"+u.getUserId()+"</code>  "+sanitize(u.getRole().toString())+"\n";
		}
		
		reply.setText(text);		
		try {
			bot.sendMessage(reply);
		} catch (TelegramApiException e) {
			logger.error("Error sending list of users");
			e.printStackTrace();
		}
		
	}
        
    private void sendUserDbInfo(long chatId, UserEntity user) {
    	SendMessage reply = new SendMessage();
		reply.setChatId(chatId);
		reply.enableHtml(true);
		String username = "<i>NON SALVATO</i>";
		if(user.getUsername() != null) username = "@" + sanitize(user.getUsername());
		reply.setText(
				"Username: " + username + "\n" +
				"<code>" + user.getUserId() +"</code>\n" +
				"Status: " + sanitize(user.getRole().toString()) + "\n"
				);
		try {
			bot.sendMessage(reply);
		} catch (TelegramApiException e) {
			logger.error("Error sending db user info");
			e.printStackTrace();
		}
    }
    
    private int getBotID() {
		int botId = 0;
		try {
			botId = bot.getMe().getId();
		} catch (TelegramApiException e) {
			logger.error("Error getting bot id");
			e.printStackTrace();
		}
		return botId;
    }
    
    private void promoteUser(long chatId, long userToPromoteID, UserEntity senderUser) {
    	UserEntity userToPromote = UserEntity.getById(userToPromoteID);
		if(userToPromote == null) {
			logger.info("Approved: "+userToPromoteID);
			userToPromote = new UserEntity();
			userToPromote.setUserId(userToPromoteID);
			userToPromote.setRole(UserRole.USER);
		}
		else {
			if(senderUser.getRole().compareTo(userToPromote.getRole()) < 0) {
				UserRole[] roles = UserRole.values();
				UserRole previousRole = UserRole.SUPERADMIN;
				for(UserRole currentRole : roles) {
					if(currentRole.compareTo(userToPromote.getRole()) == 0) {
						userToPromote.setRole(previousRole);
						logger.info("Member "+ senderUser.getUserId() +" promote: "+userToPromoteID);
						break;
					}
					previousRole = currentRole;
				}
			}
		}
		UserEntity.saveUser(userToPromote);
		sendUserDbInfo(chatId, userToPromote);
    }
    
    private void demoteUser(long chatId, long userToDemoteID, UserEntity senderUser) {
    	UserEntity userToDemote = UserEntity.getById(userToDemoteID);
		if(userToDemote == null) {
			logger.info("Approved: "+userToDemoteID);
			userToDemote = new UserEntity();
			userToDemote.setUserId(userToDemoteID);
			userToDemote.setRole(UserRole.BANNED);
			sendTelegramMessage(chatId, 
					"Utente sconosciuto, status ridotto a "+ UserRole.BANNED +". Se è n errore invia:\n"
					+ "<code>/"+PrivateCommand.APPROVA+" "+userToDemoteID+"</code>");
		}
		else {
			if(senderUser.getRole().compareTo(userToDemote.getRole()) < 0) {
				UserRole[] roles = UserRole.values();
				boolean previousRole = false;
				for(UserRole role : roles) {
					if(previousRole) {
						userToDemote.setRole(role);
						logger.info("Member "+ senderUser.getUserId() +" demote: "+userToDemoteID);
						break;
					}
					if(role.compareTo(userToDemote.getRole()) == 0) {
						previousRole = true;
					}
				}
			}
			else if (senderUser.getRole().compareTo(UserRole.SUPERADMIN) == 0) {
				if(userToDemote.getRole().compareTo(UserRole.SUPERADMIN) == 0) {
					userToDemote.setRole(UserRole.USER);
					logger.info("Demote owner: "+userToDemoteID);
				}
				
			}
		}
		UserEntity.saveUser(userToDemote);
		sendUserDbInfo(chatId, userToDemote);
    }

    private void banUser(long chatId, long userToBanID, UserEntity senderUser) {
    	UserEntity userToBan = UserEntity.getById(userToBanID);
		if(userToBan == null) {
			logger.info("Ban: "+userToBanID);
			userToBan = new UserEntity();
			userToBan.setUserId(userToBanID);
		}
		else if( senderUser.getRole().compareTo(userToBan.getRole()) > 0) {
			sendTelegramMessage(chatId, "Non puoi bannare qualcuno con status maggiore o uguale al tuo");
			return;
		}
		userToBan.setRole(UserRole.BANNED);
		UserEntity.saveUser(userToBan);
		sendUserDbInfo(chatId, userToBan);
    }
    
	private void deleteUser(long chatId, long userToDeleteID) {
		UserEntity userToDelete = UserEntity.getById(userToDeleteID);
		if(userToDelete == null) {
			sendTelegramMessage(chatId, "ID non riconosciuto");
		}
		else {
			sendUserDbInfo(chatId, userToDelete);
			UserEntity.deleteUser(userToDelete);
			sendTelegramMessage(chatId, "Cancellato!");
		}	
	}
}
