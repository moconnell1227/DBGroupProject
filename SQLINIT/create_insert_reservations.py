import csv
import datetime

reservations_path = "INN/Reservations.csv"
sql = ""
dict = {}

# process csv data into dictionary
with open(reservations_path) as csv_file:
    csv_reader = csv.reader(csv_file, delimiter=',')
    in_cols = True
    # dict[<customer_name>] key is first name concatenated with last name,
    #                       entry is list of length 2
    #   --- dict[<customer_name>][0] = first name
    #   --- dict[<customer_name>][1] = last name
    #   --- dict[<customer_name>][2] = id

    for row in csv_reader:
        if in_cols == True:
            print(f'Column names are {", ".join(row)}')
            in_cols = False
        else:
            cur_key = row[6].strip("\'") + row[5].strip("\'")
            if cur_key in dict:
                continue
            else:
                dict[cur_key] = [row[6].strip("\'"), row[5].strip("\'"), -1]

i = 1
for e in dict:
    dict[e][2] = i
    i += 1

# process csv data into sql code
with open(reservations_path) as csv_file_2:
    res_reader = csv.reader(csv_file_2, delimiter=',')
    in_cols = True

    for row in res_reader:
        if in_cols == True:
            in_cols = False
        else:
            ci_str1 = row[9].lower() # modified this with new Reservations csv that has more recent dates
            co_str1 = row[10].lower()
            #ci_str2 = ci_str1[1:3] + ci_str1[3:5].upper() + ci_str1[5:10]
            #co_str2 = co_str1[1:3] + co_str1[3:5].upper() + co_str1[5:10]

            #check_in = datetime.datetime.strptime(ci_str1, '%d-%b-%y')
            #check_out = datetime.datetime.strptime(co_str1, '%d-%b-%y')
            cur_key = row[6].strip("\'") + row[5].strip("\'")

            sql += 'INSERT INTO Reservations (\n\t'
            sql += 'CheckIn, CheckOut, Rate, NumOcc, RoomCode, CustomerId, CardNum'
            sql += '\n\t) VALUES (\n\t\t'
            sql += '\"' + ci_str1 + '\", '
            sql += '\"' + co_str1 + '\", '
            sql += str(row[4]) + ', '
            sql += str(int(row[7]) + int(row[8])) + ', '
            sql += row[1] + ', '
            sql += str(dict[cur_key][2]) + ', ' + str(dict[cur_key][2]) + '\n\t);\n'

# write sql code to file
sql_file = open('reservations.sql', 'w')
sql_file.write(sql)
sql_file.close()
