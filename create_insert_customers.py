import csv

reservations_path = "INN/Reservations.csv"
sql = ""

# process csv data into dictionary
with open(reservations_path) as csv_file:
    csv_reader = csv.reader(csv_file, delimiter=',')
    in_cols = True
    # dict[<customer_name>] key is first name concatenated with last name,
    #                       entry is list of length 2
    #   --- dict[<customer_name>][0] = first name
    #   --- dict[<customer_name>][1] = last name

    dict = {}

    for row in csv_reader:
        if in_cols == True:
            print(f'Column names are {", ".join(row)}')
            in_cols = False
        else:
            cur_key = row[6].strip("\'") + row[5].strip("\'")
            if cur_key in dict:
                continue
            else:
                dict[cur_key] = [row[6].strip("\'"), row[5].strip("\'")]

# parse dictionary entries into sql code
for e in dict:
    sql += 'INSERT INTO Customers (\n\t'
    sql += 'LastName, FirstName\n\t) VALUES (\n\t\t'
    sql += '\'' + dict[e][1] + '\'' + ', '
    sql += '\'' + dict[e][0] + '\'' + '\n\t);\n'

# write sql code to file
sql_file = open('customers.sql', 'w')
sql_file.write(sql)
sql_file.close()
