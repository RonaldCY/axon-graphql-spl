# axon-graphql-spl

## start supporting servers

```shell script
docker run -d --name some-axon-server -p 8024:8024 -p 8124:8124 axoniq/axonserver   
docker run -d -e "MYSQL_ROOT_PASSWORD=Admin12345" -e "MYSQL_USER=usr" -e "MYSQL_PASSWORD=User12345" -e "MYSQL_DATABASE=development" -e "MYSQL_AUTHENTICATION_PLUGIN=mysql_native_password" -p 3306:3306 --name some-mysql bitnami/mysql:8.0.19
```

## stop supporting servers

```shell script
docker stop some-axon-server; docker rm some-axon-server
docker stop some-mysql; docker rm some-mysql
```

## Testing

### Command

```shell script
curl -X POST 'http://localhost:8080/hospital' -H 'Content-Type: application/json' \
  -d '{ "hospCode": "VH" }'

curl -X POST 'http://localhost:8080/hospital/VH' -H 'Content-Type: application/json' \
  -d '{ "wardCode": "A1" }'

curl -X POST 'http://localhost:8080/hospital/VH/ward/A1' -H 'Content-Type: application/json' \
  -d '{ "bedNum": 12 }'

curl -X POST 'http://localhost:8080/patient' -H 'Content-Type: application/json' \
  -d '{ "hkid": "A1234563", "name": "Mary" }'

curl -X POST 'http://localhost:8080/hospital/VH/ward/A1/checkIn' -H 'Content-Type: application/json' \
  -d '{ "hkid": "A1234563", "bedNum": 12 }'
```

```Graph QL
mutation {
  createHospital(input: { hospCode: "VH" })
}

mutation {
  addWard(hospCode: "VH", input: { wardCode: "A1" })
}

mutation {
  addBed(hospCode: "VH", wardCode: "A1", input: { bedNum: 12 })
}

mutation {
  createPatient(input: { hkid: "A1234563", name: "Mary" })
}

mutation {
  checkIn(
    hospCode: "VH"
    wardCode: "A1"
    input: { hkid: "A1234563", bedNum: 12 }
  )
}
```

### Query

```shell script
curl -X GET 'http://localhost:8080/hospital/VH'
```