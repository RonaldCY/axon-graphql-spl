package org.ha.spl.api

class ListHospitalQuery
data class FindHospitalQuery(var hospCode: String)
data class FindWardQuery(var hospCode: String, var wardCode: String)
