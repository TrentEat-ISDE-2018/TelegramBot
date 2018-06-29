package gabry147.bots.broadcaster_bot;

import org.apache.log4j.Logger;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import gabry147.bots.broadcaster_bot.entities.UserEntity;
import gabry147.bots.broadcaster_bot.entities.extra.UserRole;

public class Main {

    public static Logger logger=Logger.getLogger(Main.class);

    public static void main(String args[]){
        ApiContextInitializer.init();

        TelegramBotsApi botsApi=new TelegramBotsApi();

        Broadcaster_bot broadcasterBot=new Broadcaster_bot();

        System.out.println(System.getenv("ADMIN_ID"));
        Long firstAdminId = Long.decode(System.getenv("ADMIN_ID"));
        UserEntity firstAdmin = UserEntity.getById(firstAdminId);
        if(firstAdmin == null) {
        	logger.info("no admin");
        	firstAdmin = new UserEntity();
        	firstAdmin.setUserId(firstAdminId);
        	firstAdmin.setRole(UserRole.SUPERADMIN);
        	UserEntity.saveUser(firstAdmin);
        }
        else if(! (firstAdmin.getRole().equals(UserRole.SUPERADMIN))) {
        	logger.error("user exist but not owner");
        	firstAdmin.setRole(UserRole.SUPERADMIN);
        	UserEntity.saveUser(firstAdmin);
        }
        
        try {
            botsApi.registerBot(broadcasterBot);
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
            logger.error("Unable to register bot");
        }
    }
}
