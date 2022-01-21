package com.game.service;


import com.game.entity.Player;
import com.game.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PlayerService
{
    @Autowired
    PlayerRepository repository;

    public List<Player> getAllPlayers()
    {

        return (List<Player>) repository.findAll();

//        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
//
//
//        Page<Player> pagedResult = repository.findAll(paging);
//        if(pagedResult.hasContent()) {
//            return pagedResult.getContent();
//        } else {
//            return new ArrayList<Player>();
//        }
    }

    public List<Player> getAllPlayer() {


        List<Player> players = new ArrayList<Player>();

        repository.findAll().forEach(players::add);
        return players;
    }

    public ResponseEntity<Player>  getPlayerById(Long id)
    {
        if(id < 1){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Optional<Player> playerData = repository.findById(id);

        if (playerData.isPresent()) {
            return new ResponseEntity<>(playerData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }


    public boolean existsById(Long id){
       return repository.existsById(id);
    }


    public ResponseEntity<Player> createPlayer(Player player) {

        LocalDate dateStart = LocalDate.of(2000, 01, 01);
        LocalDate dateEnd = LocalDate.of(3000, 12, 31);

        if(player.getName() == null || player.getName() == "" || player.getTitle() == null || player.getTitle() == "" || player.getRace() == null
                || player.getProfession() == null || player.getBirthday() == null || player.getExperience() == null||
        player.getName().length() > 12 || player.getTitle().length() > 30 || player.getExperience() > 10000000||
                player.getBirthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().isBefore(dateStart)||
                player.getBirthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().isAfter(dateEnd)){
            return new ResponseEntity<>(player, HttpStatus.BAD_REQUEST);
        }
        else {

            Player _player = repository
                    .save(new Player(player.getName(), player.getTitle(), player.getRace(), player.getProfession(), player.getBirthday(), player.getBanned(), player.getExperience()));
            return new ResponseEntity<>(_player, HttpStatus.OK);
        }


}



    public ResponseEntity<Player> updatePlayer(Long id,  Player player) {

        if (id < 1) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else if (!repository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        } else {
            Optional<Player> playerOld = repository.findById(id);
            Player _player = playerOld.get();
            LocalDate dateStart = LocalDate.of(2000, 01, 01);
            LocalDate dateEnd = LocalDate.of(3000, 12, 31);
            if (player.getName() != null) {
                if (player.getName().length() > 0 && player.getName().length() < 13) {
                    _player.setName(player.getName());
                } else {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
            }

            if (player.getTitle() != null) {
                if (player.getTitle().length() > 0 && player.getTitle().length() < 30) {
                    _player.setTitle(player.getTitle());
                } else {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
            }


            if (player.getRace() != null) {
                _player.setRace(player.getRace());
            }
            if (player.getProfession() != null) {
                _player.setProfession(player.getProfession());
            }

            if (player.getExperience() != null) {
                if (player.getExperience() >= 0 && player.getExperience() <= 10000000) {
                    _player.setExperience(player.getExperience());
                    _player.updateLevel(_player.getExperience());
                    _player.updateUntilNextLevel(_player.getLevel());
                } else {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
            }

            _player.setBanned(player.getBanned());


            if (player.getBirthday() != null) {
                if (!player.getBirthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().isBefore(dateStart)
                        && !player.getBirthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().isAfter(dateEnd)) {
                    _player.setBirthday(player.getBirthday());
                } else {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
            }


            return new ResponseEntity<>(repository.save(_player), HttpStatus.OK);
        }
    }


    public ResponseEntity<HttpStatus>  deletePlayerById(Long id)
    {

        if (id <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {

            if (!repository.existsById(id)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            else{
                repository.deleteById(id);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }

    }


}