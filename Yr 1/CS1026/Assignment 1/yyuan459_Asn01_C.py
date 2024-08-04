'''
    CS1026a 2023
    Project One (01) – Part C
    Danry Yuan
    251368314
    yyuan459@uwo.ca
    October 7th 2023
'''
# starting message
print("Part One - Project C: The Moore the Merrier")

# get input on starting variables
start_transistors = int(input("Starting Number of transistors: "))
start_years = int(input("Starting Year: "))
total_num_years = int(input("Total Number of Years: "))
# flops are just transistor * 50
flops = start_transistors * 50
# format the transistor number
formatted_transistors = "{:,}".format(start_transistors)

def convert_flops (flops):
    '''
    reduce inputted flops by unit and formats it to 2 decimal places. Also formats the original flops
    :param flops: inputted flops to be reduced
    :return: list of [0]: reduced formatted flops, [1]: unit of reduced flops, [2]: formatted input
    flops
    '''
    # check if input flops is greater than a unit definition's amount of flops from the largest unit
    # to smallest
    if flops >= 10**24:
        reduced_flops = "{:.2f}".format(flops/10**24)
        unit = "yotta"
    elif flops >= 10**21:
        reduced_flops = "{:.2f}".format(flops / 10 ** 21)
        unit = "zetta"
    elif flops >= 10**18:
        reduced_flops = "{:.2f}".format(flops / 10 ** 18)
        unit = "exa"
    elif flops >= 10**15:
        reduced_flops = "{:.2f}".format(flops / 10 ** 15)
        unit = "peta"
    elif flops >= 10**12:
        reduced_flops = "{:.2f}".format(flops / 10 ** 12)
        unit = "tera"
    elif flops >= 10**9:
        reduced_flops = "{:.2f}".format(flops / 10 ** 9)
        unit = "giga"
    elif flops >= 10**6:
        reduced_flops = "{:.2f}".format(flops / 10 ** 6)
        unit = "mega"
    elif flops >= 10**3:
        reduced_flops = "{:.2f}".format(flops / 10 ** 3)
        unit = "kilo"
    else:
        # if input flops cannot be reduced at all just format input flops into reduced_flops and leave
        # unit blank so return statement can stay general
        reduced_flops = "{:.2f}".format(flops)
        unit = ""
    formatted_flops = "{:,}".format(flops)
    return [reduced_flops, unit, formatted_flops]

# print header of what is calculated
print("\nYEAR : TRANSISTORS : FLOPS:")
# call and assign the result of convert_flops to converted_flops
converted_flops = convert_flops(flops)
# output the hard-coded info the user wants
print(f"{start_years} {formatted_transistors} {converted_flops[0]} {converted_flops[1]}FLOPS "
      + f"{converted_flops[2]}")

# loop for non-hard coded info the user wants
for n in range(2, total_num_years + 1, 2):
    # double start_transistors according to Moore's law
    start_transistors += start_transistors
    # format new transistor amount
    formatted_transistors = "{:,}".format(start_transistors)
    # double flops as well
    flops += flops
    # convert newfound flops
    converted_flops = convert_flops(flops)
    # print out info user wants for this iteration of the loop
    print(f"{start_years + n} {formatted_transistors} {converted_flops[0]} {converted_flops[1]}FLOPS "
          + f"{converted_flops[2]}")

# ending message
print("\nEND: Project One <01> – Part C")
print("Danry Yuan yyuan459 251368314")