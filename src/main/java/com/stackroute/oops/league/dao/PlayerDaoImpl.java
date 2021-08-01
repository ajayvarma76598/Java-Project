package com.stackroute.oops.league.dao;

import com.stackroute.oops.league.exception.PlayerAlreadyExistsException;
import com.stackroute.oops.league.exception.PlayerNotFoundException;
import com.stackroute.oops.league.model.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * This class is implementing the PlayerDao interface
 * This class has one field playerList and a String constant for storing file name
 */
public class PlayerDaoImpl implements PlayerDao {
    private static final String PLAYER_FILE_NAME = "src/main/resources/player.csv";
    private List<Player> playerList;

    /**
     * Constructor to initialize an empty ArrayList for playerList
     */
    public PlayerDaoImpl() {
        playerList=new ArrayList<Player>();

    }

    /**
     * Return true if  player object is stored in "player.csv" as comma separated fields successfully
     * when password length is greater than six and yearExpr is greater than zero
     */
    public boolean addPlayer(Player player) throws PlayerAlreadyExistsException {
        try{
            if(findPlayer(player.getPlayerId())!=null){
                throw new PlayerAlreadyExistsException("Player Exists");
            }
        }
        catch(PlayerNotFoundException e){
            System.out.println("Error player dao-"+e.getMessage());
        }
        try (FileWriter fileWriter=new FileWriter(PLAYER_FILE_NAME,true);){
            if(player.getPassword().length()>6 && player.getYearExpr()>0){
//                if(player.getTeamTitle() == null)
//                    player.setTeamTitle("");
                fileWriter.write(player.getPlayerId()+","+player.getPlayerName()+","+player.getPassword()+","+player.getYearExpr());
                fileWriter.append("\n");
                fileWriter.flush();
                fileWriter.close();
                return true;
            }
        }catch (IOException e){
            System.out.println("io exception -"+e.getMessage());

        }catch(Exception e) {
            System.out.println("exception - "+e.getMessage());
        }
        return false;
    }

    //Return the list of player objects by reading data from the file "player.csv"
    @Override
    public List<Player> getAllPlayers() {
        playerList=new LinkedList<>();
        File file=new File(PLAYER_FILE_NAME);
        if(file.length()==0){
            return Collections.emptyList();
        }else{
            BufferedReader bufferedReader;
            try {
                bufferedReader = new BufferedReader(new FileReader(file));
                String strCurrentLine;
                while((strCurrentLine=bufferedReader.readLine())!= null){
                    String[] players=strCurrentLine.split(",");
                    Player player=new Player(players[0],players[1],players[2],Integer.parseInt(players[3]));
                    playerList.add(player);
                }
                bufferedReader.close();
                return playerList;
            }catch(Exception e){
                System.out.println("error"+e.getMessage());
            }
        }
        return null;
    }

    /**
     * Return Player object given playerId to search
     */
    @Override
    public Player findPlayer(String playerId) throws PlayerNotFoundException {
//        File file=new File(PLAYER_FILE_NAME);
//        if(file.length()==0){
//           throw new PlayerNotFoundException("Empty file");
//        }
        List<Player> players=getAllPlayers();
        if(playerId == null || playerId.trim().isEmpty()){
            throw new PlayerNotFoundException("IS Null or Empty");
        }else{
            int c=0;
            for(Player p: players){
                if(p.getPlayerId().equalsIgnoreCase(playerId)) {
                    c=1;
                    return p;
                }
            }
            if(c==0)
                throw new PlayerNotFoundException("Not present in list");
//            return
//            return players.stream().filter(player->player.getPlayerId().equalsIgnoreCase(playerId)).findFirst().
//                    orElseThrow(()-> new PlayerNotFoundException("Not present in list"));
        }
        return null;

    }}