/*8 Crea un trigger que, si detecta que se ha incrementado el sueldo de un jugador/a, 
registre el cambio en una tabla canvis_sou_jugadors (que deberás crear) incluyendo: la id del jugador/a, 
el sueldo anterior, el sueldo nuevo y la fecha del cambio.*/


CREATE TABLE cambios_salario_jugadores (
    id INT AUTO_INCREMENT PRIMARY KEY,
    jugador_id INT NOT NULL,
    salario_anterior DECIMAL(10,2) NOT NULL,
    salario_nuevo DECIMAL(10,2) NOT NULL,
    fecha_cambio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    /*FOREIGN KEY (jugador_id) REFERENCES jugadores(persones_id)*/
);


DELIMITER //
CREATE TRIGGER registrar_cambio_salario
BEFORE UPDATE ON personas
FOR EACH ROW
BEGIN
    IF NEW.salario != OLD.salario AND NEW.tipo_persona = 'jugador' THEN
        INSERT INTO cambios_salario_jugadores (jugador_id, salario_anterior, salario_nuevo)
        VALUES (NEW.id, OLD.salario, NEW.salario);
    END IF;
END //
DELIMITER ;
 

/*9 Crea una tabla log_equips_modificats que registre cualquier cambio de presidente de un equipo. 
Implementa un trigger que inserte automáticamente un registro cada vez que haya una actualización. 
Debe registrar el nombre del equipo, el presidente anterior, 
el nuevo presidente y la fecha en que se ha producido el cambio.*/

CREATE TABLE log_equipos_modificados (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre_equipo VARCHAR(45) NOT NULL,
    presidente_anterior VARCHAR(45),
    presidente_nuevo VARCHAR(45),
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

DELIMITER //
CREATE TRIGGER registro_cambio_presidente
BEFORE UPDATE ON equipos
FOR EACH ROW
BEGIN  
    IF NEW.nom_president != OLD.nom_president THEN
        INSERT INTO log_equipos_modificados (nombre_equipo, presidente_anterior, presidente_nuevo)
        VALUES (OLD.nom, OLD.nom_president, NEW.nom_president);
    END IF;
END //
DELIMITER ;

/*10 Control de errores en inscripción de jornadas --> Crea una tabla log_errors_jornades para registrar intentos fallidos de inserción en la tabla jornadas 
(por ejemplo, si se repite el número de jornada para una misma liga). 
Añade un trigger para controlarlo.*/

CREATE TABLE log_errores_jornadas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    numero_jornada INT NOT NULL,
    liga_id INT NOT NULL,
    fecha_intento DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    mensaje_error VARCHAR(255) NOT NULL,
    usuario VARCHAR(100) DEFAULT CURRENT_USER()
);

DELIMITER //
CREATE TRIGGER control_jornadas_duplicadas
BEFORE INSERT ON jornades
FOR EACH ROW
BEGIN
    DECLARE jornada_existente INT;
    
    SELECT COUNT(*) INTO jornada_existente
    FROM jornades
    WHERE jornada = NEW.jornada
    AND lligues_id = NEW.lligues_id;
    
   
    IF jornada_existente > 0 THEN
        
        INSERT INTO log_errores_jornadas (numero_jornada, liga_id, mensaje_error)
        VALUES (NEW.jornada, NEW.lligues_id,
            CONCAT('Intento de insertar jornada duplicada: ', NEW.jornada, 
                   ' para la liga ID: ', NEW.lligues_id)
        );
        
        
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'No se puede insertar: jornada ya existe para esta liga';
    END IF;
END //
DELIMITER ;
/*11 Registro de jugadores eliminados --> Crea un trigger que, 
antes de eliminar un jugador/a, inserte sus datos en una tabla jugadors_eliminats, 
junto con la marca temporal del momento del borrado.

    ¿Funciona el trigger en todos los casos? ¿En qué casos no funciona y por qué? */


CREATE TABLE jugadors_eliminats (
    id_persona INT PRIMARY KEY,
    nom VARCHAR(45) NOT NULL,
    cognoms VARCHAR(45) NOT NULL,
    data_naixement DATE NOT NULL,
    dorsal INT NOT NULL,
    qualitat INT NOT NULL,
    posicio_id INT NOT NULL,
    data_eliminacio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuari_eliminacio VARCHAR(100) DEFAULT CURRENT_USER(),
    CONSTRAINT fk_posicio_eliminada FOREIGN KEY (posicio_id) REFERENCES posicions(id)
);

DELIMITER //
CREATE TRIGGER registrar_jugador_eliminat
BEFORE DELETE ON jugadors
FOR EACH ROW
BEGIN
    INSERT INTO jugadors_eliminats (
        id_persona, nom, cognoms, data_naixement, 
        dorsal, qualitat, posicio_id
    )
    SELECT 
        p.id, p.nom, p.cognoms, p.data_naixement,
        j.dorsal, j.qualitat, j.posicions_id
    FROM 
        persones p
    JOIN 
        jugadors j ON j.persones_id = p.id
    WHERE 
        j.persones_id = OLD.persones_id;
END //
DELIMITER ;

/*Si eliminas al jugador desde la tabla persones*/
/*Si ese id también era un jugador (tenía fila en jugadors), 
no se activará el trigger, porque está definido solo sobre la tabla jugadors, 
no sobre persones.*/

/*Si existe ON DELETE CASCADE entre persones y jugadors*/
/*Si definiste la clave foránea con ON DELETE CASCADE entre persones y jugadors, 
al borrar una persona, también se eliminará el jugador automáticamente, 
sin disparar el trigger.*/


/*---------------------------------------------------------------*/


/*12 Queremos garantizar que la eliminación de un/a entrenador/a se haga de forma segura y auditada. 
Crea un procedure que, dado el id de un entrenador/a, realice las siguientes acciones:

    Si el/la entrenador/a está actualmente asignado/a a un equipo, cierra la relación estableciendo la data_baixa con la fecha actual.

    Registra los datos del/de la entrenador/a en la tabla entrenadors_eliminats (que deberás crear), 
    incluyendo: nombre, apellidos, persones_id y el nombre del equipo con el que estaba vinculado/a, 
    si lo tenía. Si no tenía ninguno, se debe indicar "Sin equipo vigente". También debe guardar la marca temporal del momento de la eliminación.

    Elimina la fila correspondiente de la tabla entrenadors.*/

/*❗Este procedure debe ser transaccional: si cualquier paso falla, ningún cambio debe quedar aplicado en la base de datos.*/


CREATE TABLE entrenadors_eliminats (
    persones_id INT PRIMARY KEY,
    nom VARCHAR(45) NOT NULL,
    cognoms VARCHAR(45) NOT NULL,
    nom_equip VARCHAR(100) NOT NULL,
    data_eliminacio TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

DELIMITER //

CREATE PROCEDURE eliminar_entrenador_seguro(IN entrenador_id INT)
BEGIN
    DECLARE nom_entrenador VARCHAR(45);
    DECLARE cognoms_entrenador VARCHAR(45);
    DECLARE nom_equip_actual VARCHAR(100);

    START TRANSACTION;

    
    UPDATE entrenar_equips
    SET data_baixa = CURDATE()
    WHERE entrenadors_id = entrenador_id AND data_baixa IS NULL;

    
    SELECT p.nom, p.cognoms INTO nom_entrenador, cognoms_entrenador
    FROM persones p
    WHERE p.id = entrenador_id;

    
    SELECT e.nom INTO nom_equip_actual
    FROM entrenar_equips ee
    JOIN equips e ON e.id = ee.equips_id
    WHERE ee.entrenadors_id = entrenador_id AND ee.data_baixa = CURDATE()
    LIMIT 1;

    
    IF nom_equip_actual IS NULL THEN
        SET nom_equip_actual = 'Sin equipo vigente';
    END IF;

    
    INSERT INTO entrenadors_eliminats (persones_id, nom, cognoms, nom_equip)
    VALUES (entrenador_id, nom_entrenador, cognoms_entrenador, nom_equip_actual);

    DELETE FROM entrenadors WHERE persones_id = entrenador_id;

    COMMIT;
END;
//

DELIMITER ;


