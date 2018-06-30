package gabry147.bots.trenteatbot.tasks;

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

import businesslayer.ws.Agritur;
import businesslayer.ws.AgriturService;
import businesslayer.ws.AgriturServiceImplService;
import gabry147.bots.trenteatbot.TrentEatBot;
import gabry147.bots.trenteatbot.entities.UserEntity;
import gabry147.bots.trenteatbot.entities.extra.UserRole;

import java.util.*;
import java.util.regex.Pattern;

public class UpdateTask implements Runnable {

    public static Logger logger=Logger.getLogger(UpdateTask.class);

    private TrentEatBot bot;
    private Update update;

    public UpdateTask(TrentEatBot bot, Update update){
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
    		
    		if(! message.hasText()) {
    			if(message.hasLocation()) {
    				//TODO
    			}
    			else {
    				sendTelegramMessage(chatId, "Not supported");
    				Thread.currentThread().interrupt();
                    return;
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
				updateUserDbInfo(message.getFrom());
				sendTelegramHtmlMessage(chatId, "<b>Welcome in TrentEat</b> \n"
						+ sanitize("forward your position to get agritur near you (/range for set distance)\n"
						+ "/place <place name> for agritur near <place name>\n"
						+ "/agritur <agritur full name> for realtime info\n"
						+ "/like <agritur full name> for a good agritur\n"
						+ "/dislike <agritur full name> for a bad agritur\n"
						+ "/findForMe for recommendation"), true);
				Thread.currentThread().interrupt();
				return;
			}
			
			//user is in db
			if(userEntity != null) {
				if(userEntity.getRole().compareTo(UserRole.USER) <= 0) {
					if( command.equals( "PLACE" ) ) {
						if(alphanumericalSplit.length == 1) {
							sendTelegramMessage(chatId, "Send a place name and you'll recevive the nearest agriturs");
						}
						String place = alphanumericalSplit[1];
						//starting from 1, 0 is the command
						for(int i = 2; i<alphanumericalSplit.length; i++) {
							place += " " + alphanumericalSplit[i];
						}
						AgriturServiceImplService service = new AgriturServiceImplService();
						AgriturService agriturService = service.getAgriturServiceImplPort();
						List<Agritur> near = agriturService.getAgriturByPlace(place, userEntity.getRange());
						if(near.size() > 0){
							sendAgriturList(chatId, near);
						}
						else {
							sendTelegramMessage(chatId, "Not found, place not recognized or no near agritur");
						}
						Thread.currentThread().interrupt();
    					return;
					}
					else if( command.equals( "AGRITUR" ) ) {
						if(alphanumericalSplit.length == 1) {
							sendTelegramMessage(chatId, "Send a complete agritur name and you'll recevive all the info");
						}
						String name = alphanumericalSplit[1];
						//starting from 1, 0 is the command
						for(int i = 2; i<alphanumericalSplit.length; i++) {
							name += " " + alphanumericalSplit[i];
						}
						AgriturServiceImplService service = new AgriturServiceImplService();
						AgriturService agriturService = service.getAgriturServiceImplPort();
						Agritur ag = agriturService.getDetailedAgritur(name);
						if(ag != null){
							sendAgriturInfo(chatId, ag);
							agriturService.userViewAgritur(Long.toString(userEntity.getUserId()), ag.getName());
						}
						else {
							sendTelegramMessage(chatId, "Agritur not found");
						}
						Thread.currentThread().interrupt();
    					return;					
					}
					else if( command.equals( "LIKE" ) ) {
						if(alphanumericalSplit.length == 1) {
							sendTelegramMessage(chatId, "Send a complete agritur name and your like will be saved");
						}
						String name = alphanumericalSplit[1];
						//starting from 1, 0 is the command
						for(int i = 2; i<alphanumericalSplit.length; i++) {
							name += " " + alphanumericalSplit[i];
						}
						AgriturServiceImplService service = new AgriturServiceImplService();
						AgriturService agriturService = service.getAgriturServiceImplPort();
						Agritur ag = agriturService.getDetailedAgritur(name);
						if(ag != null){
							agriturService.userMarkAgritur(Long.toString(userEntity.getUserId()), ag.getName(), 1);
							sendTelegramMessage(chatId, "Thanks for your feedback");
						}
						else {
							sendTelegramMessage(chatId, "Agritur not found");
						}
						Thread.currentThread().interrupt();
    					return;
					}
					else if( command.equals( "DISLIKE" ) ) {
						if(alphanumericalSplit.length == 1) {
							sendTelegramMessage(chatId, "Send a complete agritur name and your dislike will be saved");
						}
						String name = alphanumericalSplit[1];
						//starting from 1, 0 is the command
						for(int i = 2; i<alphanumericalSplit.length; i++) {
							name += " " + alphanumericalSplit[i];
						}
						AgriturServiceImplService service = new AgriturServiceImplService();
						AgriturService agriturService = service.getAgriturServiceImplPort();
						Agritur ag = agriturService.getDetailedAgritur(name);
						if(ag != null){
							agriturService.userMarkAgritur(Long.toString(userEntity.getUserId()), ag.getName(), -1);
							sendTelegramMessage(chatId, "Thanks for your feedback");
						}
						else {
							sendTelegramMessage(chatId, "Agritur not found");
						}
						Thread.currentThread().interrupt();
    					return;
					}
					else if( command.equals( "FINDFORME" ) ) {
						AgriturServiceImplService service = new AgriturServiceImplService();
						AgriturService agriturService = service.getAgriturServiceImplPort();
						List<Agritur> recommends = agriturService.recommendAgritur(Long.toString(userEntity.getUserId()));
						if(recommends.size() != 0){
							sendAgriturList(chatId, recommends);
						}
						else {
							sendTelegramMessage(chatId, "Nothing to recommend");
						}
						Thread.currentThread().interrupt();
    					return;
					}
    			}
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
    
	private void sendAgriturInfo(long chatId, Agritur ag) {
		String text = sanitize(ag.getName() + "\n" + ag.getPhone());
		//TODO
		SendMessage reply = new SendMessage();
		reply.setChatId(chatId);
		reply.enableHtml(true);
		reply.setText(text);
		try {
			bot.sendMessage(reply);
		} catch (TelegramApiException e) {
			logger.error("Error sending text message");
			e.printStackTrace();
		}
	}
	
	private void sendAgriturList(long chatId, List<Agritur> list) {
		String text = "<b>Here is the list!</b>"
				+ "Press on a Agritur and paste it on a message to get more info:\n\n";
		for(Agritur a : list) {
			text += "<code>/agritur "+sanitize(a.getName())+"</code>\n";
			text += "<i>"+sanitize(a.getAddress())+"</i>\n\n";
		}
		SendMessage reply = new SendMessage();
		reply.setChatId(chatId);
		reply.enableHtml(true);
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
