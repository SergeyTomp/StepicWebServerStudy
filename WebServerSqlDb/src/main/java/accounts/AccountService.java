package accounts;

import dbService.DBException;
import dbService.DBService;
import dbService.dataSets.UsersDataSet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author v.chibrikov
 *         <p>
 *         Пример кода для курса на https://stepic.org/
 *         <p>
 *         Описание курса и лицензия: https://github.com/vitaly-chibrikov/stepic_java_webserver
 */
public class AccountService {

    private final Map<String, UserProfile> sessionIdToProfile;
    private DBService dbService;

    public AccountService(DBService dbService) {
        sessionIdToProfile = new HashMap<>();
        this.dbService = dbService;
    }

    public void addNewUser(UserProfile userProfile) throws DBException {
        dbService.addUser(userProfile.getLogin(), userProfile.getPass());
    }

    // используем упрощённую реализацию метода, только для теста - там не важен пароль, а запись будет только одна
    public UserProfile getUserByLogin(String login) throws DBException {

        UserProfile profile = null;
        List<UsersDataSet> usersDataSets = dbService.getUsersByName(login);
        if (!usersDataSets.isEmpty()) {
            UsersDataSet user = usersDataSets.get(0);
            profile = new UserProfile(user.getName(), user.getPassword(), user.getName());
        }
        return profile;
    }


    public UserProfile getUserBySessionId(String sessionId) {
        return sessionIdToProfile.get(sessionId);
    }

    public void addSession(String sessionId, UserProfile userProfile) {
        sessionIdToProfile.put(sessionId, userProfile);
    }

    public void deleteSession(String sessionId) {
        sessionIdToProfile.remove(sessionId);
    }
}
