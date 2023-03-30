# Employees Salary Management API

## Description 

Employees Salary Management Api contains following API
1. API to upload employee details( id, login, name and salary) in a multipart csv file format
2. API to fetch employee list with query params to filter and sort

## Installation & Run
Checkout and run as a spring boot application 

## API Details

### Request

`POST /users/upload HTTP/1.1
    Host: localhost:8080
    Content-Length: 196
    Content-Type: multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW
    ----WebKitFormBoundary7MA4YWxkTrZu0gW
    Content-Disposition: form-data; name="file"; filename="[test.csv](https://github.com/saravananinstro/emp-sal-mngmt-api/files/11110520/test.csv)"
    Content-Type: text/csv
    (data)
    ----WebKitFormBoundary7MA4YWxkTrZu0gW`

### Response

    {
        "message": "File uploaded successfully."
    }

### Request

`GET /users/?minSalary=10&maxSalary=11500&offset=1&limit=10&sort=%2bname HTTP/1.1
Host: localhost:8080`

### Response

    {
        "results": [
            {
                "id": "emp_1002",
                "login": "sathish_p",
                "name": "Sathish",
                "salary": 200.0
            },
            {
                "id": "emp_1004",
                "login": "balu",
                "name": "balu",
                "salary": 120.0
            },
            {
                "id": "emp_1003",
                "login": "jothimani_p",
                "name": "jothi",
                "salary": 300.0
            }
        ]
    }
    
