"""
Danry Yuan
251368314
yyuan459
12/10/2023

everything needed for a shopping cart
"""
class Product:
    def __init__(self, name, price, category):
        # Initialize product attributes
        self._name = name
        self._price = price
        self._category = category

    # Define how products are classified
    def __eq__(self, other):
        if isinstance(other, Product):
             if  (self._name == other._name and self._price == other._price) and (self._category==other._category):
                return True
             else:
                return False
        else:
            return False

    def get_name(self):
        return self._name

    def get_price(self):
        return int(self._price)

    def get_category(self):
        return self._category

    # Implement string representation
    def __repr__(self):
        rep = 'Product(' + self._name + ',' + str(self._price) + ',' + self._category + ')'
        return rep

class Inventory():
    def __init__(self):
        # initial attribute
        self._items = {}
        # self._name = ""
        # self.price = 0
        # self.quantity = 0

    def add_to_productInventory(self, productName, productPrice, productQuantity):
        # store items as dictionaries attached to a key with their product name in an outer dictionary
        self._items.update({productName:{"price":productPrice, "quantity":productQuantity}})
        return self._items

    def add_productQuantity(self, nameProduct, addQuantity):
        self._items[nameProduct]["quantity"] += abs(int(addQuantity))
        return self._items

    def remove_productQuantity(self, nameProduct, removeQuantity):
        # make sure users can't remove a product infinitely from their cart for an exploit
        if self._items[nameProduct]["quantity"] < removeQuantity:
            raise "Over remove"
        self._items[nameProduct]["quantity"] -= removeQuantity
        return self._items

    def get_productPrice(self, nameProduct):
        return self._items[nameProduct]["price"]

    def get_productQuantity(self, nameProduct):
        return self._items[nameProduct]["quantity"]

    def display_Inventory(self):
        items = ""
        # quick loop to make a nice looking message
        for n in self._items:
            items += f"{n}, {self._items[n]['price']}, {self._items[n]['quantity']} \n"

        print(items)

    def get_inventory(self):
        # copy of display_inventory but returns instead of prints for modular use
        items = ""
        # quick loop to make a nice looking message
        for n in self._items:
            items += f"{n} {self._items[n]['quantity']} \n"
        return items

    def price(self):
        # module for calculating total price of current inventory
        total = 0
        for n in self._items:
            total += (self._items[n]['price']*self._items[n]['quantity'])
        return total

class ShoppingCart:
        def __init__(self, buyerName, inventory):
            # initial attributes
            self._name = buyerName
            self._inventory = inventory
            self._cart = Inventory()

        def add_to_cart(self, nameProduct, requestedQuantity):
            # try to remove amount user wants in their cart from our inventory. If key error is
            # raised that item doesn't exist in our inventory, and we can't fill the order
            try:
                self._inventory.remove_productQuantity(nameProduct, requestedQuantity)
            except:
                return "Can not fill the order"
            # try to just add to the product's quantity of cart but if it doesn't exist yet create
            # new entry for the product in cart
            try:
                self._cart.add_productQuantity(nameProduct, requestedQuantity)
                return "Filled the order"
            except:
                self._cart.add_to_productInventory(nameProduct, self._inventory.get_productPrice(
                    nameProduct), requestedQuantity)
                return "Filled the order"

        def remove_from_cart(self, nameProduct, requestedQuantity):
            # try to remove as user requests
            try:
                self._cart.remove_productQuantity(nameProduct, requestedQuantity)
                self._inventory.add_productQuantity(nameProduct, requestedQuantity)
                return "Successful"
            except:
                return "Product not in the cart"
            # except "Over remove":
            #     return "The requested quantity to be removed from cart exceeds what is in the cart"

        # view cart
        def view_cart(self):
            view = self._cart.get_inventory() + (f"Total: {self._cart.price()} \nBuyer "
                                                         f"Name: {self._name}")
            print(view)

class product_catalog:
    # initialize
    def __init__(self):
        self._catalog = []
        self._low_prices = set()
        self._medium_prices = set()
        self._highPrices = set()
    def addProduct(self, product):
        self._catalog.append(product)

    # categorize prices
    def price_category(self):
        # loop through catalog set and check prices against pricing brackets
        for n in self._catalog:
            if 0 < n.get_price() <= 99:
                self._low_prices.add(n.get_price)
            elif 100 <= n.get_price() <= 499:
                self._medium_prices.add(n.get_price)
            elif 500 <= n.get_price():
                self._highPrices.add(n.get_price)
        print(f"Number of low price items: {len(self._low_prices)} \nNumber of medium price items:"
              f" {len(self._medium_prices)} \nNumber of high price items: {len(self._highPrices)}")

    def display_catalog(self):
        for n in self._catalog:
            print(f"Product: {n.get_name()} Price: {n.get_price()} Category: {n.get_category()}")

def read_csv(filename):
    '''
    read csv file
    :param filename: name of the file to be read
    :return: list of text split by comma with \n removed
    '''
    text_list = []
    try:
        csv = open(filename, "r")
        for n in csv:
            # get rid of \n just in case and split into list by commas since comma seperated
            n = n.replace("\n", "")
            text_list.append(n.split(","))
        csv.close()
        return text_list
    except:
        print(f"Could not open file {filename}")
        return []

def populate_inventory(filename):
    '''
    Populate the inventory with a csv file
    :param filename: name of file to be read into the inventory
    :return: inventory object with file content read into it
    '''
    text = read_csv(filename)
    inventory = Inventory()
    for n in text:
        inventory.add_to_productInventory(n[0], int(n[1]), int(n[2]))
    return inventory

def populate_catalog(fileName):
    '''
    Populate catalog with a csv file
    :param fileName: name of csv file to be put into the catalog
    :return: catalog object with csv file's contents read
    '''
    text = read_csv(fileName)
    catalog = product_catalog()


    for n in text:
        product = Product(n[0], int(n[1]), n[3])
        catalog.addProduct(product)

    return catalog
