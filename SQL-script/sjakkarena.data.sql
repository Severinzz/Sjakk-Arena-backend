SET SQL_SAFE_UPDATES = 0;
DELETE FROM `sjakkarena`.`player`;
DELETE FROM `sjakkarena`.`tournament`;

-- -----------------------------------------------------
-- Table `sjakkarena`.`tournament`
-- -----------------------------------------------------
INSERT INTO sjakkarena.tournament (`tournament_id`, `tournament_name`, `admin_email`, `start`, `end`,
                                   `tables`, `max_rounds`, `admin_uuid`, `early_start`)
VALUES (1234, "sjakkturnering", "sjakk@turnering.no", "22:03", "23:04", 4, 5, "fsa32", 1);

-- -----------------------------------------------------
-- Table `sjakkarena`.`player`
-- -----------------------------------------------------
INSERT INTO `sjakkarena`.`player` (`name`, `tournament`)
VALUES ("Bjarne", 1234),
       ("Pål", 1234),
       ("Per", 1234),
       ("Espen", 1234);
