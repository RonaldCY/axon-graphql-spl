package org.ha.spl.api

import lombok.Value

class ListHospitalQuery
data class FindHospitalQuery(var hospCode: String)
data class FindWardQuery(var hospCode: String, var wardCode: String)

@Value
class NotificationQuery {}
