package gabry147.bots.broadcaster_bot.entities;

import javax.persistence.*;

import gabry147.bots.broadcaster_bot.entities.dao.Broadcaster_BotDao;
import gabry147.bots.broadcaster_bot.entities.extra.UserRole;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Entity
@NamedQuery(name = "UserEntity.findAll",query = "SELECT u FROM UserEntity u")
@Table(name = "users")
public class UserEntity implements Serializable {
    @Id
    @Column(name="user_id")
    private long userId;

    @Column(name="username")
    private String username;

    @Enumerated(EnumType.STRING)
    @Column(name="role")
    private UserRole role;
    
    @Column(name="range")
    private int range = 1;

    public static UserEntity getById(Long id){
        EntityManager em= Broadcaster_BotDao.instance.createEntityManager();
        UserEntity u=em.find(UserEntity.class,id);
        Broadcaster_BotDao.instance.closeConnections(em);

        return u;
    }
    
    public static List<UserEntity> getAll(){
        EntityManager em=Broadcaster_BotDao.instance.createEntityManager();
        List<UserEntity> userEntities = em.createNamedQuery("UserEntity.findAll").getResultList();
        Broadcaster_BotDao.instance.closeConnections(em);
        return userEntities;
    }

    public static UserEntity saveUser(UserEntity u){
        EntityManager em=Broadcaster_BotDao.instance.createEntityManager();
        EntityTransaction tx=em.getTransaction();
        tx.begin();
        u=em.merge(u);
        tx.commit();
        Broadcaster_BotDao.instance.closeConnections(em);

        return u;
    }
    
    public static void deleteUser(UserEntity u){
        EntityManager em=Broadcaster_BotDao.instance.createEntityManager();
        EntityTransaction tx=em.getTransaction();
        tx.begin();
        u=em.merge(u);
        em.remove(u);
        tx.commit();
        Broadcaster_BotDao.instance.closeConnections(em);
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}
}
