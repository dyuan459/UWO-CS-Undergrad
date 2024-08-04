'''
    CS1026a 2023
    Assignment 02 - Library
    Danry Yuan
    251368314
    yyuan459@uwo.ca
    October 25th 2023
'''


def start():
    '''
    The main function to call other functions
    '''
    # initial variables
    borrowed_ISBNs = []
    all_books = [
        ['9780596007126', "The Earth Inside Out", "Mike B", 2, ['Ali']],
        ['9780134494166', "The Human Body", "Dave R", 1, []],
        ['9780321125217', "Human on Earth", "Jordan P", 1, ['David', 'b1', 'user123']]
    ]
    # main program loop
    while True:
        # call printMenu which gives input
        user_choice = printMenu()
        # I love switches, I don't need to keep writing elif user_choice ==
        if user_choice == "a" or user_choice == "1":
            # all_books is changed by addBook's return
            all_books = addBook(all_books)
        elif user_choice == "r" or user_choice == "2":
            # borrowBook returns a big list of lists
            borrowed = borrowBook(all_books, borrowed_ISBNs)
            all_books = borrowed[2]
            borrowed_ISBNs = borrowed[0]
        elif user_choice == "t" or user_choice == "3":
            result = returnBook(all_books)
            # if result is false, meaning no book to return
            if not result:
                print("Nobody has borrowed this book!")
            else:
                print(f"You have returned {result[-1]}.")
                result.pop()
                borrowed_ISBNs = result
        elif user_choice == "l" or user_choice == "4":
            listBooks(all_books, borrowed_ISBNs)
        elif user_choice == "x" or user_choice == "5":
            result = [all_books, borrowed_ISBNs]
            return result
        else:
            print("Invalid input!")


def checkISBN(isbn):
    '''
    checks ISBNs
    :param isbn: isbn user wants to check
    :return: if the isbn is valid, the isbn is returned else an error is returned
    '''
    check_vector = [1, 3, 1, 3, 1, 3, 1, 3, 1, 3, 1, 3, 1]
    # checks through isbn possibilities, if it doesn't meet a condition then return the error
    if isbn.isnumeric():
        if len(isbn) == 13:
            vector = splitInt(isbn)
            dot_product = dotProduct(vector, check_vector)
            if dot_product % 10 == 0:
                return isbn
            else:
                return "isbn error"
        else:
            return "digit error"
    else:
        return "type error"


def dotProduct(vector1, vector2):
    '''
    internal function that dot products 2 vectors (basically multiply the same terms and sum
    everything)
    :param vector1: list of numbers like a vector
    :param vector2: list of numbers like a vector
    :return: the dot product
    '''
    product = 0
    for n in range(len(vector1)):
        dot = vector1[n] * vector2[n]
        product += int(dot)
    return product


def splitInt(stringy_int):
    '''
    splits multi digit integers into vectors
    :param stringy_int: an int, likely to be string
    :return: split up integer
    '''
    vector = []
    for i in stringy_int:
        i = int(i)
        vector.append(i)
    return vector


def checkEdition():
    '''
    small function to receive input edition and check if it's a proper number
    :return: a valid edition number
    '''
    while True:
        edition = input("Please enter the book's edition: ")
        if edition.isnumeric():
            break
        else:
            print("Invalid input!")
    return edition


def checkTitle():
    '''
    loop asking for the title until a valid title is input
    :return: valid title
    '''
    while True:
        book_name = input("Please enter the book's name (without the characters '*' or '%'): ")
        if "*" in book_name or "%" in book_name:
            print("Invalid input!")
        else:
            return book_name


def addBook(all_books):
    '''
    asks for information of the new book the user wishes to add and adds it to the list of all books
    :param all_books: list of all books
    :return: allBooks: edited list of all books
    '''
    book_name = checkTitle()
    author_name = input("Please enter the book's author: ")
    edition = checkEdition()
    input_isbn = input("Please enter the book's ISBN: ")
    isbn = checkISBN(input_isbn)
    if isbn == "isbn error":
        print("Invalid input! Input ISBN is not a valid ISBN!")
    elif isbn == "digit error":
        print("Invalid input! ISBNs are 13 digits!")
    elif isbn == "type error":
        print("Invalid input! ISBNs are numbers!")
    else:
        new_entry = [isbn, book_name, author_name, edition, []]
    try:
        all_books.append(new_entry)
    except:
        return all_books
    print("A new book was added successfully")
    return all_books


def checkBorrowed(input, borrowed_isbn):
    '''
    check if an isbn is borrowed
    :param input: input isbn
    :param borrowed_isbn: list of borrowed isbns
    :return: true or false. False if it is borrowed, true if not
    '''
    for n in borrowed_isbn:
        if input == n:
            return False
    return True


def search(all_books, search_term, borrower, borrowed_isbn):
    '''
    search list of all books with the user's search term
    :param all_books: list of all books
    :param search_term: what user wishes to search for
    :param borrower: who the user is
    :param borrowed_isbn: isbns that have been borrowed already
    :return: list of: [0] borrowed isbns list [1] titles of books that was borrowed this time [2]
    list of all books
    '''
    ISBNs = []
    titles = []
    result = []
    if search_term[-1] == "*":
        search_term = search_term.replace("*", "")
        # loop through allBooks, if the user search is in the title part of allBooks borrow it
        for n in all_books:
            if search_term in n[1].lower():
                if checkBorrowed(n[0], borrowed_isbn):
                    ISBNs.append(n[0])
                    titles.append(n[1])
                    n[4] = borrower
    elif search_term[-1] == "%":
        search_term = search_term.replace("%", "")
        # loop through allBooks and check letter by letter from the first letter of both the title
        # section of allBooks and the search term. Loop until one letter is off which means it is
        # not correct and break the loop without appending anything
        for n in all_books:
            counter = 0
            for s in n[1].lower():
                if counter == len(search_term):
                    if checkBorrowed(n[0], borrowed_isbn):
                        ISBNs.append(n[0])
                        titles.append(n[1])
                        n[4] = borrower
                        break
                elif search_term[counter] != s:
                    break
                else:
                    counter += 1
    else:
        # just loop through titles and look for exact matches
        for n in all_books:
            if search_term == n[1].lower():
                if checkBorrowed(n[0], borrowed_isbn):
                    ISBNs.append(n[0])
                    titles.append(n[1])
                    n[4] = borrower

    result.append(ISBNs)
    result.append(titles)
    result.append(all_books)
    return result


def borrowBook(all_books, borrowed_isbn):
    '''
    search, borrow or give error for the book the user wants
    :param all_books: list of all books
    :param borrowed_isbn: list of borrowed books
    :return: list of: [0] borrowed isbns list [1] titles of books that was borrowed this time [2]
    list of all books
    '''
    borrower = input("What is your name? ")
    print("Advice on searching: \nEnding your search term with '*' will find books that matches the "
          + "term anywhere in their title \nEnding your search term with '%' will search for titles " +
          "starting with the search term \nWithout a special ending, we will search for a title " +
          "exactly matching your search term \nOur search engine is case insensitive\n")
    searchTerm = input("What do you wish to find? ").lower()
    result = search(all_books, searchTerm, borrower, borrowed_isbn)
    if result[1]:
        borrowed_isbn = result[0]
        titles = " ".join(result[1])
        print(f"You have borrowed {titles}.")
    else:
        print("We do not have any books by that title.")
    return result


def returnBook(borrowed_isbn):
    '''
    returns books the user wishes to return by isbn
    :param borrowed_isbn: list of borrowed isbns
    :return: edited list of borrowed isbns without the input isbn or False meaning the user didn't
    input a valid isbn to return
    '''
    returned = input("What is the ISBN of the book you wish to return? ")
    if checkISBN(returned).isnumeric():
        if checkBorrowed(returned, borrowed_isbn):
            borrowed_isbn.remove(returned)
            borrowed_isbn.append(returned)
            return borrowed_isbn
    return False


def listBooks(all_books, borrowed_isbn):
    '''
    list books the user has stored
    :param all_books: list of all books
    :param borrowed_isbn: list of borrowed isbns
    :return: no return, only prints out list of all books in a nice format
    '''
    for n in all_books:
        if checkBorrowed(n[0], borrowed_isbn):
            availability = "[Available]"
        else:
            availability = "[Unavailable]"
        title = n[1]
        author = n[2]
        edition = n[3]
        isbn = n[0]
        borrowed = n[4]
        print("-" * 15)
        print(f"{availability} \n{title} - {author} \nE: {edition} ISBN: {isbn} \nBorrowed by: "
              f"{borrowed}")


def printMenu():
    '''
    prints out the menu of options
    :return: returns lowercase input of user
    '''
    print('\n######################')
    print('1: (A)dd a new book.')
    print('2: Bo(r)row books.')
    print('3: Re(t)urn a book.')
    print('4: (L)ist all books.')
    print('5: E(x)it.')
    print('######################\n')
    return input().lower()


def exit(all_books, borrowed_isbn):
    '''
    final message after exiting program
    :param all_books: list of all books
    :param borrowed_isbn: list of borrowed isbns
    :return: no return only print
    '''
    print("$$$$$$$$ FINAL LIST OF BOOKS $$$$$$$$")
    listBooks(all_books, borrowed_isbn)


# finally, at line 295 we actually execute something!
result = start()
exit(result[0], result[1])
