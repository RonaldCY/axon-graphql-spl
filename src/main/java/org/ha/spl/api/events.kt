package org.ha.spl.api

// Hospital
data class HospitalCreatedEvent(var hospCode: String)
data class WardAddedEvent(var hospCode: String, var wardCode: String)
data class WardCreatedEvent(var hospCode: String, var wardCode: String)

// Ward
data class BedAddedEvent(var wardId: String, var bedNum: Int)
data class PatientCheckedInEvent(var wardId: String, var bedNum: Int, var hkid: String, var name: String)

// Patient
data class PatientCreatedEvent(var hkid: String, var name: String)
data class WardCheckedInEvent(var hkid: String, var name: String, var hospCode: String, var wardCode: String, var bedNum: Int)
data class CheckInWardCancelledEvent(var hkid: String)
