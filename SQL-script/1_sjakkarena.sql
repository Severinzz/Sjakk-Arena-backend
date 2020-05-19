-- MySQL Script generated by MySQL Workbench
-- Wed Feb  5 15:00:07 2020
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0;
SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0;
SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE =
    'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema sjakkarena
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS sjakkarena;

-- -----------------------------------------------------
-- Schema sjakkarena
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS sjakkarena DEFAULT CHARACTER SET utf8;
USE sjakkarena;

-- -----------------------------------------------------
-- Table `sjakkarena`.`tournament`
-- -----------------------------------------------------
DROP TABLE IF EXISTS sjakkarena.`tournament`;

CREATE TABLE IF NOT EXISTS sjakkarena.`tournament`
(
  `tournament_id`   INT               NOT NULL,
  `tournament_name` VARCHAR(255),
  `admin_email`     VARCHAR(255),
  `start`           DATETIME          NULL,
  `end`             DATETIME          NULL,
  `tables`          TINYINT UNSIGNED  NULL,
  `max_rounds`      SMALLINT UNSIGNED NULL,
  `active`          TINYINT(1) DEFAULT 0,
  `admin_uuid`      VARCHAR(255)      NULL UNIQUE,
  `early_start`     TINYINT(1) DEFAULT 0,
  `finished`        TINYINT(1) DEFAULT 0,
  `salt`            VARCHAR(255),
  PRIMARY KEY (`tournament_id`)
)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `sjakkarena`.`player`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `sjakkarena`.`player`;

CREATE TABLE IF NOT EXISTS `sjakkarena`.`player`
(
  `player_id`     INT          NOT NULL AUTO_INCREMENT,
  `name`          VARCHAR(45)  NULL,
  `in_tournament` TINYINT(1) UNSIGNED DEFAULT 1,
  `paused`        TINYINT(1) UNSIGNED DEFAULT 0,
  `tournament`    INT          NOT NULL,
  `icon`          VARCHAR(255) NOT NULL,
  `bib_number`    FLOAT        NULL,
  PRIMARY KEY (`player_id`),
  UNIQUE INDEX `Id_UNIQUE` (`player_id` ASC) VISIBLE,
  UNIQUE KEY `name_tournament` (`name`, `tournament`),
  INDEX `fk_user_tournament_idx` (`tournament` ASC) VISIBLE,
  CONSTRAINT `fk_user_tournament`
    FOREIGN KEY (`tournament`)
      REFERENCES sjakkarena.`tournament` (`tournament_id`)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION
)
  ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `sjakkarena`.`game`
-- -----------------------------------------------------
DROP TABLE IF EXISTS sjakkarena.`game`;

CREATE TABLE IF NOT EXISTS sjakkarena.`game`
(
  `game_id`             INT                    NOT NULL AUTO_INCREMENT,
  `table`               TINYINT UNSIGNED       NULL,
  `start`               DATETIME               NULL,
  `end`                 DATETIME               NULL,
  `white_player`        INT                    NOT NULL,
  `black_player`        INT                    NOT NULL,
  `white_player_points` DECIMAL(2, 1) UNSIGNED NULL,
  `active`              TINYINT(1) DEFAULT 0,
  `valid_result`        TINYINT(1) DEFAULT 1,
  PRIMARY KEY (`game_id`),
  INDEX `fk_game_white_idx` (`white_player` ASC) VISIBLE,
  INDEX `fk_game_black_idx` (`black_player` ASC) VISIBLE,
  CONSTRAINT `fk_game_white`
    FOREIGN KEY (`white_player`)
      REFERENCES sjakkarena.`player` (`player_id`)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION,
  CONSTRAINT `fk_game_black`
    FOREIGN KEY (`black_player`)
      REFERENCES sjakkarena.`player` (`player_id`)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION
)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `sjakkarena`.`image`
-- -----------------------------------------------------
DROP TABLE IF EXISTS sjakkarena.`image`;

CREATE TABLE IF NOT EXISTS sjakkarena.`image`
(
    `image_id`            INT                    NOT NULL AUTO_INCREMENT,
    `fileName`            VARCHAR(255)           NOT NULL UNIQUE,
    `playerId`            INT                    NOT NULL,
    `gameId`              INT                    NOT NULL,
    `time_uploaded`       DATETIME               NULL,
    PRIMARY KEY (`image_id`),
    INDEX `fk_player_idx` (`playerId` ASC) VISIBLE,
    INDEX `fk_game_idx` (`gameId` ASC) VISIBLE,
    CONSTRAINT `fk_player`
        FOREIGN KEY (`playerId`)
            REFERENCES sjakkarena.`player` (`player_id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `fk_game`
        FOREIGN KEY (`gameId`)
            REFERENCES sjakkarena.`game` (`game_Id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB;

SET SQL_MODE = @OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
--  Functions
-- -----------------------------------------------------
-- -----------------------------------------------------
--  get_last_played_color
-- -----------------------------------------------------

DROP FUNCTION IF EXISTS sjakkarena.get_last_played_color;
DELIMITER //
CREATE FUNCTION sjakkarena.get_last_played_color(player_id INT(11))
  RETURNS VARCHAR(255)
  READS SQL DATA
BEGIN
  DECLARE last_played_color VARCHAR(255) DEFAULT '';
  SET last_played_color = (SELECT CAST(
                                      CASE
                                        WHEN white_player = player_id
                                          THEN 'white'
                                        ELSE 'black'
                                        END AS char) AS last_played_color
                           FROM `sjakkarena`.`game`
                           WHERE (`white_player` = player_id OR `black_player` = player_id)
                           ORDER BY `start` DESC
                           LIMIT 1);
  RETURN last_played_color;
END//
DELIMITER ;

-- -----------------------------------------------------
--  get_rounds
-- -----------------------------------------------------
DROP FUNCTION IF EXISTS sjakkarena.get_rounds;
DELIMITER //
CREATE FUNCTION sjakkarena.get_rounds(player_id INT(11))
  RETURNS INT
  READS SQL DATA
BEGIN
  DECLARE rounds INT DEFAULT 0;
  SET rounds = (SELECT COUNT(*)
                FROM `sjakkarena`.`game`
                WHERE (`white_player` = player_id OR `black_player` = player_id)
  );
  RETURN rounds;
END//
DELIMITER ;

-- -----------------------------------------------------
--  get_points
-- -----------------------------------------------------
DROP FUNCTION IF EXISTS sjakkarena.get_points;
DELIMITER //
CREATE FUNCTION sjakkarena.get_points(player_id INT(11))
  RETURNS DECIMAL(5, 1)
  READS SQL DATA
BEGIN
  DECLARE points DECIMAL(5, 1) DEFAULT 0;
  SET points = (SELECT SUM(CASE
                             WHEN white_player = player_id
                               THEN white_player_points
                             ELSE (1 - white_player_points)
                END)
                FROM `sjakkarena`.`game`
                WHERE (`white_player` = player_id OR `black_player` = player_id) AND `active` = 0
  );
  RETURN points;
END//
DELIMITER ;

-- -----------------------------------------------------
--  get_number_of_white_games
-- -----------------------------------------------------
DROP FUNCTION IF EXISTS sjakkarena.get_number_of_white_games;
DELIMITER //
CREATE FUNCTION sjakkarena.get_number_of_white_games(player_id INT(11))
  RETURNS INT
  READS SQL DATA
BEGIN
  DECLARE number_of_white_games INT DEFAULT 0;
  SET number_of_white_games = (SELECT COUNT(*)
                               FROM `sjakkarena`.`game`
                               WHERE white_player = player_id);
  RETURN number_of_white_games;
END//
DELIMITER ;

-- -----------------------------------------------------
--  get_last_played_color_streak
-- -----------------------------------------------------
DROP FUNCTION IF EXISTS sjakkarena.get_last_played_color_streak;
DELIMITER //
CREATE FUNCTION sjakkarena.get_last_played_color_streak(player_id INT(11))
  RETURNS INT(11)
  READS SQL DATA
BEGIN
  DECLARE color VARCHAR(255) DEFAULT '';
  DECLARE last_played_color_streak INT DEFAULT 0;
  DECLARE last_played_color VARCHAR(255) DEFAULT '';
  SET last_played_color = get_last_played_color(player_id);
  SET color = last_played_color;
  WHILE color = last_played_color
  DO
  SET last_played_color_streak = last_played_color_streak + 1;
  SET color = (SELECT CASE
                        WHEN white_player = player_id
                          THEN 'white'
                        ELSE 'black'
                        END
               FROM `sjakkarena`.`game`
               WHERE (white_player = player_id OR black_player = player_id)
               ORDER BY start DESC
               LIMIT 1
                 OFFSET last_played_color_streak) ;
  END WHILE;
  RETURN last_played_color_streak;
END//
DELIMITER ;

-- -----------------------------------------------------
--  get_random_bib_number
-- -----------------------------------------------------
DROP FUNCTION IF EXISTS sjakkarena.get_random_bib_number;
DELIMITER //
CREATE FUNCTION sjakkarena.get_random_bib_number(tournament_id INT(11))
  RETURNS FLOAT
  READS SQL DATA
BEGIN
  DECLARE bib_number FLOAT;
  DECLARE i INT DEFAULT 0;
  SET bib_number = RAND();
  WHILE (i < POWER(2, 32)) AND EXISTS(SELECT player.bib_number
                                      FROM sjakkarena.player
                                      WHERE player.tournament = tournament_id
                                        AND player.bib_number = bib_number)
  DO
  SET i = i + 1;
  SET bib_number = RAND();
  END WHILE;
  RETURN bib_number;
END//
DELIMITER ;

-- -----------------------------------------------------
--  Stored Procedures
-- -----------------------------------------------------

-- -----------------------------------------------------
--  get_player
-- -----------------------------------------------------
DROP PROCEDURE IF EXISTS sjakkarena.get_player;
DELIMITER //
CREATE PROCEDURE sjakkarena.get_player(IN `player_id` INT(11))
BEGIN
  SELECT `player`.*,
         get_points(player_id)                AS `points`,
         get_rounds(player_id)                AS `rounds`,
         get_last_played_color_streak(player_id)     AS `last_played_color_streak`,
         get_last_played_color(player_id)     AS `last_played_color`,
         get_number_of_white_games(player_id) AS `number_of_white_games`
  FROM `sjakkarena`.`player`
  WHERE `player`.`player_id` = `player_id`;
END//
DELIMITER ;

-- -----------------------------------------------------
--  get_players_in_tournament
-- -----------------------------------------------------
DROP PROCEDURE IF EXISTS sjakkarena.get_players_in_tournament;
DELIMITER //
CREATE PROCEDURE sjakkarena.get_players_in_tournament(IN tournament_id INT(11))
BEGIN
  SELECT `player`.*,
         get_points(player_id)                AS `points`,
         get_rounds(player_id)                AS `rounds`,
         get_last_played_color_streak(player_id)     AS `last_played_color_streak`,
         get_last_played_color(player_id)     AS last_played_color,
         get_number_of_white_games(player_id) AS `number_of_white_games`
  FROM `sjakkarena`.`player`
  WHERE `tournament` = `tournament_id`
    AND `in_tournament` = 1;
END//
DELIMITER ;

-- -----------------------------------------------------
--  get_players_in_tournament_ready_to_play
-- -----------------------------------------------------
DROP PROCEDURE IF EXISTS sjakkarena.get_players_in_tournament_ready_to_play;
DELIMITER //
CREATE PROCEDURE sjakkarena.get_players_in_tournament_ready_to_play(IN tournament_id INT(11))
BEGIN
  SELECT DISTINCT `player`.*,
                  get_points(player_id)                AS `points`,
                  get_rounds(player_id)                AS `rounds`,
                  get_last_played_color_streak(player_id)     AS `last_played_color_streak`,
                  get_last_played_color(player_id)     AS `last_played_color`,
                  get_number_of_white_games(player_id) AS `number_of_white_games`
  FROM `sjakkarena`.`player`, `sjakkarena`.`tournament`
  WHERE `player`.`tournament` = tournament_id
    AND `tournament`.`tournament_id` = `player`.`tournament`
    AND `player`.`in_tournament` = 1
    AND `player`.`paused` = 0
    AND get_rounds(player_id) < `tournament`.`max_rounds`
    AND player_id NOT IN (SELECT DISTINCT white_player
                          FROM `sjakkarena`.`game`
                          WHERE active = 1
                          UNION
                          SELECT DISTINCT black_player
                          FROM `sjakkarena`.`game`
                          WHERE active = 1);

END//
DELIMITER ;

-- -----------------------------------------------------
--  get_leader_board
-- -----------------------------------------------------
DROP PROCEDURE IF EXISTS sjakkarena.get_leader_board;
DELIMITER //
CREATE PROCEDURE sjakkarena.get_leader_board(IN tournament_id INT(11))
BEGIN
  SELECT `player`.*,
         get_points(player_id)                AS `points`,
         get_rounds(player_id)                AS `rounds`,
         get_last_played_color_streak(player_id)     AS `last_played_color_streak`,
         get_last_played_color(player_id)     AS last_played_color,
         get_number_of_white_games(player_id) AS number_of_white_games
  FROM `sjakkarena`.`player`
  WHERE `player`.`tournament` = `tournament_id`
  ORDER BY `points` DESC;
END//
DELIMITER ;


-- -----------------------------------------------------
--  get_available_tables
-- -----------------------------------------------------
DROP PROCEDURE IF EXISTS sjakkarena.get_available_tables;
DELIMITER //
CREATE PROCEDURE sjakkarena.get_available_tables(IN `tournament_id` INT(11))
BEGIN
  DECLARE number_of_tables INT DEFAULT 1;
  DECLARE i INT DEFAULT 1;
  SET number_of_tables = (
    SELECT tables
    FROM `sjakkarena`.`tournament`
    WHERE `tournament`.`tournament_id` = `tournament_id`);
  START TRANSACTION
    ;
    DROP TABLE IF EXISTS `tables`;
    CREATE TEMPORARY TABLE `tables`
    (
      table_nr INT(11) DEFAULT NULL
    )
      ENGINE = MEMORY
      DEFAULT CHARSET = utf8;
    WHILE i <= number_of_tables
    DO
    INSERT INTO `tables`
    VALUES (i);
    SET i = i + 1;
    END WHILE;
    SELECT *
    FROM tables
    WHERE table_nr NOT IN (SELECT DISTINCT `table`
                           FROM sjakkarena.game,
                                sjakkarena.player
                           WHERE active = 1
                             AND player_id = white_player
                             AND player.tournament = tournament_id);
  COMMIT;
END//
DELIMITER ;


-- -----------------------------------------------------
--  get_previous_opponents
-- -----------------------------------------------------

DROP PROCEDURE IF EXISTS sjakkarena.get_previous_opponents;
DELIMITER //
CREATE PROCEDURE sjakkarena.get_previous_opponents(IN player_id INT(11))
BEGIN
  SELECT DISTINCT CASE
                    WHEN white_player = player_id
                      THEN black_player
                    ELSE white_player
                    END AS opponent
  FROM `sjakkarena`.`game`
  WHERE (white_player = player_id OR black_player = player_id);
END//
DELIMITER ;

-- -----------------------------------------------------
--  insert_player
-- -----------------------------------------------------
DROP PROCEDURE IF EXISTS sjakkarena.insert_player;
DELIMITER //
CREATE PROCEDURE sjakkarena.insert_player(IN `name` VARCHAR(255), IN `tournament` INT, IN `icon` VARCHAR(255))
BEGIN
  INSERT INTO sjakkarena.player (`player`.`name`, `player`.`tournament`, `player`.`icon`, `player`.`bib_number`)
  VALUES (`name`, `tournament`, `icon`, get_random_bib_number(tournament));
END//
DELIMITER ;
