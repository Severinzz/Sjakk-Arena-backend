SET SQL_SAFE_UPDATES = 0;
DELETE FROM `sjakkarena`.`game`;
DELETE FROM `sjakkarena`.`player`;
DELETE FROM `sjakkarena`.`tournament`;

-- -----------------------------------------------------
-- Table `sjakkarena`.`tournament`
-- -----------------------------------------------------
INSERT INTO sjakkarena.tournament (`tournament_id`, `tournament_name`, `admin_email`, `start`, `end`,
                                   `tables`, `max_rounds`, `admin_uuid`, `early_start`)
VALUES (535937, "sjakkturnering", "sjakk@turnering.no", "2020-02-23 22:03", "2020-02-23 23:04", 4, 5, "fsa32", 1), -- jwt: eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJUT1VSTkFNRU5UIiwianRpIjoiNTM1OTM3In0.wct6xzanPitCfQ7nSMa4KMq1bIsFnp0vthRcZhRhG18
       (1923, "En annen sjakkturnering", "turnering@sjakk.com", "1996-01-01 00:00:00", "2000-01-01 00:00:00", 100, 5,
        "dfa34", 0);

-- -----------------------------------------------------
-- Table `sjakkarena`.`player`
-- -----------------------------------------------------
INSERT INTO `sjakkarena`.`player` (`player_id`, `name`, `tournament`, `icon`, `points`)
VALUES (1, "Bjarne", 535937, "fas fa-chess-pawn fa-3x", 10),
       (2, "Pål", 535937, "fas fa-chess-knight fa-3x", 9),
       (3, "Per", 535937, "fas fa-chess-king fa-3x", 8),
       (4, "Espen", 535937, "fas fa-chess-bishop fa-3x", 7),
       (5, "Hans", 1923, "fas fa-chess-knight fa-3x", 2),
       (6, "Grete", 1923, "fas fa-chess-knight fa-3x", 2),
       (48, "Ole", 535937, "fas fa-chess-bishop fa-3x", 7); -- jwt: eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJQTEFZRVIiLCJqdGkiOiI0OCJ9.-kZd6IlAA1f6nYPxyQWH3-6KfXSFRWBdwE9oH7TqqoA

-- -----------------------------------------------------
-- Table `sjakkarena`.`game`
-- -----------------------------------------------------
INSERT INTO `sjakkarena`.`game` (`table`, `start`, `white_player`, `black_player`, `active`)
VALUES (1, "2020-02-23 22:05", 48, 2, 0),
       (2, "2020-02-23 20:05", 48, 2, 0),
       (1, "2020-02-23 19:45", 1, 3, 0),
       (2, "2020-02-23 23:34", 4, 2, 1),
       (4, "1997-02-27 23:43", 5, 6, 0),
       (5, "2020-02-25 09:33", 2, 48, 0),
       (1, "2020-02-25 09:34", 3, 48, 0),
       (2, "2020-02-25 10:00", 48, 4, 1);