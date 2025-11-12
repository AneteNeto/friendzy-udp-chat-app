package controllers;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;



public class SessionController {
    private Map<String, String> activeSessions = new ConcurrentHashMap<>();
    // ajouter nouvelle session
    public void registerSession(String clientId) {
        activeSessions.put(clientId, "Ative");
    }
    
    //getter
    
    // se deconnecter
    public void removeSession(String clientId) {
        activeSessions.remove(clientId);
    }
    // verifier si session active
    public boolean isSessionActive(String clientId) {
        return activeSessions.containsKey(clientId);
    }
}