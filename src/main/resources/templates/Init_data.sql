
-- Insert data into t_user table
INSERT INTO t_user (username, password) VALUES
    ( 'test', '{bcrypt}123');

-- Insert data into user_role table
INSERT INTO user_role (user_id, roles) VALUES
    ((SELECT id FROM t_user WHERE username = 'test'), 'ROLE_ADMIN' );
