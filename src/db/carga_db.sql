INSERT INTO producto(nombre, precio, stock, fecha_inicio, fecha_fin) values 
    ('Sal', 10.99, 100, '23/03/2022', '23/03/2023'),
    ('Pan', 1.50, 50, '23/03/2022', '23/04/2022'),
    ('Agua', 1, 70, '23/03/2022', '23/09/2022'),
    ('Queso', 3.45, 20, '23/03/2022', '23/05/2022'),
    ('Mermelada', 5.99, 35, '23/03/2022', '23/05/2022'),
    ('Donuts', 3, 50, '23/03/2022', '23/04/2022'),
    ('Pescado', 5, 20, '23/03/2022', '23/05/2022'),
    ('Manzana', 1.99, 30, '23/03/2022', '23/04/2022'),
    ('Platano', 2.99, 20, '23/03/2022', '23/04/2022'),
    ('Sandia', 6, 10, '23/03/2022', '23/06/2022');

INSERT INTO pack(nombre, precio, stock, fecha_inicio, fecha_fin, dto) values 
    ('Agua_Pan', 2, 30, '23/03/2022', '23/04/2022', 5),
    ('Agua_Sal', 10, 50, '23/03/2022', '23/09/2022', 10),
    ('Macedonia', 8, 5, '23/03/2022', '23/04/2022', 20),
    ('Pescado-queso', 7.50, 20, '23/03/2022', '23/04/2022', 5),
    ('Donuts-Agua', 3, 10, '23/03/2022', '23/04/2022', 10),
    ('Mermelada-Pan', 6.30, 15, '23/03/2022', '23/04/2022', 7),
    ('Donut-Mermelada', 7.99, 10, '23/03/2022', '23/04/2022', 9),
    ('Manzana-Agua', 2, 16, '23/03/2022', '23/04/2022', 2),
    ('Platano-Agua', 3, 20, '23/03/2022', '23/04/2022', 5),
    ('Agua-Queso', 3.50, 25, '23/03/2022', '23/04/2022', 2);

INSERT INTO productos_pack(id_pack, id_producto) values
    --agua-pan
    (11, 3),
    (11, 2),
    --agua-sañ
    (12, 3),
    (12, 1),
    --macedonia
    (13, 8),
    (13, 9),
    (13, 10),
    --Pescado-Queso
    (14, 7),
    (14, 4),
    --Donuts-Agua
    (15, 3),
    (15, 6),
    --Mermelada-Pan
    (16, 5),
    (16, 2),
    --Donut-Mermelada
    (17, 6),
    (17, 5),
    --Manzana-Agua
    (18, 3),
    (18, 8),
    --Platano-Agua
    (19, 3),
    (19, 9),
    --Agua-Queso
    (20, 3),
    (20, 4);

INSERT INTO cliente (dni, name, lastname, fecha_nacimiento, email, telefonos, direccion) values 
    ('456789123E', 'Elaine', 'Spaulding', '31/03/1974', 'e.spaul@gmail.com', array['+34 661341185', '+34 610393302'], ROW('Sabadell', 'Barcelona', '35300', 'av. explanada')),
    ('123963978H', 'Nubia', 'Hernández', '24/02/1979', 'nubiahm@gmail.com', array['+34 669737846', '+34 669737456'], ROW('Madrid', 'Agost', '03698', 'Paraguay')),
    ('132714825I', 'Tirso', 'Iglesias', '22/05/1948', 'tirsois@gmail.com', array['+34 733433894', '+34 733433487'], ROW('Salamanca', 'Salamanca', '45400', 'Urzáiz')),
    ('195748632K', 'Milba', 'Cotto', '27/02/1978', 'milbacc@gmail.com', array['+34 639177235', '+34 639159235'], ROW('Alaoir', 'Andalucia', '07730', 'Boriñaur')),
    ('357896564F', 'Gedeón', 'Núñez', '19/07/1966', 'gedeonns@gmail.com', array['+34 779091893', '+34 779091945'], ROW('Nueva York', 'Nueva York', '08214', 'Queens')),
    ('915456132L', 'Cory', 'Price', '28/02/1987', 'corpri@gmail.com', array['+34 666123687'], ROW('Nueva York', 'Nueva York', '08214', 'Queens') ),
    ('142695478B', 'Peter', 'Lopez', '05/05/1984', 'pelop@gmail.com', array['+34 648251324'], ROW('Nueva Orleans', 'Luisiana', '39084', 'Oled dr') ),
    ('586326245Y', 'Maria', 'Antonieta', '22/11/1965', 'marant@gmail.com', array['+34 639215632', '+34 654896547'], ROW('Barbera Valles', 'Barcelona', '08215', 'c\ mediterraneo') ),
    ('659847251A', 'Elena', 'Gracia', '16/09/1999', 'elegra@gmail.com', array['+34 698456123', '+34 654326156'], ROW('Cerdanyola', 'Barcelona', '08225', 'C\ principal') ),
    ('456326478S', 'Mario', 'Mario', '15/04/1964', 'marmar@gmail.com', array['+34 654789123'], ROW('Roma', 'Italia', '16251', 'c\ fontaneti'));

INSERT INTO proveedor (dni, name, lastname, fecha_nacimiento, email, telefonos, direccion) values 
    ('456459123E', 'Abancuy', 'Jimínez', '31/03/1974', 'a.jimin@gmail.com', array['+34 661231185', '+34 618993302'], ROW('Sabadell', 'Barcelona', '35300', 'av. explanada')),
    ('123453978H', 'Indiana', 'Alvarez', '24/02/1979', 'indiaa@gmail.com', array['+34 669737846', '+34 669737456'], ROW('Madrid', 'Agost', '03698', 'Paraguay')),
    ('132717825I', 'Pía', 'Ojeda ', '22/05/1948', 'piaojeda@gmail.com', array['+34 733433894', '+34 733433487'], ROW('Salamanca', 'Salamanca', '45400', 'Urzáiz')),
    ('195784632K', 'Danae', 'Coronado', '27/02/1978', 'danco@gmail.com', array['+34 639177235', '+34 639159235'], ROW('Alaoir', 'Andalucia', '07730', 'Boriñaur')),
    ('357889564F', 'Claribel', 'Urrútia', '19/07/1966', 'clariur@gmail.com', array['+34 779091893', '+34 779091945'], ROW('Nueva York', 'Nueva York', '08214', 'Queens')),
    ('915454532L', 'Ina', 'Heredia', '28/02/1987', 'inapri@gmail.com', array['+34 666123687'], ROW('Nueva York', 'Nueva York', '08214', 'Queens') ),
    ('142612478B', 'Estelinda', 'Lomeli', '05/05/1984', 'estelo@gmail.com', array['+34 648251324'], ROW('Nueva Orleans', 'Luisiana', '39084', 'Oled dr') ),
    ('586323245Y', 'Taiana', 'Palomo', '22/11/1965', 'taipa@gmail.com', array['+34 639215632', '+34 654896547'], ROW('Barbera Valles', 'Barcelona', '08215', 'c\ mediterraneo') ),
    ('698478751A', 'Waldemar', 'Arana', '16/09/1999', 'waldar@gmail.com', array['+34 698456123', '+34 654326156'], ROW('Cerdanyola', 'Barcelona', '08225', 'C\ principal') ),
    ('456412478S', 'Luigi', 'Mario', '15/04/1964', 'luimar@gmail.com', array['+34 654789123'], ROW('Roma', 'Italia', '16251', 'c\ fontaneti'));

INSERT INTO asistencia (fecha_entrada, fecha_salida) values 
    ('2022-05-10 11:59:02', '2022-05-10 17:03:02'),
    ('2022-03-23 13:36:03', null),
    ('2022-07-14 14:42:04', null),
    ('2022-11-22 19:12:05', null),
    ('2022-12-02 18:45:06', null),
    ('2022-01-01 18:06:07', '2022-01-01 22:03:07'),
    ('2022-03-12 17:15:08', null),
    ('2022-10-03 23:19:09', null),
    ('2022-09-14 22:29:10', null),
    ('2022-08-16 01:39:11', null),
    ('2022-07-26 09:01:22', '2022-07-26 16:10:22');



