CREATE DATABASE IF NOT EXISTS golf_game;
USE golf_game;

CREATE TABLE if not exists Users (
                       id_user INT PRIMARY KEY AUTO_INCREMENT,
                       name VARCHAR(30) NOT NULL,
                       password VARCHAR(30) NOT NULL
);

CREATE TABLE if not exists Points (
                        id_points INT PRIMARY KEY AUTO_INCREMENT,
                        ID_user INT,
                        clubs_Types ENUM('DRIVER', 'IRON', 'WOOD') NOT NULL,
                        round_duration DECIMAL(10,2),
                        money DECIMAL(10,2),
                        points INT,
                        Date DATETIME DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY (ID_user) REFERENCES Users(id_user)
);
select * from Points;

ALTER TABLE Points
    MODIFY COLUMN clubs_Types ENUM('DRIVER', 'IRON', 'WOOD') NOT NULL;