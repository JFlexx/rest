SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';
CREATE SCHEMA IF NOT EXISTS `myDeptosdb` DEFAULT CHARACTER SET latin1;
USE `myDeptosdb` ;

-- -----------------------------------------------------
-- Table `myDeptosdb`.`Usuarios`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `myDeptosdb`.`Usuarios` (
	`id` INT NOT NULL AUTO_INCREMENT ,
	`name` VARCHAR(45) NOT NULL ,
	`email` VARCHAR(45) NOT NULL,
	`edad` INT NOT NULL,
	`uname` VARCHAR(45) NOT NULL,
	 PRIMARY KEY (`id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `myDeptosdb`.`Lecturas`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `myDeptosdb`.`Lecturas` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`idLectura` INT NULL,
	`fecha_lectura` date DEFAULT NULL,
	`calificacion` int NULL,
	`autor` VARCHAR(45) NOT NULL,
	`categoria` VARCHAR(45) NOT NULL,
	PRIMARY KEY (`id`),
	INDEX `idLectura_idx` (`idLectura` ASC),
	CONSTRAINT `idLectura` 
		FOREIGN KEY (`idLectura`)
		REFERENCES `myDeptosdb`.`Usuarios` (`id`) 
		ON DELETE NO ACTION 
		ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `myDeptosdb`.`Amigos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `myDeptosdb`.`Amigos` (
	`idAmigo` INT NULL,
	`idAmistad` INT NULL,
	INDEX `fk_Amistad_1_idx` (`idAmigo` ASC),
	INDEX `fk_Amistad_2_idx` (`idAmistad` ASC),
	CONSTRAINT `fk_Amistad_1`
		FOREIGN KEY (`idAmigo`)
		REFERENCES `myDeptosdb`.`Usuarios` (`id`)
		ON DELETE CASCADE
		ON UPDATE CASCADE,
	CONSTRAINT `fk_Amistad_2`
		FOREIGN KEY (`idAmistad`)
		REFERENCES `myDeptosdb`.`Usuarios` (`id`)
		ON DELETE CASCADE
		ON UPDATE CASCADE)
ENGINE = InnoDB;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

/* Datos necesarios para insertar en las tablas */
/* Inserción de datos en la BBDD de Usuarios */
INSERT INTO myDeptosdb.Usuarios(name,email,edad,uname) VALUES ('Pepe','pepe@gmail.com','21','Pepe99');
INSERT INTO myDeptosdb.Usuarios(name,email,edad,uname) VALUES ('Jason','Jason@gmail.com','22','Jason99');
INSERT INTO myDeptosdb.Usuarios(name,email,edad,uname) VALUES ('Henry','Henry@gmail.com','23','Henry99');

/* Inserción de datos en la BBDD de Lecturas(2 lecturas por usuario) */
INSERT INTO myDeptosdb.Lecturas(idLectura,fecha_lectura,calificacion,autor,categoria) VALUES ('1','2004-02-05','5','Cervantez','Novela');
INSERT INTO myDeptosdb.Lecturas(idLectura,fecha_lectura,calificacion,autor,categoria) VALUES ('1','2004-04-05','2','Ramirez','Arte');
INSERT INTO myDeptosdb.Lecturas(idLectura,fecha_lectura,calificacion,autor,categoria) VALUES ('2','2009-07-11','6','Marquez','Novela');
INSERT INTO myDeptosdb.Lecturas(idLectura,fecha_lectura,calificacion,autor,categoria) VALUES ('2','2009-10-10','8','Gabriel Garcia','Novela');
INSERT INTO myDeptosdb.Lecturas(idLectura,fecha_lectura,calificacion,autor,categoria) VALUES ('3','2005-02-05','10','Charles','Novela');
INSERT INTO myDeptosdb.Lecturas(idLectura,fecha_lectura,calificacion,autor,categoria) VALUES ('3','2010-03-05','7','Franz','Musical');
INSERT INTO myDeptosdb.Lecturas(id,idLectura,fecha_lectura,calificacion,autor,categoria) VALUES ('8','1','2010-03-05','7','Franz','Musical');
INSERT INTO myDeptosdb.Lecturas(idLectura,fecha_lectura,calificacion,autor,categoria) VALUES ('1','2010-03-05','10','Charles','Novela');



/* Inserción de datos en la BBDD de amigos(las relaciones de amistad son recíprocas) */
SELECT * FROM myDeptosdb.Amigos;
INSERT INTO myDeptosdb.Amigos(idAmigo,idAmistad) VALUES ('1','2');
INSERT INTO myDeptosdb.Amigos(idAmigo,idAmistad) VALUES ('1','3');







