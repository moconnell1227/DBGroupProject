DROP TRIGGER reservations_before_insert_overbooking;
DROP TRIGGER reservations_before_update_overbooking;
DROP TRIGGER reservations_before_insert_guest_count;
DROP TRIGGER reservations_before_update_guest_count;
DROP TRIGGER credit_cards_before_insert_balance_check;
DROP TRIGGER credit_cards_before_refund;

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

    set @bal = (select balance from CreditCards where CardNum = NEW.CardNum) + NEW.Rate;
    set @lim = (select `limit` from CreditCards where CardNum = NEW.CardNum);

    IF (@bal > @lim)
    then
        signal sqlstate '99999'
        set MESSAGE_TEXT = 'This Charge exceeds limit';
    else
        update CreditCards set balance=@bal where CardNum =NEW.CardNum;
    end if;
END$
DELIMITER ;


-- trigger to check if a refund will make a card balance less than 0
DELIMITER $
CREATE TRIGGER credit_cards_before_refund BEFORE
DELETE ON Reservations
FOR EACH ROW
BEGIN
    IF NOT EXISTS
        (SELECT * FROM CreditCards
        WHERE CreditCards.CardNum = OLD.CardNum)
    THEN
        SIGNAL SQLSTATE '99999'
        SET MESSAGE_TEXT = 'This Credit Card Does Not Exist';
    END IF;

    SET @bal = (SELECT balance FROM CreditCards WHERE CardNum = OLD.CardNum) - OLD.Rate;
    IF (@bal < 0)
    THEN
        SIGNAL SQLSTATE '99999'
        SET MESSAGE_TEXT = 'Cannot refund the balance';
    ELSE
        UPDATE CreditCards SET balance=@bal WHERE CardNum = OLD.CardNum;
    END IF;
END$
DELIMITER ;
