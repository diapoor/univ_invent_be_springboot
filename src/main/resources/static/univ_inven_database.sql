drop database univ_invent;
CREATE DATABASE univ_invent;
USE univ_invent;
CREATE TABLE warehouse (
    warehouse_id INT AUTO_INCREMENT PRIMARY KEY,
    warehouse_name VARCHAR(100) NOT NULL,
    location VARCHAR(200)
);


CREATE TABLE inventory (
    item_id INT AUTO_INCREMENT PRIMARY KEY,
    item_name VARCHAR(100) NOT NULL,
    description TEXT,
    total_quantity INT DEFAULT 0,
    warehouse_id INT,
    image nvarchar(500),
    FOREIGN KEY (warehouse_id) REFERENCES Warehouse(warehouse_id) 
);

CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    hash_password VARCHAR(100) NOT NULL,
    full_name VARCHAR(100)              
);

CREATE TABLE borrowings (
    borrowing_id INT AUTO_INCREMENT PRIMARY KEY,
    borrower nvarchar(50) not null,
    borrower_phone nvarchar(12) not null, 
    item_id INT,
    borrowed_date DATE,
    due_date DATE,
    returned_date DATE,
    status ENUM('BORROWED', 'RETURNED', 'OVERDUE'),
    FOREIGN KEY (item_id) REFERENCES inventory(item_id) ON DELETE CASCADE
);

CREATE TABLE maintenance (
    maintenance_id INT AUTO_INCREMENT PRIMARY KEY,
    item_id INT,
    issue_description TEXT,
    reported_date DATE,
    resolved_date DATE,
    status ENUM('PENDING', 'IN_PROGRESS', 'RESOLVED'),
    FOREIGN KEY (item_id) REFERENCES inventory(item_id) ON DELETE CASCADE
);

CREATE TABLE lossesAndDamages (
    report_id INT AUTO_INCREMENT PRIMARY KEY,
    item_id INT,
    report_type ENUM('LOST', 'DAMAGED'),
    description TEXT,
    resolved_date date default (curdate()),
    FOREIGN KEY (item_id) REFERENCES inventory(item_id) ON DELETE CASCADE
);
INSERT INTO `warehouse` VALUES (1,'Main Campus Store','123 University Ave, Collegeville'),(2,'Science Building Store','456 Science St, Collegeville'),(3,'Engineering Warehouse','789 Engineering Rd, Collegeville'),(4,'Library Supplies','101 Library Rd, Collegeville'),(6,'Medical Campus Store','303 Medical St, Collegeville'),(7,'Sports Equipment Room','404 Sports Ave, Collegeville'),(8,'Dormitory Supplies','505 Dormitory Ln, Collegeville'),(9,'Administration Supplies','606 Admin Rd, Collegeville'),(10,'Maintenance Depot','707 Maintenance Dr, Collegeville'),(11,'Main Campus Store55','123 University Ave, Collegeville66');
INSERT INTO `users` VALUES (1,'prof_jdoe','hashed_password1','Prof. John Doe'),(2,'admin_asmith','hashed_password2','Alice Smith'),(3,'student_bwhite','hashed_password3','Bob White'),(4,'staff_cjohnson','hashed_password4','Cathy Johnson'),(5,'prof_dmiller','hashed_password5','Prof. David Miller'),(6,'student_earmstrong','hashed_password6','Eve Armstrong'),(7,'staff_flee','hashed_password7','Frank Lee'),(8,'student_gwilliams','hashed_password8','Grace Williams'),(9,'admin_hmartin','hashed_password9','Henry Martin'),(10,'student_irichards','hashed_password10','Ivy Richards'),(11,'adminsuper','$2a$10$21cKVIhtdCSh2CpmBNq7nuVhxuUTprrI0KecjtPAcxhVlG.G69bse','Lee trung Duc'),(12,'adminsuper23','$2a$10$nWCTiDMIIhHKy8flluxlouV/TKVI4OqP8lIWOqFjfskDXE4MXgDbi','Lee trung Duc');
INSERT INTO `inventory` VALUES (1,'Laptop','Dell XPS 13',50,1,'d28e706a-c32c-4b1e-96b0-da22d51d6a3f_images.png'),(2,'Projector','Epson Projector',20,2,'d28e706a-c32c-4b1e-96b0-da22d51d6a3f_images.png'),(3,'Whiteboard','Large classroom whiteboard',30,3,'d28e706a-c32c-4b1e-96b0-da22d51d6a3f_images.png'),(4,'Monitor','HP Monitor 24-inch',40,4,'d28e706a-c32c-4b1e-96b0-da22d51d6a3f_images.png'),(5,'ziper','Bảng vip thế hệ 10.0',4,1,'f7564706-dbfc-4770-980e-ec0c9bbaa16c_Screenshot2024-03-10151040.png'),(6,'Scanner','Fujitsu Scanner',25,6,'d28e706a-c32c-4b1e-96b0-da22d51d6a3f_images.png'),(7,'Mouse','Logitech Wireless Mouse',100,7,'d28e706a-c32c-4b1e-96b0-da22d51d6a3f_images.png'),(8,'Keyboard','Dell Keyboard',80,8,'d28e706a-c32c-4b1e-96b0-da22d51d6a3f_images.png'),(9,'Tablet','Apple iPad',35,9,'d28e706a-c32c-4b1e-96b0-da22d51d6a3f_images.png'),(10,'Smartphone','Samsung Galaxy',60,10,'d28e706a-c32c-4b1e-96b0-da22d51d6a3f_images.png');
INSERT INTO `borrowings` VALUES (1,'Prof. John Doe','1234567890',1,'2024-05-01','2024-05-15',NULL,'BORROWED'),(2,'Alice Smith','1234567891',2,'2024-05-01','2024-05-15',NULL,'BORROWED'),(3,'Bob White','1234567892',3,'2024-05-03','2024-05-17',NULL,'OVERDUE'),(4,'Cathy Johnson','1234567893',4,'2024-05-04','2024-05-18',NULL,'BORROWED'),(5,'Prof. David Miller','1234567894',5,'2024-05-05','2024-05-19',NULL,'RETURNED'),(6,'Eve Armstrong','1234567895',6,'2024-05-06','2024-05-20',NULL,'OVERDUE'),(7,'Frank Lee','1234567896',7,'2024-05-06','2024-05-20','2024-05-20','RETURNED'),(8,'Grace Williams','1234567897',8,'2024-05-08','2024-05-22',NULL,'RETURNED'),(9,'Henry Martin','12345678981',9,'2024-05-08','2024-05-22',NULL,'OVERDUE'),(11,'Prof. John Doe','1234567890',5,'2024-04-28','2024-04-29','2024-04-30','RETURNED'),(12,'Prof. John Doe','1234567890',5,'2024-04-28','2024-05-14',NULL,'BORROWED');
INSERT INTO `lossesanddamages` VALUES (1,1,'LOST','Lost during transit between campuses','2024-05-12'),(2,2,'DAMAGED','Cracked projector lens','2024-05-12'),(3,3,'LOST','Lost in student dormitory','2024-05-12'),(4,4,'DAMAGED','Broken laptop screen','2024-05-12'),(5,5,'LOST','Lost during inventory check in the library','2024-05-12'),(6,6,'DAMAGED','Overheating printer','2024-05-12'),(7,7,'DAMAGED','Damaged mouse USB connector','2024-05-12'),(8,8,'LOST','Missing keyboard from classroom','2024-05-12'),(9,9,'DAMAGED','Cracked monitor housing','2024-05-12'),(10,10,'LOST','Lost in maintenance transport','2024-05-12'),(12,4,'DAMAGED','Cracked projector lens','2024-05-16');
INSERT INTO `maintenance` VALUES (1,1,'Battery issue','2024-05-10',NULL,'PENDING'),(3,3,'Whiteboard smudge','2024-05-12',NULL,'RESOLVED'),(4,4,'Broken screen','2024-05-13',NULL,'PENDING'),(6,6,'Scanner malfunction','2024-05-15',NULL,'RESOLVED'),(7,7,'Mouse sensor error','2024-05-16',NULL,'PENDING'),(8,8,'Keyboard key malfunction','2024-05-17',NULL,'IN_PROGRESS'),(9,9,'Cracked monitor screen','2024-05-18',NULL,'RESOLVED'),(10,10,'Lens problem hè hè','2024-05-19','2024-05-09','RESOLVED'),(13,2,'Lens problem','2024-05-10',NULL,'IN_PROGRESS'),(16,5,'Lens problem','2024-05-10',NULL,'IN_PROGRESS'),(17,5,'Lens problem','2024-05-10',NULL,'IN_PROGRESS'),(18,5,'Lens problem','2024-05-10','2024-05-10','RESOLVED');

