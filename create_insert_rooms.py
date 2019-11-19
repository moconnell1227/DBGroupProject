import csv

reservations_path = "INN/Rooms.csv"
sql = ""

# process csv data into sql code
with open(reservations_path) as csv_file:
    csv_reader = csv.reader(csv_file, delimiter=',')
    in_cols = True

    for row in csv_reader:
        if in_cols == True:
            print(f'Column names are {", ".join(row)}')
            in_cols = False
        else:
            sql += 'INSERT INTO Rooms (\n\t'
            sql += 'CODE, Name, Num_Beds, BedType, MaxOcc, BasePrice, Decor\n\t) '
            sql += 'VALUES (\n\t\t'
            sql += row[0] + ', '
            sql += row[1] + ', '
            sql += str(row[2]) + ', '
            sql += row[3] + ', '
            sql += str(row[4]) + ', '
            sql += str(row[5]) + ', '
            sql += row[6] + '\n\t);\n'

# write sql code to file
sql_file = open('rooms.sql', 'w')
sql_file.write(sql)
sql_file.close()
