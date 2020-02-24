SET SQL_SAFE_UPDATES = 0;
DELETE FROM `sjakkarena`.`game`;
DELETE FROM `sjakkarena`.`player`;
DELETE FROM `sjakkarena`.`tournament`;

-- -----------------------------------------------------
-- Table `sjakkarena`.`tournament`
-- -----------------------------------------------------
INSERT INTO sjakkarena.tournament (`tournament_id`, `tournament_name`, `admin_email`, `start`, `end`,
                                   `tables`, `max_rounds`, `admin_uuid`, `early_start`)
VALUES (535937, "sjakkturnering", "sjakk@turnering.no", "22:03", "23:04", 4, 5, "fsa32", 1); -- jwt: eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJUT1VSTkFNRU5UIiwianRpIjoiNTM1OTM3In0.wct6xzanPitCfQ7nSMa4KMq1bIsFnp0vthRcZhRhG18

-- -----------------------------------------------------
-- Table `sjakkarena`.`player`
-- -----------------------------------------------------
INSERT INTO `sjakkarena`.`player` (`player_id`, `name`, `tournament`, `icon`)
VALUES (1, "Bjarne", 535937, "fas fa-chess-pawn"),
       (2, "Pål", 535937, "fas fa-chess-knight"),
       (3, "Per", 535937, "fas fa-chess-king"),
       (4, "Espen", 535937, "fas fa-chess-bishop"),
       (48, "Ole", 535937, "fas fa-chess-bishop"); -- jwt: eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJQTEFZRVIiLCJqdGkiOiI0OCJ9.-kZd6IlAA1f6nYPxyQWH3-6KfXSFRWBdwE9oH7TqqoA

-- -----------------------------------------------------
-- Table `sjakkarena`.`game`
-- -----------------------------------------------------
INSERT INTO `sjakkarena`.`GAME` (`table`, `start`, `white_player`, `black_player`, `active`)
VALUES (1, "22:05", 48, 2, 1),
       (2, "20:05", 48, 2, 0);
