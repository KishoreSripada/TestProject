Please follow the below instructions to test the application

1) Execute below command in MySQL -
CREATE DATABASE 'marathon_registration';

2) Run the java class -> SensiniMarathonApplication.java (This will start the tomcat server)

3) Open Postman and try to execute the below -
a) Runner Registration ->
POST -> http://localhost:8085/sensis/marathon/register

Successful Payload -> 
{
	"name":"test",
	"email":"email@gmail.com",
	"password":"pass1234"
}
Body -> Raw, content -> JSON (application/json)

Expected Response ->
{
	"name":"test",
	"email":"email@gmail.com",
	"password":"pass1234",
	"statusCode":200,
	"Status":"User details captured successfully"
}

Validations ->
null or blank values -> 
Payload ->
{
	"name":null,
	"email":"email@gmail.com",
	"password":"pass1234"
}
or 
{
	"name":"test",
	"email":"email@gmail.com",
	"password":""
}

Expected Response ->
{
    "name": null,
    "email": "email@gmail.com",
    "password": "pass1234",
    "statusCode": 400,
    "status": "Bad Request : Name, Email and Password cannot be empty !"
}


Email Validation Failure
Payload -> 
{
	"name":"test",
	"email":"emailgmail.com",
	"password":"pass1234"
}

Expected Response ->
{
    "name": "test",
    "email": "emailgmail.com",
    "password": "pass1234",
    "statusCode": 400,
    "status": "Bad Request : Email format is incorrect. Please check !"
}

a) Image/Profile Picture Upload -> 
POST -> http://localhost:8085/sensis/marathon/uploadprofilepicture
Body -> form-data 
Key -> file
Value -> Image file to be uploaded (upto 128kb, the size can be configured in application.properties)

The image should be uploaded to Amazon S3 bucket details provided and below response should be returned
Expected Response ->
File Upload Successful