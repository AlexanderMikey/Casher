CREATE TABLE Customers (
  CustomerID VARCHAR(25) NOT NULL PRIMARY KEY,
  CustomerName VARCHAR(50) NOT NULL,
  CustomerEmail VARCHAR(30) NOT NULL,
  CustomerPhoneNumber VARCHAR(25) NOT NULL,
  Street VARCHAR(50) NOT NULL,
  City VARCHAR(25) NOT NULL,
  ZipCode VARCHAR(25)NOT NULL
  );

  CREATE TABLE Owner (
  OwnerID VARCHAR(25) NOT NULL PRIMARY KEY,
  OwnerName VARCHAR(50) NOT NULL,
  OwnerEmail VARCHAR(30) NOT NULL,
  OwnerPhoneNumber VARCHAR(25) NOT NULL
);

CREATE TABLE Products (
  ProductID VARCHAR(25) NOT NULL PRIMARY KEY,
  ProductBrand VARCHAR(25) NOT NULL,
  ProductPrice DECIMAL(11,2) NOT NULL,
  Stock INT NOT NULL,
  Size VARCHAR(25) NOT NULL
);

CREATE TABLE Payments (
  PaymentID VARCHAR(25) NOT NULL PRIMARY KEY,
  OrderID VARCHAR(25) NOT NULL,
  PaymentType VARCHAR(25) NOT NULL,
  Amount DECIMAL(11,2) NOT NULL,
  Currency VARCHAR(20) NOT NULL
);

CREATE TABLE Orders (
  OrderID VARCHAR(25) NOT NULL PRIMARY KEY,
  OrderDate DATE NOT NULL DEFAULT(CURRENT_TIMESTAMP)
);

CREATE TABLE OrderDetails(
  OrderID VARCHAR(25) NOT NULL FOREIGN KEY REFERENCES Orders(OrderID) ON UPDATE CASCADE ON DELETE CASCADE,
  ProductID VARCHAR(25) NOT NULL FOREIGN KEY REFERENCES Products(ProductID) ON UPDATE CASCADE ON DELETE CASCADE,
  CustomerID VARCHAR(25) NOT NULL FOREIGN KEY REFERENCES Customers(CustomerID) ON UPDATE CASCADE ON DELETE CASCADE,
  Quantity INT NOT NULL
);

CREATE TABLE productEvents(
  ProductID VARCHAR(25) NOT NULL,
  Quantity INT NOT NULL,
  Action VARCHAR(20) NOT NULL
);
