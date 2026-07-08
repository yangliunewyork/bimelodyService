LOCK TABLES customer Write;

INSERT INTO customer
(     
	cognito_user_id,
	cognito_user_pool_id,
	email,
	phone_number,
	username,
	profile_picture_link
)
VALUES
("CognitoUserId001", "CognitoUserPoolId000", "MarcBenioff@gmail.com", "+1 9172020000", "Marc-Benioff",
              "https://static01.nyt.com/images/2018/06/17/business/18caltoday-benioff/18caltoday-benioff-superJumbo-v2.jpg"),
("CognitoUserId002", "CognitoUserPoolId000", "tobi-lutke@gmail.com", "+1 9173021234", "tobi-lutke",
              "https://upload.wikimedia.org/wikipedia/commons/thumb/2/2d/NYC-Commerce-Tobi-L%C3%BCtki-561.jpg/1200px-NYC-Commerce-Tobi-L%C3%BCtki-561.jpg"),
("CognitoUserId003", "CognitoUserPoolId000", "Brian-Armstrong@gmail.com", "+1 9173334321", "Brian-Armstrong",
              "https://techcrunch.com/wp-content/uploads/2018/05/screen-shot-2018-05-09-at-4-09-23-pm.png");

UNLOCK TABLES;