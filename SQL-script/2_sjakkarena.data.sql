SET SQL_SAFE_UPDATES = 0;
DELETE FROM `sjakkarena`.`game`;
DELETE FROM `sjakkarena`.`player`;
DELETE FROM `sjakkarena`.`tournament`;

-- -----------------------------------------------------
-- Table `sjakkarena`.`tournament`
-- -----------------------------------------------------
INSERT INTO sjakkarena.tournament (`tournament_id`, `tournament_name`, `admin_email`, `start`, `end`,
                                   `tables`, `max_rounds`, `admin_uuid`, `early_start`, `active`, `salt`)
VALUES (530508, "sjakkturnering", "sjakk@turnering.no", "2020-02-23 22:03", "2020-02-23 23:04", 100, 5,
        "R2szX7qXNxagY/jNQXw/XA==", 1, 1, "c2A+vKgVPJsvwjr+VoGd"); -- adminUUID: ugKon

-- -----------------------------------------------------
-- Table `sjakkarena`.`player`
-- -----------------------------------------------------
INSERT INTO `sjakkarena`.`player` (`player_id`, `name`, `tournament`, `icon`)
VALUES (1, "Bjarne", 530508, "fas fa-chess-pawn fa-3x"),
       (2, "Pål", 530508, "fas fa-chess-knight fa-3x"),
       (3, "Per", 530508, "fas fa-chess-king fa-3x"),
       (4, "Espen", 530508, "fas fa-chess-bishop fa-3x"),
       (5, "Hans", 530508, "fas fa-chess-knight fa-3x"),
       (6, "Grete", 530508, "fas fa-chess-knight fa-3x"),
       (7, "123", 530508, "fas fa-chess-knight fa-3x"),
       (133, "Ole", 530508, "fas fa-chess-bishop fa-3x");

-- -----------------------------------------------------
-- Table `sjakkarena`.`game`
-- -----------------------------------------------------
INSERT INTO `sjakkarena`.`game` (`game_id`, `table`, `start`, `white_player`, `black_player`, `active`, `white_player_points`)
VALUES (1, 1, "2020-02-23 22:05", 133, 2, 0, 1),
       (2, 2, "2020-02-23 20:05", 133, 2, 0, 1),
       (3, 1, "2020-02-23 19:45", 1, 3, 0, 0),
       (4, 2, "2020-02-23 23:34", 4, 2, 1, 0.5),
       (5, 4, "1997-02-27 23:43", 5, 6, 0, 0.5),
       (6, 5, "2020-02-25 09:33", 2, 133, 0, 1),
       (7, 1, "2020-02-25 09:34", 3, 133, 0, 1),
       (8, 4, "2020-02-25 10:00", 133, 4, 1, 0.5);
