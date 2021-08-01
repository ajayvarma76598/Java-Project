package com.stackroute.oops.league.dao;

import com.stackroute.oops.league.exception.PlayerAlreadyExistsException;
import com.stackroute.oops.league.exception.PlayerNotFoundException;
import com.stackroute.oops.league.model.Player;
import com.stackroute.oops.league.model.PlayerTeam;

import java.io.*;
import java.util.*;

/**
 * This class implements the PlayerTeamDao interface
 * This class has two fields playerTeamSet,playerDao and a String constant for storing file name.
 */
public class PlayerTeamDaoImpl implements PlayerTeamDao {
    private static final String TEAM_FILE_NAME = "src/main/resources/finalteam.csv";
    private TreeSet<PlayerTeam> playerTeamList;
    private PlayerDaoImpl playerDao;
    /**
     * Constructor to initialize an empty TreeSet and PlayerDao object
     */
    public PlayerTeamDaoImpl() {
        playerDao= new PlayerDaoImpl();
        playerTeamList=new TreeSet<PlayerTeam>();
    }

    /*
    Returns the list of players belonging to a particular teamTitle by reading
    from the file finalteam.csv
     */
    @Override
    public Set<PlayerTeam> getPlayerSetByTeamTitle(String teamTitle) {
        Set<PlayerTeam> playerTeamSet = getAllPlayerTeams();
        Set<PlayerTeam> playerTeamSetWithTeamTitle = new TreeSet<PlayerTeam>();
        for(PlayerTeam pt : playerTeamSet)
            if (pt.getTeamTitle().equalsIgnoreCase(teamTitle)) {
                playerTeamSetWithTeamTitle.add(pt);
            }
        return playerTeamSetWithTeamTitle;
    }

    //Add he given PlayerTeam Object to finalteam.csv file
    public boolean addPlayerToTeam(PlayerTeam playerTeam) throws PlayerNotFoundException {

        Set<PlayerTeam> playerTeams=getAllPlayerTeams();
        int c=0;
        if(playerTeams!=null) {
            for(PlayerTeam p:playerTeams){
                if(p.getPlayerId().equalsIgnoreCase(playerTeam.getPlayerId()))
                    System.out.println("Player Exists");
                c=1;
                break;
            }
        }
        if(c==0) {
            try (FileWriter fileWriter=new FileWriter(TEAM_FILE_NAME,true);){
                fileWriter.write(playerTeam.getPlayerId()+","+playerTeam.getTeamTitle());
                fileWriter.append("\n");
                fileWriter.flush();
                fileWriter.close();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    //Return the set of all PlayerTeam by reading the file content from finalteam.csv file
    @Override
    public Set<PlayerTeam> getAllPlayerTeams() {
        Set<PlayerTeam> playerTeamSet=new TreeSet<PlayerTeam>();
        File file=new File(TEAM_FILE_NAME);
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file));) {
            String strCurrentLine;
            while ((strCurrentLine = bufferedReader.readLine()) != null) {
                String[] player = strCurrentLine.split(",");
                PlayerTeam playerTeam = new PlayerTeam(player[0], player[1]);
                playerTeamSet.add(playerTeam);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return playerTeamSet;
//        }
    }
}