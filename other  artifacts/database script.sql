create table user (
id bigint PRIMARY KEY AUTO_INCREMENT,
name VARCHAR(100),
email VARCHAR(100),
password VARCHAR(100),
mobile VARCHAR(15),
role VARCHAR(100),
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
)

CREATE TABLE Product (
    id bigint PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(150) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    stock_quantity INT NOT NULL,
    category_id bigint,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES Category(id)
);

CREATE TABLE Category (
    id bigint PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description TEXT
);

CREATE TABLE Cart (
    id bigint PRIMARY KEY AUTO_INCREMENT,
    user_id bigint,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES User(id)
);

CREATE TABLE Cart_Item (
    id bigint PRIMARY KEY AUTO_INCREMENT,
    cart_id bigint,
    product_id bigint,
    quantity INT NOT NULL,
    FOREIGN KEY (cart_id) REFERENCES Cart(id),
    FOREIGN KEY (product_id) REFERENCES Product(id)
);

CREATE TABLE Shipping_Address (
    id bigint PRIMARY KEY AUTO_INCREMENT,
    user_id bigint,
    address_line1 VARCHAR(255) NOT NULL,
    address_line2 VARCHAR(255),
    city VARCHAR(100) NOT NULL,
    state VARCHAR(100) NOT NULL,
    postal_code VARCHAR(20) NOT NULL,
    country VARCHAR(100) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES User(id)
);

CREATE TABLE `Order` (
    id bigint PRIMARY KEY AUTO_INCREMENT,
    user_id bigint,
    total_amount DECIMAL(10, 2) NOT NULL,
    order_status VARCHAR(50) NOT NULL,
    shipping_address_id bigint,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES User(id),
    FOREIGN KEY (shipping_address_id) REFERENCES Shipping_Address(id)
);

CREATE TABLE Order_Item (
    id bigint PRIMARY KEY AUTO_INCREMENT,
    order_id bigint,
    product_id bigint,
    quantity INT NOT NULL,
    price_at_time DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES `Order`(id),
    FOREIGN KEY (product_id) REFERENCES Product(id)
);


CREATE TABLE Payment (
    id bigint PRIMARY KEY AUTO_INCREMENT,
    order_id bigint,
    payment_method VARCHAR(50),
    payment_status VARCHAR(50),
    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES `Order`(id)
);

drop table user;
drop table Payment;
drop table Shipping_Address;
drop table Order_Item;
drop table Cart_Item;
drop table Cart;
drop table Category;
drop table Product;
drop table `Order`;
