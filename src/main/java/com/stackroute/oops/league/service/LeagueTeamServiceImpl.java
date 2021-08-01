package com.stackroute.oops.league.service;

import com.stackroute.oops.league.dao.PlayerDao;
import com.stackroute.oops.league.dao.PlayerDaoImpl;
import com.stackroute.oops.league.dao.PlayerTeamDao;
import com.stackroute.oops.league.dao.PlayerTeamDaoImpl;
import com.stackroute.oops.league.exception.PlayerAlreadyAllottedException;
import com.stackroute.oops.league.exception.PlayerAlreadyExistsException;
import com.stackroute.oops.league.exception.PlayerNotFoundException;
import com.stackroute.oops.league.exception.TeamAlreadyFormedException;
import com.stackroute.oops.league.model.Player;
import com.stackroute.oops.league.model.PlayerTeam;

import java.io.File;
import java.util.*;

/**
 * This class implements leagueTeamService
 * This has four fields: playerDao, playerTeamDao and registeredPlayerList and playerTeamSet
 */
public class LeagueTeamServiceImpl implements LeagueTeamService {
    //Changes
    private List<Player>registeredPlayerList;
    private Set<PlayerTeam> playerTeamSet;
    private PlayerDao playerDao;
    private PlayerTeamDao playerTeamDao;
    /**
     * Constructor to initialize playerDao, playerTeamDao
     * empty ArrayList for registeredPlayerList and empty TreeSet for playerTeamSet
     */
    public LeagueTeamServiceImpl() {
//Changes
        playerDao= new PlayerDaoImpl();
        playerTeamSet=new TreeSet<PlayerTeam>();
        registeredPlayerList=new ArrayList<Player>();
        playerTeamDao=new PlayerTeamDaoImpl();
    }

    //Add player data to file using PlayerDao object
    @Override
    public boolean addPlayer(Player player) throws PlayerAlreadyExistsException {
        boolean isAdded  = playerDao.addPlayer(player);
        return isAdded;
    }

    /**
     * Register the player for the given teamTitle
     * Throws PlayerNotFoundException if the player does not exists
     * Throws PlayerAlreadyAllottedException if the player is already allotted to team
     * Throws TeamAlreadyFormedException if the maximum number of players has reached for the given teamTitle
     * Returns null if there no players available in the file "player.csv"
     * Returns "Registered" for successful registration
     * Returns "Invalid credentials" when player credentials are wrong
     */
    @Override
    public synchronized String registerPlayerToLeague(String playerId, String password, LeagueTeamTitles teamTitle)
            throws PlayerNotFoundException, TeamAlreadyFormedException, PlayerAlreadyAllottedException {

        if(playerDao.getAllPlayers().isEmpty())
            return null;
        Player playerFind = playerDao.findPlayer(playerId);
        if(playerFind == null)
            throw new PlayerNotFoundException();
        else{
            if (!playerFind.getPassword().equalsIgnoreCase(password))
                return "Invalid credentials";
        }
//		int size = getExistingNumberOfPlayersInTeam(teamTitle);
//		if(size>11) {
//			throw new TeamAlreadyFormedException();
//		}
        Set<PlayerTeam> pTeamList = playerTeamDao.getAllPlayerTeams();
        int c=0;
        if(pTeamList!=null) {
            for (PlayerTeam p : pTeamList) {
                if (p.getPlayerId().equalsIgnoreCase(playerId)) {
                    c = 1;
                    throw new PlayerAlreadyAllottedException();
                }
            }
        }
        if(c==0){
            PlayerTeam pt = new PlayerTeam(playerId,teamTitle.toString());
            playerTeamDao.addPlayerToTeam(pt);
            registeredPlayerList.add(playerFind);
            return "Registered";
        }
        return "Not Registered";
    }

    /**
     * Return the list of all registered players
     */
    @Override
    public List<Player> getAllRegisteredPlayers() {
        return this.registeredPlayerList;
    }


    /**
     * Return the existing number of players for the given title
     */
    @Override
    public int getExistingNumberOfPlayersInTeam(LeagueTeamTitles teamTitle) {

        playerTeamSet = playerTeamDao.getPlayerSetByTeamTitle(teamTitle.toString());
        List<Player> pSet = new ArrayList<Player>();
        if(playerTeamSet.size() == 0) {
            List<Player> pl = playerDao.getAllPlayers();
            for (Player p : pl) {
                if (p.getTeamTitle() != null) {
                    if (p.getTeamTitle().equalsIgnoreCase(teamTitle.toString())) {
                        pSet.add(p);
                    }
                }
            }
            return pSet.size();
        }
        return playerTeamSet.size();
    }

    /**
     * Admin credentials are authenticated and registered players are allotted to requested teams if available
     * If the requested teams are already formed,admin randomly allocates to other available teams
     * PlayerTeam object is added to "finalteam.csv" file allotted by the admin using PlayerTeamDao
     * Return "No player is registered" when registeredPlayerList is empty
     * Throw TeamAlreadyFormedException when maximum number is reached for all teams
     * Return "Players allotted to teams" when registered players are successfully allotted
     * Return "Invalid credentials for admin" when admin credentials are wrong
     */
    @Override
    public String allotPlayersToTeam(String adminName, String password, LeagueTeamTitles teamTitle)
            throws TeamAlreadyFormedException, PlayerNotFoundException {
        LeagueTeamTitles lt[] = LeagueTeamTitles.values();
        if (adminName.equalsIgnoreCase(AdminCredentials.admin)
                && password.equalsIgnoreCase(AdminCredentials.password)) {

            if (registeredPlayerList == null || registeredPlayerList.isEmpty())
            {

                return "No player is registered";
            }
            for (Player p : registeredPlayerList) {
                int size = getExistingNumberOfPlayersInTeam(teamTitle);
                int c = 0;
                if (size >= 11) {
//					for (LeagueTeamTitles str : lt) {
//						int size1 = getExistingNumberOfPlayersInTeam(str);
//						if (size1 < 11) {
//							c = 1;
//							PlayerTeam pt = new PlayerTeam(p.getPlayerId(), str.toString());
//							playerTeamDao.addPlayerToTeam(pt);
//							break;
//						}
//					}
//					if (c == 0)
                    throw new TeamAlreadyFormedException();
                } else {
                    PlayerTeam pt = new PlayerTeam(p.getPlayerId(), teamTitle.toString());
                    playerTeamDao.addPlayerToTeam(pt);
                }
            }

        } else {
            return "Invalid credentials";
        }
        return "Players allotted to teams";
    }

    /**
     * static nested class to initialize admin credentials
     * admin name='admin' and password='pass'
     */
    static class AdminCredentials {
        private static String admin = "admin";
        private static String password = "pass";
    }
}
