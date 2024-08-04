'''
    CS1026a 2023
    Assignment 01 Project One (01) – Part A
    Danry Yuan
    251368314
    yyuan459@uwo.ca
    October 7th 2023
'''

# first number of the sequence
first_num = 1
# second number
second_num = 1
# to hold the most current Fibonacci number in the loop
temp = 0

# intro message
print("Project One (01) - Part A : Fibonacci Sequence")

# ask for input which is converted to int
sequence_end = int(input("Sequence ends at: "))
# if and elifs for when user asks for hardcoded sequence_end-s
if sequence_end == 0:
    print("0: 0 0")
elif sequence_end == 1:
    print("0: 0 0")
    print("1: 1 1")
elif sequence_end == 2:
    print("0: 0 0")
    print("1: 1 1")
    print("2: 1 1")

# for non-hard coded terms
else:
    # create a variable that removes the first 3 hard-coded terms from sequence_end
    terms = sequence_end - 3
    # create a counter variable
    n = 0

    # hard-coded terms
    print("0: 0 0")
    print("1: 1 1")
    print("2: 1 1")

    # loop for as long as counter is less than or equal to how many non hard-coded terms the user wants
    while terms >= n:
        # this term's fibonacci number stored temporarily
        temp = first_num + second_num
        # move second_number into first_number
        first_num = second_num
        # move temporary number into second_number
        second_num = temp
        # create a formatted number with .format
        formatted_num = "{:,}".format(second_num)
        # print the iteration for every loop
        print(f"{3 + n}: {second_num} {formatted_num}")
        # count number go up
        n += 1

# ending message
print("\nEND: Project One <01> – Part A")
print("Danry Yuan yyuan459 251368314")
