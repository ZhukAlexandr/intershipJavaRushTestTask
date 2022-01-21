package com.game.repository;

import com.game.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface PlayerRepository  extends JpaRepository<Player, Long> {
    //Player findById(Long id);

}
