# propriety-auditing-service

This service have two endpoints
1- POST http://localhost:8080/insert For posting a userRequest
   I chose to work with ArrayList so in most case the comlexity will bee O(1) and in the worth case o(n)
   
   example - 
   request: 
   curl --location --request POST 'http://localhost:8080/insert' \
   --header 'Content-Type: application/json' \
   --data-raw '{
    "uid":"12345",
    "data":"qwerty"
     }'
     
     response:
     {
    "id": 5,
    "data": "qwerty",
    "timestamp": "2022-01-05T07:08:36.631+00:00",
    "uid": "12345"
     }
 
2- GET http://localhost:8080/get/{uid}
  I implemented it with binary search. did a checking in the last element of the array list. 
  So the best case will be o(1) and the worst case will be o(log n)
  
  example- 
  request:
  curl --location --request GET 'http://localhost:8080/get/12345'
  response- 
  2
