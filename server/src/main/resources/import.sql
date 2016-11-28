-- user login: theo / geheim
INSERT INTO User(user_id, username, password,account_non_expired,account_Non_Locked,credentials_Non_Expired,enabled ) VALUES('f7d1783c-734e-11e6-8b77-86f30ca893d3','theo','$2a$10$kADgLn2OaQjvoBd5/hjsjOyx6tgrc6eSINkLsN4Pi1RBuY6WIBPaO',true,true,true,true);
-- admin: admin / geheim
INSERT INTO User(user_id, username, password,account_non_expired,account_Non_Locked,credentials_Non_Expired,enabled ) VALUES('571ea611-5b4d-4f4f-95d5-2239580518b5','admin','$2a$10$kADgLn2OaQjvoBd5/hjsjOyx6tgrc6eSINkLsN4Pi1RBuY6WIBPaO',true,true,true,true);
INSERT INTO User_Authority(authority, description)  VALUES('ROLE_ADMIN','Darf alles');
INSERT INTO User_Authorities(user_user_id, AUTHORITIES_AUTHORITY) VALUES('571ea611-5b4d-4f4f-95d5-2239580518b5','ROLE_ADMIN');