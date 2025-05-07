/*
1. Control de entrenador único --> 
Crea un trigger que impida que un equipo tenga más de un entrenador activo al mismo tiempo, 
es decir, solo puede haber un entrenador sin fecha de baja registrada en la tabla 'entrenar_equips'. 
Muestra un mensaje de error con la instrucción SIGNAL.
*/
DELIMITER //
create trigger entrenador_unico
Before insert on entrenador_equips
for each row
begin
	declare num_activos int;
    
    select count(*) into num_activos
    from entrenar equips
    where equips_id = new.equips_id
		and data_baixa is null;
	
    if num_activos >= 1 then
		signal sqlstate '45000' /*lanzar error*/
        set message_text = 'Ya existe un entrenador activo para este equipo';
	end if;
end //
DELIMITER ;

/*
2. Control de capacidad de los estadios --> 
Crea un trigger que, al añadir o modificar un estadio, verifique que la capacidad (num_espectadors) 
sea mayor de 5.000 y menor de 100.000. En caso contrario, establecer el valor mínimo o máximo, según corresponda.
*/

DELIMITER //
create trigger control_de_capacidad_estadis
before insert on estadis 
for each row 
begin
	if new.num_espectadors < 5000 then
		set new.num_espectadors = 5000;
	elseif new.num_espectadors > 100000 then
		set new.num_espectadors = 100000;
	end if;
end//
DELIMITER ;
DELIMITER //
create trigger control_de_capacidad_update
before update on estadis 
for each row 
begin
	if new.num_espectadors < 5000 then
		set new.num_espectadors = 5000;
	elseif new.num_espectadors > 100000 then
		set new.num_espectadors = 100000;
	end if;
end//
DELIMITER ;

/*
3. Control de entrenador con contrato vigente --> 
Crea un trigger que impida que un entrenador que ya está asignado actualmente a un equipo 
(fecha de baja nula) pueda ser asignado de nuevo sin cerrar la relación anterior. 
Muestra un mensaje de error con la instrucción SIGNAL.
*/

DELIMITER //
create trigger entrenador_contrato_vigente
before insert on entrenar_equips
for each row
begin
	if exists( /*verifcar si contrato activo*/
		select 1 /*Existe al menos una fila que cumpla la condición?¿*/
        from entrenar_equips
		where entrenadors_id = new.entrenadors_id
        and data_baixa is null
    ) then 
		signal sqlstate '45000'
        set message_text = 'este entrenador ya tiene un contrato activo'
DELIMITER ;

/*
4. Versión alternativa del punto anterior --> 
¿Eres capaz de modificar el trigger anterior para que, si esto ocurre, 
se finalice la asignación actual (estableciendo la fecha de baja) 
antes de proceder con la nueva?
*/

DELIMITER //

DELIMITER ;

/*
5. Normalización de nombres --> 
Crea un trigger que, cada vez que se añadan o modifiquen jugadores y entrenadores, 
se asegure de que el nombre y los apellidos se almacenen con la primera letra en mayúscula 
y el resto en minúscula (ejemplo: jOaN viDal → Joan Vidal).
*/

/*
6. Límite de jugadores por equipo --> 
Crea un trigger que, antes de asignar un nuevo jugador/a a un equipo, 
compruebe que dicho equipo no supere los 25 jugadores activos (sin fecha de baja). 
En caso contrario, impide la inserción y muestra un mensaje de error con SIGNAL.
*/

/*
7. Restricción de partidos repetidos en una jornada --> 
Crea un trigger que impida programar dos equipos para jugar entre ellos más de una vez 
en la misma jornada y liga, independientemente de quién es local o visitante. 
Si esto ocurre, muestra un mensaje de error con la instrucción SIGNAL.
*/

/*
8. Conflicto de horarios de equipos --> 
De manera similar, crea un trigger que impida insertar un nuevo partido si alguno de los equipos 
(local o visitante) ya tiene programado otro partido en la misma jornada y liga. 
El trigger debe validar tanto al equipo local como al visitante.
*/
