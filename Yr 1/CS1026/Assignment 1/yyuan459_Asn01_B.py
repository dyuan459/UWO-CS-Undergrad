'''
    CS1026a 2023
    Project One (01) – Part B
    Danry Yuan
    251368314
    yyuan459@uwo.ca
    October 7th 2023
'''

def isprime(number):
    '''
    checks if an inputted number is prime or not
    :param number: number to check if prime or not
    :return: True or False based on whether the number is prime (True meaning it is prime)
    '''
    # 0 and 1 are prime numbers and are a bit too special to just throw into a loop to check with
    if number == 0 or number == 1:
        return True
    # negative numbers cannot be prime
    elif number < 0:
        return False
    # loop through possible terms (numbers less than input but greater than 2)
    for i in range(2, number):
        # if a term comes back without remainder then input is divisible by a number other than
        # itself and 1 so return false meaning it is not prime
        if number % i == 0:
            return False
    # if nothing returned after full loop then it's prime
    return True

# starting message
print("Part One - Project B: Prime Choice\n")
# input of start of range
prime_num_start = int(input("Prime Numbers starting with: "))
# input of end of range
prime_num_end = int(input("and ending with: "))

# swap the start with the end of the range if start > end since that makes no sense
if prime_num_start > prime_num_end:
    # let the user know this problem
    print(f"\nIncorrect Input: {prime_num_start} is greater than {prime_num_end}")
    # swap numbers around
    temp = prime_num_start
    prime_num_start = prime_num_end
    prime_num_end = temp
    # let user know how the problem was solved
    print("The numbers have been automatically switched.")

# reiterating range the user picked
print(f"\nPrime numbers in the range from {prime_num_start} and {prime_num_end} are:")

# loop through range user picked
for n in range(prime_num_start, prime_num_end + 1):
    # if isprime the number in the current iteration is true, print the number
    if isprime(n):
        print(f"{n} is prime")

# ending message
print("\nEND: Project One <01> – Part B")
print("Danry Yuan yyuan459 251368314")
