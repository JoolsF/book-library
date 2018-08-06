# book library

Akka HTTP REST service - toy example

## endpoints


#### ping
```
curl -X GET \
  http://localhost:8080/library/ping
```


#### add a book the library
```
curl -X POST \
  http://localhost:8080/library/books \
  -H 'X-Library-User-Token: foo' \
  -H 'content-type: application/json' \
  -d '{"title":"The Lord Of The Rings"}'
```

#### add an employee
```
 curl -X POST \
   http://localhost:8080/library/employees \
   -H 'X-Library-User-Token: foo' \
   -H 'content-type: application/json' \
   -d '{"name":"Julian Fenner"}'
```

#### loan a book
```
curl -X POST \
  http://localhost:8080/library/loans \
  -H 'X-Library-User-Token: foo' \
  -H 'content-type: application/json' \
  -d '{"bookId":2, "employeeId":3}'
```

#### get an employee's loans
```
curl -X GET \
  http://localhost:8080/library/employees/3/loans \
  -H 'X-Library-User-Token: foo' \
  -H 'content-type: application/json'
```