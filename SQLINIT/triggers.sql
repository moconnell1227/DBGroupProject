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


-- trigger to check to ensure the credit card balance does not exceed limit
-- on insert for booking reservation 

DELIMITER $
CREATE TRIGGER credit_cards_before_insert_balance_check BEFORE
INSERT ON Reservations
FOR EACH ROW
BEGIN
    IF NOT EXISTS
        (SELECT * FROM CreditCards
        WHERE CreditCards.CardNum = NEW.CardNum)
    THEN
        SIGNAL SQLSTATE '99999'
        SET MESSAGE_TEXT = 'This Credit Card Does Not Exist';
    END IF;
END$
DELIMITER ;
