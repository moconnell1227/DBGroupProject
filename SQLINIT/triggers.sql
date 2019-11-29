-- trigger to enforce constraint that the system must not allow overbooking

DELIMITER $
CREATE TRIGGER reservations_before_insert_overbooking BEFORE
INSERT ON Reservations
FOR EACH ROW
BEGIN
    IF EXISTS
        (SELECT * FROM Reservations
        WHERE NEW.RoomCode = RoomCode AND NEW.CheckIn <= CheckOut AND NEW.CheckOut >= CheckIn)

    THEN
        SIGNAL SQLSTATE '99999'
        SET MESSAGE_TEXT = 'Check constraint on Transaction.CheckIn, Transaction.CheckOut failed';
    END IF;
END$
DELIMITER ;

-- trigger to enforce constraint that the system must not allow overbooking

DELIMITER $
CREATE TRIGGER reservations_before_update_overbooking BEFORE
UPDATE ON Reservations
FOR EACH ROW
BEGIN
    IF EXISTS
        (SELECT * FROM Reservations
        WHERE NEW.RoomCode = RoomCode AND NEW.CheckIn <= CheckOut AND NEW.CheckOut >= CheckIn)

    THEN
        SIGNAL SQLSTATE '99999'
        SET MESSAGE_TEXT = 'Check constraint on Transaction.CheckIn, Transaction.CheckOut failed';
    END IF;
END$
DELIMITER ;

-- trigger to enforce constraint that the system must not allow a room to be
-- occupied by guests beyond its capacity

DELIMITER $
CREATE TRIGGER reservations_before_insert_guest_count BEFORE
INSERT ON Reservations
FOR EACH ROW
BEGIN
    IF EXISTS
        (SELECT * FROM Rooms
        WHERE NEW.RoomCode = CODE AND NEW.NumOcc > MaxOcc)
    THEN
        SIGNAL SQLSTATE '99999'
        SET MESSAGE_TEXT = 'Check constraint on Reservations.NumOcc failed';
    END IF;
END$
DELIMITER ;

-- trigger to enforce constraint that the system must not allow a room to be
-- occupied by guests beyond its capacity

DELIMITER $
CREATE TRIGGER reservations_before_update_guest_count BEFORE
UPDATE ON Reservations
FOR EACH ROW
BEGIN
    IF EXISTS
        (SELECT * FROM Rooms
        WHERE NEW.RoomCode = CODE AND NEW.NumOcc > MaxOcc)
    THEN
        SIGNAL SQLSTATE '99999'
        SET MESSAGE_TEXT = 'Check constraint on Reservations.NumOcc failed';
    END IF;
END$
DELIMITER ;
