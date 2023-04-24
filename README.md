# REST API Tennis club app
Example Spring Boot REST API application simulating tennis club reservation system.


It uses the following technologies:
* Java 20
* Spring Boot 
* Hibernate
* H2 database
* Lombok
* JUnit
* Mockito

## Install
    mvn clean install

## Run the app
    mvn spring-boot:run
### Initialize database with data
    mvn spring-boot:run -Dspring-boot.run.arguments=--initialize=TRUE
Runs the app and creates two surfaces and four courts.

# REST API
The REST API to the example app is described below.

## Create new court
### Request
`POST /api/courts`
### Body
```json
{
    "name": "Court 1",
    "surfaceId": 1
}
```
### Response
```json
{
    "id": 1,
    "name": "Court 1",
    "surface": {
        "id": 1,
        "name": "Clay",
        "minutePrice": 10.0
    }
}
```

## Get all courts
### Request
`GET /api/courts/all`
### Response
```json
[
    {
        "id": 1,
        "name": "Court 1",
        "surface": {
            "id": 1,
            "name": "Clay",
            "minutePrice": 10
        }
    },
    {
        "id": 2,
        "name": "Court 2",
        "surface": {
            "id": 2,
            "name": "Grass",
            "minutePrice": 15
        }
    }
]
```

## Get court by id
### Request
`GET /api/courts/id/{id}`
### Response
```json
{
    "id": 1,
    "name": "Court 1",
    "surface": {
        "id": 1,
        "name": "Clay",
        "minutePrice": 10
    }
}
```

## Update court
### Request
`PUT /api/courts/id/{id}`
### Body
```json
{
    "name": "Court 1",
    "surfaceId": 2
}
```
### Response
```json
{
    "id": 1,
    "name": "Court 1",
    "surface": {
        "id": 2,
        "name": "Grass",
        "minutePrice": 15
    }
}
```

## Patch court
### Request
`PATCH /api/courts/id/{id}`
### Body
```json
{
    "surfaceId": 2
}
```
### Response
```json
{
    "id": 1,
    "name": "Court 1",
    "surface": {
        "id": 2,
        "name": "Grass",
        "minutePrice": 15
    }
}
```

## Delete court
### Request
`DELETE /api/courts/id/{id}`

## Create new reservation
### Request
`POST /api/reservations`
### Body
```json
{
  "courtId": 2,
  "customerPhoneNumber": "123456789",
  "customerName": "John Doe",
  "isDoubles": false,
  "startsAt": "2023-05-01T10:00:00Z",
  "endsAt": "2023-05-01T11:00:00Z"
}
```
### Response
`600.0` - price of the reservation

## Get all reservations
### Request
`GET /api/reservations/all`
### Response
```json
[
  {
    "id": 1,
    "doubles": false,
    "createdAt": "2023-05-02T23:24:52.779+00:00",
    "startsAt": "2023-05-01T10:00:00.000+00:00",
    "endsAt": "2023-05-01T11:00:00.000+00:00",
    "price": 600.0,
    "customer": {
      "id": 1,
      "phoneNumber": "123456789",
      "name": "John Doe"
    },
    "court": {
      "id": 2,
      "name": "Court 2",
      "surface": {
        "id": 1,
        "name": "Clay",
        "minutePrice": 10
      }
    }
  }
]
```

## Get reservation by id
### Request
`GET /api/reservations/id/{id}`
### Response
```json
{
  "id": 1,
  "doubles": false,
  "createdAt": "2023-05-02T23:24:52.779+00:00",
  "startsAt": "2023-05-01T10:00:00.000+00:00",
  "endsAt": "2023-05-01T11:00:00.000+00:00",
  "price": 600.0,
  "customer": {
    "id": 1,
    "phoneNumber": "123456789",
    "name": "John Doe"
  },
  "court": {
    "id": 2,
    "name": "Court 2",
    "surface": {
      "id": 1,
      "name": "Clay",
      "minutePrice": 10
    }
  }
}
```

## Get reservations by court id sorted by creation time
### Request
`GET /api/reservations/court/{id}`
### Response
```json
[
  {
    "id": 1,
    "doubles": false,
    "createdAt": "2023-05-02T23:24:52.779+00:00",
    "startsAt": "2023-05-01T10:00:00.000+00:00",
    "endsAt": "2023-05-01T11:00:00.000+00:00",
    "price": 600.0,
    "customer": {
      "id": 1,
      "phoneNumber": "123456789",
      "name": "John Doe"
    },
    "court": {
      "id": 2,
      "name": "Court 2",
      "surface": {
        "id": 1,
        "name": "Clay",
        "minutePrice": 10
      }
    }
  },
  {
    "id": 2,
    "doubles": true,
    "createdAt": "2023-05-02T23:30:37.837+00:00",
    "startsAt": "2023-05-01T13:00:00.000+00:00",
    "endsAt": "2023-05-01T14:00:00.000+00:00",
    "price": 900.0,
    "customer": {
      "id": 2,
      "phoneNumber": "987654321",
      "name": "Jane Doe"
    },
    "court": {
      "id": 2,
      "name": "Court 2",
      "surface": {
        "id": 1,
        "name": "Clay",
        "minutePrice": 10
      }
    }
  }
]
```

## Get all reservations for customer by phone number
### Request
`GET /api/reservations/phone/all/{phoneNumber}`
### Response
```json
[
  {
    "id": 1,
    "doubles": true,
    "createdAt": "2023-05-02T23:33:05.806+00:00",
    "startsAt": "2025-05-01T13:00:00.000+00:00",
    "endsAt": "2025-05-01T14:00:00.000+00:00",
    "price": 900.0,
    "customer": {
      "id": 1,
      "phoneNumber": "123456789",
      "name": "John Doe"
    },
    "court": {
      "id": 2,
      "name": "Court 2",
      "surface": {
        "id": 1,
        "name": "Clay",
        "minutePrice": 10
      }
    }
  },
  {
    "id": 2,
    "doubles": false,
    "createdAt": "2023-05-02T23:33:22.098+00:00",
    "startsAt": "2015-05-01T13:00:00.000+00:00",
    "endsAt": "2015-05-01T14:00:00.000+00:00",
    "price": 600.0,
    "customer": {
      "id": 1,
      "phoneNumber": "123456789",
      "name": "John Doe"
    },
    "court": {
      "id": 2,
      "name": "Court 2",
      "surface": {
        "id": 1,
        "name": "Clay",
        "minutePrice": 10
      }
    }
  }
]
```

## Get future reservations for customer by phone number
### Request
`GET /api/reservations/phone/future/{phoneNumber}`
### Response
```json
[
  {
    "id": 1,
    "doubles": true,
    "createdAt": "2023-05-02T23:33:05.806+00:00",
    "startsAt": "2025-05-01T13:00:00.000+00:00",
    "endsAt": "2025-05-01T14:00:00.000+00:00",
    "price": 900.0,
    "customer": {
      "id": 1,
      "phoneNumber": "123456789",
      "name": "John Doe"
    },
    "court": {
      "id": 2,
      "name": "Court 2",
      "surface": {
        "id": 1,
        "name": "Clay",
        "minutePrice": 10
      }
    }
  }
]
```

## Update reservation
### Request
`PUT /api/reservations/id/{id}`
### Body
```json
{
  "courtId": 2,
  "customerPhoneNumber": "123456789",
  "customerName": "John Doe",
  "isDoubles": true,
  "startsAt": "2023-05-01T10:00:00Z",
  "endsAt": "2023-05-01T12:00:00Z"
}
```
### Response
```json
{
  "id": 1,
  "doubles": true,
  "createdAt": "2023-05-02T23:43:51.754+00:00",
  "startsAt": "2023-05-01T10:00:00.000+00:00",
  "endsAt": "2023-05-01T12:00:00.000+00:00",
  "price": 1800.0,
  "customer": {
    "id": 1,
    "phoneNumber": "123456789",
    "name": "John Doe"
  },
  "court": {
    "id": 2,
    "name": "Court 2",
    "surface": {
      "id": 1,
      "name": "Clay",
      "minutePrice": 10
    }
  }
}
```

## Patch reservation
### Request
`PATCH /api/reservations/id/{id}`
### Body
```json
{
  "courtId": 4
}
```
### Response
```json
{
  "id": 1,
  "doubles": true,
  "createdAt": "2023-05-02T23:43:51.754+00:00",
  "startsAt": "2023-05-01T10:00:00.000+00:00",
  "endsAt": "2023-05-01T12:00:00.000+00:00",
  "price": 2700.0,
  "customer": {
    "id": 1,
    "phoneNumber": "123456789",
    "name": "John Doe"
  },
  "court": {
    "id": 4,
    "name": "Court 4",
    "surface": {
      "id": 2,
      "name": "Grass",
      "minutePrice": 15
    }
  }
}
```

## Delete reservation
### Request
`DELETE /api/reservations/id/{id}`