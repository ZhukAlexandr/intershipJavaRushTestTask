package com.game.controller;


import com.game.entity.Player;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController

public class PlayerController {


    @Autowired
    PlayerService service;

    @GetMapping("/rest/players")
    public ResponseEntity<List<Player>> getAllPlayer(HttpServletRequest request) {


        String pageSizeStr = request.getParameter("pageSize");
        Integer pageSize = pageSizeStr == null? null: Integer.valueOf(pageSizeStr);
        String playerOrderStr = request.getParameter("order");

        String name = request.getParameter("name");
        String title = request.getParameter("title");
        String race = request.getParameter("race");
        String profession = request.getParameter("profession");
        String afterStr =  request.getParameter("after");
        Long after =  afterStr == null? null:Long.valueOf(afterStr);
        String beforeStr =  request.getParameter("before");
        Long before =  beforeStr == null? null:Long.valueOf(beforeStr);
        String bannedStr = request.getParameter("banned");
        Boolean banned = bannedStr == null? null: Boolean.valueOf(bannedStr);
        String minExperienceStr = request.getParameter("minExperience");
        Integer minExperience = minExperienceStr == null? null: Integer.valueOf(minExperienceStr);
        String maxExperienceStr = request.getParameter("maxExperience");
        Integer maxExperience = maxExperienceStr == null? null: Integer.valueOf(maxExperienceStr);
        String minLevelStr = request.getParameter("minLevel");
        Integer minLevel = minLevelStr == null? null: Integer.valueOf(minLevelStr);
        String maxLevelStr = request.getParameter("maxLevel");
        Integer maxLevel = maxLevelStr == null? null: Integer.valueOf(maxLevelStr);

        String pageNumberStr = request.getParameter("pageNumber");
        Integer pageNumber = pageNumberStr == null? null: Integer.valueOf(pageNumberStr);
        if (pageNumber == null){
            pageNumber = 0;
        }
        if(pageSize==null){
            pageSize = 3;
        }
        if(playerOrderStr == null){
            playerOrderStr = "ID";
        }
        List<Player> list = service.getAllPlayers();
        list = list.stream().filter(player ->
                (name == null || player.getName().contains(name)) &&
                        (title == null || player.getTitle().contains(title)) &&
                        (race == null || player.getRace().equals(race)) &&
                        (profession == null || player.getProfession().equals(profession)) &&
                        (after == null || player.getBirthday().getTime() >= after) &&
                        (before == null || player.getBirthday().getTime() <= before) &&
                        (bannedStr == null || player.getBanned() == banned) &&
                        (minExperience == null || player.getExperience() >= minExperience) &&
                        (maxExperience == null || player.getExperience() <= maxExperience) &&
                        (minLevel == null || player.getLevel() >= minLevel) &&
                        (maxLevel == null || player.getLevel() <= maxLevel)
        ).collect(Collectors.toList());

        list = list.stream().skip(pageSize*pageNumber)
                .limit(pageSize).collect(Collectors.toList());


        return new ResponseEntity<List<Player>>(list, new HttpHeaders(), HttpStatus.OK);
    }


    @GetMapping("/rest/players/count")
    public  ResponseEntity<Integer> getAllPlayerCount(HttpServletRequest request) {

        String name = request.getParameter("name");
        String title = request.getParameter("title");
        String race = request.getParameter("race");
        String profession = request.getParameter("profession");
        String afterStr =  request.getParameter("after");
        Long after =  afterStr == null? null:Long.valueOf(afterStr);
        String beforeStr =  request.getParameter("before");
        Long before =  beforeStr == null? null:Long.valueOf(beforeStr);
        String bannedStr = request.getParameter("banned");
        Boolean banned = bannedStr == null? null: Boolean.valueOf(bannedStr);
        String minExperienceStr = request.getParameter("minExperience");
        Integer minExperience = minExperienceStr == null? null: Integer.valueOf(minExperienceStr);
        String maxExperienceStr = request.getParameter("maxExperience");
        Integer maxExperience = maxExperienceStr == null? null: Integer.valueOf(maxExperienceStr);
        String minLevelStr = request.getParameter("minLevel");
        Integer minLevel = minLevelStr == null? null: Integer.valueOf(minLevelStr);
        String maxLevelStr = request.getParameter("maxLevel");
        Integer maxLevel = maxLevelStr == null? null: Integer.valueOf(maxLevelStr);

        List<Player> players = service.getAllPlayer();

        players = players.stream().filter(player ->
                (name == null || player.getName().contains(name)) &&
                        (title == null || player.getTitle().contains(title)) &&
                        (race == null || player.getRace().equals(race)) &&
                        (profession == null || player.getProfession().equals(profession)) &&
                        (after == null || player.getBirthday().getTime() >= after) &&
                        (before == null || player.getBirthday().getTime() <= before) &&
                        (bannedStr == null || player.getBanned() == banned) &&
                        (minExperience == null || player.getExperience() >= minExperience) &&
                        (maxExperience == null || player.getExperience() <= maxExperience) &&
                        (minLevel == null || player.getLevel() >= minLevel) &&
                        (maxLevel == null || player.getLevel() <= maxLevel)
        ).collect(Collectors.toList());
        return new ResponseEntity<Integer>(players.size(), HttpStatus.OK);

    }
    //
//
    @GetMapping("/rest/players/{id}")
    public ResponseEntity<Player> getPlayerById(@PathVariable("id") long id) {
        return  service.getPlayerById(id);
    }

    @PostMapping("/rest/players/{id}")
    public ResponseEntity<Player> updatePlayer(@PathVariable("id") Long id, @RequestBody Player player) {

        return service.updatePlayer(id, player);
    }

    @PostMapping("/rest/players")
    public ResponseEntity<Player> createPlayer(@RequestBody Player player) {
            return service.createPlayer(player);
    }

    @DeleteMapping("/rest/players/{id}")
    public ResponseEntity<HttpStatus> delPlayer(@PathVariable("id") long id) {
        return  service.deletePlayerById(id);
    }
}
