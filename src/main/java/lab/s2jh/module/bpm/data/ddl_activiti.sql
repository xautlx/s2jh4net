CREATE VIEW ACT_ID_USER AS 
SELECT
auth_User.authUid AS ID_,
0 AS REV_,
null AS FIRST_,
null AS LAST_,
auth_User.email AS EMAIL_,
auth_User.password AS PWD_,
null AS PICTURE_ID_
FROM
auth_User;

CREATE VIEW ACT_ID_MEMBERSHIP AS 
SELECT
auth_User.authUid AS USER_ID_,
auth_Role.code AS GROUP_ID_
FROM
auth_Role,auth_User,auth_UserR2Role
WHERE auth_Role.id=auth_UserR2Role.role_id
and auth_User.id=auth_UserR2Role.user_id;

CREATE VIEW ACT_ID_GROUP AS 
SELECT
auth_Role.code AS ID_,
0 AS REV_,
auth_Role.name AS NAME_,
'role' AS TYPE_
FROM
auth_Role