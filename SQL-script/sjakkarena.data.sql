SET SQL_SAFE_UPDATES = 0;
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
INSERT INTO `sjakkarena`.`player` (`name`, `tournament`)
VALUES ("Bjarne", 535937),
       ("Pål", 535937),
       ("Per", 535937),
       ("Espen", 535937);
