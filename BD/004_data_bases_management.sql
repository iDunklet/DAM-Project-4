/*13 Estadísticas de un jugador/a → Crea un procedimiento que muestre el número total de goles y el número de partidos jugados 
por un jugador/a dado (proporciona la id del jugador/a).*/

DELIMITER //

CREATE PROCEDURE estadistiques_jugador(IN jugador_id INT)
BEGIN
  SELECT 
    jugador.nom,
    COUNT(DISTINCT participacio_jugador.partit_id) AS partits_jugats,
    SUM(COALESCE(participacio_jugador.gols, 0)) AS total_gols
  FROM jugador
  LEFT JOIN participacio_jugador ON jugador.id = participacio_jugador.jugador_id
  WHERE jugador.id = jugador_id
  GROUP BY jugador.nom;
END //

DELIMITER ;

/*14 Reasignar entrenador/a → Crea un procedimiento que, dado un identificador de equipo y uno de entrenador/a, 
finalice automáticamente el contrato vigente (añadiendo la fecha de baja) y cree una nueva relación con el equipo indicado. 
El procedimiento debe ser transaccional.*/

DELIMITER //

CREATE PROCEDURE reassignar_entrenador(
  IN equip_id_nou INT,
  IN entrenador_id_nou INT
)
BEGIN
  UPDATE entrenament
  SET data_baixa = CURRENT_DATE
  WHERE equip_id = equip_id_nou
    AND data_baixa IS NULL;

  INSERT INTO entrenament (entrenador_id, equip_id, data_alta)
  VALUES (entrenador_id_nou, equip_id_nou, CURRENT_DATE);
END //

DELIMITER ;

/*15 Estadística de goles por jugador/a → Crea un procedimiento que reciba el nombre de una liga, 
calcule cuántos jugadores han marcado más de 10, 20 y 30 goles, 
e inserte la información en una nueva tabla golejadors, con la siguiente estructura:*/

CREATE TABLE golejadors (
  id INT AUTO_INCREMENT PRIMARY KEY,
  categoria VARCHAR(20), 
  total_jugadors INT,
  lliga VARCHAR(100),
  data_calcul TIMESTAMP
);

DELIMITER //

CREATE PROCEDURE estadistica_golejadors_per_lliga(IN nom_lliga VARCHAR(100))
BEGIN
  DECLARE jugadors_10 INT DEFAULT 0;
  DECLARE jugadors_20 INT DEFAULT 0;
  DECLARE jugadors_30 INT DEFAULT 0;

  -- Calcular cuántos jugadores tienen más de 10 goles en la liga
  SELECT COUNT(*) INTO jugadors_10
  FROM (
    SELECT participacio_jugador.jugador_id, SUM(participacio_jugador.gols) AS total
    FROM participacio_jugador
    JOIN jugador ON participacio_jugador.jugador_id = jugador.id
    JOIN equip ON jugador.equip_id = equip.id
    JOIN lliga ON equip.lliga_id = lliga.id
    WHERE lliga.nom = nom_lliga
    GROUP BY participacio_jugador.jugador_id
    HAVING total > 10
  ) AS t1;

  -- 20
  SELECT COUNT(*) INTO jugadors_20
  FROM (
    SELECT participacio_jugador.jugador_id, SUM(participacio_jugador.gols) AS total
    FROM participacio_jugador
    JOIN jugador ON participacio_jugador.jugador_id = jugador.id
    JOIN equip ON jugador.equip_id = equip.id
    JOIN lliga ON equip.lliga_id = lliga.id
    WHERE lliga.nom = nom_lliga
    GROUP BY participacio_jugador.jugador_id
    HAVING total > 20
  ) AS t2;

  -- 30
  SELECT COUNT(*) INTO jugadors_30
  FROM (
    SELECT participacio_jugador.jugador_id, SUM(participacio_jugador.gols) AS total
    FROM participacio_jugador
    JOIN jugador ON participacio_jugador.jugador_id = jugador.id
    JOIN equip ON jugador.equip_id = equip.id
    JOIN lliga ON equip.lliga_id = lliga.id
    WHERE lliga.nom = nom_lliga
    GROUP BY participacio_jugador.jugador_id
    HAVING total > 30
  ) AS t3;

  -- Insertar los resultados en la tabla golejadors
  INSERT INTO golejadors (categoria, total_jugadors, lliga, data_calcul)
  VALUES 
    ('+10 gols', jugadors_10, nom_lliga, NOW()),
    ('+20 gols', jugadors_20, nom_lliga, NOW()),
    ('+30 gols', jugadors_30, nom_lliga, NOW());
END //

DELIMITER ;








/*16 Transferir jugador/a --> Crea un procedure que permita transferir a un jugador/a de un equipo a otro. 
El sistema debe verificar que el jugador/a y ambos equipos existen y que el jugador/a está actualmente vinculado 
al primer equipo proporcionado por parámetro. El procedimiento debe actualizar la fecha de baja en el antiguo equipo 
e insertar una nueva alta en el nuevo equipo. Todo el proceso debe realizarse dentro de una transacción.*/

DELIMITER //

CREATE PROCEDURE transferir_jugador(
    IN p_jugador_id INT,
    IN p_equip_origen_id INT,
    IN p_equip_desti_id INT,
    IN p_data_transfer DATE
)
BEGIN
    DECLARE jugador_exists INT;
    DECLARE equip_origen_exists INT;
    DECLARE equip_desti_exists INT;
    DECLARE jugador_vinculat INT;
    
    START TRANSACTION;
    
    
    SELECT COUNT(*) INTO jugador_exists FROM jugadors WHERE persones_id = p_jugador_id;
    
  
    SELECT COUNT(*) INTO equip_origen_exists FROM equips WHERE id = p_equip_origen_id;
    
  
    SELECT COUNT(*) INTO equip_desti_exists FROM equips WHERE id = p_equip_desti_id;
    
    -- Verificar si el jugador está vinculado al equipo origen
    SELECT COUNT(*) INTO jugador_vinculat FROM jugadors_equips 
    WHERE jugadors_id = p_jugador_id 
    AND equips_id = p_equip_origen_id 
    AND (data_baixa IS NULL OR data_baixa > p_data_transfer);
    
    IF jugador_exists = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El jugador no existe';
    ELSEIF equip_origen_exists = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El equipo origen no existe';
    ELSEIF equip_desti_exists = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El equipo destino no existe';
    ELSEIF jugador_vinculat = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El jugador no está vinculado al equipo origen';
    ELSE
        -- Actualizar 
        UPDATE jugadors_equips 
        SET data_baixa = p_data_transfer 
        WHERE jugadors_id = p_jugador_id 
        AND equips_id = p_equip_origen_id 
        AND (data_baixa IS NULL OR data_baixa > p_data_transfer);
        
        -- Insertar 
        INSERT INTO jugadors_equips (data_fitxatge, jugadors_id, equips_id, data_baixa)
        VALUES (p_data_transfer, p_jugador_id, p_equip_desti_id, NULL);
        
        COMMIT;
    END IF;
END //

DELIMITER ;