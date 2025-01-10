delete from users;
insert into users (name, password, role) values
    ('admin', '$2a$10$Q9l2Qyb8Bn/Zy7rZkPu9UeK3aCxyUkwBFjfr/rgX.4Q/VzO9z1twC', 'admin'),
    ('user', '$2a$10$1vf3FEfTOZ0knCSHHQGlQebm5R4xd7cloRzJ/2SP2q/XFGxkCgxRu', 'user');